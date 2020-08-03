package com.hyva.restopos.rest.repository;


import com.hyva.restopos.rest.entities.OtherReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OtherReceiptRepository extends JpaRepository<OtherReceipt, Long> {
    OtherReceipt findByOrno(String orNo);
   List<OtherReceipt> findAllByOrno(String orNo);
   List<OtherReceipt> findAllByStatusNotInAndOrnoContaining(List<String> orNo,String searchText);
   List<OtherReceipt> findAllByStatusNotIn(List<String> orNo);

}
