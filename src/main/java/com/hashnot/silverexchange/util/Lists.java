package com.hashnot.silverexchange.util;

import java.util.List;
import java.util.function.Function;

import static com.google.common.collect.Lists.transform;

public class Lists {
    private Lists() {
        //Util class
    }

    /**
     * {@link List#lastIndexOf(Object)}
     */
    public static <T, F> int lastIndexOf(List<T> c, T e, Function<T, F> accessor) {
        @SuppressWarnings("all")
        List<F> elements = transform(c, accessor::apply);
        return elements.lastIndexOf(accessor.apply(e));
    }
}
