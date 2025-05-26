package com.github.petrovyegor.currencyexchange.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class ResponsePreparer {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void prepareResponse(HttpServletResponse response, int status){
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }

    public <T> void writeValue(HttpServletResponse response, List<T> value) throws IOException {
        objectMapper.writeValue(response.getWriter(), value);
    }
    public <T> void writeValue(HttpServletResponse response, T value) throws IOException {
        objectMapper.writeValue(response.getWriter(), value);
    }
}
