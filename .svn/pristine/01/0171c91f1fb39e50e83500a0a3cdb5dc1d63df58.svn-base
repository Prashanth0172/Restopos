package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.PaymentVoucher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PaymentVoucherRepository extends JpaRepository<PaymentVoucher,Long> {
    PaymentVoucher findAllByVouId(Long id);
    PaymentVoucher findAllByVocherCode(String name);
    PaymentVoucher findAllByVocherCodeAndVouIdNotIn(String name, Long id);
    List<PaymentVoucher> findAllByVocherCodeContainingAndStatus(String name,String status);
    List<PaymentVoucher> findAllByStatus(String status);
    PaymentVoucher findFirstByVocherCodeContainingAndStatus(String countryName, String status,Sort sort);
    List<PaymentVoucher>findAllByVocherCodeContainingAndStatus(String countryName, String status, Pageable pageable);
    PaymentVoucher findFirstByStatus(String status, Sort sort);
    List<PaymentVoucher> findAllByStatus(String status, Pageable pageable);
    List<PaymentVoucher> findAllByStatusAndDefaultVoucherAndFromDateLessThanEqualAndToDateGreaterThanEqual(String status, String value,Date fromDate,Date toDate);
    List<PaymentVoucher> findAllByVocherCodeAndFromDateLessThanEqualAndToDateGreaterThanEqual(String code,Date fromDate,Date toDate);
}