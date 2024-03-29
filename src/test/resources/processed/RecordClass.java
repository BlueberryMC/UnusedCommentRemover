package src;

public record RecordClass(int foo, int bar) {

    public static RecordClass from(int foo, int bar) {
        return new RecordClass(foo, bar);
    }

    public static record Inner(long fooo) {

        public Inner() {
            this(0L);
        }
    }
}