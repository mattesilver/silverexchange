package com.hashnot.silverexchange.xchange.service.trade;

import com.hashnot.silverexchange.Exchange;
import com.hashnot.silverexchange.match.Offer;
import com.hashnot.silverexchange.xchange.model.OrderConverter;
import com.hashnot.silverexchange.xchange.model.SilverExchangeOrder;
import com.hashnot.silverexchange.xchange.service.IIdGenerator;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.*;
import org.knowm.xchange.exceptions.ExchangeException;
import org.knowm.xchange.exceptions.NotAvailableFromExchangeException;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.CancelOrderByIdParams;
import org.knowm.xchange.service.trade.params.CancelOrderParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.orders.OpenOrdersParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import static com.hashnot.silverexchange.xchange.model.OrderConverter.toOpenOrders;
import static com.hashnot.silverexchange.xchange.model.OrderConverter.toOrder;

public class SilverTradeService implements TradeService {
    final private static Logger log = LoggerFactory.getLogger(SilverTradeService.class);

    final private Exchange exchange;
    final private IIdGenerator idGenerator;

    public SilverTradeService(Exchange exchange, IIdGenerator idGenerator) {
        this.exchange = exchange;
        this.idGenerator = idGenerator;
    }

    @Override
    public OpenOrders getOpenOrders() {
        return toOpenOrders(exchange.getAllOffers());
    }

    @Override
    public OpenOrders getOpenOrders(OpenOrdersParams $) {
        return getOpenOrders();
    }

    @Override
    public String placeLimitOrder(LimitOrder limitOrder) {
        SilverExchangeOrder order = toOrder(limitOrder, idGenerator);
        Offer remainder = exchange.post(order);
        assert remainder == null : "Remainder from posting market order";
        return order.getId().toString();
    }

    @Override
    public String placeMarketOrder(MarketOrder marketOrder) {
        SilverExchangeOrder order = toOrder(marketOrder, idGenerator);
        Offer remainder = exchange.post(order);
        if (remainder != null)
            log.warn("Unhandled remainder from executing market order {}", remainder);
        return order.getId().toString();
    }

    @Override
    public String placeStopOrder(StopOrder stopOrder) {
        throw new NotAvailableFromExchangeException();
    }

    public static class SilverCancelOrderParams implements CancelOrderByIdParams {
        final private String orderId;

        SilverCancelOrderParams(String orderId) {
            this.orderId = orderId;
        }

        @Override
        public String getOrderId() {
            return orderId;
        }
    }

    @Override
    public boolean cancelOrder(String orderId) {
        return cancelOrder(new SilverCancelOrderParams(orderId));
    }

    @Override
    public boolean cancelOrder(CancelOrderParams orderParams) {
        UUID id;
        try {
            CancelOrderByIdParams byIdParams = (CancelOrderByIdParams) orderParams;
            id = UUID.fromString(byIdParams.getOrderId());
        } catch (Exception e) {
            throw new ExchangeException(e);
        }

        for (List<Offer> offers : exchange.getAllOffers().values()) {
            for (Iterator<Offer> iterator = offers.iterator(); iterator.hasNext(); ) {
                Offer offer = iterator.next();
                SilverExchangeOrder order = (SilverExchangeOrder) offer;
                if (order.getId().equals(id)) {
                    iterator.remove();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public UserTrades getTradeHistory(TradeHistoryParams params) {
        throw new NotAvailableFromExchangeException();
    }

    @Override
    public TradeHistoryParams createTradeHistoryParams() {
        return null;
    }

    @Override
    public OpenOrdersParams createOpenOrdersParams() {
        return null;
    }

    @Override
    public void verifyOrder(LimitOrder limitOrder) {
        if (limitOrder.getLimitPrice() == null)
            throw new IllegalArgumentException("No Limit Price");
        else if (limitOrder.getOriginalAmount() == null)
            throw new IllegalArgumentException("No Amount");
    }

    @Override
    public void verifyOrder(MarketOrder marketOrder) {
        if (marketOrder.getOriginalAmount() == null)
            throw new IllegalArgumentException("No Amount");
    }

    @Override
    public Collection<Order> getOrder(String... orderIds) {
        return exchange.getAllOffers().values().stream().flatMap(List::stream)
                .filter(o -> match(o, orderIds))
                .map(OrderConverter::toLimitOrder)
                .collect(Collectors.toList());
    }

    private static boolean match(Offer o, String[] ids) {
        return Arrays.asList(ids).contains(((SilverExchangeOrder) o).getId().toString());
    }
}
