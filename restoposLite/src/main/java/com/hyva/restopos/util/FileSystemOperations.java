package com.hyva.restopos.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Component("fileSystemOperations")
public class FileSystemOperations {
    private final Logger log = Logger.getLogger(getClass());

    private static String resourceDir;
    private static String imagesDir;
    private static String backupDir;

    @PostConstruct
    public String init() {
        if (!StringUtils.isBlank(resourceDir)) {
            File file = new File(resourceDir + File.separator + "hiaccounts" + File.separator);
            file.mkdir();
        } else {
            resourceDir = System.getProperty("user.home") + File.separator + "hiaccounts" + File.separator;
            File file = new File(resourceDir);
            if (!file.exists()) {
                file.mkdir();
            }

        }
        log.info("resourceDir = " + resourceDir);
        File file = new File(resourceDir + File.separator + "images" + File.separator);
        if (!file.exists()) {
            file.mkdir();
        }

        File backupFileDir = new File(resourceDir + File.separator + HiNextConstants.BACKUP_FOLDER + File.separator);
        if (!backupFileDir.exists()) {
            backupFileDir.mkdir();
        }
        backupDir = backupFileDir.getAbsolutePath();
        imagesDir = file.getAbsolutePath();
        return resourceDir;
    }

    public static String getImagesDir(String fileName) {
        return imagesDir + File.separator + TenantContext.getCurrentTenant() + File.separator;
    }
    public static String getImagesDirItem() {
        File file=new File(imagesDir + File.separator + TenantContext.getCurrentTenant() + File.separator);
        file.mkdir();
        return imagesDir + File.separator + TenantContext.getCurrentTenant() + File.separator;
    }

    public static String getImagesDirWithoutTenant() {
        return imagesDir;
    }

    public static String getBackupDir() {
        return backupDir;
    }

    /**
     * For storing image.
     *
     * @param inputStream
     * @param fileName
     * @return stored image path.just put it in front end.image will be fetched.
     * @throws IOException
     */
    public static String storeImage(InputStream inputStream, String fileName) throws IOException {
        File fileNameToStore = new File(imagesDir + File.separator + TenantContext.getCurrentTenant()
                + File.separator + RandomUUID.getRandomUUID() + "." + FilenameUtils.getExtension(fileName));
        File parentFile = fileNameToStore.getParentFile();
        if (!fileNameToStore.getParentFile().exists()) {
            parentFile.mkdir();
        }
        FileUtils.copyInputStreamToFile(inputStream, fileNameToStore);
        return "/images/" + TenantContext.getCurrentTenant() + "/" + fileNameToStore.getName();
    }

    public static String deleteImage(String fileName) {
        File file = new File(resourceDir + fileName);
        if (file.exists()) {
            file.delete();
        }
        return file.getAbsolutePath();
    }

    public static String getResourceDir() {
        return resourceDir;
    }

    @Value("${file.system.dir}")
    public void setResourceDir(String resourceDir) {
        FileSystemOperations.resourceDir = resourceDir;
    }
}
