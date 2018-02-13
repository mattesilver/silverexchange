package com.hashnot.silverexchange;

import com.hashnot.silverexchange.match.Transaction;
import com.hashnot.silverexchange.util.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import static com.hashnot.silverexchange.TestModelFactory.*;
import static java.math.BigDecimal.ONE;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
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
        assertTrue(OrderBook.isEmpty(x.getAllOffers()));

        assertEquals(singletonList(tx(ONE, ONE)), x.getAllTransactions());
    }

    @Mock
    private ITransactionFactory txFactory;

    @Test
    void testUsageOfTransactionFactory() {
        Transaction myTx = new Transaction(ONE, new TransactionRate(ONE), TS);
        when(txFactory.apply(any(), any())).thenReturn(myTx);
        Exchange x = new Exchange(txFactory);

        x.post(ask(ONE, ONE));
        x.post(bid(ONE, ONE));

        assertEquals(singletonList(myTx), x.getAllTransactions());
    }
}
