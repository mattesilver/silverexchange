package com.hashnot.silverexchange;

import com.hashnot.silverexchange.match.Offer;
import com.hashnot.silverexchange.match.Transaction;
import com.hashnot.silverexchange.util.Clock;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Exchange {
    private OrderBook orderBook;
    private List<Transaction> transactions = new LinkedList<>();

    public Exchange(Clock clock) {
        orderBook = new OrderBook(clock);
    }

    /**
     * Execute o as an order. If o is a Market Order it may return remaining part as another Offer object. All executed transactions are added to the internal transactions list.
     *
     * @param o an offer to execute against the order book
     * @return Non-executed part of an offer represented by the parameter
     */
    public Offer post(Offer o) {
        ExecutionResult result = orderBook.post(o);
        this.transactions.addAll(result.transactions);
        return result.remainder;
    }

    public List<Transaction> getAllTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public Collection<Offer> getAllOffers() {
        return orderBook.getAllOffers();
    }
}
