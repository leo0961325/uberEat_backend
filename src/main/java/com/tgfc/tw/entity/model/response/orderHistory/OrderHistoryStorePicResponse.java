package com.tgfc.tw.entity.model.response.orderHistory;

import com.tgfc.tw.entity.model.po.StorePicture;
import org.springframework.beans.BeanUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrderHistoryStorePicResponse {
    private int id;
    private String fileName;
    private String fileUrl;

    public OrderHistoryStorePicResponse() {
    }

    public OrderHistoryStorePicResponse(StorePicture storePicture){
        this.id = storePicture.getId();
        this.fileName = storePicture.getName();
        this.fileUrl = storePicture.getUrl();
    }

    public OrderHistoryStorePicResponse(int id, String fileName, String fileUrl) {
        this.id = id;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public static List<OrderHistoryStorePicResponse> valueListOf(List<StorePicture> storePictureList){
        List<OrderHistoryStorePicResponse> list = new ArrayList<>();

        for(StorePicture storePicture : storePictureList){
                OrderHistoryStorePicResponse newStorePic = OrderHistoryStorePicResponse.valueOf(storePicture);
                list.add(newStorePic);
        }
        return list;
    }
    private static OrderHistoryStorePicResponse valueOf(StorePicture storePictures) {
        OrderHistoryStorePicResponse newStorePictures = new OrderHistoryStorePicResponse();
        BeanUtils.copyProperties(storePictures, newStorePictures);
        newStorePictures.setFileName(storePictures.getName());
        newStorePictures.setFileUrl(storePictures.getUrl());
        return newStorePictures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderHistoryStorePicResponse that = (OrderHistoryStorePicResponse) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
