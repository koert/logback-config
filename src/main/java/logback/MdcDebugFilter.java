package logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * Filter to accept events when a specified MCD key exists.
 * @author Koert Zeilstra
 */
public class MdcDebugFilter extends Filter<ILoggingEvent> {

  private String mdcKey;

  /**
   * @param mdcKey MDC key - when this is set, this filter will accept every event for logging.
   */
  public void setMdcKey(String mdcKey) {
    this.mdcKey = mdcKey;
  }

  @Override
  public FilterReply decide(ILoggingEvent event) {
    FilterReply reply = null;
    if (event.getMDCPropertyMap().containsKey(mdcKey)) {
      reply = FilterReply.ACCEPT;
    } else {
      reply = FilterReply.NEUTRAL;
    }
    return reply;
  }
}
