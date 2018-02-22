package com.hashnot.silverexchange.match;

import org.junit.jupiter.api.Test;

import static com.hashnot.silverexchange.match.Side.ASK;
import static com.hashnot.silverexchange.match.Side.BID;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SideTest {
    @Test
    void testReverseOfAskIsBid() {
        assertEquals(BID, ASK.reverse());
    }

    @Test
    void testReverseOfBidIsAsk() {
        assertEquals(ASK, BID.reverse());
    }
}
