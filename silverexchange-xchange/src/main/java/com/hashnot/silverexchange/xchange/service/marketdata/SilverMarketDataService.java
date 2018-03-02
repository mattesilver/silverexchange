package com.hashnot.silverexchange.xchange.service.marketdata;

import com.hashnot.silverexchange.Exchange;
import com.hashnot.silverexchange.xchange.model.SilverOrder;
import com.hashnot.silverexchange.xchange.model.SilverTransaction;
import com.hashnot.silverexchange.xchange.util.Clock;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.service.marketdata.MarketDataService;

import static com.hashnot.silverexchange.xchange.service.marketdata.OrderBookConverter.toOrderBook;
import static com.hashnot.silverexchange.xchange.service.marketdata.TickerConverter.toTicker;
import static com.hashnot.silverexchange.xchange.service.marketdata.TransactionConverter.toTrades;

public class SilverMarketDataService implements MarketDataService {
    final private Exchange<SilverTransaction, SilverOrder> exchange;
    final private Clock clock;

    public SilverMarketDataService(Exchange<SilverTransaction, SilverOrder> exchange, Clock clock) {
        this.exchange = exchange;
        this.clock = clock;
    }

    @Override
    public Ticker getTicker(CurrencyPair currencyPair, Object... args) {
        return toTicker(exchange.getAllOffers(), exchange.getAllTransactions(), clock);
    }

    @Override
    public OrderBook getOrderBook(CurrencyPair currencyPair, Object... args) {
        return toOrderBook(exchange.getAllOffers(), clock);
    }

    @Override
    public Trades getTrades(CurrencyPair currencyPair, Object... args) {
        return toTrades(exchange.getAllTransactions());
    }
}
