package cn.mrs47.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

/**
 * @author mrs47
 */
public class FTPUtil {
    private static  final Logger logger = LoggerFactory.getLogger(FTPUtil.class);

    private static String ftpIp ;
    private static String ftpUser;
    private static String ftpPass ;
    private static Properties pop;
    private static FTPClient ftpClient;


    public FTPUtil() {
        pop=new Properties();
        try {
            pop.load(new InputStreamReader(FTPUtil.class.getClassLoader().getResourceAsStream("ftp.properties"),"UTF-8"));
            ftpIp = pop.getProperty("ftp.server.ip");
            ftpUser = pop.getProperty("ftp.user");
            ftpPass = pop.getProperty("ftp.pass");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean uploadFile(String path,List<File> files) throws Exception {
        FTPUtil ftpUtil=new FTPUtil();
        boolean result=false;

            result = ftpUtil.makeUploadFile(path, files);

        return result;
    }


    private boolean makeUploadFile(String remotePath,List<File> files) throws Exception {
        boolean uploaded = true;
        FileInputStream fis = null;
        //连接FTP服务器
        if (connectServer()){
            try {
                boolean tag = ftpClient.changeWorkingDirectory(remotePath);
                //文件不存在则创建
                if (!tag){
                    String[] split = remotePath.split("/");
                    for (int i=0;i<split.length;i++){
                        ftpClient.makeDirectory(split[i]);
                        tag=ftpClient.changeWorkingDirectory(split[i]);
                    }
                }
                if (!tag){
                    return false;
                }
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                for (File file:files){
                    fis=new FileInputStream(file);
                    logger.info(file.getPath());
                    ftpClient.storeFile(file.getName(),fis);
                }
            } catch (IOException e) {
                logger.error("上传文件异常",e);
                uploaded = false;
                e.printStackTrace();
            }finally {
                fis.close();
                ftpClient.logout();
                ftpClient.disconnect();
            }
        }
        return uploaded;
    }

    public static boolean removeFile(String remotePath,List<String> fileNames) throws Exception {
        FTPUtil ftpUtil=new FTPUtil();
        boolean result=false;
        result = ftpUtil.makeRemoveFile(remotePath, fileNames);
        return result;
    }

    private boolean makeRemoveFile(String remotePath,List<String> fileNames) throws Exception {
        boolean removed = true;
        //连接FTP服务器
        if (connectServer()){
            try {
                boolean b = ftpClient.changeWorkingDirectory(remotePath);
                for (String fileName:fileNames){
                    boolean b1 = ftpClient.deleteFile(fileName);
                }
            } catch (IOException e) {
                logger.error("删除文件异常",e);
                removed = false;
                e.printStackTrace();
                throw e;
            }finally {
                ftpClient.logout();
                ftpClient.disconnect();
            }
        }
        return removed;
    }

    private boolean connectServer() throws Exception {
        boolean isSuccess=false;
        ftpClient=new FTPClient();
        try {
            ftpClient.connect(ftpIp);
            isSuccess = ftpClient.login(ftpUser, ftpPass);
        } catch (IOException e) {
            logger.error("连接FTP服务器异常",e);
            throw new IOException();
        }
        return isSuccess;
    }

    public static String getPropertiesValue(String key){
        Properties pop=new Properties();
        try {
            pop.load(new InputStreamReader(FTPUtil.class.getClassLoader().getResourceAsStream("ftp.properties"),"UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pop.getProperty(key);
    }
}
