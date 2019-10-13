package logback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.AppenderBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Logback appender keeps events in memory (per thread), writes these to errorLogger/errorAppender when an error is logged.
 * @author Koert Zeilstra
 */
public class LogContextAppender<E> extends AppenderBase<E> {
  public static final String MARKER_RESET_SESSION = "resetSession";

  private final ThreadLocal<List<E>> events = ThreadLocal.withInitial(() -> new ArrayList<>());

  private String errorLogger = "incidentLogger";
  private String errorAppender = "incidentAppender";

  /**
   * @param errorLogger Name of logger where events will be logged, if ERROR event is received.
   */
  public void setErrorLogger(String errorLogger) {
    this.errorLogger = errorLogger;
  }

  /**
   * @param errorAppender Name of appender (contained in errorLogger) where events will be logged, if ERROR event is received.
   */
  public void setErrorAppender(String errorAppender) {
    this.errorAppender = errorAppender;
  }

  @Override
  protected void append(E event) {
    events.get().add(event);
    if (event instanceof ILoggingEvent) {
      ILoggingEvent loggingEvent = (ILoggingEvent) event;
      if (loggingEvent.getMarker().contains(MARKER_RESET_SESSION)) {
        this.clearLog();
        events.get().add(event);
      }
      if (loggingEvent.getLevel().isGreaterOrEqual(Level.ERROR)) {
        this.writeLogToIncidentLogger();
        this.clearLog();
      }
    }
  }

  /**
   * Write all stored events to errorLogger/errorAppender.
   */
  private void writeLogToIncidentLogger() {
    Logger incidentLogger = LoggerFactory.getLogger(this.errorLogger);
    if (incidentLogger != null) {
      Appender<ILoggingEvent> incidentAppender = ((ch.qos.logback.classic.Logger) incidentLogger).getAppender(this.errorAppender);
      if (incidentAppender != null) {
        for(E event:events.get()) {
          if (event instanceof ILoggingEvent) {
            incidentAppender.doAppend((ILoggingEvent) event);
          }
        }
      }
    }

  }

  /**
   * Clear the stored events.
   */
  private void clearLog() {
    events.get().clear();
  }

}