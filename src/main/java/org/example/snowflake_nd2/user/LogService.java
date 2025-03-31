package org.example.snowflake_nd2.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void processLogFile(MultipartFile file) throws IOException {
        List<String> logLines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                logLines.add(line);
            }
        }
        insertLogsToSnowflake(logLines);
    }

    private void insertLogsToSnowflake(List<String> logLines) {
        String sql = "INSERT INTO LOGS_RAW (LOG_ENTRY) VALUES (?)";
        List<Object[]> batchArgs = logLines.stream()
                .map(line -> new Object[]{line})
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate(sql, batchArgs);
    }
}
