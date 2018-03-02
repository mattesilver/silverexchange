package com.hashnot.silverexchange.ext;

import com.hashnot.silverexchange.Transaction;
import com.hashnot.silverexchange.match.Offer;

import java.math.BigDecimal;

public interface ITransactionFactory<OfferT extends Offer, TransactionT extends Transaction> {
    TransactionT create(BigDecimal amount, BigDecimal rate, OfferT offer);
}
