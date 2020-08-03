package com.hyva.restopos.rest.repository;
import com.hyva.restopos.rest.entities.AccountGroup;
import com.hyva.restopos.rest.entities.AccountMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountMasterRepository extends JpaRepository<AccountMaster, Long> {
    List<AccountMaster> findAllByAgid(AccountGroup accountGroup);
    List<AccountMaster> findByAgidAndAmaccountidIsNull(AccountGroup accountGroup);
    List<AccountMaster> findByAgidAndAmaccountid(AccountGroup accountGroup, AccountMaster accountMaster);
    List<AccountMaster> findAllByAccountid(Long id);
    List<AccountMaster> findAllByAmaccountid(AccountMaster accountMaster);
    AccountMaster findByAmaccountid(AccountMaster accountMaster);
    List<AccountMaster> findAllByAccountname(String name);
    List<AccountMaster> findAllByReportvalue(String name);
    AccountMaster findByAccountname(String accountName);
    AccountMaster findByAccountid(Long id);
    @Query("from AccountMaster as am where  am.stringAccountCode like %:accountCode% and length(am.accountcode)=4 order by am.accountid DESC")
    List<AccountMaster> findAllBy(@Param("accountCode") String accountCode);
    @Query("from AccountMaster as am where  am.stringAccountCode  like %:accountCode%  and length(am.accountcode)=3 order by am.accountid DESC")
    List<AccountMaster> findAllBy1(@Param("accountCode") String accountCode);
    @Query("from AccountMaster as am where  length(am.accountcode)=4 order by am.accountid DESC")
    List<AccountMaster> findAllBy2(@Param("accountCode") String accountCode);




}
