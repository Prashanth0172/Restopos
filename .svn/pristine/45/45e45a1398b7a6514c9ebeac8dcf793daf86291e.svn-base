package com.hyva.restopos.rest.repository;



import com.hyva.restopos.rest.entities.OtherPayment;
import com.hyva.restopos.rest.entities.OtherReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OtherPaymentRepository extends JpaRepository<OtherPayment, Long> {
    OtherPayment findByOpNo(String opNo);
    List<OtherPayment> findAllByOpNo(String opNo);
    List<OtherPayment> findAllByStatusNotIn(List<String> opNo);
    List<OtherPayment> findAllByStatus(String status);
    List<OtherPayment> findAllByStatusNotInAndOpNoContaining(List<String> opNo, String searchText);


}
