package com.tgfc.tw.service.impl;

import com.tgfc.tw.entity.model.po.Team;
import com.tgfc.tw.entity.model.po.User;
import com.tgfc.tw.entity.model.request.team.AddTeamRequest;
import com.tgfc.tw.entity.model.request.team.TeamUpdateRequest;
import com.tgfc.tw.entity.model.response.team.TeamGetResponse;
import com.tgfc.tw.entity.model.response.team.TeamListResponse;
import com.tgfc.tw.entity.model.response.user.UserNameOnlyResponse;
import com.tgfc.tw.entity.repository.GroupRepository;
import com.tgfc.tw.entity.repository.TeamRepository;
import com.tgfc.tw.entity.repository.UserRepository;
import com.tgfc.tw.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

@Service
public class TeamServiceImpl implements TeamService {
    @Autowired
    TeamRepository teamRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupRepository groupRepository;

    @Transactional
    @Override
    public void add(AddTeamRequest addTeamRequest) throws Exception {
        String name = addTeamRequest.getName().trim();
        List<Team> byNameList = teamRepository.findByName(name);
        for (Team t : byNameList) {
            if (name.equals(t.getName()) && !t.isDeleted())
                throw new Exception("群組名稱已存在");
        }
        if (name.isEmpty())
            throw new Exception("名稱必填");
        Team team = new Team();
        team.setName(name);
        team.setDeleted(false);

        List<Integer> userList = addTeamRequest.getUserList();
        List<User> users = new ArrayList<>();
        for (Integer i : userList) {
            if (userRepository.findById(i).isPresent()) {
                User u = userRepository.getOne(i);
                users.add(u);
            } else {
                throw new Exception("無此帳號");
            }
        }
        team.setUserList(users);
        teamRepository.save(team);
        userRepository.saveAll(users);
    }

    @Transactional
    @Override
    public void update(TeamUpdateRequest teamUpdateRequest) throws Exception {
        String name = teamUpdateRequest.getName().trim();
        Optional<Team> team = teamRepository.findById(teamUpdateRequest.getId());
        if (name.isEmpty())
            throw new Exception("名稱必填");
        List<Team> byNameList = teamRepository.findByName(name);
        for (Team t : byNameList) {
            if (teamUpdateRequest.getId() != t.getId())
                if (name.equals(t.getName()) && !t.isDeleted())
                    throw new Exception("群組名稱已存在");
        }
        List<Integer> userIdList = teamUpdateRequest.getUserList();
        List<User> userList = new ArrayList<>();
        for (int i = 0; i < userIdList.size(); i++) {
            if (userRepository.findById(userIdList.get(i)).isPresent()) {
                User user1 = userRepository.getOne(userIdList.get(i));
                userList.add(user1);
            } else {
                throw new Exception("無此帳號");
            }
        }
        Team teamReal = team.get();
        teamReal.setName(name);
        teamReal.setUserList(userList);
        teamRepository.save(teamReal);
        userRepository.saveAll(userList);
    }

    @Transactional
    @Override
    public void delete(int id) throws Exception {
        Optional<Team> team = teamRepository.findById(id);
        if (!team.isPresent())
            throw new Exception("團體不存在");
        List<Map<String, Object>> groupItem = groupRepository.getAllTeamGroupList(id);
        if (!groupItem.isEmpty()) {
            throw new Exception("團體進行中，無法刪除群組。");
        } else {
            Team team1 = team.get();
            team1.setDeleted(true);
            teamRepository.save(team1);
            teamRepository.delUserTeam(id);
        }
    }

    @Override
    public TeamGetResponse getOne(int id) {
        Optional<Team> team = teamRepository.findById(id);
        TeamGetResponse teamGetResponse = TeamGetResponse.valueOf(team.get());
        List<User> userList = team.get().getUserList();
        List<UserNameOnlyResponse> userNameOnlyResponseList = new ArrayList<>();
        for (User user : userList) {
            UserNameOnlyResponse userNameOnlyResponse = UserNameOnlyResponse.value(user);
            userNameOnlyResponseList.add(userNameOnlyResponse);
        }
        teamGetResponse.setUserNameOnlyResponseList(userNameOnlyResponseList);
        return teamGetResponse;
    }

    @Override
    public Page<TeamGetResponse> getList(String keyword, int pageNumber, int pageSize) {
        List<TeamGetResponse> teamGetResponseList = new ArrayList<>();
        String key = keyword.isEmpty() ? "%" : "%" + keyword + "%";
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<TeamGetResponse> pageItem = teamRepository.getTeamByKeyword(key, pageable);
        List<TeamGetResponse> item = pageItem.getContent();
        for (TeamGetResponse response : item) {
            TeamGetResponse teamGetResponse = new TeamGetResponse(response.getId(), response.getTeamName());
            List<UserNameOnlyResponse> userNameOnlyResponseList = new ArrayList<>();
            try {
                List<Map<String, Object>> userItem = userRepository.getUserByTeamId(response.getId());
                for (Map<String, Object> map : userItem) {
                    UserNameOnlyResponse userNameOnlyResponse = UserNameOnlyResponse.valueOf(map);
                    userNameOnlyResponseList.add(userNameOnlyResponse);
                    teamGetResponse.setUserNameOnlyResponseList(userNameOnlyResponseList);
                }
            } catch (Exception e) {
            }
            teamGetResponseList.add(teamGetResponse);
        }
        Page<TeamGetResponse> team = new PageImpl<>(teamGetResponseList, pageable, pageItem.getTotalElements());
        return team;
    }

    @Override
    public TeamListResponse getAllNotDeleted() {
        TeamListResponse response = new TeamListResponse();
        List<com.tgfc.tw.entity.model.response.team.TeamResponse> allNotDeleted = teamRepository.getAllNotDeleted();
        response.setTeamListRsponse(allNotDeleted);
        return response;
    }
}
