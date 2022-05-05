package com.bank.privatebnk.domain;

import com.bank.privatebnk.domain.enumeration.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name="tbl_transaction")
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    @Column(length=200,nullable=false)
    private String description;

    private double amount;

    private BigDecimal availableBalance;

    private TransactionType type;


    @ManyToOne
    @JoinColumn (name="account_id")
    private Account account;


    public Transaction(LocalDateTime date, String description, double amount, BigDecimal availableBalance,
                       TransactionType type, Account account) {
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.availableBalance = availableBalance;
        this.type = type;
        this.account = account;
    }
}
