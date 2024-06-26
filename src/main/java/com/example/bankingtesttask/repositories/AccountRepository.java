package com.example.bankingtesttask.repositories;


import com.example.bankingtesttask.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {

    @Query(value = "select * from accounts where user_id = :userId", nativeQuery = true)
    List<Account> getAccountsByUserId(@Param("userId")Long userId);

    Account getAccountById(Long accountId);


}
