package example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Koert Zeilstra
 */
public class Main {

  private static final Logger log = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    log.debug("debug 1");
    log.info("info 1");
    log.error("error 1");
  }
}
