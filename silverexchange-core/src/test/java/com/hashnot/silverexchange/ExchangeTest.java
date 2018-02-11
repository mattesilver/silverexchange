package com.hashnot.silverexchange;

import org.junit.jupiter.api.Test;

import static com.hashnot.silverexchange.TestModelFactory.*;
import static java.math.BigDecimal.ONE;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExchangeTest {
    @Test
    void testNoOfferEmptyTransactionList() {
        assertEquals(emptyList(), n().getAllTransactions());
    }

    @Test
    void testOneOfferEmptyTxList() {
        Exchange x = n();
        x.post(ask(ONE, ONE));

        assertEquals(emptyList(), x.getAllTransactions());
    }

    @Test
    void testSameSideOffersEmptyTxList() {
        Exchange x = n();
        x.post(ask(ONE, ONE));
        x.post(ask(ONE, ONE));
        assertEquals(2, x.getAllOffers().size());

        assertEquals(emptyList(), x.getAllTransactions());
    }

    @Test
    void testOneTxAppearsInTxList() {
        Exchange x = n();
        x.post(ask(ONE, ONE));
        x.post(bid(ONE, ONE));
        assertTrue(x.getAllOffers().isEmpty());

        assertEquals(singletonList(tx(ONE, ONE)), x.getAllTransactions());
    }

}
