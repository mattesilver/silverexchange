package com.hashnot.silverexchange.xchange.service.marketdata;

import com.hashnot.silverexchange.match.Offer;
import com.hashnot.silverexchange.match.Side;
import com.hashnot.silverexchange.xchange.util.Clock;
import org.knowm.xchange.dto.marketdata.OrderBook;

import java.util.List;
import java.util.Map;

import static com.hashnot.silverexchange.xchange.service.trade.OrderConverter.toOrders;
import static java.util.Date.from;

class OrderBookConverter {
    static OrderBook toOrderBook(Map<Side, List<Offer>> offers, Clock clock) {
        return new OrderBook(
                from(clock.get()),
                toOrders(offers.get(Side.ASK)),
                toOrders(offers.get(Side.BID))
        );
    }
}
