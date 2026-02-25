package net.zaharenko424.cmrs.util;

import java.util.Objects;

@FunctionalInterface
public interface Consumer4<A, B, C, D> {

    void accept(A a, B b, C c, D d);

    default Consumer4<A, B, C, D> andThen(Consumer4<? super A, ? super B, ? super C, ? super D> after) {
        Objects.requireNonNull(after);

        return (a, b, c, d) -> {
            accept(a, b, c, d);
            after.accept(a, b, c, d);
        };
    }
}
