package com.tgfc.tw.service.impl;

import com.tgfc.tw.entity.model.po.Tag;
import com.tgfc.tw.entity.model.request.TagAddRequest;
import com.tgfc.tw.entity.model.request.TagUpdateRequest;
import com.tgfc.tw.entity.model.response.TagResponse;
import com.tgfc.tw.entity.repository.StoreRepository;
import com.tgfc.tw.entity.repository.TagRepository;
import com.tgfc.tw.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TagImpl implements TagService {

    @Autowired
    TagRepository tagRepository;

    @Autowired
    StoreRepository storeRepository;

    private static Logger logger = LoggerFactory.getLogger(TagService.class);


    //新增店家類型
    @Override
    @Transactional
    public void addTag(TagAddRequest request) throws Exception {

        logger.info("TagService addTag ： tagName = {}", request.getName());

        Optional<Tag> check = tagRepository.findByName(request.getName());
        if (check.isPresent())
            throw new Exception("The tag is already exist.");

        Tag newTag = new Tag();
        newTag.setName(request.getName());
        tagRepository.save(newTag);

        logger.info("TagService addTag ： add success");
    }

    @Override
    @Transactional
    public void deleteTag(int id) throws Exception {
        logger.info("TagService deleteTag ： deleteTagId = {}", id);

        Optional<Tag> typeExist = tagRepository.findById(id);
        if (!typeExist.isPresent())
            throw new Exception("id doesn't exist.");

        tagRepository.deleteById(id);

        logger.info("TagService deleteTag ： delete success");
    }

    @Override
    @Transactional
    public void updateTag(TagUpdateRequest request) throws Exception {

        logger.info("TagService updateTag ： updateTagId = {}", request.getId());

        Optional<Tag> typeExist = tagRepository.findById(request.getId());
        if(!typeExist.isPresent())
            throw new Exception("id doesn't exist.");

        Optional<Tag> check = tagRepository.findByName(request.getName());
        if (check.isPresent())
            throw new Exception("The tag is already exist.");

        Tag updateTag = tagRepository.getById(request.getId());
        updateTag.setName(request.getName());
        tagRepository.save(updateTag);

        logger.info("TagService updateTag ： update success");
    }

    public List<TagResponse> getTag() {
        return tagRepository.findAllBy();
    }
}
