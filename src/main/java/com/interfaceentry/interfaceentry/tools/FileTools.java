package com.interfaceentry.interfaceentry.tools;

import com.interfaceentry.interfaceentry.entity.MerchantEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Base64;
import java.util.Map;

/**
 * 文件帮助
 *
 * @author chengxiaohong coax@outlook.it
 * @create 2018-08-20 22:25
 **/
public class FileTools {
    /**
     * inputStream转File
     *
     * @param ins
     * @param file
     */
    public static void inputStreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 图片转换
     *
     * @param merchantEntity
     * @param fileMap
     */
    public static void initFile2Base64Set2Obj(MerchantEntity merchantEntity, Map<String, MultipartFile> fileMap) throws IOException {
        //身份证正面
        MultipartFile _identityCardFrontPic = fileMap.get("_identityCardFrontPic");
        merchantEntity.setIdentityCardFrontPic(multipartFile2Base64(_identityCardFrontPic));

        MultipartFile _identityCardReversePic = fileMap.get("_identityCardReversePic");
        merchantEntity.setIdentityCardReversePic(multipartFile2Base64(_identityCardReversePic));

        MultipartFile _licensePic = fileMap.get("_licensePic");
        merchantEntity.setLicensePic(multipartFile2Base64(_licensePic));

        MultipartFile _storeInteriorPic = fileMap.get("_storeInteriorPic");
        merchantEntity.setStoreInteriorPic(multipartFile2Base64(_storeInteriorPic));

        MultipartFile _storeSignBoardPic = fileMap.get("_storeSignBoardPic");
        merchantEntity.setStoreSignBoardPic(multipartFile2Base64(_storeInteriorPic));


    }

    public static String multipartFile2Base64(MultipartFile multipartFile) throws IOException {
        File f = null;
        if (multipartFile.equals("") || multipartFile.getSize() <= 0) {
            return null;
        } else {
            InputStream ins = multipartFile.getInputStream();
            f = new File(multipartFile.getOriginalFilename());
            FileTools.inputStreamToFile(ins, f);
        }

        FileInputStream idReversePicInput = new FileInputStream(f);
        byte[] idReversePicBuffer = new byte[(int) f.length()];
        idReversePicInput.read(idReversePicBuffer);
        idReversePicInput.close();
        //操作完成后删除文件
        f.delete();
        return Base64.getEncoder().encodeToString(idReversePicBuffer);
    }


}
