package com.tgfc.tw.entity.model.response;

import com.tgfc.tw.entity.model.po.Store;
import com.tgfc.tw.entity.model.po.StorePicture;
import com.tgfc.tw.entity.model.po.Tag;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class StoreInfoResponse implements Serializable {


    private int id;
    private String storeName;
    private String storeTel;
    private String remark;
    private List<TagResponse> tag;
    private int userLike;
    private int userDislike;
    private List<String> message;
    private List<StorePictureResponse> picUrl;
    private Integer reviewed;
    private boolean canDelete;

    public StoreInfoResponse() {
    }

    public StoreInfoResponse(int id, String storeName, String storeTel, String remark, boolean canDelete) {
        this.id = id;
        this.storeName = storeName;
        this.storeTel = storeTel;
        this.remark = remark;
        this.canDelete = canDelete;
    }

    public StoreInfoResponse(Store store, Integer reviewed, long groupCount) {
        this.id = store.getId();
        this.storeName = store.getName();
        this.storeTel = store.getTel();
        if (!(store.getStoreReviewList() == null || store.getStoreReviewList().size() == 0)) {
            List<Integer> review = store.getStoreReviewList().stream().map(r -> r.getReview()).collect(Collectors.toList());
            for (Integer count : review) {
                if (count == 1)
                    this.userLike++;
                else
                    this.userDislike++;
            }
        }
//        if (!(store.getStoreCommentList() == null || store.getStoreCommentList().size() == 0)) {
//            this.message = store.getStoreCommentList().stream().map(r -> r.getMessage()).collect(Collectors.toList());
//        }
        this.picUrl = store.getStorePictureList().stream().map(sp -> new StorePictureResponse(sp)).collect(Collectors.toList());
        this.reviewed = reviewed;
        this.canDelete = groupCount == 0;
        this.remark= store.getRemark();
        this.tag = store.getTagList().stream().map(TagResponse::new).collect(Collectors.toList());
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

    public int getUserLike() {
        return userLike;
    }

    public void setUserLike(int userLike) {
        this.userLike = userLike;
    }

    public int getUserDislike() {
        return userDislike;
    }

    public void setUserDislike(int userDislike) {
        this.userDislike = userDislike;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }

    public List<StorePictureResponse> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(List<StorePictureResponse> picUrl) {
        this.picUrl = picUrl;
    }

    public Integer getReviewed() {
        return reviewed;
    }

    public void setReviewed(Integer reviewed) {
        this.reviewed = reviewed;
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
}
