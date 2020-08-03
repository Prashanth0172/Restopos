package com.hyva.restopos.rest.HiposControllerendpoints;

import com.hyva.restopos.rest.Hiposservice.GTService;
import com.hyva.restopos.rest.entities.JournalEntry;
import com.hyva.restopos.rest.entities.OtherPayment;
import com.hyva.restopos.rest.pojo.*;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/gt")
public class GTController extends HttpServlet{
    @Autowired
    GTService gtService;

    @RequestMapping(value = "/saveOtherRecieptGt",method = RequestMethod.POST,consumes ="application/json",produces = "application/json")
    public ResponseEntity saveOtherRecieptGt(@RequestBody OtherRecieptGtDTO orgtDTO)throws IOException, JSONException,ParseException {
        OtherRecieptGtDTO rpdto = null;
        rpdto = gtService.createOtherRecieptNPayment(orgtDTO);
        return ResponseEntity.status(200).body(rpdto);
    }

    @RequestMapping(value = "/getDuplicateReceipt",method = RequestMethod.GET,produces="application/json")
    public ResponseEntity getDuplicateReceipt(@RequestParam(value = "searchText", required = false) String searchText) {
        List<PosInvoiceDTO> invDTO = gtService.getOtherReceiptDuplicateList(searchText);
        return ResponseEntity.status(200).body(invDTO);
    }
    @RequestMapping(value = "/duplicateReceiptPrint/{paymentId}",method = RequestMethod.POST,produces="application/json")
    public ResponseEntity duplicateReceiptPrint(@PathVariable(value = "paymentId") String paymentId) {
        OtherRecieptGtDTO rpdto = gtService.duplicateReceiptPrint(paymentId);
        return ResponseEntity.status(200).body(rpdto);
    }
    @RequestMapping(value = "/saveOtherPurchaseGt",method = RequestMethod.POST,consumes="application/json")
    public ResponseEntity saveOtherPurchaseGt(@RequestBody OtherPurchaseGtDTO purchaseDTO) throws IOException, SQLException,  JSONException,ParseException {
        purchaseDTO = gtService.createOtherPurchaseNPayment(purchaseDTO);
        return ResponseEntity.status(200).body(purchaseDTO);
    }
    @RequestMapping(value = "/saveJournalEntryGt",method = RequestMethod.POST,consumes ="application/json",produces = "application/json")
    public ResponseEntity saveJournalEntryGt(@RequestBody JournalEntryDTO jegtDTO) throws IOException, SQLException,  JSONException {
        JournalEntryDTO rpdto = null;
        jegtDTO.setStatus("Prepared");
        rpdto = gtService.createJournalEntryNPayment(jegtDTO);
        return ResponseEntity.status(200).body(rpdto);
    }
    @RequestMapping(value = "/getDuplicateJE",method = RequestMethod.GET,produces="application/json")
    public ResponseEntity getDuplicateJE(@RequestParam(value = "searchText") String searchText) {
        List<JournalEntry> otherPaymentList = gtService.getOtherJournalEntryList(searchText);
        return ResponseEntity.status(200).body(otherPaymentList);
    }
    @RequestMapping(value = "/duplicateJEPrint/{paymentId}",method = RequestMethod.POST,produces="application/json")
    public ResponseEntity duplicateJEPrint(@PathVariable(value = "paymentId") String paymentId) {
        JournalEntryDTO rpdto = gtService.duplicateJournalEntryPrint(paymentId);
        return ResponseEntity.status(200).body(rpdto);
    }

    @RequestMapping(value = "/getDuplicatePaymentVoucher",method = RequestMethod.GET,produces="application/json")
    public ResponseEntity getDuplicatePaymentVoucher(@RequestParam(value = "searchText") String searchText) {
        List<OtherPayment> otherPaymentList = gtService.getOtherPaymentDuplicateList(searchText);
        return ResponseEntity.status(200).body(otherPaymentList);
    }

    @RequestMapping(value = "/printDuplicateVoucher/{paymentId}",method = RequestMethod.POST,produces="application/json")
    public ResponseEntity printDuplicateVoucher(@PathVariable(value = "paymentId") String paymentId) {
        OtherPurchaseGtDTO rpdto = gtService.duplicateVoucherPrint(paymentId);
        return ResponseEntity.status(200).body(rpdto);
    }
    @RequestMapping(value = "/saveDraftGtSales",method = RequestMethod.POST,consumes ="application/json",produces = "application/json")
    public ResponseEntity saveDraftGtSales(@RequestBody OtherRecieptGtDTO otherRecieptGtDTO) throws IOException, ParseException, JSONException {
        OtherRecieptGtDTO rpdto = null;
        rpdto = gtService.createDraftGtSalesPayment(otherRecieptGtDTO);
        return ResponseEntity.status(200).body(rpdto);

    }
    @RequestMapping(value = "/getOtherGTSalesEdit/{invoiceNo}",method = RequestMethod.GET,produces="application/json")
    public ResponseEntity getOtherGTSalesEdit(@PathVariable(value = "invoiceNo") String invoiceNo) {
        OtherRecieptGtDTO otherRecieptGtDTO = gtService.retrieveGtSalesForEditPopulate(invoiceNo);
        if(otherRecieptGtDTO.getSelectedAccountList().isEmpty()){
            otherRecieptGtDTO.setMessage("Invoice Not Found");
            return  ResponseEntity.status(200).body(otherRecieptGtDTO);
        }
        return  ResponseEntity.status(200).body(otherRecieptGtDTO);
    }
    @RequestMapping(value = "/cancelGtReceipt",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity cancelDebitNote(@RequestParam("invoiceNo") String  invoiceNo) {

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", gtService.makeCancelledGtReceipt(invoiceNo));
        return ResponseEntity.status(HttpStatus.OK).body(objectNode.toString());
    }
    @RequestMapping(value = "/postGtforSales/{invoiceNo}",method = RequestMethod.GET,produces="application/json")
    public ResponseEntity postGtforSales(@PathVariable(value = "invoiceNo") String invoiceNo) throws IOException, SQLException,  JSONException {

        OtherRecieptGtDTO otherRecieptGtDTO = gtService.postGTSales(invoiceNo);
        return  ResponseEntity.status(200).body(otherRecieptGtDTO);
    }
    @RequestMapping(value = "/getOtherGTPurchaseEdit/{invoiceNo}",method = RequestMethod.GET,produces="application/json")
    public ResponseEntity getOtherGTPurchaseEdit(@PathVariable(value = "invoiceNo") String invoiceNo) {
        OtherPurchaseGtDTO otherPurchaseGtDTO = gtService.retrievePIDetailsForEditPopulate(invoiceNo);
        if(otherPurchaseGtDTO.getSelectedAccountList().isEmpty()){
            otherPurchaseGtDTO.setMessage("Invoice Not Found");
            return ResponseEntity.status(200).body(otherPurchaseGtDTO);
        }
        return ResponseEntity.status(200).body(otherPurchaseGtDTO);
    }
    @RequestMapping(value = "/saveDraftGtPurchase",method = RequestMethod.POST,consumes ="application/json",produces = "application/json")
    public ResponseEntity saveDraftGtPurchase(@RequestBody OtherPurchaseGtDTO otherPurchaseGtDTO) throws IOException, ParseException,
            SQLException,  JSONException {
        OtherPurchaseGtDTO rpdto = null;
        rpdto = gtService.createDraftGtPurchasePayment(otherPurchaseGtDTO);
        return ResponseEntity.status(200).body(rpdto);

    }
    @RequestMapping(value = "/postGtforExpense/{invoiceNo}",method = RequestMethod.GET,produces="application/json")
    public ResponseEntity postGtforExpense(@PathVariable(value = "invoiceNo") String invoiceNo) throws IOException, SQLException,  JSONException {

        OtherPurchaseGtDTO otherPurchaseGtDTO = gtService.postGTPurchase(invoiceNo);
        otherPurchaseGtDTO.setResult("SUCCESS");
        return  ResponseEntity.status(200).body(otherPurchaseGtDTO);
    }
    @RequestMapping(value = "/cancelGtforExpense/{invoiceNo}",method = RequestMethod.GET,produces="application/json")
    public ResponseEntity cancelGtforExpense(@PathVariable(value = "invoiceNo") String invoiceNo) {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message",gtService.cancelGtforExpense(invoiceNo));
        return ResponseEntity.status(HttpStatus.OK).body(objectNode.toString());
    }
    @RequestMapping(value = "/cancelGtExpense",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity cancelGtExpense(@RequestParam("invoiceNo") String  invoiceNo) {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message", gtService.makeCancelledGtExpense(invoiceNo));
        return ResponseEntity.status(HttpStatus.OK).body(objectNode.toString());
    }
    @RequestMapping(value = "/cancelGtforSales/{invoiceNo}",method = RequestMethod.GET,produces="application/json")
    public ResponseEntity cancelGtforSales(@PathVariable(value = "invoiceNo") String invoiceNo) {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("message",gtService.cancelGtforSales(invoiceNo));
        return ResponseEntity.status(HttpStatus.OK).body(objectNode.toString());
    }
    @RequestMapping(value = "/saveDraftJournalEntryGt",method = RequestMethod.POST,consumes ="application/json",produces = "application/json")
    public ResponseEntity saveDraftJournalEntryGt(@RequestBody JournalEntryDTO jegtDTO) throws IOException, SQLException,  JSONException {
        JournalEntryDTO rpdto = null;
        jegtDTO.setStatus("Draft");
        rpdto = gtService.createJournalEntryNPayment(jegtDTO);
        return ResponseEntity.status(200).body(rpdto);
    }
    @RequestMapping(value = "/getJREEdit/{invoiceNo}",method = RequestMethod.GET,produces="application/json")
    public ResponseEntity getJREEdit(@PathVariable(value = "invoiceNo") String invoiceNo) {
        JournalEntryDTO journalEntryDTO = gtService.retrieveJREForEditPopulate(invoiceNo);
        if(journalEntryDTO.getSelectedAccountList().isEmpty()){
            journalEntryDTO.setMessage("Invoice Not Found");
            return  ResponseEntity.status(200).body(journalEntryDTO);
        }
        return  ResponseEntity.status(200).body(journalEntryDTO);
    }
    @RequestMapping(value = "/postJournalEntryGt/{invoiceNo}",method = RequestMethod.GET,produces="application/json")
    public ResponseEntity postJournalEntryGt(@PathVariable(value = "invoiceNo") String invoiceNo) throws IOException, SQLException, JSONException {

        JournalEntryDTO journalEntryDTO = gtService.postJournalEntry(invoiceNo);
        journalEntryDTO.setResult("SUCCESS");
        return  ResponseEntity.status(200).body(journalEntryDTO);
    }

}
