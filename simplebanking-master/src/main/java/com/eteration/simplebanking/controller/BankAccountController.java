package com.eteration.simplebanking.controller;

import com.eteration.simplebanking.model.BankAccount;
import com.eteration.simplebanking.model.DepositTransaction;
import com.eteration.simplebanking.model.WithdrawalTransaction;
import com.eteration.simplebanking.services.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/account/v1")
public class BankAccountController {
    @Autowired
    private BankAccountService bankAccountService;

    @PostMapping("/create-account")
    public ResponseEntity<BankAccount> saveAccount(@RequestBody BankAccount newAccount) {
        //to do: zaten hesap varsa kontrolü lazım
        BankAccount bankAccount = bankAccountService.saveAccount(newAccount);
        return ResponseEntity.ok(bankAccount);
    }

    @GetMapping("/find-account/{accountNumber}")
    public ResponseEntity<?> findAccount(@PathVariable String accountNumber) {
        Optional<BankAccount> bankAccount = bankAccountService.getAccount(accountNumber);
        return ResponseEntity.ok(bankAccount);
    }

    @GetMapping()
    public ResponseEntity<?> getNum() {
        return ResponseEntity.ok(new TransactionResponse("OK", "2"));
    }

    @PostMapping("/credit/{accountNumber}")
    public ResponseEntity<?> creditAccount(@PathVariable String accountNumber, @RequestBody TransactionRequest request) {
        Optional<BankAccount> accountOpt = bankAccountService.getAccount(accountNumber);
        if (!accountOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        BankAccount account = accountOpt.get();
        account.post(new DepositTransaction(request.getAmount()));
        bankAccountService.saveAccount(account);

        return ResponseEntity.ok(new TransactionResponse("OK", UUID.randomUUID().toString()));
    }

    @PostMapping("/debit/{accountNumber}")
    public ResponseEntity<?> debitAccount(@PathVariable String accountNumber, @RequestBody TransactionRequest request) {
        Optional<BankAccount> accountOpt = bankAccountService.getAccount(accountNumber);
        if (!accountOpt.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        BankAccount account = accountOpt.get();
        account.post(new WithdrawalTransaction(request.getAmount()));
        bankAccountService.saveAccount(account);

        return ResponseEntity.ok(new TransactionResponse("OK", UUID.randomUUID().toString()));
    }

    public static class TransactionRequest {
        private double amount;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }
    }

    public static class TransactionResponse {
        private String status;
        private String approvalCode;

        public TransactionResponse(String status, String approvalCode) {
            this.status = status;
            this.approvalCode = approvalCode;
        }

        public String getStatus() {
            return status;
        }

        public String getApprovalCode() {
            return approvalCode;
        }
    }
}
