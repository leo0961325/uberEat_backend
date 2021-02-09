package com.tgfc.tw.service.impl;

import com.tgfc.tw.entity.cenum.PermissionEnum;
import com.tgfc.tw.entity.cenum.TimeStatusEnum;
import com.tgfc.tw.entity.model.po.*;
import com.tgfc.tw.entity.model.request.AddGroupRequest;
import com.tgfc.tw.entity.model.request.GroupRequest;
import com.tgfc.tw.entity.model.request.GroupWithoutIdRequest;
import com.tgfc.tw.entity.model.response.*;
import com.tgfc.tw.entity.model.response.order.OrderDetailResponse;
import com.tgfc.tw.entity.model.response.user.UserNameOnlyResponse;
import com.tgfc.tw.entity.repository.*;
import com.tgfc.tw.security.ContextHolderHandler;
import com.tgfc.tw.service.GroupService;
import com.tgfc.tw.service.StorePictureService;
import com.tgfc.tw.service.exception.IdNotFoundException;
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
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderPassRepository orderPassRepository;

    @Autowired
    FloorRepository floorRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    StorePictureService storePictureService;

    @Autowired
    HttpServletRequest request;

    @Autowired
    FileServiceImpl fileServiceImpl;

    @Autowired
    StoreReviewRepository storeReviewRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    OrdServiceImpl ordServiceImpl;

    @Autowired
    UserPayRepository userPayRepository;


    private Logger logger = LoggerFactory.getLogger(GroupService.class);

    @Override
    @Transactional
    public void insertGroup(GroupWithoutIdRequest model) throws Exception {

        logger.info("GroupService_insertGroup GroupName:{}, Store:{}," +
                        "UserManager:{}, OpenDate:{}, StartTime:{}, EndTime:{}, Floors:{} ", model.getName(), model.getStoreList(),
                model.getUserList(), model.getOpenDate(), model.getStartTime(),
                model.getEndTime(), model.getFloorIdList());

        Group group = new Group();
        if (group.getId() == 0) {
            group.setName(model.getName());

            Timestamp openDate = new Timestamp(new Date().getTime());
            if (model.getOpenDate() != null)
                openDate = model.getOpenDate();
            group.setOpenDate(openDate);

            Timestamp startTime = new Timestamp(new Date().getTime());
            if (model.getStartTime() != null)
                startTime = model.getStartTime();
            group.setStartTime(startTime);

            Timestamp endTime = model.getEndTime();
            group.setEndTime(endTime);


            List<Store> storeList = storeRepository.getByIdIn(model.getStoreList());
            if (storeList.size() < model.getStoreList().size())
                throw new Exception("店家ID不存在");

            List<Integer> userIdList = model.getUserList();
            addAdmin(userIdList);
            List<User> userList = userRepository.getByIdIn(userIdList);
            if (userList.size() < model.getUserList().size())
                throw new Exception("使用者ID不存在");

            List<Integer> floorIdList = model.getFloorIdList(); //所選樓層資料(含多選或單選樓層)
            List<Floor> floorList = new ArrayList<>();
            List<FloorResponse> floorResponsesList = floorRepository.getAllResponseBy(); //全選樓層資料

            //全選/單選/樓層處理
            if (floorIdList == null || floorIdList.isEmpty()) {

                for (int i = 0; i < floorResponsesList.size(); i++) {
                    Floor floor = new Floor();
                    floor.setId(floorResponsesList.get(i).getId());
                    floorList.add(floor);
                }

            } else if (floorIdList != null || !floorIdList.isEmpty()) {

                for (Integer id : floorIdList) {
                    if (!floorRepository.findById(id).isPresent()) {
                        throw new Exception("無此樓層ID:" + id + "或系統異常，請洽管理員!");
                    }
                }

                for (int i = 0; i < floorIdList.size(); i++) {
                    Floor floor = new Floor();
                    floor.setId(floorIdList.get(i));
                    floorList.add(floor);
                }

            }

            group.setFloorList(floorList);
            //group.setGroupBuyingTypeId(type);
            group.setStoreList(storeList);
            group.setUserList(userList);
            groupRepository.save(group);
        }
    }

    @Override
    public void addGroup(AddGroupRequest request) throws Exception {
        logger.info("GroupService_addGroup GroupName:{}, Store:{}," +
                        "UserManager:{}, OpenDate:{}, StartTime:{}, EndTime:{}, Floors:{} ", request.getName(), request.getStoreList(),
                request.getUserList(), request.getOpenDate(), request.getStartTime(),
                request.getEndTime(), request.getTeamIdList());

        Group group = new Group();
        if (group.getId() == 0) {
            group.setName(request.getName());

            Timestamp openDate = new Timestamp(new Date().getTime());
            if (request.getOpenDate() != null)
                openDate = request.getOpenDate();
            group.setOpenDate(openDate);

            Timestamp startTime = new Timestamp(new Date().getTime());
            if (request.getStartTime() != null)
                startTime = request.getStartTime();
            group.setStartTime(startTime);

            Timestamp endTime = request.getEndTime();
            group.setEndTime(endTime);


            List<Store> storeList = storeRepository.getByIdIn(request.getStoreList());
            if (storeList.size() < request.getStoreList().size())
                throw new Exception("店家ID不存在");

            List<Integer> userIdList = request.getUserList();
            addAdmin(userIdList);
            List<User> userList = userRepository.getByIdIn(userIdList);
            if (userList.size() < request.getUserList().size())
                throw new Exception("使用者ID不存在");

            List<Integer> teamIdList = request.getTeamIdList(); //所選樓層資料(含多選或單選樓層)
            List<Team> teamList = new ArrayList<>();
            List<TeamResponse> teamResponseList = teamRepository.getAll(); //全選樓層資料

            //全選/單選/樓層處理
            if (teamIdList == null || teamIdList.isEmpty()) {

//                for (int i = 0; i < floorResponsesList.size(); i++) {
//                    Team team = new Team();
//                    floor.setId(floorResponsesList.get(i).getId());
//                    teamList.add(floor);
//                }
                for (TeamResponse teamResponse : teamResponseList) {
                    Team team = new Team();
                    team.setId(teamResponse.getId());
                    teamList.add(team);
                }


            } else if (teamIdList != null || !teamIdList.isEmpty()) {

                for (Integer id : teamIdList) {
                    if (!teamRepository.findById(id).isPresent()) {
                        throw new Exception("無此群組ID:" + id + "或系統異常，請洽管理員!");
                    }
                }

//                for (int i = 0; i < floorIdList.size(); i++) {
//                    Floor floor = new Floor();
//                    floor.setId(floorIdList.get(i));
//                    teamList.add(floor);
//                }

                for (Integer teamId : teamIdList) {
                    Team team = new Team();
                    team.setId(teamId);
                    teamList.add(team);
                }


            }

            group.setTeamList(teamList);
            //group.setGroupBuyingTypeId(type);
            group.setStoreList(storeList);
            group.setUserList(userList);
            groupRepository.save(group);
        }
    }

    private void addAdmin(List<Integer> userIdList) {
        int adminId = userRepository.findByUsername("admin").get().getId();
        if (!userIdList.contains(adminId)) {
            userIdList.add(adminId);
        }
    }

    @Override
    @Transactional
    public void updateGroup(GroupRequest model) throws Exception {

        logger.info("GroupService_updateGroup GroupId:{}, GroupName:{}, " +
                        " UserManager:{}, OpenDate:{}, StartTime:{}, EndTime:{}", model.getId(), model.getName(),
                model.getUserList(), model.getOpenDate(), model.getStartTime(), model.getEndTime());

        int userIdServer = ContextHolderHandler.getId();

        Group group = groupRepository.getById(model.getId());
        if (group == null)
            throw new Exception("團ID不存在");

        List<Integer> groupUser = group.getUserList().stream().map(User::getId).collect(Collectors.toList());
        if (!groupUser.contains(userIdServer))
            throw new Exception("本用戶沒有權限可以修改此團");

        List<Integer> userIdList = model.getUserList();
        addAdmin(userIdList);
        List<User> users = userRepository.getByIdIn(userIdList);
        if (users.size() < model.getUserList().size())
            throw new Exception("有使用者id不存在");

        group.setName(model.getName());
        group.setOpenDate(model.getOpenDate());
        group.setStartTime(model.getStartTime());
        group.setEndTime(model.getEndTime());
        group.setUserList(users);

        groupRepository.save(group);
    }

    @Override
    @Transactional
    public void deleteGroup(int id) throws Exception {

        logger.info("GroupService_deleteGroup GroupId:{}", id);

        //檢查團ID是否存在
        Optional<Group> group = groupRepository.findById(id);
        if (!group.isPresent())
            throw new Exception("團ID不存在");

        //檢查使用者是否為團管理員，是才可刪除
        int userIdServer = ContextHolderHandler.getId();
        List<Integer> groupUser = group.get().getUserList().stream().map(User::getId).collect(Collectors.toList());
        if (!groupUser.contains(userIdServer)) {
            throw new Exception("本用戶沒有權限可以刪除此團");
        }
        //刪除團不刪除資料改為設定isDeleted參數值為true
        group.get().setDeleted(true);
    }

    @Override
    @Transactional
    public void deleteGroupV2_1(int id) {
        logger.info("GroupService_deleteGroupV2_1 GroupId:{}", id);
        Group group = groupRepository.getGroupByIdAndIsDeleted(id, false);
        if (group == null)
            throw new IllegalArgumentException("此團不存在");

        int userIdServer = ContextHolderHandler.getId();
        User manager = userRepository.getUserById(userIdServer);
        ordServiceImpl.isGroupManager(group, userIdServer, "刪除此團");

        Timestamp nowTime = new Timestamp(new Date().getTime());
        List<UserPay> userPayList = userPayRepository.getUserPayByGroupId(group.getId());
        if (!group.isLocked() && group.getEndTime().after(nowTime)) {
            userPayRepository.deleteAll(userPayList);
            groupRepository.delete(group);
        } else {
            group.setDeleted(true);
            for (UserPay item : userPayList) {
                item.setDebit(ordServiceImpl.CHARGE);
                item.getPayDetailList().forEach(r -> r.setManagerDeleted(true));

                ordServiceImpl.refundOfStoredValue(group, manager, item.getUser(), item.getPay());
            }
        }
    }

    @Override
    public List<InitResponse> getInitData() {
        List<InitResponse> responses = new ArrayList<>();
        InitResponse response = new InitResponse();
        List<TagResponse> tags = tagRepository.findAllBy();
        response.setTags(tags);
        List<TimeStatusResponse> timeStatusList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TimeStatusResponse timeResponse = new TimeStatusResponse();
            timeResponse.setTimeStatus(i);
            switch (i) {
                case 0:
                    timeResponse.setStatus(TimeStatusEnum.Status.EARLY);
                    break;
                case 1:
                    timeResponse.setStatus(TimeStatusEnum.Status.TODAY);
                    break;
                case 2:
                    timeResponse.setStatus(TimeStatusEnum.Status.AFTER);
                    break;
                case 3:
                    timeResponse.setStatus(TimeStatusEnum.Status.FUTURE);
                    break;
                case 4:
                    timeResponse.setStatus(TimeStatusEnum.Status.TIMEOUT);
                    break;
            }
            timeStatusList.add(timeResponse);
        }
        response.setTimeTypeResponses(timeStatusList);
        responses.add(response);
        return responses;
    }

    @Override
    public List<GroupResponseV2_1> getGroupListV2_1(List<Integer> tagId, String keyword, List<Integer> teamId) {

        User user = userRepository.getUserById(ContextHolderHandler.getId());

        keyword = keyword == null || keyword.isEmpty() ? "%" : "%" + keyword + "%";
        tagId = tagId == null || (tagId.size() == 0) ? null : tagId;
        teamId = teamId == null || (teamId.size() == 0) ? null : teamId;

        //使用NativeSQL回傳的資料型態為List<Map<String,Object>>(詳細見Repository Method)
        List<Map<String, Object>> getGroupList = groupRepository.getGroupByNameLikeV2_1(tagId, keyword, teamId);
        List<Map<String, Object>> getTagList = tagRepository.getAllTags();
        List<Map<String, Object>> getTeamList = teamRepository.getAllGroupTeams();
        List<Map<String, Object>> getStoreList = storeRepository.getALlStoreByGroup();
        List<Map<String, Object>> getStorePicList = storeRepository.getAllGroupPic();
        List<Map<String, Object>> getGroupUserList = userRepository.getAllGroupUserList();

        List<GroupResponseV2_1> groupList = new ArrayList<>();

        //StoreReview 取值
        List<Integer> storeIdList = new ArrayList<>();
        getStoreList.forEach(r -> storeIdList.add((int) r.get("id")));
        List<Map<String, Object>> storeReviewList = storeReviewRepository.getReview(storeIdList);

        //GroupList 取值
        getGroupList.forEach(mGroup -> {

            List<Map<String, Object>> getGroupUserStatus = groupRepository.getOrderListByGroupAndUser((int) mGroup.get("id"), user.getId());

            GroupResponseV2_1 gr = GroupResponseV2_1.valueOf(mGroup);

            //List資料
            List<TeamResponse> teamList = new ArrayList<>();
            List<StoreResponseV2_1> storeList = new ArrayList<>();
            List<StorePictureResponse> storePicList = new ArrayList<>();
            List<UserNameOnlyResponse> groupUserList = new ArrayList<>();
            List<Tag> groupTagList = new ArrayList<>();
            Map<Integer, GroupUserStatusResponse> userStatusMap = new HashMap<>();

            //UserList取值
            getGroupUserList.forEach(mUser -> {
                if (mGroup.get("id").equals(mUser.get("groupId"))) {
                    UserNameOnlyResponse userNameOnlyResponse = UserNameOnlyResponse.valueOf(mUser);
                    groupUserList.add(userNameOnlyResponse);
                }
            });

            //TeamList取值
            getTeamList.forEach(mTeam -> {
                if (mGroup.get("id").equals(mTeam.get("groupId"))) {
                    TeamResponse tr = TeamResponse.valueOf(mTeam);
                    teamList.add(tr);
                }
            });

            //StoreList取值
            List<Tag> finalGroupTagList = groupTagList;
            getStoreList.forEach(mStore -> {
                if (mGroup.get("id").equals(mStore.get("groupId"))) {

                    StoreResponseV2_1 sr = StoreResponseV2_1.valueOf(mStore);

                    Map<Integer, String> tagMap = new TreeMap<>();

                    //StoreTag取值
                    getTagList.forEach(mTag -> {
                        if (mStore.get("id").equals(mTag.get("storeId"))) {
                            tagMap.put((int) mTag.get("id"), (String) mTag.get("name"));

                            Tag t = new Tag();
                            t.setId((int) mTag.get("id"));
                            t.setName((String) mTag.get("name"));

                            if (finalGroupTagList.stream().noneMatch(r -> r.getId() == t.getId()))
                                finalGroupTagList.add(t);
                        }
                    });

                    //StorePic取值
                    getStorePicList.forEach(mPic -> {
                        if (mStore.get("id").equals(mPic.get("storeId"))) {
                            StorePictureResponse spr = StorePictureResponse.valueOf(mPic);
                            storePicList.add(spr);
                        }
                    });

                    List<OrderDetailResponse> orderDetailResponseList = new ArrayList<>();
                    GroupUserStatusResponse item = new GroupUserStatusResponse();

                    //UserStatus取值
                    getGroupUserStatus.forEach(mUserStatus -> {

                        //UserOrderDetail取值
                        if ((int) mUserStatus.get("storeId") == (int) mStore.get("id")) {
                            OrderDetailResponse odr = OrderDetailResponse.valueOf(mUserStatus);
                            orderDetailResponseList.add(odr);
                        }
                    });

                    if (orderDetailResponseList.size() > 0)
                        item.setStatus(1);
                    else
                        item.setStatus(0);

                    item.setOrderDetailResponsesList(orderDetailResponseList);

                    //StoreReview 取值
                    if (storeReviewList.size() != 0) {
                        storeReviewList.forEach(mStoreReview -> {
                            if ((int) mStore.get("id") == ((int) mStoreReview.get("storeId"))) {
                                EvaluationResponse evaluationResponse = EvaluationResponse.valueOf(mStoreReview);
                                sr.setRank(evaluationResponse.getRank());
                                sr.setTotalUsers(evaluationResponse.getTotalUsers());
                            }
                        });
                    }

                    if (sr.getRank() == null && sr.getTotalUsers() == null) {
                        sr.setRank(0.0);
                        sr.setTotalUsers(0);
                    }

                    sr.setTagList(tagMap);
                    storeList.add(sr);
                    userStatusMap.put((int) mStore.get("id"), item);
                }
            });

            //GroupTag 排序
            groupTagList = groupTagList.stream().sorted(Comparator.comparing(Tag::getId)).collect(Collectors.toList());

            gr.setTagList(groupTagList);
            gr.setTeamList(teamList);
            gr.setStoreList(storeList);
            gr.setStorePicList(storePicList);
            gr.setUserList(groupUserList);
            gr.setUserStatus(userStatusMap);
            groupList.add(gr);
        });

        return groupList;
    }

    @Override
    public List<GroupTimeResponse> getGroupList(List<Integer> tagId, String keyword, List<Integer> floorId) {

        User user = userRepository.getUserById(ContextHolderHandler.getId());

        keyword = keyword == null || keyword.isEmpty() ? "%" : "%" + keyword + "%";
        tagId = tagId == null || (tagId.size() == 0) ? null : tagId;
        floorId = floorId == null || (floorId.size() == 0) ? null : floorId;

        //使用NativeSQL回傳的資料型態為List<Map<String,Object>>(詳細見Repository Method)
        List<Map<String, Object>> getGroupList = groupRepository.getGroupByNameLike(tagId, keyword, floorId);
        List<Map<String, Object>> getTagList = tagRepository.getAllTags();
        List<Map<String, Object>> getFloorList = floorRepository.getAllGroupFloor();
        List<Map<String, Object>> getStoreList = storeRepository.getALlStoreByGroup();
        List<Map<String, Object>> getStorePicList = storeRepository.getAllGroupPic();
        List<Map<String, Object>> getGroupUserList = userRepository.getAllGroupUserList();

        List<GroupResponse> groupList = new ArrayList<>();

        //GroupList 取值
        for (Map mGroup : getGroupList) {

            List<Map<String, Object>> getGroupUserStatus = groupRepository.getOrderListByGroupAndUser((int) mGroup.get("id"), user.getId());

            GroupResponse gr = new GroupResponse(
                    (int) mGroup.get("id"),
                    (String) mGroup.get("name"),
                    (Timestamp) mGroup.get("startTime"),
                    (Timestamp) mGroup.get("endTime"),
                    (boolean) mGroup.get("isLocked"),
                    (Timestamp) mGroup.get("openDate"));

            //List資料
            List<FloorResponse> floorList = new ArrayList<>();
            List<StoreResponse> storeList = new ArrayList<>();
            List<StorePictureResponse> storePicList = new ArrayList<>();
            List<UserNameOnlyResponse> groupUserList = new ArrayList<>();
            List<Tag> groupTagList = new ArrayList<>();
            Map<Integer, GroupUserStatusResponse> userStatusMap = new HashMap<>();

            //UserList取值
            for (Map mUser : getGroupUserList) {
                if (mGroup.get("id").equals(mUser.get("groupId"))) {
                    UserNameOnlyResponse unor = new UserNameOnlyResponse(
                            (int) mUser.get("id"),
                            (String) mUser.get("userName"),
                            (String) mUser.get("name"),
                            (String) mUser.get("englishName"));
                    groupUserList.add(unor);
                }
            }

            //FloorList取值
            for (Map mFloor : getFloorList) {
                if (mGroup.get("id").equals(mFloor.get("groupId"))) {
                    FloorResponse fr = new FloorResponse(
                            (int) mFloor.get("id"),
                            (String) mFloor.get("name")
                    );
                    floorList.add(fr);
                }
            }

            //StoreList取值
            for (Map mStore : getStoreList) {
                if (mGroup.get("id").equals(mStore.get("groupId"))) {

                    StoreResponse sr = new StoreResponse(
                            (int) mStore.get("id"),
                            (String) mStore.get("name"),
                            (int) mStore.get("groupId"));

                    Map<Integer, String> tagMap = new TreeMap<>();

                    //StoreTag取值
                    for (Map mTag : getTagList) {
                        if (mStore.get("id").equals(mTag.get("storeId"))) {
                            tagMap.put(
                                    (int) mTag.get("id"),
                                    (String) mTag.get("name"));

                            Tag t = new Tag();
                            t.setId((int) mTag.get("id"));
                            t.setName((String) mTag.get("name"));

                            if (groupTagList.stream().noneMatch(r -> r.getId() == t.getId()))
                                groupTagList.add(t);
                        }
                    }

                    //StorePic取值
                    for (Map mPic : getStorePicList) {
                        if (mStore.get("id").equals(mPic.get("storeId"))) {
                            StorePictureResponse spr = new StorePictureResponse(
                                    (int) mPic.get("id"),
                                    (String) mPic.get("url"),
                                    (String) mPic.get("name"));
                            storePicList.add(spr);
                        }
                    }

                    List<OrderDetailResponse> orderDetailResponseList = new ArrayList<>();
                    GroupUserStatusResponse item = new GroupUserStatusResponse();

                    //UserStatus取值
                    for (Map mUserStatus : getGroupUserStatus) {

                        //UserOrderDetail取值
                        if ((int) mUserStatus.get("storeId") == (int) mStore.get("id")) {
                            OrderDetailResponse odr = new OrderDetailResponse(
                                    (int) mUserStatus.get("productId"),
                                    (String) mUserStatus.get("productName"),
                                    (String) mUserStatus.get("optionName"),
                                    (BigInteger) mUserStatus.get("itemCount"),
                                    (int) mUserStatus.get("productPrice"),
                                    (int) mUserStatus.get("optionPrice"),
                                    (String) mUserStatus.get("productRemark"));
                            orderDetailResponseList.add(odr);
                        }
                    }

                    if (orderDetailResponseList.size() > 0)
                        item.setStatus(1);
                    else
                        item.setStatus(0);

                    item.setOrderDetailResponsesList(orderDetailResponseList);

                    sr.setTagList(tagMap);
                    storeList.add(sr);
                    userStatusMap.put((int) mStore.get("id"), item);
                }
            }

            //GroupTag 排序
            groupTagList = groupTagList.stream().sorted(Comparator.comparing(Tag::getId)).collect(Collectors.toList());

            gr.setTagList(groupTagList);
            gr.setFloorResponses(floorList);
            gr.setStoreList(storeList);
            gr.setStorePicList(storePicList);
            gr.setUserList(groupUserList);
            gr.setUserStatus(userStatusMap);
            groupList.add(gr);
        }

        List<GroupTimeResponse> groupTimeResponses = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            GroupTimeResponse timeResponse = new GroupTimeResponse();
            timeResponse.setTimeStatus(i);
            int timeStatus = i;

            timeResponse.setGroupResponses(groupList.stream()
                    .filter(g -> g.getTimeStatus() == timeStatus)
                    .sorted(Comparator.comparing(GroupResponse::getOpenDate)
                            .thenComparing(r -> r.getFloorResponses().size() > 0 ? r.getFloorResponses().get(0).getId() : 0)
                            .thenComparing(GroupResponse::getId))
                    .collect(Collectors.toList()));
            groupTimeResponses.add(timeResponse);
        }

        if (ContextHolderHandler.getPermissionsList(user).contains(PermissionEnum.Role.SUPER_MANAGER) ||
                ContextHolderHandler.getPermissionsList(user).contains(PermissionEnum.Role.MANAGER)) {
            GroupTimeResponse timeResponse = new GroupTimeResponse();
            timeResponse.setTimeStatus(4);
            timeResponse.setGroupResponses(groupList.stream().filter(g -> g.getTimeStatus() == 4).collect(Collectors.toList()));
            groupTimeResponses.add(timeResponse);
        }

        return groupTimeResponses;
    }

    @Override  //by SQL
    public Page<GroupResponse> getByTypeAndKeyword(String keyword, boolean isOwner, int pageNumber, int pageSize) {
        String kw = keyword == null || keyword.isEmpty() ? "%" : "%" + keyword + "%";
        if (pageNumber < 0) pageNumber = 0;
        if (pageSize < 1) pageSize = 1;
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);

        int userId = ContextHolderHandler.getId();
        Page<GroupResponse> groupsPage = groupRepository.getByTypeAndKeyword(userId, kw, isOwner, pageable);

        List<GroupResponse> groupResponseList = groupsPage.getContent();

        for (GroupResponse groupResponse : groupResponseList) {
            groupResponse.setUserStatus(getUserStatus(groupResponse.getId(), groupResponse.getStoreList(), userId));
        }

        return new PageImpl<>(groupResponseList, groupsPage.getPageable(), groupsPage.getTotalElements());
    }

    public Map<Integer, GroupUserStatusResponse> getUserStatus(int groupId, List<StoreResponse> storeList, int userId) {

        Map<Integer, GroupUserStatusResponse> userStatus = new HashMap<>();
        List<GroupUserOrderResponse> groupUserOrderResponseList = groupRepository.getUserOrderListByGroup(groupId, userId);

        if (!(storeList == null || storeList.size() == 0)) {
            for (StoreResponse store : storeList) {

                List<OrderDetailResponse> orderResponses = new ArrayList<>();
                GroupUserStatusResponse item = new GroupUserStatusResponse();

                for (GroupUserOrderResponse groupUserOrderResponse : groupUserOrderResponseList) {
                    if (groupUserOrderResponse.getStoreId() == store.getId()) {
                        orderResponses.add(groupUserOrderResponse.getOrderDetailResponse());
                    }
                }

                if (orderResponses.size() > 0) {
                    item.setStatus(1);
                } else {
                    item.setStatus(0);
                }

                item.setOrderDetailResponsesList(orderResponses);
                userStatus.put(store.getId(), item);
            }
        }
        return userStatus;
    }

    @Transactional
    public void lock(int groupId, Boolean status) throws Exception {

        logger.info("GroupService_lock GroupId:{}, Status:{}", groupId, status);

        Group group = groupRepository.getById(groupId);
        int userIdServer = ContextHolderHandler.getId();
        List<Integer> groupUser = group.getUserList().stream().map(r -> r.getId()).collect(Collectors.toList());
        if (!groupUser.contains(userIdServer)) {
            throw new Exception("本用戶沒有權限可以操作結單此團");
        }
        group.setLocked(status);
    }

    @Override
    @Transactional
    public void lockV2_1(int groupId, boolean status) {

        logger.info("GroupService_lockV2_1 GroupId:{}, Status:{}", groupId, status);
        Group group = groupRepository.getGroupByIdAndIsDeleted(groupId, false);
        if (group == null)
            throw new IllegalArgumentException("此團不存在");

        int userIdServer = ContextHolderHandler.getId();
        ordServiceImpl.isGroupManager(group, userIdServer, "操作結單此團");
        List<UserPay> userPayList = userPayRepository.getUserPayByGroupId(group.getId());
        Timestamp nowTime = new Timestamp(new Date().getTime());
        if (group.getEndTime().after(nowTime)) {
            group.setLocked(status);
            userPayList.forEach(r -> r.setDebit(status));
        }
    }

    public List<FloorResponse> getFloorItemData() {
        List<FloorResponse> allItem = floorRepository.getAllResponseBy();
        return allItem;
    }

    @Override
    @Transactional
    public GroupItemResponse getOne(int id) throws IdNotFoundException {
        Optional<Group> group = groupRepository.findById(id);
        if (!group.isPresent())
            throw new IdNotFoundException();

        GroupItemResponse result = GroupItemResponse.valueOf(group.get());
        addUserStatusInGroup(result);
        return result;
    }

    private void addUserStatusInGroup(GroupItemResponse groupItemResponse) {
        int groupId = groupItemResponse.getId();
        int userId = ContextHolderHandler.getId();
        List<StoreResponse> storeResponseList = groupItemResponse.getStoreList();
        Map<Integer, GroupUserStatusResponse> userStatus = new HashMap<>();
        List<GroupUserOrderResponse> groupUserOrderResponseList = groupRepository.getUserOrderListByGroup(groupId, userId);

        List<OrderPass> orderPassList = groupRepository.getById(groupId).getOrderPassList();
        if (!(storeResponseList == null || storeResponseList.size() == 0)) {
            for (StoreResponse storeResponse : storeResponseList) {
                List<OrderDetailResponse> orderResponses = new ArrayList<>();
                int countOrder = 0;
                for (GroupUserOrderResponse groupUserOrderResponse : groupUserOrderResponseList) {
                    if (groupUserOrderResponse.getStoreId() == storeResponse.getId()) {
                        orderResponses.add(groupUserOrderResponse.getOrderDetailResponse());
                        countOrder++;
                    }
                }
                GroupUserStatusResponse item = new GroupUserStatusResponse();
                userStatus.put(storeResponse.getId(), item);
                if (countOrder > 0) {
                    item.setStatus(1);
                    item.setOrderDetailResponsesList(orderResponses);
                } else {
                    int countPass = 0;
                    for (OrderPass orderPass : orderPassList) {
                        if ((orderPass.getStore().getId() == storeResponse.getId()) && (orderPass.getUser().getId() == userId))
                            countPass++;
                    }
                    if (countPass > 0) {
                        item.setStatus(-1);
                    } else {
                        item.setStatus(0);
                    }
                }
            }
        }
        groupItemResponse.setUserStatus(userStatus);
    }

    public List<GroupTeamUserResponse> getGroupManagerV2_1() {
        List<GroupTeamUserResponse> list;
        List<Integer> roleIdList = new ArrayList<>();
        roleIdList.add(1);
        roleIdList.add(2);
        list = roleRepository.getGroupManagerV2_1(roleIdList);
        return list;
    }

    @Deprecated
    public List<GroupFloorUserResponse> getGroupManager() {
        List<GroupFloorUserResponse> list;
        List<Integer> roleIdList = new ArrayList<>();
        roleIdList.add(1);
        roleIdList.add(2);
        list = roleRepository.getGroupManager(roleIdList);
        return list;
    }

}
