package com.tgfc.tw.service;

import com.tgfc.tw.entity.model.po.StorePicture;
import com.tgfc.tw.entity.model.response.StorePictureResponse;
import com.tgfc.tw.service.exception.FileNameEncodeException;
import com.tgfc.tw.service.exception.FileNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorePictureService {

    void deletePicUrl(int picId) throws FileNotFoundException;

    void deletePic(int picId) throws FileNotFoundException;

    List<StorePicture> getStorePicture(int storeId);

    void insertStorePicture(MultipartFile[] files, int storeId) throws FileNotFoundException, FileNameEncodeException;

    List<StorePictureResponse> insertPicture(MultipartFile[] files) throws FileNotFoundException, FileNameEncodeException;

}
