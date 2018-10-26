package com.westar.base.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 *文件的MD5
 * @author H87
 *
 */
public class FileMD5Util {
	/**
	 * 服务器端的文件md5
	 * @param fileName
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
    public static String getHash(String fileName) throws IOException, NoSuchAlgorithmException{
        
        File f = new File(fileName);
        InputStream ins = new FileInputStream(f);
        
        byte[] buffer = new byte[8192];
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        
        int len;
        while((len = ins.read(buffer)) != -1){
            md5.update(buffer, 0, len);
        }

        ins.close();
        String hashString = new BigInteger(1, md5.digest()).toString(16);
        return DigestUtils.md5Hex(hashString);
    }
    /**
     * 客户端附件的md5
     * @param upload
     * @return
     * @throws Exception
     */
    public static String getFileMd5(MultipartFile upload) throws Exception {
        byte[] uploadBytes = upload.getBytes();
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] digest = md5.digest(uploadBytes);
        String hashString = new BigInteger(1, digest).toString(16);
        return DigestUtils.md5Hex(hashString);
    }

}
