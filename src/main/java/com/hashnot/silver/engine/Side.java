package com.hashnot.silver.engine;

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

    public Side reverse() {
        return this == Ask ? Bid : Ask;
    }
}
