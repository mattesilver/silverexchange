package com.hashnot.silverexchange.xchange;

import org.junit.jupiter.api.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SilverExchangeTest {
    @Test
    void testCreateExchangeObject() {
        Exchange x = ExchangeFactory.INSTANCE.createExchange(SilverExchange.class.getName());
        assertEquals(SilverExchange.NAME, x.getExchangeSpecification().getExchangeName());
    }
}
