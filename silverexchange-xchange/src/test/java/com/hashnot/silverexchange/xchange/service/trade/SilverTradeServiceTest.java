package com.hashnot.silverexchange.xchange.service.trade;

import com.google.common.collect.Lists;
import com.hashnot.silverexchange.Exchange;
import com.hashnot.silverexchange.TestTransactionFactory;
import com.hashnot.silverexchange.test.MockitoExtension;
import com.hashnot.silverexchange.xchange.model.TestModelFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.dto.trade.StopOrder;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.CancelOrderParams;
import org.mockito.Mock;

import java.io.IOException;
import java.util.UUID;

import static com.hashnot.silverexchange.xchange.model.TestModelFactory.*;
import static java.math.BigDecimal.ONE;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SilverTradeServiceTest {

    @Mock
    private Exchange exchange;

    private static TradeService ts(Exchange exchange) {
        return new SilverTradeService(exchange, ID_GEN);
    }

    @Test
    void testPlaceLimitOrder() throws IOException {
        when(exchange.post(any())).thenReturn(null);
        TradeService service = ts(exchange);

        LimitOrder limitOrder = new LimitOrder(Order.OrderType.ASK, ONE, TestModelFactory.PAIR, null, null, ONE);
        String id = service.placeLimitOrder(limitOrder);

        assertEquals(ID_STR, id);
        verify(exchange).post(any());
    }

    @Test
    void testPlaceMarketOrder() throws IOException {
        when(exchange.post(any())).thenReturn(null);
        TradeService service = ts(exchange);

        MarketOrder marketOrder = new MarketOrder(Order.OrderType.ASK, ONE, TestModelFactory.PAIR);
        String id = service.placeMarketOrder(marketOrder);

        assertEquals(ID_STR, id);
        verify(exchange).post(any());
    }

    @Test
    void testGetOpenOrders() throws IOException {
        when(exchange.getAllOffers()).thenReturn(sides(emptyList(), singletonList(bid(ONE, ONE))));
        TradeService service = ts(exchange);

        OpenOrders openOrders = service.getOpenOrders();

        LimitOrder expectedOrder = new LimitOrder.Builder(Order.OrderType.BID, PAIR)
                .id(ID_STR)
                .originalAmount(ONE)
                .limitPrice(ONE)
                .build();
        OpenOrders expected = new OpenOrders(singletonList(expectedOrder));
        assertEquals(expected.getOpenOrders(), openOrders.getOpenOrders());

        assertNull(service.createOpenOrdersParams());
        assertEquals(expected.getOpenOrders(), service.getOpenOrders(service.createOpenOrdersParams()).getOpenOrders());
    }

    @Test
    void testCancelOrderOnEmptyOrderBook() throws IOException {
        when(exchange.getAllOffers()).thenReturn(sides(emptyList(), emptyList()));
        TradeService service = ts(exchange);

        assertFalse(service.cancelOrder(ID_STR));
    }

    @Test
    void testCancelOrderNonMatchingOrder() throws IOException {
        when(exchange.getAllOffers()).thenReturn(sides(emptyList(), singletonList(bid(ONE, ONE))));
        TradeService service = ts(exchange);

        assertFalse(service.cancelOrder(new UUID(0L, 1L).toString()));
        assertFalse(service.getOpenOrders().getOpenOrders().isEmpty());
    }

    @Test
    void testCancelMatchingOrder() throws IOException {
        when(exchange.getAllOffers()).thenReturn(sides(emptyList(), Lists.newArrayList(bid(ONE, ONE))));
        TradeService service = ts(exchange);

        assertFalse(service.getOpenOrders().getOpenOrders().isEmpty());
        assertTrue(service.cancelOrder(ID_STR));
        assertTrue(service.getOpenOrders().getOpenOrders().isEmpty());
    }

    @Test
    void testOverrideId() throws IOException {
        Exchange x = new Exchange(new TestTransactionFactory(CLOCK));
        TradeService service = ts(x);

        String actualId = service.placeLimitOrder(new LimitOrder.Builder(Order.OrderType.ASK, PAIR)
                .originalAmount(ONE)
                .limitPrice(ONE)
                .id("RANDOM")
                .build());

        assertEquals(ID_STR, actualId);

        LimitOrder expectedOrder = new LimitOrder.Builder(Order.OrderType.ASK, PAIR)
                .originalAmount(ONE)
                .limitPrice(ONE)
                .id(ID_STR)
                .build();
        assertEquals(singletonList(expectedOrder), service.getOpenOrders().getOpenOrders());
    }

    @Test
    void testPlaceStopOrderFails() {
        TradeService service = ts(exchange);

        assertThrows(NotAvailableFromExchangeException.class, () -> service.placeStopOrder(new StopOrder.Builder(Order.OrderType.ASK, PAIR).build()));
    }

    @Test
    void testCancelOrderWrongParamsFails() {
        TradeService service = ts(exchange);

        assertThrows(ExchangeException.class, () -> service.cancelOrder(new CancelOrderParams() {
        }));
    }
}
