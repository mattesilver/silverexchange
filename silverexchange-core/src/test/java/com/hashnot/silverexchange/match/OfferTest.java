package com.hashnot.silverexchange.match;

import com.hashnot.silverexchange.OfferRate;
import com.hashnot.silverexchange.Transaction;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.hashnot.silverexchange.OfferRate.market;
import static com.hashnot.silverexchange.TestModelFactory.*;
import static com.hashnot.silverexchange.match.Side.ASK;
import static com.hashnot.silverexchange.util.BigDecimalsTest.*;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

class OfferTest {

    @Test
    void testExceptionOnNonPositiveRate() {
        assertThrows(IllegalArgumentException.class, () -> ask(ONE, ONE.negate()));
        assertThrows(IllegalArgumentException.class, () -> ask(ONE, ZERO));
    }

    @Test
    void testExceptionOnNonPositiveAmount() {
        assertThrows(IllegalArgumentException.class, () -> ask(ONE.negate(), ONE));
        assertThrows(IllegalArgumentException.class, () -> ask(ZERO, ONE));
    }

    @Test
    void executeExactlyMatchingOffers() {
        final BigDecimal rate = TWO;
        Offer passive = bid(ONE, rate);
        Offer active = ask(ONE, rate);

        TestTransactionListener l = new TestTransactionListener();
        OfferMatchResult<Offer> result = active.match(passive, l);


        assertNull(result.passiveRemainder);
        assertNull(result.activeRemainder);

        Transaction expectedTx = tx(ONE, rate);
        assertEquals(expectedTx, l.transaction);
    }

    @Test
    void testExecutePartialMatchWithRemainder() {
        final BigDecimal rate = TWO;
        Offer passive = bid(ONE, rate);
        Offer active = ask(THREE, rate);


        TestTransactionListener l = new TestTransactionListener();
        OfferMatchResult<Offer> result = active.match(passive, l);

        assertNull(result.passiveRemainder);

        Offer expectedRemainder = ask(TWO, active.getRate());
        assertEquals(expectedRemainder, result.activeRemainder);

        Transaction expectedTx = tx(ONE, rate);
        assertEquals(expectedTx, l.transaction);
    }

    @Test
    void testExecutePartialMatchWithAgainstRemainder() {
        final BigDecimal rate = TWO;
        Offer passive = bid(THREE, rate);
        Offer active = ask(TWO, rate);


        TestTransactionListener l = new TestTransactionListener();
        OfferMatchResult<Offer> result = active.match(passive, l);

        assertNull(result.activeRemainder);

        Offer expectedAgainstRemainder = bid(ONE, passive.getRate());
        assertEquals(expectedAgainstRemainder, result.passiveRemainder);

        Transaction expectedTx = tx(TWO, rate);
        assertEquals(expectedTx, l.transaction);
    }

    @Test
    void testExecuteNoMatch() {
        Offer passive = bid(THREE, ONE);
        Offer active = ask(TWO, TWO);


        TestTransactionListener l = new TestTransactionListener();
        OfferMatchResult<Offer> result = active.match(passive, l);

        assertNull(l.transaction);

        assertEquals(passive, result.passiveRemainder);

        assertEquals(active, result.activeRemainder);
    }

    @Test
    void testEqualRateMatch() {
        final BigDecimal rate = TWO;
        Offer passive = bid(rate, ONE);
        Offer active = ask(rate, ONE);
        assertTrue(active.rateMatch(passive));
    }

    /**
     * Buyer is paying more then seller is selling for
     */
    @Test
    void testGreaterRateMatch() {
        final BigDecimal rate = TWO;
        Offer passive = bid(rate, TWO);
        Offer active = ask(rate, ONE);
        assertTrue(active.rateMatch(passive));
    }

    /**
     * Buyer is willing less the the seller is selling for
     */
    @Test
    void testSmallerRateNoMatch() {
        final BigDecimal rate = TWO;
        Offer passive = bid(rate, ONE);
        Offer active = ask(rate, TWO);
        assertFalse(active.rateMatch(passive));
    }

    @Test
    void testRateComparatorTwoBidsDescending() {
        Offer o1 = bid(ONE, ONE);
        Offer o2 = bid(ONE, TWO);
        int compare = Offer.compareByRate(o1, o2);
        assertTrue(compare > 0);

        List<Offer> offers = asList(o1, o2);
        offers.sort(Offer::compareByRate);

        List<Offer> expected = asList(o2, o1);
        assertEquals(expected, offers);
    }

    @Test
    void testRateComparatorTwoAsksAscending() {
        Offer o1 = ask(ONE, ONE);
        Offer o2 = ask(ONE, TWO);
        int compare = Offer.compareByRate(o1, o2);
        assertTrue(compare < 0);

        List<Offer> offers = asList(o1, o2);
        offers.sort(Offer::compareByRate);

        List<Offer> expected = asList(o1, o2);
        assertEquals(expected, offers);
    }

    @Test
    void createMarketOrder() {
        Offer ask = ask(ONE, market());
        assertEquals(ONE, ask.getAmount());
        assertEquals(market(), ask.getRate());

        Offer bid = bid(ONE, market());
        assertEquals(ONE, bid.getAmount());
        assertEquals(market(), bid.getRate());
    }

    @Test
    void testMarketOrderAgainstMatchingOffer() {
        Offer passive = ask(ONE, ONE);
        Offer active = bid(ONE, market());

        TestTransactionListener l = new TestTransactionListener();
        OfferMatchResult<Offer> result = active.match(passive, l);

        OfferMatchResult<Offer> expected = new OfferMatchResult<>(null, null);
        assertEquals(expected, result);
    }

    @Test
    void testConstructorAssertions() {
        Assumptions.assumeTrue(Offer.class.desiredAssertionStatus());

        assertThrows(AssertionError.class, () -> new Offer(null, ONE, new OfferRate(ONE)));
        assertThrows(AssertionError.class, () -> new Offer(ASK, null, new OfferRate(ONE)));
        assertThrows(AssertionError.class, () -> new Offer(ASK, ONE, null));
    }
}
