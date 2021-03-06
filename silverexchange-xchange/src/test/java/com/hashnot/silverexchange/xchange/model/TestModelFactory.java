package com.hashnot.silverexchange.xchange.model;

import com.hashnot.silverexchange.OfferRate;
import com.hashnot.silverexchange.TransactionRate;
import com.hashnot.silverexchange.match.Side;
import com.hashnot.silverexchange.xchange.service.IIdGenerator;
import com.hashnot.silverexchange.xchange.util.Clock;
import org.knowm.xchange.currency.CurrencyPair;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

public class TestModelFactory {
    public static final BigDecimal ONE = BigDecimal.ONE;
    public static final BigDecimal TWO = new BigDecimal(2);
    public static final BigDecimal THREE = new BigDecimal(3);

    public static final Instant TS = Instant.ofEpochMilli(0);
    public static final Date TS_DATE = Date.from(TS);
    public static final Clock CLOCK = () -> TS;

    public static final UUID ID = new UUID(0L, 0L);
    public static final IIdGenerator ID_GEN = () -> ID;
    public static final String ID_STR = ID.toString();

    public static final CurrencyPair PAIR = CurrencyPair.BTC_EUR;

    public static Map<Side, List<SilverOrder>> sides(List<SilverOrder> bids, List<SilverOrder> asks) {
        Map<Side, List<SilverOrder>> result = new EnumMap<>(Side.class);
        result.put(Side.BID, bids);
        result.put(Side.ASK, asks);

        return result;
    }

    public static SilverOrder bid(BigDecimal amount, BigDecimal rate) {
        return new SilverOrder(ID, PAIR, Side.BID, amount, new OfferRate(rate), TS);
    }

    public static SilverOrder ask(BigDecimal amount, BigDecimal rate) {
        return new SilverOrder(ID, PAIR, Side.ASK, amount, new OfferRate(rate), TS);
    }

    public static SilverTransaction tx(BigDecimal amount, BigDecimal rate) {
        return new SilverTransaction(ID, amount, new TransactionRate(rate), TS);
    }
}
