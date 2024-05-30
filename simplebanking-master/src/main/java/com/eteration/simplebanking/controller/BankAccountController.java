package com.eteration.simplebanking.controller;

import com.eteration.simplebanking.exception.BankAccounNotFoundException;
import com.eteration.simplebanking.exception.InsufficientBalanceException;
import com.eteration.simplebanking.model.*;
import com.eteration.simplebanking.repository.TransactionRepository;
import com.eteration.simplebanking.request.TransactionRequest;
import com.eteration.simplebanking.response.TransactionResponse;
import com.eteration.simplebanking.services.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account/v1")
public class BankAccountController {
    @Autowired
    private BankAccountService bankAccountService;

    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping("/create-account")
    public ResponseEntity<BankAccount> saveAccount(@RequestBody BankAccount newAccount) {
        BankAccount bankAccount = bankAccountService.saveAccount(newAccount);
        return ResponseEntity.ok(bankAccount);
    }

    @GetMapping("/find-account/{accountNumber}")
    public ResponseEntity<BankAccount> findAccount(@PathVariable String accountNumber) throws BankAccounNotFoundException {
        return bankAccountService.findAccount(accountNumber);
    }

    @PostMapping("/credit/{accountNumber}")
    public ResponseEntity<TransactionResponse> creditAccount(@PathVariable String accountNumber, @RequestBody TransactionRequest request) throws InsufficientBalanceException {
        return bankAccountService.creditAccount(accountNumber, request);
    }

    @PostMapping("/debit/{accountNumber}")
    public ResponseEntity<TransactionResponse> debitAccount(@PathVariable String accountNumber, @RequestBody TransactionRequest request) throws InsufficientBalanceException {
        return bankAccountService.debitAccount(accountNumber, request);
    }

}
