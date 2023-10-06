package src;

public record ObfRecordClass(int foo, int bar) implements SomeListener {

    public static ObfRecordClass from(int foo, int bar) {
        return new ObfRecordClass(foo, bar);
    }

    public record Inner(long fooo) {

        public Inner() {
            this(0L);
        }
    }

    record Inner2(long foo) {
    }
}
