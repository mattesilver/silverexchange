package com.hashnot.silverexchange.xchange.service.marketdata;

import com.hashnot.silverexchange.OfferRate;
import com.hashnot.silverexchange.match.Offer;
import com.hashnot.silverexchange.match.Side;
import com.hashnot.silverexchange.util.Clock;
import org.knowm.xchange.dto.marketdata.Ticker;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class TickerConverter {
    static Ticker toTicker(Map<Side, List<Offer>> offers, Clock clock) {
        Ticker.Builder builder = new Ticker.Builder();

        extract(offers.get(Side.Ask), builder::ask);
        extract(offers.get(Side.Bid), builder::bid);

        builder.timestamp(Date.from(clock.get()));

        return builder.build();
    }

    private static void extract(Iterable<Offer> offers, Consumer<BigDecimal> consumer) {
        Iterator<Offer> i = offers.iterator();
        if (i.hasNext()) {
            OfferRate rate = i.next().getRate();
            assert !rate.isMarket();
            consumer.accept(rate.getValue());
        }
    }
}
