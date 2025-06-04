import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

public class LogGenerator {
    private static final String[] LEVELS = {"INFO", "WARN", "DEBUG", "ERROR"};
    private static final String[] THREADS = {
        "main", "worker-1", "worker-2", "http-nio-8080-exec-1", "scheduler"
    };
    private static final String[] CLASSES = {
        "com.example.api.UserController",
        "com.example.service.DataService",
        "com.example.config.ServerConfig",
        "com.example.task.SchedulerTask",
        "com.example.db.DatabaseConnector",
        "com.example.auth.AuthService",
        "com.example.email.EmailService"
    };
    private static final String[] USER_IDS = {
        "user1", "user2", "admin", "guest", "service", "api-bot", "support"
    };

    // Standard error code mapping for different scenarios
    private static final String[][] ERROR_MESSAGES_AND_CODES = {
        {"User login failed", "AUTH_401"},
        {"Connection timeout", "NET_408"},
        {"Service unavailable", "SRV_503"},
        {"Null pointer exception", "APP_500"},
        {"Unauthorized access attempt", "AUTH_403"},
        {"File not found", "FS_404"},
        {"Failed to send email", "MAIL_550"},
        {"Payment transaction failed", "PAY_402"},
        {"API limit exceeded", "API_429"},
        {"Data validation error", "VAL_422"},
        {"Invalid input parameter", "VAL_400"},
        {"Duplicate record detected", "DB_409"},
        {"Configuration file missing", "CFG_404"},
        {"Resource temporarily locked", "RES_423"},
        {"Background job failed", "JOB_520"}
    };

    private static final String[] GENERIC_MESSAGES = {
        "User login succeeded",
        "Fetching data from API",
        "Data saved to database",
        "Server started on port 8080",
        "User registration completed",
        "User password changed",
        "Session expired",
        "Database connection established",
        "Cache miss for session",
        "Payment transaction successful",
        "Order shipped",
        "Order cancelled",
        "Email verification link sent",
        "Configuration file loaded",
        "Background job started"
    };

    private static final int NUM_CORR_IDS = 30;
    private static final String[] CORR_IDS = new String[NUM_CORR_IDS];
    static {
        for (int i = 0; i < NUM_CORR_IDS; i++) {
            CORR_IDS[i] = UUID.randomUUID().toString();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        FileWriter fw = new FileWriter("generated_logs_detailed.txt");
        Random rand = new Random();

        for (int i = 0; i < 10000; i++) {
            String timestamp = LocalDateTime.now().toString();
            String correlationId = CORR_IDS[rand.nextInt(NUM_CORR_IDS)];
            String level = LEVELS[rand.nextInt(LEVELS.length)];
            String thread = THREADS[rand.nextInt(THREADS.length)];
            String clazz = CLASSES[rand.nextInt(CLASSES.length)];
            String userId = USER_IDS[rand.nextInt(USER_IDS.length)];

            String message;
            // 30% chance for error messages, 70% for generic
            if (rand.nextDouble() < 0.3) {
                int idx = rand.nextInt(ERROR_MESSAGES_AND_CODES.length);
                message = ERROR_MESSAGES_AND_CODES[idx][0] + " [code:" + ERROR_MESSAGES_AND_CODES[idx][1] + "]";
            } else {
                message = GENERIC_MESSAGES[rand.nextInt(GENERIC_MESSAGES.length)];
            }

            String log = String.format(
                "%s [CorrelationID:%s] [%s] [%s] [%s] [user:%s] %s%n",
                timestamp, correlationId, level, thread, clazz, userId, message
            );

            fw.write(log);
            Thread.sleep(rand.nextInt(50)); // simulate some delay
        }

        fw.close();
        System.out.println("Log generation complete. Check generated_logs_detailed.txt");
    }
}
