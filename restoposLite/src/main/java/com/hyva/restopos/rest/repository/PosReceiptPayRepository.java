package com.hyva.restopos.rest.repository;


import com.hyva.restopos.rest.entities.OtherReceipt;
import com.hyva.restopos.rest.entities.PosReceiptPaymentTypes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PosReceiptPayRepository extends JpaRepository<PosReceiptPaymentTypes, Long> {

    List<PosReceiptPaymentTypes> findAllByOtherReceipt(OtherReceipt id);
    PosReceiptPaymentTypes findByOtherReceipt(OtherReceipt id);
}
