package com.hashnot.silver.engine;

import java.util.*;

public class SilverEngine {
    private Map<Side, List<Offer>> offers = new EnumMap<>(Side.class);

    {
        offers.put(Side.Ask, new LinkedList<>());
        offers.put(Side.Bid, new LinkedList<>());
    }

    public void execute(Offer o) {
        List<Offer> offers = this.offers.get(o.getSide().opposite());
        while(!offers.isEmpty()){
            Offer first = offers.get(0);
            if(o.match(first));
        }
    }
}
