package com.eteration.simplebanking.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String owner;
    private String accountNumber;
    private double balance;

    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    // Constructors, getters, and setters

    public BankAccount() {
    }

    /*public BankAccount(String owner, String accountNumber) {
        this.owner = owner;
        this.accountNumber = accountNumber;
        this.balance = 0.0;
    }*/

    public void post(Transaction transaction) {
        transactions.add(transaction);
        transaction.setBankAccount(this);
        transaction.apply(this);
    }

    // credit and debit methods
}
