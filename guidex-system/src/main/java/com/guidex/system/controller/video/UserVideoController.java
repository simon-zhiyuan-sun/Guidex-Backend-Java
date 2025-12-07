package com.guidex.system.controller.video;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guidex.common.annotation.Log;
import com.guidex.common.core.controller.BaseController;
import com.guidex.common.core.domain.AjaxResult;
import com.guidex.common.core.domain.PythonAnalyzeResponse;
import com.guidex.common.core.page.TableDataInfo;
import com.guidex.common.core.service.IAvatarStorageService;
import com.guidex.common.core.service.IVideoStorageService;
import com.guidex.common.enums.*;
import com.guidex.common.utils.SecurityUtils;
import com.guidex.common.utils.poi.ExcelUtil;
import com.guidex.system.domain.issues.UserAnalysisIssues;
import com.guidex.system.domain.video.UserVideo;
import com.guidex.system.domain.video.UserVideoDto;
import com.guidex.system.domain.video.UserVideoResults;
import com.guidex.system.domain.video.VideoAnalysisResult;
import com.guidex.system.service.IUserAnalysisIssuesService;
import com.guidex.system.service.IUserVideoResultsService;
import com.guidex.system.service.IUserVideoService;
import com.guidex.system.utils.PythonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 【请填写功能名称】Controller
 *
 * @author guidex
 * @date 2025-04-19
 */
@RestController
@RequestMapping("/system/video")
public class UserVideoController extends BaseController {
    @Autowired
    private IUserAnalysisIssuesService userAnalysisIssuesService;
    @Autowired
    private IUserVideoService userVideoService;
    @Autowired
    private IVideoStorageService videoStorage;
    @Autowired
    private IAvatarStorageService avatarStorage;
    @Autowired
    private IUserVideoResultsService videoResultsService;
    @Autowired
    @Qualifier("analysisExecutor")
    private ExecutorService analysisExecutor;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private PythonClient pythonClient;
    public static final Logger log = LoggerFactory.getLogger(UserVideoController.class);

    @PostMapping("/upload")
    public AjaxResult uploadVideo(
            @RequestParam("videoFile") MultipartFile videoFile
    ) throws IOException {
        Long userId = SecurityUtils.getUserId();

        // 上传到 GCS，拿到 objectName
        String objectName;
        try {
            objectName = videoStorage.saveVideo(videoFile, userId);
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        }

        // 拼 URL 并持久化
        String videoUrl = videoStorage.toUrl(objectName);
        UserVideo newVideo = new UserVideo();
        newVideo.setVideoUrl(videoUrl);
        newVideo.setUserId(userId);
        newVideo.setUploadTime(new Date());
        newVideo.setFileName(videoFile.getOriginalFilename());
        newVideo.setStatus((long) VideoAnalysisStatusEnum.UN_ANALYZED.getCode());
        // newVideo.setDuration();
        // newVideo.setCoverUrl();
        // newVideo.setFileName();
        userVideoService.insertUserVideo(newVideo);

        Map<String, Object> data = new HashMap<>();
        data.put("videoUrl", videoUrl);
        data.put("videoId", newVideo.getId());

        return AjaxResult.success("上传成功", data);
    }

    // todo 考虑限制上传视频次数 每日三次

    /**
     * 分析视频接口 做了三件事：上传视频到云存储，调用python模块分析视频，插入分析结果到数据库
     *
     * @param videoFile
     * @return
     */
    @PostMapping("/analyze")
    public AjaxResult analyzeVideo(@RequestParam("videoFile") MultipartFile videoFile,
                                   @RequestParam("category") int category,
                                   @RequestParam("standard") int standard,
                                   @RequestParam("type") int type) throws IOException {
        // check params
        if (!(category == 0 || category == 1)) {
            category = 0;
        }
        if (!(standard == 0 || standard == 1 || standard == 2 || standard == 3)) {
            standard = 0;
        }
        if (!(type == 0 || type == 1)) {
            type = 0;
        }

        Long userId = SecurityUtils.getUserId();
        log.info("用户 {} 上传了一个视频，开始处理...", userId);

        String objectName;
        try {
            objectName = videoStorage.saveVideo(videoFile, userId);
        } catch (IllegalArgumentException e) {
            log.error("视频上传失败: {}", e.getMessage());
            return error(e.getMessage());
        }

        String videoUrl = videoStorage.toUrl(objectName);
        log.info("视频已上传至 GCS: {}, URL: {}", objectName, videoUrl);

        UserVideo newVideo = new UserVideo();
        newVideo.setVideoUrl(videoUrl);
        newVideo.setUserId(userId);
        newVideo.setUploadTime(new Date());
        newVideo.setFileName(videoFile.getOriginalFilename());
        newVideo.setStatus((long) VideoAnalysisStatusEnum.ANALYZING.getCode());
        userVideoService.insertUserVideo(newVideo);
        log.info("视频信息已插入数据库，videoId={}", newVideo.getId());

        int finalCategory = category;
        int finalStandard = standard;
        int finalType = type;

        // 先生成记录
        UserVideoResults newVideoResult = new UserVideoResults();
        newVideoResult.setCreatedTime(new Date());
        newVideoResult.setVideoId(newVideo.getId());
        newVideoResult.setCategory((long) finalCategory);
        newVideoResult.setStandard((long) finalStandard);
        newVideoResult.setType((long) finalType);
        videoResultsService.insertUserVideoResults(newVideoResult);

        // 在主线程中立即保存 MultipartFile
        String tempDir = System.getProperty("java.io.tmpdir");
        File tempVideoFile = new File(tempDir, UUID.randomUUID() + ".mp4");
        videoFile.transferTo(tempVideoFile);


        analysisExecutor.submit(() -> {
            log.info("开始分析视频：videoId={}, url={}", newVideo.getId(), videoUrl);
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String result = pythonClient.analyze(videoUrl, CategoryEnum.getDescByCode(finalCategory), StandardEnum.getDescByCode(finalStandard), TypeEnum.getDescByCode(finalType));
                log.info("Python 模块返回结果: {}", result);

                PythonAnalyzeResponse resp = objectMapper.readValue(result, PythonAnalyzeResponse.class);
                if (Objects.equals(resp.getStatus(), "success")) {
                    String analysisJson = resp.getAnalysis_result();
                    if (analysisJson.startsWith("```json")) {
                        analysisJson = analysisJson.replace("```json", "").replace("```", "").trim();
                    }
                    VideoAnalysisResult analysisResult = objectMapper.readValue(analysisJson, VideoAnalysisResult.class);
                    log.info("解析成功: issue_count={}", analysisResult.getIssue_count());

                    for (UserAnalysisIssues issue : analysisResult.getIssues()) {
                        issue.setResultId(newVideoResult.getId());
                        setCoverUrl(issue, tempVideoFile);
                        userAnalysisIssuesService.insertUserAnalysisIssues(issue);
                    }
                    tempVideoFile.delete();

                    Map<String, Object> detailMap = new HashMap<>();
                    detailMap.put("issues", analysisResult.getIssues());

                    String resultDetailJson = objectMapper.writeValueAsString(detailMap);
                    log.info("分析细节 JSON: {}", resultDetailJson);

                    newVideoResult.setResultDetail(resultDetailJson);
                    newVideoResult.setIssueCount((long) analysisResult.getIssue_count());
                    newVideoResult.setCreatedTime(new Date());
                    newVideoResult.setIsDeleted(0L);
                    newVideoResult.setVideoId(newVideo.getId());
                    newVideoResult.setSkiTitle(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")));

                    // 判读视频分析是否被取消
                    UserVideo currentVideo = userVideoService.selectUserVideoById(newVideo.getId());
                    if (currentVideo.getStatus() == VideoAnalysisStatusEnum.ANALYZE_CANCELED.getCode()) {
                        log.info("分析被取消，结果不入库，videoId={}", newVideo.getId());
                        return;
                    }

                    videoResultsService.updateUserVideoResults(newVideoResult);
                    userVideoService.markAsSuccess(newVideo.getId());

                    log.info("分析结果写入成功，视频标记为成功，videoId={}", newVideo.getId());
                } else {
                    log.error("Python 返回非 success 状态: {}", resp.getStatus());
                    userVideoService.markAsFailed(newVideo.getId());
                }
            } catch (Exception e) {
                log.error("分析任务失败，videoId={}，异常信息: {}", newVideo.getId(), e.getMessage(), e);
                userVideoService.markAsFailed(newVideo.getId());
            }
        });

        Map<String, Object> data = new HashMap<>();
        data.put("videoId", newVideo.getId());
        data.put("resultId", newVideoResult.getId());
        return AjaxResult.success("Video is analyzing, please check it later.", data);
    }

    private void setCoverUrl(UserAnalysisIssues issue, File videoFile) {
        String videoPath = null;
        String imagePath = null;
        try {
            double timeInSeconds = issue.getTime();
            String tempDir = System.getProperty("java.io.tmpdir");
            videoPath = tempDir + "/temp_" + UUID.randomUUID() + ".mp4";
            imagePath = tempDir + "/frame_" + UUID.randomUUID() + ".gif";

            // Step 1: 写入临时视频文件
            videoPath = videoFile.getAbsolutePath();

            // Step 2: 使用 ffmpeg 抽帧生成gif
            double startTime = Math.max(0, timeInSeconds - 1); // 防止负时间
            String[] cmd = {
                    "ffmpeg", "-ss", String.valueOf(startTime),
                    "-t", "2", // 持续2秒（前后1秒）
                    "-i", videoPath,
                    "-vf", "fps=10,scale=240:-1:flags=lanczos", // 控制帧率和宽度（可调）
                    "-y", imagePath
            };
            Process process = new ProcessBuilder(cmd).redirectErrorStream(true).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.toLowerCase().contains("error") || line.toLowerCase().contains("invalid")) {
                    log.error("[ffmpeg] {}", line);
                } else if (line.toLowerCase().contains("frame=")) {
                    log.debug("[ffmpeg] {}", line); // 只打印帧处理情况
                }
            }

            process.waitFor(10, TimeUnit.SECONDS);

            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                log.error("封面生成失败：图像文件未创建，issueTime={}, videoFile={}", timeInSeconds, videoFile.getName());
                return;
            }

            // Step 3: 上传封面图
            String imageUrl = avatarStorage.saveImage(imageFile);
            if (imageUrl == null || imageUrl.isEmpty()) {
                log.error("封面图上传失败：返回空 objectName，issueTime={}, imageFile={}", timeInSeconds, imagePath);
                return;
            }

            // Step 4: 设置封面地址
            issue.setCoverUrl(imageUrl);

        } catch (Exception e) {
            log.error("生成封面失败：异常信息：{}", e.getMessage(), e);
        } finally {
            // Step 5: 清理临时文件
            new File(imagePath).delete();
        }
    }

    // /**
    //  * 根据用户id查询正在分析中的视频
    //  */
    // @GetMapping("/analyzing")
    // public AjaxResult analyzing() {
    //     Long userId = getUserId();
    //     List<UserVideo> analyzingVideos = userVideoService.selectAnalyzingVideos(userId);
    //     return AjaxResult.success(analyzingVideos);
    // }


    /**
     * 查询【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('system:video:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserVideo userVideo) {
        startPage();
        List<UserVideo> list = userVideoService.selectUserVideoList(userVideo);
        return getDataTable(list);
    }

    /**
     * 导出【请填写功能名称】列表
     */
    @PreAuthorize("@ss.hasPermi('system:video:export')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserVideo userVideo) {
        List<UserVideo> list = userVideoService.selectUserVideoList(userVideo);
        ExcelUtil<UserVideo> util = new ExcelUtil<UserVideo>(UserVideo.class);
        util.exportExcel(response, list, "【请填写功能名称】数据");
    }

    /**
     * 获取【请填写功能名称】详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        UserVideo userVideo = userVideoService.selectUserVideoById(id);
        UserVideoDto dto = new ObjectMapper().convertValue(userVideo, UserVideoDto.class);

        String redisKey = "video:progress:" + id;

        Long statusCode = userVideo.getStatus(); // 状态从 userVideo 中获取
        int progress = 0;

        if (statusCode == VideoAnalysisStatusEnum.ANALYZE_SUCCESS.getCode() ||
                statusCode == VideoAnalysisStatusEnum.ANALYZE_FAILED.getCode()) {
            // 如果分析成功或失败，直接返回 100%
            progress = 100;
            // 可以顺带删除 Redis 中这个 key
            redisTemplate.delete(redisKey);
        } else if (statusCode == VideoAnalysisStatusEnum.ANALYZING.getCode()) {
            // 否则走伪进度逻辑
            Integer cachedProgress = (Integer) redisTemplate.opsForValue().get(redisKey);
            if (cachedProgress == null) {
                progress = 10; // 初始值
            } else {
                progress = cachedProgress + new Random().nextInt(10) + 5; // 增加 5~14
                progress = Math.min(progress, 99); // 不超过99
            }
            redisTemplate.opsForValue().set(redisKey, progress, 10, TimeUnit.MINUTES);
        }

        dto.setProgress(progress);

        return success(dto);
    }

    /**
     * 新增【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:video:add')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserVideo userVideo) {
        return toAjax(userVideoService.insertUserVideo(userVideo));
    }

    /**
     * 修改【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:video:edit')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserVideo userVideo) {
        return toAjax(userVideoService.updateUserVideo(userVideo));
    }

    /**
     * 删除【请填写功能名称】
     */
    @PreAuthorize("@ss.hasPermi('system:video:remove')")
    @Log(title = "【请填写功能名称】", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(userVideoService.deleteUserVideoByIds(ids));
    }

    @GetMapping("/{id}/markAsSuccess")
    public AjaxResult markAsSuccess(@PathVariable Long id) {
        userVideoService.markAsSuccess(id);
        return success();
    }

    @PutMapping("/cancel/{id}")
    public AjaxResult cancelAnalyze(@PathVariable Long id) {
        return userVideoService.markAsCanceled(id) == 0 ? success("This analyze process has already been canceled.") : success("Cancel analyze process successfully.");
    }
}
