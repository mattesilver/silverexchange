package com.hashnot.silver.exchange;

import com.hashnot.silver.engine.ExecutionResult;
import com.hashnot.silver.engine.Offer;
import com.hashnot.silver.engine.Side;
import com.hashnot.silver.engine.Transaction;
import com.hashnot.silver.exchange.util.Lists;

import java.util.*;

import static java.util.Collections.emptyList;

public class OrderBook {
    private Map<Side, List<Offer>> orderBook = new EnumMap<>(Side.class);

    {
        // these lists will be more or less as frequently searched as inserted to, use LinkedList for now.
        orderBook.put(Side.Ask, new LinkedList<>());
        orderBook.put(Side.Bid, new LinkedList<>());
    }

    public List<Transaction> post(Offer o) {
        List<Offer> otherSideOffers = orderBook.get(o.getSide().reverse());
        if (otherSideOffers.isEmpty()) {
            insert(o);
            return emptyList();
        } else {
            return execute(o, otherSideOffers);
        }
    }

    private List<Transaction> execute(Offer offer, List<Offer> otherOffers) {
        assert !otherOffers.isEmpty();
        assert otherOffers.get(0).getSide() != offer.getSide();
        assert otherOffers.get(0).getPair().equals(offer.getPair());

        List<Transaction> result = new LinkedList<>();

        Offer handled = offer;
        do {
            Offer against = otherOffers.get(0);
            ExecutionResult execResult = handled.execute(against);
            if (execResult.transaction != null)
                result.add(execResult.transaction);

            handled = execResult.remainder;

            if (execResult.againstRemainder != null) {
                otherOffers.set(0, execResult.againstRemainder);
                break;
            } else {
                otherOffers.remove(0);
            }

        } while (!otherOffers.isEmpty() && handled != null);

        if (handled != null)
            insert(handled);

        return result;
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
