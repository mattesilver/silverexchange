package com.hashnot.silverexchange.match;

/**
 * A result of a single atomic execution of one {@link Offer} against another.
 * All fields are nullable
 */
public class OfferExecutionResult {
    /**
     * If the execution was successful, this field contains the resulting transaction.
     */
    public final Transaction transaction;

    /**
     * A remainder of the currently executed {@link Offer}. If the offer was fully matched it's null. If the offer wasn't matched at all, it's the same object as the original offer
     */
    public final Offer remainder;

    /**
     * A remainder of the {@link Offer} from order book. if the offer was full matched, it's null and the offer should be removed from the order book. Otherwise it should replace the first offer in the order book.
     */
    public final Offer againstRemainder;

    OfferExecutionResult(Transaction transaction, Offer remainder, Offer againstRemainder) {
        this.transaction = transaction;
        this.remainder = remainder;
        this.againstRemainder = againstRemainder;
    }

    @Override
    public String toString() {
        return
                "transaction=" + transaction
                        + ", remainder=" + remainder
                        + ", againstRemainder=" + againstRemainder
                ;
    }
}
