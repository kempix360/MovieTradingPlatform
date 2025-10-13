package com.app.movietradingplatform.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void writeJson(HttpServletResponse resp, Object obj) throws IOException {
        mapper.writeValue(resp.getOutputStream(), obj);
    }
}
