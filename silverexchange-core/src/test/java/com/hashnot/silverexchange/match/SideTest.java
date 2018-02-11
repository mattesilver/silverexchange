package com.hashnot.silverexchange.match;

import org.junit.jupiter.api.Test;

import static com.hashnot.silverexchange.match.Side.Ask;
import static com.hashnot.silverexchange.match.Side.Bid;
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
