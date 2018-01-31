package com.hashnot.silver.engine;

import java.util.Optional;

public class ExecutionResult {
    public final Optional<Transaction> executed;
    public final Optional<Offer> reminder;
    public final Optional<Offer> againstReminder;

    public ExecutionResult(Transaction executed, Offer reminder, Offer againstReminder) {
        this.executed = Optional.ofNullable(executed);
        this.reminder = Optional.ofNullable(reminder);
        this.againstReminder = Optional.ofNullable(againstReminder);
    }
}
