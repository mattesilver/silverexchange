package com.hashnot.silverexchange.xchange.service.trade;

import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.*;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.service.trade.TradeService;
import org.knowm.xchange.service.trade.params.CancelOrderParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.orders.OpenOrdersParams;

import java.util.Collection;

public class SilverTradeService implements TradeService {
    @Override
    public OpenOrders getOpenOrders() {
        throw new NotYetImplementedForExchangeException();
    }

    @Override
    public OpenOrders getOpenOrders(OpenOrdersParams params) {
        throw new NotYetImplementedForExchangeException();
    }

    @Override
    public String placeMarketOrder(MarketOrder marketOrder) {
        throw new NotYetImplementedForExchangeException();
    }

    @Override
    public String placeLimitOrder(LimitOrder limitOrder) {
        throw new NotYetImplementedForExchangeException();
    }

    @Override
    public String placeStopOrder(StopOrder stopOrder) {
        throw new NotYetImplementedForExchangeException();
    }

    @Override
    public boolean cancelOrder(String orderId) {
        throw new NotYetImplementedForExchangeException();
    }

    @Override
    public boolean cancelOrder(CancelOrderParams orderParams) {
        throw new NotYetImplementedForExchangeException();
    }

    @Override
    public UserTrades getTradeHistory(TradeHistoryParams params) {
        throw new NotYetImplementedForExchangeException();
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
        throw new NotYetImplementedForExchangeException();
    }

    @Override
    public void verifyOrder(MarketOrder marketOrder) {
        throw new NotYetImplementedForExchangeException();
    }

    @Override
    public Collection<Order> getOrder(String... orderIds) {
        throw new NotYetImplementedForExchangeException();
    }
}
