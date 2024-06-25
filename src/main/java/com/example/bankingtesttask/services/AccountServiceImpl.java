package com.example.bankingtesttask.services;

import com.example.bankingtesttask.entities.Account;
import com.example.bankingtesttask.entities.Currency;
import com.example.bankingtesttask.entities.Division;
import com.example.bankingtesttask.exceptions.*;
import com.example.bankingtesttask.repositories.AccountRepository;
import com.example.bankingtesttask.repositories.CurrencyRepository;
import com.example.bankingtesttask.repositories.DivisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;


@Service

public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final DivisionRepository divisionRepository;
    private final CurrencyRepository currencyRepository;


    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, DivisionRepository divisionRepository,
                              CurrencyRepository currencyRepository) {
        this.accountRepository = accountRepository;
        this.divisionRepository = divisionRepository;
        this.currencyRepository = currencyRepository;

    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity<Long> addAccount(Account account) throws InvalidAccountNumberException,
            InvalidInitAccountsBalanceException, CreateClosedAccountException {
        if(account.getTimeClose() != null){
            throw new CreateClosedAccountException("Creation of closed account is prohibited");
        }

        if(account.getBalance().compareTo(BigDecimal.ZERO)<0){
            throw new InvalidInitAccountsBalanceException("Negative balance is prohibited when creating an account");
        }
        if(validateAccountNumber(account.getAccNumber(), account.getCurrencyId(),account.getDivisionId())
                && validateAccountNumber(account.getCorAcc(), account.getCurrencyId(),account.getDivisionId())
                && account.getCorAcc().startsWith("301")){
            accountRepository.save(account);
            return new ResponseEntity<>(account.getId(), HttpStatus.OK);
        }
        else{
            throw new InvalidAccountNumberException("Account number or correspondent account number is invalid");
        }

    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public Account getAccountById(Long id){
        return accountRepository.getAccountById(id);
    }

    @Override
    @Transactional(readOnly = true, isolation = Isolation.READ_COMMITTED)
    public List<Account> getUsersAccountsWithParameters(Long userId, Boolean active, Boolean closed,
                                                        String currencyName, BigDecimal balanceM,
                                                        BigDecimal balanceL, Integer limit, Integer offset){

        if(balanceM==null){
            balanceM = BigDecimal.valueOf(Double.MIN_VALUE);
        }
        if(balanceL==null){
            balanceL = BigDecimal.valueOf(Double.MAX_VALUE);
        }
        List<Account> res = accountRepository.getAccountByUserId(userId);
        if(!active){
            res = res.stream().filter(e -> e.getTimeClose() != null).collect(Collectors.toList());
        }
        if(!closed){
            res = res.stream().filter(e -> e.getTimeClose() == null).collect(Collectors.toList());
        }
        BigDecimal finalBalanceM = balanceM;
        BigDecimal finalBalanceL = balanceL;
        res = res.stream().filter(e->e.getCurrency().getName().equals(currencyName)
                        && e.getBalance().compareTo(finalBalanceM)>0
                        && e.getBalance().compareTo(finalBalanceL)<0)
                .collect(Collectors.toList());
        if(offset!=null){
            res = res.stream().skip(offset).collect(Collectors.toList());
        }
        if(limit!=null){
            res = res.stream().limit(limit).collect(Collectors.toList());
        }
        return res;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public ResponseEntity<String> changeAccountsBalance(Long accountId, BigDecimal value)
            throws BalanceChangingOfClosedAccountException, DecreaseNegativeBalanceException {
        Account account = accountRepository.getAccountById(accountId);
        if(account.getTimeClose()!=null){
            throw new BalanceChangingOfClosedAccountException("Changing the balance of a closed account is prohibited");
        }
        if(account.getBalance().compareTo(BigDecimal.ZERO)<0 && value.compareTo(BigDecimal.ZERO)<0){
            throw new DecreaseNegativeBalanceException("Debiting from an account with a negative balance is prohibited");
        }

        account.setBalance(account.getBalance().add(value));
        accountRepository.save(account);

        return new ResponseEntity<>(account.getBalance().toString(), HttpStatus.OK);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity<String> closeAccount(Long accountId) throws ClosingClosedAccountException {
        Account account = accountRepository.getAccountById(accountId);
        if(account.getTimeClose()!=null){
            throw new ClosingClosedAccountException("Account was closed before");
        }
        else {
            account.setTimeClose(new Timestamp(System.currentTimeMillis()));
            accountRepository.save(account);
            return new ResponseEntity<>(account.getTimeClose().toString(), HttpStatus.OK);
        }
    }


    private boolean validateAccountNumber(String accNumber, Integer curId, Long divId){
        Currency tmpCur = currencyRepository.findCurrencyByCode(accNumber.substring(5,8));
        Division tmpDiv = divisionRepository.findDivisionByCode(accNumber.substring(9, 13));
        return tmpCur.getId().equals(curId) && tmpDiv.getId().equals(divId);
    }



}
