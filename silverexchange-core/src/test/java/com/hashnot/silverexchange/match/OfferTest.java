package com.hashnot.silverexchange.match;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.hashnot.silverexchange.TestModelFactory.*;
import static com.hashnot.silverexchange.util.BigDecimalsTest.*;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

// TODO rename variable RATE to lower-case
class OfferTest {

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
        Offer passive = bid(ONE, RATE);
        Offer active = ask(ONE, RATE);


        OfferExecutionResult result = active.execute(passive, TX_FACTORY);


        assertNull(result.passiveRemainder);
        assertNull(result.remainder);

        Transaction expectedTx = tx(ONE, RATE);
        assertEquals(expectedTx, result.transaction);
    }

    @Test
    void testExecutePartialMatchWithRemainder() {
        final BigDecimal RATE = TWO;
        Offer passive = bid(ONE, RATE);
        Offer active = ask(THREE, RATE);


        OfferExecutionResult result = active.execute(passive, TX_FACTORY);

        assertNull(result.passiveRemainder);

        Offer expectedRemainder = ask(TWO, active.getRate());
        assertEquals(expectedRemainder, result.remainder);

        Transaction expectedTx = tx(ONE, RATE);
        assertEquals(expectedTx, result.transaction);
    }

    @Test
    void testExecutePartialMatchWithAgainstRemainder() {
        final BigDecimal RATE = TWO;
        Offer passive = bid(THREE, RATE);
        Offer active = ask(TWO, RATE);


        OfferExecutionResult result = active.execute(passive, TX_FACTORY);

        assertNull(result.remainder);

        Offer expectedAgainstRemainder = bid(ONE, passive.getRate());
        assertEquals(expectedAgainstRemainder, result.passiveRemainder);

        Transaction expectedTx = tx(TWO, RATE);
        assertEquals(expectedTx, result.transaction);
    }

    @Test
    void testExecuteNoMatch() {
        Offer passive = bid(THREE, ONE);
        Offer active = ask(TWO, TWO);


        OfferExecutionResult result = active.execute(passive, TX_FACTORY);

        assertNull(result.transaction);

        assertEquals(passive, result.passiveRemainder);

        assertEquals(active, result.remainder);
    }

    @Test
    void testEqualRateMatch() {
        final BigDecimal ANY = TWO;
        Offer passive = bid(ANY, ONE);
        Offer active = ask(ANY, ONE);
        assertTrue(active.rateMatch(passive));
    }

    /**
     * Buyer is paying more then seller is selling for
     */
    @Test
    void testGreaterRateMatch() {
        final BigDecimal ANY = TWO;
        Offer passive = bid(ANY, TWO);
        Offer active = ask(ANY, ONE);
        assertTrue(active.rateMatch(passive));
    }

    /**
     * Buyer is willing less the the seller is selling for
     */
    @Test
    void testSmallerRateNoMatch() {
        final BigDecimal ANY = TWO;
        Offer passive = bid(ANY, ONE);
        Offer active = ask(ANY, TWO);
        assertFalse(active.rateMatch(passive));
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

    @Test
    void createMarketOrder() {
        ask(ONE, market());
        bid(ONE, market());
        // no exceptions
    }

    @Test
    void testMarketOrderAgainstMatchingOffer() {
        Offer passive = ask(ONE, ONE);
        Offer active = bid(ONE, market());

        OfferExecutionResult result = active.execute(passive, TX_FACTORY);

        OfferExecutionResult expected = new OfferExecutionResult(tx(ONE, ONE), null, null);
        assertEquals(expected, result);
    }
}
