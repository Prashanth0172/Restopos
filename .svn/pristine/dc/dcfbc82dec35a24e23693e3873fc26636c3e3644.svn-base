package com.hyva.restopos.rest.repository;

import com.hyva.restopos.rest.entities.Employee;
import com.hyva.restopos.rest.entities.PaymentMethod;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod,Long>{

    PaymentMethod findAllByPaymentmethodId(Long id);
    List<PaymentMethod> findAllByPaymentmethodTypeAndPaymentmethodId(String type,Long id);
    PaymentMethod findByPaymentmethodName(String name);
    PaymentMethod findAllByPaymentmethodNameAndPaymentmethodIdNotIn(String name, Long id);
    PaymentMethod findAllByPaymentmethodName(String name);
    List<PaymentMethod> findAllByStatus(String status);
    PaymentMethod findByPaymentmethodType(String paymentType);
    List<PaymentMethod>findAllByPaymentmethodNameContainingAndStatus(String countryName,String status);
    List<PaymentMethod>findAllByPaymentmethodNameContainingAndStatus(String countryName,String status,Pageable pageable);
    PaymentMethod findFirstByStatus(String status,Sort sort);
    List<PaymentMethod>findAllByStatus(String status,Pageable pageable);
    PaymentMethod findFirstByPaymentmethodNameContainingAndStatus(String countryName,String status,Sort sort);
}
