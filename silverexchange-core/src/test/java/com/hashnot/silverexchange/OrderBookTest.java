package com.hashnot.silverexchange;

import com.hashnot.silverexchange.match.Offer;
import com.hashnot.silverexchange.match.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.hashnot.silverexchange.ExecutionResult.empty;
import static com.hashnot.silverexchange.TestModelFactory.*;
import static com.hashnot.silverexchange.util.BigDecimalsTest.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderBookTest {

    @Test
    void postValidOfferToEmptyBookEmptyResult() {
        ExecutionResult transactions = b().post(ask(ONE, TWO));
        assertEquals(empty(), transactions);
    }

    @Test
    void postValidNonMatchingOfferToNonEmptyBookEmptyResult() {
        OrderBook book = b();

        book.post(ask(ONE, TWO));
        ExecutionResult transactions = book.post(ask(ONE, TWO));
        assertEquals(empty(), transactions);

        transactions = book.post(bid(ONE, ONE));
        assertEquals(empty(), transactions);
    }

    @Test
    void testPostMatchingOfferResultTransaction() {
        OrderBook book = b();
        book.post(ask(ONE, ONE));
        ExecutionResult result = book.post(bid(ONE, ONE));

        List<Transaction> expectedTxs = singletonList(tx(ONE, ONE));

        assertEquals(new ExecutionResult(expectedTxs, null), result);


        book = b();
        book.post(bid(ONE, ONE));
        result = book.post(ask(ONE, ONE));

        expectedTxs = singletonList(tx(ONE, ONE));

        assertEquals(new ExecutionResult(expectedTxs, null), result);
    }

    @Test
    void testPartialExecAgainstSingleOffer() {
        OrderBook book = b();
        book.post(ask(THREE, ONE));

        ExecutionResult result = book.post(bid(TWO, ONE));

        List<Transaction> expectedTxs = singletonList(tx(TWO, ONE));
        assertEquals(new ExecutionResult(expectedTxs, null), result);
    }

    @Test
    void testPartialExecAgainstMultiOffers() {
        OrderBook book = b();
        book.post(ask(ONE, ONE));
        book.post(ask(ONE, ONE));

        ExecutionResult result = book.post(bid(THREE, ONE));

        List<Transaction> expectedTxs = asList(
                tx(ONE, ONE),
                tx(ONE, ONE)
        );
        assertEquals(new ExecutionResult(expectedTxs, null), result);
    }

    @Test
    void testEmptyOfferList() {
        OrderBook book = b();
        assertEquals(emptyList(), book.getAllOffers());
    }

    @Test
    void testSingleOfferList() {
        Offer offer = ask(ONE, ONE);
        OrderBook book = b();
        book.post(offer);

        assertEquals(singletonList(offer), book.getAllOffers());
    }

    @Test
    void testMultiOfferList() {
        Offer offer1 = ask(ONE, ONE);
        Offer offer2 = ask(ONE, ONE);
        OrderBook book = b();
        book.post(offer1);
        book.post(offer2);

        assertEquals(asList(offer1, offer2), book.getAllOffers());
    }

    @Test
    void testOfferListOrder() {
        OrderBook book = b();

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
        OrderBook book = b();

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

    @Test
    void testMarketOrderOnEmptyOrderBook() {
        //given
        OrderBook book = b();

        //when
        ExecutionResult result = book.post(bid(ONE, market()));

        //expect
        assertEquals(new ExecutionResult(emptyList(), bid(ONE, market())), result);
    }

    @Test
    void testMarketOrderAgainstOrderBookWithNonMatchingOrder() {
        //given
        OrderBook book = b();
        book.post(bid(ONE, ONE));

        //when
        ExecutionResult result = book.post(bid(ONE, market()));

        //expect
        assertEquals(new ExecutionResult(emptyList(), bid(ONE, market())), result);
        assertEquals(singletonList(bid(ONE, ONE)), book.getAllOffers());
    }

    @Test
    void testMarketOrderAgainstOrderBookWithMatchingOrder() {
        //given
        OrderBook book = b();
        book.post(ask(ONE, ONE));

        //when
        ExecutionResult result = book.post(bid(ONE, market()));

        //expect
        assertEquals(new ExecutionResult(singletonList(tx(ONE, ONE)), null), result);
        assertEquals(emptyList(), book.getAllOffers());
    }

    @Test
    void testPartialExecutionOfMarketOrderInOrderBook() {
        //given
        OrderBook book = b();
        book.post(ask(ONE, ONE));

        //when
        ExecutionResult result = book.post(bid(TWO, market()));

        //expect
        assertEquals(new ExecutionResult(singletonList(tx(ONE, ONE)), bid(ONE, market())), result);
        assertEquals(emptyList(), book.getAllOffers());
    }

    @Test
    void testPartialExecutionOfMarketOrderInOrderBookWithRemainderInOrderBook() {
        //given
        OrderBook book = b();
        book.post(ask(TWO, ONE));

        //when
        ExecutionResult result = book.post(bid(ONE, market()));

        //expect
        assertEquals(new ExecutionResult(singletonList(tx(ONE, ONE)), null), result);
        assertEquals(singletonList(ask(ONE, ONE)), book.getAllOffers());
    }
}
