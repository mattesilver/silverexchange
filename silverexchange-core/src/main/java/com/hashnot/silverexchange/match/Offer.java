package com.hashnot.silverexchange.match;

import com.hashnot.silverexchange.OfferRate;
import com.hashnot.silverexchange.util.BigDecimals;
import com.hashnot.silverexchange.util.Clock;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Comparator;
import java.util.Objects;

/**
 * Private API
 */
public class Offer {
    public static final Comparator<Offer> COMPARATOR_BY_RATE = Offer::compareByRate;

    private Object pair;
    private Side side;
    private BigDecimal amount;
    private OfferRate rate;

    public Offer(Object pair, Side side, BigDecimal amount, OfferRate rate) {
        assert pair != null;
        assert side != null;
        assert amount != null;

        if (!BigDecimals.gtz(amount))
            throw new IllegalArgumentException("Non-positive amount");

        this.pair = pair;
        this.side = side;
        this.amount = amount;
        this.rate = rate;
    }

    private static int compareByRate(Offer a, Offer b) {
        return a.getRate().compareTo(b.getRate()) * a.getSide().orderSignum;
    }

    public Object getPair() {
        return pair;
    }

    public Side getSide() {
        return side;
    }

    public OfferRate getRate() {
        return rate;
    }

    /**
     * @param against an offer from the order book, against which <code>this</code> order is executed
     * @param clock Clock used to generate transaction timestamp
     */
    public OfferExecutionResult execute(Offer against, Clock clock) {
        assert pair.equals(against.pair) : "Not executing against offer of the same pair";
        assert side != against.side : "Not executing against offer of opposite side";

        if (!rateMatch(against)) {
            // no execution due to no price match
            return new OfferExecutionResult(null, this, against);
        }

        Instant timestamp = clock.get();
        BigDecimal amountDiff = amount.subtract(against.amount);
        int amountDiffSig = amountDiff.signum();
        if (amountDiffSig == 0)
            // 1-to-1 match
            return new OfferExecutionResult(new Transaction(amount, against.rate, timestamp), null, null);

        // here we have to null either of remainders in the result
        Offer remainder,
                againstRemainder;
        Transaction tx;


        // if this.amount > against.amount, null againstRemainder and tx.amount comes from against
        if (amountDiffSig > 0) {
            remainder = new Offer(against.pair, side, amountDiff, rate);
            againstRemainder = null;
            tx = new Transaction(against.amount, against.rate, timestamp);

            // otherwise, i.e. this.amount < against.amount, null remainder and tx.amount comes from this
        } else {
            remainder = null;
            againstRemainder = new Offer(pair, against.side, amountDiff.negate(), against.rate);
            tx = new Transaction(amount, against.rate, timestamp);
        }

        return new OfferExecutionResult(tx, remainder, againstRemainder);
    }

    boolean rateMatch(Offer against) {
        assert side != against.side;
        return rate.compareTo(against.rate) * side.orderSignum <= 0;
    }

    public boolean isMarketOrder() {
        return rate.isMarket();
    }

    @Override
    public String toString() {
        return side
                + " " + amount
                + " " + pair
                + "@" + rate
                ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pair, side, amount, rate);
    }

    @Override
    public boolean equals(Object obj) {
        return
                this == obj
                        || obj instanceof Offer && equals((Offer) obj);
    }

    private boolean equals(Offer o) {
        return
                pair.equals(o.pair)
                        && side == o.side
                        && amount.equals(o.amount)
                        && rate.equals(o.rate)
                ;
    }
}
