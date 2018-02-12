package com.hashnot.silverexchange.xchange.service.account;

import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.AccountInfo;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.exceptions.NotYetImplementedForExchangeException;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.WithdrawFundsParams;

import java.math.BigDecimal;
import java.util.List;

public class SilverAccountService implements AccountService {
    @Override
    public AccountInfo getAccountInfo() {
        throw new NotYetImplementedForExchangeException();
    }

    @Override
    public String withdrawFunds(Currency currency, BigDecimal amount, String address) {
        throw new NotYetImplementedForExchangeException();
    }

    @Override
    public String withdrawFunds(WithdrawFundsParams params) {
        throw new NotYetImplementedForExchangeException();
    }

    @Override
    public String requestDepositAddress(Currency currency, String... args) {
        throw new NotYetImplementedForExchangeException();
    }

    @Override
    public TradeHistoryParams createFundingHistoryParams() {
        return null;
    }

    @Override
    public List<FundingRecord> getFundingHistory(TradeHistoryParams params) {
        throw new NotYetImplementedForExchangeException();
    }
}
