package com.eteration.simplebanking.services;

import com.eteration.simplebanking.model.BankAccount;
import com.eteration.simplebanking.model.DepositTransaction;
import com.eteration.simplebanking.exception.InsufficientBalanceException;
import com.eteration.simplebanking.model.WithdrawalTransaction;
import com.eteration.simplebanking.repository.BankAccountRepository;
import com.eteration.simplebanking.repository.TransactionRepository;
import com.eteration.simplebanking.request.TransactionRequest;
import com.eteration.simplebanking.response.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.eteration.simplebanking.exception.BankAccounNotFoundException;

import java.util.Optional;
import java.util.UUID;

@Service
public class BankAccountService {
    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public ResponseEntity<BankAccount> findAccount(String accountNumber) throws BankAccounNotFoundException {
        Optional<BankAccount> bankAccount = Optional.ofNullable(bankAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new BankAccounNotFoundException("Account with ID " + accountNumber + " not found")));
        return ResponseEntity.ok(bankAccount.get());
    }

    public BankAccount saveAccount(BankAccount account) {
        return bankAccountRepository.save(account);
    }

    public ResponseEntity<TransactionResponse> creditAccount(String accountNumber, TransactionRequest request) throws InsufficientBalanceException {
        Optional<BankAccount> accountOpt = bankAccountRepository.findByAccountNumber(accountNumber);
        if (!accountOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        BankAccount account = accountOpt.get();
        DepositTransaction depositTransaction = new DepositTransaction(request.getAmount());
        account.post(depositTransaction);
        transactionRepository.save(depositTransaction);
        bankAccountRepository.save(account);

        return ResponseEntity.ok(new TransactionResponse("OK", UUID.randomUUID().toString()));
    }

    public ResponseEntity<TransactionResponse> debitAccount(String accountNumber, TransactionRequest request) throws InsufficientBalanceException {
        Optional<BankAccount> accountOpt = bankAccountRepository.findByAccountNumber(accountNumber);
        if (!accountOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        BankAccount account = accountOpt.get();
        WithdrawalTransaction withdrawalTransaction = new WithdrawalTransaction(request.getAmount());

        account.post(withdrawalTransaction);

        transactionRepository.save(withdrawalTransaction);
        bankAccountRepository.save(account);

        return ResponseEntity.ok(new TransactionResponse("OK", UUID.randomUUID().toString()));
    }
}