package com.example.bankingtesttask;

import com.example.bankingtesttask.entities.Account;
import com.example.bankingtesttask.entities.Currency;
import com.example.bankingtesttask.entities.Division;
import com.example.bankingtesttask.entities.User;
import com.example.bankingtesttask.exceptions.*;
import com.example.bankingtesttask.repositories.AccountRepository;
import com.example.bankingtesttask.repositories.CurrencyRepository;
import com.example.bankingtesttask.repositories.DivisionRepository;
import com.example.bankingtesttask.services.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CurrencyRepository currencyRepository;

    @Mock
    private DivisionRepository  divisionRepository;


    @InjectMocks
    private AccountServiceImpl accountServiceImpl;

    @BeforeEach
    public void setUp() {
        openMocks(this);
    }

    @Test
    public void testGetAccountByIdSuccess() throws CreateClosedAccountException, InvalidAccountNumberException,
            InvalidInitAccountsBalanceException {
        Account account = new Account();
        account.setId(1L);
        account.setUserId(1L);
        account.setDivisionId(1L);
        account.setCurrencyId(2);
        account.setBalance(BigDecimal.valueOf(0L));
        account.setAccNumber("12345840811114567891");
        account.setCorAcc("30145840811114567891");
        account.setTimeOpen(new Timestamp(System.currentTimeMillis()));
        account.setTimeClose(null);
        account.setUser(new User(1L,"Иванов Иван Иванович"));
        account.setDivision(new Division(1L,"Отделение 1 ПАО NБанк",
                "1111", "123456780","7700000000"));
        account.setCurrency(new Currency(2, "USD","840"));

        when(accountRepository.getAccountById(1L)).thenReturn(account);

        var retrievedAccount = accountServiceImpl.getAccountById(1L);
        assertEquals(retrievedAccount, account);
    }

    @Test
    public void testGetAccountByIdFail() {
        when(accountRepository.getAccountById(1L)).thenReturn(null);
        Account retrievedAccount = accountServiceImpl.getAccountById(1L);
        assertNull(retrievedAccount);
    }

    @Test
    public void testAddAccountSuccess() throws CreateClosedAccountException, InvalidAccountNumberException,
            InvalidInitAccountsBalanceException {
        Account account = new Account();
        account.setId(1L);
        account.setUserId(1L);
        account.setDivisionId(1L);
        account.setCurrencyId(2);
        account.setBalance(BigDecimal.valueOf(0L));
        account.setAccNumber("12345840811114567891");
        account.setCorAcc("30101840811114567891");
        account.setTimeOpen(new Timestamp(System.currentTimeMillis()));
        account.setTimeClose(null);
        account.setUser(new User(1L,"Иванов Иван Иванович"));
        account.setDivision(new Division(1L,"Отделение 1 ПАО NБанк",
                "1111", "123456780","7700000000"));
        account.setCurrency(new Currency(2, "USD","840"));

        when(accountRepository.getAccountById(1L)).thenReturn(account);
        when(currencyRepository.findCurrencyByCode("840")).thenReturn(new Currency(2,"USD","840"));
        when(divisionRepository.findDivisionByCode("1111")).thenReturn(
                new Division(1L,"Отделение 1 ПАО NБанк",
                "1111", "123456780","7700000000"));

        var retrievedAccount = accountServiceImpl.addAccount(account);
        assertEquals(new ResponseEntity<>(1L,HttpStatus.OK), retrievedAccount);
    }

    @Test
    public void testAddAccountWhenAccountNumberContainsInvalidCurrencyCode() throws CreateClosedAccountException,
            InvalidAccountNumberException, InvalidInitAccountsBalanceException {
        Account account = new Account();
        account.setId(1L);
        account.setUserId(1L);
        account.setDivisionId(1L);
        account.setCurrencyId(2);
        account.setBalance(BigDecimal.valueOf(0L));
        account.setAccNumber("12345000811114567891");
        account.setCorAcc("30145840811114567891");
        account.setTimeOpen(new Timestamp(System.currentTimeMillis()));
        account.setTimeClose(null);

        when(currencyRepository.findCurrencyByCode("000")).thenReturn(null);
        when(divisionRepository.findDivisionByCode("1111")).thenReturn(
                new Division(1L,"Отделение 1 ПАО NБанк",
                "1111", "123456780","7700000000"));

        assertThrows(InvalidAccountNumberException.class, () -> accountServiceImpl.addAccount(account));
    }

    @Test
    public void testAddAccountWhenCorAccContainsInvalidCurrencyCode() throws CreateClosedAccountException,
            InvalidAccountNumberException, InvalidInitAccountsBalanceException {
        Account account = new Account();
        account.setId(1L);
        account.setUserId(1L);
        account.setDivisionId(1L);
        account.setCurrencyId(2);
        account.setBalance(BigDecimal.valueOf(0L));
        account.setAccNumber("12345840811114567891");
        account.setCorAcc("30145000811114567891");
        account.setTimeOpen(new Timestamp(System.currentTimeMillis()));
        account.setTimeClose(null);

        when(currencyRepository.findCurrencyByCode("000")).thenReturn(null);
        when(divisionRepository.findDivisionByCode("1111")).thenReturn(
                new Division(1L,"Отделение 1 ПАО NБанк",
                "1111", "123456780","7700000000"));

        assertThrows(InvalidAccountNumberException.class, () -> accountServiceImpl.addAccount(account));
    }


    @Test
    public void testAddClosedAccount() throws CreateClosedAccountException, InvalidAccountNumberException,
            InvalidInitAccountsBalanceException {
        Account account = new Account();
        account.setId(1L);
        account.setUserId(1L);
        account.setDivisionId(1L);
        account.setCurrencyId(2);
        account.setBalance(BigDecimal.valueOf(0L));
        account.setAccNumber("12345840811114567891");
        account.setCorAcc("30145000811114567891");
        account.setTimeOpen(new Timestamp(System.currentTimeMillis()));
        account.setTimeClose(new Timestamp(System.currentTimeMillis()));

        when(currencyRepository.findCurrencyByCode("000")).thenReturn(null);
        when(divisionRepository.findDivisionByCode("1111")).thenReturn(
                new Division(1L,"Отделение 1 ПАО NБанк",
                "1111", "123456780","7700000000"));

        assertThrows(CreateClosedAccountException.class, () -> accountServiceImpl.addAccount(account));
    }

    @Test
    public void testAddAccountWithNegativeBalance() throws CreateClosedAccountException, InvalidAccountNumberException,
            InvalidInitAccountsBalanceException {
        Account account = new Account();
        account.setId(1L);
        account.setUserId(1L);
        account.setDivisionId(1L);
        account.setCurrencyId(2);
        account.setBalance(BigDecimal.valueOf(-1L));
        account.setAccNumber("12345840811114567891");
        account.setCorAcc("30145000811114567891");
        account.setTimeOpen(new Timestamp(System.currentTimeMillis()));
        account.setTimeClose(null);

        when(currencyRepository.findCurrencyByCode("000")).thenReturn(null);
        when(divisionRepository.findDivisionByCode("1111")).thenReturn(
                new Division(1L,"Отделение 1 ПАО NБанк",
                "1111", "123456780","7700000000"));

        assertThrows(InvalidInitAccountsBalanceException.class, () -> accountServiceImpl.addAccount(account));
    }

    @Test
    public void testGetUsersAccountsWithParametersSuccess(){
        List<Account> accounts = new LinkedList<>();
        accounts.add(new Account(1L,1L,1L,2, "12345840811114567891",
                "30101840811114567891",BigDecimal.valueOf(50L),
                new Timestamp(System.currentTimeMillis()),null,
                new User(1L,"Иванов Иван Иванович"),
                new Division(1L,"Отделение 1 ПАО NБанк",
                "1111", "123456780","7700000000"),
                new Currency(2, "USD","840")));
        accounts.add(new Account(2L,1L,1L,2, "12345840811114567892",
                "30101840811114567891",BigDecimal.valueOf(10L),
                new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()),
                new User(1L, "Иванов Иван Иванович"),
                new Division(1L,"Отделение 1 ПАО NБанк",
                        "1111", "123456780","7700000000"),
                new Currency(2, "USD","840")));
        accounts.add(new Account(3L,1L,1L,2, "12345810811114567893",
                "30101810811114567891",BigDecimal.valueOf(0L),
                new Timestamp(System.currentTimeMillis()),null,
                new User(1L,"Иванов Иван Иванович"),
                new Division(1L,"Отделение 1 ПАО NБанк",
                        "1111", "123456780","7700000000"),
                new Currency(1, "RUB","810")));
        accounts.add(new Account(4L,2L,1L,2, "12345840811114567894",
                "30101840811114567891",BigDecimal.valueOf(10L),
                new Timestamp(System.currentTimeMillis()),null,
                new User(2L,"Васильев Василий Васильевич"),
                new Division(1L,"Отделение 1 ПАО NБанк",
                        "1111", "123456780","7700000000"),
                new Currency(2, "USD","840")));
        when(accountRepository.getAccountsByUserId(1L)).thenReturn(accounts);

        List<Account> assertionList = new LinkedList<>();
        assertionList.add(accounts.get(1));

        assertEquals(assertionList, accountServiceImpl.getUsersAccountsWithParameters(1L,
                true,true,"USD",BigDecimal.valueOf(-1),BigDecimal.valueOf(1000),1,1));
    }

    @Test
    public void testGetUsersAccountsWithParametersWhenActiveFalse(){
        List<Account> accounts = new LinkedList<>();
        accounts.add(new Account(1L,1L,1L,2, "12345840811114567891",
                "30101840811114567891",BigDecimal.valueOf(50L),
                new Timestamp(System.currentTimeMillis()),null,
                new User(1L,"Иванов Иван Иванович"),
                new Division(1L,"Отделение 1 ПАО NБанк",
                        "1111", "123456780","7700000000"),
                new Currency(2, "USD","840")));
        accounts.add(new Account(2L,1L,1L,2, "12345840811114567892",
                "30101840811114567891",BigDecimal.valueOf(10L),
                new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()),
                new User(1L,"Иванов Иван Иванович"),
                new Division(1L,"Отделение 1 ПАО NБанк",
                        "1111", "123456780","7700000000"),
                new Currency(2, "USD","840")));
        accounts.add(new Account(3L,1L,1L,2, "12345810811114567893",
                "30101810811114567891",BigDecimal.valueOf(0L),
                new Timestamp(System.currentTimeMillis()),null,
                new User(1L,"Иванов Иван Иванович"),
                new Division(1L,"Отделение 1 ПАО NБанк",
                        "1111", "123456780","7700000000"),
                new Currency(1, "RUB","810")));
        accounts.add(new Account(4L,2L,1L,2, "12345840811114567894",
                "30101840811114567891",BigDecimal.valueOf(10L),
                new Timestamp(System.currentTimeMillis()),null,
                new User(2L,"Васильев Василий Васильевич"),
                new Division(1L,"Отделение 1 ПАО NБанк",
                        "1111", "123456780","7700000000"),
                new Currency(2, "USD","840")));
        when(accountRepository.getAccountsByUserId(1L)).thenReturn(accounts);

        List<Account> assertionList = new LinkedList<>();
        assertionList.add(accounts.get(1));

        assertEquals(assertionList, accountServiceImpl.getUsersAccountsWithParameters(1L,
                false,true,"USD",BigDecimal.valueOf(-1),BigDecimal.valueOf(1000),10,0));
    }

    @Test
    public void testGetUsersAccountsWithParametersWhenClosedFalse(){
        List<Account> accounts = new LinkedList<>();
        accounts.add(new Account(1L,1L,1L,2, "12345840811114567891",
                "30101840811114567891",BigDecimal.valueOf(50L),
                new Timestamp(System.currentTimeMillis()),null,
                new User(1L,"Иванов Иван Иванович"),
                new Division(1L,"Отделение 1 ПАО NБанк",
                        "1111", "123456780","7700000000")
                ,new Currency(2, "USD","840")));
        accounts.add(new Account(2L,1L,1L,2, "12345840811114567892",
                "30101840811114567891",BigDecimal.valueOf(10L),
                new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()),
                new User(1L,"Иванов Иван Иванович"),
                new Division(1L,"Отделение 1 ПАО NБанк",
                        "1111", "123456780","7700000000"),
                new Currency(2, "USD","840")));
        accounts.add(new Account(3L,1L,1L,2, "12345810811114567893",
                "30101810811114567891",BigDecimal.valueOf(0L),
                new Timestamp(System.currentTimeMillis()),null,
                new User(1L,"Иванов Иван Иванович"),
                new Division(1L,"Отделение 1 ПАО NБанк",
                        "1111", "123456780","7700000000"),
                new Currency(1, "RUB","810")));
        when(accountRepository.getAccountsByUserId(1L)).thenReturn(accounts);

        List<Account> assertionList = new LinkedList<>();
        assertionList.add(accounts.get(0));

        assertEquals(assertionList, accountServiceImpl.getUsersAccountsWithParameters(1L,
                true,false,"USD",BigDecimal.valueOf(-1),BigDecimal.valueOf(1000),10,0));
    }

    @Test
    public void testGetUsersAccountsWhenBalanceMBiggerThanBalanceL(){
        List<Account> accounts = new LinkedList<>();
        accounts.add(new Account(1L,1L,1L,2, "12345840811114567891",
                "30101840811114567891",BigDecimal.valueOf(50L),
                new Timestamp(System.currentTimeMillis()),null,
                new User(1L,"Иванов Иван Иванович"),
                new Division(1L,"Отделение 1 ПАО NБанк",
                        "1111", "123456780","7700000000"),
                new Currency(2, "USD","840")));
        accounts.add(new Account(2L,1L,1L,2, "12345840811114567892",
                "30101840811114567891",BigDecimal.valueOf(10L),
                new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()),
                new User(1L,"Иванов Иван Иванович"),
                new Division(1L,"Отделение 1 ПАО NБанк",
                        "1111", "123456780","7700000000"),
                new Currency(2, "USD","840")));
        accounts.add(new Account(3L,1L,1L,2, "12345810811114567893",
                "30101810811114567891",BigDecimal.valueOf(0L),
                new Timestamp(System.currentTimeMillis()),null,
                new User(1L,"Иванов Иван Иванович"),
                new Division(1L,"Отделение 1 ПАО NБанк",
                        "1111", "123456780","7700000000"),
                new Currency(1, "RUB","810")));
        when(accountRepository.getAccountsByUserId(1L)).thenReturn(accounts);

        List<Account> assertionList = new LinkedList<>();

        assertEquals(assertionList, accountServiceImpl.getUsersAccountsWithParameters(1L,
                true,true,"USD",BigDecimal.valueOf(1000),BigDecimal.valueOf(-1),10,0));
    }

    @Test
    public void testGetUsersAccountsWithParametersWhenClosedFalseAndActiveFalseSuccess(){
        List<Account> accounts = new LinkedList<>();
        accounts.add(new Account(1L,1L,1L,2, "12345840811114567891",
                "30101840811114567891",BigDecimal.valueOf(50L),
                new Timestamp(System.currentTimeMillis()),null,
                new User(1L,"Иванов Иван Иванович"),
                new Division(1L,"Отделение 1 ПАО NБанк",
                        "1111", "123456780","7700000000"),
                new Currency(2, "USD","840")));
        accounts.add(new Account(2L,1L,1L,2, "12345840811114567892",
                "30101840811114567891",BigDecimal.valueOf(10L),
                new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()),
                new User(1L,"Иванов Иван Иванович"),
                new Division(1L,"Отделение 1 ПАО NБанк",
                        "1111", "123456780","7700000000"),
                new Currency(2, "USD","840")));
        accounts.add(new Account(3L,1L,1L,2, "12345810811114567893",
                "30101810811114567891",BigDecimal.valueOf(0L),
                new Timestamp(System.currentTimeMillis()),null,
                new User(1L,"Иванов Иван Иванович"),
                new Division(1L,"Отделение 1 ПАО NБанк",
                        "1111", "123456780","7700000000"),
                new Currency(1, "RUB","810")));
        when(accountRepository.getAccountsByUserId(1L)).thenReturn(accounts);

        List<Account> assertionList = new LinkedList<>();

        assertEquals(assertionList, accountServiceImpl.getUsersAccountsWithParameters(1L,
                false,false,"USD",BigDecimal.valueOf(-1),BigDecimal.valueOf(1000),10,0));
    }

    @Test
    public void testIncreaseBalanceSuccess() throws DecreaseNegativeBalanceException,
            BalanceChangingOfClosedAccountException {
        Account account = new Account(1L,1L,1L,2, "12345840811114567891",
                "30101840811114567891",BigDecimal.valueOf(50L),
                new Timestamp(System.currentTimeMillis()),null,
                new User(1L,"Иванов Иван Иванович"),
                new Division(1L,"Отделение 1 ПАО NБанк",
                        "1111", "123456780","7700000000"),new Currency(2, "USD","840"));

        Account accountAfterChanges = new Account(1L,1L,1L,2,
                "12345840811114567891","30101840811114567891",BigDecimal.valueOf(60.75),
                new Timestamp(System.currentTimeMillis()),null,
                new User(1L,"Иванов Иван Иванович"),
                new Division(1L,"Отделение 1 ПАО NБанк",
                        "1111", "123456780","7700000000"),
                new Currency(2, "USD","840"));

        when(accountRepository.getAccountById(1L)).thenReturn(account);
        when(accountRepository.save(accountAfterChanges)).thenReturn(accountAfterChanges);

        assertEquals(new ResponseEntity<>(accountAfterChanges.getBalance().toString(), HttpStatus.OK),
                accountServiceImpl.changeAccountsBalance(1L,BigDecimal.valueOf(10.75)));

    }

    @Test
    public void testDecreaseBalanceSuccess() throws DecreaseNegativeBalanceException,
            BalanceChangingOfClosedAccountException {
        Account account = new Account(1L,1L,1L,2, "12345840811114567891",
                "30101840811114567891",BigDecimal.valueOf(50L),
                new Timestamp(System.currentTimeMillis()),null,
                new User(1L,"Иванов Иван Иванович"),
                new Division(1L,"Отделение 1 ПАО NБанк",
                        "1111", "123456780","7700000000"),
                new Currency(2, "USD","840"));

        Account accountAfterChanges = new Account(1L,1L,1L,2, "12345840811114567891",
                "30101840811114567891",BigDecimal.valueOf(-10.5),
                new Timestamp(System.currentTimeMillis()),null,
                new User(1L,"Иванов Иван Иванович"),
                new Division(1L,"Отделение 1 ПАО NБанк",
                        "1111", "123456780","7700000000"),
                new Currency(2, "USD","840"));

        when(accountRepository.getAccountById(1L)).thenReturn(account);
        when(accountRepository.save(accountAfterChanges)).thenReturn(accountAfterChanges);

        assertEquals(new ResponseEntity<>(accountAfterChanges.getBalance().toString(), HttpStatus.OK),
                accountServiceImpl.changeAccountsBalance(1L,BigDecimal.valueOf(-60.5)));

    }

    @Test
    public void testWhenDecreasingAccountWithNegativeBalance() throws DecreaseNegativeBalanceException,
            BalanceChangingOfClosedAccountException {
        Account account = new Account(1L,1L,1L,2, "12345840811114567891",
                "30101840811114567891",BigDecimal.valueOf(-50L),
                new Timestamp(System.currentTimeMillis()),null,
                new User(1L,"Иванов Иван Иванович"),
                new Division(1L,"Отделение 1 ПАО NБанк",
                        "1111", "123456780","7700000000"),
                new Currency(2, "USD","840"));


        when(accountRepository.getAccountById(1L)).thenReturn(account);


        assertThrows(DecreaseNegativeBalanceException.class, () -> accountServiceImpl.changeAccountsBalance(1L,BigDecimal.valueOf(-1L)));
    }

    @Test
    public void testWhenChangingBalanceOfClosedAccount() throws DecreaseNegativeBalanceException,
            BalanceChangingOfClosedAccountException {
        Account account = new Account(1L,1L,1L,2, "12345840811114567891",
                "30101840811114567891",BigDecimal.valueOf(-50L),
                new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()),
                new User(1L,"Иванов Иван Иванович"),
                new Division(1L,"Отделение 1 ПАО NБанк",
                        "1111", "123456780","7700000000"),new Currency(2, "USD","840"));


        when(accountRepository.getAccountById(1L)).thenReturn(account);


        assertThrows(BalanceChangingOfClosedAccountException.class,
                () -> accountServiceImpl.changeAccountsBalance(1L,BigDecimal.valueOf(-1L)));
    }

    @Test
    public void testCloseAccountSuccess() throws ClosingClosedAccountException {
        Account account = new Account(1L,1L,1L,2, "12345840811114567891",
                "30101840811114567891",BigDecimal.valueOf(-50L),
                new Timestamp(System.currentTimeMillis()),null,
                new User(1L,"Иванов Иван Иванович"),
                new Division(1L,"Отделение 1 ПАО NБанк",
                        "1111", "123456780","7700000000"),
                new Currency(2, "USD","840"));
        Account accountAfterChanging = new Account(1L,1L,1L,2,
                "12345840811114567891","30101840811114567891",BigDecimal.valueOf(-50L),
                new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()),
                new User(1L,"Иванов Иван Иванович"),
                new Division(1L,"Отделение 1 ПАО NБанк",
                        "1111", "123456780","7700000000"),
                new Currency(2, "USD","840"));

        when(accountRepository.getAccountById(1L)).thenReturn(account);
        when(accountRepository.save(accountAfterChanging)).thenReturn(accountAfterChanging);

        assertNotEquals(new ResponseEntity<>(null, HttpStatus.OK), accountServiceImpl.closeAccount(1L));


    }

    @Test
    public void testWhenClosingClosedAccount() throws ClosingClosedAccountException {
        Account account = new Account(1L,1L,1L,2,
                "12345840811114567891","30101840811114567891",BigDecimal.valueOf(-50L),
                new Timestamp(System.currentTimeMillis()),new Timestamp(System.currentTimeMillis()),
                new User(1L,"Иванов Иван Иванович"),
                new Division(1L,"Отделение 1 ПАО NБанк",
                        "1111", "123456780","7700000000"),
                new Currency(2, "USD","840"));


        when(accountRepository.getAccountById(1L)).thenReturn(account);

        assertThrows(ClosingClosedAccountException.class, () -> accountServiceImpl.closeAccount(1L));


    }



}
