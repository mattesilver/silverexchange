package com.hashnot.silver.engine;

import org.junit.jupiter.api.Test;

import static com.hashnot.silver.engine.Side.Ask;
import static com.hashnot.silver.engine.Side.Bid;
import static org.junit.jupiter.api.Assertions.*;

class SideTest {
    @Test
    void testReverseOfAskIsBid() {
        assertEquals(Bid, Ask.reverse());
    }

    @Test
    void testReverseOfBidIsAsk() {
        assertEquals(Ask, Bid.reverse());
    }
}
