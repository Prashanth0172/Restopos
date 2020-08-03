package com.hyva.restopos.rest.repository;


import com.hyva.restopos.rest.entities.OtherPayment;
import com.hyva.restopos.rest.entities.PosExpensePaymentTypes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PosExpensePayRepository extends JpaRepository<PosExpensePaymentTypes, Long> {

    List<PosExpensePaymentTypes> findAllByOtherPayment(OtherPayment id);
    PosExpensePaymentTypes findByOtherPayment(OtherPayment id);
}
