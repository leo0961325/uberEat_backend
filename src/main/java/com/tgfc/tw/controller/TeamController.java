package com.tgfc.tw.controller;

import com.tgfc.tw.entity.cenum.PermissionEnum;
import com.tgfc.tw.entity.model.request.team.AddTeamRequest;
import com.tgfc.tw.entity.model.request.team.TeamUpdateRequest;
import com.tgfc.tw.entity.model.response.team.TeamGetResponse;
import com.tgfc.tw.entity.model.response.team.TeamListResponse;
import com.tgfc.tw.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@RequestMapping("api/team")
public class TeamController {
    @Autowired
    TeamService teamService;

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @PostMapping(value = "v2.1/add")
    public void add(@Valid @RequestBody AddTeamRequest addTeamRequest) throws Exception {
        teamService.add(addTeamRequest);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @PutMapping(value = "v2.1/update")
    public void update(@Valid @RequestBody TeamUpdateRequest teamUpdateRequest)throws Exception{
        teamService.update(teamUpdateRequest);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @DeleteMapping(value = "v2.1/remove")
    public void delete(@RequestParam int id)throws Exception{
        teamService.delete(id);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping(value = "v2.1/get")
    public TeamGetResponse getOne(@RequestParam int id){
        return teamService.getOne(id);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping(value = "v2.1/list")
    public Page<TeamGetResponse> getList(@RequestParam String keyword, @RequestParam int pageNumber, @RequestParam int pageSize){
        return teamService.getList(keyword,pageNumber,pageSize);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping(value = "v2.1/getAllTeam")
	public TeamListResponse getAllNotDeleted(){
		return teamService.getAllNotDeleted();
	}







}
