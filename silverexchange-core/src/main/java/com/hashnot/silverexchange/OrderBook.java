package com.hashnot.silverexchange;

import com.hashnot.silverexchange.match.ITransactionListener;
import com.hashnot.silverexchange.match.Offer;
import com.hashnot.silverexchange.match.OfferExecutionResult;
import com.hashnot.silverexchange.match.Side;
import com.hashnot.silverexchange.util.Lists;

import java.util.*;

public class OrderBook<OfferT extends Offer> {
    private final ITransactionListener<OfferT> transactionListener;
    private final Map<Side, List<OfferT>> allOffers;

    OrderBook(ITransactionListener<OfferT> transactionListener) {
        assert transactionListener != null;

        this.transactionListener = transactionListener;

        allOffers = new EnumMap<>(Side.class);
        // these lists will be more or less as frequently searched as inserted to, use LinkedList for now.
        allOffers.put(Side.ASK, new LinkedList<>());
        allOffers.put(Side.BID, new LinkedList<>());
    }

    public OfferT post(OfferT o) {
        assert o != null;

        List<OfferT> otherSideOffers = allOffers.get(o.getSide().reverse());
        if (otherSideOffers.isEmpty()) {
            if (o.isMarketOrder()) {
                return o;
            } else {
                insert(o);
                return null;
            }
        } else {
            return execute(o, otherSideOffers);
        }
    }

    private OfferT execute(OfferT active, List<OfferT> passiveOffers) {
        assert active != null;
        assert passiveOffers != null;
        assert !passiveOffers.isEmpty();
        assert passiveOffers.get(0).getSide() != active.getSide();

        OfferT currentActive = active;
        do {
            OfferT passive = passiveOffers.get(0);
            OfferExecutionResult<OfferT> execResult = currentActive.execute(passive, transactionListener);

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

        return currentActive;
    }

    private void insert(OfferT o) {
        assert o != null;

        List<OfferT> offers = this.allOffers.get(o.getSide());
        int index = Collections.binarySearch(offers, o, Offer::compareByRate);
        if (index < 0)
            offers.add(-index - 1, o);
        else {
            // Offer with the same rate as offers already present in the order book, should always be placed after the all existing ones
            // Consider forward search starting from the result of binarySearch, but for now, avoid premature optimisation
            index = Lists.lastIndexOf(offers, o, OfferT::getRate);
            offers.add(index + 1, o);
        }
    }

    /**
     * @return All offers, bids ordered by price in descending order, then asks ordered ascending
     */
    public Map<Side, List<OfferT>> getAllOffers() {
        return allOffers;
    }

    /**
     * @return true if this order book contains no passive offers
     */
    public boolean isEmpty() {
        return isEmpty(allOffers);
    }

    static <OfferT extends Offer> boolean isEmpty(Map<Side, List<OfferT>> orderBook) {
        return orderBook.values().stream().allMatch(List::isEmpty);
    }
}
