package com.hashnot.silverexchange;

import com.hashnot.silverexchange.ext.ITransactionFactory;
import com.hashnot.silverexchange.match.Offer;
import com.hashnot.silverexchange.match.Side;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Exchange<TransactionT extends Transaction, OfferT extends Offer> {
    private OrderBook<OfferT> orderBook;
    private List<TransactionT> transactions = new LinkedList<>();
    private ITransactionFactory<OfferT, TransactionT> transactionFactory;

    public static Exchange<Transaction, Offer> create() {
        return new Exchange<>(Exchange::createTransaction);
    }

    public Exchange(ITransactionFactory<OfferT, TransactionT> transactionFactory) {
        this.transactionFactory = transactionFactory;
        orderBook = new OrderBook<>(this::transactionHandler);
    }

    /**
     * Execute o as an order. If o is a Market Order it may return remaining part as another Offer object. All executed transactions are added to the internal transactions list.
     *
     * @param o an offer to execute against the order book
     * @return Non-executed part of an offer represented by the parameter
     */
    public Offer post(OfferT o) {
        return orderBook.post(o);
    }

    public List<TransactionT> getAllTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public Map<Side, List<OfferT>> getAllOffers() {
        return orderBook.getAllOffers();
    }

    private void transactionHandler(BigDecimal amount, BigDecimal rate, OfferT offer) {
        assert amount != null;
        assert rate != null;
        assert offer != null;

        transactions.add(transactionFactory.create(amount, rate, offer));
    }

    @SuppressWarnings("unchecked")
    private static Transaction createTransaction(BigDecimal amount, BigDecimal rate, Offer offer) {
        assert amount != null;
        assert rate != null;
        assert offer != null;

        return new Transaction(amount, new TransactionRate(rate));
    }

}
