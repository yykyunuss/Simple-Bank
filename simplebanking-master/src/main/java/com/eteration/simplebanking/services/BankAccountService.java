package com.eteration.simplebanking.services;

import com.eteration.simplebanking.model.BankAccount;
import com.eteration.simplebanking.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankAccountService {
    @Autowired
    private BankAccountRepository bankAccountRepository;

    public Optional<BankAccount> getAccount(String accountNumber) {
        return bankAccountRepository.findByAccountNumber(accountNumber);
    }

    public BankAccount  saveAccount(BankAccount account) {
        return bankAccountRepository.save(account);
    }
}