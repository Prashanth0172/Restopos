package com.hyva.restopos.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.hyva.restopos.util.HiNextConstants.DEFAULT_TENANT_ID;

/**
 * @Author udaybhaskar
 * Created on 7/15/17.
 */
public class TenantContext {
    private static Logger logger = LoggerFactory.getLogger(TenantContext.class.getName());
    private static ThreadLocal<String> currentTenant = new ThreadLocal<>();

    public static void setCurrentTenant(String tenant) {
        logger.debug("Setting tenant to " + tenant);
        currentTenant.set(tenant);
    }

    public static String getCurrentTenant() {
        String tenantId = currentTenant.get();
        if (StringUtils.isBlank(tenantId)){
            return DEFAULT_TENANT_ID;
        }
        return tenantId;
    }

    public static void clear() {
        currentTenant.set(null);
    }

}
