package com.hashnot.silver.engine;

import org.junit.jupiter.api.Test;
import org.knowm.xchange.currency.CurrencyPair;

import java.math.BigDecimal;

import static com.hashnot.silver.engine.Side.Ask;
import static com.hashnot.silver.engine.Side.Bid;
import static java.math.BigDecimal.ONE;
import static org.junit.jupiter.api.Assertions.*;

class OfferTest {
    private static CurrencyPair PAIR = CurrencyPair.BTC_EUR;
    private static BigDecimal AMT = ONE;
    private static BigDecimal RATE = new BigDecimal(2);

    @Test
    void executeExactlyMatchingOffers() {
        Offer against = new Offer(PAIR, Bid, AMT, RATE);
        Offer offer = new Offer(PAIR, Ask, AMT, RATE);
        ExecutionResult result = offer.execute(against);
        assertFalse(result.againstReminder.isPresent());
        assertFalse(result.reminder.isPresent());
        assertTrue(result.executed.isPresent());

        Transaction tx = result.executed.get();
        assertEquals(tx.amount, AMT);
        assertEquals(tx.rate, RATE);
    }
}
