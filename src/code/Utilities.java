package code;

public class Utilities {
    public static boolean doubleEquals (double d1, double d2, double tolerance) {
        return Math.abs(d1-d2) < tolerance;
    }
    public static boolean between (double small, double d, double large) {
        return small < d && d < large;
    }
}
