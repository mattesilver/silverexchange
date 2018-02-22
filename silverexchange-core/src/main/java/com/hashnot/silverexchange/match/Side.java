package com.hashnot.silverexchange.match;

public enum Side {
    /**
     * Buy price or order
     */
    BID(-1),

    /**
     * Sell price or order
     */
    ASK(1);

    public final int orderSignum;

    Side(int orderSignum) {
        this.orderSignum = orderSignum;
    }

    public Side reverse() {
        return this == ASK ? BID : ASK;
    }
}
