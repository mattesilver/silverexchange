package com.hashnot.silver.engine;

import java.util.Comparator;

public enum Side {
    /**
     * Buy price or order
     */
    Bid(-1),

    /**
     * Sell price or order
     */
    Ask(1);

    final public int orderSignum;

    Side(int orderSignum) {
        this.orderSignum = orderSignum;
    }

    public Side opposite() {
        return this == Ask ? Bid : Ask;
    }

    static class RateComparator implements Comparator<Offer> {
        @Override
        public int compare(Offer o1, Offer o2) {
            assert o1.getPair().equals(o2.getPair());
            assert o1.getSide() == o2.getSide();
            return o1.getRate().compareTo(o2.getRate()) * o1.getSide().orderSignum;
        }
    }

    final public static Comparator<Offer> RATE_COMPARATOR = new RateComparator();
}
