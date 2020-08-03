package com.hyva.restopos.rest.repository;


import com.hyva.restopos.rest.entities.OcOtherPaymentWithoutItem;
import com.hyva.restopos.rest.entities.OtherPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OcOtherPaymentWOItemRep extends JpaRepository<OcOtherPaymentWithoutItem, Long> {
    List<OcOtherPaymentWithoutItem> findAllByOcdetails(Long id);
    List<OcOtherPaymentWithoutItem> findByOcdetails(OtherPayment otherPayment);

//    List<OcOtherPaymentWithoutItem> findByOcdetails(OtherPayment id);
}
