package com.zjft.configuration;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.File;

/**
 * @Descrition TODO
 * @Author shanwang
 * @Date 2019/11/21 19:39
 * @Version 1.0.0
 */

public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static String readFile(String filePath) {
        File file = new File(filePath);
        String content = null;
        try {
            content = FileUtils.readFileToString(file,"UTF-8");
        } catch (IOException e) {
            logger.error("FileUtil->readFile catch Exception:",e);
        }
        return content;
    }
}
