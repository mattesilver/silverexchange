package com.hashnot.silver.exchange;

import com.hashnot.silver.engine.Offer;
import com.hashnot.silver.engine.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.hashnot.silver.engine.Side.Ask;
import static com.hashnot.silver.engine.Side.Bid;
import static java.math.BigDecimal.ONE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderBookTest {

    private static final Object PAIR = new Object();
    private static final BigDecimal TWO = new BigDecimal(2);
    private static final BigDecimal THREE = new BigDecimal(3);


    @Test
    void postValidOfferToEmptyBookEmptyResult() {
        List<Transaction> transactions = new OrderBook().post(new Offer(PAIR, Ask, ONE, TWO));
        assertEquals(emptyList(), transactions);
    }

    @Test
    void postValidNonMatchingOfferToNonEmptyBookEmptyResult() {
        OrderBook book = new OrderBook();

        book.post(new Offer(PAIR, Ask, ONE, TWO));
        List<Transaction> transactions = book.post(new Offer(PAIR, Ask, ONE, TWO));
        assertEquals(emptyList(), transactions);

        transactions = book.post(new Offer(PAIR, Bid, ONE, ONE));
        assertEquals(emptyList(), transactions);
    }

    @Test
    void testPostMatchingOfferResultTransaction() {
        OrderBook book = new OrderBook();
        book.post(new Offer(PAIR, Ask, ONE, ONE));
        List<Transaction> txs = book.post(new Offer(PAIR, Bid, ONE, ONE));

        List<Transaction> expectedTxs = singletonList(new Transaction(ONE, ONE));

        assertEquals(expectedTxs, txs);


        book = new OrderBook();
        book.post(new Offer(PAIR, Bid, ONE, ONE));
        txs = book.post(new Offer(PAIR, Ask, ONE, ONE));

        expectedTxs = singletonList(new Transaction(ONE, ONE));

        assertEquals(expectedTxs, txs);
    }

    @Test
    void testPartialExecAgainstSingleOffer() {
        OrderBook book = new OrderBook();
        book.post(new Offer(PAIR, Ask, THREE, ONE));

        List<Transaction> txs = book.post(new Offer(PAIR, Bid, TWO, ONE));

        List<Transaction> expectedTxs = singletonList(new Transaction(TWO, ONE));
        assertEquals(expectedTxs, txs);
    }

    @Test
    void testPartialExecAgainstMultiOffers() {
        OrderBook book = new OrderBook();
        book.post(new Offer(PAIR, Ask, ONE, ONE));
        book.post(new Offer(PAIR, Ask, ONE, ONE));

        List<Transaction> txs = book.post(new Offer(PAIR, Bid, THREE, ONE));

        List<Transaction> expectedTxs = asList(
                new Transaction(ONE, ONE),
                new Transaction(ONE, ONE)
        );
        assertEquals(expectedTxs, txs);
    }
}
