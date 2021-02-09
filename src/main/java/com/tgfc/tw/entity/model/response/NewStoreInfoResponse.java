package com.tgfc.tw.entity.model.response;

import java.io.Serializable;
import java.util.List;

public class NewStoreInfoResponse implements Serializable {


    private int id;
    private String storeName;
    private String storeTel;
    private String remark;
    private List<TagResponse> tag;
    private List<StorePictureResponse> picUrl;
    private boolean canDelete;
    private EvaluationResponse evaluation;

    public NewStoreInfoResponse() {
    }

    public NewStoreInfoResponse(int id, String storeName, String storeTel, String remark, boolean canDelete) {
        this.id = id;
        this.storeName = storeName;
        this.storeTel = storeTel;
        this.remark = remark;
        this.canDelete = canDelete;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreTel() {
        return storeTel;
    }

    public void setStoreTel(String storeTel) {
        this.storeTel = storeTel;
    }

    public List<StorePictureResponse> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(List<StorePictureResponse> picUrl) {
        this.picUrl = picUrl;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<TagResponse> getTag() {
        return tag;
    }

    public void setTag(List<TagResponse> tag) {
        this.tag = tag;
    }

    public EvaluationResponse getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(EvaluationResponse evaluation) {
        this.evaluation = evaluation;
    }
}
