package com.hashnot.silver.engine;

import org.knowm.xchange.currency.CurrencyPair;

import java.math.BigDecimal;
import java.util.Comparator;

public class Offer {
    private CurrencyPair pair;
    private Side side;
    private BigDecimal amount;
    private BigDecimal rate;

    public Offer(CurrencyPair pair, Side side, BigDecimal amount, BigDecimal rate) {
        this.pair = pair;
        this.side = side;
        this.amount = amount;
        this.rate = rate;
    }

    public CurrencyPair getPair() {
        return pair;
    }

    public Side getSide() {
        return side;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public boolean match(Offer other) {
        return
                pair.equals(other.pair)
                        && side != other.side
                        && rateMatch(other);
    }

    /**
     * TODO revert semantics of this and against parameter
     *
     * @param against an offer from the table against which this order is executed
     */
    public ExecutionResult execute(Offer against) {
        assert pair.equals(against.pair) : "Not executing against offer of the same pair";
        assert side != against.side : "Not executing against offer of opposite side";

        if (!rateMatch(against)) {
            // no execution due to no price match
            return new ExecutionResult(null, this, against);
        }

        if (amount.equals(against.amount))
            // 1-to-1 match
            return new ExecutionResult(new Transaction(amount, against.rate), null, null);


        BigDecimal txAmount = BigDecimals.min(amount, against.amount);
        Transaction tx = new Transaction(txAmount, against.rate);

        BigDecimal reminderAmount = BigDecimals.max(amount, against.amount).add(txAmount.negate());


        Offer reminder = new Offer(pair, side, amount.add(against.amount.negate()), rate);
        Offer againstReminder = new Offer(null, against.side, null, null);
        return new ExecutionResult(tx, reminder, againstReminder);


    }

    private boolean rateMatch(Offer against) {
        return rate.compareTo(against.rate) * side.orderSignum >= 0;
    }

    static class RateComparator implements Comparator<Offer> {

        @Override
        public int compare(Offer o1, Offer o2) {
            assert o1.pair.equals(o2.pair);
            assert o1.side == o2.side;
            return Side.RATE_COMPARATOR.compare(o1, o2);
        }
    }

    final public static Comparator<Offer> RATE_COMPARATOR = new RateComparator();

    @Override
    public String toString() {
        return side + " " + amount + " " + pair + "@" + rate;
    }
}
