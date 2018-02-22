package com.hashnot.silverexchange.xchange.service.trade;

import com.hashnot.silverexchange.OfferRate;
import com.hashnot.silverexchange.match.Offer;
import com.hashnot.silverexchange.match.Side;
import com.hashnot.silverexchange.xchange.model.SilverOrder;
import com.hashnot.silverexchange.xchange.service.IIdGenerator;
import com.hashnot.silverexchange.xchange.util.Clock;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.dto.trade.OpenOrders;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrderConverter {
    static OpenOrders toOpenOrders(Map<Side, List<Offer>> book) {
        return new OpenOrders(
                Stream.concat(
                        book.get(Side.BID).stream(),
                        book.get(Side.ASK).stream()
                )
                        .map(OrderConverter::toLimitOrder)
                        .collect(Collectors.toList())
        );
    }


    public static LimitOrder toLimitOrder(Offer offer) {
        SilverOrder order = (SilverOrder) offer;

        OrderType orderType = fromSide(offer.getSide());
        CurrencyPair pair = order.getPair();

        return
                new LimitOrder.Builder(orderType, pair)
                        .id(order.getId().toString())
                        .limitPrice(order.getRate().getValue())
                        .originalAmount(order.getAmount())
                        .timestamp(Date.from(order.getTimestamp()))
                        .build();
    }

    static SilverOrder fromLimitOrder(LimitOrder limitOrder, IIdGenerator idGenerator, Clock clock) {
        return new SilverOrder(
                idGenerator.get(), limitOrder.getCurrencyPair(),
                toSide(limitOrder.getType()),
                limitOrder.getOriginalAmount(),
                new OfferRate(limitOrder.getLimitPrice()),
                clock.get()
        );
    }

    static SilverOrder fromMarketOrder(MarketOrder marketOrder, IIdGenerator idGenerator, Clock clock) {
        return new SilverOrder(
                idGenerator.get(), marketOrder.getCurrencyPair(),
                toSide(marketOrder.getType()),
                marketOrder.getOriginalAmount(),
                OfferRate.market(),
                clock.get()
        );
    }

    private static OrderType fromSide(Side side) {
        return OrderType.valueOf(side.name().toUpperCase());
    }

    private static Side toSide(OrderType type) {
        switch (type) {
            case ASK:
                return Side.ASK;
            case BID:
                return Side.BID;
            default:
                throw new IllegalArgumentException(type.name());
        }
    }


    public static List<LimitOrder> toOrders(List<Offer> offers) {
        return offers.stream()
                .map(OrderConverter::toLimitOrder)
                .collect(Collectors.toList())
                ;
    }
}
