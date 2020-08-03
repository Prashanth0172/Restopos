package com.hyva.restopos.util;

import java.util.UUID;

/**
 * Created by udaybhaskar on 28/10/16.
 */
public class RandomUUID {
    public static String getRandomUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
