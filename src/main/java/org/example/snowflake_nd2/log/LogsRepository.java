package org.example.snowflake_nd2.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogsRepository extends JpaRepository<LogsRaw, Long> {
    @Query(value = """
        SELECT\s
            REGEXP_SUBSTR(log_entry, '\\\\[([0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2})\\\\]') AS log_timestamp,
            REGEXP_SUBSTR(log_entry, 'INFO|WARNING|ERROR') AS log_level,
            REGEXP_REPLACE(log_entry, '\\\\[([0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2})\\\\] (INFO|WARNING|ERROR): ', '') AS log_message
        FROM logs_raw
   \s""", nativeQuery = true)
    List<Object[]> getStructuredLogs();
}
