package com.tgfc.tw.service;

import com.tgfc.tw.entity.model.request.AddGroupRequest;
import com.tgfc.tw.entity.model.request.GroupRequest;
import com.tgfc.tw.entity.model.request.GroupWithoutIdRequest;
import com.tgfc.tw.entity.model.response.*;
import com.tgfc.tw.service.exception.IdNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface GroupService {
    List<FloorResponse> getFloorItemData();

    @Deprecated
    void insertGroup(GroupWithoutIdRequest model) throws Exception;

    void addGroup(AddGroupRequest request) throws Exception;

    void updateGroup(GroupRequest model) throws Exception;

    @Deprecated
    void deleteGroup(int id) throws Exception;

    void deleteGroupV2_1(int id);

    Page<GroupResponse> getByTypeAndKeyword(String keyword, boolean isOwner, int pageNumber, int pageSize);

    List<GroupResponseV2_1> getGroupListV2_1(List<Integer> tagId, String keyword, List<Integer> floorId);

    @Deprecated
    List<GroupTimeResponse> getGroupList(List<Integer> tagId, String keyword, List<Integer> floorId);

    List<InitResponse> getInitData();

    @Deprecated
    void lock(int groupId, Boolean status) throws Exception;

    void lockV2_1(int groupId, boolean status);

    GroupItemResponse getOne(int id) throws IdNotFoundException;

    Map<Integer, GroupUserStatusResponse> getUserStatus(int groupId, List<StoreResponse> storeList, int userId);

    List<GroupTeamUserResponse> getGroupManagerV2_1();

    @Deprecated
    List<GroupFloorUserResponse> getGroupManager();
}
