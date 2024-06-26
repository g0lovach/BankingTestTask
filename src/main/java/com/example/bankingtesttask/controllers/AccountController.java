package com.example.bankingtesttask.controllers;


import com.example.bankingtesttask.entities.Account;
import com.example.bankingtesttask.exceptions.*;
import com.example.bankingtesttask.services.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountServiceImpl accountServiceImpl;

    @Autowired
    public AccountController(AccountServiceImpl accountServiceImpl) {
        this.accountServiceImpl = accountServiceImpl;
    }


    @GetMapping("{accountId}")
    public Account getAccountById(@PathVariable("accountId") Long accountId) {
        return accountServiceImpl.getAccountById(accountId);
    }


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
        return accountServiceImpl.getUsersAccountsWithParameters(userId, active,closed,currencyName,balanceM,balanceL,
                limit, offset);
    }



    @PostMapping("")
    public ResponseEntity<Long> addAccount(@RequestBody Account account) throws InvalidAccountNumberException,
            InvalidInitAccountsBalanceException, CreateClosedAccountException {
        return accountServiceImpl.addAccount(account);
    }


    @PostMapping("{accountId}/changeBalance")
    public ResponseEntity<String> changeAccountsBalance(@PathVariable(name = "accountId") Long accountId,
                                                        @RequestParam(name = "value") BigDecimal value)
            throws BalanceChangingOfClosedAccountException, DecreaseNegativeBalanceException {
        return accountServiceImpl.changeAccountsBalance(accountId,value);
    }


    @PostMapping("{accountId}/close")
    public ResponseEntity<String> closeAccount(@PathVariable(name = "accountId") Long accountId)
            throws ClosingClosedAccountException {

        return accountServiceImpl.closeAccount(accountId);
    }


}
