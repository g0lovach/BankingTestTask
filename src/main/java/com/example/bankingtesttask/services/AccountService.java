package com.example.bankingtesttask.services;

import com.example.bankingtesttask.entities.Account;
import com.example.bankingtesttask.exceptions.*;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

     ResponseEntity<Long> addAccount(Account account) throws InvalidAccountNumberException,
            InvalidInitAccountsBalanceException, CreateClosedAccountException;

     Account getAccountById(Long id);

     List<Account> getUsersAccountsWithParameters(Long userId, Boolean active, Boolean closed,
                                                        String currencyName, BigDecimal balanceM,
                                                        BigDecimal balanceL, Integer limit, Integer offset);

     ResponseEntity<String> changeAccountsBalance(Long accountId, BigDecimal value)
            throws BalanceChangingOfClosedAccountException, DecreaseNegativeBalanceException;

     ResponseEntity<String> closeAccount(Long accountId) throws ClosingClosedAccountException;


}
