package com.hashnot.silver.engine;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.hashnot.silver.engine.TestOfferFactory.ask;
import static com.hashnot.silver.engine.TestOfferFactory.bid;
import static java.math.BigDecimal.ONE;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class OfferTest {
    private static final BigDecimal TWO = new BigDecimal(2);
    private static final BigDecimal THREE = new BigDecimal(3);

    @Test
    void testExceptionOnNonPositiveRate() {
        assertThrows(IllegalArgumentException.class, () -> ask(ONE, ONE.negate()));
    }

    @Test
    void testExceptionOnNonPositiveAmount() {
        assertThrows(IllegalArgumentException.class, () -> ask(ONE.negate(), ONE));
    }

    @Test
    void executeExactlyMatchingOffers() {
        final BigDecimal RATE = TWO;
        Offer against = bid(ONE, RATE);
        Offer offer = ask(ONE, RATE);


        ExecutionResult result = offer.execute(against);


        assertNull(result.againstRemainder);
        assertNull(result.remainder);

        Transaction expectedTx = new Transaction(ONE, RATE);
        assertEquals(expectedTx, result.transaction);
    }

    @Test
    void testExecutePartialMatchWithRemainder() {
        final BigDecimal RATE = TWO;
        Offer against = bid(ONE, RATE);
        Offer offer = ask(THREE, RATE);


        ExecutionResult result = offer.execute(against);

        assertNull(result.againstRemainder);

        Offer expectedRemainder = ask(TWO, offer.getRate());
        assertEquals(expectedRemainder, result.remainder);

        Transaction expectedTx = new Transaction(ONE, RATE);
        assertEquals(expectedTx, result.transaction);
    }

    @Test
    void testExecutePartialMatchWithAgainstRemainder() {
        final BigDecimal RATE = TWO;
        Offer against = bid(THREE, RATE);
        Offer offer = ask(TWO, RATE);


        ExecutionResult result = offer.execute(against);

        assertNull(result.remainder);

        Offer expectedAgainstRemainder = bid(ONE, against.getRate());
        assertEquals(expectedAgainstRemainder, result.againstRemainder);

        Transaction expectedTx = new Transaction(TWO, RATE);
        assertEquals(expectedTx, result.transaction);
    }

    @Test
    void testExecuteNoMatch() {
        Offer against = bid(THREE, ONE);
        Offer offer = ask(TWO, TWO);


        ExecutionResult result = offer.execute(against);

        assertNull(result.transaction);

        assertEquals(against, result.againstRemainder);

        assertEquals(offer, result.remainder);
    }

    @Test
    void testEqualRateMatch() {
        final BigDecimal ANY = TWO;
        Offer against = bid(ANY, ONE);
        Offer offer = ask(ANY, ONE);
        assertTrue(offer.rateMatch(against));
    }

    /**
     * Buyer is paying more then seller is selling for
     */
    @Test
    void testGreaterRateMatch() {
        final BigDecimal ANY = TWO;
        Offer against = bid(ANY, TWO);
        Offer offer = ask(ANY, ONE);
        assertTrue(offer.rateMatch(against));
    }

    /**
     * Buyer is willing less the the seller is selling for
     */
    @Test
    void testSmallerRateNoMatch() {
        final BigDecimal ANY = TWO;
        Offer against = bid(ANY, ONE);
        Offer offer = ask(ANY, TWO);
        assertFalse(offer.rateMatch(against));
    }

    @Test
    void testRateComparatorTwoBidsDescending() {
        Offer o1 = bid(ONE, ONE);
        Offer o2 = bid(ONE, TWO);
        int compare = Offer.COMPARATOR_BY_RATE.compare(o1, o2);
        assertTrue(compare > 0);

        List<Offer> offers = asList(o1, o2);
        offers.sort(Offer.COMPARATOR_BY_RATE);

        List<Offer> expected = asList(o2, o1);
        assertEquals(expected, offers);
    }

    @Test
    void testRateComparatorTwoAsksAscending() {
        Offer o1 = ask(ONE, ONE);
        Offer o2 = ask(ONE, TWO);
        int compare = Offer.COMPARATOR_BY_RATE.compare(o1, o2);
        assertTrue(compare < 0);

        List<Offer> offers = asList(o1, o2);
        offers.sort(Offer.COMPARATOR_BY_RATE);

        List<Offer> expected = asList(o1, o2);
        assertEquals(expected, offers);
    }
}
