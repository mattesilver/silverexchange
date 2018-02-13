package com.hashnot.silverexchange;

import com.hashnot.silverexchange.match.Offer;
import com.hashnot.silverexchange.match.OfferExecutionResult;
import com.hashnot.silverexchange.match.Side;
import com.hashnot.silverexchange.match.Transaction;
import com.hashnot.silverexchange.util.Lists;

import java.util.*;

import static java.util.Collections.emptyList;

public class OrderBook {
    private final ITransactionFactory transactionFactory;
    private final Map<Side, List<Offer>> orderBook = new EnumMap<>(Side.class);

    {
        // these lists will be more or less as frequently searched as inserted to, use LinkedList for now.
        orderBook.put(Side.Ask, new LinkedList<>());
        orderBook.put(Side.Bid, new LinkedList<>());
    }

    OrderBook(ITransactionFactory transactionFactory) {
        this.transactionFactory = transactionFactory;
    }

    public ExecutionResult post(Offer o) {
        List<Offer> otherSideOffers = orderBook.get(o.getSide().reverse());
        if (otherSideOffers.isEmpty()) {
            if (o.isMarketOrder()) {
                return new ExecutionResult(emptyList(), o);
            } else {
                insert(o);
                return ExecutionResult.empty();
            }
        } else {
            return execute(o, otherSideOffers);
        }
    }

    private ExecutionResult execute(Offer active, List<Offer> passiveOffers) {
        assert !passiveOffers.isEmpty();
        assert passiveOffers.get(0).getSide() != active.getSide();
        assert passiveOffers.get(0).getPair().equals(active.getPair());

        List<Transaction> transactions = new LinkedList<>();

        Offer currentActive = active;
        do {
            Offer passive = passiveOffers.get(0);
            OfferExecutionResult execResult = currentActive.execute(passive, transactionFactory);
            if (execResult.transaction != null)
                transactions.add(execResult.transaction);

            currentActive = execResult.remainder;

            if (execResult.passiveRemainder != null) {
                passiveOffers.set(0, execResult.passiveRemainder);
                break;
            } else {
                passiveOffers.remove(0);
            }

        } while (!passiveOffers.isEmpty() && currentActive != null);

        if (currentActive != null && !currentActive.isMarketOrder()) {
            insert(currentActive);
            currentActive = null;
        }

        return new ExecutionResult(transactions, currentActive);
    }

    private void insert(Offer o) {
        List<Offer> offers = orderBook.get(o.getSide());
        int index = Collections.binarySearch(offers, o, Offer.COMPARATOR_BY_RATE);
        if (index < 0)
            offers.add(-index - 1, o);
        else {
            // Offer with the same rate as offers already present in the order book, should always be placed after the all existing ones
            // Consider forward search starting from the result of binarySearch, but for now, avoid premature optimisation
            index = Lists.lastIndexOf(offers, o, Offer::getRate);
            offers.add(index + 1, o);
        }
    }


    /**
     * @return All offers, bids ordered by price in descending order, then asks ordered ascending
     */
    public Collection<Offer> getAllOffers() {
        /*
        Could be made faster by concatenating the lists instead of copying the elements, but avoid premature optimisation
         */
        List<Offer> asks = orderBook.get(Side.Ask);
        List<Offer> bids = orderBook.get(Side.Bid);

        List<Offer> all = new ArrayList<>(asks.size() + bids.size());
        all.addAll(bids);
        Collections.reverse(all);
        all.addAll(asks);
        return all;
    }

}
