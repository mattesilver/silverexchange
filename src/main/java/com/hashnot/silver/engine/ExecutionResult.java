package com.hashnot.silver.engine;

/**
 * A result of a single atomic execution of one Offer against another.
 * All fields Are nullable
 */
public class ExecutionResult {
    /**
     * If the execution was successful, this field contains the resulting transaction.
     */
    public final Transaction executed;

    /**TODO fix typo
     * A reminder of the currently executed Offer. If the offer was fully matched it's null. If the offer wasn't matched at all, it's the same object as the original offer
     */
    public final Offer reminder;

    /**
     * A reminder of the offer from order book. if the offer was full matched, it's null and the offer should be removed from the order book. Otherwise it should replace the first offer in the order book.
     */
    public final Offer againstReminder;

    public ExecutionResult(Transaction executed, Offer reminder, Offer againstReminder) {
        this.executed = executed;
        this.reminder = reminder;
        this.againstReminder = againstReminder;
    }

    @Override
    public String toString() {
        return
                "executed=" + executed
                        + ", reminder=" + reminder
                        + ", againstReminder=" + againstReminder
                ;
    }
}
