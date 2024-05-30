package com.eteration.simplebanking.model;


import com.eteration.simplebanking.exception.InsufficientBalanceException;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Withdrawal")
public class WithdrawalTransaction extends Transaction {
    public WithdrawalTransaction() {
    }

    public WithdrawalTransaction(double amount) {
        super(amount);
    }

    @Override
    public void apply(BankAccount account) throws InsufficientBalanceException {
        if (getAmount() <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        }
        if (account.getBalance() - getAmount() < 0) {
            throw new InsufficientBalanceException("Insufficient balance for withdrawal");
        }
        account.setBalance(account.getBalance() - getAmount());
    }
}

