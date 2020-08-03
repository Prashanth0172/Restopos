package com.hyva.restopos.rest.Mapper;

import com.hyva.restopos.rest.entities.PaymentVoucher;
import com.hyva.restopos.rest.pojo.PaymentVoucherPojo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class VoucherMapper {

    public static PaymentVoucher MapPojoToEntity(PaymentVoucherPojo paymentVoucherPojo)throws Exception{
        PaymentVoucher paymentVoucher = new PaymentVoucher();
        paymentVoucher.setCouponid(paymentVoucherPojo.getCoupon_id());
        paymentVoucher.setName(paymentVoucherPojo.getName());
        paymentVoucher.setCode(paymentVoucherPojo.getCode());
        paymentVoucher.setType(paymentVoucherPojo.getType());
        paymentVoucher.setDiscount(paymentVoucherPojo.getDiscount());
        paymentVoucher.setMintotal(paymentVoucherPojo.getMin_total());
        paymentVoucher.setRedemptions(paymentVoucherPojo.getRedemptions());
        paymentVoucher.setCustomerredemptions(paymentVoucherPojo.getCustomer_redemptions());
        paymentVoucher.setOrderrestriction(paymentVoucherPojo.getOrder_restriction());
        paymentVoucher.setValidity(paymentVoucherPojo.getValidity());
        paymentVoucher.setDescription(paymentVoucherPojo.getDescription());
        paymentVoucher.setDateadded(paymentVoucherPojo.getDate_added());
        paymentVoucher.setFixeddate(paymentVoucherPojo.getFixed_date());
        paymentVoucher.setFixedfromtime(paymentVoucherPojo.getFixed_from_time());
        paymentVoucher.setFixedtotime(paymentVoucherPojo.getFixed_to_time());
        paymentVoucher.setPeriodstartdate(paymentVoucherPojo.getPeriod_start_date());
        paymentVoucher.setPeriodenddate(paymentVoucherPojo.getPeriod_end_date());
        paymentVoucher.setRecurringevery(paymentVoucherPojo.getRecurring_every());
        paymentVoucher.setRecurringfromtime(paymentVoucherPojo.getRecurring_from_time());
        paymentVoucher.setRecurringtotime(paymentVoucherPojo.getRecurring_to_time());
        paymentVoucher.setStatus(paymentVoucherPojo.getStatus());
        return paymentVoucher;
    }

    public static List<PaymentVoucherPojo> mapEntityToPojo(List<PaymentVoucher> List) {
        List<PaymentVoucherPojo> list = new ArrayList<>();
        for (PaymentVoucher voucher : List) {
            PaymentVoucherPojo paymentVoucherPojo = new PaymentVoucherPojo();
            paymentVoucherPojo.setCoupon_id(voucher.getCouponid());
            paymentVoucherPojo.setName(voucher.getName());
            paymentVoucherPojo.setCode(voucher.getCode());
            paymentVoucherPojo.setType(voucher.getType());
            paymentVoucherPojo.setDiscount(voucher.getDiscount());
            paymentVoucherPojo.setMin_total(voucher.getMintotal());
            paymentVoucherPojo.setRedemptions(voucher.getRedemptions());
            paymentVoucherPojo.setCustomer_redemptions(voucher.getCustomerredemptions());
            paymentVoucherPojo.setOrder_restriction(voucher.getOrderrestriction());
            paymentVoucherPojo.setValidity(voucher.getValidity());
            paymentVoucherPojo.setDescription(voucher.getDescription());
            paymentVoucherPojo.setDate_added(voucher.getDateadded());
            paymentVoucherPojo.setFixed_date(voucher.getFixeddate());
            paymentVoucherPojo.setFixed_from_time(voucher.getFixedfromtime());
            paymentVoucherPojo.setFixed_to_time(voucher.getFixedtotime());
            paymentVoucherPojo.setPeriod_start_date(voucher.getPeriodstartdate());
            paymentVoucherPojo.setPeriod_end_date(voucher.getPeriodenddate());
            paymentVoucherPojo.setRecurring_every(voucher.getRecurringevery());
            paymentVoucherPojo.setRecurring_from_time(voucher.getRecurringfromtime());
            paymentVoucherPojo.setRecurring_to_time(voucher.getRecurringtotime());
            paymentVoucherPojo.setStatus(voucher.getStatus());
            list.add(paymentVoucherPojo);
        }
        return list;
    }


}
