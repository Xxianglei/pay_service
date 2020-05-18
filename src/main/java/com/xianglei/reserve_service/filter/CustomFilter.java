package com.xianglei.reserve_service.filter;

import com.xianglei.reserve_service.common.http.RequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
@WebFilter(urlPatterns = {"/subOrder/lockOrder"},filterName = "customFilter")
public class CustomFilter implements Filter {
    private Logger log = LoggerFactory.getLogger(CustomFilter.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info(">>>> customFilter init <<<<");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        log.info(">>>> customFilter doFilter start <<<<");
        RequestWrapper requestWapper = null;
        if (servletRequest instanceof HttpServletRequest) {
            requestWapper = new RequestWrapper((HttpServletRequest) servletRequest);
        }

        if (requestWapper != null) {
            filterChain.doFilter(requestWapper,servletResponse);
        } else {
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }

    @Override
    public void destroy() {
        log.info(">>>> customFilter destroy <<<<");
    }
}
