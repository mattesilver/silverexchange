package com.hashnot.silverexchange.ext;

import com.hashnot.silverexchange.TransactionRate;
import com.hashnot.silverexchange.match.Transaction;

import java.math.BigDecimal;
import java.util.function.BiFunction;

public interface ITransactionFactory extends BiFunction<BigDecimal, TransactionRate, Transaction> {
}
