package com.hyva.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by bpradeep on 02-07-2017.
 */
public class Common
{

    public static String property_saas_domain = "hisaas_domainame";
    public static String saas_auth_header = "SaaS-Authorization";

    // JSON Keys
    public static String authToken = "authToken";


    public static String readDomainName(String domain) throws IOException {
        Properties prop = new Properties();
        InputStream in=null;
        try {
            in= Common.class.getClassLoader().getResourceAsStream("application.properties");
            prop.load(in);
            in.close();
        } catch (Exception e) {
            //LOGGER.error("Error in reading file=" + e);
        }
        finally {
            in.close();
        }
        return prop.getProperty(domain);
    }
}
