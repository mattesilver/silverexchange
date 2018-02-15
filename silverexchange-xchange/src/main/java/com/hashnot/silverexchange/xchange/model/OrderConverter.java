package com.hashnot.silverexchange.xchange.model;

import com.hashnot.silverexchange.OfferRate;
import com.hashnot.silverexchange.match.Offer;
import com.hashnot.silverexchange.match.Side;
import com.hashnot.silverexchange.xchange.service.IIdGenerator;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order.OrderType;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.MarketOrder;
import org.knowm.xchange.dto.trade.OpenOrders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderConverter {
    public static OpenOrders toOpenOrders(Map<Side, List<Offer>> book) {
        int size = book.values().stream().mapToInt(List::size).sum();
        List<LimitOrder> result = new ArrayList<>(size);

        book.get(Side.Bid).stream().map(OrderConverter::toLimitOrder).forEachOrdered(result::add);
        book.get(Side.Ask).stream().map(OrderConverter::toLimitOrder).forEachOrdered(result::add);
        return new OpenOrders(result);
    }

    public static LimitOrder toLimitOrder(Offer offer) {
        SilverExchangeOrder order = (SilverExchangeOrder) offer;

        OrderType orderType = fromSide(offer.getSide());
        CurrencyPair pair = (CurrencyPair) offer.getPair();

        return
                new LimitOrder.Builder(orderType, pair)
                        .id(order.getId().toString())
                        .limitPrice(order.getRate().getValue())
                        .originalAmount(order.getAmount())
                        .build();

    }


    public static SilverExchangeOrder toOrder(LimitOrder limitOrder, IIdGenerator idGenerator) {
        return new SilverExchangeOrder(
                limitOrder.getCurrencyPair(),
                toSide(limitOrder.getType()),
                limitOrder.getOriginalAmount(),
                new OfferRate(limitOrder.getLimitPrice()),
                idGenerator.get()
        );
    }

    public static SilverExchangeOrder toOrder(MarketOrder marketOrder, IIdGenerator idGenerator) {
        return new SilverExchangeOrder(
                marketOrder.getCurrencyPair(),
                toSide(marketOrder.getType()),
                marketOrder.getOriginalAmount(),
                OfferRate.market(),
                idGenerator.get()
        );
    }

    private static OrderType fromSide(Side side) {
        return OrderType.valueOf(side.name().toUpperCase());
    }

    private static Side toSide(OrderType type) {
        switch (type) {
            case ASK:
                return Side.Ask;
            case BID:
                return Side.Bid;
            default:
                throw new IllegalArgumentException(type.name());
        }
    }


}
