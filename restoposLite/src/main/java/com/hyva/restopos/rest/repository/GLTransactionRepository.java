package com.hyva.restopos.rest.repository;


import com.hyva.restopos.rest.entities.AccountMaster;
import com.hyva.restopos.rest.entities.GLTransactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GLTransactionRepository extends JpaRepository<GLTransactions, Long> {
    List<GLTransactions> findAllByAccount(AccountMaster accountMaster);
    GLTransactions findByAccount(AccountMaster accountMaster);

}
