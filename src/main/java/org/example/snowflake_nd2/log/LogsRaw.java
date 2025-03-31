package org.example.snowflake_nd2.log;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "logs_raw")
public class LogsRaw {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "log_entry")
    private String logEntry;

}
