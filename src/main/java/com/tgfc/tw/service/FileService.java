package com.tgfc.tw.service;

import com.tgfc.tw.service.exception.FileNameEncodeException;
import com.tgfc.tw.service.exception.FileNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface FileService {
    byte[] getFile(int storePicId) throws FileNotFoundException;
    byte[] getFile(String fileName, int storeId) throws FileNotFoundException;
    void uploadFiles(MultipartFile[] files, int storeId) throws FileNotFoundException, FileNameEncodeException;

    byte[] getPhoto(String fileName) throws FileNotFoundException;
    Map<String,String> uploadPhoto(MultipartFile[] files) throws FileNotFoundException, FileNameEncodeException;

    String getPhotoUrl(String photoId);

    boolean deletePhoto(String newName) ;
}
