package io.github.tim12135.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.tim12135.demo.common.R;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 * http response json 工具类
 *
 * @author tim12135
 * @since 2023/5/10
 */
public class ResponseUtils {

    public static void out(HttpServletResponse response, R<?> r) {
        ObjectMapper mapper = new ObjectMapper();
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8)) {
            mapper.writeValue(outputStreamWriter, r);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
