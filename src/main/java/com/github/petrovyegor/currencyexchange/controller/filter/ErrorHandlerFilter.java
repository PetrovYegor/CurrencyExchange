package com.github.petrovyegor.currencyexchange.controller.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.petrovyegor.currencyexchange.exception.DBException;
import com.github.petrovyegor.currencyexchange.exception.InvalidParamException;
import com.github.petrovyegor.currencyexchange.exception.InvalidRequestException;
import com.github.petrovyegor.currencyexchange.exception.RestErrorException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ErrorHandlerFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (InvalidParamException e) {
            sendError(e.getCode(), e.getMessage(), response);
        } catch (InvalidRequestException e) {
            sendError(e.getCode(), e.getMessage(), response);
        } catch (RestErrorException e) {
            sendError(e.getCode(), e.getMessage(), response);
        } catch (DBException e) {
            sendError(e.getCode(), e.getMessage(), response);
        } catch (Exception e) {
            sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Fatal error", response);
        }
    }

    private void sendError(int code, String message, HttpServletResponse response) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            response.setStatus(code);
            response.getWriter().print(objectMapper.createObjectNode().put("message", message));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
    }
}
