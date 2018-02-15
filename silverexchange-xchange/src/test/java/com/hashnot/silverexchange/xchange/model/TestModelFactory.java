package com.hashnot.silverexchange.xchange.model;

import com.hashnot.silverexchange.OfferRate;
import com.hashnot.silverexchange.match.Offer;
import com.hashnot.silverexchange.match.Side;
import com.hashnot.silverexchange.util.Clock;
import com.hashnot.silverexchange.xchange.service.IIdGenerator;
import org.knowm.xchange.currency.CurrencyPair;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TestModelFactory {


    private static final Instant TS = Instant.ofEpochMilli(0);
    public static final Clock CLOCK = () -> TS;

    private static final UUID ID = new UUID(0L, 0L);
    public static final IIdGenerator IDGEN = () -> ID;
    public static final String ID_STR = ID.toString();

    public static final CurrencyPair PAIR = CurrencyPair.BTC_EUR;

    public static Map<Side, List<Offer>> sides(List<SilverExchangeOrder> bids, List<SilverExchangeOrder> asks) {
        Map<Side, List<SilverExchangeOrder>> result = new EnumMap<>(Side.class);
        result.put(Side.Bid, bids);
        result.put(Side.Ask, asks);

        return (Map<Side, List<Offer>>) (Map) result;
    }

    public static SilverExchangeOrder bid(BigDecimal amount, BigDecimal rate) {
        return new SilverExchangeOrder(PAIR, Side.Bid, amount, new OfferRate(rate), ID);
    }


}
