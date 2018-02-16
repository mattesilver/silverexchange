package com.hashnot.silverexchange.xchange.service.marketdata;

import com.hashnot.silverexchange.match.Transaction;
import com.hashnot.silverexchange.xchange.model.SilverTransaction;
import org.knowm.xchange.dto.marketdata.Trade;
import org.knowm.xchange.dto.marketdata.Trades;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Date.from;

public class TransactionConverter {
    public static Trade toTrade(Transaction transaction) {
        assert transaction != null;
        assert transaction instanceof SilverTransaction;

        SilverTransaction tx = (SilverTransaction) transaction;

        return new Trade.Builder()
                .price(tx.getRate().getValue())
                .timestamp(from(tx.getTimestamp()))
                .originalAmount(tx.getAmount())
                .id(tx.getId().toString())
                .build();
    }

    static Trades toTrades(List<Transaction> transactions) {
        List<Trade> trades = transactions.stream()
                .map(TransactionConverter::toTrade)
                .collect(Collectors.toList());
        return new Trades(trades, Trades.TradeSortType.SortByTimestamp);
    }
}
