package src;

public class InnerRecordClass {
    private final int foo = 0;
    private final int bar = 1;

    public static record Inner(long fooo) {
    }
}