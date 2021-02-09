package com.tgfc.tw.controller;

import com.tgfc.tw.controller.exception.ErrorCodeException;
import com.tgfc.tw.controller.exception.enums.UploadErrorCode;
import com.tgfc.tw.entity.cenum.PermissionEnum;
import com.tgfc.tw.entity.model.po.StorePicture;
import com.tgfc.tw.entity.model.response.StorePictureResponse;
import com.tgfc.tw.service.FileService;
import com.tgfc.tw.service.StorePictureService;
import com.tgfc.tw.service.exception.FileNameEncodeException;
import com.tgfc.tw.service.exception.FileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;

@RestController
public class StorePictureController {

    @Autowired
    StorePictureService storePictureService;
    @Autowired
    FileService fileService;

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @DeleteMapping(value = "/api/storePicture/deletePic")
    public void deletePic(@RequestParam int picId) throws ErrorCodeException {
        try {
            storePictureService.deletePic(picId);
        } catch (FileNotFoundException e) {
            throw new ErrorCodeException(UploadErrorCode.FILE_NOT_FOUND, e.getMessage());
        }
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping(value = "/api/storePicture/getPicUrlByStoreId")
    public List<StorePicture> getPicUrlByStoreId(@RequestParam int storeId) {
        return storePictureService.getStorePicture(storeId);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @PostMapping(value = "/api/upload/photo")
    public List<StorePictureResponse> uploadPhoto(@RequestParam("files") MultipartFile[] files) throws ErrorCodeException {
        List<StorePictureResponse> pictureRequests = new ArrayList<>();
        try {
            pictureRequests = storePictureService.insertPicture(files);

        } catch (FileNotFoundException e) {
            throw new ErrorCodeException(UploadErrorCode.FILE_NOT_FOUND, e.getMessage());
        } catch (FileNameEncodeException e) {
            throw new ErrorCodeException(UploadErrorCode.FILE_NAME_INCORRECT, e.getMessage());
        }

        return pictureRequests;
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping(value = "/api/images/{fileName}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable String fileName) throws ErrorCodeException {
        try {
            byte[] image = fileService.getPhoto(fileName);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
        } catch (FileNotFoundException e) {
            throw new ErrorCodeException(UploadErrorCode.IMAGE_FILE_NOT_FOUND, e.getMessage());
        }
    }


}
