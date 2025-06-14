package vn.edu.hcmaf.apigamestore.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class Utils {

  public static LocalDateTime getDateTimeFromEpochSeconds(long epochSeconds) {
    return Instant.ofEpochSecond(epochSeconds).atZone(ZoneOffset.UTC).toLocalDateTime();
  }
}
