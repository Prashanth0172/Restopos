package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.Employee;
import com.hyva.restopos.rest.entities.Item;
import com.hyva.restopos.rest.entities.PaymentMethod;
import com.hyva.restopos.rest.entities.State;
import com.hyva.restopos.rest.pojo.AddNewItemDTO;
import com.hyva.restopos.rest.pojo.EmployeePojo;
import com.hyva.restopos.rest.pojo.PaymentMethodDTO;
import com.hyva.restopos.rest.pojo.StatePojo;

import java.util.ArrayList;
import java.util.List;

public class PaymentMethodMapper {

    public static PaymentMethod MapPaymentmethodPojoToEntity(PaymentMethodDTO paymentMethodDTO){
        PaymentMethod paymentMethod = new PaymentMethod();
        paymentMethod.setPaymentmethodId(paymentMethodDTO.getPaymentmethodId());
        paymentMethod.setPaymentmethodName(paymentMethodDTO.getPaymentmethodName());
        paymentMethod.setPaymentmethodDescription(paymentMethodDTO.getPaymentmethodDescription());
        paymentMethod.setPaymentmethodType(paymentMethodDTO.getPaymentmethodType());
        paymentMethod.setStatus(paymentMethodDTO.getStatus());

        return paymentMethod;
    }

    public static List<PaymentMethodDTO> mapPaymentMethodEntityToPojo(List<PaymentMethod> List) {
        List<PaymentMethodDTO> list = new ArrayList<>();
        for (PaymentMethod config : List) {
            PaymentMethodDTO paymentMethodDTO = new PaymentMethodDTO();
            paymentMethodDTO.setPaymentmethodId(config.getPaymentmethodId());
            paymentMethodDTO.setPaymentmethodName(config.getPaymentmethodName());
            paymentMethodDTO.setPaymentmethodDescription(config.getPaymentmethodDescription());
            paymentMethodDTO.setPaymentmethodType(config.getPaymentmethodType());
            paymentMethodDTO.setStatus(config.getStatus());
            list.add(paymentMethodDTO);
        }
        return list;
    }




}
