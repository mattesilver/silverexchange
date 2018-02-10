package com.hashnot.silverexchange;

import com.hashnot.silverexchange.match.Offer;
import com.hashnot.silverexchange.match.Transaction;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Exchange {
    private OrderBook orderBook = new OrderBook();
    private List<Transaction> transactions = new LinkedList<>();

    public void post(Offer o) {
        ExecutionResult result = orderBook.post(o);
        this.transactions.addAll(result.transactions);
    }

    public List<Transaction> getAllTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public Collection<Offer> getAllOffers() {
        return orderBook.getAllOffers();
    }
}
