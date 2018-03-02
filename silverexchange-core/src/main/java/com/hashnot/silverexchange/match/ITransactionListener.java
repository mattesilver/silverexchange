package com.hashnot.silverexchange.match;

import java.math.BigDecimal;

public interface ITransactionListener<OfferT extends Offer> {
    /**
     * Notify the environment of a new transaction.
     * Matching engine has no use for them, apart for creation,
     * so it doesn't have any type for transactions and this method is of type void.
     *
     * @param amount amount of the new transaction
     * @param rate   rate of the new transaction
     * @param active active {@link Offer} that triggered the transaction
     */
    void notifyTransaction(BigDecimal amount, BigDecimal rate, OfferT active);
}
