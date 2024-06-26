package com.example.bankingtesttask;

import com.example.bankingtesttask.controllers.AccountController;
import com.example.bankingtesttask.entities.Account;
import com.example.bankingtesttask.entities.Currency;
import com.example.bankingtesttask.entities.Division;
import com.example.bankingtesttask.entities.User;
import com.example.bankingtesttask.services.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AccountControllerTest {

    @Mock
    private AccountServiceImpl accountServiceImpl;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    public void testGetAccountById() throws Exception {
        Account account = new Account();
        account.setId(1L);
        account.setUserId(1L);
        account.setDivisionId(1L);
        account.setCurrencyId(2);
        account.setBalance(BigDecimal.valueOf(0L));
        account.setAccNumber("12345840811114567891");
        account.setCorAcc("30145840811114567891");
        account.setTimeOpen(new Timestamp(0L));
        account.setTimeClose(null);
        account.setUser(new User(1L,"Иванов Иван Иванович"));
        account.setDivision(new Division(1L,"Отделение 1 ПАО NБанк",
                "1111", "123456780","7700000000"));
        account.setCurrency(new Currency(2, "USD","840"));

        when(accountServiceImpl.getAccountById(1L)).thenReturn(account);

        mockMvc.perform(get("/api/v1/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accNumber").value("12345840811114567891"))
                .andExpect(jsonPath("$.corAcc").value("30145840811114567891"))
                .andExpect(jsonPath("$.user.fio").value("Иванов Иван Иванович"))
                .andExpect(jsonPath("$.division.name").value("Отделение 1 ПАО NБанк"))
                .andExpect(jsonPath("$.division.bic").value("123456780"))
                .andExpect(jsonPath("$.division.inn").value("7700000000"))
                .andExpect(jsonPath("$.currency.name").value("USD"))
                .andExpect(jsonPath("$.currency.code").value("840"));

    }


    @Test
    public void testAddAccount() throws Exception {
        Account account = new Account();
        account.setUserId(1L);
        account.setDivisionId(1L);
        account.setCurrencyId(2);
        account.setBalance(BigDecimal.valueOf(0L));
        account.setAccNumber("12345840811114567891");
        account.setCorAcc("30145840811114567891");
        account.setTimeOpen(new Timestamp(0L));


        when(accountServiceImpl.addAccount(account)).thenReturn(new ResponseEntity<>(1L, HttpStatus.OK));

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": 1,\"divisionId\": 1,\"currencyId\": 2," +
                                "\"accNumber\": \"12345840811114567891\"," +
                                "\"corAcc\": \"30145840811114567891\"," +
                                "\"balance\": \"0\"," +
                                "\"timeOpen\": \"2024-06-24T00:29:56.000000\"}"))
                .andExpect(status().isOk());

    }

    @Test
    public void testUsersAccountsWithParameters() throws Exception {
        List<Account> accounts = new LinkedList<>();

        Account account = new Account();
        account.setId(1L);
        account.setUserId(1L);
        account.setDivisionId(1L);
        account.setCurrencyId(2);
        account.setBalance(BigDecimal.valueOf(0L));
        account.setAccNumber("12345840811114567891");
        account.setCorAcc("30145840811114567891");
        account.setTimeOpen(new Timestamp(0L));
        account.setTimeClose(null);
        account.setUser(new User(1L,"Иванов Иван Иванович"));
        account.setDivision(new Division(1L,"Отделение 1 ПАО NБанк",
                "1111", "123456780","7700000000"));
        account.setCurrency(new Currency(2, "USD","840"));

        accounts.add(account);


        when(accountServiceImpl.getUsersAccountsWithParameters(1L,true,true,"RUB",
                BigDecimal.valueOf(-1L), BigDecimal.valueOf(100L),100,0)).thenReturn(accounts);



        mockMvc.perform(get("/api/v1/accounts")
                        .param("userId","1")
                        .param("active","true")
                        .param("closed","true")
                        .param("currencyName","RUB")
                        .param("balanceM","-1")
                        .param("balanceL","100")
                        .param("limit","100")
                        .param("offset","0"))
                .andExpect(status().isOk());

    }
    @Test
    public void testChangeBalance() throws Exception {


        when(accountServiceImpl.changeAccountsBalance(1L,BigDecimal.valueOf(10L)))
                .thenReturn(new ResponseEntity<>("1", HttpStatus.OK));

        mockMvc.perform(post("/api/v1/accounts/1/changeBalance")
                        .param("value","10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("1"))
                .andExpect(status().isOk());

    }
    @Test
    public void testCloseAccount() throws Exception {


        when(accountServiceImpl.closeAccount(1L))
                .thenReturn(new ResponseEntity<>("2024-06-24T00:29:56.000000", HttpStatus.OK));

        mockMvc.perform(post("/api/v1/accounts/1/close")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("2024-06-24T00:29:56.000000"))
                .andExpect(status().isOk());

    }

}
