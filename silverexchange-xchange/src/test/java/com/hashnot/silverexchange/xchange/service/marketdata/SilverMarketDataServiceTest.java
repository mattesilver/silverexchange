package com.hashnot.silverexchange.xchange.service.marketdata;

import com.hashnot.silverexchange.Exchange;
import com.hashnot.silverexchange.test.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.mockito.Mock;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class SilverMarketDataServiceTest {
    @Mock
    private Exchange exchange;

    @Test
    void testGetTrades() throws IOException {
        MarketDataService service = service(exchange);

        Trades trades = service.getTrades(null);
        assertNotNull(trades);
        assertTrue(trades.getTrades().isEmpty());
    }

    private static MarketDataService service(Exchange exchange) {
        return new SilverMarketDataService(exchange);
    }
}
