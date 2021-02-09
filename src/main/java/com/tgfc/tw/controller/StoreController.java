package com.tgfc.tw.controller;

import com.tgfc.tw.entity.cenum.PermissionEnum;
import com.tgfc.tw.entity.model.request.store.StoreRequest;
import com.tgfc.tw.entity.model.request.store.StoreRequestV2_1;
import com.tgfc.tw.entity.model.request.store.StoreUpdateRequest;
import com.tgfc.tw.entity.model.response.*;
import com.tgfc.tw.service.StoreInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RequestMapping("api/store")
@RestController
public class StoreController {

    @Autowired
    StoreInfoService storeInfoService;

    /*******
     *  新增店家
     *
     * @param/ 店家資料
     * @return/ 是否成功
     */
    @Deprecated
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @PostMapping("add")
    public StoreAddResponse addStore(@RequestBody StoreRequest request) throws Exception {
        return storeInfoService.addStore(request);
    }

    //新增店家 V2.1
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @PostMapping("v2.1/add")
    public StoreAddResponseV2_1 addStoreV2_1(@RequestBody StoreRequestV2_1 request) {
        return storeInfoService.addStoreV2_1(request);
    }

    /*******
     *  取得店家資料(單筆)
     *
     * @param/ 店家ID
     * @return/ 店家資料
     */
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping("get")
    public StoreGetOneResponse getStore(@RequestParam int id) throws Exception {

        return storeInfoService.getStore(id);
    }


    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping("v2.1/get")
    public StoreGetOneResponse getStoreV2_1(@RequestParam int id) throws Exception {

        return storeInfoService.getStoreV2_1(id);
    }


    /*******
     *  刪除店家
     *
     * @param/ 要刪除的店家ID
     * @return/ 刪除是否成功
     */
    @Deprecated
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @DeleteMapping("delete")
    public void removeStore(@RequestParam int id) throws Exception {

        storeInfoService.removeStore(id);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @DeleteMapping ("v2.1/delete")
    public void newRemoveStore(@RequestParam int id) throws Exception {

        storeInfoService.newRemoveStore(id);
    }


    /*******
     *  修改店家
     *
     * @param/ 修改資料
     * @return/ 修改是否成功
     */
    @Deprecated
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @PutMapping("update")
    public void updateStore(@RequestBody StoreUpdateRequest request) throws Exception {
        storeInfoService.updateStore(request);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER})
    @PutMapping("v2.1/update")
    public void newUpdateStore(@RequestBody StoreUpdateRequest request) throws Exception {
        storeInfoService.newUpdateStore(request);
    }

    @Deprecated
    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping("list")
    public Page<StoreInfoResponse> getAllStore(@RequestParam String storeName, @RequestParam(required = false) List<Integer> tagIdList, @RequestParam int pageNumber, @RequestParam int pageSize) {
        String keyword = storeName != null ? "%" + storeName + "%" : "%";
        pageNumber = (pageNumber >= 1) ? (pageNumber - 1) : 0;
        return storeInfoService.getStoreAllOrByName(keyword, tagIdList, pageSize, pageNumber);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @GetMapping("v2.1/list")
    public Page<NewStoreInfoResponse> getStoreList(@RequestParam String storeName, @RequestParam(required = false) List<Integer> tagIdList, @RequestParam int pageNumber, @RequestParam int pageSize) {
        String keyword = storeName != null ? "%" + storeName + "%" : "%";
        pageNumber = (pageNumber >= 1) ? (pageNumber - 1) : 0;
        return storeInfoService.getStoreList(keyword, tagIdList, pageSize, pageNumber);
    }

    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
    @PostMapping("review")
    public void reviewStore(@RequestParam("storeId") int storeId, @RequestParam("review") int review) throws Exception {
        storeInfoService.reviewStore(storeId, review);
    }

//    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
//    @PostMapping("comment")
//    public void commentStore(@RequestBody StoreCommentRequest commentRequest) throws Exception {
//        storeInfoService.commentStore(commentRequest);
//
//    }

//    @RolesAllowed({PermissionEnum.Role.SUPER_MANAGER, PermissionEnum.Role.MANAGER, PermissionEnum.Role.NORMAL})
//    @DeleteMapping("deleteComment")
//    public void deleteComment(@RequestParam int id) throws Exception {
//        storeInfoService.deleteComment(id);
//    }
}
