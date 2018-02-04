package com.hashnot.silver.exchange.util;

import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.function.Function;

public class Lists {
    private Lists() {
        //Util class
    }

    /**
     * {@link List#lastIndexOf(Object)}
     */
    public static <T> int lastIndexOf(List<T> c, T e, Function<T, ?> accessor) {
        Objects.requireNonNull(e);
        Objects.requireNonNull(accessor);

        ListIterator<T> i = c.listIterator(c.size());
        int index = c.size();
        while (i.hasPrevious()) {
            --index;
            if (accessor.apply(i.previous()).equals(accessor.apply(e)))
                return index;
        }
        return -1;
    }
}
