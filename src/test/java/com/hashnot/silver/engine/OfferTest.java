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
    void testExceptionOnNonPositiveRate() {
        assertThrows(IllegalArgumentException.class, () -> new Offer(PAIR, Ask, ONE, ONE.negate()));
    }

    @Test
    void testExceptionOnNonPositiveAmount() {
        assertThrows(IllegalArgumentException.class, () -> new Offer(PAIR, Ask, ONE.negate(), ONE));
    }

    @Test
    void executeExactlyMatchingOffers() {
        final BigDecimal RATE = TWO;
        Offer against = new Offer(PAIR, Bid, ONE, RATE);
        Offer offer = new Offer(PAIR, Ask, ONE, RATE);


        ExecutionResult result = offer.execute(against);


        assertNull(result.againstRemainder);
        assertNull(result.remainder);

        Transaction expectedTx = new Transaction(ONE, RATE);
        assertEquals(expectedTx, result.transaction);
    }

    @Test
    void testExecutePartialMatchWithRemainder() {
        final BigDecimal RATE = TWO;
        Offer against = new Offer(PAIR, Bid, ONE, RATE);
        Offer offer = new Offer(PAIR, Ask, THREE, RATE);


        ExecutionResult result = offer.execute(against);

        assertNull(result.againstRemainder);

        Offer expectedRemainder = new Offer(offer.getPair(), offer.getSide(), TWO, offer.getRate());
        assertEquals(expectedRemainder, result.remainder);

        Transaction expectedTx = new Transaction(ONE, RATE);
        assertEquals(expectedTx, result.transaction);
    }

    @Test
    void testExecutePartialMatchWithAgainstRemainder() {
        final BigDecimal RATE = TWO;
        Offer against = new Offer(PAIR, Bid, THREE, RATE);
        Offer offer = new Offer(PAIR, Ask, TWO, RATE);


        ExecutionResult result = offer.execute(against);

        assertNull(result.remainder);

        Offer expectedAgainstRemainder = new Offer(against.getPair(), against.getSide(), ONE, against.getRate());
        assertEquals(expectedAgainstRemainder, result.againstRemainder);

        Transaction expectedTx = new Transaction(TWO, RATE);
        assertEquals(expectedTx, result.transaction);
    }

    @Test
    void testExecuteNoMatch() {
        Offer against = new Offer(PAIR, Bid, THREE, ONE);
        Offer offer = new Offer(PAIR, Ask, TWO, TWO);


        ExecutionResult result = offer.execute(against);

        assertNull(result.transaction);

        assertEquals(against, result.againstRemainder);

        assertEquals(offer, result.remainder);
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
    void testGreaterRateMatch() {
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
