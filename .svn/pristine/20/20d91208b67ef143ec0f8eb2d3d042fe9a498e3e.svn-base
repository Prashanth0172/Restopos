package com.hyva.restopos.rest.repository;


import com.hyva.restopos.rest.entities.OcOtherReceiptWithoutItem;
import com.hyva.restopos.rest.entities.OtherReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OcOtherReceiptWOItemRep extends JpaRepository<OcOtherReceiptWithoutItem, Long> {
    List<OcOtherReceiptWithoutItem> findAllByOcdetails(OtherReceipt id);
    List<OcOtherReceiptWithoutItem> findByOcdetails(OtherReceipt id);
}
