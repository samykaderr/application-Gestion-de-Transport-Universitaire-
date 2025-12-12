package DATA;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerUtil {
    private static final Logger logger = Logger.getLogger(LoggerUtil.class.getName());

    static {
        try {
            FileHandler fileHandler = new FileHandler("app_transport_errors.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to initialize logger FileHandler", e);
        }
    }

    public static void log(Level level, String msg, Throwable thrown) {
        logger.log(level, msg, thrown);
    }

    public static Logger getLogger() {
        return logger;
    }
}

