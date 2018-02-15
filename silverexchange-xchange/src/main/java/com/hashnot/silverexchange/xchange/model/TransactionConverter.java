package com.hashnot.silverexchange.xchange.model;

import com.hashnot.silverexchange.match.Transaction;
import org.knowm.xchange.dto.trade.UserTrade;

import java.util.Date;

public class TransactionConverter {
    public static UserTrade fromTransaction(Transaction tx) {
        assert tx != null;

        return new UserTrade.Builder()
                .price(tx.getRate().getValue())
                .timestamp(Date.from(tx.getTimestamp()))
                .originalAmount(tx.getAmount())
                .build();
    }
}
