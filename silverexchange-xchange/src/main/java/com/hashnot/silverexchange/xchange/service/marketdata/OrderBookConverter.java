package com.hashnot.silverexchange.xchange.service.marketdata;

import com.hashnot.silverexchange.ext.Clock;
import com.hashnot.silverexchange.match.Offer;
import com.hashnot.silverexchange.match.Side;
import org.knowm.xchange.dto.marketdata.OrderBook;

import java.util.List;
import java.util.Map;

import static com.hashnot.silverexchange.xchange.service.trade.OrderConverter.toOrders;
import static java.util.Date.from;

class OrderBookConverter {
    static OrderBook toOrderBook(Map<Side, List<Offer>> offers, Clock clock) {
        return new OrderBook(
                from(clock.get()),
                toOrders(offers.get(Side.Ask)),
                toOrders(offers.get(Side.Bid))
        );
    }
}
