package com.hashnot.silverexchange.xchange.service.account;

import org.junit.jupiter.api.Test;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;

import static java.math.BigDecimal.ONE;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SilverAccountServiceTest {

    private SilverAccountService service = new SilverAccountService();

    @Test
    void getAccountInfo() {
        assertThrows(NotYetImplementedForExchangeException.class, service::getAccountInfo);
    }

    @Test
    void withdrawFunds() {
        assertThrows(NotYetImplementedForExchangeException.class, () -> service.withdrawFunds(Currency.BTC, ONE, "Address"));
    }

    @Test
    void withdrawFundsWithParams() {
        assertThrows(NotYetImplementedForExchangeException.class, () -> service.withdrawFunds(null));
    }

    @Test
    void requestDepositAddress() {
        assertThrows(NotYetImplementedForExchangeException.class, () -> service.requestDepositAddress(Currency.BTC));
    }

    @Test
    void createFundingHistoryParams() {
        assertNull(service.createFundingHistoryParams());
    }

    @Test
    void getFundingHistory() {
        assertThrows(NotYetImplementedForExchangeException.class, () -> service.getFundingHistory(null));
    }
}
