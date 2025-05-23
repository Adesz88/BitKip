package io.beanvortex.bitkip.repo;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import io.beanvortex.bitkip.config.AppConfigs;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ch.qos.logback.classic.Logger;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelperTest {
    static class TestAppender extends AppenderBase<ILoggingEvent> {
        private final List<ILoggingEvent> logs = new ArrayList<>();

        @Override
        protected void append(ILoggingEvent eventObject) {
            logs.add(eventObject);
        }

        public List<ILoggingEvent> getLogs() {
            return logs;
        }

        public void clear() {
            logs.clear();
        }
    }

    private Logger bitkipLogger;
    private TestAppender appender;

    @BeforeEach
    void setupLogger() {
        AppConfigs.log = LoggerFactory.getLogger("BitKip");
        bitkipLogger = (Logger) AppConfigs.log;

        // Attach appender
        appender = new TestAppender();
        appender.setContext(bitkipLogger.getLoggerContext());
        appender.start();
        bitkipLogger.addAppender(appender);
    }

    @AfterEach
    void cleanupLogger() {
        if (bitkipLogger != null && appender != null) {
            bitkipLogger.detachAppender(appender);
        }
    }

    @Test
    public void testRunSQLWithoutLog() {
        AppConfigs.log = LoggerFactory.getLogger("BitKip");

        DatabaseHelper.runSQL("", true);

        boolean isEmpty = appender.getLogs().isEmpty();
        Assertions.assertTrue(isEmpty);
    }

    @Test
    public void testRunSQLWithLog() {
        AppConfigs.log = LoggerFactory.getLogger("BitKip");

        DatabaseHelper.runSQL("", false);

        boolean isEmpty = appender.getLogs().isEmpty();
        Assertions.assertFalse(isEmpty);
    }

    @Test
    public void testUpdateColsException() {
        String[] columns = {""};
        String[] values = {"", ""};
        Exception exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> DatabaseHelper.updateCols(columns, values, "", 1)
        );

        String expectedMessage = "columns and values do not match by length";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Assertions.assertDoesNotThrow(
                () -> DatabaseHelper.updateCols(columns, columns, "", 1)
        );
    }

    @Test
    public void testUpdateCols() {
        String[] columns = {"id", "name", "admin"};
        String[] values = {"1", "a", "NULL"};
        MockedStatic<DatabaseHelper> mocked = Mockito.mockStatic(DatabaseHelper.class, Mockito.CALLS_REAL_METHODS);

        DatabaseHelper.updateCols(columns, values, "user", 1);
        mocked.verify(() -> DatabaseHelper.runSQL("UPDATE user SET id=1,name=\"a\",admin=NULL WHERE id=1;", false));

        mocked.close();
    }

    @Test
    public void testUpdateCol() {
        MockedStatic<DatabaseHelper> mocked = Mockito.mockStatic(DatabaseHelper.class, Mockito.CALLS_REAL_METHODS);
        DatabaseHelper.updateCol("id", "1", "user", 1);
        mocked.verify(() -> DatabaseHelper.runSQL("UPDATE user SET id=1 WHERE id=1;\n", false));

        DatabaseHelper.updateCol("id", "a", "user", 1);
        mocked.verify(() -> DatabaseHelper.runSQL("UPDATE user SET id=1 WHERE id=1;\n", false));

        DatabaseHelper.updateCol("id", "NULL", "user", 1);
        mocked.verify(() -> DatabaseHelper.runSQL("UPDATE user SET id=1 WHERE id=1;\n", false));

        mocked.close();
    }
}
