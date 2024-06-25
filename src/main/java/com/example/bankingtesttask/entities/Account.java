package com.example.bankingtesttask.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@AllArgsConstructor
@Table(name = "Accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    @Column(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    private Long userId;

    @Column(name = "division_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    private Long divisionId;

    @Column(name = "currency_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull
    private Integer currencyId;

    @Column(name = "acc_number", unique = true)
    @Length(min = 20, max=20)
    @NotNull
    private String accNumber;


    @Column(name = "cor_acc", unique = true)
    @Length(min = 20, max=20)
    @NotNull
    private String corAcc;

    @Column(name = "balance")
    @NotNull
    private BigDecimal balance;

    /**
     * мб переименовать в date???
     */
    @Column(name = "time_open")
    @NotNull
    private Timestamp timeOpen;


    /**
     * мб переименовать в date???
     */
    @Column(name = "time_close")
    private Timestamp timeClose;


    @ManyToOne(optional = false, targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable=false, updatable=false )
    User user;

    @ManyToOne(optional = false, targetEntity = Division.class)
    @JoinColumn(name = "division_id", referencedColumnName = "id", insertable=false, updatable=false )
    Division division;

    @ManyToOne(optional = false, targetEntity = Currency.class)
    @JoinColumn(name = "currency_id", referencedColumnName = "id", insertable=false, updatable=false )
    Currency currency;

    public Account() {

    }
}
