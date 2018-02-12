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
        Offer against = bid(ONE, RATE);
        Offer offer = ask(ONE, RATE);


        OfferExecutionResult result = offer.execute(against, CLOCK);


        assertNull(result.againstRemainder);
        assertNull(result.remainder);

        Transaction expectedTx = tx(ONE, RATE);
        assertEquals(expectedTx, result.transaction);
    }

    @Test
    void testExecutePartialMatchWithRemainder() {
        final BigDecimal RATE = TWO;
        Offer against = bid(ONE, RATE);
        Offer offer = ask(THREE, RATE);


        OfferExecutionResult result = offer.execute(against, CLOCK);

        assertNull(result.againstRemainder);

        Offer expectedRemainder = ask(TWO, offer.getRate());
        assertEquals(expectedRemainder, result.remainder);

        Transaction expectedTx = tx(ONE, RATE);
        assertEquals(expectedTx, result.transaction);
    }

    @Test
    void testExecutePartialMatchWithAgainstRemainder() {
        final BigDecimal RATE = TWO;
        Offer against = bid(THREE, RATE);
        Offer offer = ask(TWO, RATE);


        OfferExecutionResult result = offer.execute(against, CLOCK);

        assertNull(result.remainder);

        Offer expectedAgainstRemainder = bid(ONE, against.getRate());
        assertEquals(expectedAgainstRemainder, result.againstRemainder);

        Transaction expectedTx = tx(TWO, RATE);
        assertEquals(expectedTx, result.transaction);
    }

    @Test
    void testExecuteNoMatch() {
        Offer against = bid(THREE, ONE);
        Offer offer = ask(TWO, TWO);


        OfferExecutionResult result = offer.execute(against, CLOCK);

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

    @Test
    void createMarketOrder() {
        ask(ONE, market());
        bid(ONE, market());
        // no exceptions
    }

    @Test
    void testMarketOrderAgainstMatchingOffer() {
        Offer existing = ask(ONE, ONE);
        Offer market = bid(ONE, market());

        OfferExecutionResult result = market.execute(existing, CLOCK);

        OfferExecutionResult expected = new OfferExecutionResult(tx(ONE, ONE), null, null);
        assertEquals(expected, result);
    }
}
