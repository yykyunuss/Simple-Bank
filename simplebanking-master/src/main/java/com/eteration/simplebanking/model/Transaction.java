package com.eteration.simplebanking.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "transaction_type")
public abstract class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;
    private double amount;

    @ManyToOne
    @JoinColumn(name = "bank_account_id")
    private BankAccount bankAccount;

    public Transaction() {
    }

    public Transaction(double amount) {
        this.date = LocalDateTime.now();
        this.amount = amount;
    }

    public abstract void apply(BankAccount account);

    public void setBankAccount(BankAccount bankAccount){
        this.bankAccount = bankAccount;
    }

    public double getAmount() {
        return amount;
    }

}
