package com.hashnot.silver.exchange;

import com.hashnot.silver.engine.ExecutionResult;
import com.hashnot.silver.engine.Offer;
import com.hashnot.silver.engine.Side;
import com.hashnot.silver.engine.Transaction;

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
        Side otherSide = o.getSide() == Side.Ask ? Side.Bid : Side.Ask;
        List<Offer> otherSideOffers = orderBook.get(otherSide);
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
            if (execResult.executed != null)
                result.add(execResult.executed);

            handled = execResult.reminder;

            if (execResult.againstReminder != null) {
                otherOffers.set(0, execResult.againstReminder);
                break;
            } else {
                otherOffers.remove(0);
            }

        } while (!otherOffers.isEmpty() && handled != null);

        return result;
    }

    private void insert(Offer o) {
        List<Offer> offers = orderBook.get(o.getSide());
        int index = Collections.binarySearch(offers, o, ((a, b) -> a.getRate().compareTo(b.getRate()) * a.getSide().orderSignum));
        if (index < 0)
            offers.add(-index - 1, o);
        else
            offers.add(index + 1, o);
    }


}
