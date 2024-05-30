package com.eteration.simplebanking.model;

import com.eteration.simplebanking.exception.InsufficientBalanceException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "transaction_type")
@Getter
@Setter
public abstract class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    @NonNull
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

    public abstract void apply(BankAccount account) throws InsufficientBalanceException;

    public void setBankAccount(BankAccount bankAccount){
        this.bankAccount = bankAccount;
    }

    public double getAmount() {
        return amount;
    }

}
