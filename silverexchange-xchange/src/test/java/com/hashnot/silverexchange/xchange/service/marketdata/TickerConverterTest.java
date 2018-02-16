package com.hashnot.silverexchange.xchange.service.marketdata;

import com.hashnot.silverexchange.match.Transaction;
import com.hashnot.silverexchange.test.MockitoExtension;
import com.hashnot.silverexchange.xchange.util.Clock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.mockito.Mock;

import java.sql.Date;
import java.time.Instant;
import java.util.List;

import static com.hashnot.silverexchange.xchange.model.TestModelFactory.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TickerConverterTest {

    @Mock
    private Clock clock;

    @Test
    void testToTickerEmpty() {
        Instant ts = Instant.ofEpochMilli(Integer.MAX_VALUE);
        when(clock.get()).thenReturn(ts);

        Ticker ticker = TickerConverter.toTicker(sides(emptyList(), emptyList()), emptyList(), clock);

        assertNull(ticker.getAsk());
        assertNull(ticker.getBid());
        assertEquals(Date.from(ts), ticker.getTimestamp());
        assertNull(ticker.getCurrencyPair());
        assertNotNull(ticker.toString());
        assertNull(ticker.getHigh());
        assertNull(ticker.getLow());
        assertNull(ticker.getOpen());
        assertNull(ticker.getLast());
    }

    @Test
    void testToTickerValues() {
        Instant ts = Instant.ofEpochMilli(Integer.MAX_VALUE);
        when(clock.get()).thenReturn(ts);

        List<Transaction> transactions = singletonList(tx(ONE, THREE));

        Ticker t = TickerConverter.toTicker(sides(singletonList(bid(ONE, ONE)), singletonList(ask(ONE, TWO))), transactions, clock);

        assertEquals(Date.from(ts), t.getTimestamp());
        assertEquals(TWO, t.getAsk());
        assertEquals(ONE, t.getBid());
        assertEquals(THREE, t.getLast());
        assertNotNull(t.toString());
        assertNull(t.getCurrencyPair());
        assertNull(t.getHigh());
        assertNull(t.getLow());
        assertNull(t.getOpen());

    }
}