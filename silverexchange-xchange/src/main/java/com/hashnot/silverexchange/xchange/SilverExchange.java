package com.hashnot.silverexchange.xchange;

import com.hashnot.silverexchange.xchange.service.account.SilverAccountService;
import com.hashnot.silverexchange.xchange.service.marketdata.SilverMarketDataService;
import com.hashnot.silverexchange.xchange.service.trade.SilverTradeService;
import org.knowm.xchange.BaseExchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.exceptions.ExchangeException;

import java.io.InputStream;

public class SilverExchange extends BaseExchange {

    static final String NAME = "SilverExchange";

    @Override
    protected void initServices() {
        this.accountService = new SilverAccountService();
        this.marketDataService = new SilverMarketDataService();
        this.tradeService = new SilverTradeService();
    }

    @Override
    public si.mazi.rescu.SynchronizedValueFactory<Long> getNonceFactory() {
        return null;
    }

    @Override
    public void remoteInit() throws ExchangeException {
        //no remote
    }

    @Override
    protected void loadExchangeMetaData(InputStream is) {
        // no metadata
        // TODO prepare sample metadata, encourage users to provide some, or prepare explicit means to load state of the exchange.
    }

    @Override
    public ExchangeSpecification getDefaultExchangeSpecification() {
        ExchangeSpecification exchangeSpecification = new ExchangeSpecification(this.getClass().getCanonicalName());
        exchangeSpecification.setExchangeName(NAME);
        exchangeSpecification.setExchangeDescription("In memory exchange for testing trade bots");

        return exchangeSpecification;
    }


}
