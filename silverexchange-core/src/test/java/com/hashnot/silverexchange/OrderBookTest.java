package com.hashnot.silverexchange;

import com.hashnot.silverexchange.match.ITransactionListener;
import com.hashnot.silverexchange.match.Offer;
import com.hashnot.silverexchange.match.Side;
import com.hashnot.silverexchange.test.MockitoExtension;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.hashnot.silverexchange.OfferRate.market;
import static com.hashnot.silverexchange.TestModelFactory.*;
import static com.hashnot.silverexchange.util.BigDecimalsTest.*;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class})
class OrderBookTest {

    @Mock
    private ITransactionListener<Offer> l;

    @Test
    void postValidOfferToEmptyBookEmptyResult() {
        Offer remainder = b(l).post(ask(ONE, TWO));
        verify(l, never()).notifyTransaction(any(), any(), any());
        assertNull(remainder);
    }

    @Test
    void postValidNonMatchingOfferToNonEmptyBookEmptyResult() {
        OrderBook<Offer> book = b(l);

        book.post(ask(ONE, TWO));
        Offer remainder = book.post(ask(ONE, TWO));
        verify(l, never()).notifyTransaction(any(), any(), any());
        assertNull(remainder);

        remainder = book.post(bid(ONE, ONE));
        verify(l, never()).notifyTransaction(any(), any(), any());
        assertNull(remainder);
    }

    @Test
    void testPostMatchingOfferResultTransaction() {
        OrderBook<Offer> book = b(l);
        book.post(ask(ONE, ONE));
        Offer remainder = book.post(bid(ONE, ONE));

        verify(l).notifyTransaction(eq(ONE), eq(ONE), eq(bid(ONE, ONE)));
        assertNull(remainder);


        book = b(l);
        book.post(bid(ONE, ONE));
        remainder = book.post(ask(ONE, ONE));

        verify(l).notifyTransaction(eq(ONE), eq(ONE), eq(bid(ONE, ONE)));
        assertNull(remainder);
    }

    @Test
    void testPartialExecAgainstSingleOffer() {
        OrderBook<Offer> book = b(l);
        book.post(ask(THREE, ONE));

        Offer remainder = book.post(bid(TWO, ONE));

        verify(l).notifyTransaction(eq(TWO), eq(ONE), eq(bid(TWO, ONE)));
        assertNull(remainder);
    }

    @Test
    void testPartialExecAgainstMultiOffers() {
        OrderBook<Offer> book = b(l);
        book.post(ask(ONE, ONE));
        book.post(ask(ONE, ONE));

        Offer remainder = book.post(bid(THREE, ONE));

        verify(l).notifyTransaction(eq(ONE), eq(ONE), eq(bid(THREE, ONE)));
        verify(l).notifyTransaction(eq(ONE), eq(ONE), eq(bid(THREE, ONE)));
        assertNull(remainder);
    }

    @Test
    void testEmptyOfferList() {
        OrderBook<Offer> book = b(l);
        assertTrue(book.isEmpty());
    }

    @Test
    void testSingleOfferList() {
        Offer offer = ask(ONE, ONE);
        OrderBook<Offer> book = b(l);
        book.post(offer);

        assertEquals(sides(emptyList(), singletonList(offer)), book.getAllOffers());
    }

    @Test
    void testMultiOfferList() {
        Offer offer1 = ask(ONE, ONE);
        Offer offer2 = ask(ONE, ONE);
        OrderBook<Offer> book = b(l);
        book.post(offer1);
        book.post(offer2);

        assertEquals(sides(emptyList(), asList(offer1, offer2)), book.getAllOffers());
    }

    @Test
    void testOfferListOrder() {
        OrderBook<Offer> book = b(l);

        Offer offer1 = bid(ONE, ONE);
        book.post(offer1);

        Offer offer2 = bid(ONE, TWO);
        book.post(offer2);

        Offer offer3 = ask(ONE, new BigDecimal(3));
        book.post(offer3);

        Offer offer4 = ask(ONE, new BigDecimal(4));
        book.post(offer4);

        Map<Side, List<Offer>> expected = sides(asList(offer2, offer1), asList(offer3, offer4));
        assertEquals(expected, book.getAllOffers());
    }

    @Test
    void testNewElementIsAfterOtherWithSameRate() {
        OrderBook<Offer> book = b(l);

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

        Map<Side, List<Offer>> expected = sides(
                Arrays.stream(new int[]{1, 3, 5, 7})
                        .mapToObj(input::get)
                        .collect(Collectors.toList()),
                Arrays.stream(new int[]{0, 2, 4, 6})
                        .mapToObj(input::get)
                        .collect(Collectors.toList())
        );
        assertEquals(expected, book.getAllOffers());
    }

    @Test
    void testMarketOrderOnEmptyOrderBook() {
        //given
        OrderBook<Offer> book = b(l);

        //when
        Offer remainder = book.post(bid(ONE, market()));

        //expect
        verify(l, never()).notifyTransaction(any(), any(), any());
        assertEquals(bid(ONE, market()), remainder);
    }

    @Test
    void testMarketOrderAgainstOrderBookWithNonMatchingOrder() {
        //given
        OrderBook<Offer> book = b(l);
        book.post(bid(ONE, ONE));

        //when
        Offer remainder = book.post(bid(ONE, market()));

        //expect
        verify(l, never()).notifyTransaction(any(), any(), any());
        assertEquals(bid(ONE, market()), remainder);
        assertEquals(sides(singletonList(bid(ONE, ONE)), emptyList()), book.getAllOffers());
    }

    @Test
    void testMarketOrderAgainstOrderBookWithMatchingOrder() {
        //given
        OrderBook<Offer> book = b(l);
        book.post(ask(ONE, ONE));

        //when
        Offer remainder = book.post(bid(ONE, market()));

        //expect
        verify(l).notifyTransaction(eq(ONE), eq(ONE), eq(bid(ONE, market())));
        assertNull(remainder);
        assertTrue(book.isEmpty());
    }

    @Test
    void testPartialExecutionOfMarketOrderInOrderBook() {
        //given
        OrderBook<Offer> book = b(l);
        book.post(ask(ONE, ONE));

        //when
        Offer remainder = book.post(bid(TWO, market()));

        //expect
        verify(l).notifyTransaction(eq(ONE), eq(ONE), eq(bid(TWO, market())));
        assertEquals(bid(ONE, market()), remainder);
        assertTrue(book.isEmpty());
    }

    @Test
    void testPartialExecutionOfMarketOrderInOrderBookWithRemainderInOrderBook() {
        //given
        OrderBook<Offer> book = b(l);
        book.post(ask(TWO, ONE));

        //when
        Offer remainder = book.post(bid(ONE, market()));

        //expect
        verify(l).notifyTransaction(eq(ONE), eq(ONE), eq(bid(ONE, market())));
        assertNull(remainder);
        assertEquals(sides(emptyList(), singletonList(ask(ONE, ONE))), book.getAllOffers());
    }

    @Test
    void testCtorAssert() {
        Assumptions.assumeTrue(OrderBook.class.desiredAssertionStatus());

        assertThrows(AssertionError.class, () -> new OrderBook<>(null));
    }

    @Test
    void testExecuteAssert() {
        Assumptions.assumeTrue(OrderBook.class.desiredAssertionStatus());

        OrderBook<Offer> orderBook = new OrderBook<>(l);

        assertThrows(AssertionError.class, () -> orderBook.post(null));
        verify(l, never()).notifyTransaction(any(), any(), any());
    }
}
