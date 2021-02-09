package com.tgfc.tw.controller;

import com.tgfc.tw.entity.cenum.PermissionEnum;
import com.tgfc.tw.entity.model.request.TagAddRequest;
import com.tgfc.tw.entity.model.request.TagUpdateRequest;
import com.tgfc.tw.entity.model.response.TagResponse;
import com.tgfc.tw.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/tag")
public class TagController {

    @Autowired
    TagService tagService;

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @PostMapping("add")
    public void addTag(@Valid @RequestBody TagAddRequest request) throws Exception  {
        tagService.addTag(request);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @DeleteMapping("delete")
    public void deleteTag(@RequestParam int id) throws Exception{
        tagService.deleteTag(id);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @PutMapping("update")
    public void updateTag(@RequestBody TagUpdateRequest request) throws Exception{
        tagService.updateTag(request);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping("list")
    public List<TagResponse> getTag(){
        return tagService.getTag();
    }
}
