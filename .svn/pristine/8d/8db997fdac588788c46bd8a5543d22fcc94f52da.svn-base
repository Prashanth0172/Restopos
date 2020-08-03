package com.hyva.restopos.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hyva.restopos.rest.pojo.POSKOTItemOrderDTO;
import com.hyva.restopos.rest.pojo.RetailPrintDTO;
import com.hyva.restopos.util.PrintJobWatcher;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.RestTemplate;

import javax.print.*;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;


public interface BasePosPrinterService {

    Map formatAndPrintBill(RetailPrintDTO retailPrintDTO);
    Map formatAndPrintBillSI(RetailPrintDTO retailPrintDTO);
    Map formatAndPrintPayBill(RetailPrintDTO retailPrintDTO);
    Map placeOrdersToKOT(List<POSKOTItemOrderDTO> kitchenOrders, String tableName, String waiterName, String tokenNo, String orderType, String customerName, String instructions, String orderNo, String pax, String agentName);
    Map formatAndPrintCustomerBill(String jsonString);
    Map formatAndPrintBillEstimate(RetailPrintDTO retailPrintDTO, String locationId, int userId);

    default public JSONObject getParentChildConfiguration() {
        JSONObject masterCategoryJsonObj = null;

        try {
            ClassPathResource cpr = new ClassPathResource("static/resource/printerCategoryConf.json");
            byte[] bdata = FileCopyUtils.copyToByteArray(cpr.getInputStream());
            String jsonValue = new String(bdata, StandardCharsets.UTF_8);
            masterCategoryJsonObj = new JSONObject(jsonValue);
        } catch (IOException e) {
            //JSON File Not found
            //Means consider KOT-Category Mapping table in Configuration
            masterCategoryJsonObj = new JSONObject();
        } catch (JSONException e) {
            //JSON Parsing never happens
        } finally {
        }
        return masterCategoryJsonObj;
    }


    default public boolean feedPrinter(byte[] b, String printerName) {
        try {
            //printerName = "EPSON TM-T82 Receipt";
            AttributeSet attrSet = new HashPrintServiceAttributeSet(new PrinterName(printerName, null));
            DocPrintJob job = PrintServiceLookup.lookupPrintServices(null, attrSet)[0].createPrintJob();
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            Doc doc = new SimpleDoc(b, flavor, null);
            PrintJobWatcher pjDone = new PrintJobWatcher(job);
            job.print(doc, null);
            pjDone.waitForDone();
            //logger.info("Done !");
        } catch (PrintException pex) {
            //logger.info("Printer Error " + pex.getMessage());
            return false;
        } catch (Exception e) {
            //logger.info(e);
            return false;
        }
        return true;
    }

    default public boolean cloudFeedPrinter(byte[] b, String printerName){
        try {
            RestTemplate restTemplate = new RestTemplate();
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("printerType", printerName);
            objectNode.put("htmlString", new String(b, "UTF-8"));
            objectNode.put("cssStyle", "");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<String>(objectNode.toString(), headers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}
