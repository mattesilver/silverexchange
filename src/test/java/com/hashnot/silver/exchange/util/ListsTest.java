package com.hashnot.silver.exchange.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ListsTest {
    static class Elem {
        final private int value;

        Elem(int value) {
            this.value = value;
        }

        int getValue() {
            return value;
        }
    }

    @Test
    void testNullColl() {
        @SuppressWarnings("all")
        Executable call = () -> Lists.lastIndexOf(null, new Elem(0), Elem::getValue);

        assertThrows(NullPointerException.class, call);
    }

    @Test
    void testNullSearched() {
        List<Elem> col = new ArrayList<>();
        Executable call = () -> Lists.lastIndexOf(col, null, Elem::getValue);
        assertThrows(NullPointerException.class, call);
    }

    @Test
    void testNullAccessor() {
        List<Elem> col = new ArrayList<>();
        Executable call = () -> Lists.lastIndexOf(col, new Elem(0), null);
        assertThrows(NullPointerException.class, call);
    }

    @Test
    void testEmptyReturnsMinus1() {
        List<Elem> col = new ArrayList<>();
        int i = Lists.lastIndexOf(col, new Elem(0), Elem::getValue);
        assertEquals(-1, i);
    }

    @Test
    void testNotFoundReturnsZero() {
        List<Elem> col = new ArrayList<>();
        col.add(new Elem(0));
        int i = Lists.lastIndexOf(col, new Elem(1), Elem::getValue);
        assertEquals(-1, i);
    }

    @Test
    void testFoundSingleReturnsZero() {
        List<Elem> col = new ArrayList<>();
        col.add(new Elem(0));
        int i = Lists.lastIndexOf(col, new Elem(0), Elem::getValue);
        assertEquals(0, i);
    }

    @Test
    void testFoundTwoReturnsOne() {
        List<Elem> col = new ArrayList<>();
        col.add(new Elem(0));
        col.add(new Elem(0));
        int i = Lists.lastIndexOf(col, new Elem(0), Elem::getValue);
        assertEquals(1, i);
    }

    @Test
    void testFoundMultiReturnsIndex() {
        List<Elem> col = new ArrayList<>();
        col.add(new Elem(0));
        col.add(new Elem(0));
        col.add(new Elem(1));
        int i = Lists.lastIndexOf(col, new Elem(0), Elem::getValue);
        assertEquals(1, i);
    }
}
