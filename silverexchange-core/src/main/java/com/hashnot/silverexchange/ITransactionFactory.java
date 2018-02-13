package com.hashnot.silverexchange;

import com.hashnot.silverexchange.match.Transaction;

import java.math.BigDecimal;
import java.util.function.BiFunction;

public interface ITransactionFactory extends BiFunction<BigDecimal, TransactionRate, Transaction> {
}
