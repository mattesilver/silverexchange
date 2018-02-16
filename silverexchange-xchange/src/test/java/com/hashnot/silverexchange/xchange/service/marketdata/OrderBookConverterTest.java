package com.hashnot.silverexchange.xchange.service.marketdata;

import org.junit.jupiter.api.Test;
import org.knowm.xchange.dto.marketdata.OrderBook;

import static com.hashnot.silverexchange.xchange.model.TestModelFactory.*;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderBookConverterTest {

    @Test
    void testConvertEmptyOrderBook() {
        OrderBook orderBook = OrderBookConverter.toOrderBook(sides(emptyList(), emptyList()), CLOCK);
        assertTrue(orderBook.getAsks().isEmpty());
        assertTrue(orderBook.getBids().isEmpty());
        assertEquals(TS_DATE, orderBook.getTimeStamp());
    }


}
