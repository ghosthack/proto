package content.util;

public class Preconditions {
  public static boolean isNullOrEmpty(String s) {
    return s == null || s.isEmpty();
  }
}
