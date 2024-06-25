package com.example.bankingtesttask.controllers;


import com.example.bankingtesttask.entities.Account;
import com.example.bankingtesttask.exceptions.*;
import com.example.bankingtesttask.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }


    //два теста - есть счет и нет счета
    @GetMapping("{accountId}")
    public Account getAccountById(@PathVariable("accountId") Long accountId) {
        return accountService.getAccountById(accountId);
    }

    //1) userId левый;
    // 2) balanceM > balanceL (ничего не вернет)
    // 3) active=true, closed =false
    // 4) оба true, оба false
    // 5) limit и offset
    @GetMapping("")
    public List<Account> getUsersAccounts(@RequestParam(name = "userId") Long userId,
                                          @RequestParam(name = "active", required = false, defaultValue = "true") Boolean active,
                                          @RequestParam(name = "closed", required = false, defaultValue = "true") Boolean closed,
                                          @RequestParam(name = "currencyName", required = false, defaultValue = "RUB") String currencyName,
                                          @RequestParam(name = "balanceMoreThen", required = false) BigDecimal balanceM,
                                          @RequestParam(name = "balanceLessThen", required = false) BigDecimal balanceL,
                                          @RequestParam(name = "limit", required = false) Integer limit,
                                          @RequestParam(name = "offset", required = false) Integer offset
                                          ) {
        return accountService.getUsersAccountsWithParameters(userId, active,closed,currencyName,balanceM,balanceL,
                limit, offset);
    }


    //1) кривая валюта в счете
    // 2) кривое отделение в счете
    // 3) п1 для кс
    // 4) п2 для кс
    // 5) отриц баланс
    // 6) создание закрытого счета
    // 7) успешный
    @PostMapping("")
    public ResponseEntity<Long> addAccount(@RequestBody Account account) throws InvalidAccountNumberException,
            InvalidInitAccountsBalanceException, CreateClosedAccountException {
        return accountService.addAccount(account);
    }

    /*
    1) Изменение закрытого счета
    2) Списание с минусового счета
    3) успешный в increase
    4) успешный в decrease
    */
    @PostMapping("{accountId}/changeBalance")
    public ResponseEntity<String> changeAccountsBalance(@PathVariable(name = "accountId") Long accountId,
                                                        @RequestParam(name = "value") BigDecimal value)
            throws BalanceChangingOfClosedAccountException, DecreaseNegativeBalanceException {
        return accountService.changeAccountsBalance(accountId,value);
    }

    /*
    1) Попытка закрыть закрытый счет
    2) Успешный
     */
    @PostMapping("{accountId}/close")
    public ResponseEntity<String> closeAccount(@PathVariable(name = "accountId") Long accountId)
            throws ClosingClosedAccountException {

        return accountService.closeAccount(accountId);
    }


}
