package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.UserAccountSetup;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserAccountSetupRepository extends JpaRepository<UserAccountSetup,Integer> {
    @Query("select u from UserAccountSetup as u where (u.phone=:phone or u.fullName=:fullName or u.email=:email)")
    List<UserAccountSetup> findBy(@Param(value = "phone")String phone,@Param(value = "fullName")String fullName,@Param(value = "email")String email);
    @Query("select u from UserAccountSetup as u where (u.phone=:phone or u.fullName=:fullName or u.email=:email) and u.status=:status")
    List<UserAccountSetup> findByStatus(@Param(value = "phone")String phone,@Param(value = "fullName")String fullName,@Param(value = "email")String email,@Param(value = "status")String status);
    UserAccountSetup findByUserloginId(String loginId);
    UserAccountSetup findByUserloginIdAndUseraccountIdNotIn(String loginId,int userId);
    UserAccountSetup findAllByEmailAndFullNameAndPasswordUser(String email,String fullname,String password);
    UserAccountSetup findByFullName(String name);
    List<UserAccountSetup> findAllByStatus(String status);
    List<UserAccountSetup>findAllByUserloginIdContainingAndStatus(String countryName,String status);
    List<UserAccountSetup>findAllByUserloginIdContainingAndStatus(String countryName,String status,Pageable pageable);
    UserAccountSetup findFirstByStatus(String status,Sort sort);
    List<UserAccountSetup>findAllByStatus(String status,Pageable pageable);
    UserAccountSetup findFirstByUserloginIdContainingAndStatus(String countryName,String status,Sort sort);
    UserAccountSetup findAllByFullName(String name);
}
