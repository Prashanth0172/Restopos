package com.hyva.restopos.rest.Hiposservice;

import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hyva.restopos.rest.Mapper.AgentMapper;
import com.hyva.restopos.rest.Mapper.ReportMapper;
import com.hyva.restopos.rest.entities.*;
import com.hyva.restopos.rest.pojo.*;
import com.hyva.restopos.rest.repository.*;
import com.hyva.restopos.util.HiposUtil;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.html.WebColors;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.Transformers;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestAttribute;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.text.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.poi.hssf.usermodel.HeaderFooter.date;

/**
 * Created by hinext04 on 29/7/19.
 */
@Service
public class ReportService {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    PaymentVoucherRepository paymentVoucherRepository;
    @Autowired
    SalesInvoiceRepository salesInvoiceRepository;
    @Autowired
    PaymentMethodRepository paymentMethodRepository;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    ItemRespository itemRespository;
//    @Autowired
//    ReportMapper reportMapper;
    @Autowired
    PosPaymentTypesRepository posPaymentTypesRepository;
    @Autowired
    SalesInvoiceDetailsRepository salesInvoiceDetailsRepository;
    @Autowired
    RestaurantTokenRecordRepository restaurantTokenRecordRepository;
    @Autowired
            TablesPosRepository tablesPosRepository;
    @Autowired
    UserAccountSetupRepository userAccountSetupRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    HiposService  hiposService;
    @Autowired
    AgentRepository agentRepository;
    @Autowired
    ShiftRepository shiftRepository;
    int paginatedConstants = 14;

    public ReportOnLoadPageData getSalesInvoiceOnLoadPageData() {
        ReportOnLoadPageData reportOnLoadPageData = new ReportOnLoadPageData();
        reportOnLoadPageData.setCustomerList(getAllCustomers());
        reportOnLoadPageData.setSalesList(getAllSalesInvoice());
        reportOnLoadPageData.setSalesCancelList(getAllSalesCancelInvoice());
        reportOnLoadPageData.setPaymentList(getAllPaymentListReport());
        reportOnLoadPageData.setPaymentVoucherList(getAllPaymentVoucherReport());
        reportOnLoadPageData.setTableList(getAllTableList());
        return reportOnLoadPageData;
    }
    public List<RestaurantTokenRecordDto> getCancelledItemList(SalesRequestPojo requestPojo)throws Exception{
        return getRestaurantTokenRecord(requestPojo);
    }
    public List<String> getTokenList(){
        return getTokenRecordList();
    }

    @javax.transaction.Transactional
    public List<RestaurantTokenRecordDto> shiftSales( Date startDate, Date toDate)throws Exception {
        List<Shift> shiftList = new ArrayList<>();
        shiftList= shiftRepository.findAllByStatus("Active");
        Calendar cal = Calendar.getInstance(); // get calendar instance
        cal.setTime(startDate); // set cal to date
        cal.set(Calendar.HOUR_OF_DAY, 0); // set hour to midnight
        cal.set(Calendar.MINUTE, 0); // set minute in hour
        cal.set(Calendar.SECOND, 0); // set second in minute
        cal.set(Calendar.MILLISECOND, 0);
        Date zeroedDate = cal.getTime();
        cal.setTime(toDate); // set cal to date
        cal.set(Calendar.HOUR_OF_DAY, 23); // set hour to midnight
        cal.set(Calendar.MINUTE, 59); // set minute in hour
        cal.set(Calendar.SECOND, 59); // set second in minute
        cal.set(Calendar.MILLISECOND, 0);
        Date zeroToDate = cal.getTime();
        List<RestaurantTokenRecordDto> restaurantTokenRecords = new ArrayList<>();
        for (Shift shift : shiftList) {
            List<String> stringList = new ArrayList<>();
            String[] times=null;
            if(org.springframework.util.StringUtils.pathEquals(shift.getFromTime(),"00:00")){
                times ="24:00".split(":");
            }else {
                times = shift.getFromTime().split(":");
            }
            String[] times1 = null;
            if(org.springframework.util.StringUtils.pathEquals(shift.getToTime(),"00:00")){
                times1 = "24:00".split(":");
            }else {
                times1 = shift.getToTime().split(":");
            }
            List<java.sql.Time> intervals = new ArrayList<>(25);
            java.sql.Time startTime = new java.sql.Time(Integer.parseInt(times[0]), Integer.parseInt(times[1]), 0);
            java.sql.Time endTime = new java.sql.Time(Integer.parseInt(times1[0]), Integer.parseInt(times1[1]), 0);
            intervals.add(startTime);
            cal.setTime(startTime);
            while (cal.getTime().before(endTime)) {
                cal.add(Calendar.MINUTE, 1);
                intervals.add(new java.sql.Time(cal.getTimeInMillis()));
            }
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            for (java.sql.Time time : intervals) {
                stringList.add(sdf.format(time));
            }
            List<Date> restaurantTokenRecord = restaurantTokenRecordRepository.findAllBy(zeroedDate,zeroToDate,stringList);
            for(Date map:restaurantTokenRecord){
                cal.setTime(map); // set cal to date
                cal.set(Calendar.HOUR_OF_DAY, 0); // set hour to midnight
                cal.set(Calendar.MINUTE, 0); // set minute in hour
                cal.set(Calendar.SECOND, 0); // set second in minute
                cal.set(Calendar.MILLISECOND, 0);
                zeroedDate = cal.getTime();
                cal.setTime(map); // set cal to date
                cal.set(Calendar.HOUR_OF_DAY, 23); // set hour to midnight
                cal.set(Calendar.MINUTE, 59); // set minute in hour
                cal.set(Calendar.SECOND, 59); // set second in minute
                cal.set(Calendar.MILLISECOND, 0);
                zeroToDate = cal.getTime();
                List<Map> invoicesList = restaurantTokenRecordRepository.findAllBySiId(zeroedDate,zeroToDate,stringList);
                RestaurantTokenRecordDto restaurantTokenRecordDto = new RestaurantTokenRecordDto();
                restaurantTokenRecordDto.setShiftName(shift.getShiftName());
                restaurantTokenRecordDto.setShiftId(shift.getShiftId().toString());
                for(Map restaurantTokenRecordDto1:invoicesList){
                    SalesInvoice salesInvoice = salesInvoiceRepository.findAllBySIId(Long.parseLong(restaurantTokenRecordDto1.get("SIId").toString()));
                    List<SalesInvoiceDetails> salesInvoiceDetails = salesInvoiceDetailsRepository.findBySIId(salesInvoice);
                    restaurantTokenRecordDto1.put("totalItems",salesInvoiceDetails.size());
                }
                double amt = invoicesList.stream().mapToDouble(o -> Double.parseDouble(o.get("totalAmt").toString())).sum();
                double items = invoicesList.stream().mapToDouble(o ->Double.parseDouble(o.get("totalItems").toString())).sum();
                restaurantTokenRecordDto.setTotalItems(items);
                restaurantTokenRecordDto.setTotalAmt(amt);
                restaurantTokenRecordDto.setDate(map);
                restaurantTokenRecords.add(restaurantTokenRecordDto);
            }

        }
        return restaurantTokenRecords;
    }

    @Transactional
    public Map reportForShiftEndReport(String dt,String shiftId)throws Exception {
        Map result = new HashMap();
        Shift shift=shiftRepository.findAllByShiftId(Long.valueOf(shiftId));
        List<String> stringList = new ArrayList<>();
        String[] times=shift.getFromTime().split(":");
        String[] times1=shift.getToTime().split(":");
        List<java.sql.Time> intervals = new ArrayList<>(25);
        java.sql.Time startTime = new java.sql.Time(Integer.parseInt(times[0]), Integer.parseInt(times[1]), 0);
        java.sql.Time endTime = new java.sql.Time(Integer.parseInt(times1[0]), Integer.parseInt(times1[1]), 0);
        intervals.add(startTime);
        Calendar cal = Calendar.getInstance(); // get calendar instance
        cal.setTime(startTime);
        while (cal.getTime().before(endTime)) {
            cal.add(Calendar.MINUTE, 1);
            intervals.add(new java.sql.Time(cal.getTimeInMillis()));
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        for (java.sql.Time time : intervals) {
            stringList.add(sdf.format(time));
        }
        double itemCount = 0;
        List<Category> itemCategoryList = categoryRepository.findAll();
        Date date=new SimpleDateFormat("yyyy-MM-dd").parse(dt);
        cal.setTimeInMillis(date.getTime());
        cal.setTime(cal.getTime()); // set cal to date
        cal.set(Calendar.HOUR_OF_DAY, 0); // set hour to midnight
        cal.set(Calendar.MINUTE, 0); // set minute in hour
        cal.set(Calendar.SECOND, 0); // set second in minute
        cal.set(Calendar.MILLISECOND, 0);
        Date zeroedDate = cal.getTime();
        cal.setTime(cal.getTime()); // set cal to date
        cal.set(Calendar.HOUR_OF_DAY, 24); // set hour to midnight
        cal.set(Calendar.MINUTE, 60); // set minute in hour
        cal.set(Calendar.SECOND, 60); // set second in minute
        cal.set(Calendar.MILLISECOND, 0);
        Date zeroToDate = cal.getTime();
        List<String> invoiceList = restaurantTokenRecordRepository.findAllByshift(stringList,zeroedDate,zeroToDate);
        if (invoiceList.isEmpty())
            return result;
        List<PosPaymentTypes> posPaymentTypes = posPaymentTypesRepository.findAllBy(invoiceList);
        double totalAmt = posPaymentTypes.stream().mapToDouble(o -> o.getSalesInvoice().getTotalReceivable()).sum();
        double cashAmt = posPaymentTypes.stream().mapToDouble(o -> o.getTotalCashPayment()).sum();
        double serviceCharge = posPaymentTypes.stream().mapToDouble(o -> o.getSalesInvoice().getServiceChargeAmt()).sum();
        double roundingOffAmt = posPaymentTypes.stream().mapToDouble(o -> o.getRoundingAdjustment()).sum();
        double discountAmt = posPaymentTypes.stream().mapToDouble(o -> o.getSalesInvoice().getTotalDiscountAmount()).sum();
        List<MultiBankPayment> multiBankPaymentList = new ArrayList<>();
        List<MultiCardPayment> multiCardPaymentList = new ArrayList<>();
        List<MultiVoucherPayment> multiVoucherPaymentList = new ArrayList<>();
        Gson json = new Gson();
        java.lang.reflect.Type voucherPayment = new TypeToken<ArrayList<MultiVoucherPayment>>() {
        }.getType();
        java.lang.reflect.Type cardPayment = new TypeToken<ArrayList<MultiCardPayment>>() {
        }.getType();
        java.lang.reflect.Type bankPayment = new TypeToken<ArrayList<MultiBankPayment>>() {
        }.getType();
        for (PosPaymentTypes posPaymentTypes1 : posPaymentTypes) {
            multiBankPaymentList.addAll(json.fromJson(posPaymentTypes1.getBankPayment(), bankPayment));
            multiCardPaymentList.addAll(json.fromJson(posPaymentTypes1.getCardPayment(), cardPayment));
            multiVoucherPaymentList.addAll(json.fromJson(posPaymentTypes1.getVoucherPayment(), voucherPayment));
        }
        for (MultiVoucherPayment multiVoucherPayment : multiVoucherPaymentList) {
            MultiBankPayment multiBankPayment = new MultiBankPayment();
            multiBankPayment.setAmount(multiVoucherPayment.getVoucherAmt());
            multiBankPayment.setPaymentType(multiVoucherPayment.getPaymentType().toString());
            multiBankPaymentList.add(multiBankPayment);
        }
        for (MultiCardPayment multiCardPayment : multiCardPaymentList) {
            MultiBankPayment multiBankPayment = new MultiBankPayment();
            multiBankPayment.setAmount(multiCardPayment.getCardAmt());
            multiBankPayment.setPaymentType(multiCardPayment.getPaymentType().toString());
            multiBankPaymentList.add(multiBankPayment);
        }
        for (MultiBankPayment multiBankPayment : multiBankPaymentList) {
            if (!org.springframework.util.StringUtils.isEmpty(multiBankPayment.getPaymentType())) {
                PaymentMethod paymentMethod = hiposService.getPaymentmethodObject(Long.parseLong(multiBankPayment.getPaymentType()));
                multiBankPayment.setPaymentType(paymentMethod.getPaymentmethodName());
            }
        }
        Map<String, Double> resultMap = multiBankPaymentList.stream().filter(c -> c.getPaymentType() != null).collect(
                Collectors.groupingBy(MultiBankPayment::getPaymentType, Collectors.summingDouble(MultiBankPayment::getAmount)));
        resultMap.put("Cash", cashAmt);
        for (Category itemCategory : itemCategoryList) {
            List<Map> categoryItemList = salesInvoiceDetailsRepository.findAllBy(itemCategory,invoiceList);
            //holds the reference of category total
            float total = 0;
            for (Map<String, Double> element : categoryItemList) {
                total += element.get("totalAmtReceived");
            }
            Map newVar = new HashMap<>();
            newVar.put("itemId", "");
            newVar.put("itemName", "");
            //newVar.put("totalAmtReceived", "");
            newVar.put("QtySold", "");
            newVar.put("total", String.valueOf(total));

            if (!categoryItemList.isEmpty() && categoryItemList.get(0).get("itemId") != null) {
                itemCount = itemCount + categoryItemList.size();
                categoryItemList.add(newVar);
                result.put(itemCategory.getItemCategoryName(), categoryItemList);
            }
        }
        result.put("Discount", discountAmt);
        if (resultMap.get("Discount Voucher") == null) {
            resultMap.put("Discount Voucher", 0D);
        }
        double amt = resultMap.get("Discount Voucher").doubleValue() - discountAmt;
        resultMap.put("Discount Voucher", amt);
        result.put("Rounding Off", roundingOffAmt);
        result.put("Service Charge", serviceCharge);
        result.put("Total Amount", totalAmt);
        result.put("Payment", resultMap);
        result.put("Item Count", itemCount);
        result.put("Shift Name", shift.getShiftName());
        return result;

    }
    @Transactional
    public List<String> getTokenRecordList(){
        List<Map> restaurantTokenRecordList = restaurantTokenRecordRepository.findAllByTokenList();
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<RestaurantTokenRecordDto>>() {
        }.getType();
        List<String> listfinal=new ArrayList<>();
        for (Map val : restaurantTokenRecordList) {
            List<RestaurantTokenRecordDto> restaurantTokenRecordDtoList = gson.fromJson(val.get("itemDeteails").toString(), type);
            for (RestaurantTokenRecordDto restaurantTokenRecordDto : restaurantTokenRecordDtoList) {
                String tokenNo=null;
                tokenNo=val.get("token").toString();
                    if (StringUtils.equalsIgnoreCase(restaurantTokenRecordDto.getStatus(), "Cancel")) {
                    if(!listfinal.contains(tokenNo)) {
                        listfinal.add(tokenNo);
                    }
                }
            }
        }
        return listfinal;
    }
    public List<RestaurantTokenRecordDto> getRestaurantTokenRecord(SalesRequestPojo requestPojo)throws Exception {
        Date zeroedDate =null;
        Date zeroToDate =null;
        List<Map> restaurantTokenRecordList = new ArrayList<>();
        if (StringUtils.isEmpty(requestPojo.getToDate()) || StringUtils.isEmpty(requestPojo.getFromDate())) {
            String dat = requestPojo.getFromDate();
            long date = new SimpleDateFormat("yyyy-MM-dd").parse(dat, new ParsePosition(0)).getTime();
            zeroedDate = new java.sql.Date(date);
            String dat1 = requestPojo.getToDate();
            long date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dat1, new ParsePosition(0)).getTime();
           zeroToDate = new java.sql.Date(date1);
        }
        else {
            long date1InMilli = Long.parseLong(requestPojo.getFromDate());
            java.util.Date fromDate = new Date(date1InMilli);
            Calendar cal = Calendar.getInstance();       // get calendar instance
            cal.setTime(fromDate);                           // set cal to date
            cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
            cal.set(Calendar.MINUTE, 0);                 // set minute in hour
            cal.set(Calendar.SECOND, 0);                 // set second in minute
            cal.set(Calendar.MILLISECOND, 0);
             zeroedDate = cal.getTime();
            date1InMilli = Long.parseLong(requestPojo.getToDate());
            java.util.Date toDate = new Date(date1InMilli);
            cal.setTime(toDate);                           // set cal to date
            cal.set(Calendar.HOUR_OF_DAY, 23);            // set hour to midnight
            cal.set(Calendar.MINUTE, 59);                 // set minute in hour
            cal.set(Calendar.SECOND, 59);                 // set second in minute
            cal.set(Calendar.MILLISECOND, 0);
             zeroToDate = cal.getTime();
        }

        if (StringUtils.isNotEmpty(requestPojo.getTokenNo())) {
            restaurantTokenRecordList= restaurantTokenRecordRepository.findAllByToken(zeroedDate,zeroToDate, requestPojo.getTokenNo());
        }else {
            restaurantTokenRecordList= restaurantTokenRecordRepository.findAll( zeroedDate, zeroToDate);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<RestaurantTokenRecordDto>>() {
        }.getType();
        List<RestaurantTokenRecordDto> listfinal = new ArrayList<>();
        for (Map val : restaurantTokenRecordList) {
            if(StringUtils.isNotEmpty(val.get("itemDeteails").toString())) {
                List<RestaurantTokenRecordDto> restaurantTokenRecordDtoList = gson.fromJson(val.get("itemDeteails").toString(), type);
                for (RestaurantTokenRecordDto restaurantTokenRecordDto : restaurantTokenRecordDtoList) {
                    restaurantTokenRecordDto.setTableName(val.get("tableName").toString());
                    restaurantTokenRecordDto.setWaiterName(val.get("waiterName").toString());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date dayEndDate = simpleDateFormat.parse(val.get("date").toString());
                    restaurantTokenRecordDto.setDate(dayEndDate);
                    restaurantTokenRecordDto.setTokenNo(val.get("tokenNo").toString());
                    if (StringUtils.equalsIgnoreCase(restaurantTokenRecordDto.getStatus(), "Cancel")) {
                        listfinal.add(restaurantTokenRecordDto);
                    }
                }
            }
        }
        return listfinal;

    }

    @Transactional
    public void downloadCancelledItemReportExcelSheet(OutputStream out, SalesRequestPojo inventoryRequestPojo) {
        try {
            XSSFWorkbook hwb = new XSSFWorkbook();
            XSSFSheet sheet = hwb.createSheet("First Sheet");
            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            Company company = companyRepository.findAllByStatus("Active");
            headerRow.setHeightInPoints((3 * sheet.getDefaultRowHeightInPoints()));
            headerRow.createCell(2).setCellValue(company.getCompanyName() + "\n" + company.getAddress() + "\n" + "Cancel Item Listing");
            CellRangeAddress cell = new CellRangeAddress(0, 2, 2, 4);
            sheet.addMergedRegion(cell);
            RegionUtil.setBorderTop(2, cell, sheet, hwb);
            RegionUtil.setBorderLeft(2, cell, sheet, hwb);
            RegionUtil.setBorderRight(2, cell, sheet, hwb);
            RegionUtil.setBorderBottom(2, cell, sheet, hwb);
            org.apache.poi.ss.usermodel.Row headerRow1 = sheet.createRow(3);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(inventoryRequestPojo.getFromDate()));
            headerRow1.createCell(0).setCellValue("From Date");
            headerRow1.createCell(1).setCellValue(formatter.format(calendar.getTime()));
            calendar.setTimeInMillis(Long.parseLong(inventoryRequestPojo.getToDate()));
            headerRow1.createCell(2).setCellValue("To Date");
            headerRow1.createCell(3).setCellValue(formatter.format(calendar.getTime()));
            org.apache.poi.ss.usermodel.Row headerRow2=sheet.createRow(4);
            headerRow2.createCell(0).setCellValue("Token");
            if(!org.springframework.util.StringUtils.isEmpty(inventoryRequestPojo.getTokenNo())) {
                headerRow2.createCell(1).setCellValue(inventoryRequestPojo.getTokenNo());
            }
            org.apache.poi.ss.usermodel.Row headerRow4 = sheet.createRow(5);
            headerRow4.createCell(0).setCellValue("Date");
            headerRow4.createCell(1).setCellValue("Table Name");
            headerRow4.createCell(2).setCellValue("Waiter Name");
            headerRow4.createCell(3).setCellValue("Token No");
            headerRow4.createCell(4).setCellValue("Cancelled Items");
            int i = 5;
            for (RestaurantTokenRecordDto list :getRestaurantTokenRecord(inventoryRequestPojo)) {
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(++i);
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String str = df.format(list.getDate());
                row.createCell(0).setCellValue(str);
                row.createCell(1).setCellValue(list.getTableName());
                row.createCell(2).setCellValue(list.getWaiterName());
                row.createCell(3).setCellValue(list.getTokenNo());
                row.createCell(4).setCellValue(list.getItemName()+list.getQty());
            }
            hwb.write(out);
            out.close();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } catch (Exception gex) {
            gex.printStackTrace();
        }
    }


    @Transactional
    public void downloadCancelledItemReportPdf(OutputStream outputStream, SalesRequestPojo inventoryRequestPojo) {
        try {
            Font font1 = new Font(getcustomfont(), 12F, Font.BOLD);
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Chunk CONNECT = new Chunk(new LineSeparator(1, 100, Color.BLACK, Element.ALIGN_JUSTIFIED, 3f));
            document.add(CONNECT);
            document.add(new Paragraph("", font1));
            Chunk CONNECT1 = new Chunk(new LineSeparator(1, 100, Color.WHITE, Element.ALIGN_JUSTIFIED, 3f));
            document.add(CONNECT1);
            PdfPTable table = createCancelItemReportReport(inventoryRequestPojo);
            table.setHeaderRows(1);
            document.add(printCompanyDetails());
            document.add(table);
            document.add(CONNECT1);
            Paragraph foot = new Paragraph();
            Phrase ph5 = new Phrase("Terms And Condition:Terms                    Prepared By                                        Approved By   ", font1);
            foot.add(ph5);
            document.add(foot);
            document.add(CONNECT);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    public PdfPTable createCancelItemReportReport(SalesRequestPojo inventoryRequestPojo) throws Exception {
        Font font1 = new Font(getcustomfont(), 8, Font.NORMAL, Color.BLACK);
        int a = 0;
        PdfPTable tab = new PdfPTable(1);
        Font f = new Font(getcustomfont(), 15, Font.BOLD, Color.WHITE);
        PdfPTable tbl = new PdfPTable(a + 2);
        PdfPTable tbl1 = new PdfPTable(1);
        PdfPTable table = new PdfPTable(a + 5);
        Font font = new Font(getcustomfont(), 10, Font.NORMAL, Color.WHITE);
        Color myColor = WebColors.getRGBColor("#326397");
        PdfPCell p = new PdfPCell(new Phrase("CancelItemReport", f));
        p.setHorizontalAlignment(Element.ALIGN_CENTER);
        p.setBackgroundColor(myColor);
        tab.addCell(p);
        PdfPCell p1 = new PdfPCell(new Phrase("From Date", font));
        p1.setBackgroundColor(myColor);
        PdfPCell p2 = new PdfPCell(new Phrase("To Date",font));
        p2.setBackgroundColor(myColor);
        tbl.addCell(p1);
        tbl.addCell(p2);
        PdfPCell p3 = new PdfPCell(new Phrase("Token", font));
        p3.setBackgroundColor(myColor);
        tbl1.addCell(p3);
        PdfPCell pc2 = new PdfPCell(new Phrase("Date", font));
        pc2.setBackgroundColor(myColor);
        PdfPCell pc3 = new PdfPCell(new Phrase("Table Name", font));
        pc3.setBackgroundColor(myColor);
        PdfPCell pc4 = new PdfPCell(new Phrase("Waiter Name", font));
        pc4.setBackgroundColor(myColor);
        PdfPCell pc5 = new PdfPCell(new Phrase("Token No", font));
        pc5.setBackgroundColor(myColor);
        PdfPCell pc6 = new PdfPCell(new Phrase("Cancelled Items", font));
        pc6.setBackgroundColor(myColor);
        table.addCell(pc2);
        table.addCell(pc3);
        table.addCell(pc4);
        table.addCell(pc5);
        table.addCell(pc6);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(inventoryRequestPojo.getFromDate()));
        tbl.addCell(new Phrase(formatter.format(calendar.getTime())+ "", font1));
        calendar.setTimeInMillis(Long.parseLong(inventoryRequestPojo.getToDate()));
        tbl.addCell(new Phrase(formatter.format(calendar.getTime()) + "", font1));
        if(!org.springframework.util.StringUtils.isEmpty(inventoryRequestPojo.getTokenNo())) {
            tbl1.addCell(new Phrase(inventoryRequestPojo.getTokenNo()+ "", font1));
        }
        table.setWidthPercentage(100);
        for (RestaurantTokenRecordDto list :getRestaurantTokenRecord(inventoryRequestPojo)) {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String str = df.format(list.getDate());
            table.addCell(new Phrase(str + "", font1));
            table.addCell(new Phrase(list.getTableName() + "", font1));
            table.addCell(new Phrase(list.getWaiterName() + "", font1));
            table.addCell(new Phrase(list.getTokenNo() + "", font1));
            table.addCell(new Phrase((list.getItemName()+list.getQty()) + "", font1));
        }
        tab.addCell(tbl);
        tab.addCell(tbl1);
        tab.addCell(table);
        return tab;
    }

    public PaginatedResponsePojo getrestItemWiseList(SalesRequestPojo salesRequestPojo){
        PaginatedResponsePojo paginatedResponsePojo=new PaginatedResponsePojo();
        String dat = salesRequestPojo.getFromDate();
        long date = new SimpleDateFormat("yyyy-MM-dd").parse(dat, new ParsePosition(0)).getTime();
        java.sql.Date fromDate = new java.sql.Date(date);
        String dat1 = salesRequestPojo.getToDate();
        long date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dat1, new ParsePosition(0)).getTime();
        java.sql.Date toDate = new java.sql.Date(date1);
        List<Map> list = new ArrayList<>();
//        if (StringUtils.isNotEmpty(salesRequestPojo.getItemId())&&salesRequestPojo.getItemCategoryId() == null) {
//            list=salesInvoiceDetailsRepository.findByItemSalesbyItem(Long.parseLong(salesRequestPojo.getItemId()),fromDate,toDate);
//        }
//        if (salesRequestPojo.getItemCategoryId() != null&&StringUtils.isEmpty(salesRequestPojo.getItemId())) {
//            list=salesInvoiceDetailsRepository.findByItemSalesbyCategory(salesRequestPojo.getItemCategoryId(),fromDate,toDate);
//        }
//        if(StringUtils.isNotEmpty(salesRequestPojo.getItemId())&&salesRequestPojo.getItemCategoryId()!=null){
//            list=salesInvoiceDetailsRepository.findByItemSalesbyCategoryAndItem(Long.parseLong(salesRequestPojo.getItemId()),salesRequestPojo.getItemCategoryId(),fromDate,toDate);
//        }
//        if(StringUtils.isEmpty(salesRequestPojo.getItemId())&&salesRequestPojo.getItemCategoryId()==null){
//            list=salesInvoiceDetailsRepository.findByItemSales(fromDate,toDate);
//        }
        paginatedResponsePojo.setData(list);
        paginatedResponsePojo.setMapList(list);
        return paginatedResponsePojo;
    }

    @Transactional
    public void downloadMonthEndSalesReportExcel(OutputStream out, SalesRequestPojo salesRequestPojo) {
        try {
            PaginatedResponsePojo paginatedResponsePojo = new PaginatedResponsePojo();
            paginatedResponsePojo = getsalesMonthEndInvoice(salesRequestPojo);
            XSSFWorkbook hwb = new XSSFWorkbook();
            XSSFSheet sheet = hwb.createSheet("First Sheet");
            Row headerRow = sheet.createRow(0);
            Company company = companyRepository.findAllByStatus("Active");
            headerRow.setHeightInPoints((3 * sheet.getDefaultRowHeightInPoints()));
            headerRow.createCell(2).setCellValue(company.getCompanyName() + "\n" + company.getAddress() + "\n" + "MonthEndListing");
            CellRangeAddress cell = new CellRangeAddress(0, 2, 2, 4);
            sheet.addMergedRegion(cell);
            RegionUtil.setBorderTop(2, cell, sheet, hwb);
            RegionUtil.setBorderLeft(2, cell, sheet, hwb);
            RegionUtil.setBorderRight(2, cell, sheet, hwb);
            RegionUtil.setBorderBottom(2, cell, sheet, hwb);
            Row headerRow3 = sheet.createRow(3);
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = sdf.parse(salesRequestPojo.getFromDate());
            DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
            String fromDateFormat = df1.format(fromDate);
            Date toDate = sdf.parse(salesRequestPojo.getToDate());
            String toDateFormat = df1.format(toDate);
            headerRow3.createCell(0).setCellValue("From Date");
            headerRow3.createCell(1).setCellValue(fromDateFormat);
            headerRow3.createCell(2).setCellValue("To Date");
            headerRow3.createCell(3).setCellValue(toDateFormat);
            Row headerRow4 = sheet.createRow(4);
            headerRow4.createCell(0).setCellValue("Item Name");
            if (salesRequestPojo.getItemId() != null) {
                Item item = itemRespository.findAllByItemId(Long.parseLong(salesRequestPojo.getItemId()));
                headerRow4.createCell(1).setCellValue(item.getItemName());
            }
            Row headerRow2 = sheet.createRow(5);
            headerRow2.createCell(0).setCellValue("Category Name");
            if (salesRequestPojo.getItemCategoryId() != null) {
                Category category = categoryRepository.findAllByItemCategoryId(salesRequestPojo.getItemCategoryId());
                headerRow2.createCell(1).setCellValue(category.getItemCategoryName());
            }
            Row headerRow1 = sheet.createRow(6);
            headerRow1.createCell(0).setCellValue("Category Name");
            headerRow1.createCell(1).setCellValue("Item Name");
            headerRow1.createCell(2).setCellValue("Quantity");
            headerRow1.createCell(3).setCellValue("Total Amount");
            int i = 6;
            double totalAmt = 0.00;
            for (SalesReportListResponsePojo list : paginatedResponsePojo.getSalesReportList()) {
                totalAmt = totalAmt + list.getInvoiceAmount();
                Row row = sheet.createRow(++i);
                row.createCell(0).setCellValue(list.getItemCategoryName());
                row.createCell(1).setCellValue(list.getItemName());
                row.createCell(2).setCellValue(list.getQty());
                row.createCell(3).setCellValue(list.getInvoiceAmount());
            }
            Row row = sheet.createRow(++i);
            row.createCell(0).setCellValue("Grand Total");
            row.createCell(1).setCellValue("");
            row.createCell(2).setCellValue("");
            row.createCell(3).setCellValue(new DecimalFormat("#,##0.00").format(totalAmt));
            hwb.write(out);
            out.close();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } catch (Exception gex) {
            gex.printStackTrace();
        }
    }

    @Transactional
    public void downloadItemSalesReportExcel(OutputStream out, SalesRequestPojo salesRequestPojo) {
        try {
            PaginatedResponsePojo paginatedResponsePojo = new PaginatedResponsePojo();
            paginatedResponsePojo = getrestItemWiseList(salesRequestPojo);
            XSSFWorkbook hwb = new XSSFWorkbook();
            XSSFSheet sheet = hwb.createSheet("First Sheet");
            Row headerRow = sheet.createRow(0);
            Company company = companyRepository.findAllByStatus("Active");
            headerRow.setHeightInPoints((3 * sheet.getDefaultRowHeightInPoints()));
            headerRow.createCell(2).setCellValue(company.getCompanyName() + "\n" + company.getAddress() + "\n" + "Item Sales Listing");
            CellRangeAddress cell = new CellRangeAddress(0, 2, 2, 4);
            sheet.addMergedRegion(cell);
            RegionUtil.setBorderTop(2, cell, sheet, hwb);
            RegionUtil.setBorderLeft(2, cell, sheet, hwb);
            RegionUtil.setBorderRight(2, cell, sheet, hwb);
            RegionUtil.setBorderBottom(2, cell, sheet, hwb);
            Row headerRow1 = sheet.createRow(3);
            headerRow1.createCell(0).setCellValue("From Date");
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = sdf.parse(salesRequestPojo.getFromDate());
            DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
            String fromDateFormat = df1.format(fromDate);
            Date toDate = sdf.parse(salesRequestPojo.getToDate());
            String toDateFormat = df1.format(toDate);
            headerRow1.createCell(1).setCellValue(fromDateFormat);
            headerRow1.createCell(2).setCellValue("To Date");
            headerRow1.createCell(3).setCellValue(toDateFormat);
            Row headerRow2 = sheet.createRow(4);
            headerRow2.createCell(0).setCellValue("Item Name");
            if (salesRequestPojo.getItemId() != null) {
                Item item = itemRespository.findAllByItemId(Long.parseLong(salesRequestPojo.getItemId()));
                headerRow2.createCell(1).setCellValue(item.getItemName());
            }
            Row headerRow3 = sheet.createRow(5);
            headerRow3.createCell(0).setCellValue("Category Name");
            if (salesRequestPojo.getItemCategoryId() != null) {
                Category category = categoryRepository.findAllByItemCategoryId(salesRequestPojo.getItemCategoryId());
                headerRow2.createCell(1).setCellValue(category.getItemCategoryName());
            }
            Row headerRow4 = sheet.createRow(6);
            headerRow4.createCell(0).setCellValue("Date");
            headerRow4.createCell(1).setCellValue("Category Name");
            headerRow4.createCell(2).setCellValue("Item Name");
            headerRow4.createCell(3).setCellValue("Quantity");
            headerRow4.createCell(4).setCellValue("Total Amount");
            int i = 6;
            double totalAmt = 0.00;
            for (Map list : paginatedResponsePojo.getMapList()) {
                totalAmt = totalAmt + Double.parseDouble(list.get("invoiceAmount").toString());
                Row row = sheet.createRow(++i);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dayEndDate = simpleDateFormat.parse(list.get("date").toString());
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                String str = df.format(dayEndDate);
                row.createCell(0).setCellValue(str);
                row.createCell(1).setCellValue(list.get("itemCategoryName").toString());
                row.createCell(2).setCellValue(list.get("itemName").toString());
                row.createCell(3).setCellValue(list.get("qty").toString());
                row.createCell(4).setCellValue(Double.parseDouble(list.get("invoiceAmount").toString()));
            }
            Row row = sheet.createRow(++i);
            row.createCell(0).setCellValue("Grand Total");
            row.createCell(1).setCellValue("");
            row.createCell(2).setCellValue("");
            row.createCell(3).setCellValue("");
            row.createCell(4).setCellValue(new DecimalFormat("#,##0.00").format(totalAmt));
            hwb.write(out);
            out.close();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } catch (Exception gex) {
            gex.printStackTrace();
        }
    }
    public static BaseFont getcustomfont() {
        String relativeWebPath = "fonts/arial.ttf";
        return FontFactory.getFont("arial", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 0.8f, Font.NORMAL, Color.BLACK).getBaseFont();
    }
    @Transactional
    public void downloadItemSalesPdf(OutputStream outputStream, SalesRequestPojo salesRequestPojo) {
        try {
            Font font1 = new Font(getcustomfont(), 12F, Font.BOLD);
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Chunk CONNECT = new Chunk(new LineSeparator(1, 100, Color.BLACK, Element.ALIGN_JUSTIFIED, 3f));
            document.add(CONNECT);
            document.add(new Paragraph("", font1));
            Chunk CONNECT1 = new Chunk(new LineSeparator(1, 100, Color.WHITE, Element.ALIGN_JUSTIFIED, 3f));
            document.add(CONNECT1);
            PdfPTable table = createFirstTableItemSales(salesRequestPojo);
            PdfPTable table1 = createFirstTableItemSalesReport(salesRequestPojo);
            table.setHeaderRows(1);
            document.add(printCompanyDetails());
            document.add(table);
            document.add(table1);
            document.add(CONNECT1);
            Paragraph foot = new Paragraph();
            Phrase ph5 = new Phrase("Terms And Condition:           Prepared By                  Approved By   ", font1);
            foot.add(ph5);
            document.add(foot);
            document.add(CONNECT);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void downloadMonthEndSalesPdf(OutputStream outputStream, SalesRequestPojo salesRequestPojo) {
        try {
            Font font1 = new Font(getcustomfont(), 12F, Font.BOLD);
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Chunk CONNECT = new Chunk(new LineSeparator(1, 100, Color.BLACK, Element.ALIGN_JUSTIFIED, 3f));
            document.add(CONNECT);
            document.add(new Paragraph("", font1));
            Chunk CONNECT1 = new Chunk(new LineSeparator(1, 100, Color.WHITE, Element.ALIGN_JUSTIFIED, 3f));
            document.add(CONNECT1);
            PdfPTable table = createFirstTableMonthEndSales(salesRequestPojo);
            PdfPTable table1 = createFirstTableMonthEndSalesReport(salesRequestPojo);
            table.setHeaderRows(1);
            document.add(printCompanyDetails());
            document.add(table);
            document.add(table1);
            document.add(CONNECT1);
            Paragraph foot = new Paragraph();
            Phrase ph5 = new Phrase("Terms And Condition:           Prepared By                  Approved By   ", font1);
            foot.add(ph5);
            document.add(foot);
            document.add(CONNECT);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Transactional
    public void downloadAgentSalesPdf(OutputStream outputStream, SalesRequestPojo salesRequestPojo) {
        try {
            Font font1 = new Font(getcustomfont(), 12F, Font.BOLD);
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Chunk CONNECT = new Chunk(new LineSeparator(1, 100, Color.BLACK, Element.ALIGN_JUSTIFIED, 3f));
            document.add(CONNECT);
            document.add(new Paragraph("", font1));
            Chunk CONNECT1 = new Chunk(new LineSeparator(1, 100, Color.WHITE, Element.ALIGN_JUSTIFIED, 3f));
            document.add(CONNECT1);
            PdfPTable table = createTableAgentSales(salesRequestPojo);
            PdfPTable table1 = createFirstTableAgentSalesReport(salesRequestPojo);
            table.setHeaderRows(1);
            document.add(printCompanyDetails());
            document.add(table);
            document.add(table1);
            document.add(CONNECT1);
            Paragraph foot = new Paragraph();
            Phrase ph5 = new Phrase("Terms And Condition:           Prepared By                  Approved By   ", font1);
            foot.add(ph5);
            document.add(foot);
            document.add(CONNECT);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PdfPTable createTableAgentSales(SalesRequestPojo salesRequestPojo)throws Exception {
        Font font1 = new Font(getcustomfont(), 8, Font.NORMAL, Color.BLACK);
        int a = 0;
        PdfPTable tbl = new PdfPTable(a + 2);
        PdfPTable tbl2 = new PdfPTable(1);
        PdfPTable tab = new PdfPTable(1);
        Font f = new Font(getcustomfont(), 15, Font.BOLD, Color.WHITE);
        Font font = new Font(getcustomfont(), 10, Font.NORMAL, Color.WHITE);
        Color myColor = WebColors.getRGBColor("#326397");
        PdfPCell p = new PdfPCell(new Phrase("Agent Report Listing", f));
        p.setHorizontalAlignment(Element.ALIGN_CENTER);
        p.setBackgroundColor(myColor);
        tab.addCell(p);
        PdfPCell p1 = new PdfPCell(new Phrase("From Date", font));
        p1.setBackgroundColor(myColor);
        PdfPCell p2 = new PdfPCell(new Phrase("To Date", font));
        p2.setBackgroundColor(myColor);
        tbl.addCell(p1);
        tbl.addCell(p2);
        PdfPCell p4 = new PdfPCell(new Phrase("Agent Name", font));
        p4.setBackgroundColor(myColor);
        tbl2.addCell(p4);
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = sdf.parse(salesRequestPojo.getFromDate());
        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
        String fromDateFormat = df1.format(fromDate);
        Date toDate = sdf.parse(salesRequestPojo.getToDate());
        String toDateFormat = df1.format(toDate);
        tbl.addCell(new Phrase(fromDateFormat+ "", font1));
        tbl.addCell(new Phrase(toDateFormat + "", font1));
        if (salesRequestPojo.getAgentId() != null) {
            Agent agent = agentRepository.findAllByAgentId(salesRequestPojo.getAgentId());
            tbl2.addCell(new Phrase(agent.getAgentName() + "", font1));
        }
        tab.addCell(tbl);
        tab.addCell(tbl2);
        return tab;
    }


    @Transactional
    public void downloadAgentExcelSheet(OutputStream out, SalesRequestPojo salesRequestPojo) {
        try {
            PaginatedResponsePojo paginatedResponsePojo = new PaginatedResponsePojo();
            paginatedResponsePojo = agentReport(salesRequestPojo);
            XSSFWorkbook hwb = new XSSFWorkbook();
            XSSFSheet sheet = hwb.createSheet("First Sheet");
            Row headerRow = sheet.createRow(0);
            Company company = companyRepository.findAllByStatus("Active");
            headerRow.setHeightInPoints((3 * sheet.getDefaultRowHeightInPoints()));
            headerRow.createCell(2).setCellValue(company.getCompanyName() + "\n" + company.getAddress() + "\n" + "Agent Listing");
            CellRangeAddress cell = new CellRangeAddress(0, 2, 2, 4);
            sheet.addMergedRegion(cell);
            RegionUtil.setBorderTop(2, cell, sheet, hwb);
            RegionUtil.setBorderLeft(2, cell, sheet, hwb);
            RegionUtil.setBorderRight(2, cell, sheet, hwb);
            RegionUtil.setBorderBottom(2, cell, sheet, hwb);
            Row headerRow1 = sheet.createRow(3);
            headerRow1.createCell(0).setCellValue("From Date");
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = sdf.parse(salesRequestPojo.getFromDate());
            DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
            String fromDateFormat = df1.format(fromDate);
            Date toDate = sdf.parse(salesRequestPojo.getToDate());
            String toDateFormat = df1.format(toDate);
            headerRow1.createCell(1).setCellValue(fromDateFormat);
            headerRow1.createCell(2).setCellValue("To Date");
            headerRow1.createCell(3).setCellValue(toDateFormat);
            Row headerRow2 = sheet.createRow(4);
            headerRow2.createCell(0).setCellValue("Agent");
            if(salesRequestPojo.getAgentId()!=null) {
                Agent agent = agentRepository.findAllByAgentId(salesRequestPojo.getAgentId());
                headerRow2.createCell(1).setCellValue(agent.getAgentName());
            }
            Row headerRow3 = sheet.createRow(5);
            headerRow3.createCell(0).setCellValue("Date");
            headerRow3.createCell(1).setCellValue("Invoice No");
            headerRow3.createCell(2).setCellValue("Invoice Amount");
            headerRow3.createCell(3).setCellValue("Agent Name");
            headerRow3.createCell(4).setCellValue("Agent No");
            headerRow3.createCell(5).setCellValue("Agent Email");
            headerRow3.createCell(6).setCellValue("Commission");
            headerRow3.createCell(7).setCellValue("Commission Amt");

            int i = 5;
            double invAmt = 0.00,totalComm=0.00,com=0.00,amt=0.00;
            for (Map list :paginatedResponsePojo.getMapList()) {
                invAmt = invAmt + Double.parseDouble(list.get("totalAmount").toString());
                com= Double.parseDouble(list.get("commission").toString());
                amt= Double.parseDouble(list.get("totalAmount").toString());
                totalComm= (com*amt)/100;
                Row row = sheet.createRow(++i);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dayEndDate = simpleDateFormat.parse(list.get("invdate").toString());
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String str = df.format(dayEndDate);
                row.createCell(0).setCellValue(str);
                row.createCell(1).setCellValue(list.get("invoiceNO").toString());
                row.createCell(2).setCellValue(Double.parseDouble(list.get("totalAmount").toString()));
                row.createCell(3).setCellValue(list.get("agentName").toString());
                if(list.get("accountNo")!=null) {
                    row.createCell(4).setCellValue(list.get("accountNo").toString());
                }
                else{
                    row.createCell(4).setCellValue("");
                }
                if(list.get("email")!=null) {
                    row.createCell(5).setCellValue(list.get("email").toString());
                }
                else{
                    row.createCell(5).setCellValue("");
                }
                row.createCell(6).setCellValue(list.get("commission").toString());
                row.createCell(7).setCellValue(totalComm);
            }
            Row row = sheet.createRow(++i);
            row.createCell(0).setCellValue("Grand Total");
            row.createCell(1).setCellValue("");
            row.createCell(2).setCellValue(new DecimalFormat("#,##0.00").format(invAmt));
            row.createCell(3).setCellValue("");
            row.createCell(4).setCellValue("");
            row.createCell(5).setCellValue("");
            row.createCell(6).setCellValue("");
            row.createCell(7).setCellValue("");
            hwb.write(out);
            out.close();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } catch (Exception gex) {
            gex.printStackTrace();
        }
    }

    public PdfPTable createFirstTableAgentSalesReport(SalesRequestPojo salesRequestPojo) throws ParseException {
        Font font1 = new Font(getcustomfont(), 8, Font.NORMAL, Color.BLACK);
        int a = 0;
        Font f1 = new Font(getcustomfont(), 8, Font.BOLD, Color.BLACK);
        PdfPTable table = new PdfPTable(a + 8);
        Font font = new Font(getcustomfont(), 10, Font.NORMAL, Color.WHITE);
        Color myColor = WebColors.getRGBColor("#326397");
        PdfPCell p5 = new PdfPCell(new Phrase("Date", font));
        p5.setBackgroundColor(myColor);
        PdfPCell p6 = new PdfPCell(new Phrase("Invoice No", font));
        p6.setBackgroundColor(myColor);
        PdfPCell p7 = new PdfPCell(new Phrase("Invoice Amount", font));
        p7.setBackgroundColor(myColor);
        PdfPCell p8 = new PdfPCell(new Phrase("Agent Name", font));
        p8.setBackgroundColor(myColor);
        PdfPCell p9 = new PdfPCell(new Phrase("Agent No", font));
        p9.setBackgroundColor(myColor);
        PdfPCell p10 = new PdfPCell(new Phrase("Agent Email", font));
        p10.setBackgroundColor(myColor);
        PdfPCell p11 = new PdfPCell(new Phrase("Agent Commission", font));
        p11.setBackgroundColor(myColor);
        PdfPCell p12 = new PdfPCell(new Phrase("Commission Amt", font));
        p12.setBackgroundColor(myColor);
        table.addCell(p5);
        table.addCell(p6);
        table.addCell(p7);
        table.addCell(p8);
        table.addCell(p9);
        table.addCell(p10);
        table.addCell(p11);
        table.addCell(p12);
        PaginatedResponsePojo paginatedResponsePojo = new PaginatedResponsePojo();
        paginatedResponsePojo = agentReport(salesRequestPojo);
        double invAmt = 0.00,totalComm=0.00,com=0.00,amt=0.00;
        for (Map list : paginatedResponsePojo.getMapList()) {
            invAmt = invAmt + Double.parseDouble(list.get("totalAmount").toString());
            com= Double.parseDouble(list.get("commission").toString());
            amt= Double.parseDouble(list.get("totalAmount").toString());
            totalComm= (com*amt)/100;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dayEndDate = simpleDateFormat.parse(list.get("invdate").toString());
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String str = df.format(dayEndDate);
            table.addCell(new Phrase((str) + "", font1));
            table.addCell(new Phrase(list.get("invoiceNO") + "", font1));
            table.addCell(new Phrase(Double.parseDouble(list.get("totalAmount").toString()) + "", font1));
            table.addCell(new Phrase(list.get("agentName").toString() + "", font1));
            if(list.get("accountNo")!=null) {
                table.addCell(new Phrase(list.get("accountNo").toString() + "", font1));
            }
            else{
                table.addCell(new Phrase("" + "", font1));

            }
            if(list.get("email")!=null) {
                table.addCell(new Phrase(list.get("email").toString() + "", font1));
            }
            else{
                table.addCell(new Phrase("" + "", font1));

            }
            table.addCell(new Phrase(list.get("commission").toString() + "", font1));
            table.addCell(new Phrase(totalComm + "", font1));
        }
        table.addCell(new Phrase("Grand Total"));
        table.addCell(new Phrase(" "));
        table.addCell(new Phrase(new DecimalFormat("#,##0.00").format(invAmt)));
        table.addCell(new Phrase(" "));
        table.addCell(new Phrase(" "));
        table.addCell(new Phrase(" "));
        table.addCell(new Phrase(" "));
        table.addCell(new Phrase(" "));
        return table;
    }
    public PdfPTable createFirstTableMonthEndSalesReport(SalesRequestPojo salesRequestPojo) {
        Font font1 = new Font(getcustomfont(), 8, Font.NORMAL, Color.BLACK);
        int a = 0;
        PdfPTable table = new PdfPTable(a + 4);
        Font font = new Font(getcustomfont(), 10, Font.NORMAL, Color.WHITE);
        Color myColor = WebColors.getRGBColor("#326397");
        PdfPCell p5 = new PdfPCell(new Phrase("Category Name", font));
        p5.setBackgroundColor(myColor);
        PdfPCell p6 = new PdfPCell(new Phrase("Item Name", font));
        p6.setBackgroundColor(myColor);
        PdfPCell p7 = new PdfPCell(new Phrase("Quantity", font));
        p7.setBackgroundColor(myColor);
        PdfPCell p8 = new PdfPCell(new Phrase("Total Amount", font));
        p8.setBackgroundColor(myColor);
        table.addCell(p5);
        table.addCell(p6);
        table.addCell(p7);
        table.addCell(p8);
        PaginatedResponsePojo paginatedResponsePojo = new PaginatedResponsePojo();
        paginatedResponsePojo = getsalesMonthEndInvoice(salesRequestPojo);

        double totalAmt = 0.00;
        for (SalesReportListResponsePojo list : paginatedResponsePojo.getSalesReportList()) {
            totalAmt = totalAmt + list.getInvoiceAmount();
            table.addCell(new Phrase(list.getItemCategoryName() + "", font1));
            table.addCell(new Phrase(list.getItemName() + "", font1));
            table.addCell(new Phrase(list.getQty() + "", font1));
            table.addCell(new Phrase(list.getInvoiceAmount() + "", font1));
        }
        table.addCell(new Phrase("Grand Total"));
        table.addCell(new Phrase(" "));
        table.addCell(new Phrase(" "));
        table.addCell(new Phrase(new DecimalFormat("#,##0.00").format(totalAmt)));
        return table;
    }


    public PdfPTable createFirstTableMonthEndSales(SalesRequestPojo salesRequestPojo)throws Exception {
        Font font1 = new Font(getcustomfont(), 8, Font.NORMAL, Color.BLACK);
        int a = 0;
        PdfPTable tbl = new PdfPTable(a + 2);
        PdfPTable tbl3 = new PdfPTable(a + 2);
        PdfPTable tbl2 = new PdfPTable(1);
        PdfPTable tbl5 = new PdfPTable(1);
        PdfPTable tbl7 = new PdfPTable(1);
        PdfPTable tab = new PdfPTable(1);
        Font f = new Font(getcustomfont(), 15, Font.BOLD, Color.WHITE);
        Font font = new Font(getcustomfont(), 10, Font.NORMAL, Color.WHITE);
        Color myColor = WebColors.getRGBColor("#326397");
        PdfPCell p = new PdfPCell(new Phrase("Month End Report", f));
        p.setHorizontalAlignment(Element.ALIGN_CENTER);
        p.setBackgroundColor(myColor);
        tab.addCell(p);
        PdfPCell p1 = new PdfPCell(new Phrase("From Date", font));
        p1.setBackgroundColor(myColor);
        PdfPCell p2 = new PdfPCell(new Phrase("To Date", font));
        p2.setBackgroundColor(myColor);
        tbl.addCell(p1);
        tbl.addCell(p2);
        PdfPCell p4 = new PdfPCell(new Phrase("Item Name", font));
        p4.setBackgroundColor(myColor);
        tbl2.addCell(p4);
        PdfPCell pc6 = new PdfPCell(new Phrase("Category Name", font));
        pc6.setBackgroundColor(myColor);
        tbl7.addCell(pc6);
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = sdf.parse(salesRequestPojo.getFromDate());
        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
        String fromDateFormat = df1.format(fromDate);
        Date toDate = sdf.parse(salesRequestPojo.getToDate());
        String toDateFormat = df1.format(toDate);
        tbl.addCell(new Phrase(fromDateFormat + "", font1));
        tbl.addCell(new Phrase(toDateFormat + "", font1));

        if (salesRequestPojo.getItemId() != null) {
            Item item = itemRespository.findAllByItemId(Long.parseLong(salesRequestPojo.getItemId()));
            tbl2.addCell(new Phrase(item.getItemName() + "", font1));
        }
        if (salesRequestPojo.getItemCategoryId() != null) {
            Category category = categoryRepository.findAllByItemCategoryId(salesRequestPojo.getItemCategoryId());
            tbl7.addCell(new Phrase(category.getItemCategoryName() + "", font1));
        }

        tab.addCell(tbl);
        tab.addCell(tbl3);
        tab.addCell(tbl2);
        tab.addCell(tbl5);
        return tab;
    }

    public PdfPTable createFirstTableItemSales(SalesRequestPojo salesRequestPojo)throws Exception {
        Font font1 = new Font(getcustomfont(), 8, Font.NORMAL, Color.BLACK);
        int a = 0;
        PdfPTable tbl = new PdfPTable(a + 2);
        PdfPTable tbl2 = new PdfPTable(1);
        PdfPTable tbl7 = new PdfPTable(1);
        PdfPTable tab = new PdfPTable(1);
        Font f = new Font(getcustomfont(), 15, Font.BOLD, Color.WHITE);
        Font font = new Font(getcustomfont(), 10, Font.NORMAL, Color.WHITE);
        Color myColor = WebColors.getRGBColor("#326397");
        PdfPCell p = new PdfPCell(new Phrase("Item Sales Listing", f));
        p.setHorizontalAlignment(Element.ALIGN_CENTER);
        p.setBackgroundColor(myColor);
        tab.addCell(p);
        PdfPCell p1 = new PdfPCell(new Phrase("From Date", font));
        p1.setBackgroundColor(myColor);
        PdfPCell p2 = new PdfPCell(new Phrase("To Date", font));
        p2.setBackgroundColor(myColor);
        tbl.addCell(p1);
        tbl.addCell(p2);
        PdfPCell p4 = new PdfPCell(new Phrase("Item Name", font));
        p4.setBackgroundColor(myColor);
        tbl2.addCell(p4);
        PdfPCell pc6 = new PdfPCell(new Phrase("Category Name", font));
        pc6.setBackgroundColor(myColor);
        tbl7.addCell(pc6);
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = sdf.parse(salesRequestPojo.getFromDate());
        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
        String fromDateFormat = df1.format(fromDate);
        Date toDate = sdf.parse(salesRequestPojo.getToDate());
        String toDateFormat = df1.format(toDate);
        tbl.addCell(new Phrase(fromDateFormat + "", font1));
        tbl.addCell(new Phrase(toDateFormat + "", font1));
        if (salesRequestPojo.getItemId() != null) {
            Item item = itemRespository.findAllByItemId(Long.parseLong(salesRequestPojo.getItemId()));
            tbl2.addCell(new Phrase(item.getItemName() + "", font1));
        }
        if (salesRequestPojo.getItemCategoryId() != null) {
            Category category = categoryRepository.findAllByItemCategoryId(salesRequestPojo.getItemCategoryId());
            tbl7.addCell(new Phrase(category.getItemCategoryName() + "", font1));
        }
        tab.addCell(tbl);
        tab.addCell(tbl2);
        tab.addCell(tbl7);
        return tab;
    }




    public PdfPTable createFirstTableItemSalesReport(SalesRequestPojo salesRequestPojo) throws ParseException {
        Font font1 = new Font(getcustomfont(), 8, Font.NORMAL, Color.BLACK);
        int a = 0;
        Font f1 = new Font(getcustomfont(), 8, Font.BOLD, Color.BLACK);
        PdfPTable table = new PdfPTable(a + 5);
        Font font = new Font(getcustomfont(), 10, Font.NORMAL, Color.WHITE);
        Color myColor = WebColors.getRGBColor("#326397");
        PdfPCell p5 = new PdfPCell(new Phrase("Date", font));
        p5.setBackgroundColor(myColor);
        PdfPCell p6 = new PdfPCell(new Phrase("Category Name", font));
        p6.setBackgroundColor(myColor);
        PdfPCell p7 = new PdfPCell(new Phrase("Item Name", font));
        p7.setBackgroundColor(myColor);
        PdfPCell p8 = new PdfPCell(new Phrase("Quantity", font));
        p8.setBackgroundColor(myColor);
        PdfPCell p9 = new PdfPCell(new Phrase("Total Amount", font));
        p9.setBackgroundColor(myColor);
        table.addCell(p5);
        table.addCell(p6);
        table.addCell(p7);
        table.addCell(p8);
        table.addCell(p9);
        PaginatedResponsePojo paginatedResponsePojo = new PaginatedResponsePojo();
        paginatedResponsePojo = getrestItemWiseList(salesRequestPojo);
        double totalAmt = 0.00;
        for (Map list : paginatedResponsePojo.getMapList()) {
            totalAmt = totalAmt + Double.parseDouble(list.get("invoiceAmount").toString());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dayEndDate = simpleDateFormat.parse(list.get("date").toString());
            DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            String str = df.format(dayEndDate);
            table.addCell(new Phrase((str) + "", font1));
            table.addCell(new Phrase(list.get("itemCategoryName") + "", font1));
            table.addCell(new Phrase(list.get("itemName").toString() + "", font1));
            table.addCell(new Phrase(list.get("qty").toString() + "", font1));
            table.addCell(new Phrase(Double.parseDouble(list.get("invoiceAmount").toString()) + "", font1));
        }
        table.addCell(new Phrase("Grand Total"));
        table.addCell(new Phrase(" "));
        table.addCell(new Phrase(" "));
        table.addCell(new Phrase(" "));
        table.addCell(new Phrase(new DecimalFormat("#,##0.00").format(totalAmt)));
        return table;
    }




    @Transactional
    public PaginatedResponsePojo getsalesMonthEndInvoice(SalesRequestPojo requestPojo) {
        PaginatedResponsePojo paginatedResponsePojo = new PaginatedResponsePojo();
        String dat = requestPojo.getFromDate();
        long date = new SimpleDateFormat("yyyy-MM-dd").parse(dat, new ParsePosition(0)).getTime();
        java.sql.Date fromDate = new java.sql.Date(date);
        String dat1 = requestPojo.getToDate();
        long date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dat1, new ParsePosition(0)).getTime();
        java.sql.Date toDate = new java.sql.Date(date1);
        List<Map> list = new ArrayList<>();
//        if (StringUtils.isNotEmpty(requestPojo.getItemId()) && requestPojo.getItemCategoryId() == null) {
//            list = salesInvoiceDetailsRepository.findByMonthendReportbyItem(Long.parseLong(requestPojo.getItemId()), fromDate, toDate);
//        }
//        if (requestPojo.getItemCategoryId() != null && StringUtils.isEmpty(requestPojo.getItemId())) {
//            list = salesInvoiceDetailsRepository.findByMonthendReportbyCategory(requestPojo.getItemCategoryId(), fromDate, toDate);
//        }
//        if (StringUtils.isNotEmpty(requestPojo.getItemId()) && requestPojo.getItemCategoryId() != null) {
//            list = salesInvoiceDetailsRepository.findByMonthendReportbyCategoryAndItem(Long.parseLong(requestPojo.getItemId()), requestPojo.getItemCategoryId(), fromDate, toDate);
//        }
        if (StringUtils.isEmpty(requestPojo.getItemId()) && requestPojo.getItemCategoryId() == null) {
            list = salesInvoiceDetailsRepository.findByMonthendReport(fromDate, toDate);
        }
        List<SalesReportListResponsePojo> resultList = new ArrayList<>();
        List<SalesReportListResponsePojo> resultLists = new ArrayList<>();
        Gson gson = new Gson();
        Type type = new TypeToken<List<SalesReportListResponsePojo>>() {
        }.getType();
        resultList = gson.fromJson(gson.toJson(list), type);
        Map<String, List<SalesReportListResponsePojo>> map = resultList.stream()
                .collect(Collectors.groupingBy(SalesReportListResponsePojo::getItemName));
        for (Map.Entry<String, List<SalesReportListResponsePojo>> m : map.entrySet()) {
            double qty = m.getValue().stream().mapToDouble(o -> o.getQty()).sum();
            double amount = m.getValue().stream().mapToDouble(o -> o.getInvoiceAmount()).sum();
            SalesReportListResponsePojo salesReportListResponsePojo = new SalesReportListResponsePojo();
            salesReportListResponsePojo.setQty(qty);
            salesReportListResponsePojo.setInvoiceAmount(amount);
            salesReportListResponsePojo.setItemName(m.getKey().toString());
            Item item = itemRespository.findAllByItemName(salesReportListResponsePojo.getItemName());
            salesReportListResponsePojo.setItemCategoryName(item.getIdItemCategory().getItemCategoryName());
            resultLists.add(salesReportListResponsePojo);
        }
        paginatedResponsePojo.setData(resultLists);
        paginatedResponsePojo.setSalesReportList(resultLists);
        return paginatedResponsePojo;
    }
    @Transactional
    public PaginatedResponsePojo getcancelledsalesInvoice(SalesRequestPojo requestPojo) {
        PaginatedResponsePojo paginatedResponsePojo=new PaginatedResponsePojo();
        String dat = requestPojo.getFromDate();
        long date = new SimpleDateFormat("yyyy-MM-dd").parse(dat, new ParsePosition(0)).getTime();
        java.sql.Date fromDate = new java.sql.Date(date);
        String dat1 = requestPojo.getToDate();
        long date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dat1, new ParsePosition(0)).getTime();
        java.sql.Date toDate = new java.sql.Date(date1);
        List<Map> list = new ArrayList<>();
//        if (requestPojo.getCustomerId() != null) {
//            list=salesInvoiceRepository.findByCancelledSalesbyCustomer(requestPojo.getCustomerId(),fromDate,toDate);
//        }
//        if (!StringUtils.isEmpty(requestPojo.getFromSINo()) && !StringUtils.isEmpty(requestPojo.getToSINo())) {
//            list=salesInvoiceRepository.findByCancelledSalesInvoiceFilter(fromDate,requestPojo.getFromSINo(),toDate,requestPojo.getToSINo());
//        }
//        if (requestPojo.getCustomerId() == null && StringUtils.isEmpty(requestPojo.getFromSINo()) && StringUtils.isEmpty(requestPojo.getToSINo())) {
//            list=salesInvoiceRepository.findByCancelledSales(fromDate,toDate);
//        }
//        if (requestPojo.getFromSINo() != null && requestPojo.getToSINo() != null && requestPojo.getCustomerId()!=null && requestPojo.getCustomerId()!=0) {
//            list=salesInvoiceRepository.findByCancelledSalesInvoiceFilterAndCustomer(requestPojo.getCustomerId(),fromDate,requestPojo.getFromSINo(),toDate,requestPojo.getToSINo());
//        }

        paginatedResponsePojo.setData(list);
        paginatedResponsePojo.setMapList(list);
        return paginatedResponsePojo;
    }

    @Transactional
    public void downloadCancelSalesExcel(OutputStream out, SalesRequestPojo salesRequestPojo) {
        try {
            String fromInvoice = null;
            String toInvoice = null;
            PaginatedResponsePojo paginatedResponsePojo = new PaginatedResponsePojo();
            paginatedResponsePojo = getcancelledsalesInvoice(salesRequestPojo);
            HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFSheet sheet = hwb.createSheet("First Sheet");
            Row headerRow = sheet.createRow(0);
            Company company = companyRepository.findAllByStatus("Active");
            headerRow.setHeightInPoints((3 * sheet.getDefaultRowHeightInPoints()));
            headerRow.createCell(2).setCellValue(company.getCompanyName() + "\n" + company.getAddress() + "\n" + "Cancel SalesInvoice Listing");
            CellRangeAddress cell = new CellRangeAddress(0, 2, 2, 4);
            sheet.addMergedRegion(cell);
            RegionUtil.setBorderTop(2, cell, sheet, hwb);
            RegionUtil.setBorderLeft(2, cell, sheet, hwb);
            RegionUtil.setBorderRight(2, cell, sheet, hwb);
            RegionUtil.setBorderBottom(2, cell, sheet, hwb);
            Row headerRow1 = sheet.createRow(3);
            headerRow1.createCell(0).setCellValue("From Date");
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = sdf.parse(salesRequestPojo.getFromDate());
            DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
            String fromDateFormat = df1.format(fromDate);
            Date toDate = sdf.parse(salesRequestPojo.getToDate());
            String toDateFormat = df1.format(toDate);
            headerRow1.createCell(1).setCellValue(fromDateFormat);
            headerRow1.createCell(2).setCellValue("To Date");
            headerRow1.createCell(3).setCellValue(toDateFormat);
            HSSFRow headerRow6 = sheet.createRow(4);
            if(!StringUtils.isEmpty(salesRequestPojo.getFromSINo())) {
                SalesInvoice salesInvoice = salesInvoiceRepository.findAllBySINo(salesRequestPojo.getFromSINo());
                fromInvoice = salesInvoice.getSINo();
            }
            if(!StringUtils.isEmpty(salesRequestPojo.getToSINo())) {
                SalesInvoice salesInv = salesInvoiceRepository.findAllBySINo(salesRequestPojo.getToSINo());
                toInvoice = salesInv.getSINo();
            }
            headerRow6.createCell(0).setCellValue("From Invoice");
            headerRow6.createCell(2).setCellValue("To Invoice");
            if (!StringUtils.isEmpty(fromInvoice) && !StringUtils.isEmpty(toInvoice)) {
                headerRow6.createCell(1).setCellValue(fromInvoice);
                headerRow6.createCell(3).setCellValue(toInvoice);
            }
            HSSFRow headerRow7 = sheet.createRow(5);
            headerRow7.createCell(0).setCellValue("Customer Name");
            if (salesRequestPojo.getCustomerId() != null) {
                Customer customer = customerRepository.findAllByCustomerId(salesRequestPojo.getCustomerId());
                headerRow7.createCell(1).setCellValue(customer.getFirstName());
            }
            Row headerRow8 = sheet.createRow(6);
            headerRow8.createCell(0).setCellValue("Date");
            headerRow8.createCell(1).setCellValue("Invoice No");
            headerRow8.createCell(2).setCellValue("Customer Name");
            headerRow8.createCell(3).setCellValue("Total Amount");
            headerRow8.createCell(4).setCellValue("Total Recievable");
            headerRow8.createCell(5).setCellValue("Status");
            int i = 6;
            double totalAmt = 0.00, totalReceived = 0.00;
                for (Map list : paginatedResponsePojo.getMapList()) {
                    totalAmt = totalAmt + Double.parseDouble(list.get("totalAmount").toString());
                    totalReceived = totalReceived + Double.parseDouble(list.get("totalReceivable").toString());
                    HSSFRow row = sheet.createRow(++i);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date dayEndDate = simpleDateFormat.parse(list.get("date").toString());
                    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    String str = df.format(dayEndDate);
                    row.createCell(0).setCellValue(str);
                    row.createCell(1).setCellValue(list.get("sINo").toString());
                    row.createCell(2).setCellValue(list.get("customerName").toString());
                    row.createCell(3).setCellValue(list.get("totalAmount").toString());
                    row.createCell(4).setCellValue(list.get("totalReceivable").toString());
                    row.createCell(5).setCellValue(list.get("status").toString());
                }
            HSSFRow row = sheet.createRow(++i);
            row.createCell(0).setCellValue("Grand Total");
            row.createCell(1).setCellValue("");
            row.createCell(2).setCellValue("");
            row.createCell(3).setCellValue(new DecimalFormat("#,##0.00").format(totalAmt));
            row.createCell(4).setCellValue(new DecimalFormat("#,##0.00").format(totalReceived));
            row.createCell(5).setCellValue("");

            hwb.write(out);
            out.close();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } catch (Exception gex) {
            gex.printStackTrace();
        }
    }

    @Transactional
    public void downloadCategoryExcelSheet(OutputStream out) {
        try {
            List<CategoryPojo> list = hiposService.getItemCategoryList();
            XSSFWorkbook hwb = new XSSFWorkbook();
            XSSFSheet sheet = hwb.createSheet("First Sheet");
            Row headerRow1 = sheet.createRow(0);
            headerRow1.createCell(0).setCellValue("Item Category Name");
            headerRow1.createCell(1).setCellValue("Item Category Description");
            headerRow1.createCell(2).setCellValue("defaultType");
            headerRow1.createCell(3).setCellValue("Status");

            int i = 0;
            for (CategoryPojo category : list) {
                Row row = sheet.createRow(++i);
                row.createCell(0).setCellValue(category.getName());
                if (!StringUtils.isEmpty(category.getDescription()))
                    row.createCell(1).setCellValue(category.getDescription());
                else
                    row.createCell(1).setCellValue("");
                row.createCell(3).setCellValue(category.getStatus());
            }
            hwb.write(out);
            out.close();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } catch (Exception gex) {
            gex.printStackTrace();
        }
    }


    @Transactional
    public void downloadCategoryPdf(OutputStream outputStream) {
        try {
            Font font1 = new Font(getcustomfont(), 12F, Font.BOLD);
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Chunk CONNECT = new Chunk(new LineSeparator(1, 100, Color.BLACK, Element.ALIGN_JUSTIFIED, 3f));
            document.add(CONNECT);
            document.add(new Paragraph("", font1));
            Chunk CONNECT1 = new Chunk(new LineSeparator(1, 100, Color.WHITE, Element.ALIGN_JUSTIFIED, 3f));
            document.add(CONNECT1);
            PdfPTable table = createFirstTableCategory();
            table.setHeaderRows(1);
            document.add(table);
            document.add(CONNECT1);
            Paragraph foot = new Paragraph();
            Phrase ph5 = new Phrase("Terms And Condition:Terms                    Prepared By                                        Approved By   ", font1);
            foot.add(ph5);
            document.add(foot);
            document.add(CONNECT);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public PdfPTable createFirstTableCategory() {
        Font font1 = new Font(getcustomfont(), 8, Font.NORMAL, Color.BLACK);
        int a = 0;
        PdfPTable tab = new PdfPTable(1);
        Font f = new Font(getcustomfont(), 15, Font.BOLD, Color.WHITE);
        PdfPTable table = new PdfPTable(a + 4);
        Font font = new Font(getcustomfont(), 10, Font.NORMAL, Color.WHITE);
        Color myColor = WebColors.getRGBColor("#326397");
        PdfPCell p = new PdfPCell(new Phrase("Category", f));
        p.setBackgroundColor(myColor);
        tab.addCell(p);
        PdfPCell pc2 = new PdfPCell(new Phrase(" Item Category Name", font));
        pc2.setBackgroundColor(myColor);
        PdfPCell pc3 = new PdfPCell(new Phrase("Item Category Description", font));
        pc3.setBackgroundColor(myColor);
        PdfPCell pc4 = new PdfPCell(new Phrase("defaultType", font));
        pc4.setBackgroundColor(myColor);
        PdfPCell pc5 = new PdfPCell(new Phrase("Status", font));
        pc5.setBackgroundColor(myColor);
        table.addCell(pc2);
        table.addCell(pc3);
        table.addCell(pc4);
        table.addCell(pc5);
        List<CategoryPojo> list = hiposService.getItemCategoryList();
        table.setWidthPercentage(100);
        for (CategoryPojo category : list) {
            table.addCell(new Phrase(category.getName() + "", font1));

            table.addCell(new Phrase(category.getStatus() + "", font1));
        }
        tab.addCell(table);
        return tab;
    }
    public void downloadItemPdf(OutputStream outputStream) {
        try {
            Font font1 = new Font(getcustomfont(), 12F, Font.BOLD);
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Chunk CONNECT = new Chunk(new LineSeparator(1, 100, Color.BLACK, Element.ALIGN_JUSTIFIED, 3f));
            document.add(CONNECT);
            document.add(new Paragraph("", font1));
            Chunk CONNECT1 = new Chunk(new LineSeparator(1, 100, Color.WHITE, Element.ALIGN_JUSTIFIED, 3f));
            document.add(CONNECT1);
            PdfPTable table = createFirstTableItem();
            table.setHeaderRows(1);
            document.add(table);
            document.add(CONNECT1);
            Paragraph foot = new Paragraph();
            Phrase ph5 = new Phrase("Terms And Condition:Terms                    Prepared By                                        Approved By   ", font1);
            foot.add(ph5);
            document.add(foot);
            document.add(CONNECT);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    @Transactional
    public PdfPTable createFirstTableItem() throws IOException {
        Font font1 = new Font(getcustomfont(), 8, Font.NORMAL, Color.BLACK);
        int a = 0;
        PdfPTable tab = new PdfPTable(1);
        Font f = new Font(getcustomfont(), 15, Font.BOLD, Color.WHITE);
        PdfPTable table = new PdfPTable(a +9);
        Font font = new Font(getcustomfont(), 10, Font.NORMAL, Color.WHITE);
        Color myColor = WebColors.getRGBColor("#326397");
        PdfPCell p = new PdfPCell(new Phrase("Item", f));
        p.setBackgroundColor(myColor);
        tab.addCell(p);
        PdfPCell pc2 = new PdfPCell(new Phrase("Item Code", font));
        pc2.setBackgroundColor(myColor);
        PdfPCell pc3 = new PdfPCell(new Phrase("Item Name", font));
        pc3.setBackgroundColor(myColor);
        PdfPCell pc4 = new PdfPCell(new Phrase("Description", font));
        pc4.setBackgroundColor(myColor);
        PdfPCell pc5 = new PdfPCell(new Phrase("Item Type", font));
        pc5.setBackgroundColor(myColor);
//        PdfPCell pc6 = new PdfPCell(new Phrase("UOM", font));
//        pc6.setBackgroundColor(myColor);
        PdfPCell pc6 = new PdfPCell(new Phrase("HSN Code", font));
        pc6.setBackgroundColor(myColor);
        PdfPCell pc7 = new PdfPCell(new Phrase("Status", font));
        pc7.setBackgroundColor(myColor);
//        PdfPCell pc9 = new PdfPCell(new Phrase("Re-Order Level", font));
//        pc9.setBackgroundColor(myColor);
//        PdfPCell pc10 = new PdfPCell(new Phrase("Input Tax", font));
//        pc10.setBackgroundColor(myColor);
//        PdfPCell pc11 = new PdfPCell(new Phrase("Output Tax", font));
//        pc11.setBackgroundColor(myColor);
//        PdfPCell pc8 = new PdfPCell(new Phrase("Serializable Status", font));
//        pc8.setBackgroundColor(myColor);
//        PdfPCell pc13 = new PdfPCell(new Phrase("Brand Name", font));
//        pc13.setBackgroundColor(myColor);
        PdfPCell pc8 = new PdfPCell(new Phrase("Item Category", font));
        pc8.setBackgroundColor(myColor);
//        PdfPCell pc15 = new PdfPCell(new Phrase("Purchase Price", font));
//        pc15.setBackgroundColor(myColor);
        PdfPCell pc9 = new PdfPCell(new Phrase("Sales Price", font));
        pc9.setBackgroundColor(myColor);
//        PdfPCell pc17 = new PdfPCell(new Phrase("Certificate No.", font));
//        pc17.setBackgroundColor(myColor);
//        PdfPCell pc18 = new PdfPCell(new Phrase("PurchaseInTax", font));
//        pc18.setBackgroundColor(myColor);
//        PdfPCell pc19 = new PdfPCell(new Phrase("SalesOutTax", font));
//        pc19.setBackgroundColor(myColor);
//        PdfPCell pc20 = new PdfPCell(new Phrase("ProductionType", font));
//        pc20.setBackgroundColor(myColor);
//        PdfPCell pc21 = new PdfPCell(new Phrase("Online Integration(Requires Internet)", font));
//        pc21.setBackgroundColor(myColor);
        PdfPCell pc10 = new PdfPCell(new Phrase("FoodType", font));
        pc10.setBackgroundColor(myColor);
        table.addCell(pc2);
        table.addCell(pc3);
        table.addCell(pc4);
        table.addCell(pc5);
        table.addCell(pc6);
        table.addCell(pc7);
        table.addCell(pc8);
        table.addCell(pc9);
        table.addCell(pc10);
//        table.addCell(pc11);
//        table.addCell(pc12);
//        table.addCell(pc13);
//        table.addCell(pc14);
//        table.addCell(pc15);
//        table.addCell(pc16);
//        table.addCell(pc17);
//        table.addCell(pc18);
//        table.addCell(pc19);
//        table.addCell(pc20);
//        table.addCell(pc21);
//        table.addCell(pc22);
        List<AddNewItemDTO> itemList = hiposService.getAllItems();
        table.setWidthPercentage(100);
        for (AddNewItemDTO item : itemList) {
            table.addCell(new Phrase(item.getItemCode() + "", font1));
            table.addCell(new Phrase(item.getItemName() + "", font1));
            table.addCell(new Phrase(item.getItemDesc() + "", font1));
            table.addCell(new Phrase(item.getItemType() + "", font1));
//            table.addCell(new Phrase(item.getid().getUnitOfMeasurementName() + "", font1));
            table.addCell(new Phrase(item.getHsnCode() + "", font1));
            table.addCell(new Phrase(item.getItemStatus() + "", font1));
//            table.addCell(new Phrase(item.getReorderlevel() + "", font1));
//            table.addCell(new Phrase(item.getin().getTaxCode() + "", font1));
//            table.addCell(new Phrase(item.getIdItemTax().getTaxCode() + "", font1));
//            table.addCell(new Phrase(item.getSerializableStatus() + "", font1));
//            table.addCell(new Phrase((item.getBrandName() != null) ? item.getBrandName().getBrandName() : "" + "", font1));
            table.addCell(new Phrase(item.getItemCategory().getItemCategoryName() + "", font1));
//            List<PurchasePrice> purchasePrice = hiposDAO.findAll("from PurchasePrice where itemId = " + item.getItemId());
//            if (!purchasePrice.isEmpty())
//                table.addCell(new Phrase(purchasePrice.get(0).getPrice() + "", font1));
//            List<SalesPricing> salesPricing = hiposDAO.findAll("from SalesPricing where itemId = " + item.getItemId());
//            if (!salesPricing.isEmpty())
//                table.addCell(new Phrase(salesPricing.get(0).getPrice() + "", font1));
            table.addCell(new Phrase("" + "", font1));
            ObjectMapper mapper = new ObjectMapper();
//            Map<String, Object> map = new HashMap<String, Object>();
//            map = mapper.readValue(item.getInclusiveJSON(), new TypeReference<Map<String, String>>() {
//            });
//            if (StringUtils.equalsIgnoreCase(map.get("purchases").toString(), "true")) {
//                table.addCell(new Phrase("true" + "", font1));
//            } else {
//                table.addCell(new Phrase("false" + "", font1));
//            }
//            if (StringUtils.equalsIgnoreCase(map.get("sales").toString(), "true")) {
//                table.addCell(new Phrase("true" + "", font1));
//            } else {
//                table.addCell(new Phrase("false" + "", font1));
//            }
//            if (StringUtils.equalsIgnoreCase(item.getProductionName(), "productionName")) {
//                table.addCell("true");
//            } else {
//                table.addCell("false");
//            }
            Gson json = new Gson();
            List<Map<String,String>> stringList = new ArrayList<>();
            java.lang.reflect.Type type = new TypeToken<List<Map<String,String>>>(){}.getType();
            stringList = json.fromJson(item.getCartValue(), (java.lang.reflect.Type) type);
//            String ss= "";
//            for (Map<String, String> s : stringList) {
//                if (StringUtils.equalsIgnoreCase(s.get("status"),"true")) {
//                    if(StringUtils.equalsIgnoreCase(ss,"")){
//                        ss = s.get("cartName");
//                    }else {
//                        ss = ss+","+s.get("cartName");
//                    }
//                    table.addCell(ss);
//                }
//            }
            table.addCell(item.getFoodtype());
        }
        tab.addCell(table);
        return tab;
    }
    public void writeItemstoExcel(OutputStream outputStream) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Items");
        List<AddNewItemDTO> itemList = hiposService.getAllItems();
        int rowCount = 0;
        org.apache.poi.ss.usermodel.Cell cell;
        Row row;
        row = sheet.createRow(rowCount);
        cell = row.createCell(0);
        cell.setCellValue("Item Code");
        cell = row.createCell(1);
        cell.setCellValue("Item Name");
        cell = row.createCell(2);
        cell.setCellValue("Description");
        cell = row.createCell(3);
        cell.setCellValue("Item Type");
        cell = row.createCell(4);
//        cell.setCellValue("UOM");
//        cell = row.createCell(5);
        cell.setCellValue("HSN Code");
        cell = row.createCell(5);
        cell.setCellValue("Status");
//        cell = row.createCell(7);
//        cell.setCellValue("Re-Order Level");
//        cell = row.createCell(8);
//        cell.setCellValue("Input Tax");
//        cell = row.createCell(9);
//        cell.setCellValue("Output Tax");
//        cell = row.createCell(10);
//        cell.setCellValue("Serializable Status");
//        cell = row.createCell(11);
//        cell.setCellValue("Brand Name");
        cell = row.createCell(6);
        cell.setCellValue("Item Category");
//        cell = row.createCell(13);
//        cell.setCellValue("Purchase Price");
//        cell = row.createCell(7);
//        cell.setCellValue("Sales Price");
//        cell = row.createCell(15);
//        cell.setCellValue("Certificate No.");
//        cell = row.createCell(16);
//        cell.setCellValue("PurchaseInTax");
//        cell = row.createCell(17);
//        cell.setCellValue("SalesOutTax");
//        cell = row.createCell(7);
//        cell.setCellValue("ProductionType");
//        cell = row.createCell(19);
//        cell.setCellValue("Online Integration(Requires Internet)");
        cell = row.createCell(7);
        cell.setCellValue("FoodType");

        for (AddNewItemDTO item : itemList) {
            row = sheet.createRow(++rowCount);
            cell = row.createCell(0);
            cell.setCellValue(item.getItemCode());
            cell = row.createCell(1);
            cell.setCellValue(item.getItemName());
            cell = row.createCell(2);
            cell.setCellValue(item.getItemDesc());
            cell = row.createCell(3);
            cell.setCellValue(item.getItemType());
//            cell = row.createCell(4);
//            cell.setCellValue(item.getIdItemUOM().getUnitOfMeasurementName());
            cell = row.createCell(4);
            cell.setCellValue(item.getHsnCode());
            cell = row.createCell(5);
            cell.setCellValue(item.getItemStatus());
//            cell = row.createCell(7);
//            cell.setCellValue(item.getReorderlevel());
//            cell = row.createCell(8);
//            cell.setCellValue(item.getInputtax().getTaxCode());
//            cell = row.createCell(9);
//            cell.setCellValue(item.getIdItemTax().getTaxCode());
//            cell = row.createCell(10);
//            cell.setCellValue(item.getSerializableStatus());
//            cell = row.createCell(11);
//            cell.setCellValue((item.getBrandName() != null) ? item.getBrandName().getBrandName() : "");
            cell = row.createCell(6);
            cell.setCellValue(item.getItemCategory().getItemCategoryName());
//            cell = row.createCell(7);
//            List<PurchasePrice> purchasePrice = hiposDAO.findAll("from PurchasePrice where itemId = " + item.getItemId());
//            if (!purchasePrice.isEmpty())
//                cell.setCellValue(purchasePrice.get(0).getPrice());
//            cell = row.createCell(14);
//            List<SalesPricing> salesPricing = hiposDAO.findAll("from SalesPricing where itemId = " + item.getItemId());
//            if (!salesPricing.isEmpty())
//                cell.setCellValue(salesPricing.get(0).getPrice());
//            cell = row.createCell(7);
            cell.setCellValue("");
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = new HashMap<String, Object>();
//            map = mapper.readValue(item.getInclusiveJSON(), new TypeReference<Map<String, String>>() {
//            });
//            cell = row.createCell(16);
//            if (StringUtils.equalsIgnoreCase(map.get("purchases").toString(), "true")) {
//                cell.setCellValue("true");
//            } else {
//                cell.setCellValue("false");
//            }
//            cell = row.createCell(17);
//            if (StringUtils.equalsIgnoreCase(map.get("sales").toString(), "true")) {
//                cell.setCellValue("true");
//            } else {
//                cell.setCellValue("false");
//            }
//            cell = row.createCell(18);
//            if (StringUtils.equalsIgnoreCase(item.getProductionName(), "productionName")) {
//                cell.setCellValue("true");
//            } else {
//                cell.setCellValue("false");
//            }
//            cell = row.createCell(19);
            Gson json = new Gson();
            List<Map<String,String>> stringList = new ArrayList<>();
            java.lang.reflect.Type type = new TypeToken<List<Map<String,String>>>(){}.getType();
            stringList = json.fromJson(item.getCartValue(), (java.lang.reflect.Type) type);
//            String ss= "";
//            for (Map<String, String> s : stringList) {
//                if (StringUtils.equalsIgnoreCase(s.get("status"),"true")) {
//                    if(StringUtils.equalsIgnoreCase(ss,"")){
//                        ss = s.get("cartName");
//                    }else {
//                        ss = ss+","+s.get("cartName");
//                    }
//                    cell.setCellValue(ss);
//                }
//            }
            cell = row.createCell(7);
            cell.setCellValue(item.getFoodtype());
        }

        try {
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Transactional
    public void downloadCancelSalesPdf(OutputStream outputStream, SalesRequestPojo salesRequestPojo) {
        try {
            Font font1 = new Font(getcustomfont(), 12F, Font.BOLD);
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Chunk CONNECT = new Chunk(new LineSeparator(1, 100, Color.BLACK, Element.ALIGN_JUSTIFIED, 3f));
            document.add(CONNECT);
            document.add(new Paragraph("", font1));
            Chunk CONNECT1 = new Chunk(new LineSeparator(1, 100, Color.WHITE, Element.ALIGN_JUSTIFIED, 3f));
            document.add(CONNECT1);
            PdfPTable table = createFirstTableCancelSales(salesRequestPojo);
            PdfPTable table1 = createFirstTableCancelSalesReport(salesRequestPojo);
            table.setHeaderRows(1);
            document.add(printCompanyDetails());
            document.add(table);
            document.add(table1);
            document.add(CONNECT1);
            Paragraph foot = new Paragraph();
            Phrase ph5 = new Phrase("Terms And Condition:           Prepared By                  Approved By   ", font1);
            foot.add(ph5);
            document.add(foot);
            document.add(CONNECT);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public PdfPTable createFirstTableCancelSales(SalesRequestPojo salesRequestPojo)throws Exception {
        Font font1 = new Font(getcustomfont(), 8, Font.NORMAL, Color.BLACK);
        int a = 0;
        String fromInvoice = null;
        String toInvoice = null;
        PdfPTable tbl = new PdfPTable(a + 2);
        PdfPTable tbl2 = new PdfPTable(a + 2);
        PdfPTable tbl7 = new PdfPTable(1);
        PdfPTable tab = new PdfPTable(1);
        Font f = new Font(getcustomfont(), 15, Font.BOLD, Color.WHITE);
        Font font = new Font(getcustomfont(), 10, Font.NORMAL, Color.WHITE);
        Color myColor = WebColors.getRGBColor("#326397");
        PdfPCell p = new PdfPCell(new Phrase("Cancel Sales Invoice", f));
        p.setHorizontalAlignment(Element.ALIGN_CENTER);
        p.setBackgroundColor(myColor);
        tab.addCell(p);
        PdfPCell p1 = new PdfPCell(new Phrase("From Date", font));
        p1.setBackgroundColor(myColor);
        PdfPCell p2 = new PdfPCell(new Phrase("To Date", font));
        p2.setBackgroundColor(myColor);
        tbl.addCell(p1);
        tbl.addCell(p2);
        PdfPCell p3 = new PdfPCell(new Phrase("From Invoice", font));
        p3.setBackgroundColor(myColor);
        PdfPCell p4 = new PdfPCell(new Phrase("To Invoice", font));
        p4.setBackgroundColor(myColor);
        tbl2.addCell(p3);
        tbl2.addCell(p4);
        PdfPCell p5 = new PdfPCell(new Phrase("Customer Name", font));
        p5.setBackgroundColor(myColor);
        tbl7.addCell(p5);
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = sdf.parse(salesRequestPojo.getFromDate());
        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
        String fromDateFormat = df1.format(fromDate);
        Date toDate = sdf.parse(salesRequestPojo.getToDate());
        String toDateFormat = df1.format(toDate);
        tbl.addCell(new Phrase(fromDateFormat + "", font1));
        tbl.addCell(new Phrase(toDateFormat + "", font1));
        if(!StringUtils.isEmpty(salesRequestPojo.getFromSINo())) {
            SalesInvoice salesInvoice = salesInvoiceRepository.findAllBySINo(salesRequestPojo.getFromSINo());
            fromInvoice = salesInvoice.getSINo();
        }
        if(!StringUtils.isEmpty(salesRequestPojo.getToSINo())) {
            SalesInvoice salesInv = salesInvoiceRepository.findAllBySINo(salesRequestPojo.getToSINo());
            toInvoice = salesInv.getSINo();
        }
        tbl2.addCell(new Phrase(fromInvoice + "", font1));
        tbl2.addCell(new Phrase(toInvoice + "", font1));
        if (salesRequestPojo.getCustomerId() != null) {
            Customer customer = customerRepository.findAllByCustomerId(salesRequestPojo.getCustomerId());
            tbl7.addCell(new Phrase(customer.getFirstName() + "", font1));
        }
        tab.addCell(tbl);
        tab.addCell(tbl2);
        tab.addCell(tbl7);
        return tab;
    }

    public PdfPTable createFirstTableCancelSalesReport(SalesRequestPojo salesRequestPojo) throws ParseException {
        Font font1 = new Font(getcustomfont(), 8, Font.NORMAL, Color.BLACK);
        int a = 0;
        PdfPTable table = new PdfPTable(a + 6);
        Font font = new Font(getcustomfont(), 10, Font.NORMAL, Color.WHITE);
        Color myColor = WebColors.getRGBColor("#326397");
        PdfPCell p5 = new PdfPCell(new Phrase("Date", font));
        p5.setBackgroundColor(myColor);
        PdfPCell p6 = new PdfPCell(new Phrase("Invoice No", font));
        p6.setBackgroundColor(myColor);
        PdfPCell p7 = new PdfPCell(new Phrase("Customer Name", font));
        p7.setBackgroundColor(myColor);
        PdfPCell p8 = new PdfPCell(new Phrase("Total Amount", font));
        p8.setBackgroundColor(myColor);
        PdfPCell p9 = new PdfPCell(new Phrase("Total Recievable", font));
        p9.setBackgroundColor(myColor);
        PdfPCell p10 = new PdfPCell(new Phrase("Status", font));
        p10.setBackgroundColor(myColor);
        table.addCell(p5);
        table.addCell(p6);
        table.addCell(p7);
        table.addCell(p8);
        table.addCell(p9);
        table.addCell(p10);
        PaginatedResponsePojo paginatedResponsePojo = new PaginatedResponsePojo();
        paginatedResponsePojo = getcancelledsalesInvoice(salesRequestPojo);
        double totalAmt = 0.00, totalReceived = 0.00;
        for (Map list : paginatedResponsePojo.getMapList()) {
            totalAmt = totalAmt + Double.parseDouble(list.get("totalAmount").toString());
            totalReceived = totalReceived + Double.parseDouble(list.get("totalReceivable").toString());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dayEndDate = simpleDateFormat.parse(list.get("date").toString());
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String str = df.format(dayEndDate);
            table.addCell(new Phrase((str) + "", font1));
            table.addCell(new Phrase(list.get("sINo") + "", font1));
            table.addCell(new Phrase(list.get("customerName").toString() + "", font1));
            table.addCell(new Phrase(list.get("totalAmount").toString() + "", font1));
            table.addCell(new Phrase(list.get("totalReceivable").toString() + "", font1));
            table.addCell(new Phrase(list.get("status").toString() + "", font1));
        }
        table.addCell(new Phrase("Grand Total"));
        table.addCell(new Phrase(" "));
        table.addCell(new Phrase(" "));
        table.addCell(new Phrase(new DecimalFormat("#,##0.00").format(totalAmt)));
        table.addCell(new Phrase(new DecimalFormat("#,##0.00").format(totalReceived)));
        table.addCell(new Phrase(" "));
        return table;
    }

    public List<SalesListResponsePojo> getAllPaymentListReport() {
        List<PaymentMethod> paymentMethods = paymentMethodRepository.findAll();
        return ReportMapper.mapPaymentDetailsEntityToPojo(paymentMethods);
    }
    public List<SalesListResponsePojo> getAllPaymentVoucherReport() {
        List<PaymentVoucher> paymentVouchers = paymentVoucherRepository.findAll();
        return ReportMapper.mapPaymentVoucherEntityToPojo(paymentVouchers);
    }
    public List<SalesListResponsePojo> getAllTableList() {
        List<TablesPos> tablesPos=tablesPosRepository.findAll();
        return ReportMapper.mapTableEntityToPojo(tablesPos);
    }

    public List<CustomerPojo> getAllCustomers() {
        List<Customer> customerList = customerRepository.findAll();
        return ReportMapper.mapCustomerEntityToPojo(customerList);
    }

    public List<SalesListResponsePojo> getAllSalesInvoice() {
        List<SalesInvoice> salesInvoices = salesInvoiceRepository.findAllBySIStatus("Prepared");
        return ReportMapper.mapSalesInvoiceEntityToPojo(salesInvoices);
    }

    public List<SalesListResponsePojo> getAllSalesCancelInvoice() {
        List<SalesInvoice> salesInvoices = salesInvoiceRepository.findAllBySIStatus("Cancelled Invoice");
        return ReportMapper.mapSalesInvoiceEntityToPojo(salesInvoices);
    }
    @Transactional
    public PaginatedResponsePojo getRestaurantInvoiceList(SalesRequestPojo requestPojo) {

        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SalesInvoiceDetails.class);
//        PageRequest pageRequest = new PageRequest();
        detachedCriteria.createAlias("sIId", "sIId", JoinType.LEFT_OUTER_JOIN);
        detachedCriteria.createAlias("sIId.customerId", "customerId", JoinType.LEFT_OUTER_JOIN);
        detachedCriteria.createAlias("sIId.CurrencyId", "CurrencyId", JoinType.LEFT_OUTER_JOIN);
        detachedCriteria.createAlias("sIId.ExchangeRateId", "ExchangeRateId", JoinType.LEFT_OUTER_JOIN);
        detachedCriteria.createAlias("sIId.inventoryLocation", "inventoryLocation", JoinType.LEFT_OUTER_JOIN);
        detachedCriteria.createAlias("itemId", "itemId", JoinType.LEFT_OUTER_JOIN);
        detachedCriteria.createAlias("sIId.UserId", "employeeId", JoinType.LEFT_OUTER_JOIN);
        String dat = requestPojo.getFromDate();
        long date = new SimpleDateFormat("yyyy-MM-dd").parse(dat, new ParsePosition(0)).getTime();
        java.sql.Date fromDate = new java.sql.Date(date);
        String dat1 = requestPojo.getToDate();
        long date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dat1, new ParsePosition(0)).getTime();
        java.sql.Date toDate = new java.sql.Date(date1);
        //code added on 31/08/2017 for not retrieving canceled invoice by @rahul
        PaginatedResponsePojo paginatedResponsePojo = new PaginatedResponsePojo();


        List<SalesReportListResponsePojo> list = new ArrayList<>();
        List<SalesReportListResponsePojo> resultList = new ArrayList<>();
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.property("sIId.SINo"), "SQNo");
        projectionList.add(Projections.property("sIId.SIId"), "salesinvoiceId");
        projectionList.add(Projections.property("sIId.SIDate"), "date");
        projectionList.add(Projections.property("CurrencyId.currencySymbol"), "currencySymbol");
        projectionList.add(Projections.property("ExchangeRateId.exchangeRateValue"), "exchangeRate");
        projectionList.add(Projections.property("customerId.customerName"), "customerName");
        projectionList.add(Projections.property("inventoryLocation.InventoryLocationName"), "InventoryLocationName");
        projectionList.add(Projections.property("sIId.SIStatus"), "SQStatus");
        projectionList.add(Projections.property("sIId.invoiceType"), "invoiceType");
        projectionList.add(Projections.property("sIId.TotalAmount"), "totalAmount");
        projectionList.add(Projections.property("sIId.TotalReceived"), "totalReceivable");
        projectionList.add(Projections.property("sIId.totalDiscountAmount"), "discountAmount");
        projectionList.add(Projections.property("sIId.SalesTotalTaxAmt"), "salesTotalTaxAmt");
        projectionList.add(Projections.sum("ItemAmountExcTax"), "ItemAmountExcTax");
        projectionList.add(Projections.groupProperty("sIId"));
        detachedCriteria.setProjection(projectionList).setResultTransformer(new AliasToBeanResultTransformer(SalesReportListResponsePojo.class));
//        list = getRestaurantMethodList();
        PaginatedResponsePojo paymentTypePojo = getPaymentMethodReportList(requestPojo);
        for (SalesReportListResponsePojo salesList : list) {
            double cost;
            cost = Double.parseDouble(salesList.getExchangeRate()) * salesList.getItemAmountExcTax();
            salesList.setBalance(cost);
            SalesInvoice salesInvoice=salesInvoiceRepository.findAllBySIId(salesList.getSalesinvoiceId());
            RestaurantTokenRecord restaurantTokenRecord = restaurantTokenRecordRepository.findFirstBySiId(salesInvoice);
            if (paymentTypePojo != null) {
                for (SalesReportListResponsePojo paymentList : paymentTypePojo.getSalesReportList()) {
                    if (StringUtils.equalsIgnoreCase(paymentList.getSQNo(), salesList.getSQNo()))
                        salesList.setPaymentName(paymentList.getPaymentName());
                }
            }
            if (restaurantTokenRecord != null) {
                salesList.setEmployeeName(restaurantTokenRecord.getWaiterName());
                Employee employee = employeeRepository.findAllByEmployeeName(salesList.getEmployeeName());
                if(employee!=null&&employee.getIncentives()!=null) {
                    if(StringUtils.isEmpty(employee.getIncentives())){
                        employee.setIncentives("0");
                    }
                    salesList.setIncentives(Double.valueOf(employee.getIncentives()));
                    Double amt = (salesList.getIncentives() * salesList.getItemAmountExcTax()) / 100;
                    salesList.setIncentivesAmount(amt);
                }
                salesList.setTableName(restaurantTokenRecord.getTableName());
                salesList.setPax(restaurantTokenRecord.getPax());
                resultList.add(salesList);
            }
        }
        paginatedResponsePojo.setData(resultList);
        paginatedResponsePojo.setSalesReportList(resultList);
        return paginatedResponsePojo;
    }
//    @Transactional
//    public List<SalesInvoiceDetails> getRestaurantMethodList(SalesRequestPojo requestPojo,Date fromDate,Date toDate,Pageable pageable) {
//        Page<SalesInvoiceDetails> list = salesInvoiceDetailsRepository.findAll(new Specification<PosPaymentTypes>() {
//            @Override
//            public Predicate toPredicate(Root<PosPaymentTypes> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                List<Predicate> predicatesList = new ArrayList<>();
//
//                if (fromDate!=null&&toDate!=null) {
//                    Predicate predicates = cb.between(root.get("sIId").get("sIDate"),  fromDate,toDate);
//                    predicatesList.add(predicates);
//                }
//                if (requestPojo.getFromSID() != null && requestPojo.getToSID() != null && requestPojo.getFromSID() != 0 && requestPojo.getToSID() != 0) {
//                    Predicate predicate = cb.between(root.get("sIId").get("sIId"), requestPojo.getFromSID(), requestPojo.getToSID());
//                    predicatesList.add(predicate);
//                }
//                if (StringUtils.isNotEmpty(requestPojo.getInvoiceType())) {
//                    Predicate predicate = cb.equal(root.get("sIId").get("invoiceType"),requestPojo.getInvoiceType());
//                    predicatesList.add(predicate);
//                }
//                if (StringUtils.isNotEmpty(requestPojo.getSearchText())) {
//                    Predicate predicate = cb.equal(root.get("sIId").get("referenceno"),requestPojo.getSearchText());
//                    predicatesList.add(predicate);
//                }
//                if (StringUtils.isNotEmpty(requestPojo.getItemId())) {
//                    Predicate predicate = cb.equal(root.get("itemId").get("itemId"),Long.parseLong(requestPojo.getItemId()));
//                    predicatesList.add(predicate);
//                }
//                if (requestPojo.getSelectedList() != null) {
//                    Predicate predicate = cb.equal(root.get("sIId").get("userId").in("employeeName"), requestPojo.getSelectedList());
//                    predicatesList.add(predicate);
//                }
//                if (requestPojo.getCustomerId() != null && requestPojo.getCustomerId() != 0) {
//                    Predicate predicate = cb.equal(root.get("sIId").get("customerId").get("customerId"), requestPojo.getCustomerId());
//                    predicatesList.add(predicate);
//                }
//                Predicate predicates = cb.equal(root.get("sIId").get("sIStatus"), "Prepared");
//                predicatesList.add(predicates);
//                return cb.and(predicatesList.toArray(new Predicate[0]));
//            }
//        }, pageable);
//        return list.getContent();
//    }
    @Transactional
    public PaginatedResponsePojo getPaymentMethodReportList(SalesRequestPojo requestPojo) {

        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PosPaymentTypes.class);
        detachedCriteria.createAlias("salesInvoice", "sales", JoinType.LEFT_OUTER_JOIN);
        String dat = requestPojo.getFromDate();
        long date = new SimpleDateFormat("yyyy-MM-dd").parse(dat, new ParsePosition(0)).getTime();
        java.sql.Date startDate = new java.sql.Date(date);
        String dat1 = requestPojo.getToDate();
        long date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dat1, new ParsePosition(0)).getTime();
        java.sql.Date endDate = new java.sql.Date(date1);
        PaginatedResponsePojo paginatedResponsePojo = new PaginatedResponsePojo();
        List<PosPaymentTypes> salesInvoices = getPaymentMethodList(requestPojo,startDate,endDate,null);
        String paytype = null;
        PaymentMethod payments = null;
        if (requestPojo.getPaymentId() != null) {
            payments = paymentMethodRepository.findOne(requestPojo.getPaymentId());
            paytype = payments.getPaymentmethodName();
        }
        PaymentTypePojo paymentTypePojo = mapPaymentReportEntityToPojoList(salesInvoices, paytype);
        List<SalesReportListResponsePojo> list = paymentTypePojo.getSalesReportListResponsePojoList();
        List<SalesReportListResponsePojo> payment = paymentTypePojo.getPaymentTypeTotals();
        Map<String, Double> outputList = payment.stream().filter(c -> c.getPaymentName() != null).collect(
                Collectors.groupingBy(SalesReportListResponsePojo::getPaymentName, Collectors.summingDouble(SalesReportListResponsePojo::getTotalAmount)));
        paginatedResponsePojo.setSalesReportList(list);
        paginatedResponsePojo.setData(list);
        paginatedResponsePojo.setDoubleMap(outputList);
        return paginatedResponsePojo;
    }
    @Transactional
    public List<PosPaymentTypes> getPaymentMethodList(SalesRequestPojo requestPojo,Date fromDate,Date toDate,Pageable pageable) {
        Page<PosPaymentTypes> list = posPaymentTypesRepository.findAll(new Specification<PosPaymentTypes>() {
            @Override
            public Predicate toPredicate(Root<PosPaymentTypes> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicatesList = new ArrayList<>();

                if (fromDate!=null&&toDate!=null) {
                    Predicate predicates = cb.between(root.get("salesInvoice").get("sIDate"),  fromDate,toDate);
                    predicatesList.add(predicates);
                }
                if (requestPojo.getFromSID() != null && requestPojo.getToSID() != null && requestPojo.getFromSID() != 0 && requestPojo.getToSID() != 0) {
                    Predicate predicate = cb.between(root.get("salesInvoice").get("sIId"), requestPojo.getFromSID(), requestPojo.getToSID());
                    predicatesList.add(predicate);
                }
                if (requestPojo.getCustomerId() != null && requestPojo.getCustomerId() != 0) {
                    Predicate predicate = cb.equal(root.get("salesInvoice").get("customerId").get("customerId"), requestPojo.getCustomerId());
                    predicatesList.add(predicate);
                }
                Predicate predicates = cb.equal(root.get("salesInvoice").get("sIStatus"), "Prepared");
                predicatesList.add(predicates);
                if (requestPojo.getPaymentId() != null && requestPojo.getPaymentId() != 0) {
                    PaymentMethod paymentMethod = paymentMethodRepository.findOne(requestPojo.getPaymentId());
                    if (requestPojo.getPaymentId() != null && requestPojo.getPaymentId() != 0) {
                        if (StringUtils.equalsIgnoreCase(paymentMethod.getPaymentmethodType(), "Bank")) {
                            String type = "paymentType\":\"" + requestPojo.getPaymentId().toString() + "";
                            Predicate predicate1 = cb.like(root.get("cardPayment"), "%" + type + "%");
                            Predicate predicates1 = cb.like(root.get("bankPayment"), "%" + type + "%");
                            Predicate predicates12 = cb.like(root.get("voucherPayment"), "%" + type + "%");
                            Predicate or = cb.or(predicate1, predicates1, predicates12);
                            predicatesList.add(or);

                        } else if (StringUtils.equalsIgnoreCase(paymentMethod.getPaymentmethodType(), "Cash")) {
                            Predicate predicate = cb.gt(root.get("totalCashPayment"),  0D);
                            predicatesList.add(predicate);
                        } else {
                            String type = "paymentType\":" + requestPojo.getPaymentId();
                            Predicate predicate1 = cb.like(root.get("cardPayment"), "%" + type + "%");
                            Predicate predicates1 = cb.like(root.get("bankPayment"), "%" + type + "%");
                            Predicate predicates12 = cb.like(root.get("voucherPayment"), "%" + type + "%");
                            Predicate or = cb.or(predicate1, predicates1, predicates12);
                            predicatesList.add(or);
                        }
                    }
                }
                return cb.and(predicatesList.toArray(new Predicate[0]));
            }
        }, pageable);
        return list.getContent();
    }

    @Transactional
    public PaginatedResponsePojo getRestaurantInvoicepaymentList(String type1, String tables, SalesRequestPojo requestPojo) {
        List<RestaurantTokenRecord> restaurantTokenRecords = new ArrayList<>();
        List<SalesInvoice> salesInvoiceList = new ArrayList<>();
        String dat = requestPojo.getFromDate();
        long date = new SimpleDateFormat("yyyy-MM-dd").parse(dat, new ParsePosition(0)).getTime();
        java.sql.Date startDate = new java.sql.Date(date);
        String dat1 = requestPojo.getToDate();
        long date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dat1, new ParsePosition(0)).getTime();
        java.sql.Date endDate = new java.sql.Date(date1);
        if (!StringUtils.isEmpty(tables)) {
           restaurantTokenRecords=restaurantTokenRecordRepository.findByTableIdAndSiIdNotNull(tables);
        }
        PaginatedResponsePojo paginatedResponsePojo = new PaginatedResponsePojo();
        List<PosPaymentTypes> salesInvoices = new ArrayList<>();
        if(restaurantTokenRecords.size()>0)
        salesInvoiceList = restaurantTokenRecords.parallelStream().distinct().map(RestaurantTokenRecord::getSiId).collect(Collectors.toList());
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "salesInvoice.sIId"));
        Pageable pageable = null;
        if (requestPojo.getFilterApplied() == false) {
            pageable = new PageRequest(requestPojo.getPageNo(), paginatedConstants, sort);
        }
        salesInvoices = getPaginatedListrestaurantInvoice(requestPojo,type1,startDate,endDate,salesInvoiceList,pageable);

        String paytype = null;
        PaymentMethod payments = null;
        if (requestPojo.getPaymentId() != null) {
            payments = paymentMethodRepository.findOne(requestPojo.getPaymentId());
            paytype = payments.getPaymentmethodName();
        }
        PaymentTypePojo paymentTypePojo = mapPaymentReportEntityToPojoList(salesInvoices, paytype);
        List<SalesReportListResponsePojo> list = paymentTypePojo.getSalesReportListResponsePojoList();
        List<SalesReportListResponsePojo> payment = paymentTypePojo.getPaymentTypeTotals();
        Map<String, Double> outputList = payment.stream().filter(c -> c.getPaymentName() != null).collect(
                Collectors.groupingBy(SalesReportListResponsePojo::getPaymentName, Collectors.summingDouble(SalesReportListResponsePojo::getTotalAmount)));
        List<SalesReportListResponsePojo> resultList = new ArrayList<>();
        for (SalesReportListResponsePojo salesList : list) {
            double cost;
            cost = salesList.getItemAmountExcTax();
            salesList.setBalance(cost);
            SalesInvoice salesInvoice=salesInvoiceRepository.findOne(salesList.getSalesinvoiceId());
            RestaurantTokenRecord restaurantTokenRecord = restaurantTokenRecordRepository.findFirstBySiId(salesInvoice);
            if (restaurantTokenRecord != null) {
                salesList.setEmployeeName(restaurantTokenRecord.getWaiterName());
                Employee employee = employeeRepository.findByEmployeeName(salesList.getEmployeeName());
                if (employee != null && employee.getIncentives() != null) {
                    salesList.setIncentives(Double.valueOf(employee.getIncentives()));
                    Double amt = (salesList.getIncentives() * salesList.getItemAmountExcTax()) / 100;
                    salesList.setIncentivesAmount(amt);
                }
                salesList.setTableName(restaurantTokenRecord.getTableName());
                salesList.setPax(restaurantTokenRecord.getPax());
                resultList.add(salesList);
            }
        }
        paginatedResponsePojo.setSalesReportList(resultList);
        paginatedResponsePojo.setData(resultList);
        paginatedResponsePojo.setDoubleMap(outputList);
        paginatedResponsePojo.setFirst(requestPojo.isFirstPage());
        paginatedResponsePojo.setLast(requestPojo.isLastPage());
        paginatedResponsePojo.setNext(requestPojo.isNextPage());
        paginatedResponsePojo.setPrev(requestPojo.isPrevPage());
        paginatedResponsePojo=calculatePagination(paginatedResponsePojo,resultList.size());
        return paginatedResponsePojo;
    }
//    @Transactional
//    public PaginatedResponsePojo getsalesDayEndInvoice(SalesRequestPojo requestPojo) throws Exception {
//        List<Map> listResponsePojos = new ArrayList<>();
//        PaginatedResponsePojo paginatedResponsePojo = new PaginatedResponsePojo();
//        String dat = requestPojo.getFromDate();
//        long date = new SimpleDateFormat("yyyy-MM-dd").parse(dat, new ParsePosition(0)).getTime();
//        java.sql.Date fromDate = new java.sql.Date(date);
//        String dat1 = requestPojo.getToDate();
//        long date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dat1, new ParsePosition(0)).getTime();
//        java.sql.Date toDate = new java.sql.Date(date1);
//        Calendar cal = Calendar.getInstance();       // get calendar instance
//        cal.setTime(fromDate);                           // set cal to date
//        cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
//        cal.set(Calendar.MINUTE, 0);                 // set minute in hour
//        cal.set(Calendar.SECOND, 0);                 // set second in minute
//        cal.set(Calendar.MILLISECOND, 0);            // set millis in second
//        Date zeroedDate = cal.getTime();
//        cal.setTime(toDate);                           // set cal to date
//        cal.set(Calendar.HOUR_OF_DAY, 23);            // set hour to midnight
//        cal.set(Calendar.MINUTE, 59);                 // set minute in hour
//        cal.set(Calendar.SECOND, 59);                 // set second in minute
//        cal.set(Calendar.MILLISECOND, 0);
//        Date date2 = cal.getTime();
//        listResponsePojos = restaurantTokenRecordRepository.findByDayEndListing(zeroedDate,date2);
//        Map<String, Date> resultSet = new HashMap<>();
//        if (listResponsePojos.size() > 0) {
//            List<String> invoicenos = new ArrayList<>();
//            for (Map salesReportListResponsePojo : listResponsePojos) {
//                invoicenos.add(salesReportListResponsePojo.get("sQNo").toString());
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date dayEndDate = simpleDateFormat.parse(salesReportListResponsePojo.get("date").toString());
//                resultSet.put(salesReportListResponsePojo.get("sQNo").toString(), dayEndDate);
//            }
//            DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SalesInvoiceDetails.class);
//            detachedCriteria.add(Restrictions.in("sIId.SINo", invoicenos));
//            if (requestPojo.getSelectedList() != null) {
//                detachedCriteria.add((Restrictions.in("employeeId.EmployeeName", requestPojo.getSelectedList())));
//            }
//            if (requestPojo.getFromSID() != null && requestPojo.getFromSID() != 0 && requestPojo.getToSID() != null && requestPojo.getToSID() != 0) {
//                detachedCriteria.add(Restrictions.between("sIId.SIId", requestPojo.getFromSID(), requestPojo.getToSID()));
//            }
//            if (requestPojo.getCustomerId() != null && requestPojo.getCustomerId() != 0) {
//                detachedCriteria.add((Restrictions.eq("sIId.customerId.customerId", requestPojo.getCustomerId())));
//            }
//            if (StringUtils.isNotEmpty(requestPojo.getItemId())) {
//                detachedCriteria.add((Restrictions.eq("itemId.itemId", Long.parseLong(requestPojo.getItemId()))));
//            }
//            if (StringUtils.isNotEmpty(requestPojo.getStatus())) {
//                detachedCriteria.add((Restrictions.eq("sIId.SIStatus", requestPojo.getStatus())));
//            }
//            List<SalesReportListResponsePojo> list = new ArrayList<>();
//            ProjectionList projectionList = Projections.projectionList();
//            projectionList.add(Projections.property("sIId.SINo"), "SQNo");
//            projectionList.add(Projections.property("sIId.SIId"), "salesinvoiceId");
//            projectionList.add(Projections.property("sIId.SIDate"), "date");
//            projectionList.add(Projections.property("CurrencyId.currencySymbol"), "currencySymbol");
//            projectionList.add(Projections.property("ExchangeRateId.exchangeRateValue"), "exchangeRate");
//            projectionList.add(Projections.property("customerId.customerName"), "customerName");
//            projectionList.add(Projections.property("inventoryLocation.InventoryLocationName"), "InventoryLocationName");
//            projectionList.add(Projections.property("sIId.SIStatus"), "SQStatus");
//            projectionList.add(Projections.property("sIId.TotalAmount"), "totalAmount");
//            projectionList.add(Projections.property("sIId.TotalReceived"), "totalReceivable");
//            projectionList.add(Projections.property("sIId.totalDiscountAmount"), "discountAmount");
//            projectionList.add(Projections.property("sIId.SalesTotalTaxAmt"), "salesTotalTaxAmt");
//            projectionList.add(Projections.sum("ItemAmountExcTax"), "ItemAmountExcTax");
//            projectionList.add(Projections.groupProperty("sIId"));
//            detachedCriteria.setProjection(projectionList).setResultTransformer(new AliasToBeanResultTransformer(SalesReportListResponsePojo.class));
//            list = salesReportsDao.findOnCondition(detachedCriteria, pageRequest);
//
//            for (SalesReportListResponsePojo salesReportListResponsePojo : list) {
//                salesReportListResponsePojo.setDayEndDate(resultSet.get(salesReportListResponsePojo.getSQNo()));
//            }
//            paginatedResponsePojo.setData(list);
//            paginatedResponsePojo.setSalesReportList(list);
//            List<SalesReportListResponsePojo> paymentTypePojo = getPaymentMethodReport(requestPojo,invoicenos);
//            for (SalesReportListResponsePojo salesList : list) {
//                double cost;
//                cost = Double.parseDouble(salesList.getExchangeRate()) * salesList.getItemAmountExcTax();
//                salesList.setBalance(cost);
//                RestaurantTokenRecord restaurantTokenRecord = restaurantTokenRecordRepository.findAllFirstBySiId(salesList.getSalesinvoiceId());
//                if (restaurantTokenRecord != null) {
//                    salesList.setEmployeeName(restaurantTokenRecord.getWaiterName());
//                    salesList.setTableName(restaurantTokenRecord.getTableName());
//                }
//                if (paymentTypePojo != null) {
//                    for (SalesReportListResponsePojo paymentList : paymentTypePojo) {
//                        if (StringUtils.equalsIgnoreCase(paymentList.getSQNo(), salesList.getSQNo()))
//                            salesList.setPaymentName(paymentList.getPaymentName());
//                    }
//                }
//            }
//        }
//        return paginatedResponsePojo;
//    }
//    @Transactional
//    public List<SalesReportListResponsePojo> getPaymentMethodReport(SalesRequestPojo requestPojo,List<String> silist) {
//
//        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PosPaymentTypes.class);
//        PageRequest pageRequest = new PageRequest();
//        detachedCriteria.createAlias("salesInvoice", "sales", JoinType.LEFT_OUTER_JOIN);
//        FiscalYear fiscalYear = fiscalYearDao.getCurrentFiscalyearZero();
//        if (StringUtils.isEmpty(requestPojo.getToDate()) || StringUtils.isEmpty(requestPojo.getFromDate())) {
//            requestPojo.setFromDate(DateFormatUtils.format(fiscalYear.getBegin(), "yyyy-MM-dd").toString());
//            requestPojo.setToDate(DateFormatUtils.format(fiscalYear.getEnd(), "yyyy-MM-dd").toString());
//        }
//        detachedCriteria.add(Restrictions.ne("sales.SIStatus", "Cancelled Invoice"));
//        detachedCriteria.add(Restrictions.in("sales.SINo",silist));
//        detachedCriteria.add(Restrictions.ne("sales.SIStatus", "Draft Cancelled Invoice"));
//        if (requestPojo.getPaymentId() != null && requestPojo.getPaymentId() != 0) {
//            PaymentMethod paymentMethod = hiposDAO.getPaymentmethodObject(requestPojo.getPaymentId());
//            if (requestPojo.getPaymentId() != null && requestPojo.getPaymentId() != 0) {
//                if (StringUtils.equalsIgnoreCase(paymentMethod.getPaymentmethodType(), "Bank")) {
//                    String type = "paymentType\":\"" + requestPojo.getPaymentId().toString() + "";
//                    detachedCriteria.add(Restrictions.disjunction().add(Restrictions.like("voucherPayment", type, MatchMode.ANYWHERE))
//                            .add(Restrictions.like("cardPayment", type, MatchMode.ANYWHERE))
//                            .add(Restrictions.like("bankPayment", type, MatchMode.ANYWHERE)));
//
//                } else if (StringUtils.equalsIgnoreCase(paymentMethod.getPaymentmethodType(), "Cash")) {
//                    detachedCriteria.add(Restrictions.gt("totalCashPayment", 0D));
//                } else {
//                    String type = "paymentType\":" + requestPojo.getPaymentId();
//                    detachedCriteria.add(Restrictions.disjunction().add(Restrictions.like("voucherPayment", type, MatchMode.ANYWHERE))
//                            .add(Restrictions.like("cardPayment", type, MatchMode.ANYWHERE))
//                            .add(Restrictions.like("bankPayment", type, MatchMode.ANYWHERE)));
//                }
//            }
//        }
//
//        List<PosPaymentTypes> salesInvoices = salesReportsDao.findOnCondition(detachedCriteria, pageRequest);
//        String paytype = null;
//        PaymentMethod payments = null;
//        if (requestPojo.getPaymentId() != null) {
//            payments = getSession().get(PaymentMethod.class, requestPojo.getPaymentId());
//            paytype = payments.getPaymentmethodName();
//        }
//        PaymentTypePojo paymentTypePojo = reportMapper.mapPaymentReportEntityToPojoList(salesInvoices, paytype);
//        List<SalesReportListResponsePojo> list = paymentTypePojo.getSalesReportListResponsePojoList();
//        return list;
//    }

    public PaginatedResponsePojo calculatePagination(PaginatedResponsePojo basePojo, int size) {
        if (basePojo.isLast() == true) {
            basePojo.setFirst(false);
            basePojo.setNext(true);
            basePojo.setPrev(false);
        } else if (basePojo.isFirst() == true) {
            basePojo.setLast(false);
            basePojo.setNext(false);
            basePojo.setPrev(true);
            if (basePojo.isStatus() == true) {
                basePojo.setLast(true);
                basePojo.setNext(true);
            }
        } else if (basePojo.isNext() == true) {
            basePojo.setLast(false);
            basePojo.setFirst(false);
            basePojo.setPrev(false);
            basePojo.setNext(false);
            if (basePojo.isStatus() == true) {
                basePojo.setLast(true);
                basePojo.setNext(true);
            }
        } else if (basePojo.isPrev() == true) {
            basePojo.setLast(false);
            basePojo.setFirst(false);
            basePojo.setNext(false);
            basePojo.setPrev(false);
            if (basePojo.isStatus() == true) {
                basePojo.setPrev(true);
                basePojo.setFirst(true);
            }
        }else {
            basePojo.setLast(true);
            basePojo.setFirst(true);
            basePojo.setNext(true);
            basePojo.setPrev(true);
        }
        if (size == 0) {
            basePojo.setLast(true);
            basePojo.setFirst(true);
            basePojo.setNext(true);
            basePojo.setPrev(true);
        }
        return basePojo;
    }
    @Transactional
    public List<PosPaymentTypes> getPaginatedListrestaurantInvoice(SalesRequestPojo requestPojo,String type1,Date fromDate,Date toDate,List<SalesInvoice> salesInvoices, Pageable pageable) {
        Page<PosPaymentTypes> list = posPaymentTypesRepository.findAll(new Specification<PosPaymentTypes>() {
            @Override
            public Predicate toPredicate(Root<PosPaymentTypes> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicatesList = new ArrayList<>();

                if (fromDate!=null&&toDate!=null) {
                    Predicate predicates = cb.between(root.get("salesInvoice").get("sIDate"),  fromDate,toDate);
                    predicatesList.add(predicates);
                }
                if(salesInvoices.size()>0){
                    Predicate predicates = cb.equal(root.in("salesInvoice"),salesInvoices);
                    predicatesList.add(predicates);
                }
                if (!StringUtils.isEmpty(type1)) {
                    if (StringUtils.equalsIgnoreCase(type1, "discount") || StringUtils.equalsIgnoreCase(type1, "'discount'")) {
                        Predicate predicates = cb.gt(root.get("salesInvoice").get("totalDiscountAmount"),  0D);
                        predicatesList.add(predicates);
                        Predicate predicate = cb.notEqual(root.get("salesInvoice").get("totalReceivable"),  0D);
                        predicatesList.add(predicate);
                    }
                    if (StringUtils.equalsIgnoreCase(type1, "freemeal") || StringUtils.equalsIgnoreCase(type1, "'freemeal'")) {
                        Predicate predicate = cb.equal(root.get("salesInvoice").get("totalReceivable"),  0D);
                        predicatesList.add(predicate);
                    }
                    if (StringUtils.equalsIgnoreCase(type1, "onlineinvoice") || StringUtils.equalsIgnoreCase(type1, "'onlineinvoice'")) {
                        Predicate predicate = cb.isNotNull((root.get("salesInvoice").get("agentId")));
                        predicatesList.add(predicate);
                        if(requestPojo.getAgentId()!=null) {
                            Predicate predicates = cb.equal(root.get("salesInvoice").get("agentId").get("agentId"),requestPojo.getAgentId());
                            predicatesList.add(predicates);
                        }
                    }
                }
                if (requestPojo.getFromSID() != null && requestPojo.getToSID() != null && requestPojo.getFromSID() != 0 && requestPojo.getToSID() != 0) {
                    Predicate predicate = cb.between(root.get("salesInvoice").get("sIId"), requestPojo.getFromSID(), requestPojo.getToSID());
                    predicatesList.add(predicate);
                }
                if (requestPojo.getCustomerId() != null && requestPojo.getCustomerId() != 0) {
                    Predicate predicate = cb.equal(root.get("salesInvoice").get("customerId").get("customerId"), requestPojo.getCustomerId());
                    predicatesList.add(predicate);
                }
                if (requestPojo.getEmployeeId() != null) {
                    Predicate predicate = cb.equal(root.get("salesInvoice").get("userId").get("employeeId"), requestPojo.getEmployeeId());
                    predicatesList.add(predicate);
                }
                if (StringUtils.isNotEmpty(requestPojo.getInvoiceType())) {
                    Predicate predicate = cb.equal(root.get("salesInvoice").get("invoiceType"),requestPojo.getInvoiceType());
                    predicatesList.add(predicate);
                }
                if (StringUtils.isNotEmpty(requestPojo.getPaymentVoucher())) {
                    Predicate predicate = cb.equal(root.get("salesInvoice").get("discountCode"),requestPojo.getPaymentVoucher());
                    predicatesList.add(predicate);
                }
                Predicate predicates = cb.equal(root.get("salesInvoice").get("sIStatus"), "Prepared");
                predicatesList.add(predicates);
                if (requestPojo.getPaymentId() != null && requestPojo.getPaymentId() != 0) {
                    PaymentMethod paymentMethod = paymentMethodRepository.findOne(requestPojo.getPaymentId());
                    if (requestPojo.getPaymentId() != null && requestPojo.getPaymentId() != 0) {
                        if (StringUtils.equalsIgnoreCase(paymentMethod.getPaymentmethodType(), "Bank")) {
                            String type = "paymentType\":\"" + requestPojo.getPaymentId().toString() + "";
                            Predicate predicate1 = cb.like(root.get("cardPayment"), "%" + type + "%");
                            Predicate predicates1 = cb.like(root.get("bankPayment"), "%" + type + "%");
                            Predicate predicates12 = cb.like(root.get("voucherPayment"), "%" + type + "%");
                            Predicate or = cb.or(predicate1, predicates1, predicates12);
                            predicatesList.add(or);

                        } else if (StringUtils.equalsIgnoreCase(paymentMethod.getPaymentmethodType(), "Cash")) {
                            Predicate predicate = cb.gt(root.get("totalCashPayment"),  0D);
                            predicatesList.add(predicate);
                        } else {
                            String type = "paymentType\":" + requestPojo.getPaymentId();
                            Predicate predicate1 = cb.like(root.get("cardPayment"), "%" + type + "%");
                            Predicate predicates1 = cb.like(root.get("bankPayment"), "%" + type + "%");
                            Predicate predicates12 = cb.like(root.get("voucherPayment"), "%" + type + "%");
                            Predicate or = cb.or(predicate1, predicates1, predicates12);
                            predicatesList.add(or);
                        }
                    }
                }
                return cb.and(predicatesList.toArray(new Predicate[0]));
            }
        }, pageable);
        return list.getContent();
    }

    @Transactional
    public PaginatedResponsePojo getsalesDayEndInvoice(SalesRequestPojo requestPojo)throws Exception {
        PaginatedResponsePojo paginatedResponsePojo = new PaginatedResponsePojo();
        String dat = requestPojo.getFromDate();
        long date = new SimpleDateFormat("yyyy-MM-dd").parse(dat, new ParsePosition(0)).getTime();
        java.sql.Date fromDate = new java.sql.Date(date);
        String dat1 = requestPojo.getToDate();
        long date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dat1, new ParsePosition(0)).getTime();
        java.sql.Date toDate = new java.sql.Date(date1);
        Calendar cal = Calendar.getInstance();       // get calendar instance
        cal.setTime(fromDate);                           // set cal to date
        cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
        cal.set(Calendar.MINUTE, 0);                 // set minute in hour
        cal.set(Calendar.SECOND, 0);                 // set second in minute
        cal.set(Calendar.MILLISECOND, 0);            // set millis in second
        Date zeroedDate = cal.getTime();
        cal.setTime(toDate);                           // set cal to date
        cal.set(Calendar.HOUR_OF_DAY, 23);            // set hour to midnight
        cal.set(Calendar.MINUTE, 59);                 // set minute in hour
        cal.set(Calendar.SECOND, 59);                 // set second in minute
        cal.set(Calendar.MILLISECOND, 0);
        Date date2 = cal.getTime();
        List<Map> listResponsePojos = restaurantTokenRecordRepository.findAllBy(zeroedDate,date2);
        Map<String, Date> resultSet = new HashMap<>();
        if (listResponsePojos.size() > 0) {
            List<String> invoicenos = new ArrayList<>();
            for (Map salesReportListResponsePojo : listResponsePojos) {
                invoicenos.add(salesReportListResponsePojo.get("SQNo").toString());
                SimpleDateFormat sfmtDate = new SimpleDateFormat("yyyy-MM-dd");
                Date date3=sfmtDate.parse(salesReportListResponsePojo.get("date").toString());
                resultSet.put(salesReportListResponsePojo.get("SQNo").toString(), date3);
            }
            List<Map> list=new ArrayList<>();
            if(requestPojo.getCustomerId()==null&&requestPojo.getSelectedList()==null){
                list = salesInvoiceDetailsRepository.findAllByConditions(invoicenos,requestPojo.getFromSID(), requestPojo.getToSID());
            }else if(requestPojo.getCustomerId()==null&&requestPojo.getSelectedList().size()>0){
                list = salesInvoiceDetailsRepository.findAllByConditionsEmployee(invoicenos,requestPojo.getSelectedList(),requestPojo.getFromSID(), requestPojo.getToSID());
            }else if(requestPojo.getCustomerId()!=null&&requestPojo.getSelectedList()==null){
                list = salesInvoiceDetailsRepository.findAllByConditionsCustomer(invoicenos,requestPojo.getFromSID(), requestPojo.getToSID(),requestPojo.getCustomerId());
            }else {
                list = salesInvoiceDetailsRepository.findAllByConditionscustomerAndEmployee(invoicenos,requestPojo.getSelectedList(),requestPojo.getFromSID(), requestPojo.getToSID(),requestPojo.getCustomerId());
            }
            for (Map salesReportListResponsePojo : list) {
                salesReportListResponsePojo.get(resultSet.get(salesReportListResponsePojo.get("SQNo")));
            }
            paginatedResponsePojo.setData(list);
            List<SalesReportListResponsePojo> paymentTypePojo = getPaymentMethodReport(requestPojo,invoicenos);
            for (Map salesList : list) {
//                double cost;
//                cost = Double.parseDouble(salesList.getExchangeRate()) * salesList.getItemAmountExcTax();
//                salesList.setBalance(cost);
                SalesInvoice salesInvoice=salesInvoiceRepository.findOne(Long.parseLong(salesList.get("salesinvoiceId").toString()));
                RestaurantTokenRecord restaurantTokenRecord = restaurantTokenRecordRepository.findFirstBySiId(salesInvoice);
                if (restaurantTokenRecord != null) {
                    salesList.put("employeeName",restaurantTokenRecord.getWaiterName());
                    salesList.put("tableName",restaurantTokenRecord.getTableName());
                }
                if (paymentTypePojo != null) {
                    for (SalesReportListResponsePojo paymentList : paymentTypePojo) {
                        if (StringUtils.equalsIgnoreCase(paymentList.getSQNo(), salesList.get("SQNo").toString()))
                            salesList.put("paymentName",paymentList.getPaymentName());
                    }
                }
            }
        }
        return paginatedResponsePojo;
    }
    @Transactional
    public List<SalesReportListResponsePojo> getPaymentMethodReport(SalesRequestPojo requestPojo,List<String> silist) {
        List<PosPaymentTypes> salesInvoices = getPaymentMethodReports(silist,requestPojo,null);
        String paytype = null;
        PaymentMethod payments = null;
        if (requestPojo.getPaymentId() != null) {
            payments = paymentMethodRepository.findOne(requestPojo.getPaymentId());
            paytype = payments.getPaymentmethodName();
        }
        PaymentTypePojo paymentTypePojo = mapPaymentReportEntityToPojoList(salesInvoices, paytype);
        List<SalesReportListResponsePojo> list = paymentTypePojo.getSalesReportListResponsePojoList();
        return list;
    }
    @Transactional
    public List<PosPaymentTypes> getPaymentMethodReports(List<String> silist,SalesRequestPojo requestPojo,Pageable pageable) {
        Page<PosPaymentTypes> list = posPaymentTypesRepository.findAll(new Specification<PosPaymentTypes>() {
            @Override
            public Predicate toPredicate(Root<PosPaymentTypes> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicatesList = new ArrayList<>();
                Predicate predicates = cb.equal(root.get("salesInvoice").get("sIStatus"), "Prepared");
                predicatesList.add(predicates);
                if(silist.size()>0){
                    Predicate predicate = cb.equal(root.get("salesInvoice").in("sINo"),silist);
                    predicatesList.add(predicate);
                }
                if (requestPojo.getPaymentId() != null && requestPojo.getPaymentId() != 0) {
                    PaymentMethod paymentMethod = paymentMethodRepository.findOne(requestPojo.getPaymentId());
                    if (requestPojo.getPaymentId() != null && requestPojo.getPaymentId() != 0) {
                        if (StringUtils.equalsIgnoreCase(paymentMethod.getPaymentmethodType(), "Bank")) {
                            String type = "paymentType\":\"" + requestPojo.getPaymentId().toString() + "";
                            Predicate predicate1 = cb.like(root.get("cardPayment"), "%" + type + "%");
                            Predicate predicates1 = cb.like(root.get("bankPayment"), "%" + type + "%");
                            Predicate predicates12 = cb.like(root.get("voucherPayment"), "%" + type + "%");
                            Predicate or = cb.or(predicate1, predicates1, predicates12);
                            predicatesList.add(or);

                        } else if (StringUtils.equalsIgnoreCase(paymentMethod.getPaymentmethodType(), "Cash")) {
                            Predicate predicate = cb.gt(root.get("totalCashPayment"),  0D);
                            predicatesList.add(predicate);
                        } else {
                            String type = "paymentType\":" + requestPojo.getPaymentId();
                            Predicate predicate1 = cb.like(root.get("cardPayment"), "%" + type + "%");
                            Predicate predicates1 = cb.like(root.get("bankPayment"), "%" + type + "%");
                            Predicate predicates12 = cb.like(root.get("voucherPayment"), "%" + type + "%");
                            Predicate or = cb.or(predicate1, predicates1, predicates12);
                            predicatesList.add(or);
                        }
                    }
                }
                return cb.and(predicatesList.toArray(new Predicate[0]));
            }
        }, pageable);
        return list.getContent();
    }
    public PaginatedResponsePojo agentReport(SalesRequestPojo requestPojo) {

       List<SalesInvoice> salesInvoice =new ArrayList<>();
        String dat = requestPojo.getFromDate();
        long date = new SimpleDateFormat("yyyy-MM-dd").parse(dat, new ParsePosition(0)).getTime();
        java.sql.Date fromDate = new java.sql.Date(date);
        String dat1 = requestPojo.getToDate();
        long date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dat1, new ParsePosition(0)).getTime();
        java.sql.Date toDate = new java.sql.Date(date1);
        List<Map> list = new ArrayList<>();
        list = salesInvoiceRepository.findAllBy(fromDate,toDate);
        PaginatedResponsePojo paginatedResponsePojo = new PaginatedResponsePojo();

        if (requestPojo.getAgentId() != null && requestPojo.getAgentId() != 0) {
            list= salesInvoiceRepository.findAllByAgentIdAndSIDate(requestPojo.getAgentId(),fromDate,toDate);
        }
        paginatedResponsePojo.setData(list);
        paginatedResponsePojo.setMapList(list);
        return paginatedResponsePojo;
    }
    public List<AgentPojo> getAgentList(String search) {
        List<Agent> list = new ArrayList<>();
        if (org.apache.commons.lang3.StringUtils.isEmpty(search)) {
            list = agentRepository.findAll();
        } else {
            list = agentRepository.findAllByAgentName(search);
        }
        List<AgentPojo> typePojos = AgentMapper.mapAgentEntityToPojo(list);
        return typePojos;
    }


    public void calculatePagination(int listSize, PaginatedResponsePojo paginatedResponsePojo, BasePojo basePojo) {
        if (basePojo.isLastPage()) {
            paginatedResponsePojo.setFirst(false);
            paginatedResponsePojo.setLast(true);
            paginatedResponsePojo.setNext(true);
            paginatedResponsePojo.setPrev(true);

        }
    }
    public PaymentTypePojo mapPaymentReportEntityToPojoList(List<PosPaymentTypes> salesInvoices, String paymentMode) {
        List<SalesReportListResponsePojo> list = new ArrayList<>();
        PaymentTypePojo paymentTypePojo = new PaymentTypePojo();
        List<SalesReportListResponsePojo> salesReportListResponsePojoList = salesInvoices.stream().map(salesInvoice -> {
                    SalesReportListResponsePojo pojo = new SalesReportListResponsePojo
                            (salesInvoice.getSalesInvoice().getSIDate());
                    if(salesInvoice.getSalesInvoice().getCustomerId()!=null)
                    pojo.setCustomerName(salesInvoice.getSalesInvoice().getCustomerId().getFirstName());
                    pojo.setFormNo(salesInvoice.getSalesInvoice().getSINo());
                    pojo.setSalesinvoiceId(salesInvoice.getSalesInvoice().getSIId());
                    pojo.setDiscountAmount(salesInvoice.getSalesInvoice().getTotalDiscountAmount());
                    if(salesInvoice.getSalesInvoice().getAgentId()!=null) {
                        pojo.setAgentName(salesInvoice.getSalesInvoice().getAgentId().getAgentName());
                    }
                    pojo.setSalesTotalTaxAmt(salesInvoice.getSalesInvoice().getSalesTotalTaxAmt());
                    pojo.setItemAmountExcTax(salesInvoice.getSalesInvoice().getTotalAmount()-salesInvoice.getSalesInvoice().getSalesTotalTaxAmt());
                    pojo.setMemo(salesInvoice.getSalesInvoice().getMemo());
                    pojo.setTotalAmount(salesInvoice.getSalesInvoice().getTotalAmount());
                    if (HiposUtil.isStringNullrEmpty(paymentMode)) {
                        pojo.setTotalReceivable(salesInvoice.getSalesInvoice().getTotalReceived());
                    }
                    pojo.setSQNo(salesInvoice.getSalesInvoice().getSINo());
                    pojo.setArbalance(salesInvoice.getSalesInvoice().getARBalance());
                    pojo.setTotalCashPayment(salesInvoice.getTotalCashPayment());
                    pojo.setTotalCardPayment(salesInvoice.getTotalCardPayment());
                    pojo.setTotalBankAmt(salesInvoice.getTotalBankAmt());
                    pojo.setTotalVoucherPayment(salesInvoice.getTotalVoucherPayment());
                    Gson json1 = new Gson();
                    Type bankType = new TypeToken<ArrayList<MultiBankPayment>>() {
                    }.getType();
                    Type type1 = new TypeToken<ArrayList<MultiCardPayment>>() {
                    }.getType();
                    Type type = new TypeToken<ArrayList<MultiVoucherPayment>>() {
                    }.getType();
                    List<MultiVoucherPayment> multiVoucherPayments = new ArrayList<>();
                    multiVoucherPayments = json1.fromJson(salesInvoice.getVoucherPayment(), type);
                    pojo.setMultiVoucherPayments(multiVoucherPayments);
                    List<MultiCardPayment> multiCardPayments = new ArrayList<>();
                    multiCardPayments = json1.fromJson(salesInvoice.getCardPayment(), type1);
                    pojo.setCardPaymentList(multiCardPayments);
                    List<MultiBankPayment> multiBankPayment = json1.fromJson(salesInvoice.getBankPayment(), bankType);
                    pojo.setMultiBankPaymentList(multiBankPayment);
                    if (salesInvoice.getTotalCashPayment() > 0) {
                        SalesReportListResponsePojo posPaymentTypes = new SalesReportListResponsePojo();
                        if (HiposUtil.isStringNullrEmpty(paymentMode) || StringUtils.equalsIgnoreCase("Cash", paymentMode)) {
                            if (StringUtils.isNotEmpty(pojo.getPaymentName())) {
                                if (pojo.getPaymentName().indexOf("Cash") == -1) {
                                    pojo.setPaymentName("Cash" + "," + pojo.getPaymentName());
                                    posPaymentTypes.setPaymentName("Cash");
                                }
                            } else {
                                pojo.setPaymentName("Cash");
                                posPaymentTypes.setPaymentName(pojo.getPaymentName());
                            }
                            if (StringUtils.equalsIgnoreCase("Cash", paymentMode)) {
                                pojo.setTotalReceivable(salesInvoice.getTotalCashPayment());
                            }
                            posPaymentTypes.setTotalAmount(salesInvoice.getTotalCashPayment());
                            list.add(posPaymentTypes);
                        }
                    }
                    for (MultiVoucherPayment voucherPaymentList : pojo.getMultiVoucherPayments()) {
                        if (voucherPaymentList.getPaymentType() != null) {
                            SalesReportListResponsePojo posPaymentTypes = new SalesReportListResponsePojo();
                            PaymentMethod paymentMethod = paymentMethodRepository.findOne(voucherPaymentList.getPaymentType());
                            if (HiposUtil.isStringNullrEmpty(paymentMode) || StringUtils.equalsIgnoreCase(paymentMethod.getPaymentmethodName(), paymentMode)) {
                                if (StringUtils.isNotEmpty(pojo.getPaymentName())) {
                                    if (pojo.getPaymentName().indexOf(paymentMethod.getPaymentmethodName()) == -1) {
                                        pojo.setPaymentName(paymentMethod.getPaymentmethodName() + "," + pojo.getPaymentName());
                                        posPaymentTypes.setPaymentName(paymentMethod.getPaymentmethodName());
                                    }
                                } else {
                                    pojo.setPaymentName(paymentMethod.getPaymentmethodName());
                                    posPaymentTypes.setPaymentName(pojo.getPaymentName());
                                }
                            }
                            if (StringUtils.equalsIgnoreCase(paymentMethod.getPaymentmethodName(), paymentMode)) {
                                pojo.setTotalReceivable(voucherPaymentList.getVoucherAmt());
                            }
                            posPaymentTypes.setTotalAmount(voucherPaymentList.getVoucherAmt());
                            list.add(posPaymentTypes);
                        }
                    }
                    for (MultiBankPayment bankPayment : pojo.getMultiBankPaymentList()) {
                        if (bankPayment.getPaymentType() != null) {
                            Long paymentId = Long.parseLong(bankPayment.getPaymentType());
                            SalesReportListResponsePojo posPaymentTypes = new SalesReportListResponsePojo();
                            PaymentMethod paymentMethod = paymentMethodRepository.findOne( paymentId);
                            if (HiposUtil.isStringNullrEmpty(paymentMode) || StringUtils.equalsIgnoreCase(paymentMethod.getPaymentmethodName(), paymentMode)) {
                                if (StringUtils.isNotEmpty(pojo.getPaymentName())) {
                                    if (pojo.getPaymentName().indexOf(paymentMethod.getPaymentmethodName()) == -1) {
                                        pojo.setPaymentName(paymentMethod.getPaymentmethodName() + "," + pojo.getPaymentName());
                                        posPaymentTypes.setPaymentName(paymentMethod.getPaymentmethodName());
                                    }
                                } else {
                                    pojo.setPaymentName(paymentMethod.getPaymentmethodName());
                                    posPaymentTypes.setPaymentName(pojo.getPaymentName());
                                }
                            }
                            if (StringUtils.equalsIgnoreCase(paymentMethod.getPaymentmethodName(), paymentMode)) {
                                pojo.setTotalReceivable(bankPayment.getAmount());
                            }
                            posPaymentTypes.setTotalAmount(bankPayment.getAmount());
                            list.add(posPaymentTypes);
                        }
                    }
                    for (MultiCardPayment cardPayment : pojo.getCardPaymentList()) {
                        if (cardPayment.getPaymentType() != null) {
                            SalesReportListResponsePojo posPaymentTypes = new SalesReportListResponsePojo();
                            PaymentMethod paymentMethod = paymentMethodRepository.findOne(cardPayment.getPaymentType());
                            if (HiposUtil.isStringNullrEmpty(paymentMode) || StringUtils.equalsIgnoreCase(paymentMethod.getPaymentmethodName(), paymentMode)) {
                                if (StringUtils.isNotEmpty(pojo.getPaymentName())) {
                                    if (pojo.getPaymentName().indexOf(paymentMethod.getPaymentmethodName()) == -1) {
                                        pojo.setPaymentName(paymentMethod.getPaymentmethodName() + "," + pojo.getPaymentName());
                                        posPaymentTypes.setPaymentName(paymentMethod.getPaymentmethodName());
                                    }
                                } else {
                                    pojo.setPaymentName(paymentMethod.getPaymentmethodName());
                                    posPaymentTypes.setPaymentName(pojo.getPaymentName());
                                }
                            }
                            if (StringUtils.equalsIgnoreCase(paymentMethod.getPaymentmethodName(), paymentMode)) {
                                pojo.setTotalReceivable(cardPayment.getCardAmt());
                            }
                            posPaymentTypes.setTotalAmount(cardPayment.getCardAmt());
                            list.add(posPaymentTypes);
                        }
                    }
                    return pojo;
                }
        ).collect(Collectors.toList());
        paymentTypePojo.setSalesReportListResponsePojoList(salesReportListResponsePojoList);
        paymentTypePojo.setPaymentTypeTotals(list);
        return paymentTypePojo;
    }
    @Transactional
    public PaginatedResponsePojo getDayEndList(BasePojo basePojo) throws Exception {
        PaginatedResponsePojo paginatedResponsePojo=new PaginatedResponsePojo();
//        FiscalYear fiscalYear = fiscalYearDao.getCurrentFiscalyearZero();
//        if (StringUtils.isEmpty(basePojo.getToDate()) || StringUtils.isEmpty(basePojo.getFromDates())) {
//            basePojo.setFromDates(DateFormatUtils.format(fiscalYear.getBegin(), "yyyy-MM-dd").toString());
//            basePojo.setToDate(DateFormatUtils.format(fiscalYear.getEnd(), "yyyy-MM-dd").toString());
//        }
        String dat = basePojo.getFromDates();
        long date = new SimpleDateFormat("yyyy-MM-dd").parse(dat, new ParsePosition(0)).getTime();
        java.sql.Date fromDate = new java.sql.Date(date);
        String dat1 = basePojo.getToDate();
        long date1 = new SimpleDateFormat("yyyy-MM-dd").parse(dat1, new ParsePosition(0)).getTime();
        java.sql.Date toDate = new java.sql.Date(date1);
        Calendar cal = Calendar.getInstance();       // get calendar instance
        cal.setTime(fromDate);                           // set cal to date
        cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
        cal.set(Calendar.MINUTE, 0);                 // set minute in hour
        cal.set(Calendar.SECOND, 0);                 // set second in minute
        cal.set(Calendar.MILLISECOND, 0);            // set millis in second
        Date zeroedDate = cal.getTime();
        cal.setTime(toDate);                           // set cal to date
        cal.set(Calendar.HOUR_OF_DAY, 23);            // set hour to midnight
        cal.set(Calendar.MINUTE, 59);                 // set minute in hour
        cal.set(Calendar.SECOND, 59);                 // set second in minute
        cal.set(Calendar.MILLISECOND, 0);
        Date date2 = cal.getTime();
        Map<Date, List<RestaurantTokenRecordDto>> dateList = new HashMap<>();
        List<Map> list123 = restaurantTokenRecordRepository.findByDateStatus(zeroedDate, date2);
        for (Map rer : list123) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dayEndDate = simpleDateFormat.parse(rer.get("dayEndDate").toString());
            List<Map> list = restaurantTokenRecordRepository.findByDateStatusSIID(dayEndDate);
            Gson gson=new Gson();
            List<RestaurantTokenRecordDto> restaurantTokenRecordDtos=new ArrayList<>();
            Type type=new TypeToken<List<RestaurantTokenRecordDto>>(){}.getType();
            dateList.put(dayEndDate, gson.fromJson(gson.toJson(list),type));
        }
        List<Date> dates = new ArrayList<>(dateList.keySet());
        List<DayEndPojo> resultList = new ArrayList<>();
        for (Date d : dates) {
            List<Long> invoicesList = dateList.get(d).stream().distinct().map(RestaurantTokenRecordDto::getSIId).collect(Collectors.toList());
            List<Double> invoicesamt= dateList.get(d).stream().distinct().map(RestaurantTokenRecordDto::getTotalReceivable).collect(Collectors.toList());
            if (invoicesList.size() > 0) {
                DayEndPojo dayEndPojo = new DayEndPojo();
                dayEndPojo.setDate(d);
//                List<Map> categoryItemList = salesInvoiceDetailsRepository.findByMonthendReportbyCategoryitemID(invoicesList);
                dayEndPojo.setAmount(invoicesamt.stream().mapToDouble(o ->o).sum());
//                dayEndPojo.setTotalItems(categoryItemList.size());
                resultList.add(dayEndPojo);
            }
        }
        paginatedResponsePojo.setData(resultList);
        paginatedResponsePojo.setDayEndPojos(resultList);
        return paginatedResponsePojo;
    }
    @Transactional
    public Map reportForPeriodListReport(String dt) {
        Map result = new HashMap();
        double itemCount = 0;
        List<Category> itemCategoryList = categoryRepository.findAll();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(dt));
        List<String> invoiceList = restaurantTokenRecordRepository.findByDateStatusSdfIID(calendar.getTime());
        if (invoiceList.isEmpty())
            return result;
        List<PosPaymentTypes> posPaymentTypes = posPaymentTypesRepository.findAllBy(invoiceList);
        double totalAmt = posPaymentTypes.stream().mapToDouble(o -> o.getSalesInvoice().getTotalReceivable()).sum();
        double cashAmt = posPaymentTypes.stream().mapToDouble(o -> o.getTotalCashPayment()).sum();
        double serviceCharge = posPaymentTypes.stream().mapToDouble(o -> o.getSalesInvoice().getServiceChargeAmt()).sum();
        double roundingOffAmt = posPaymentTypes.stream().mapToDouble(o -> o.getRoundingAdjustment()).sum();
        double discountAmt = posPaymentTypes.stream().mapToDouble(o -> o.getSalesInvoice().getTotalDiscountAmount()).sum();
        List<MultiBankPayment> multiBankPaymentList = new ArrayList<>();
        List<MultiCardPayment> multiCardPaymentList = new ArrayList<>();
        List<MultiVoucherPayment> multiVoucherPaymentList = new ArrayList<>();
        Gson json = new Gson();
        java.lang.reflect.Type voucherPayment = new TypeToken<ArrayList<MultiVoucherPayment>>() {
        }.getType();
        java.lang.reflect.Type cardPayment = new TypeToken<ArrayList<MultiCardPayment>>() {
        }.getType();
        java.lang.reflect.Type bankPayment = new TypeToken<ArrayList<MultiBankPayment>>() {
        }.getType();
        for (PosPaymentTypes posPaymentTypes1 : posPaymentTypes) {
            multiBankPaymentList.addAll(json.fromJson(posPaymentTypes1.getBankPayment(), bankPayment));
            multiCardPaymentList.addAll(json.fromJson(posPaymentTypes1.getCardPayment(), cardPayment));
            multiVoucherPaymentList.addAll(json.fromJson(posPaymentTypes1.getVoucherPayment(), voucherPayment));
        }
        for (MultiVoucherPayment multiVoucherPayment : multiVoucherPaymentList) {
            MultiBankPayment multiBankPayment = new MultiBankPayment();
            multiBankPayment.setAmount(multiVoucherPayment.getVoucherAmt());
            multiBankPayment.setPaymentType(multiVoucherPayment.getPaymentType().toString());
            multiBankPaymentList.add(multiBankPayment);
        }
        for (MultiCardPayment multiCardPayment : multiCardPaymentList) {
            MultiBankPayment multiBankPayment = new MultiBankPayment();
            multiBankPayment.setAmount(multiCardPayment.getCardAmt());
            multiBankPayment.setPaymentType(multiCardPayment.getPaymentType().toString());
            multiBankPaymentList.add(multiBankPayment);
        }
        for (MultiBankPayment multiBankPayment : multiBankPaymentList) {
            if (!org.springframework.util.StringUtils.isEmpty(multiBankPayment.getPaymentType())) {
                PaymentMethod paymentMethod = paymentMethodRepository.findOne(Long.parseLong(multiBankPayment.getPaymentType()));
                multiBankPayment.setPaymentType(paymentMethod.getPaymentmethodName());
            }
        }
        Map<String, Double> resultMap = multiBankPaymentList.stream().filter(c -> c.getPaymentType() != null).collect(
                Collectors.groupingBy(MultiBankPayment::getPaymentType, Collectors.summingDouble(MultiBankPayment::getAmount)));
        resultMap.put("Cash", cashAmt);
        for (Category itemCategory : itemCategoryList) {
            List<Map> categoryItemList = salesInvoiceDetailsRepository.findAllBy(itemCategory,invoiceList);
            //holds the reference of category total
            float total = (float) categoryItemList.stream().mapToDouble(o -> (java.lang.Double)o.get("totalAmtReceived")).sum();
            Map newVar = new HashMap<>();
            newVar.put("itemId", "");
            newVar.put("itemName", "");
            //newVar.put("totalAmtReceived", "");
            newVar.put("QtySold", "");
            newVar.put("total", String.valueOf(total));

            if (!categoryItemList.isEmpty() && categoryItemList.get(0).get("itemId") != null) {
                itemCount = itemCount + categoryItemList.size();
                categoryItemList.add(newVar);
                result.put(itemCategory.getItemCategoryName(), categoryItemList);
            }
        }
        result.put("Discount", discountAmt);
        if (resultMap.get("Discount Voucher") == null) {
            resultMap.put("Discount Voucher", 0D);
        }
        double amt = resultMap.get("Discount Voucher").doubleValue() - discountAmt;
        resultMap.put("Discount Voucher", amt);
        result.put("Rounding Off", roundingOffAmt);
        result.put("Service Charge", serviceCharge);
        result.put("Total Amount", totalAmt);
        result.put("Payment", resultMap);
        result.put("Item Count", itemCount);
        return result;

    }

    @Transactional
    public void downloadRestaurntInvoiceExcel(OutputStream out, SalesRequestPojo salesRequestPojo,String type1,String table) {
        try {
            String name = null;
            String fromInvoice = null;
            String toInvoice = null;

            PaginatedResponsePojo paginatedResponsePojo = new PaginatedResponsePojo();
            paginatedResponsePojo = getRestaurantInvoicepaymentList(type1,table,salesRequestPojo);
            if (salesRequestPojo.getFromSID() != null && salesRequestPojo.getToSID() != null) {
                SalesInvoice salesInvoice = salesInvoiceRepository.findAllBySIId(salesRequestPojo.getFromSID());
                fromInvoice = salesInvoice.getSINo();
                SalesInvoice salesInvoice1 = salesInvoiceRepository.findAllBySIId(salesRequestPojo.getToSID());
                toInvoice = salesInvoice1.getSINo();
            };
            HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFSheet sheet = hwb.createSheet("First Sheet");
            String name1="Restaurant Invoice Listing";
            if(StringUtils.equals(type1,"'discount'")||StringUtils.equals(type1,"discount")){
                name1 ="Discount Report";
            }
            if(StringUtils.equals(type1,"'freemeal'")||StringUtils.equals(type1,"freemeal")){
                name1 ="Free Meal Report";
            }
            if(StringUtils.equals(type1,"'tableWise'")||StringUtils.equals(type1,"tableWise")){
                name1 ="Table Wise Report";
            }
            Row headerRow = sheet.createRow(0);
            Company company = companyRepository.findAllByStatus("Active");
            headerRow.setHeightInPoints((3 * sheet.getDefaultRowHeightInPoints()));
            headerRow.createCell(2).setCellValue(company.getCompanyName() + "\n" + company.getAddress() + "\n" + name1);
            CellRangeAddress cell = new CellRangeAddress(0, 2, 2, 4);
            sheet.addMergedRegion(cell);
            RegionUtil.setBorderTop(2, cell, sheet, hwb);
            RegionUtil.setBorderLeft(2, cell, sheet, hwb);
            RegionUtil.setBorderRight(2, cell, sheet, hwb);
            RegionUtil.setBorderBottom(2, cell, sheet, hwb);
            HSSFRow headerRow5 = sheet.createRow(3);
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = sdf.parse(salesRequestPojo.getFromDate());
            DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
            String fromDateFormat = df1.format(fromDate);
            Date toDate = sdf.parse(salesRequestPojo.getToDate());
            String toDateFormat = df1.format(toDate);
            headerRow5.createCell(0).setCellValue("From Date");
            headerRow5.createCell(1).setCellValue(fromDateFormat);
            headerRow5.createCell(2).setCellValue("To Date");
            headerRow5.createCell(3).setCellValue(toDateFormat);
            HSSFRow headerRow6 = sheet.createRow(4);
            headerRow6.createCell(0).setCellValue("From Invoice");
            headerRow6.createCell(2).setCellValue("To Invoice");
            if (!StringUtils.isEmpty(fromInvoice) && !StringUtils.isEmpty(toInvoice)) {
                headerRow6.createCell(1).setCellValue(fromInvoice);
                headerRow6.createCell(3).setCellValue(toInvoice);
            }
            HSSFRow headerRow8 = sheet.createRow(5);
            headerRow8.createCell(0).setCellValue("Customer Name");
            if (salesRequestPojo.getCustomerId() != null) {
                Customer customer=customerRepository.findAllByCustomerId(salesRequestPojo.getCustomerId());
                headerRow8.createCell(1).setCellValue(customer.getFirstName());
            }
            HSSFRow headerRow11 = sheet.createRow(6);
            headerRow11.createCell(0).setCellValue("Employee");
            if (salesRequestPojo.getEmployeeId() != null) {
                Employee employee = employeeRepository.findOne(salesRequestPojo.getEmployeeId());
                headerRow11.createCell(1).setCellValue(employee.getEmployeeName());

            }
            HSSFRow headerRow9 = sheet.createRow(7);
            headerRow9.createCell(0).setCellValue("Payment");
            if (salesRequestPojo.getPaymentId() != null) {
                PaymentMethod paymentMethod = paymentMethodRepository.findAllByPaymentmethodId(salesRequestPojo.getPaymentId());
                headerRow9.createCell(1).setCellValue(paymentMethod.getPaymentmethodName());
            }
            HSSFRow headerRow10 = sheet.createRow(8);
            headerRow10.createCell(0).setCellValue("Order Filter");
            if (!StringUtils.isEmpty(salesRequestPojo.getInvoiceType())) {
                headerRow10.createCell(1).setCellValue(salesRequestPojo.getInvoiceType());
            }
            Row headerRow1 = sheet.createRow(9);
            headerRow1.createCell(0).setCellValue("Date");
            headerRow1.createCell(1).setCellValue("SINo");
            headerRow1.createCell(2).setCellValue("Customer Name");
            headerRow1.createCell(3).setCellValue("Table Name");
            headerRow1.createCell(4).setCellValue("Employee Name");
            headerRow1.createCell(5).setCellValue("Discount Amount");
            headerRow1.createCell(6).setCellValue("Tax Amount(CGST:SGST)");
            headerRow1.createCell(7).setCellValue("Invoice Amount");
            if(StringUtils.equals(type1,"'freemeal'")||StringUtils.equals(type1,"freemeal")) {
                headerRow1.createCell(8).setCellValue("Reason");
            }else {
                headerRow1.createCell(8).setCellValue("Payment Type");

            }

            int i = 9;
            double totalAmt = 0.00, totalReceived = 0.00, discountAmount = 0.00, totalSalesTaxAmount = 0.00;
            for (SalesReportListResponsePojo list : paginatedResponsePojo.getSalesReportList()) {
                totalAmt = totalAmt + list.getTotalAmount();
                totalReceived = totalReceived + list.getTotalReceivable();
                discountAmount = discountAmount + list.getDiscountAmount();
                totalSalesTaxAmount = totalSalesTaxAmount + list.getSalesTotalTaxAmt();
                HSSFRow row = sheet.createRow(++i);
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String dateFormat = df.format(list.getDate());
                row.createCell(0).setCellValue(dateFormat);
                row.createCell(1).setCellValue(list.getSQNo());
                row.createCell(2).setCellValue(list.getCustomerName());
                row.createCell(3).setCellValue(list.getTableName());
                row.createCell(4).setCellValue(list.getEmployeeName());
                row.createCell(5).setCellValue(list.getDiscountAmount());
                Double taxamt = list.getSalesTotalTaxAmt();
                row.createCell(6).setCellValue(taxamt / 2 + ":" + taxamt / 2);
                row.createCell(7).setCellValue(list.getTotalAmount());
                if(StringUtils.equals(type1,"'freemeal'")||StringUtils.equals(type1,"freemeal")) {
                    row.createCell(8).setCellValue(list.getMemo());
                }else {
                    row.createCell(8).setCellValue(list.getPaymentName());
                }
            }
            HSSFRow row = sheet.createRow(++i);
            row.createCell(0).setCellValue("Grand Total");
            row.createCell(1).setCellValue("");
            row.createCell(2).setCellValue("");
            row.createCell(3).setCellValue("");
            row.createCell(4).setCellValue("");
            row.createCell(5).setCellValue(new DecimalFormat("#,##0.00").format(discountAmount));
            row.createCell(6).setCellValue(new DecimalFormat("#,##0.00").format(totalSalesTaxAmount / 2) + ":" + new DecimalFormat("#,##0.00").format(totalSalesTaxAmount / 2));
            row.createCell(7).setCellValue(new DecimalFormat("#,##0.00").format(totalAmt));
            row.createCell(8).setCellValue("");
            hwb.write(out);
            out.close();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } catch (Exception gex) {
            gex.printStackTrace();
        }
    }
    public void downloadOnlineInvoiceExcel(OutputStream out, SalesRequestPojo salesRequestPojo,String type1,String table) {
        try {
            String fromInvoice = null;
            String toInvoice = null;

            PaginatedResponsePojo paginatedResponsePojo = new PaginatedResponsePojo();
            paginatedResponsePojo = getRestaurantInvoicepaymentList(type1,table,salesRequestPojo);
            if (salesRequestPojo.getFromSID() != null && salesRequestPojo.getToSID() != null) {
                SalesInvoice salesInvoice = salesInvoiceRepository.findAllBySIId(salesRequestPojo.getFromSID());
                fromInvoice = salesInvoice.getSINo();
                SalesInvoice salesInvoice1 = salesInvoiceRepository.findAllBySIId(salesRequestPojo.getToSID());
                toInvoice = salesInvoice1.getSINo();
            };
            HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFSheet sheet = hwb.createSheet("First Sheet");
            Row headerRow = sheet.createRow(0);
            Company company = companyRepository.findAllByStatus("Active");
            headerRow.setHeightInPoints((3 * sheet.getDefaultRowHeightInPoints()));
            headerRow.createCell(2).setCellValue(company.getCompanyName() + "\n" + company.getAddress() + "\n" + "Online Invoice Listing");
            CellRangeAddress cell = new CellRangeAddress(0, 2, 2, 4);
            sheet.addMergedRegion(cell);
            RegionUtil.setBorderTop(2, cell, sheet, hwb);
            RegionUtil.setBorderLeft(2, cell, sheet, hwb);
            RegionUtil.setBorderRight(2, cell, sheet, hwb);
            RegionUtil.setBorderBottom(2, cell, sheet, hwb);
            HSSFRow headerRow5 = sheet.createRow(3);
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = sdf.parse(salesRequestPojo.getFromDate());
            DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
            String fromDateFormat = df1.format(fromDate);
            Date toDate = sdf.parse(salesRequestPojo.getToDate());
            String toDateFormat = df1.format(toDate);
            headerRow5.createCell(0).setCellValue("From Date");
            headerRow5.createCell(1).setCellValue(fromDateFormat);
            headerRow5.createCell(2).setCellValue("To Date");
            headerRow5.createCell(3).setCellValue(toDateFormat);
            HSSFRow headerRow6 = sheet.createRow(4);
            headerRow6.createCell(0).setCellValue("From Invoice");
            headerRow6.createCell(2).setCellValue("To Invoice");
            if (!StringUtils.isEmpty(fromInvoice) && !StringUtils.isEmpty(toInvoice)) {
                headerRow6.createCell(1).setCellValue(fromInvoice);
                headerRow6.createCell(3).setCellValue(toInvoice);
            }
            HSSFRow headerRow8 = sheet.createRow(5);
            headerRow8.createCell(0).setCellValue("Customer Name");
            if (salesRequestPojo.getCustomerId() != null) {
                Customer customer=customerRepository.findAllByCustomerId(salesRequestPojo.getCustomerId());
                headerRow8.createCell(1).setCellValue(customer.getFirstName());
            }
            HSSFRow headerRow9 = sheet.createRow(6);
            headerRow9.createCell(0).setCellValue("Agent");
            if (salesRequestPojo.getAgentId() != null) {
                Agent agent=agentRepository.findAllByAgentId(salesRequestPojo.getAgentId());
                headerRow9.createCell(1).setCellValue(agent.getAgentName());
            }
            HSSFRow headerRow11 = sheet.createRow(7);
            headerRow11.createCell(0).setCellValue("Employee");
            if (salesRequestPojo.getEmployeeId() != null) {
                Employee employee= employeeRepository.findOne(salesRequestPojo.getEmployeeId());
                headerRow11.createCell(1).setCellValue(employee.getEmployeeName());
            }
            Row headerRow1 = sheet.createRow(8);
            headerRow1.createCell(0).setCellValue("Date");
            headerRow1.createCell(1).setCellValue("SINo");
            headerRow1.createCell(2).setCellValue("Customer Name");
            headerRow1.createCell(3).setCellValue("Table Name");
            headerRow1.createCell(4).setCellValue("Employee Name");
            headerRow1.createCell(5).setCellValue("Agent Name");
            headerRow1.createCell(6).setCellValue("Discount Amount");
            headerRow1.createCell(7).setCellValue("Tax Amount(CGST:SGST)");
            headerRow1.createCell(8).setCellValue("Invoice Amount");
            headerRow1.createCell(9).setCellValue("Payment Type");

            int i = 8;
            double totalAmt = 0.00, totalReceived = 0.00, discountAmount = 0.00, totalSalesTaxAmount = 0.00;
            for (SalesReportListResponsePojo list : paginatedResponsePojo.getSalesReportList()) {
                totalAmt = totalAmt + list.getTotalAmount();
                totalReceived = totalReceived + list.getTotalReceivable();
                discountAmount = discountAmount + list.getDiscountAmount();
                totalSalesTaxAmount = totalSalesTaxAmount + list.getSalesTotalTaxAmt();
                HSSFRow row = sheet.createRow(++i);
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String dateFormat = df.format(list.getDate());
                row.createCell(0).setCellValue(dateFormat);
                row.createCell(1).setCellValue(list.getSQNo());
                row.createCell(2).setCellValue(list.getCustomerName());
                row.createCell(3).setCellValue(list.getTableName());
                row.createCell(4).setCellValue(list.getEmployeeName());
                row.createCell(5).setCellValue(list.getAgentName());
                row.createCell(6).setCellValue(list.getDiscountAmount());
                Double taxamt = list.getSalesTotalTaxAmt();
                row.createCell(7).setCellValue(taxamt / 2 + ":" + taxamt / 2);
                row.createCell(8).setCellValue(list.getTotalAmount());
                row.createCell(9).setCellValue(list.getPaymentName());

            }
            HSSFRow row = sheet.createRow(++i);
            row.createCell(0).setCellValue("Grand Total");
            row.createCell(1).setCellValue("");
            row.createCell(2).setCellValue("");
            row.createCell(3).setCellValue("");
            row.createCell(4).setCellValue("");
            row.createCell(5).setCellValue("");
            row.createCell(6).setCellValue(new DecimalFormat("#,##0.00").format(discountAmount));
            row.createCell(7).setCellValue(new DecimalFormat("#,##0.00").format(totalSalesTaxAmount / 2) + ":" + new DecimalFormat("#,##0.00").format(totalSalesTaxAmount / 2));
            row.createCell(8).setCellValue(new DecimalFormat("#,##0.00").format(totalAmt));
            row.createCell(9).setCellValue("");
            hwb.write(out);
            out.close();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } catch (Exception gex) {
            gex.printStackTrace();
        }
    }

    @Transactional
    public void downloadOnlineInvoicePdf(OutputStream outputStream, SalesRequestPojo salesRequestPojo,String type1,String tableId) {

        try {
            Font font1 = new Font(getcustomfont(), 12F, java.awt.Font.BOLD);
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Chunk CONNECT = new Chunk(new LineSeparator(1, 100, Color.BLACK, Element.ALIGN_JUSTIFIED, 3f));
            document.add(CONNECT);
            document.add(new Paragraph("", font1));
            Chunk CONNECT1 = new Chunk(new LineSeparator(1, 100, Color.WHITE, Element.ALIGN_JUSTIFIED, 3f));
            document.add(CONNECT1);
            PdfPTable table = createFirstTableOnlineInvoice(salesRequestPojo,type1);
            PdfPTable table1 = createFirstTableOnlineReport(salesRequestPojo,type1,tableId);
            table.setHeaderRows(1);
            document.add(printCompanyDetails());
            document.add(table);
            document.add(table1);
            document.add(CONNECT1);
            Paragraph foot = new Paragraph();
            Phrase ph5 = new Phrase("Terms And Condition:           Prepared By                  Approved By   ", font1);
            foot.add(ph5);
            document.add(foot);
            document.add(CONNECT);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public PdfPTable createFirstTableOnlineInvoice(SalesRequestPojo salesRequestPojo,String type1)throws Exception {
        Font font1 = new Font(getcustomfont(), 8, Font.NORMAL, Color.BLACK);
        int a = 0;
        PdfPTable tbl = new PdfPTable(a + 2);
        PdfPTable tbl3 = new PdfPTable(a + 2);
        PdfPTable tbl2 = new PdfPTable(1);
        PdfPTable tble = new PdfPTable(1);
        PdfPTable tbl7 = new PdfPTable(1);
        PdfPTable tab = new PdfPTable(1);
        Font f = new Font(getcustomfont(), 15, java.awt.Font.BOLD, Color.WHITE);
        Font font = new Font(getcustomfont(), 10, Font.NORMAL, Color.WHITE);
        Color myColor = WebColors.getRGBColor("#326397");
        String name= "Online Invoice Listing";
        PdfPCell p = new PdfPCell(new Phrase(name, f));
        p.setHorizontalAlignment(Element.ALIGN_CENTER);
        p.setBackgroundColor(myColor);
        tab.addCell(p);
        PdfPCell p1 = new PdfPCell(new Phrase("From Date", font));
        p1.setBackgroundColor(myColor);
        PdfPCell p2 = new PdfPCell(new Phrase("To Date", font));
        p2.setBackgroundColor(myColor);
        tbl.addCell(p1);
        tbl.addCell(p2);
        PdfPCell pI1 = new PdfPCell(new Phrase("From Invoice", font));
        pI1.setBackgroundColor(myColor);
        PdfPCell pI2 = new PdfPCell(new Phrase("To Invoice", font));
        pI2.setBackgroundColor(myColor);
        tbl3.addCell(pI1);
        tbl3.addCell(pI2);
        PdfPCell p4 = new PdfPCell(new Phrase("Customer Name", font));
        p4.setBackgroundColor(myColor);
        tbl2.addCell(p4);
        PdfPCell p5 = new PdfPCell(new Phrase("Agent", font));
        p5.setBackgroundColor(myColor);
        tble.addCell(p5);
        PdfPCell pc6 = new PdfPCell(new Phrase("Employee Name", font));
        pc6.setBackgroundColor(myColor);
        tbl7.addCell(pc6);
        String fromInvoice = null;
        String toInvoice = null;
        if (salesRequestPojo.getFromSID() != null && salesRequestPojo.getToSID() != null) {
            SalesInvoice salesInvoice = salesInvoiceRepository.findAllBySIId(salesRequestPojo.getFromSID());
            fromInvoice = salesInvoice.getSINo();
            SalesInvoice salesInvoice1 = salesInvoiceRepository.findAllBySIId(salesRequestPojo.getToSID());
            toInvoice = salesInvoice1.getSINo();
        }
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = sdf.parse(salesRequestPojo.getFromDate());
        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
        String fromDateFormat = df1.format(fromDate);
        Date toDate = sdf.parse(salesRequestPojo.getToDate());
        String toDateFormat = df1.format(toDate);
        tbl.addCell(new Phrase(fromDateFormat + "", font1));
        tbl.addCell(new Phrase(toDateFormat + "", font1));
        if (!StringUtils.isEmpty(fromInvoice) && !StringUtils.isEmpty(toInvoice)) {
            tbl3.addCell(new Phrase(fromInvoice + "", font1));
            tbl3.addCell(new Phrase(toInvoice + "", font1));
        }

        if (salesRequestPojo.getCustomerId() != null) {
            Customer customer=customerRepository.findAllByCustomerId(salesRequestPojo.getCustomerId());
            tbl2.addCell(new Phrase(customer.getFirstName() + "", font1));
        }
        if (salesRequestPojo.getAgentId() != null) {
            Agent agent=agentRepository.findAllByAgentId(salesRequestPojo.getAgentId());
            tble.addCell(new Phrase(agent.getAgentName() + "", font1));
        }
        if (salesRequestPojo.getEmployeeId() != null) {
            Employee employee = employeeRepository.findOne(salesRequestPojo.getEmployeeId());
                tbl7.addCell(new Phrase(employee.getEmployeeName() + "", font1));
        }
        tab.addCell(tbl);
        tab.addCell(tbl3);
        tab.addCell(tbl2);
        tab.addCell(tble);
        tab.addCell(tbl7);
        return tab;
    }

    public PdfPTable createFirstTableOnlineReport(SalesRequestPojo salesRequestPojo,String type1,String tableId) {
        Font font1 = new Font(getcustomfont(), 8, Font.NORMAL, Color.BLACK);
        int a = 0;
        Font f1 = new Font(getcustomfont(), 8, Font.BOLD, Color.BLACK);
        PdfPTable table = new PdfPTable(a + 10);
        Font font = new Font(getcustomfont(), 10,Font.NORMAL, Color.WHITE);
        Color myColor = WebColors.getRGBColor("#326397");
        PdfPCell p5 = new PdfPCell(new Phrase("Date", font));
        p5.setBackgroundColor(myColor);
        PdfPCell p6 = new PdfPCell(new Phrase("SQNo", font));
        p6.setBackgroundColor(myColor);
        PdfPCell p7 = new PdfPCell(new Phrase("Customer Name", font));
        p7.setBackgroundColor(myColor);
        PdfPCell p8 = new PdfPCell(new Phrase("Table Name", font));
        p8.setBackgroundColor(myColor);
        PdfPCell p9 = new PdfPCell(new Phrase("Waiter Name", font));
        p9.setBackgroundColor(myColor);
        PdfPCell p14 = new PdfPCell(new Phrase("Agent Name", font));
        p14.setBackgroundColor(myColor);
        PdfPCell p10 = new PdfPCell(new Phrase("Discount Amount", font));
        p10.setBackgroundColor(myColor);
        PdfPCell p11 = new PdfPCell(new Phrase("Tax Amount(CSGT/SGST)", font));
        p11.setBackgroundColor(myColor);
        PdfPCell p12 = new PdfPCell(new Phrase("Invoice Amount", font));
        p12.setBackgroundColor(myColor);
        PdfPCell p13 = new PdfPCell(new Phrase("Payment Type", font));
        p13.setBackgroundColor(myColor);
        table.addCell(p5);
        table.addCell(p6);
        table.addCell(p7);
        table.addCell(p8);
        table.addCell(p9);
        table.addCell(p14);
        table.addCell(p10);
        table.addCell(p11);
        table.addCell(p12);
        table.addCell(p13);
        PaginatedResponsePojo paginatedResponsePojo = new PaginatedResponsePojo();
        paginatedResponsePojo = getRestaurantInvoicepaymentList(type1,tableId,salesRequestPojo);

        double totalAmt = 0.00, totalReceived = 0.00, discountAmount = 0.00, totalSalesTaxAmount = 0.00, taxAmt = 0.00;
        for (SalesReportListResponsePojo list : paginatedResponsePojo.getSalesReportList()) {
            totalAmt = totalAmt + list.getTotalAmount();
            totalReceived = totalReceived + list.getTotalReceivable();
            discountAmount = discountAmount + list.getDiscountAmount();
            totalSalesTaxAmount = totalSalesTaxAmount + list.getSalesTotalTaxAmt();
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String dateFormat = df.format(list.getDate());
            table.addCell(new Phrase(dateFormat + "", font1));
            table.addCell(new Phrase(list.getSQNo() + "", font1));
            table.addCell(new Phrase(list.getCustomerName() + "", font1));
            table.addCell(new Phrase(list.getTableName() + "", font1));
            table.addCell(new Phrase(list.getEmployeeName() + "", font1));
            table.addCell(new Phrase(list.getAgentName() + "", font1));
            table.addCell(new Phrase(list.getDiscountAmount() + "", font1));
            table.addCell(new Phrase(list.getSalesTotalTaxAmt() / 2 + ":" + list.getSalesTotalTaxAmt() / 2 + "", font1));
            table.addCell(new Phrase(list.getTotalAmount() + "", font1));
            table.addCell(new Phrase(list.getPaymentName() + "", font1));
        }
        table.addCell(new Phrase("Grand Total"));
        table.addCell(new Phrase(" "));
        table.addCell(new Phrase(" "));
        table.addCell(new Phrase(" "));
        table.addCell(new Phrase(" "));
        table.addCell(new Phrase(" "));
        table.addCell(new Phrase(new DecimalFormat("#,##0.00").format(discountAmount)));
        table.addCell(new Phrase(new DecimalFormat("#,##0.00").format(totalSalesTaxAmount / 2) + ":" + new DecimalFormat("#,##0.00").format(totalSalesTaxAmount / 2)));
        table.addCell(new Phrase(new DecimalFormat("#,##0.00").format(totalAmt)));
        table.addCell(new Phrase(""));
        return table;
    }
    @Transactional
    public void downloadWaiterReportExcel(OutputStream out, SalesRequestPojo salesRequestPojo,String type1,String table) {
        try {
            String name = null;
            String fromInvoice = null;
            String toInvoice = null;

            PaginatedResponsePojo paginatedResponsePojo = new PaginatedResponsePojo();
            paginatedResponsePojo = getRestaurantInvoicepaymentList(type1,table,salesRequestPojo);
            if (salesRequestPojo.getFromSID() != null && salesRequestPojo.getToSID() != null) {
                SalesInvoice salesInvoice = salesInvoiceRepository.findAllBySIId(salesRequestPojo.getFromSID());
                fromInvoice = salesInvoice.getSINo();
                SalesInvoice salesInvoice1 = salesInvoiceRepository.findAllBySIId(salesRequestPojo.getToSID());
                toInvoice = salesInvoice1.getSINo();
            };
            HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFSheet sheet = hwb.createSheet("First Sheet");
            String name1="Waiter Report Listing";
            Row headerRow = sheet.createRow(0);
            Company company = companyRepository.findAllByStatus("Active");
            headerRow.setHeightInPoints((3 * sheet.getDefaultRowHeightInPoints()));
            headerRow.createCell(2).setCellValue(company.getCompanyName() + "\n" + company.getAddress() + "\n" + name1);
            CellRangeAddress cell = new CellRangeAddress(0, 2, 2, 4);
            sheet.addMergedRegion(cell);
            RegionUtil.setBorderTop(2, cell, sheet, hwb);
            RegionUtil.setBorderLeft(2, cell, sheet, hwb);
            RegionUtil.setBorderRight(2, cell, sheet, hwb);
            RegionUtil.setBorderBottom(2, cell, sheet, hwb);
            HSSFRow headerRow5 = sheet.createRow(3);
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = sdf.parse(salesRequestPojo.getFromDate());
            DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
            String fromDateFormat = df1.format(fromDate);
            Date toDate = sdf.parse(salesRequestPojo.getToDate());
            String toDateFormat = df1.format(toDate);
            headerRow5.createCell(0).setCellValue("From Date");
            headerRow5.createCell(1).setCellValue(fromDateFormat);
            headerRow5.createCell(2).setCellValue("To Date");
            headerRow5.createCell(3).setCellValue(toDateFormat);
            HSSFRow headerRow6 = sheet.createRow(4);
            headerRow6.createCell(0).setCellValue("From Invoice");
            headerRow6.createCell(2).setCellValue("To Invoice");
            if (!StringUtils.isEmpty(fromInvoice) && !StringUtils.isEmpty(toInvoice)) {
                headerRow6.createCell(1).setCellValue(fromInvoice);
                headerRow6.createCell(3).setCellValue(toInvoice);
            }
            HSSFRow headerRow8 = sheet.createRow(5);
            headerRow8.createCell(0).setCellValue("Customer Name");
            if (salesRequestPojo.getCustomerId() != null) {
                Customer customer=customerRepository.findAllByCustomerId(salesRequestPojo.getCustomerId());
                headerRow8.createCell(1).setCellValue(customer.getFirstName());
            }
            HSSFRow headerRow11 = sheet.createRow(6);
            headerRow11.createCell(0).setCellValue("Employee");
            if (salesRequestPojo.getEmployeeId() != null) {
                Employee employee= employeeRepository.findOne(salesRequestPojo.getEmployeeId());
                headerRow11.createCell(1).setCellValue(employee.getEmployeeName());
            }
            HSSFRow headerRow12 = sheet.createRow(7);
            headerRow12.createCell(0).setCellValue("Order Type");
            if (salesRequestPojo.getInvoiceType() != null) {
                headerRow12.createCell(1).setCellValue(salesRequestPojo.getInvoiceType());
            }
            Row headerRow1 = sheet.createRow(8);
            headerRow1.createCell(0).setCellValue("Date");
            headerRow1.createCell(1).setCellValue("SINo");
            headerRow1.createCell(2).setCellValue("Customer Name");
            headerRow1.createCell(3).setCellValue("Table Name");
            headerRow1.createCell(4).setCellValue("Employee Name");
            headerRow1.createCell(5).setCellValue("Discount Amount");
            headerRow1.createCell(6).setCellValue("Tax Amount(CGST:SGST)");
            headerRow1.createCell(7).setCellValue("Invoice Amount");
            headerRow1.createCell(8).setCellValue("Incentives");
            headerRow1.createCell(9).setCellValue("Incentives Amount");
            int i = 8;
            double totalAmt = 0.00, totalReceived = 0.00, discountAmount = 0.00, totalSalesTaxAmount = 0.00,incentives=0.00,incentivesAmount=0.00;
            for (SalesReportListResponsePojo list : paginatedResponsePojo.getSalesReportList()) {
                totalAmt = totalAmt + list.getTotalAmount();
                totalReceived = totalReceived + list.getTotalReceivable();
                discountAmount = discountAmount + list.getDiscountAmount();
                totalSalesTaxAmount = totalSalesTaxAmount + list.getSalesTotalTaxAmt();
                incentives = incentives + list.getIncentives();
                incentivesAmount = incentivesAmount + list.getIncentivesAmount();
                HSSFRow row = sheet.createRow(++i);
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String dateFormat = df.format(list.getDate());
                row.createCell(0).setCellValue(dateFormat);
                row.createCell(1).setCellValue(list.getSQNo());
                row.createCell(2).setCellValue(list.getCustomerName());
                row.createCell(3).setCellValue(list.getTableName());
                row.createCell(4).setCellValue(list.getEmployeeName());
                row.createCell(5).setCellValue(list.getDiscountAmount());
                Double taxamt = list.getSalesTotalTaxAmt();
                row.createCell(6).setCellValue(taxamt / 2 + ":" + taxamt / 2);
                row.createCell(7).setCellValue(list.getTotalAmount());
                row.createCell(8).setCellValue(list.getIncentives());
                row.createCell(9).setCellValue(list.getIncentivesAmount());
            }
            HSSFRow row = sheet.createRow(++i);
            row.createCell(0).setCellValue("Grand Total");
            row.createCell(1).setCellValue("");
            row.createCell(2).setCellValue("");
            row.createCell(3).setCellValue("");
            row.createCell(4).setCellValue("");
            row.createCell(5).setCellValue(new DecimalFormat("#,##0.00").format(discountAmount));
            row.createCell(6).setCellValue(new DecimalFormat("#,##0.00").format(totalSalesTaxAmount / 2) + ":" + new DecimalFormat("#,##0.00").format(totalSalesTaxAmount / 2));
            row.createCell(7).setCellValue(new DecimalFormat("#,##0.00").format(totalAmt));
            row.createCell(8).setCellValue(new DecimalFormat("#,##0.00").format(incentives));
            row.createCell(9).setCellValue(new DecimalFormat("#,##0.00").format(incentivesAmount));
            hwb.write(out);
            out.close();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } catch (Exception gex) {
            gex.printStackTrace();
        }
    }
    @Transactional
    public void downloadRestaurantInvoicePdf(OutputStream outputStream, SalesRequestPojo salesRequestPojo,String type1,String tableId) {

        try {
            Font font1 = new Font(getcustomfont(), 12F, java.awt.Font.BOLD);
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Chunk CONNECT = new Chunk(new LineSeparator(1, 100, Color.BLACK, Element.ALIGN_JUSTIFIED, 3f));
            document.add(CONNECT);
            document.add(new Paragraph("", font1));
            Chunk CONNECT1 = new Chunk(new LineSeparator(1, 100, Color.WHITE, Element.ALIGN_JUSTIFIED, 3f));
            document.add(CONNECT1);
            PdfPTable table = createFirstTableRestaurantInvoice(salesRequestPojo,type1);
            PdfPTable table1 = createFirstTableRestaurantReport(salesRequestPojo,type1,tableId);
            table.setHeaderRows(1);
            document.add(printCompanyDetails());
            document.add(table);
            document.add(table1);
            document.add(CONNECT1);
            Paragraph foot = new Paragraph();
            Phrase ph5 = new Phrase("Terms And Condition:           Prepared By                  Approved By   ", font1);
            foot.add(ph5);
            document.add(foot);
            document.add(CONNECT);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Transactional
    public void downloadWaiterReportPdf(OutputStream outputStream, SalesRequestPojo salesRequestPojo,String type1,String tableId) {

        try {
            Font font1 = new Font(getcustomfont(), 12F, java.awt.Font.BOLD);
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Chunk CONNECT = new Chunk(new LineSeparator(1, 100, Color.BLACK, Element.ALIGN_JUSTIFIED, 3f));
            document.add(CONNECT);
            document.add(new Paragraph("", font1));
            Chunk CONNECT1 = new Chunk(new LineSeparator(1, 100, Color.WHITE, Element.ALIGN_JUSTIFIED, 3f));
            document.add(CONNECT1);
            PdfPTable table = createFirstTableRestaurantInvoice(salesRequestPojo,type1);
            PdfPTable table1 = createFirstTableWaiterReport(salesRequestPojo,type1,tableId);
            table.setHeaderRows(1);
            document.add(printCompanyDetails());
            document.add(table);
            document.add(table1);
            document.add(CONNECT1);
            Paragraph foot = new Paragraph();
            Phrase ph5 = new Phrase("Terms And Condition:           Prepared By                  Approved By   ", font1);
            foot.add(ph5);
            document.add(foot);
            document.add(CONNECT);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public PdfPTable createFirstTableRestaurantInvoice(SalesRequestPojo salesRequestPojo,String type1)throws Exception {
        Font font1 = new Font(getcustomfont(), 8, Font.NORMAL, Color.BLACK);
        int a = 0;
        PdfPTable tbl = new PdfPTable(a + 2);
        PdfPTable tbl3 = new PdfPTable(a + 2);
        PdfPTable tbl2 = new PdfPTable(1);
        PdfPTable tble = new PdfPTable(1);
        PdfPTable tbl7 = new PdfPTable(1);
        PdfPTable tbl8 = new PdfPTable(1);
        PdfPTable tbl9 = new PdfPTable(1);
        PdfPTable tab = new PdfPTable(1);
        Font f = new Font(getcustomfont(), 15, java.awt.Font.BOLD, Color.WHITE);
        Font font = new Font(getcustomfont(), 10, Font.NORMAL, Color.WHITE);
        Color myColor = WebColors.getRGBColor("#326397");
        String name= "Restaurant Invoice Listing";
        if(StringUtils.equals(type1,"'discount'")||StringUtils.equals(type1,"discount")){
            name ="Discount Report";
        }
        if(StringUtils.equals(type1,"'freemeal'")||StringUtils.equals(type1,"freemeal")){
            name ="Free Meal Report";
        }
        if(StringUtils.equals(type1,"'tableWise'")||StringUtils.equals(type1,"tableWise")){
            name ="Table Wise Report";
        }
        PdfPCell p = new PdfPCell(new Phrase(name, f));
        p.setHorizontalAlignment(Element.ALIGN_CENTER);
        p.setBackgroundColor(myColor);
        tab.addCell(p);
        PdfPCell p1 = new PdfPCell(new Phrase("From Date", font));
        p1.setBackgroundColor(myColor);
        PdfPCell p2 = new PdfPCell(new Phrase("To Date", font));
        p2.setBackgroundColor(myColor);
        tbl.addCell(p1);
        tbl.addCell(p2);
        PdfPCell pI1 = new PdfPCell(new Phrase("From Invoice", font));
        pI1.setBackgroundColor(myColor);
        PdfPCell pI2 = new PdfPCell(new Phrase("To Invoice", font));
        pI2.setBackgroundColor(myColor);
        tbl3.addCell(pI1);
        tbl3.addCell(pI2);
        PdfPCell p4 = new PdfPCell(new Phrase("Customer Name", font));
        p4.setBackgroundColor(myColor);
        tbl2.addCell(p4);
//        PdfPCell p5 = new PdfPCell(new Phrase("Location", font));
//        p5.setBackgroundColor(myColor);
//        tble.addCell(p5);
        PdfPCell pc6 = new PdfPCell(new Phrase("Employee Name", font));
        pc6.setBackgroundColor(myColor);
        tbl7.addCell(pc6);
        PdfPCell pc7 = new PdfPCell(new Phrase("Payment", font));
        pc7.setBackgroundColor(myColor);
        tbl8.addCell(pc7);
        PdfPCell pc8 = new PdfPCell(new Phrase("OrderType", font));
        pc8.setBackgroundColor(myColor);
        tbl9.addCell(pc8);
        String fromInvoice = null;
        String toInvoice = null;
        if (salesRequestPojo.getFromSID() != null && salesRequestPojo.getToSID() != null) {
            SalesInvoice salesInvoice = salesInvoiceRepository.findAllBySIId(salesRequestPojo.getFromSID());
//            SalesInvoice salesInvoice = salesInvoiceRepository.(salesRequestPojo.getFromSINo());
            fromInvoice = salesInvoice.getSINo();
            SalesInvoice salesInvoice1 = salesInvoiceRepository.findAllBySIId(salesRequestPojo.getToSID());
            toInvoice = salesInvoice1.getSINo();
        }
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = sdf.parse(salesRequestPojo.getFromDate());
        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
        String fromDateFormat = df1.format(fromDate);
        Date toDate = sdf.parse(salesRequestPojo.getToDate());
        String toDateFormat = df1.format(toDate);
        tbl.addCell(new Phrase(fromDateFormat + "", font1));
        tbl.addCell(new Phrase(toDateFormat + "", font1));
        if (!StringUtils.isEmpty(fromInvoice) && !StringUtils.isEmpty(toInvoice)) {
            tbl3.addCell(new Phrase(fromInvoice + "", font1));
            tbl3.addCell(new Phrase(toInvoice + "", font1));
        }

        if (salesRequestPojo.getCustomerId() != null) {
            Customer customer=customerRepository.findAllByCustomerId(salesRequestPojo.getCustomerId());
            tbl2.addCell(new Phrase(customer.getFirstName() + "", font1));
        }
        if (salesRequestPojo.getEmployeeId() != null) {
            Employee emp = employeeRepository.findOne(salesRequestPojo.getEmployeeId());
            tbl7.addCell(new Phrase(emp.getEmployeeName() + "", font1));
        }
        if (salesRequestPojo.getPaymentId() != null) {
            PaymentMethod paymentMethod=paymentMethodRepository.findAllByPaymentmethodId(salesRequestPojo.getPaymentId());
            tbl8.addCell(new Phrase(paymentMethod.getPaymentmethodName() + "", font1));
        }
        if (!StringUtils.isEmpty(salesRequestPojo.getInvoiceType())) {
            tbl9.addCell(new Phrase(salesRequestPojo.getInvoiceType() + "", font1));
        }
        tab.addCell(tbl);
        tab.addCell(tbl3);
        tab.addCell(tbl2);
        tab.addCell(tble);
        tab.addCell(tbl7);
        tab.addCell(tbl8);
        tab.addCell(tbl9);
        return tab;
    }

    public PdfPTable createFirstTableRestaurantReport(SalesRequestPojo salesRequestPojo,String type1,String tableId) {
        Font font1 = new Font(getcustomfont(), 8, Font.NORMAL, Color.BLACK);
        int a = 0;
        Font f1 = new Font(getcustomfont(), 8, Font.BOLD, Color.BLACK);
        PdfPTable table = new PdfPTable(a + 9);
        Font font = new Font(getcustomfont(), 10,Font.NORMAL, Color.WHITE);
        Color myColor = WebColors.getRGBColor("#326397");
        PdfPCell p5 = new PdfPCell(new Phrase("Date", font));
        p5.setBackgroundColor(myColor);
        PdfPCell p6 = new PdfPCell(new Phrase("SQNo", font));
        p6.setBackgroundColor(myColor);
        PdfPCell p7 = new PdfPCell(new Phrase("Customer Name", font));
        p7.setBackgroundColor(myColor);
        PdfPCell p8 = new PdfPCell(new Phrase("Table Name", font));
        p8.setBackgroundColor(myColor);
        PdfPCell p9 = new PdfPCell(new Phrase("Waiter Name", font));
        p9.setBackgroundColor(myColor);

        PdfPCell p10 = new PdfPCell(new Phrase("Discount Amount", font));
        p10.setBackgroundColor(myColor);
        PdfPCell p11 = new PdfPCell(new Phrase("Tax Amount(CSGT/SGST)", font));
        p11.setBackgroundColor(myColor);
        PdfPCell p12 = new PdfPCell(new Phrase("Invoice Amount", font));
        p12.setBackgroundColor(myColor);
        PdfPCell p13 = new PdfPCell(new Phrase("Payment Type", font));
        p13.setBackgroundColor(myColor);
        table.addCell(p5);
        table.addCell(p6);
        table.addCell(p7);
        table.addCell(p8);
        table.addCell(p9);
        table.addCell(p10);
        table.addCell(p11);
        table.addCell(p12);
        table.addCell(p13);
        PaginatedResponsePojo paginatedResponsePojo = new PaginatedResponsePojo();
        paginatedResponsePojo = getRestaurantInvoicepaymentList(type1,tableId,salesRequestPojo);

        double totalAmt = 0.00, totalReceived = 0.00, discountAmount = 0.00, totalSalesTaxAmount = 0.00, taxAmt = 0.00;
        for (SalesReportListResponsePojo list : paginatedResponsePojo.getSalesReportList()) {
            totalAmt = totalAmt + list.getTotalAmount();
            totalReceived = totalReceived + list.getTotalReceivable();
            discountAmount = discountAmount + list.getDiscountAmount();
            totalSalesTaxAmount = totalSalesTaxAmount + list.getSalesTotalTaxAmt();
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String dateFormat = df.format(list.getDate());
            table.addCell(new Phrase(dateFormat + "", font1));
            table.addCell(new Phrase(list.getSQNo() + "", font1));
            table.addCell(new Phrase(list.getCustomerName() + "", font1));
            table.addCell(new Phrase(list.getTableName() + "", font1));
            table.addCell(new Phrase(list.getEmployeeName() + "", font1));
            table.addCell(new Phrase(list.getDiscountAmount() + "", font1));
            table.addCell(new Phrase(list.getSalesTotalTaxAmt() / 2 + ":" + list.getSalesTotalTaxAmt() / 2 + "", font1));
            table.addCell(new Phrase(list.getTotalAmount() + "", font1));
            if(!StringUtils.isEmpty(list.getPaymentName())) {
                table.addCell(new Phrase(list.getPaymentName() + "", font1));
            }else {
                table.addCell(new Phrase(""));
            }
        }
        table.addCell(new Phrase("Grand Total"));
        table.addCell(new Phrase(" "));
        table.addCell(new Phrase(" "));
        table.addCell(new Phrase(" "));
        table.addCell(new Phrase(" "));
        table.addCell(new Phrase(new DecimalFormat("#,##0.00").format(discountAmount)));
        table.addCell(new Phrase(new DecimalFormat("#,##0.00").format(totalSalesTaxAmount / 2) + ":" + new DecimalFormat("#,##0.00").format(totalSalesTaxAmount / 2)));
        table.addCell(new Phrase(new DecimalFormat("#,##0.00").format(totalAmt)));
        table.addCell(new Phrase(""));
        return table;
    }
    public PdfPTable createFirstTableWaiterReport(SalesRequestPojo salesRequestPojo,String type1,String tableId) {
        Font font1 = new Font(getcustomfont(), 8, Font.NORMAL, Color.BLACK);
        int a = 0;
        Font f1 = new Font(getcustomfont(), 8, Font.BOLD, Color.BLACK);
        PdfPTable table = new PdfPTable(a + 10);
        Font font = new Font(getcustomfont(), 10,Font.NORMAL, Color.WHITE);
        Color myColor = WebColors.getRGBColor("#326397");
        PdfPCell p5 = new PdfPCell(new Phrase("Date", font));
        p5.setBackgroundColor(myColor);
        PdfPCell p6 = new PdfPCell(new Phrase("SQNo", font));
        p6.setBackgroundColor(myColor);
        PdfPCell p7 = new PdfPCell(new Phrase("Customer Name", font));
        p7.setBackgroundColor(myColor);
        PdfPCell p8 = new PdfPCell(new Phrase("Table Name", font));
        p8.setBackgroundColor(myColor);
        PdfPCell p9 = new PdfPCell(new Phrase("Waiter Name", font));
        p9.setBackgroundColor(myColor);

        PdfPCell p10 = new PdfPCell(new Phrase("Discount Amount", font));
        p10.setBackgroundColor(myColor);
        PdfPCell p11 = new PdfPCell(new Phrase("Tax Amount(CSGT/SGST)", font));
        p11.setBackgroundColor(myColor);
        PdfPCell p12 = new PdfPCell(new Phrase("Invoice Amount", font));
        p12.setBackgroundColor(myColor);
        PdfPCell p13 = new PdfPCell(new Phrase("Incentives", font));
        p13.setBackgroundColor(myColor);
        PdfPCell p14 = new PdfPCell(new Phrase("Incentives Amount", font));
        p14.setBackgroundColor(myColor);
        table.addCell(p5);
        table.addCell(p6);
        table.addCell(p7);
        table.addCell(p8);
        table.addCell(p9);
        table.addCell(p10);
        table.addCell(p11);
        table.addCell(p12);
        table.addCell(p13);
        table.addCell(p14);
        PaginatedResponsePojo paginatedResponsePojo = new PaginatedResponsePojo();
        paginatedResponsePojo = getRestaurantInvoicepaymentList(type1,tableId,salesRequestPojo);

        double totalAmt = 0.00, totalReceived = 0.00, discountAmount = 0.00, totalSalesTaxAmount = 0.00, taxAmt = 0.00,incentivesAmount=0.00,incentives=0.00;
        for (SalesReportListResponsePojo list : paginatedResponsePojo.getSalesReportList()) {
            totalAmt = totalAmt + list.getTotalAmount();
            totalReceived = totalReceived + list.getTotalReceivable();
            discountAmount = discountAmount + list.getDiscountAmount();
            totalSalesTaxAmount = totalSalesTaxAmount + list.getSalesTotalTaxAmt();
            incentives = incentives + list.getIncentives();
            incentivesAmount = incentivesAmount + list.getIncentivesAmount();
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String dateFormat = df.format(list.getDate());
            table.addCell(new Phrase(dateFormat + "", font1));
            table.addCell(new Phrase(list.getSQNo() + "", font1));
            table.addCell(new Phrase(list.getCustomerName() + "", font1));
            table.addCell(new Phrase(list.getTableName() + "", font1));
            table.addCell(new Phrase(list.getEmployeeName() + "", font1));
            table.addCell(new Phrase(list.getDiscountAmount() + "", font1));
            table.addCell(new Phrase(list.getSalesTotalTaxAmt() / 2 + ":" + list.getSalesTotalTaxAmt() / 2 + "", font1));
            table.addCell(new Phrase(list.getTotalAmount() + "", font1));
            table.addCell(new Phrase(list.getIncentives() + "", font1));
            table.addCell(new Phrase(list.getIncentivesAmount() + "", font1));
        }
        table.addCell(new Phrase("Grand Total"));
        table.addCell(new Phrase(" "));
        table.addCell(new Phrase(" "));
        table.addCell(new Phrase(" "));
        table.addCell(new Phrase(" "));
        table.addCell(new Phrase(new DecimalFormat("#,##0.00").format(discountAmount)));
        table.addCell(new Phrase(new DecimalFormat("#,##0.00").format(totalSalesTaxAmount / 2) + ":" + new DecimalFormat("#,##0.00").format(totalSalesTaxAmount / 2)));
        table.addCell(new Phrase(new DecimalFormat("#,##0.00").format(totalAmt)));
        table.addCell(new Phrase(new DecimalFormat("#,##0.00").format(incentives)));
        table.addCell(new Phrase(new DecimalFormat("#,##0.00").format(incentivesAmount)));
        return table;
    }
    @Transactional
    public void downloadDayendListExcel(OutputStream out,BasePojo basePojo) {
        try {
            PaginatedResponsePojo paginatedResponsePojo = new PaginatedResponsePojo();
            paginatedResponsePojo = getDayEndList(basePojo);
            XSSFWorkbook hwb = new XSSFWorkbook();
            XSSFSheet sheet = hwb.createSheet("First Sheet");
            Row headerRow = sheet.createRow(0);
            Company company = companyRepository.findAllByStatus("Active");
            headerRow.setHeightInPoints((3 * sheet.getDefaultRowHeightInPoints()));
            headerRow.createCell(2).setCellValue(company.getCompanyName()+"\n" + "DayEnd Report");
            CellRangeAddress cell = new CellRangeAddress(0, 2, 2, 4);
            sheet.addMergedRegion(cell);
            RegionUtil.setBorderTop(2, cell, sheet, hwb);
            RegionUtil.setBorderLeft(2, cell, sheet, hwb);
            RegionUtil.setBorderRight(2, cell, sheet, hwb);
            RegionUtil.setBorderBottom(2, cell, sheet, hwb);
            Row headerRow5=sheet.createRow(3);
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = sdf.parse(basePojo.getFromDates());
            Date toDate = sdf.parse(basePojo.getToDate());
            headerRow5.createCell(0).setCellValue("From Date");
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String dateFormat = df.format(fromDate);
            headerRow5.createCell(1).setCellValue(dateFormat);
            headerRow5.createCell(2).setCellValue("To Date");
            DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
            String toDateFormat = df1.format(toDate);
            headerRow5.createCell(3).setCellValue(toDateFormat);
            Row headerRow1 = sheet.createRow(4);
            headerRow1.createCell(0).setCellValue("Date");
            headerRow1.createCell(1).setCellValue("Total Amount");
            headerRow1.createCell(2).setCellValue("No of Items");
            int i = 4;
            for (DayEndPojo list : paginatedResponsePojo.getDayEndPojos()) {
                Row row = sheet.createRow(++i);
                Date date = list.getDate();
                DateFormat df12 = new SimpleDateFormat("dd/MM/yyyy");
                String recDate = df12.format(date);
                row.createCell(0).setCellValue(recDate);
                row.createCell(1).setCellValue(list.getAmount());
                row.createCell(2).setCellValue(list.getTotalItems());
            }
            hwb.write(out);
            out.close();
        } catch (Exception gex) {
            gex.printStackTrace();
        }
    }
    @Transactional
    public void downloadDayEndPdf(OutputStream outputStream, BasePojo basePojo) {
        try {
            Font font1 = new Font(getcustomfont(), 12F, Font.BOLD);
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Chunk CONNECT = new Chunk(new LineSeparator(1, 100, Color.BLACK, Element.ALIGN_JUSTIFIED, 3f));
            document.add(CONNECT);
            document.add(new Paragraph("", font1));
            Chunk CONNECT1 = new Chunk(new LineSeparator(1, 100, Color.WHITE, Element.ALIGN_JUSTIFIED, 3f));
            document.add(CONNECT1);
            PdfPTable table = createFirstTableDayEndList( basePojo);
            table.setHeaderRows(1);
            document.add(printCompanyDetails());
            document.add(table);
            document.add(CONNECT1);
            Paragraph foot = new Paragraph();
            Phrase ph5 = new Phrase("Terms And Condition:Terms                    Prepared By                                        Approved By   ", font1);
            foot.add(ph5);
            document.add(foot);
            document.add(CONNECT);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    public PdfPTable createFirstTableDayEndList( BasePojo basePojo) throws Exception {
        Font font1 = new Font(getcustomfont(), 8, Font.NORMAL, Color.BLACK);
        int a = 0;
        PdfPTable tab = new PdfPTable(1);
        PdfPTable tbl = new PdfPTable(a + 2);
        Font f = new Font(getcustomfont(), 15, Font.BOLD, Color.WHITE);
        PdfPTable tab1 = new PdfPTable(a + 3);
        Font font = new Font(getcustomfont(), 10, Font.NORMAL, Color.WHITE);
        Color myColor = WebColors.getRGBColor("#326397");
        PdfPCell p = new PdfPCell(new Phrase("DayEnd List", f));
        p.setHorizontalAlignment(Element.ALIGN_CENTER);
        p.setBackgroundColor(myColor);
        tab.addCell(p);
        PdfPCell p1 = new PdfPCell(new Phrase("From Date", font));
        p1.setBackgroundColor(myColor);
        PdfPCell p2 = new PdfPCell(new Phrase("To Date",font));
        p2.setBackgroundColor(myColor);
        tbl.addCell(p1);
        tbl.addCell(p2);
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date fromDate = sdf.parse(basePojo.getFromDates());
        Date toDate = sdf.parse(basePojo.getToDate());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String dateFormat = df.format(fromDate);
        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
        String dateFormat1 = df1.format(fromDate);
        tbl.addCell(new Phrase(dateFormat + "", font1));
        tbl.addCell(new Phrase(dateFormat1 + "", font1));
        PdfPCell pc2 = new PdfPCell(new Phrase("Date", font));
        pc2.setBackgroundColor(myColor);
        PdfPCell pc3 = new PdfPCell(new Phrase("Total Amount", font));
        pc3.setBackgroundColor(myColor);
        PdfPCell pc4 = new PdfPCell(new Phrase("No of Items", font));
        pc4.setBackgroundColor(myColor);
        tab1.addCell(pc2);
        tab1.addCell(pc3);
        tab1.addCell(pc4);
        PaginatedResponsePojo paginatedResponsePojo = new PaginatedResponsePojo();
        paginatedResponsePojo = getDayEndList( basePojo);
        tab1.setWidthPercentage(100);
        for (DayEndPojo list : paginatedResponsePojo.getDayEndPojos()) {
            Date date = list.getDate();
            DateFormat df12 = new SimpleDateFormat("dd/MM/yyyy");
            String recDate = df12.format(date);
            tab1.addCell(new Phrase(recDate + "", font1));
            tab1.addCell(new Phrase(list.getAmount() + "", font1));
            tab1.addCell(new Phrase(list.getTotalItems() + "", font1));
        }
        tab.addCell(tbl);
        tab.addCell(tab1);
        return tab;
    }
    public Paragraph printCompanyDetails() {
        Company company = companyRepository.findAllByStatus("Active");
        Paragraph preface = new Paragraph(company.getCompanyName() + "\n" + company.getAddress());
        Paragraph preface2 = new Paragraph(" ");
        preface.setAlignment(Element.ALIGN_CENTER);
        preface2.setAlignment(" ");
        preface.add("\n");
        preface.add(preface2);
        return preface;
    }
    @Transactional
    public void downloadDaynedReportExcelSheet(OutputStream out, String date) {
        try {
            Map<String, Object> map = reportForPeriodListReport(date.toString());
            HSSFWorkbook hwb = new HSSFWorkbook();
            HSSFSheet sheet = hwb.createSheet("First Sheet");
            HSSFRow headerRow = sheet.createRow(0);
            Company company = companyRepository.findAllByStatus("Active");
            headerRow.setHeightInPoints((3 * sheet.getDefaultRowHeightInPoints()));
            headerRow.createCell(2).setCellValue(company.getCompanyName() + "\n"+company.getAddress()+"\n"+company.getGstNo() + "DayEnd Report");
            CellRangeAddress cell = new CellRangeAddress(0, 2, 2, 4);
            sheet.addMergedRegion(cell);
            RegionUtil.setBorderTop(2, cell, sheet, hwb);
            RegionUtil.setBorderLeft(2, cell, sheet, hwb);
            RegionUtil.setBorderRight(2, cell, sheet, hwb);
            RegionUtil.setBorderBottom(2, cell, sheet, hwb);
            HSSFRow headerRow1 = sheet.createRow(3);
            headerRow1.createCell(0).setCellValue("Date");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(date));
            headerRow1.createCell(1).setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime()));
            HSSFRow headerRow3 = sheet.createRow(5);
            headerRow3.createCell(0).setCellValue("Item Name");
            headerRow3.createCell(1).setCellValue("Qty");
            headerRow3.createCell(2).setCellValue("Amount");
            int i = 5;
            for (Map.Entry<String, Object> m : map.entrySet()) {
                if (!StringUtils.equalsIgnoreCase(m.getKey(), "Payment") && !StringUtils.equalsIgnoreCase(m.getKey(), "Service Charge") && !StringUtils.equalsIgnoreCase(m.getKey(), "Item Count") && !StringUtils.equalsIgnoreCase(m.getKey(), "Total Amount") && !StringUtils.equalsIgnoreCase(m.getKey(), "Rounding Off") && !StringUtils.equalsIgnoreCase(m.getKey(), "Discount")) {
                    HSSFRow row = sheet.createRow(++i);
                    row.createCell(0).setCellValue(m.getKey());
                    for (Map list : (List<Map>) m.getValue()) {
                        row = sheet.createRow(++i);
                        row.createCell(0).setCellValue(list.get("itemName").toString());
                        row.createCell(1).setCellValue(list.get("QtySold").toString());
                        if (list.get("totalAmtReceived") != null) {
                            row.createCell(2).setCellValue(list.get("totalAmtReceived").toString());
                        } else {
                            row.createCell(2).setCellValue(list.get("total").toString());
                        }
                    }
                }
            }
            for (Map.Entry<String, Object> m : map.entrySet()) {
                if (StringUtils.equalsIgnoreCase(m.getKey(), "Discount") && Double.parseDouble(m.getValue().toString()) > 0) {
                    HSSFRow row = sheet.createRow(i + 1);
                    row.createCell(1).setCellValue("Total Discount");
                    row.createCell(2).setCellValue(Double.parseDouble(m.getValue().toString()));
                }
                if (StringUtils.equalsIgnoreCase(m.getKey(), "Service Charge") && Double.parseDouble(m.getValue().toString()) > 0) {
                    HSSFRow row = sheet.createRow(i + 2);
                    row.createCell(1).setCellValue("Service Charge");
                    row.createCell(2).setCellValue(Double.parseDouble(m.getValue().toString()));
                }
                if (StringUtils.equalsIgnoreCase(m.getKey(), "Rounding Off") && Double.parseDouble(m.getValue().toString()) != 0) {
                    HSSFRow row = sheet.createRow(i + 3);
                    row.createCell(1).setCellValue(m.getKey());
                    if (Double.parseDouble(m.getValue().toString()) > 0) {
                        row.createCell(2).setCellValue("-" + new DecimalFormat("#,##0.00").format(Double.parseDouble(m.getValue().toString())));
                    } else {
                        row.createCell(2).setCellValue(new DecimalFormat("#,##0.00").format(Double.parseDouble(m.getValue().toString()) * -1));
                    }
                }
                if (StringUtils.equalsIgnoreCase(m.getKey(), "Total Amount") && Double.parseDouble(m.getValue().toString()) > 0) {
                    HSSFRow row = sheet.createRow(i + 4);
                    row.createCell(1).setCellValue("Total Bill");
                    row.createCell(2).setCellValue(Double.parseDouble(m.getValue().toString()));
                }
                if (StringUtils.equalsIgnoreCase(m.getKey(), "Payment")) {
                    HSSFRow row = sheet.createRow(i + 5);
                    int y = i + 4;
                    row.createCell(0).setCellValue("Payment Type");
                    Map<String, Double> payment = (Map<String, Double>) m.getValue();
                    for (Map.Entry<String, Double> entry : payment.entrySet()) {
                        if (entry.getValue() > 0) {
                            row = sheet.createRow(++y);
                            row.createCell(1).setCellValue(entry.getKey());
                            row.createCell(2).setCellValue(entry.getValue());
                        }
                    }
                }
            }
            hwb.write(out);
            out.close();
        } catch (IOException ioex) {
            ioex.printStackTrace();
        } catch (Exception gex) {
            gex.printStackTrace();
        }
    }
    @Transactional
    public void downloadDaynedReportPdf(OutputStream outputStream, String date) {
        try {
            Font font1 = new Font(getcustomfont(), 12F, Font.BOLD);

            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Chunk CONNECT = new Chunk(new LineSeparator(1, 100, Color.BLACK, Element.ALIGN_JUSTIFIED, 3f));
            document.add(CONNECT);
            document.add(new Paragraph("", font1));
            Chunk CONNECT1 = new Chunk(new LineSeparator(1, 100, Color.WHITE, Element.ALIGN_JUSTIFIED, 3f));
            document.add(CONNECT1);
            PdfPTable table = createDayEndReport(date);
            table.setHeaderRows(1);
            document.add(printCompanyDetails());
            document.add(table);
            document.add(CONNECT1);
            Paragraph foot = new Paragraph();
            Phrase ph5 = new Phrase("Terms And Condition:Terms                    Prepared By                                        Approved By   ", font1);
            foot.add(ph5);
            document.add(foot);
            document.add(CONNECT);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    public PdfPTable createDayEndReport(String date) {
        Map<String, Object> map = reportForPeriodListReport(date.toString());
        Font font1 = new Font(getcustomfont(), 8, Font.NORMAL, Color.BLACK);
        int a = 0;
        PdfPTable tab = new PdfPTable(1);
        Font f = new Font(getcustomfont(), 15, Font.BOLD, Color.WHITE);
        PdfPTable tbl1 = new PdfPTable(1);
        PdfPTable table = new PdfPTable(a + 3);
        Font font = new Font(getcustomfont(), 10, Font.NORMAL, Color.WHITE);
        Color myColor = WebColors.getRGBColor("#326397");
        PdfPCell p = new PdfPCell(new Phrase("DayEndReport", f));
        p.setHorizontalAlignment(Element.ALIGN_CENTER);
        p.setBackgroundColor(myColor);
        tab.addCell(p);
        PdfPCell p1 = new PdfPCell(new Phrase("Date", font));
        p1.setBackgroundColor(myColor);
        tbl1.addCell(p1);
        PdfPCell pc2 = new PdfPCell(new Phrase("Item Name", font));
        pc2.setBackgroundColor(myColor);
        PdfPCell pc3 = new PdfPCell(new Phrase("Qty", font));
        pc3.setBackgroundColor(myColor);
        PdfPCell pc4 = new PdfPCell(new Phrase("Amount", font));
        pc4.setBackgroundColor(myColor);
        table.addCell(pc2);
        table.addCell(pc3);
        table.addCell(pc4);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(date));
        tbl1.addCell(new Phrase(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime()) + "", font1));
        table.setWidthPercentage(100);
        for (Map.Entry<String, Object> m : map.entrySet()) {
            if (!StringUtils.equalsIgnoreCase(m.getKey(), "Payment") && !StringUtils.equalsIgnoreCase(m.getKey(), "Service Charge") && !StringUtils.equalsIgnoreCase(m.getKey(), "Item Count") && !StringUtils.equalsIgnoreCase(m.getKey(), "Total Amount") && !StringUtils.equalsIgnoreCase(m.getKey(), "Rounding Off") && !StringUtils.equalsIgnoreCase(m.getKey(), "Discount")) {
                table.addCell(new Phrase(m.getKey() + "", font1));
                table.addCell(new Phrase("", font1));
                table.addCell(new Phrase("", font1));
                for (Map list : (List<Map>) m.getValue()) {
                    table.addCell(new Phrase(list.get("itemName").toString() + "", font1));
                    table.addCell(new Phrase(list.get("QtySold").toString() + "", font1));
                    if (list.get("totalAmtReceived") != null) {
                        table.addCell(new Phrase(list.get("totalAmtReceived").toString() + "", font1));
                    } else {
                        table.addCell(new Phrase(list.get("total").toString() + "", font1));
                    }
                }
            }
        }
        tab.addCell(tbl1);
        tab.addCell(table);
        PdfPTable tbl2 = new PdfPTable(2);
        PdfPTable tbl7 = new PdfPTable(2);
        PdfPTable tbl3 = new PdfPTable(a + 2);
        PdfPTable tbl4 = new PdfPTable(a + 2);
        PdfPTable tbl5 = new PdfPTable(1);
        PdfPTable tbl6 = new PdfPTable(2);
        for (Map.Entry<String, Object> m : map.entrySet()) {
            if (StringUtils.equalsIgnoreCase(m.getKey(), "Discount") && Double.parseDouble(m.getValue().toString()) > 0) {
                tbl2.addCell(new Phrase("Discount Amount" + "", font1));
                tbl2.addCell(new Phrase(Double.parseDouble(m.getValue().toString()) + "", font1));
            }
            if (StringUtils.equalsIgnoreCase(m.getKey(), "Service Charge") && Double.parseDouble(m.getValue().toString()) > 0) {
                tbl7.addCell(new Phrase(m.getKey().toString() + "", font1));
                tbl7.addCell(new Phrase(Double.parseDouble(m.getValue().toString()) + "", font1));
            }
            if (StringUtils.equalsIgnoreCase(m.getKey(), "Rounding Off") && Double.parseDouble(m.getValue().toString()) != 0) {
                tbl3.addCell(new Phrase(m.getKey().toString() + "", font1));
                if (Double.parseDouble(m.getValue().toString()) > 0) {
                    tbl3.addCell(new Phrase("-" + new DecimalFormat("#,##0.00").format(Double.parseDouble(m.getValue().toString())) + "", font1));
                } else {
                    tbl3.addCell(new Phrase(new DecimalFormat("#,##0.00").format(Double.parseDouble(m.getValue().toString()) * -1) + "", font1));
                }
            }
            if (StringUtils.equalsIgnoreCase(m.getKey(), "Total Amount") && Double.parseDouble(m.getValue().toString()) > 0) {
                tbl4.addCell(new Phrase("Total Bill" + "", font1));
                tbl4.addCell(new Phrase(Double.parseDouble(m.getValue().toString()) + "", font1));
            }
            if (StringUtils.equalsIgnoreCase(m.getKey(), "Payment")) {
                tbl5.addCell(new Phrase("Payment Type" + "", font1));
                Map<String, Double> payment = (Map<String, Double>) m.getValue();
                for (Map.Entry<String, Double> entry : payment.entrySet()) {
                    if (entry.getValue() > 0) {
                        tbl6.addCell(new Phrase(entry.getKey() + "", font1));
                        tbl6.addCell(new Phrase(entry.getValue() + "", font1));
                    }
                }
            }
        }
        tab.addCell(tbl2);
        tab.addCell(tbl7);
        tab.addCell(tbl3);
        tab.addCell(tbl4);
        tab.addCell(tbl5);
        tab.addCell(tbl6);
        return tab;
    }
}