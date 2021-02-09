package com.tgfc.tw.service;

import com.tgfc.tw.entity.model.request.team.AddTeamRequest;
import com.tgfc.tw.entity.model.request.team.SearchTeamRequest;
import com.tgfc.tw.entity.model.request.team.TeamUpdateRequest;
import com.tgfc.tw.entity.model.response.team.TeamGetResponse;
import com.tgfc.tw.entity.model.response.team.TeamListResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TeamService {
    void add(AddTeamRequest addTeamRequest) throws Exception;
    void update(TeamUpdateRequest teamUpdateRequest) throws Exception;
    void delete(int id) throws Exception;
    TeamGetResponse getOne(int id);
    Page<TeamGetResponse> getList(String keyword, int pageNumber, int pageSize);
    TeamListResponse getAllNotDeleted();
}
