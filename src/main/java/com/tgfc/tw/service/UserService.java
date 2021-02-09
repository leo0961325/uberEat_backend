package com.tgfc.tw.service;

import com.tgfc.tw.entity.model.request.SearchUserRequest;
import com.tgfc.tw.entity.model.request.UserUpdateRequest;
import com.tgfc.tw.entity.model.response.RoleResponse;
import com.tgfc.tw.entity.model.response.user.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    UserDevResponseV2_1 getUserV2_1(int id);

    UserListResponseV2_1 getUserListV2_1(String keyword, List<Integer> teamIdList, int pageNumber, int pageSize);

    void updateV2_1(UserUpdateRequestV2_1 request);

    @Deprecated
    Page<UserListResponse> getUserList(SearchUserRequest model);

    @Deprecated
    UserDevResponse getUser(int id);

    @Deprecated
    void update(UserUpdateRequest request);

    void addUserByLdap(List<String> accounts);

    List<String> getPermission();

//    Boolean insertGroupPay(UserPayRequest model) throws Exception;
//
//    Boolean insertGroupPayTotal(UserPayTotalRequest model) throws Exception;


    List<RoleResponse> getRole();

    List<UserAllListResponse> getAllUserList();
}
