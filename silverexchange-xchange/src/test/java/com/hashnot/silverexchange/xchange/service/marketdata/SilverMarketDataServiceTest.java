package com.hashnot.silverexchange.xchange.service.marketdata;

import com.hashnot.silverexchange.Exchange;
import com.hashnot.silverexchange.TestModelFactory;
import com.hashnot.silverexchange.match.Offer;
import com.hashnot.silverexchange.test.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.mockito.Mock;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import static com.hashnot.silverexchange.xchange.model.TestModelFactory.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SilverMarketDataServiceTest {
    @Mock
    private Exchange exchange;

    @Test
    void testGetTradesEmpyu() throws IOException {
        MarketDataService service = service(exchange);

        Trades trades = service.getTrades(null);
        assertNotNull(trades);
        assertTrue(trades.getTrades().isEmpty());
    }

    @Test
    void testGetTrades() throws IOException {
        when(exchange.getAllTransactions()).thenReturn(singletonList(tx(ONE, ONE)));
        MarketDataService service = service(exchange);

        Trades trades = service.getTrades(null);
        assertNotNull(trades);
        assertFalse(trades.getTrades().isEmpty());

        assertEquals(Trades.TradeSortType.SortByTimestamp, trades.getTradeSortType());
        assertEquals(0, trades.getlastID());
        Trade expectedTrade = new Trade.Builder()
                .originalAmount(ONE)
                .timestamp(Date.from(TS))
                .price(ONE)
                .id(ID_STR)
                .build();
        assertEquals(singletonList(expectedTrade), trades.getTrades());
    }

    @Test
    void testGetOrderBook() throws IOException {
        Offer o = bid(ONE, ONE);
        when(exchange.getAllOffers()).thenReturn(TestModelFactory.sides(singletonList(o), emptyList()));

        MarketDataService service = service(exchange);

        OrderBook orderBook = service.getOrderBook(null);

        verify(exchange).getAllOffers();

        assertEquals(emptyList(), orderBook.getAsks());

        List<LimitOrder> bids = orderBook.getBids();
        assertEquals(1, bids.size());

        LimitOrder bid = bids.get(0);
        assertEquals(ONE, bid.getLimitPrice());
        assertEquals(PAIR, bid.getCurrencyPair());
        assertEquals(ID_STR, bid.getId());
//        assertEquals(TS_DATE, bid.getTimestamp());
        assertEquals(ONE, bid.getOriginalAmount());
    }

    private static MarketDataService service(Exchange exchange) {
        return new SilverMarketDataService(exchange, CLOCK);
    }
}
