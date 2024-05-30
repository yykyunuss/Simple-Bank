package com.eteration.simplebanking;

import com.eteration.simplebanking.exception.BankAccounNotFoundException;
import com.eteration.simplebanking.exception.InsufficientBalanceException;
import com.eteration.simplebanking.model.BankAccount;
import com.eteration.simplebanking.model.DepositTransaction;
import com.eteration.simplebanking.model.PhoneBillPaymentTransaction;
import com.eteration.simplebanking.model.WithdrawalTransaction;
import com.eteration.simplebanking.repository.BankAccountRepository;
import com.eteration.simplebanking.repository.TransactionRepository;
import com.eteration.simplebanking.request.TransactionRequest;
import com.eteration.simplebanking.response.TransactionResponse;
import com.eteration.simplebanking.services.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceTests {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private BankAccountService bankAccountService;

    private BankAccount bankAccount;
    private TransactionRequest transactionRequest;

    @BeforeEach
    void setUp() {
        bankAccount = new BankAccount();
        bankAccount.setId(1L);
        bankAccount.setAccountNumber("12345");
        bankAccount.setOwner("Yunus");
        bankAccount.setBalance(1000.0);

        transactionRequest = new TransactionRequest();
        transactionRequest.setAmount(200.0);
        transactionRequest.setPayee("Turk Telekom");
    }

    @Test
    void testFindAccount_Success() throws BankAccounNotFoundException {
        when(bankAccountRepository.findByAccountNumber(anyString())).thenReturn(Optional.of(bankAccount));

        ResponseEntity<BankAccount> response = bankAccountService.findAccount("12345");

        assertEquals(ResponseEntity.ok(bankAccount), response);
        verify(bankAccountRepository, times(1)).findByAccountNumber("12345");
    }

    @Test
    void testFindAccount_NotFound() {
        when(bankAccountRepository.findByAccountNumber(anyString())).thenReturn(Optional.empty());

        assertThrows(BankAccounNotFoundException.class, () -> bankAccountService.findAccount("12345"));
        verify(bankAccountRepository, times(1)).findByAccountNumber("12345");
    }

    @Test
    void testCreditAccount_Success() throws InsufficientBalanceException {
        when(bankAccountRepository.findByAccountNumber(anyString())).thenReturn(Optional.of(bankAccount));
        when(transactionRepository.save(any(DepositTransaction.class))).thenReturn(null);
        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(bankAccount);

        ResponseEntity<TransactionResponse> response = bankAccountService.creditAccount("12345", transactionRequest);

        assertEquals("OK", response.getBody().getStatus());
        verify(bankAccountRepository, times(1)).findByAccountNumber("12345");
        verify(transactionRepository, times(1)).save(any(DepositTransaction.class));
        verify(bankAccountRepository, times(1)).save(any(BankAccount.class));
    }

    @Test
    void testCreditAccount_NotFound() throws InsufficientBalanceException {
        when(bankAccountRepository.findByAccountNumber(anyString())).thenReturn(Optional.empty());

        ResponseEntity<TransactionResponse> response = bankAccountService.creditAccount("12345", transactionRequest);

        assertEquals(ResponseEntity.notFound().build(), response);
        verify(bankAccountRepository, times(1)).findByAccountNumber("12345");
        verify(transactionRepository, times(0)).save(any(DepositTransaction.class));
        verify(bankAccountRepository, times(0)).save(any(BankAccount.class));
    }

    @Test
    void testDebitAccount_Success() throws InsufficientBalanceException {
        when(bankAccountRepository.findByAccountNumber(anyString())).thenReturn(Optional.of(bankAccount));
        when(transactionRepository.save(any(WithdrawalTransaction.class))).thenReturn(null);
        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(bankAccount);

        ResponseEntity<TransactionResponse> response = bankAccountService.debitAccount("12345", transactionRequest);

        assertEquals("OK", response.getBody().getStatus());
        verify(bankAccountRepository, times(1)).findByAccountNumber("12345");
        verify(transactionRepository, times(1)).save(any(WithdrawalTransaction.class));
        verify(bankAccountRepository, times(1)).save(any(BankAccount.class));
    }

    @Test
    void testDebitAccount_NotFound() throws InsufficientBalanceException {
        when(bankAccountRepository.findByAccountNumber(anyString())).thenReturn(Optional.empty());

        ResponseEntity<TransactionResponse> response = bankAccountService.debitAccount("12345", transactionRequest);

        assertEquals(ResponseEntity.notFound().build(), response);
        verify(bankAccountRepository, times(1)).findByAccountNumber("12345");
        verify(transactionRepository, times(0)).save(any(WithdrawalTransaction.class));
        verify(bankAccountRepository, times(0)).save(any(BankAccount.class));
    }

    @Test
    void testPhoneBillPayment_Success() throws InsufficientBalanceException {
        when(bankAccountRepository.findByAccountNumber(anyString())).thenReturn(Optional.of(bankAccount));
        when(transactionRepository.save(any(PhoneBillPaymentTransaction.class))).thenReturn(null);
        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(bankAccount);

        ResponseEntity<TransactionResponse> response = bankAccountService.phoneBillPayment("12345", transactionRequest);

        assertEquals("OK", response.getBody().getStatus());
        verify(bankAccountRepository, times(1)).findByAccountNumber("12345");
        verify(transactionRepository, times(1)).save(any(PhoneBillPaymentTransaction.class));
        verify(bankAccountRepository, times(1)).save(any(BankAccount.class));
    }

    @Test
    void testPhoneBillPayment_NotFound() throws InsufficientBalanceException {
        when(bankAccountRepository.findByAccountNumber(anyString())).thenReturn(Optional.empty());

        ResponseEntity<TransactionResponse> response = bankAccountService.phoneBillPayment("12345", transactionRequest);

        assertEquals(ResponseEntity.notFound().build(), response);
        verify(bankAccountRepository, times(1)).findByAccountNumber("12345");
        verify(transactionRepository, times(0)).save(any(PhoneBillPaymentTransaction.class));
        verify(bankAccountRepository, times(0)).save(any(BankAccount.class));
    }
}