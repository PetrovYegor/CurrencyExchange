package com.github.petrovyegor.currencyexchange.controller.filter;

import jakarta.servlet.*;

import java.io.IOException;

public class ContentTypeAndEncodingFilter implements Filter {
    private static final String ENCODING = "UTF-8";
    private static final String RESPONSE_CONTENT_TYPE = "application/json";

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding(ENCODING);
        servletResponse.setCharacterEncoding(ENCODING);
        servletResponse.setContentType(RESPONSE_CONTENT_TYPE);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}

