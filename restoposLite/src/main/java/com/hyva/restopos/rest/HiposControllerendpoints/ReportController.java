package com.hyva.restopos.rest.HiposControllerendpoints;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyva.restopos.rest.Hiposservice.HiposService;
import com.hyva.restopos.rest.Hiposservice.ReportService;
import com.hyva.restopos.rest.pojo.BasePojo;
import com.hyva.restopos.rest.pojo.PaginatedResponsePojo;
import com.hyva.restopos.rest.pojo.SalesRequestPojo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ReportController {
    @Autowired
    ReportService reportService;
    @RequestMapping(value = "/restaurantInvoice", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public PaginatedResponsePojo getRestaurantInvoiceList(@Valid SalesRequestPojo requestPojo) {
        return reportService.getRestaurantInvoiceList(requestPojo);
    }
    @RequestMapping(value = "/restaurantInvoicePaymentList", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity getRestaurantInvoicepaymentList(@RequestParam(value = "type",required = false)String type, @RequestParam(value = "table",required = false)String tableId, @Valid SalesRequestPojo requestPojo) {
        return ResponseEntity.status(200).body(reportService.getRestaurantInvoicepaymentList(type,tableId,requestPojo));
    }


    @RequestMapping(value = "/agentReport", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity agentReport(@Valid SalesRequestPojo requestPojo) {
        return ResponseEntity.status(200).body(reportService.agentReport(requestPojo));
    }


    @RequestMapping(value = "/salesDayEndInvoice", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public PaginatedResponsePojo getsalesDayEndInvoice(@Valid SalesRequestPojo requestPojo)throws Exception {
        return reportService.getsalesDayEndInvoice(requestPojo);
    }
    @RequestMapping(value = "/invoice/onLoadPageData", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity getSalesInvoiceLoadPageData() {
        return ResponseEntity.status(200).body(reportService.getSalesInvoiceOnLoadPageData());

    }
    @RequestMapping(value = "/restItemWise", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity restItemWise(@Valid SalesRequestPojo requestPojo) {
        return ResponseEntity.status(200).body(reportService.getrestItemWiseList(requestPojo));

    }

    @RequestMapping(path = "/downloadItemSalesExcelSheet", method = RequestMethod.GET)
    public ResponseEntity downloadItemSalesExcelSheet(@RequestParam("fromDate") String fromDate,
                                                      @RequestParam("toDate") String toDate,
                                                      @RequestParam("category") Long category,
                                                      @RequestParam("item") String item,
                                                      @RequestParam("filterApplied") boolean filterApplied) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        SalesRequestPojo salesRequestPojo = new SalesRequestPojo();
        salesRequestPojo.setFromDate(fromDate);
        salesRequestPojo.setToDate(toDate);
        if(category!=null){
            salesRequestPojo.setItemCategoryId(category);
        }
        if(!StringUtils.isEmpty(item)){
            salesRequestPojo.setItemId(item);
        }
        salesRequestPojo.setFilterApplied(true);
        headers.add("Content-Disposition", "attachment; filename=\"" + "ItemSalesListing.xls" + "\"");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        reportService.downloadItemSalesReportExcel(outputStream,salesRequestPojo);
        ByteArrayResource byteArrayResource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(byteArrayResource.contentLength())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(byteArrayResource);
    }
    @RequestMapping(path = "/downloadItemSalesPdf", method = RequestMethod.GET)
    public ResponseEntity downloadItemSalesPdf(@RequestParam("fromDate") String fromDate,
                                               @RequestParam("toDate") String toDate,
                                               @RequestParam("category") Long category,
                                               @RequestParam("item") String item,
                                               @RequestParam("filterApplied") boolean filterApplied) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        SalesRequestPojo salesRequestPojo = new SalesRequestPojo();
        salesRequestPojo.setFromDate(fromDate);
        salesRequestPojo.setToDate(toDate);
        if(category!=null){
            salesRequestPojo.setItemCategoryId(category);
        }
        if(!StringUtils.isEmpty(item)){
            salesRequestPojo.setItemId(item);
        }
        salesRequestPojo.setFilterApplied(true);
        headers.add("Content-Disposition", "attachment; filename=\"" + "ItemSalesListing.pdf" + "\"");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        reportService.downloadItemSalesPdf(outputStream,salesRequestPojo);
        ByteArrayResource byteArrayResource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(byteArrayResource.contentLength())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(byteArrayResource);
    }

    @RequestMapping(path = "/downloadMonthEndSalesExcelSheet", method = RequestMethod.GET)
    public ResponseEntity downloadMonthEndSalesExcelSheet(@RequestParam("fromDate") String fromDate,
                                                          @RequestParam("toDate") String toDate,
                                                          @RequestParam("category") Long category,
                                                          @RequestParam("item") String item,
                                                          @RequestParam("filterApplied") boolean filterApplied) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        SalesRequestPojo salesRequestPojo = new SalesRequestPojo();
        salesRequestPojo.setFromDate(fromDate);
        salesRequestPojo.setToDate(toDate);
        if(category!=null){
            salesRequestPojo.setItemCategoryId(category);
        }
        if(!StringUtils.isEmpty(item)){
            salesRequestPojo.setItemId(item);
        }
        salesRequestPojo.setFilterApplied(true);
        headers.add("Content-Disposition", "attachment; filename=\"" + "MonthEndSalesListing.xls" + "\"");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        reportService.downloadMonthEndSalesReportExcel(outputStream,salesRequestPojo);
        ByteArrayResource byteArrayResource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(byteArrayResource.contentLength())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(byteArrayResource);
    }

    @RequestMapping(path = "/downloadMonthEndSalesPdf", method = RequestMethod.GET)
    public ResponseEntity downloadMonthEndSalesPdf(@RequestParam("fromDate") String fromDate,
                                                   @RequestParam("toDate") String toDate,
                                                   @RequestParam("category") Long category,
                                                   @RequestParam("item") String item,
                                                   @RequestParam("filterApplied") boolean filterApplied) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        SalesRequestPojo salesRequestPojo = new SalesRequestPojo();
        salesRequestPojo.setFromDate(fromDate);
        salesRequestPojo.setToDate(toDate);
        if(category!=null){
            salesRequestPojo.setItemCategoryId(category);
        }
        if(!StringUtils.isEmpty(item)){
            salesRequestPojo.setItemId(item);
        }
        salesRequestPojo.setFilterApplied(true);
        headers.add("Content-Disposition", "attachment; filename=\"" + "MonthEndSalesListing.pdf" + "\"");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        reportService.downloadMonthEndSalesPdf(outputStream,salesRequestPojo);
        ByteArrayResource byteArrayResource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(byteArrayResource.contentLength())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(byteArrayResource);
    }
    @RequestMapping(value = "/getAgentList",method = RequestMethod.POST,produces = "application/json")
    public ResponseEntity getAgentList(@RequestParam(value = "searchText", required = false) String searchText,
                                       @RequestParam(value = "type",required = false)String type) {
        return ResponseEntity.status(200).body(reportService.getAgentList(searchText));
    }
    @RequestMapping(value = "/salesMonthEndInvoice", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity getsalesMonthEndInvoice(@Valid SalesRequestPojo requestPojo) {
        return ResponseEntity.status(200).body(reportService.getsalesMonthEndInvoice(requestPojo));
    }
    @RequestMapping(value ="/getDayEndReportList", method = RequestMethod.GET)
    public ResponseEntity getDayEndReportList(@Valid BasePojo basePojo)throws Exception{
        return ResponseEntity.status(200).body(reportService.getDayEndList(basePojo));
    }
    @RequestMapping(value="/getCancelledItemList",method=RequestMethod.GET)
    public ResponseEntity getCancelledItemList(@Valid SalesRequestPojo requestPojo)throws Exception{
        return ResponseEntity.ok(reportService.getCancelledItemList(requestPojo));
    }
    @RequestMapping(value = "/CancelItemExcel", method = RequestMethod.GET, produces = {org.springframework.http.MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity CancelItemExcel(@RequestParam(value="fromDate",required = false) String fromDate,
                                          @RequestParam(value="toDate",required = false) String toDate,
                                          @RequestParam(value="fromId",required = false) String fromId,
                                          @RequestParam(value = "filterApplied",required = false) boolean filterApplied) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SalesRequestPojo requestPojo=new SalesRequestPojo();
        requestPojo.setTokenNo(fromId);
        requestPojo.setFromDate(fromDate);
        requestPojo.setToDate(toDate);
        requestPojo.setFilterApplied(true);
        reportService.downloadCancelledItemReportExcelSheet(outputStream,requestPojo);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"" + "CancelItemreport.xls" + "\"");
        ByteArrayResource byteArrayResource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(byteArrayResource.contentLength())
                .contentType(org.springframework.http.MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(byteArrayResource);
    }
    @RequestMapping(value = "/CancelItemPdf", method = RequestMethod.GET, produces = {org.springframework.http.MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity CancelItemPdf(@RequestParam(value="fromDate",required = false) String fromDate,
                                        @RequestParam(value="toDate",required = false) String toDate,
                                        @RequestParam(value="fromId",required = false) String fromId,
                                        @RequestParam(value = "filterApplied",required = false) boolean filterApplied) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        SalesRequestPojo requestPojo=new SalesRequestPojo();
        requestPojo.setTokenNo(fromId);
        requestPojo.setFromDate(fromDate);
        requestPojo.setToDate(toDate);
        requestPojo.setFilterApplied(true);
        reportService.downloadCancelledItemReportPdf(outputStream,requestPojo);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"" + "CancelItemreport.pdf" + "\"");
        ByteArrayResource byteArrayResource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(byteArrayResource.contentLength())
                .contentType(org.springframework.http.MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(byteArrayResource);
    }

    @RequestMapping(path = "/downloadAgentExcelSheet", method = RequestMethod.GET)
    public ResponseEntity downloadAgentExcelSheet(@RequestParam("fromDate") String fromDate,
                                                       @RequestParam("toDate") String toDate,
                                                       @RequestParam("agentId") Long agentId) throws IOException {

        HttpHeaders headers = new HttpHeaders();
        SalesRequestPojo salesRequestPojo = new SalesRequestPojo();
        salesRequestPojo.setFromDate(fromDate);
        salesRequestPojo.setToDate(toDate);
        if(agentId!=null) {
            salesRequestPojo.setAgentId(agentId);
        }
        salesRequestPojo.setFilterApplied(true);
        headers.add("Content-Disposition", "attachment; filename=\"" + "AgentReport.xls" + "\"");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        reportService.downloadAgentExcelSheet(outputStream,  salesRequestPojo);
        ByteArrayResource byteArrayResource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(byteArrayResource.contentLength())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(byteArrayResource);
    }

    @RequestMapping(path = "/downloadAgentPdf", method = RequestMethod.GET)
    public ResponseEntity downloadAgentPdf(@RequestParam("fromDate") String fromDate,
                                           @RequestParam("toDate") String toDate,
                                           @RequestParam("agentId") Long agentId) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        SalesRequestPojo salesRequestPojo = new SalesRequestPojo();
        salesRequestPojo.setFromDate(fromDate);
        salesRequestPojo.setToDate(toDate);
        if(agentId!=null) {
            salesRequestPojo.setAgentId(agentId);
        }
        salesRequestPojo.setFilterApplied(true);
        headers.add("Content-Disposition", "attachment; filename=\"" + "AgentReport.pdf" + "\"");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        reportService.downloadAgentSalesPdf(outputStream,salesRequestPojo);
        ByteArrayResource byteArrayResource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(byteArrayResource.contentLength())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(byteArrayResource);
    }



    @RequestMapping(value="/getTokenList",method=RequestMethod.GET)
    public ResponseEntity getTokenList(){
        return ResponseEntity.ok(reportService.getTokenList());

    }
    @RequestMapping(value ="/getShiftEndReport", method = RequestMethod.GET)
    public ResponseEntity getDayEndReport(@RequestParam(value = "fromDate",required = false) String fromDate,
                                          @RequestParam(value = "shiftId") String shiftId)throws Exception{
        Map map=reportService.reportForShiftEndReport(fromDate,shiftId);
        return ResponseEntity.ok(map);
    }
    @RequestMapping(value ="/getShiftListReport", method = RequestMethod.GET)
    public ResponseEntity getShiftListReport(@RequestParam(value = "fromdate",required = false) String fromdate,
                                             @RequestParam(value="todate")String todate) throws Exception{
        SimpleDateFormat parseFormat =
                new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z ");
        Date startDate = parseFormat.parse(fromdate);
        Date toDate = parseFormat.parse(todate);
        return ResponseEntity.ok(reportService.shiftSales(startDate,toDate));
    }

    @RequestMapping(value = "/cancelSalesInvoice", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity getcancelledsalesInvoice(@Valid SalesRequestPojo requestPojo) {
        return ResponseEntity.status(200).body(reportService.getcancelledsalesInvoice(requestPojo));
    }
    @RequestMapping(path = "/downloadRestaurantInvoiceExcelSheet", method = RequestMethod.GET)
    public ResponseEntity downloadRestaurantInvoiceExcelSheet(@RequestParam("fromDate") String fromDate,
                                                              @RequestParam("toDate") String toDate,
                                                              @RequestParam("customerId") Long customerId,
                                                              @RequestParam("fromPID") Long fromPID,
                                                              @RequestParam("toPID") Long toPID,
                                                              @RequestParam("paymentId") Long paymentId,
                                                              @RequestParam("employee") Long employee,
                                                              @RequestParam("invoiceType") String invoiceType,
                                                              @RequestParam("filterApplied") boolean filterApplied,
                                                              @RequestParam(value = "type",required = false)String type1,
                                                              @RequestParam(value="table",required = false)String table) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        SalesRequestPojo salesRequestPojo = new SalesRequestPojo();
        salesRequestPojo.setFromDate(fromDate);
        salesRequestPojo.setToDate(toDate);
        salesRequestPojo.setPaymentId(paymentId);
        salesRequestPojo.setInvoiceType(invoiceType);
        salesRequestPojo.setEmployeeId(employee);
        String name ="RestaurantInvoiceListRep";
        if (customerId != null) {
            salesRequestPojo.setCustomerId(customerId);
        }
        if (fromPID != null && toPID != null) {
            salesRequestPojo.setFromSID(fromPID);
            salesRequestPojo.setToSID(toPID);
        }
        salesRequestPojo.setFilterApplied(true);
        if(StringUtils.equals(type1,"'discount'")||StringUtils.equals(type1,"discount")){
            name ="Discount Report";
        }
        if(StringUtils.equals(type1,"'freemeal'")||StringUtils.equals(type1,"freemeal")){
            name ="Freemeal Report";
        }
        if(StringUtils.equals(type1,"'tableWise'")||StringUtils.equals(type1,"tableWise")){
            name ="Table Wise Report";
        }
        headers.add("Content-Disposition", "attachment; filename=\"" +name+".xls" + "\"");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        reportService.downloadRestaurntInvoiceExcel(outputStream,salesRequestPojo,type1,table);
        ByteArrayResource byteArrayResource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(byteArrayResource.contentLength())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(byteArrayResource);
    }
    @RequestMapping(path = "/downloadOnlineInvoiceExcel", method = RequestMethod.GET)
    public ResponseEntity downloadOnlineInvoiceExcel(@RequestParam("fromDate") String fromDate,
                                                              @RequestParam("toDate") String toDate,
                                                              @RequestParam("customerId") Long customerId,
                                                              @RequestParam("fromPID") Long fromPID,
                                                              @RequestParam("toPID") Long toPID,
                                                              @RequestParam("employee") Long employee,
                                                              @RequestParam("agent") Long agent,
                                                              @RequestParam("filterApplied") boolean filterApplied,
                                                              @RequestParam(value = "type",required = false)String type1,
                                                              @RequestParam(value="table",required = false)String table) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        SalesRequestPojo salesRequestPojo = new SalesRequestPojo();
        salesRequestPojo.setFromDate(fromDate);
        salesRequestPojo.setToDate(toDate);
        salesRequestPojo.setEmployeeId(employee);
        String name ="OnlineInvoiceListing";
        if (customerId != null) {
            salesRequestPojo.setCustomerId(customerId);
        }
        if (agent != null) {
            salesRequestPojo.setAgentId(agent);
        }
        if (fromPID != null && toPID != null) {
            salesRequestPojo.setFromSID(fromPID);
            salesRequestPojo.setToSID(toPID);
        }
        salesRequestPojo.setFilterApplied(true);
        headers.add("Content-Disposition", "attachment; filename=\"" +name+".xls" + "\"");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        reportService.downloadOnlineInvoiceExcel(outputStream,salesRequestPojo,type1,table);
        ByteArrayResource byteArrayResource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(byteArrayResource.contentLength())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(byteArrayResource);
    }
    @RequestMapping(path = "/downloadOnlineInvoicePdf", method = RequestMethod.GET)
    public ResponseEntity downloadOnlineInvoicePdf(@RequestParam("fromDate") String fromDate,
                                                     @RequestParam("toDate") String toDate,
                                                     @RequestParam("customerId") Long customerId,
                                                     @RequestParam("fromPID") Long fromPID,
                                                     @RequestParam("toPID") Long toPID,
                                                     @RequestParam("employee") Long employee,
                                                     @RequestParam("agent") Long agent,
                                                     @RequestParam("filterApplied") boolean filterApplied,
                                                     @RequestParam(value = "type",required = false)String type1,
                                                     @RequestParam(value="table",required = false)String table) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        SalesRequestPojo salesRequestPojo = new SalesRequestPojo();
        salesRequestPojo.setFromDate(fromDate);
        salesRequestPojo.setToDate(toDate);
        salesRequestPojo.setEmployeeId(employee);
        String name ="OnlineInvoiceListing";
        if (customerId != null) {
            salesRequestPojo.setCustomerId(customerId);
        }
        if (agent != null) {
            salesRequestPojo.setAgentId(agent);
        }
        if (fromPID != null && toPID != null) {
            salesRequestPojo.setFromSID(fromPID);
            salesRequestPojo.setToSID(toPID);
        }
        salesRequestPojo.setFilterApplied(true);
        headers.add("Content-Disposition", "attachment; filename=\"" +name+".pdf" + "\"");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        reportService.downloadOnlineInvoicePdf(outputStream,salesRequestPojo,type1,table);
        ByteArrayResource byteArrayResource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(byteArrayResource.contentLength())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(byteArrayResource);
    }
    @RequestMapping(path = "/downloadWaiterReportExcelSheet", method = RequestMethod.GET)
    public ResponseEntity downloadWaiterReportExcelSheet(@RequestParam("fromDate") String fromDate,
                                                              @RequestParam("toDate") String toDate,
                                                              @RequestParam("customerId") Long customerId,
                                                              @RequestParam("fromPID") Long fromPID,
                                                              @RequestParam("toPID") Long toPID,
                                                              @RequestParam("employee") Long employee,
                                                              @RequestParam("invoiceType") String invoiceType,
                                                              @RequestParam("filterApplied") boolean filterApplied,
                                                              @RequestParam(value = "type",required = false)String type1,
                                                              @RequestParam(value="table",required = false)String table) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        SalesRequestPojo salesRequestPojo = new SalesRequestPojo();
        salesRequestPojo.setFromDate(fromDate);
        salesRequestPojo.setToDate(toDate);
        salesRequestPojo.setEmployeeId(employee);
        salesRequestPojo.setInvoiceType(invoiceType);
        String name ="Waiter Report";
        if (customerId != null) {
            salesRequestPojo.setCustomerId(customerId);
        }
        if (fromPID != null && toPID != null) {
            salesRequestPojo.setFromSID(fromPID);
            salesRequestPojo.setToSID(toPID);
        }
        salesRequestPojo.setFilterApplied(true);
        headers.add("Content-Disposition", "attachment; filename=\"" +name+".xls" + "\"");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        reportService.downloadWaiterReportExcel(outputStream,salesRequestPojo,type1,table);
        ByteArrayResource byteArrayResource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(byteArrayResource.contentLength())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(byteArrayResource);
    }
    @RequestMapping(path = "/downloadRestaurnatInvoicePdf", method = RequestMethod.GET)
    public ResponseEntity downloadRestaurnatInvoicePdf(@RequestParam("fromDate") String fromDate,
                                                       @RequestParam("toDate") String toDate,
                                                       @RequestParam("customerId") Long customerId,
                                                       @RequestParam("fromPID") Long fromPID,
                                                       @RequestParam("toPID") Long toPID,
                                                       @RequestParam("paymentId") Long paymentId,
                                                       @RequestParam("employee") Long employee,
                                                       @RequestParam("invoiceType") String invoiceType,
                                                       @RequestParam("filterApplied") boolean filterApplied,
                                                       @RequestParam(value = "type",required = false)String type1,
                                                       @RequestParam(value="table",required = false)String table) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        SalesRequestPojo salesRequestPojo = new SalesRequestPojo();
        String name = "RestaurantInvoiceRep";
        salesRequestPojo.setFromDate(fromDate);
        salesRequestPojo.setToDate(toDate);
        salesRequestPojo.setPaymentId(paymentId);
        salesRequestPojo.setInvoiceType(invoiceType);
        salesRequestPojo.setEmployeeId(employee);
        if (customerId != null) {
            salesRequestPojo.setCustomerId(customerId);
        }
        if (fromPID != null && toPID != null) {
            salesRequestPojo.setFromSID(fromPID);
            salesRequestPojo.setToSID(toPID);
        }


        salesRequestPojo.setFilterApplied(true);
        if(StringUtils.equals(type1,"'discount'")||StringUtils.equals(type1,"discount")){
            name ="Discount Report";
        }
        if(StringUtils.equals(type1,"'freemeal'")||StringUtils.equals(type1,"freemeal")){
            name ="Freemeal Report";
        }
        if(StringUtils.equals(type1,"'tableWise'")||StringUtils.equals(type1,"tableWise")){
            name ="Table Wise Report";
        }
        headers.add("Content-Disposition", "attachment; filename=\"" +name+ ".pdf" + "\"");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        reportService.downloadRestaurantInvoicePdf(outputStream,salesRequestPojo,type1,table);
        ByteArrayResource byteArrayResource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(byteArrayResource.contentLength())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(byteArrayResource);
    }
    @RequestMapping(path = "/downloadWaiterReportPdf", method = RequestMethod.GET)
    public ResponseEntity downloadWaiterReportPdf(@RequestParam("fromDate") String fromDate,
                                                       @RequestParam("toDate") String toDate,
                                                       @RequestParam("customerId") Long customerId,
                                                       @RequestParam("fromPID") Long fromPID,
                                                       @RequestParam("toPID") Long toPID,
                                                       @RequestParam("employee") Long employee,
                                                       @RequestParam("invoiceType") String invoiceType,
                                                       @RequestParam("filterApplied") boolean filterApplied,
                                                       @RequestParam(value = "type",required = false)String type1,
                                                       @RequestParam(value="table",required = false)String table) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        SalesRequestPojo salesRequestPojo = new SalesRequestPojo();
        String name = "WaiterReportPDF";
        salesRequestPojo.setFromDate(fromDate);
        salesRequestPojo.setToDate(toDate);
        salesRequestPojo.setEmployeeId(employee);
        salesRequestPojo.setInvoiceType(invoiceType);
        if (customerId != null) {
            salesRequestPojo.setCustomerId(customerId);
        }
        if (fromPID != null && toPID != null) {
            salesRequestPojo.setFromSID(fromPID);
            salesRequestPojo.setToSID(toPID);
        }
        salesRequestPojo.setFilterApplied(true);
        headers.add("Content-Disposition", "attachment; filename=\"" +name+ ".pdf" + "\"");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        reportService.downloadWaiterReportPdf(outputStream,salesRequestPojo,type1,table);
        ByteArrayResource byteArrayResource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(byteArrayResource.contentLength())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(byteArrayResource);
    }

    @RequestMapping(path = "/downloadCancelSalesInVoiceReportExcelSheet", method = RequestMethod.GET)
    public ResponseEntity downloadCancelSalesInVoiceReportExcelSheet(@RequestParam("fromDate") String fromDate,
                                                                     @RequestParam("toDate") String toDate,
                                                                     @RequestParam("customerId") Long customerId,
                                                                     @RequestParam("fromPID") String fromPID,
                                                                     @RequestParam("toPID") String toPID) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        SalesRequestPojo salesRequestPojo = new SalesRequestPojo();
        salesRequestPojo.setFromDate(fromDate);
        salesRequestPojo.setToDate(toDate);
        if (customerId != null) {
            salesRequestPojo.setCustomerId(customerId);
        }
        if (fromPID != null && toPID != null) {
            salesRequestPojo.setFromSINo(fromPID);
            salesRequestPojo.setToSINo(toPID);
        }
        salesRequestPojo.setFilterApplied(true);
        headers.add("Content-Disposition", "attachment; filename=\"" + "CancelSalesInVoiceReport.xls" + "\"");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        reportService.downloadCancelSalesExcel(outputStream, salesRequestPojo);
        ByteArrayResource byteArrayResource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(byteArrayResource.contentLength())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(byteArrayResource);
    }

    @RequestMapping(path = "/downloadCancelSalesInVoiceReportPdf", method = RequestMethod.GET)
    public ResponseEntity downloadCancelSalesInVoiceReportPdf(@RequestParam("fromDate") String fromDate,
                                                        @RequestParam("toDate") String toDate,
                                                        @RequestParam("customerId") Long customerId,
                                                        @RequestParam("fromPID") String fromPID,
                                                        @RequestParam("toPID") String toPID) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        SalesRequestPojo salesRequestPojo = new SalesRequestPojo();
        salesRequestPojo.setFromDate(fromDate);
        salesRequestPojo.setToDate(toDate);
        if (customerId != null) {
            salesRequestPojo.setCustomerId(customerId);
        }
        if (fromPID != null && toPID != null) {
            salesRequestPojo.setFromSINo(fromPID);
            salesRequestPojo.setToSINo(toPID);
        }
        salesRequestPojo.setFilterApplied(true);
        headers.add("Content-Disposition", "attachment; filename=\"" + "CancelSalesInVoiceReport.pdf" + "\"");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        reportService.downloadCancelSalesPdf(outputStream,salesRequestPojo);
        ByteArrayResource byteArrayResource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(byteArrayResource.contentLength())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(byteArrayResource);
    }
    @RequestMapping(value ="/getDayEndReport", method = RequestMethod.GET)
    public ResponseEntity getDayEndReport(@RequestParam(value = "fromDate",required = false) String fromDate){
        Map map=reportService.reportForPeriodListReport(fromDate);
        return ResponseEntity.ok(map);
    }
    @RequestMapping(value = "/downloadDayEndExcelSheet", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity downloadDayEndExcelSheet(@RequestParam("fromDate") String fromDate,
                                                   @RequestParam("toDate") String toDate){
        BasePojo basePojo = new BasePojo();
        basePojo.setFromDates(fromDate);
        basePojo.setToDate(toDate);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        reportService.downloadDayendListExcel(outputStream,basePojo);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"" + "DayEnd.xls" + "\"");
        ByteArrayResource byteArrayResource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(byteArrayResource.contentLength())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(byteArrayResource);
    }
    @RequestMapping(value = "/downloadDayEndPdf", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity downloadDayEndPdf(@RequestParam("fromDate") String fromDate,
                                            @RequestParam("toDate") String toDate){
        BasePojo basePojo = new BasePojo();
        basePojo.setFromDates(fromDate);
        basePojo.setToDate(toDate);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        reportService.downloadDayEndPdf(outputStream,basePojo);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"" + "DayEnd.pdf" + "\"");
        ByteArrayResource byteArrayResource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(byteArrayResource.contentLength())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(byteArrayResource);
    }
    @RequestMapping(value = "/dayEndExcel", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity dayEndExcel(@RequestParam(value="date",required = false) String date) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        reportService.downloadDaynedReportExcelSheet(outputStream,date);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"" + "DayEnd.xls" + "\"");
        ByteArrayResource byteArrayResource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(byteArrayResource.contentLength())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(byteArrayResource);
    }
    @RequestMapping(value = "/dayEndPdf", method = RequestMethod.GET, produces = {org.springframework.http.MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity dayEndPdf(@RequestParam(value="date",required = false) String date) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        reportService.downloadDaynedReportPdf(outputStream,date);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=\"" + "DayEnd.pdf" + "\"");
        ByteArrayResource byteArrayResource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(byteArrayResource.contentLength())
                .contentType(org.springframework.http.MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(byteArrayResource);
    }
}



