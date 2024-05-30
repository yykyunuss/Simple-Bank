package com.eteration.simplebanking.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("BillPayment")
public class PhoneBillPaymentTransaction extends Transaction {
    private String payee;

    public PhoneBillPaymentTransaction() {
    }

    public PhoneBillPaymentTransaction(double amount, String payee) {
        super(amount);
        this.payee = payee;
    }

    @Override
    public void apply(BankAccount account) {
        account.setBalance(account.getBalance() - getAmount());
    }

}