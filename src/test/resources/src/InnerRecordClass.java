package src;

public class InnerRecordClass {
    private final int foo = 0;
    private final int bar = 1;

    public static final class Inner extends Record {
        private final long fooo;

        public Inner(long fooo) {
            this.fooo = fooo;
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