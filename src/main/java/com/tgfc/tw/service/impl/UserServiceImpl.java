package com.tgfc.tw.service.impl;

import com.tgfc.tw.entity.cenum.PermissionEnum;
import com.tgfc.tw.entity.model.po.*;
import com.tgfc.tw.entity.model.request.SearchUserRequest;
import com.tgfc.tw.entity.model.request.UserUpdateRequest;
import com.tgfc.tw.entity.model.response.FloorResponse;
import com.tgfc.tw.entity.model.response.RoleResponse;
import com.tgfc.tw.entity.model.response.team.TeamResponse;
import com.tgfc.tw.entity.model.response.user.*;
import com.tgfc.tw.entity.repository.*;
import com.tgfc.tw.security.ContextHolderHandler;
import com.tgfc.tw.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tw.tgfc.common.spring.ldap.model.LdapUser;
import tw.tgfc.common.spring.ldap.service.LDAPService;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    FloorRepository floorRepository;

    @Autowired
    LDAPService ldapService;

    @Autowired
    UserPayRepository userPayRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    TeamRepository teamRepository;

    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Override
    public UserDevResponseV2_1 getUserV2_1(int id) {

        Optional<User> optionalUser = userRepository.getById(id);

        if (!optionalUser.isPresent()) throw new IllegalArgumentException("id entity is not found");

        UserDevResponseV2_1 userDevResponseV2_1 = UserDevResponseV2_1.valueOfPermission(optionalUser.get());
        userDevResponseV2_1.setAllTeamList(teamRepository.getAll());

        return userDevResponseV2_1;
    }

    @Override
    public UserListResponseV2_1 getUserListV2_1(String keyword, List<Integer> teamIdList, int pageNumber, int pageSize) {
        keyword = keyword == null ? "%" : keyword + "%";
        teamIdList =  teamIdList == null || (teamIdList.size() == 0) ? null : teamIdList;

        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        AtomicInteger totalBalance = new AtomicInteger(0);
        UserListResponseV2_1 userListResponseV2_1 = new UserListResponseV2_1();
        Page<PageUserListResponse> userListPage = userRepository.findAllByKeywordV2_1(keyword, teamIdList, pageable);
        List<PageUserListResponse> userList = userRepository.getAll(keyword, teamIdList);

        userList.forEach(s -> {
            if(s.getId() != null){
                Integer balance = userPayRepository.getActualBalance(s.getId());
                Integer userBalance = userPayRepository.getBalance(s.getId());
                AtomicInteger finalBalance = new AtomicInteger(0);
                AtomicInteger finalUserBalance = new AtomicInteger(0);

                if(balance == null){
                    balance = 0;
                }
                if (userBalance == null) {
                    userBalance = 0;
                }
                finalUserBalance.addAndGet(userBalance);

                finalBalance.addAndGet(balance);

                userListPage.getContent().forEach(personal->{
                    if(s.getId().equals(personal.getId()))
                        personal.setDeposit(finalUserBalance.get());
                });
                totalBalance.addAndGet(finalBalance.get());
            }
        });

        userListResponseV2_1.setTotalDeposit(totalBalance.get());
        userListResponseV2_1.setUserListResponsePage(userListPage);
        return userListResponseV2_1;
    }

    @Override
    @Transactional
    public void updateV2_1(UserUpdateRequestV2_1 request) {
        logger.info("UserService update : updateId = {}", request.getId());

        Optional<User> userOptional = userRepository.getById(request.getId());
        if (!userOptional.isPresent())
            throw new IllegalArgumentException("id entity is not found");

        List<Team> teamList = teamRepository.getByIdList(request.getTeamIdList());

        if (teamList.size() == 0)
            throw new IllegalArgumentException("team entity is not found");

        User user = userOptional.get();
        if (ContextHolderHandler.getId() != 1 && ContextHolderHandler.getId() != user.getId()) {
            if (user.getRoleList().stream().filter(r -> r.getCode().equals(PermissionEnum.Role.MANAGER) || r.getCode().equals(PermissionEnum.Role.SUPER_MANAGER)).collect(Collectors.toList()).size() > 0) {
                throw new IllegalArgumentException("不能修改其他管理者資料");
            }
        }

        List<Integer> permissions = request.getPermissions();
        if (permissions != null && permissions.contains(PermissionEnum.ROLE_NORMAL.getPermissionOrder())) {
            permissions.remove((Integer) PermissionEnum.ROLE_NORMAL.getPermissionOrder());
        }

        if (permissions != null && permissions.size() > 0) {
            List<Role> listLole = roleRepository.getByIdIn(permissions);
            if (listLole != null && listLole.size() == 0) {
                throw new IllegalArgumentException("找不到權限ID");
            }
            if (listLole != null) {
                user.setRoleList(listLole);
            }
        } else if (permissions != null && permissions.size() == 0) {
            user.getRoleList().clear();
        }

        user.setName(request.getName());
        user.setEnglishName(request.getEnglishName());
        user.setTeamList(teamList);
        userRepository.save(user);

        logger.info("UserService update : update success");

    }

    @Override
    public UserDevResponse getUser(int id) {
        Optional<User> user = userRepository.getById(id);
        if (!user.isPresent()) throw new IllegalArgumentException("id entity is not found");

        UserDevResponse userResponse = UserDevResponse.valueOfPermission(user.get());

        List<FloorResponse> list = new ArrayList<>();
        floorRepository.findAll().forEach(r -> list.add(new FloorResponse(r)));
        userResponse.setAllFloors(list);
        return userResponse;
    }

    @Override
    public Page<UserListResponse> getUserList(SearchUserRequest model) {
        String keyword = model.getKeyword() == null ? "%" : "%" + model.getKeyword() + "%";
        Pageable pageable = PageRequest.of(model.getPageNumber() - 1, model.getPageSize());
        return userRepository.findAllByKeyword(keyword, pageable);
    }

    @Override
    @Transactional
    public void update(UserUpdateRequest request) {
        logger.info("UserService update : updateId = {}", request.getId());


        Optional<User> userOptional = userRepository.getById(request.getId());
        if (!userOptional.isPresent())
            throw new IllegalArgumentException("找不到會員ID");

        Optional<Floor> floorOptional = floorRepository.getById(request.getFloorId());
        if (!floorOptional.isPresent())
            throw new IllegalArgumentException("找不到樓層ID");

        User user = userOptional.get();
        if (ContextHolderHandler.getId() != 1 && ContextHolderHandler.getId() != user.getId()) {
            if (user.getRoleList().stream().filter(r -> r.getCode().equals(PermissionEnum.Role.MANAGER) || r.getCode().equals(PermissionEnum.Role.SUPER_MANAGER)).collect(Collectors.toList()).size() > 0) {
                throw new IllegalArgumentException("不能修改其他管理者資料");
            }
        }
        List<Integer> permissions = request.getPermissions();
        if (permissions != null && permissions.contains(PermissionEnum.ROLE_NORMAL.getPermissionOrder())) {
            int i = permissions.indexOf(PermissionEnum.ROLE_NORMAL.getPermissionOrder());
            permissions.remove(i);
        }

        if (permissions != null && permissions.size() > 0) {
            List<Role> listLole = roleRepository.getByIdIn(permissions);
            if (listLole != null && listLole.size() == 0) {
                throw new IllegalArgumentException("找不到權限ID");
            }
            if (listLole != null) {
                user.setRoleList(listLole);
            }
        } else if (permissions != null && permissions.size() == 0) {
            user.getRoleList().clear();
        }

        user.setName(request.getName());
        user.setEnglishName(request.getEnglishName());
        user.setFloor(floorOptional.get());
        userRepository.save(user);

//        ContextHolderHandler.setUserResponse(UserResponse.valueOfPermission(user));
        logger.info("UserService update : update success");
    }

    @Override
    @Transactional
    public void addUserByLdap(List<String> accounts) {
        logger.info("UserService addUserByLdap Call ");

        Iterable<LdapUser> ldapUsers = ldapService.totalList();
        List<LdapUser> ldapUserList = new ArrayList<>();
        for (LdapUser ldapUser : ldapUsers) {
            if (accounts.contains(ldapUser.getAccount())) {
                ldapUserList.add(ldapUser);
            }
        }
        addByLdap(ldapUserList);
    }

    private void addByLdap(List<LdapUser> ldapUsers) {
        Optional<Floor> floor = floorRepository.findById(1);
        List<User> users = userRepository.findAll();
        for (LdapUser ldapUser : ldapUsers) {
            Optional<User> oUser = users.stream()
                    .filter(r -> r.getUsername() != null)
                    .filter(r -> !r.getUsername().isEmpty())
                    .filter(r -> r.getUsername().equals(ldapUser.getAccount())).findFirst();
            if (!oUser.isPresent()) {
                User user = new User();
                user.setUsername(ldapUser.getAccount());
                user.setName(ldapUser.getName());
                user.setEnglishName(ldapUser.getName());
                user.setEmail(ldapUser.getEmail());
                user.setFloor(floor.isPresent() ? floor.get() : null);
                user.setPassword(encoder.encode("123456"));
                userRepository.save(user);
            }
        }
    }

    @Override
    public List<String> getPermission() {
        return ContextHolderHandler.getUserPermission();
    }


//    @Override
//    @Transactional
//    public Boolean insertGroupPay(UserPayRequest model) throws Exception {
//        UserPay userPay = null;
//        Group group = groupRepository.getById(model.getGroupId());
//        if (group == null) {
//            throw new Exception("GroupId不存在");
//        }
//
//        List<Map<String, Object>> storeIdcheck = null;
//        try {
//            storeIdcheck = groupRepository.getStoreIdBy(model.getGroupId(), model.getUserId());
//        } catch (Exception e) {
//            throw new Exception("出現無法預期的意外");
//        }
//        List<Integer> integers = new ArrayList<>();
//        storeIdcheck.forEach(m -> integers.add((int) m.get("store_id")));
//        model.getStoreId().retainAll(integers);
//        if (model.getStoreId().size() < 1)
//            throw new Exception("沒有storeId訂購的任何餐點");
//        for (Map m : storeIdcheck) {
//            for (int storeId : model.getStoreId()) {
//                if ((int) m.get("store_id") == storeId) {
//                    model.setPay(model.getPay() + (int) (m.get("pay") == null ? 0 : m.get("pay")));
//                }
//            }
//
//        }
//
//        for (int i = 0; i < model.getStoreId().size(); i++) {
//            int storeId = model.getStoreId().get(i);
//            try {
//                userPay = userPayRepository.getByGroupIdaAndUserId(model.getGroupId(), model.getUserId(), storeId) == null ? new UserPay()
//                        : userPayRepository.getByGroupIdaAndUserId(model.getGroupId(), model.getUserId(), storeId);
//            } catch (Exception e) {
//                throw new Exception("出現無法預期的意外");
//            }
//
//            if (!group.getOrderList().stream().anyMatch(r -> r.getUserList().stream().anyMatch(a -> a.getId() == model.getUserId()))) {
//                throw new Exception("UserID不存在");
//            }
//            userPay.setUser(userRepository.getUserById(model.getUserId()));
//            userPay.setGroup(group);
//            userPay.setStore(group.getStoreList().stream().filter(a -> a.getId() == storeId).findFirst().get());
//
//            int totalPrice;
//            try {
//                totalPrice = groupRepository.getUserStoreTotalPris(model.getGroupId(), storeId, model.getUserId());
//            } catch (Exception e) {
//                throw new Exception("沒有訂購任何餐點");
//            }
//            userPay.setTotalPrice(totalPrice);
//            userPay.setStatus(false);
//            if (i + 1 == model.getStoreId().size()) {
//                userPay.setPay(model.getPay());
//            } else if (model.getPay() > totalPrice) {
//                userPay.setPay(totalPrice);
//                model.setPay(model.getPay() - totalPrice);
//            } else {
//                userPay.setPay(model.getPay());
//                model.setPay(0);
//            }
//
//            try {
//                userPayRepository.save(userPay);
//            } catch (Exception e) {
//                throw new Exception("出現無法預期的意外");
//            }
//        }
//
//        return true;
//    }


//    @Override
//    @Transactional
//    public Boolean insertGroupPayTotal(UserPayTotalRequest model) throws Exception {
//        UserPay userPay = null;
//        Group group = groupRepository.getById(model.getGroupId());
//        if (group == null) {
//            throw new Exception("GroupId不存在");
//        }
//        for (Integer StoreId : model.getStoreId()) {
//            if (!group.getStoreList().stream().anyMatch(r -> r.getId() == StoreId)) {
//                throw new Exception("StoreId不存在");
//            }
//            try {
//                userPay = userPayRepository.getByGroupIdaAndUserId(model.getGroupId(), model.getUserId(), StoreId) == null ? new UserPay()
//                        : userPayRepository.getByGroupIdaAndUserId(model.getGroupId(), model.getUserId(), StoreId);
//            } catch (Exception e) {
//                throw new Exception("出現無法預期的意外");
//            }
//
//            if (!group.getOrderList().stream().anyMatch(r -> r.getUserList().stream().anyMatch(a -> a.getId() == model.getUserId()))) {
//                throw new Exception("UserID不存在");
//            }
//
//            userPay.setUser(userRepository.getUserById(model.getUserId()));
//            userPay.setGroup(group);
//            userPay.setStore(group.getStoreList().stream().filter(a -> a.getId() == StoreId).findFirst().get());
//            try {
//                userPay.setTotalPrice(groupRepository.getUserStoreTotalPris(model.getGroupId(), StoreId, model.getUserId()));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            userPay.setPay(userPay.getTotalPrice());
//            userPay.setStatus(true);
//
//            try {
//                userPayRepository.save(userPay);
//
//            } catch (Exception e) {
//                throw new Exception("出現無法預期的意外");
//            }
//        }
//
//        return true;
//    }

    @Override
    public List<RoleResponse> getRole() {
        RoleResponse roleResponse = new RoleResponse();
        List<RoleResponse> orginalList = roleRepository.findList();

        //給于一般使用者權限資料
        roleResponse.setId(PermissionEnum.ROLE_NORMAL.getPermissionOrder());
        roleResponse.setCode(PermissionEnum.Role.NORMAL);
        roleResponse.setName(PermissionEnum.RoleUserName.NORMAL_USER_NAME);
        orginalList.add(roleResponse);

        return orginalList;
    }

    @Override
    public List<UserAllListResponse> getAllUserList() {
        List<UserAllListResponse> userAllListResponseList = new ArrayList<>();
        List<Map<String, Object>> item = userRepository.getAllUser();
        for (int i = 0; i < item.size(); i++) {
            UserAllListResponse userAllListResponse = new UserAllListResponse((int) item.get(i).get("userId"), (String) item.get(i).get("name")
                    , (String) item.get(i).get("englishName"));
            List<TeamResponse> teamResponseList = new ArrayList<>();
            try {
                List<Map<String, Object>> teamItem = teamRepository.getTeamByUserId((int) item.get(i).get("userId"));
                for (int t = 0; t < teamItem.size(); t++) {
                    TeamResponse teamResponse = new TeamResponse((int) teamItem.get(t).get("teamID"), (String) teamItem.get(t).get("teamName"));
                    teamResponseList.add(teamResponse);
                    userAllListResponse.setTeam(teamResponseList);
                }
            } catch (Exception e) {
            }
            userAllListResponseList.add(userAllListResponse);
        }
        return userAllListResponseList;
    }

}