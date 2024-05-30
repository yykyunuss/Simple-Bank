package com.eteration.simplebanking;

import com.eteration.simplebanking.controller.BankAccountController;
import com.eteration.simplebanking.exception.BankAccounNotFoundException;
import com.eteration.simplebanking.model.BankAccount;
import com.eteration.simplebanking.request.TransactionRequest;
import com.eteration.simplebanking.response.TransactionResponse;
import com.eteration.simplebanking.services.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BankAccountController.class)
public class BankAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
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
    void testSaveAccount() throws Exception {
        when(bankAccountService.saveAccount(any(BankAccount.class))).thenReturn(bankAccount);

        mockMvc.perform(post("/account/v1/create-account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"owner\": \"Yunus\", \"accountNumber\": \"12345\", \"balance\": 1000.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("12345"));
    }

    @Test
    void testFindAccount_Success() throws Exception {
        when(bankAccountService.findAccount(anyString())).thenReturn(ResponseEntity.ok(bankAccount));

        mockMvc.perform(get("/account/v1/find-account/12345")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("12345"));
    }

    @Test
    void testCreditAccount_Success() throws Exception {
        TransactionResponse response = new TransactionResponse("OK", UUID.randomUUID().toString());
        when(bankAccountService.creditAccount(anyString(), any(TransactionRequest.class))).thenReturn(ResponseEntity.ok(response));

        mockMvc.perform(post("/account/v1/credit/12345")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 200.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"));
    }

    @Test
    void testCreditAccount_NotFound() throws Exception {
        when(bankAccountService.creditAccount(anyString(), any(TransactionRequest.class))).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(post("/account/v1/credit/12345")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 200.0}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDebitAccount_Success() throws Exception {
        TransactionResponse response = new TransactionResponse("OK", UUID.randomUUID().toString());
        when(bankAccountService.debitAccount(anyString(), any(TransactionRequest.class))).thenReturn(ResponseEntity.ok(response));

        mockMvc.perform(post("/account/v1/debit/12345")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 200.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"));
    }

    @Test
    void testDebitAccount_NotFound() throws Exception {
        when(bankAccountService.debitAccount(anyString(), any(TransactionRequest.class))).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(post("/account/v1/debit/12345")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 200.0}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testPhoneBillPayment_Success() throws Exception {
        TransactionResponse response = new TransactionResponse("OK", UUID.randomUUID().toString());
        when(bankAccountService.phoneBillPayment(anyString(), any(TransactionRequest.class))).thenReturn(ResponseEntity.ok(response));

        mockMvc.perform(post("/account/v1/phone-bill-payment/12345")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 200.0, \"payee\": \"Turk Telekom\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"));
    }

    @Test
    void testPhoneBillPayment_NotFound() throws Exception {
        when(bankAccountService.phoneBillPayment(anyString(), any(TransactionRequest.class))).thenReturn(ResponseEntity.notFound().build());

        mockMvc.perform(post("/account/v1/phone-bill-payment/12345")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 200.0, \"payee\": \"Turk Telekom\"}"))
                .andExpect(status().isNotFound());
    }
}