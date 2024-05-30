package com.eteration.simplebanking.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Deposit")
public class DepositTransaction extends Transaction {
    public DepositTransaction() {
    }

    public DepositTransaction(double amount) {
        super(amount);
    }

    @Override
    public void apply(BankAccount account) {
        account.setBalance(account.getBalance() + getAmount());
    }
}