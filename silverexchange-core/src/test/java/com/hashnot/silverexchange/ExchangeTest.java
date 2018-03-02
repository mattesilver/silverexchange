package com.hashnot.silverexchange;

import com.hashnot.silverexchange.ext.ITransactionFactory;
import com.hashnot.silverexchange.match.Offer;
import com.hashnot.silverexchange.test.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import static com.hashnot.silverexchange.TestModelFactory.*;
import static java.math.BigDecimal.ONE;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith({MockitoExtension.class})
class ExchangeTest {
    @Test
    void testNoOfferEmptyTransactionList() {
        assertEquals(emptyList(), Exchange.create().getAllTransactions());
    }

    @Test
    void testOneOfferEmptyTxList() {
        Exchange<Transaction, Offer> x = Exchange.create();
        x.post(ask(ONE, ONE));

        assertEquals(emptyList(), x.getAllTransactions());
    }

    @Test
    void testSameSideOffersEmptyTxList() {
        Exchange<Transaction, Offer> x = Exchange.create();
        x.post(ask(ONE, ONE));
        x.post(ask(ONE, ONE));
        assertEquals(2, x.getAllOffers().size());

        assertEquals(emptyList(), x.getAllTransactions());
    }

    @Test
    void testOneTxAppearsInTxList() {
        Exchange<Transaction, Offer> x = Exchange.create();
        x.post(ask(ONE, ONE));
        x.post(bid(ONE, ONE));
        assertTrue(OrderBook.isEmpty(x.getAllOffers()));

        assertEquals(singletonList(tx(ONE, ONE)), x.getAllTransactions());
    }

    @Mock
    private ITransactionFactory<Offer, Transaction> txFactory;

    @Test
    void testUsageOfTransactionFactory() {
        Transaction myTx = tx(ONE, ONE);

        Exchange<Transaction, Offer> x = new Exchange<>(txFactory);

        x.post(ask(ONE, ONE));
        x.post(bid(ONE, ONE));

        Mockito.verify(txFactory).create(eq(ONE), eq(ONE), eq(bid(ONE, ONE)));
    }
}
