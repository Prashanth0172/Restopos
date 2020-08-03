package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.PaymentVoucher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PaymentVoucherRepository extends JpaRepository<PaymentVoucher,Long> {
    PaymentVoucher findAllByCouponid(Long id);
    PaymentVoucher findAllByCode(String name);
    PaymentVoucher findAllByCodeAndCouponidNotIn(String name, Long id);
    List<PaymentVoucher> findAllByCodeContainingAndStatus(String name,String status);
    List<PaymentVoucher> findAllByStatus(String status);
    PaymentVoucher findFirstByCodeContainingAndStatus(String countryName, String status,Sort sort);
    List<PaymentVoucher>findAllByCodeContainingAndStatus(String countryName, String status, Pageable pageable);
    PaymentVoucher findFirstByStatus(String status, Sort sort);
    List<PaymentVoucher> findAllByStatus(String status, Pageable pageable);
    List<PaymentVoucher> findAllByStatusAndTypeAndDateaddedLessThanEqualAndFixeddateGreaterThanEqual(String status, String type,Date fromDate,Date toDate);
    List<PaymentVoucher> findAllByCodeAndDateaddedLessThanEqualAndFixeddateGreaterThanEqual(String code,Date fromDate,Date toDate);
}