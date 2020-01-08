package example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;

/**
 * @author Koert Zeilstra
 */
public class Main {

  private static final Logger log = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    log.debug("debug 1");
    log.info("info 1");
    log.debug(MarkerFactory.getMarker("resetSession"), "reset log context session");
    log.debug("debug 2");
    log.info("info 2");
    log.error("error 1");
  }
}
