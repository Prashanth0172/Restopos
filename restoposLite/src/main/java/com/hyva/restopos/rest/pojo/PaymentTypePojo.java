package com.hyva.restopos.rest.pojo;

import java.util.List;

public class PaymentTypePojo {
    List<SalesReportListResponsePojo> salesReportListResponsePojoList;
    List<SalesReportListResponsePojo> paymentTypeTotals;

    public List<SalesReportListResponsePojo> getSalesReportListResponsePojoList() {
        return salesReportListResponsePojoList;
    }

    public void setSalesReportListResponsePojoList(List<SalesReportListResponsePojo> salesReportListResponsePojoList) {
        this.salesReportListResponsePojoList = salesReportListResponsePojoList;
    }

    public List<SalesReportListResponsePojo> getPaymentTypeTotals() {
        return paymentTypeTotals;
    }

    public void setPaymentTypeTotals(List<SalesReportListResponsePojo> paymentTypeTotals) {
        this.paymentTypeTotals = paymentTypeTotals;
    }
}
