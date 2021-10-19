package src;

public record RecordClass(int foo, int bar) {
    public static RecordClass from(int foo, int bar) {
        return new RecordClass(foo, bar);
    }
}