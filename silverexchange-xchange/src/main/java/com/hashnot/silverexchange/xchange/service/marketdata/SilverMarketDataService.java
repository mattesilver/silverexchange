package com.hashnot.silverexchange.xchange.service.marketdata;

import com.hashnot.silverexchange.Exchange;
import com.hashnot.silverexchange.util.Clock;
import com.hashnot.silverexchange.xchange.model.TransactionConverter;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.dto.marketdata.Trades;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.service.marketdata.MarketDataService;

import java.util.List;
import java.util.stream.Collectors;

public class SilverMarketDataService implements MarketDataService {
    final private Exchange exchange;
    final private Clock clock;

    public SilverMarketDataService(Exchange exchange, Clock clock) {
        this.exchange = exchange;
        this.clock = clock;
    }

    @Override
    public Ticker getTicker(CurrencyPair currencyPair, Object... args) {
        return TickerConverter.toTicker(exchange.getAllOffers(), clock);
    }

    @Override
    public OrderBook getOrderBook(CurrencyPair currencyPair, Object... args) {
        throw new NotYetImplementedForExchangeException();
    }

    @Override
    public Trades getTrades(CurrencyPair currencyPair, Object... args) {
        List<Trade> trades = exchange.getAllTransactions().stream()
                .map(TransactionConverter::fromTransaction)
                .collect(Collectors.toList());
        return new Trades(trades, Trades.TradeSortType.SortByTimestamp);
    }
}
