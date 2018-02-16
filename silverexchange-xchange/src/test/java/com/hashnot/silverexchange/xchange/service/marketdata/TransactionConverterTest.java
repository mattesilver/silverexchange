package com.hashnot.silverexchange.xchange.service.marketdata;

import com.hashnot.silverexchange.xchange.model.TestModelFactory;
import org.junit.jupiter.api.Test;
import org.knowm.xchange.dto.marketdata.Trade;

import static com.hashnot.silverexchange.xchange.model.TestModelFactory.ID_STR;
import static com.hashnot.silverexchange.xchange.model.TestModelFactory.TS_DATE;
import static java.math.BigDecimal.ONE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TransactionConverterTest {

    @Test
    void testToTrade() {
        Trade t = TransactionConverter.toTrade(TestModelFactory.tx(ONE, ONE));
        assertNull(t.getCurrencyPair());
        assertEquals(ID_STR, t.getId());
        assertEquals(ONE, t.getOriginalAmount());
        assertEquals(TS_DATE, t.getTimestamp());
        assertNull(t.getType());
        assertEquals(ONE, t.getPrice());
    }
}