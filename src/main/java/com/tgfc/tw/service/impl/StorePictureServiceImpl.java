package com.tgfc.tw.service.impl;

import com.tgfc.tw.entity.model.po.Store;
import com.tgfc.tw.entity.model.po.StorePicture;
import com.tgfc.tw.entity.model.response.StorePictureResponse;
import com.tgfc.tw.entity.repository.StorePictureRepository;
import com.tgfc.tw.entity.repository.StoreRepository;
import com.tgfc.tw.service.FileService;
import com.tgfc.tw.service.StorePictureService;
import com.tgfc.tw.service.exception.FileNameEncodeException;
import com.tgfc.tw.service.exception.FileNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StorePictureServiceImpl implements StorePictureService {

    @Autowired
    StorePictureRepository storePictureRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    FileService fileService;

    @Autowired
    HttpServletRequest request;

    @Autowired
    FileServiceImpl fileServiceImpl;

    private static Logger logger = LoggerFactory.getLogger(StorePictureServiceImpl.class);

    @Override
    @Transactional
    public void deletePicUrl(int picId) throws FileNotFoundException {

        logger.info("Service:StorePictureServiceImpl/Method:deletePicUrl()-Var1 Member:{}", picId);

        Optional<StorePicture> storePictureOptional = storePictureRepository.findById(picId);
        if (!storePictureOptional.isPresent())
            throw new FileNotFoundException();

        storePictureRepository.deleteById(picId);
    }

    public void deletePicList(int storeId) throws FileNotFoundException {

        logger.info("Service:StorePictureServiceImpl/Method:deletePicList()-Var1 Member:{}", storeId);

        List<StorePicture> pictures = storePictureRepository.getUrlByStoreId(storeId);

        for (StorePicture item : pictures) {
            Optional<StorePicture> storePictureOptional = storePictureRepository.findById(item.getId());
            if (!storePictureOptional.isPresent())
                throw new FileNotFoundException();
            storePictureRepository.deleteById(item.getId());
        }
    }

    @Override
    @Transactional
    public void deletePic(int picId) throws FileNotFoundException {

        logger.info("Service:StorePictureServiceImpl/Method:deletePic()-Var1 Member:{}", picId);

        StorePicture storePicture = storePictureRepository.getById(picId);
        if (storePicture == null)
            throw new FileNotFoundException();
        if (!fileService.deletePhoto(storePicture.getName()))
            throw new FileNotFoundException();

        storePictureRepository.deleteById(picId);
    }

    @Override
    public List<StorePicture> getStorePicture(int storeId) {
        List<StorePicture> pictures = storePictureRepository.getUrlByStoreId(storeId);
        return pictures;
    }

    @Override
    @Transactional
    public void insertStorePicture(MultipartFile[] files, int storeId) throws FileNotFoundException, FileNameEncodeException {

        logger.info("Service:StorePictureServiceImpl/Method:insertStorePicture()-Var1 Member:{},Var2 Member:{}", files.length, storeId);

        Optional<Store> storeOptional = storeRepository.findById(storeId);
        if (!storeOptional.isPresent())
            throw new RuntimeException("store is not found");
        Store store = storeOptional.get();

        fileService.uploadFiles(files, storeId);

        List<StorePicture> storeList = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = null;
            try {
                fileName = URLEncoder.encode(file.getOriginalFilename(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new FileNameEncodeException();
            }
            StorePicture item = new StorePicture();
            item.setStore(store);
            item.setUrl(fileName);
            storeList.add(item);
        }

        storePictureRepository.saveAll(storeList);
    }

    @Override
    @Transactional
    public List<StorePictureResponse> insertPicture(MultipartFile[] files) throws FileNotFoundException, FileNameEncodeException {

        logger.info("Service:StorePictureServiceImpl/Method:insertStorePicture()-Var1 Member:{},Var2 Member:{}", files.length);

        Map fileNames = fileService.uploadPhoto(files);

        List<StorePicture> storeList = new ArrayList<>();
        for (MultipartFile file : files) {
            StorePicture item = new StorePicture();
            item.setName(fileNames.get(file.getOriginalFilename()).toString());
            item.setUrl(fileService.getPhotoUrl(fileNames.get(file.getOriginalFilename()).toString()));
            storeList.add(item);
        }

        storePictureRepository.saveAll(storeList);

        List<StorePictureResponse> pictureRequests = new ArrayList<>();
        for (StorePicture picture : storeList) {
            StorePictureResponse pictureItem = new StorePictureResponse();
            pictureItem.setId(picture.getId());
            pictureItem.setStorePicName(picture.getName());
            pictureItem.setStorePictureUrl(picture.getUrl());
            pictureItem.setId(picture.getId());
            pictureRequests.add(pictureItem);
        }

        return pictureRequests;
    }


}
