package src;

public final class RecordClass extends Record {
    private final int foo;
    private final int bar;

    public RecordClass(int foos, int bars) {
        this.foo = foos;
        this.bar = bars;
    }

    public static RecordClass from(int foo, int bar) {
        return new RecordClass(foo, bar);
    }

    public final String toString() {
        return this.toString<invokedynamic>(this);
    }

    public final int hashCode() {
        return this.hashCode<invokedynamic>(this);
    }

    public final boolean equals(Object object) {
        return this.equals<invokedynamic>(this, object);
    }

    public int foo() {
        return this.foo;
    }

    public int bar() {
        return this.bar;
    }

    public static final class Inner extends Record {
        private final long fooo;

        public Inner(long fooo) {
            this.fooo = fooo;
        }

        public Inner() {
            this(0L);
        }

        public final String toString() {
            return this.toString<invokedynamic>(this);
        }
        public final int hashCode() {
            return this.hashCode<invokedynamic>(this);
        }
        public final boolean equals(Object object) {
            return this.equals<invokedynamic>(this, object);
        }

        public long fooo() {
            return this.fooo;
        }
    }
}