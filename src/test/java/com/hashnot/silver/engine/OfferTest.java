package com.hashnot.silver.engine;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.hashnot.silver.engine.Side.Ask;
import static com.hashnot.silver.engine.Side.Bid;
import static java.math.BigDecimal.ONE;
import static org.junit.jupiter.api.Assertions.*;

class OfferTest {
    private static final Object PAIR = new Object();
    private static final BigDecimal TWO = new BigDecimal(2);
    private static final BigDecimal THREE = new BigDecimal(3);

    @Test
    void executeExactlyMatchingOffers() {
        final BigDecimal RATE = TWO;
        Offer against = new Offer(PAIR, Bid, ONE, RATE);
        Offer offer = new Offer(PAIR, Ask, ONE, RATE);


        ExecutionResult result = offer.execute(against);


        assertNull(result.againstReminder);
        assertNull(result.reminder);

        Transaction expectedTx = new Transaction(ONE, RATE);
        assertEquals(expectedTx, result.executed);
    }

    @Test
    void executePartialMatchWithReminder() {
        final BigDecimal RATE = TWO;
        Offer against = new Offer(PAIR, Bid, ONE, RATE);
        Offer offer = new Offer(PAIR, Ask, THREE, RATE);


        ExecutionResult result = offer.execute(against);

        assertNull(result.againstReminder);

        Offer expectedReminder = new Offer(offer.getPair(), offer.getSide(), TWO, offer.getRate());
        assertEquals(expectedReminder, result.reminder);

        Transaction expectedTx = new Transaction(ONE, RATE);
        assertEquals(expectedTx, result.executed);
    }

    @Test
    void executePartialMatchWithAgainstReminder() {
        final BigDecimal RATE = TWO;
        Offer against = new Offer(PAIR, Bid, THREE, RATE);
        Offer offer = new Offer(PAIR, Ask, TWO, RATE);


        ExecutionResult result = offer.execute(against);

        assertNull(result.reminder);

        Offer expectedAgainstReminder = new Offer(against.getPair(), against.getSide(), ONE, against.getRate());
        assertEquals(expectedAgainstReminder, result.againstReminder);

        Transaction expectedTx = new Transaction(TWO, RATE);
        assertEquals(expectedTx, result.executed);
    }

    @Test
    void testExecuteNoMatch() {
        Offer against = new Offer(PAIR, Bid, THREE, ONE);
        Offer offer = new Offer(PAIR, Ask, TWO, TWO);


        ExecutionResult result = offer.execute(against);

        assertNull(result.executed);

        assertEquals(against, result.againstReminder);

        assertEquals(offer, result.reminder);
    }

    @Test
    void testEqualRateMatch() {
        final BigDecimal ANY = TWO;
        Offer against = new Offer(PAIR, Bid, ANY, ONE);
        Offer offer = new Offer(PAIR, Ask, ANY, ONE);
        assertTrue(offer.rateMatch(against));
    }

    /**
     * Buyer is paying more then seller is selling for
     */
    @Test
    void testBiggerRateMatch() {
        final BigDecimal ANY = TWO;
        Offer against = new Offer(PAIR, Bid, ANY, TWO);
        Offer offer = new Offer(PAIR, Ask, ANY, ONE);
        assertTrue(offer.rateMatch(against));
    }

    /**
     * Buyer is willing less the the seller is selling for
     */
    @Test
    void testSmallerRateNoMatch() {
        final BigDecimal ANY = TWO;
        Offer against = new Offer(PAIR, Bid, ANY, ONE);
        Offer offer = new Offer(PAIR, Ask, ANY, TWO);
        assertFalse(offer.rateMatch(against));
    }

}
