package com.hashnot.silverexchange;

import com.hashnot.silverexchange.match.Offer;
import com.hashnot.silverexchange.match.Transaction;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ExecutionResult {
    /**
     * List of all transactions that happened as a result of matching an offer against the order book
     */
    final public List<Transaction> transactions;

    /**
     * An {@link Offer} that represents unexecuted part of the original market order. Unexecuted Offers representing market orders
     * cannot be added to order book as they lack {@link Offer#rate}
     */
    final public Offer remainder;

    public ExecutionResult(List<Transaction> transactions, Offer remainder) {
        this.transactions = transactions;
        this.remainder = remainder;
    }

    static ExecutionResult empty() {
        return new ExecutionResult(Collections.emptyList(), null);
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof ExecutionResult && equals((ExecutionResult) o);
    }

    private boolean equals(ExecutionResult r) {
        return
                Objects.equals(transactions, r.transactions)
                        && Objects.equals(remainder, r.remainder)
                ;
    }


    @Override
    public int hashCode() {
        return Objects.hash(transactions, remainder);
    }

    @Override
    public String toString() {
        return "ExecutionResult{" +
                "transactions=" + transactions +
                ", remainder=" + remainder +
                '}';
    }
}
