package org.example.snowflake_nd2.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LogService logService;

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public Users createUser(@RequestBody Users user) {
        return userService.saveUser(user);
    }

    @GetMapping("/all")
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Optional<Users> getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping(value = "/snapshot/{timestamp}")
    public List<Users> getSnapshot(@PathVariable String timestamp) {
        LocalDateTime localDateTime = LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME);
        ZonedDateTime vilniusTime = localDateTime.atZone(ZoneId.of("Europe/Vilnius"));
        ZonedDateTime snowflakeTime = vilniusTime.withZoneSameInstant(ZoneId.of("UTC-0"));
        String formattedTimestamp = snowflakeTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String sql = "SELECT * FROM USERS AT (TIMESTAMP => ?)";

        return jdbcTemplate.query(sql, new Object[]{formattedTimestamp}, (rs, rowNum) -> {
            Users user = new Users();
            user.setId((rs.getLong("ID")));
            user.setName(rs.getString("NAME"));
            user.setEmail(rs.getString("EMAIL"));
            user.setAge(rs.getInt("AGE"));
            return user;
        });
    }


    @PostMapping("/upload")
    public ResponseEntity<String> uploadLogFile(@RequestParam("file") MultipartFile file) {
        try {
            logService.processLogFile(file);
            return ResponseEntity.ok("Log file uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing log file: " + e.getMessage());
        }
    }
}
