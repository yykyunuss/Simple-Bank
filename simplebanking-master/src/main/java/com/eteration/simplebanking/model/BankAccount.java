package com.eteration.simplebanking.model;

import com.eteration.simplebanking.exception.InsufficientBalanceException;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String owner;

    @NonNull
    private String accountNumber;

    private double balance;

    private LocalDateTime createDate = LocalDateTime.now();

    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    public void post(Transaction transaction) throws InsufficientBalanceException {
        transactions.add(transaction);
        transaction.setBankAccount(this);
        transaction.apply(this);
    }

    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }
        this.balance = this.balance + amount;
    }

    public void withdraw(double amount) throws InsufficientBalanceException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        }
        if (this.balance < amount) {
            throw new InsufficientBalanceException("Insufficient balance for withdrawal");
        }
        this.balance = this.balance - amount;
    }
}
