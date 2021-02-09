package com.tgfc.tw.service.impl;

import com.tgfc.tw.entity.model.po.StorePicture;
import com.tgfc.tw.entity.repository.StorePictureRepository;
import com.tgfc.tw.entity.repository.StoreRepository;
import com.tgfc.tw.service.FileService;
import com.tgfc.tw.service.exception.FileNameEncodeException;
import com.tgfc.tw.service.exception.FileNotFoundException;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private HttpServletRequest request;

    String filePath = null;
    @Autowired
    ServletContext context;
    @Autowired
    StorePictureRepository storePictureRepository;
    @Autowired
    StoreRepository storeRepository;

    @Override
    public byte[] getFile(int storePicId) throws FileNotFoundException {
        Optional<StorePicture> optionalStorePicture = storePictureRepository.findById(storePicId);
        if (!optionalStorePicture.isPresent())
            throw new FileNotFoundException();

        StorePicture storePicture = optionalStorePicture.get();

        return getFile(storePicture.getUrl(), storePicture.getStore().getId());
    }

    @Override
    public byte[] getFile(String fileName, int storeId) throws FileNotFoundException {
        if (filePath == null) {
            filePath = Paths.get(request.getServletContext().getRealPath("/")).getParent().toAbsolutePath() + "/hungryFiles/";
        }

        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new FileNotFoundException();
        }

        Path path = Paths.get(String.format("%s/%d/%s", filePath, storeId, fileName));
        byte[] data = new byte[0];
        try {
            data = Files.readAllBytes(path);
            return data;
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
    }

    @Override
    @Transactional
    public void uploadFiles(MultipartFile[] files, int storeId) throws FileNotFoundException, FileNameEncodeException {
        if (files == null || files.length == 0)
            throw new FileNotFoundException();

        if (filePath == null) {
            filePath = Paths.get(request.getServletContext().getRealPath("/")).getParent().toAbsolutePath() + "/hungryFiles/";
        }

        Path path = Paths.get(String.format("%s/%d/", filePath, storeId));

        for (MultipartFile file : files) {
            String fileName = null;
            try {
                fileName = URLEncoder.encode(file.getOriginalFilename(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new FileNameEncodeException();
            }
            File dest = new File(path.toAbsolutePath() + "/" + fileName);
            try {
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                file.transferTo(dest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public byte[] getPhoto(String fileName) throws FileNotFoundException {
        if (filePath == null) {
            filePath = Paths.get(request.getServletContext().getRealPath("/")).getParent().toAbsolutePath() + "/hungryFiles/";
        }
        Path path = Paths.get(String.format("%s/%s", filePath, fileName));
        byte[] data = new byte[0];
        try {
            data = Files.readAllBytes(path);
            return data;
        } catch (IOException e) {
            throw new FileNotFoundException();
        }
    }


    @Override
    @Transactional
    public Map<String,String> uploadPhoto(MultipartFile[] files) throws FileNotFoundException, FileNameEncodeException {

        Map<String,String> photoNames=new HashMap<>();
        if (files == null || files.length == 0)
            throw new FileNotFoundException();

        if (filePath == null) {
            filePath = Paths.get(request.getServletContext().getRealPath("/")).getParent().toAbsolutePath() + "/hungryFiles/";
        }

        Path path = Paths.get(String.format("%s/", filePath));

        for (MultipartFile file : files) {
            if(!FilenameUtils.getExtension(file.getOriginalFilename()).matches("(jpg|gif|jpeg|png){1}$"))
                throw new FileNameEncodeException();
            Date date = new Date();
            long second=date.getTime();
            String photoName = second+"."+ FilenameUtils.getExtension(file.getOriginalFilename());
            String photoNameSmall = second+ "s" +"."+ FilenameUtils.getExtension(file.getOriginalFilename());
            File dest = new File(path.toAbsolutePath() + "/" + photoName);
            try {
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs();
                }
                file.transferTo(dest);
                Thumbnails.of(dest).scale(0.2f).toFile(path.toAbsolutePath() + "/" +photoNameSmall);
                photoNames.put(file.getOriginalFilename(),photoName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return photoNames;
    }

    @Override
    public String getPhotoUrl(String photoId) {
        String filePath = request.getContextPath() + "/api/images/"+ photoId ;
        return filePath;
    }



    @Override
    @Transactional
    public boolean deletePhoto(String picName) {
        if (filePath == null) {
            filePath = Paths.get(request.getServletContext().getRealPath("/")).getParent().toAbsolutePath() + "/hungryFiles/";
        }
        File file = new File(filePath+picName);
        String[] name=picName.replace("."," ").split(" ");
        File fileSmall = new File(filePath+name[0]+"s."+FilenameUtils.getExtension(picName));
        if (file.isFile() && file.exists()&&fileSmall.isFile()&&fileSmall.exists()) {
            fileSmall.delete();
            return file.delete();
        }
        return false;
    }

}
