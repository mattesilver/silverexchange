package com.hashnot.silverexchange.xchange.service.marketdata;

import com.hashnot.silverexchange.match.Offer;
import com.hashnot.silverexchange.match.Side;
import com.hashnot.silverexchange.match.Transaction;
import com.hashnot.silverexchange.xchange.util.Clock;
import org.knowm.xchange.dto.marketdata.Ticker;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class TickerConverter {
    static Ticker toTicker(Map<Side, List<Offer>> offers, List<Transaction> transactions, Clock clock) {
        Ticker.Builder builder = new Ticker.Builder();

        consumeFirst(offers, Side.ASK, builder::ask);
        consumeFirst(offers, Side.BID, builder::bid);
        consumeFirst(transactions, combine(t -> t.getRate().getValue(), builder::last));
        builder.timestamp(Date.from(clock.get()));

        return builder.build();
    }

    private static void consumeFirst(Map<Side, List<Offer>> offers, Side side, Consumer<BigDecimal> consumer) {
        consumeFirst(offers.get(side), combine(o -> o.getRate().getValue(), consumer));
    }

    private static <E> void consumeFirst(Collection<E> collection, Consumer<E> consumer) {
        collection.stream().findFirst().ifPresent(consumer);
    }

    private static <I, O> Consumer<I> combine(Function<I, O> fn, Consumer<O> c) {
        return e -> c.accept(fn.apply(e));
    }
}
