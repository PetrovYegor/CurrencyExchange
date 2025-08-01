package com.github.petrovyegor.currencyexchange.controller.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.petrovyegor.currencyexchange.exception.*;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ErrorHandlerFilter implements Filter {
    private static final String FATAL_ERROR_MESSAGE = "Fatal error";

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
        } catch (AlreadyExistsException e) {
            sendError(e.getCode(), e.getMessage(), response);
        } catch (RestErrorException e) {
            sendError(e.getCode(), e.getMessage(), response);
        } catch (DBException e) {
            sendError(e.getCode(), e.getMessage(), response);
        } catch (Exception e) {
            sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, FATAL_ERROR_MESSAGE, response);
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
