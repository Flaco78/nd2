package org.example.snowflake_nd2.log;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/logs")
public class LogsController {

    private final LogsRepository logsRepository;

    public LogsController(LogsRepository logsRepository) {
        this.logsRepository = logsRepository;
    }

    @GetMapping("/structured")
    public List<Map<String, Object>> getStructuredLogs() {
        List<Object[]> rawData = logsRepository.getStructuredLogs();
        List<Map<String, Object>> structuredLogs = new ArrayList<>();

        for (Object[] row : rawData) {
            Map<String, Object> log = new HashMap<>();
            log.put("log_timestamp", row[0] != null ? row[0].toString() : "Unknown timestamp");
            log.put("log_level", row[1] != null ? row[1] : "Unknown");
            log.put("log_message", row[2] != null ? row[2] : "No message");
            structuredLogs.add(log);
        }


        return structuredLogs;
    }
}
