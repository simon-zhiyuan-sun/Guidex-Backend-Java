package com.guidex.framework.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author kled2
 * @date 2025/5/13
 */
@Configuration
public class AnalysisExecutorConfig {
    @Bean(name = "analysisExecutor")
    public ExecutorService analysisExecutor() {
        return new ThreadPoolExecutor(
                1, 1, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(20),
                new ThreadFactoryBuilder().setNameFormat("video-analysis-%d").build(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
