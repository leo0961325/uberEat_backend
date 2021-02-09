package com.tgfc.tw.service;

import com.tgfc.tw.entity.model.request.TagAddRequest;
import com.tgfc.tw.entity.model.request.TagUpdateRequest;
import com.tgfc.tw.entity.model.response.TagResponse;

import java.util.List;

public interface TagService {
    void addTag(TagAddRequest request) throws Exception;
    void deleteTag(int id) throws Exception;
    void updateTag(TagUpdateRequest request) throws Exception;
    List<TagResponse> getTag() ;
}
