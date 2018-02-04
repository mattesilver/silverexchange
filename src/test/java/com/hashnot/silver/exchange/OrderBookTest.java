package com.hashnot.silver.exchange;

import com.hashnot.silver.engine.Offer;
import com.hashnot.silver.engine.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.hashnot.silver.engine.Side.Ask;
import static com.hashnot.silver.engine.Side.Bid;
import static com.hashnot.silver.engine.TestOfferFactory.ask;
import static com.hashnot.silver.engine.TestOfferFactory.bid;
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

    @Test
    void testEmptyOfferList() {
        OrderBook book = new OrderBook();
        assertEquals(emptyList(), book.getAllOffers());
    }

    @Test
    void testSingleOfferList() {
        Offer offer = ask(ONE, ONE);
        OrderBook book = new OrderBook();
        book.post(offer);

        assertEquals(singletonList(offer), book.getAllOffers());
    }

    @Test
    void testMultiOfferList() {
        Offer offer1 = ask(ONE, ONE);
        Offer offer2 = ask(ONE, ONE);
        OrderBook book = new OrderBook();
        book.post(offer1);
        book.post(offer2);

        assertEquals(asList(offer1, offer2), book.getAllOffers());
    }

    @Test
    void testOfferListOrder() {
        OrderBook book = new OrderBook();

        Offer offer1 = bid(ONE, ONE);
        book.post(offer1);

        Offer offer2 = bid(ONE, TWO);
        book.post(offer2);

        Offer offer3 = ask(ONE, new BigDecimal(3));
        book.post(offer3);

        Offer offer4 = ask(ONE, new BigDecimal(4));
        book.post(offer4);

        List<Offer> expected = asList(offer1, offer2, offer3, offer4);
        assertEquals(expected, book.getAllOffers());
    }

    @Test
    void testNewElementIsAfterOtherWithSameRate() {
        OrderBook book = new OrderBook();

        // generate a bunch of orders and make sure orderbook returns them in the same order (bids reversed)

        List<Offer> input = new ArrayList<>(8);
        for (int i = 0; i < 4; i++) {
            BigDecimal amt = new BigDecimal(i + 1);
            Offer ask = ask(amt, TWO);
            book.post(ask);
            input.add(ask);

            Offer bid = bid(amt, ONE);
            book.post(bid);
            input.add(bid);
        }

        int[] expectedOrder = {7, 5, 3, 1, 0, 2, 4, 6};
        List<Offer> expected = Arrays.stream(expectedOrder)
                .mapToObj(input::get)
                .collect(Collectors.toList());
        assertEquals(expected, book.getAllOffers());
    }
}
