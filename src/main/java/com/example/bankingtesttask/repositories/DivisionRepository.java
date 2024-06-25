package com.example.bankingtesttask.repositories;

import com.example.bankingtesttask.entities.Division;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DivisionRepository extends JpaRepository<Division, Long> {

    Division findDivisionByCode(String code);
    

}
