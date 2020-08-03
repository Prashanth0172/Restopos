/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hyva.restopos.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * @author admin
 */
public class HiposUtil {

    public static String getNextRefInvoice(String prefix, String nextRef) {
        StringBuilder sb = new StringBuilder();
        return sb.append(prefix).append(nextRef).toString();

    }

    public static boolean isStringNullrEmpty(String value) {

        return value == null || value.trim().isEmpty();
    }

    public static boolean isStringUndefined(String value) {
        return value == "undefined";
    }

    public static Date parseDate(String strDate) {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        f.setTimeZone(utc);
        GregorianCalendar cal = new GregorianCalendar(utc);
        try {
            cal.setTime(f.parse(strDate));
        } catch (Exception e) {
            e.getMessage();
        }
        return cal.getTime();

    }
}
