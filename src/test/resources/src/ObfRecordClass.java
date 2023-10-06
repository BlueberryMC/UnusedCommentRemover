package src;

public record ObfRecordClass(int a, int b) implements SomeListener {
    private final int foo;
    private final int bar;

    public ObfRecordClass(int foos, int bars) {
        this.foo = foos;
        this.bar = bars;
    }

    public static ObfRecordClass from(int foo, int bar) {
        return new ObfRecordClass(foo, bar);
    }

    public static record Inner(long a) {
        private final long fooo;

        public Inner(long fooo) {
            this.fooo = fooo;
        }

        public Inner() {
            this(0L);
        }
    }

    static record Inner2(long a) {
        private final long foo;

        Inner2(long l) {
            this.foo = l;
        }
    }
}
