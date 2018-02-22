package com.hashnot.silverexchange.match;

import com.hashnot.silverexchange.OfferRate;
import com.hashnot.silverexchange.TransactionRate;
import com.hashnot.silverexchange.ext.ITransactionFactory;
import com.hashnot.silverexchange.util.BigDecimals;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Private API
 */
public class Offer {
    private Side side;
    private BigDecimal amount;
    private OfferRate rate;

    public Offer(Side side, BigDecimal amount, OfferRate rate) {
        assert side != null;
        assert amount != null;

        if (!BigDecimals.gtz(amount))
            throw new IllegalArgumentException("Non-positive amount");

        this.side = side;
        this.amount = amount;
        this.rate = rate;
    }

    public static int compareByRate(Offer a, Offer b) {
        return a.getRate().compareTo(b.getRate()) * a.getSide().orderSignum;
    }

    public Side getSide() {
        return side;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public OfferRate getRate() {
        return rate;
    }

    /**
     * @param passive   An offer from the order book (hence the name passive, it's waiting in the order book), against
     *                  which <code>this</code> order (active) is executed
     * @param txFactory Transaction factory object used to create Transaction objects
     */
    public OfferExecutionResult execute(Offer passive, ITransactionFactory txFactory) {
        assert side != passive.side : "Not executing against offer of opposite side";

        if (!rateMatch(passive)) {
            // no execution due to no price match
            return new OfferExecutionResult(null, this, passive);
        }

        BigDecimal amountDiff = amount.subtract(passive.amount);
        int amountDiffSig = amountDiff.signum();

        // here we have to null either of remainders in the result
        Offer remainder;
        Offer passiveRemainder;
        Transaction tx;

        if (amountDiffSig == 0) {
            // 1-to-1 match
            tx = txFactory.apply(amount, TransactionRate.from(passive.rate));
            remainder = passiveRemainder = null;
        } else if (amountDiffSig > 0) {
            // if this.amount > against.amount, null passiveRemainder and tx.amount comes from against
            remainder = new Offer(side, amountDiff, rate);
            passiveRemainder = null;
            tx = txFactory.apply(passive.amount, TransactionRate.from(passive.rate));

            // otherwise, i.e. this.amount < against.amount, null remainder and tx.amount comes from this
        } else {
            remainder = null;
            passiveRemainder = new Offer(passive.side, amountDiff.negate(), passive.rate);
            tx = txFactory.apply(amount, TransactionRate.from(passive.rate));
        }

        return new OfferExecutionResult(tx, remainder, passiveRemainder);
    }

    boolean rateMatch(Offer passive) {
        assert side != passive.side;
        return rate.compareTo(passive.rate) * side.orderSignum <= 0;
    }

    public boolean isMarketOrder() {
        return rate.isMarket();
    }

    @Override
    public String toString() {
        return side
                + " " + amount
                + "@" + rate
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                side,
                amount,
                rate
        );
    }

    @Override
    public boolean equals(Object obj) {
        return
                this == obj
                        || obj instanceof Offer && equals((Offer) obj);
    }

    private boolean equals(Offer o) {
        return
                side == o.side
                        && amount.equals(o.amount)
                        && rate.equals(o.rate)
                ;
    }
}
