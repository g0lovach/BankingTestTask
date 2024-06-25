package com.example.bankingtesttask.repositories;

import com.example.bankingtesttask.entities.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CurrencyRepository extends JpaRepository<Currency, Integer> {

    Currency findCurrencyByCode(String code);


}
