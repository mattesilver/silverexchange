package com.hashnot.silverexchange;

import com.hashnot.silverexchange.match.Offer;
import com.hashnot.silverexchange.match.Side;
import com.hashnot.silverexchange.match.Transaction;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Exchange {
    private OrderBook orderBook;
    private List<Transaction> transactions = new LinkedList<>();

    public Exchange(ITransactionFactory txFactory) {
        orderBook = new OrderBook(txFactory);
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

    public Map<Side, List<Offer>> getAllOffers() {
        return orderBook.getAllOffers();
    }
}
