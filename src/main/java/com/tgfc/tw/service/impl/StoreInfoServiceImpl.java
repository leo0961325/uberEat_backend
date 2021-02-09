package com.tgfc.tw.service.impl;

import com.tgfc.tw.entity.model.po.*;
import com.tgfc.tw.entity.model.request.store.StoreRequest;
import com.tgfc.tw.entity.model.request.store.StoreRequestV2_1;
import com.tgfc.tw.entity.model.request.store.StoreUpdateRequest;
import com.tgfc.tw.entity.model.response.*;
import com.tgfc.tw.entity.repository.*;
import com.tgfc.tw.security.ContextHolderHandler;
import com.tgfc.tw.service.FileService;
import com.tgfc.tw.service.StoreInfoService;
import com.tgfc.tw.service.StorePictureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StoreInfoServiceImpl implements StoreInfoService {

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    FileService fileService;

    @Autowired
    StorePictureService storePictureService;

    @Autowired
    StoreReviewRepository storeReviewRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    StorePictureRepository storePictureRepository;

    @Autowired
    HttpServletRequest request;

    @Autowired
    FileServiceImpl fileServiceImpl;

    private static Logger logger = LoggerFactory.getLogger(StoreInfoServiceImpl.class);

    final boolean STORE_NOT_DELETE = false;

    @Override
    @Transactional
    public StoreAddResponse addStore(StoreRequest store) throws Exception {

        logger.info("Service:StoreInfoServiceImpl/Method:addStore()-Var1 Member:{},{},{},{},{},Var2 Member:{}",
                store.getStoreName(), store.getStoreTel(), store.getTagList(), store.getStoreAddress(), store.getRemark(), store.getPicList() != null ? store.getPicList().size() : null);

        if (storeRepository.getByStoreName(store.getStoreName()) != null)
            throw new Exception("此店家已存在");

        Store newStore = new Store(store);

        List<Tag> tagList = store.getTagList() != null ? tagRepository.getAllByIdIn(store.getTagList().toArray(new Integer[0])) : new ArrayList<>();
        newStore.setTagList(tagList);

        if (store.getPicList() != null) {
            List<StorePicture> list = new ArrayList<>();
            for (Integer picId : store.getPicList()) {
                Optional<StorePicture> storePicture = storePictureRepository.findById(picId);
                if (!storePicture.isPresent()) {
                    throw new Exception("新增店家時，因找不到圖片ID:" + picId + "，\n無法將ID寫入圖片資料庫及新增店家資料，\n請洽系統管理者!");
                }
                list.add(storePicture.get());
            }
            list.stream().forEach(r -> r.setStore(newStore));
            newStore.setStorePictureList(list);
        }

        storeRepository.save(newStore);
        StoreAddResponse response = new StoreAddResponse();
        response.setId(newStore.getId());
        response.setStoreName(newStore.getName());
        return response;
    }

    @Override
    @Transactional
    public StoreAddResponseV2_1 addStoreV2_1(StoreRequestV2_1 request) {
        logger.info("Service:StoreInfoServiceImpl/Method:addStore()-Var1 Member:{},{},{},{},{},Var2 Member:{}",
                request.getStoreName(), request.getStoreTel(), request.getTagList(), request.getStoreAddress(), request.getRemark(), request.getPicList() != null ? request.getPicList().size() : null);

        if (storeRepository.existsByNameAndIsDeleted(request.getStoreName(), STORE_NOT_DELETE))
            throw new IllegalArgumentException("此店家已存在");

        Store newStore = Store.valueOf(request);

        List<Tag> tagList = request.getTagList() != null ? tagRepository.getAllByIdIn(request.getTagList().toArray(new Integer[0])) : new ArrayList<>();
        newStore.setTagList(tagList);

        if (request.getPicList() != null) {
            List<StorePicture> list = new ArrayList<>();
            for (Integer picId : request.getPicList()) {
                Optional<StorePicture> storePicture = storePictureRepository.findById(picId);
                if (!storePicture.isPresent()) {
                    throw new IllegalArgumentException("新增店家時，因找不到圖片ID:" + picId + "，\n無法將ID寫入圖片資料庫及新增店家資料，\n請洽系統管理者!");
                }
                list.add(storePicture.get());
            }
            list.stream().forEach(r -> r.setStore(newStore));
            newStore.setStorePictureList(list);
        }

        Store save = storeRepository.save(newStore);
        return StoreAddResponseV2_1.valueOf(save);
    }

    @Override
    @Transactional
    public void updateStore(StoreUpdateRequest request) throws Exception {

        logger.info("Service:StoreInfoServiceImpl/Method:updateStore()-Var1 Member:{},{},{},{},{},Var2",
                request.getId(), request.getStoreName(), request.getStoreTel(), request.getStoreAddress(), request.getRemark());

        Optional<Store> storeOptional = storeRepository.findById(request.getId());
        if (!storeOptional.isPresent())
            throw new IllegalArgumentException("請輸入有效ID");

        List<Tag> tagList = request.getTagId() != null ? tagRepository.getAllByIdIn(request.getTagId().toArray(new Integer[0])) : new ArrayList<>();
        List<StorePicture> pictureList = request.getPicId() != null ? storePictureRepository.getAllByIdIn(request.getPicId().toArray(new Integer[0])) : new ArrayList<>();

        Store updateStore = storeOptional.get();


        updateStore.getStorePictureList().stream().forEach(r -> r.setStore(null));
        pictureList.stream().forEach(r -> r.setStore(storeOptional.get()));

        updateStore.setName(request.getStoreName());
        updateStore.setRemark(request.getRemark());
        updateStore.setAddress(request.getStoreAddress());
        updateStore.setTel(request.getStoreTel());
        updateStore.setTagList(tagList);
        updateStore.setStorePictureList(pictureList);

        storeRepository.save(updateStore);
    }

    @Override
    @Transactional
    public void newUpdateStore(StoreUpdateRequest request) throws Exception {

        logger.info("Service:StoreInfoServiceImpl/Method:newUpdateStore()-Var1 Member:{},{},{},{},{},Var2",
                request.getId(), request.getStoreName(), request.getStoreTel(), request.getStoreAddress(), request.getRemark());

        Optional<Store> storeOptional = storeRepository.findById(request.getId());
        if (!storeOptional.isPresent())
            throw new IllegalArgumentException("請輸入有效ID");

        List<Tag> tagList = request.getTagId() != null ? tagRepository.getAllByIdIn(request.getTagId().toArray(new Integer[0])) : new ArrayList<>();
        List<StorePicture> pictureList = request.getPicId() != null ? storePictureRepository.getAllByIdIn(request.getPicId().toArray(new Integer[0])) : new ArrayList<>();

        Store updateStore = storeOptional.get();
        if (updateStore.isDeleted()) {
            throw new IllegalArgumentException("請輸入有效ID");
        }

        if (!updateStore.getName().equals(request.getStoreName())) {
            if (storeRepository.existsByNameAndIsDeleted(request.getStoreName(), false)) {
                throw new Exception("店家名稱不得重複");
            }
        }


        updateStore.getStorePictureList().stream().forEach(r -> r.setStore(null));
        pictureList.stream().forEach(r -> r.setStore(storeOptional.get()));

        updateStore.setName(request.getStoreName());
        updateStore.setRemark(request.getRemark());
        updateStore.setAddress(request.getStoreAddress());
        updateStore.setTel(request.getStoreTel());
        updateStore.setTagList(tagList);
        updateStore.setStorePictureList(pictureList);

        storeRepository.save(updateStore);

    }

    @Override
    public StoreGetOneResponse getStore(int id) throws Exception {
        if (!storeRepository.existsById(id))
            throw new IllegalArgumentException("請輸入有效ID");
        return getStoreById(id);
    }

    @Override
    public StoreGetOneResponse getStoreV2_1(int id) throws Exception {
        if (!storeRepository.existsByIdAndIsDeleted(id, false))
            throw new IllegalArgumentException("請輸入有效ID");
        return getStoreById(id);
    }

    @Override
    @Transactional
    public void removeStore(int id) throws Exception {

        logger.info("Service:StoreInfoServiceImpl/Method:removeStore()-Var1 Member:{}", id);

        if (!storeRepository.existsById(id))
            throw new IllegalArgumentException("請輸入有效ID");


        List<Group> groupUsing = groupRepository.storeUsing(id);
        if (!groupUsing.isEmpty())
            throw new IllegalArgumentException("店家正在被團使用中，無法刪除");

        storeRepository.deleteById(id);
        orderRepository.deleteAllByStoreId(id);
    }

    @Override
    @Transactional
    public void newRemoveStore(int id) throws Exception {

        logger.info("Service:StoreInfoServiceImpl/Method:newRemoveStore()-Var1 Member:{}", id);

        Optional<Store> storeOptional = storeRepository.findByIdAndIsDeleted(id, false);


        if (!storeOptional.isPresent()) {
            throw new IllegalArgumentException("請輸入有效ID");
        }
        Store store = storeOptional.get();
        store.setDeleted(true);

        List<Group> groupUsing = groupRepository.storeUsing(id);
        if (!groupUsing.isEmpty())
            throw new IllegalArgumentException("店家正在被團使用中，無法刪除");


        storeRepository.save(store);
        orderRepository.deleteAllByStoreId(id);


    }

    @Deprecated
    @Override
    public Page<StoreInfoResponse> getStoreAllOrByName(String keyword, List<Integer> tagIdList, int pageSize, int pageNumber) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        if (tagIdList != null)
            tagIdList = (tagIdList.size() == 0) ? (null) : tagIdList;

        Page<Map<String, Object>> pageItem = storeRepository.getStoreByKeyword(keyword, tagIdList, pageable);
        List<Map<String, Object>> item = pageItem.getContent();
        List<StoreInfoResponse> storeList = item.stream().map(r -> new StoreInfoResponse((int) r.get("id"),
                (String) r.get("storeName"), (String) r.get("storeTel"),
                (String) r.get("remark"), ((BigInteger) r.get("canDelete")).intValue() == 0))
                .collect(Collectors.toList());
        List<Integer> storeIdList = item.stream().map(r -> (Integer) r.get("id")).collect(Collectors.toList());
        if (storeIdList != null && storeIdList.size() != 0) {
            List<Map<String, Object>> tagItem = tagRepository.getTagByStoreId(storeIdList);
            List<Map<String, Object>> pictureItem = storePictureRepository.getPictureByStoreId(storeIdList);
//            List<Map<String, Object>> commentItem = storeCommentRepository.getByStoreId(storeIdList);
            List<Map<String, Object>> reviewItem = storeReviewRepository.getByStoreId(storeIdList);
            for (StoreInfoResponse store : storeList) {
                if (tagItem != null && tagItem.size() != 0) {
                    List<TagResponse> tagList = tagItem.stream().filter(r -> (int) r.get("storeId") == store.getId())
                            .map(q -> new TagResponse((int) q.get("tagId"), (String) q.get("tagName")))
                            .collect(Collectors.toList());
                    store.setTag(tagList);
                }
                if (pictureItem != null && pictureItem.size() != 0) {
                    List<StorePictureResponse> pictureList = pictureItem.stream().filter(r -> (int) r.get("storeId") == store.getId())
                            .map(q -> new StorePictureResponse((int) q.get("pictureId"), (String) q.get("pictureUrl"), (String) q.get("pictureName")))
                            .collect(Collectors.toList());
                    store.setPicUrl(pictureList);
                }
//                if (commentItem != null && commentItem.size() != 0) {
//                    List<String> commentList = commentItem.stream().filter(r -> (int) r.get("storeId") == store.getId())
//                            .map(q -> (String) q.get("message"))
//                            .collect(Collectors.toList());
//                    store.setMessage(commentList);
//                }
                if (reviewItem != null && reviewItem.size() != 0) {
                    List<Integer> reviewList = reviewItem.stream().filter(r -> (int) r.get("storeId") == store.getId())
                            .map(q -> (int) q.get("review"))
                            .collect(Collectors.toList());
                    int likes = (int) reviewList.stream().filter(r -> r == 1).count();
                    int unlikes = (int) reviewList.stream().filter(r -> r == 2).count();
                    store.setUserLike(likes);
                    store.setUserDislike(unlikes);
                    store.setReviewed(likes - unlikes);
                }
            }
        }
        Page<StoreInfoResponse> stores = new PageImpl<>(storeList, pageable, pageItem.getTotalElements());

        return stores;
    }

    @Override
    public Page<NewStoreInfoResponse> getStoreList(String keyword, List<Integer> tagIdList, int pageSize, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        if (tagIdList != null)
            tagIdList = (tagIdList.size() == 0) ? (null) : tagIdList;

        DecimalFormat df = new DecimalFormat("##.0");
        Page<Map<String, Object>> pageItem = storeRepository.getNewStoreByKeyword(keyword, tagIdList, pageable);
        List<Map<String, Object>> item = pageItem.getContent();
        List<NewStoreInfoResponse> storeList = item.stream().map(r -> new NewStoreInfoResponse((int) r.get("id"),
                (String) r.get("storeName"), (String) r.get("storeTel"),
                (String) r.get("remark"), ((BigInteger) r.get("canDelete")).intValue() == 0))
                .collect(Collectors.toList());
        List<Integer> storeIdList = item.stream().map(r -> (Integer) r.get("id")).collect(Collectors.toList());
        if (storeIdList != null && storeIdList.size() != 0) {
            List<Map<String, Object>> tagItem = tagRepository.getTagByStoreId(storeIdList);
            List<Map<String, Object>> pictureItem = storePictureRepository.getPictureByStoreId(storeIdList);
            List<Map<String, Object>> reviewItem = storeReviewRepository.getReview(storeIdList);
            for (NewStoreInfoResponse store : storeList) {
                if (tagItem != null && tagItem.size() != 0) {
                    List<TagResponse> tagList = tagItem.stream().filter(r -> (int) r.get("storeId") == store.getId())
                            .map(q -> new TagResponse((int) q.get("tagId"), (String) q.get("tagName")))
                            .collect(Collectors.toList());
                    store.setTag(tagList);
                }
                if (pictureItem != null && pictureItem.size() != 0) {
                    List<StorePictureResponse> pictureList = pictureItem.stream().filter(r -> (int) r.get("storeId") == store.getId())
                            .map(q -> new StorePictureResponse((int) q.get("pictureId"), (String) q.get("pictureUrl"), (String) q.get("pictureName")))
                            .collect(Collectors.toList());
                    store.setPicUrl(pictureList);
                }
                if (reviewItem != null && reviewItem.size() != 0) {
                    for (Map<String, Object> reviewMap : reviewItem) {
                        if ((int) reviewMap.get("storeId") == store.getId()) {
                            EvaluationResponse evaluationResponse = new EvaluationResponse(
                                    Double.parseDouble(df.format((((BigDecimal) reviewMap.get("review")).doubleValue()) / ((BigInteger) reviewMap.get("totalUsers")).intValue()))
                                    , ((BigInteger) reviewMap.get("totalUsers")).intValue()
                            );
                            store.setEvaluation(evaluationResponse);
                        }

                    }

                }
                if (store.getEvaluation() == null) {
                    store.setEvaluation(new EvaluationResponse(0, 0));
                }


            }
        }
        Page<NewStoreInfoResponse> stores = new PageImpl<>(storeList, pageable, pageItem.getTotalElements());

        return stores;

    }

    @Transactional
    @Override
    public void reviewStore(int storeId, int review) throws Exception {

        logger.info("Service:StoreInfoServiceImpl/Method:reviewStore()-Var1 Member:{} -Var2 Member:{}", storeId, review);

        int userIdServer = ContextHolderHandler.getId();
        Optional<StoreReview> storeReview = storeReviewRepository.findReview(storeId, userIdServer);
        Optional<Store> store = storeRepository.findById(storeId);
        if (!store.isPresent()) {
            throw new Exception("此店家不存在");
        }
        User user = userRepository.getUserById(userIdServer);

        if (!storeReview.isPresent()) {
            StoreReview newReview = new StoreReview(store.get(), review, user);
            storeReviewRepository.save(newReview);
        } else {
            if (review == storeReview.get().getReview()) {
                storeReviewRepository.delete(storeReview.get());
            } else {
                storeReview.get().setReview(review);
            }
        }
    }

    StoreGetOneResponse getStoreById(int id) {
        List<Map<String, Object>> item = storeRepository.getStoreById(id);
        Set<StorePictureResponse> storePictureSet = item.stream().filter(q -> q.get("storePictureId") != null)
                .map(r -> new StorePictureResponse((int) r.get("storePictureId"), (String) r.get("storePictureUrl"), (String) r.get("storePictureName")))
                .collect(Collectors.toSet());
        Set<TagResponse> tagSet = item.stream().filter(q -> q.get("tagId") != null)
                .map(r -> new TagResponse((int) r.get("tagId"), (String) r.get("tagName")))
                .collect(Collectors.toSet());
        List<StorePictureResponse> storePictureList = new ArrayList<>(storePictureSet);
        List<TagResponse> tagList = new ArrayList<>(tagSet);
        StoreGetOneResponse store = new StoreGetOneResponse((int) item.get(0).get("storeId"), (String) item.get(0).get("storeName"),
                (String) item.get(0).get("storeTel"), (String) item.get(0).get("storeAddress"), (String) item.get(0).get("storeRemark"),
                tagList, storePictureList);
        return store;
    }
}
