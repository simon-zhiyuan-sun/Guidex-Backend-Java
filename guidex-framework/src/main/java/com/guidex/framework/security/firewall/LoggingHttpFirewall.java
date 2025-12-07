package com.guidex.framework.security.firewall;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.firewall.FirewalledRequest;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import javax.servlet.http.HttpServletRequest;

/**
 * @author kled2
 * @date 2025/5/22
 */
public class LoggingHttpFirewall extends StrictHttpFirewall {
    public static final Logger log = LoggerFactory.getLogger(LoggingHttpFirewall.class);
    @Override
    public FirewalledRequest getFirewalledRequest(HttpServletRequest request) throws RequestRejectedException {
        try {
            return super.getFirewalledRequest(request);
        } catch (RequestRejectedException ex) {
            String uri = request.getRequestURI();
            String query = request.getQueryString();
            String fullUrl = uri + (query != null ? "?" + query : "");
            String ip = request.getRemoteAddr();

            log.error("拒绝访问：IP={} URL={} 原因={}", ip, fullUrl, ex.getMessage());
            throw ex;
        }
    }
}
