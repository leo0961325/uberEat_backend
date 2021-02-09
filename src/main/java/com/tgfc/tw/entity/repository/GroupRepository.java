package com.tgfc.tw.entity.repository;

import com.tgfc.tw.entity.model.po.Group;
import com.tgfc.tw.entity.model.response.GroupResponse;
import com.tgfc.tw.entity.model.response.GroupUserOrderResponse;
import com.tgfc.tw.entity.model.response.StoreResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface GroupRepository extends CrudRepository<Group, Integer> {

    Group getById(int id);

    @Query(value = "SELECT g.id AS groupId, g.name AS groupName, g.start_time AS startTime, " +
            "g.end_time AS endTime, g.is_locked AS isLocked, gs.store_id AS storeId, " +
            "gu.user_id AS userId " +
            "FROM groups g " +
            "LEFT JOIN group_store gs ON gs.group_id = g.id " +
            "LEFT JOIN group_user gu ON gu.group_id = g.id " +
            "WHERE g.id = :groupId", nativeQuery = true)
    List<Map<String, Object>> getGroupById(@Param("groupId") int groupId);

    @Query(value = "SELECT new com.tgfc.tw.entity.model.response.GroupResponse(g)" +
            " FROM Group g" +
            " LEFT JOIN g.userList gu" +
            " LEFT JOIN g.orderList oh" +
            " LEFT JOIN oh.userList ohu ON ohu.id = :userId" +
            " WHERE g.name LIKE :keyword" +
            " AND (:isOwner = true OR  g.endTime >= CURRENT_DATE )" +
            " AND ((:isOwner = true AND gu.id = :userId) OR :isOwner = false) " +
            " AND g.isDeleted = false " +
            " GROUP BY g.id " +
            " ORDER BY CASE WHEN g.endTime IS NULL THEN 1 ELSE 0 END ASC," +
            "CASE WHEN CURRENT_DATE > g.endTime THEN 4 " +
            "WHEN g.endTime < CURRENT_TIMESTAMP OR g.isLocked = 1 THEN 0 " +
            "WHEN g.startTime < CURRENT_TIMESTAMP AND g.endTime > CURRENT_TIMESTAMP AND g.isLocked=0 THEN 1 " +
            "WHEN g.startTime > CURRENT_TIMESTAMP OR g.isLocked = 0 THEN 2 " +
            "ELSE 3 END ASC, " +
            "g.endTime ASC ")
    Page<GroupResponse> getByTypeAndKeyword(@Param("userId") int userId,
                                            @Param("keyword") String keyword, @Param("isOwner") boolean isOwner, Pageable pageable);


    @Query(nativeQuery = true, value = "" +
            "SELECT g.id as id, g.name as name, g.start_time as startTime, g.end_time as endTime, is_locked as isLocked, open_date as openDate " +
            "FROM groups g LEFT JOIN group_store gs ON g.id = gs.group_id " +
            "LEFT JOIN store s ON s.id = gs.store_id " +
            "LEFT JOIN store_tag st ON st.store_id = s.id " +
            "LEFT JOIN tag t ON t.id = st.tag_id " +
            "LEFT JOIN group_team gt ON gt.group_id = g.id LEFT JOIN team tm ON gt.team_id = tm.id " +
            "WHERE g.name LIKE :keyword AND g.is_deleted = FALSE AND (COALESCE(:tagId) IS NULL OR t.id IN (:tagId)) " +
            "AND (COALESCE(:teamId) IS NULL OR tm.id IN (:teamId)) GROUP BY g.id ")
    List<Map<String, Object>> getGroupByNameLikeV2_1(@Param("tagId") List<Integer> tagId, @Param("keyword") String keyword, @Param("teamId") List<Integer> teamId);

    @Deprecated
    @Query(nativeQuery = true, value = "" +
            "SELECT g.id as id, g.name as name, g.start_time as startTime, g.end_time as endTime, is_locked as isLocked, open_date as openDate " +
            "FROM groups g LEFT JOIN group_store gs ON g.id = gs.group_id " +
            "LEFT JOIN store s ON s.id = gs.store_id " +
            "LEFT JOIN store_tag st ON st.store_id = s.id " +
            "LEFT JOIN tag t ON t.id = st.tag_id " +
            "LEFT JOIN group_floor gf ON gf.group_id = g.id LEFT JOIN floor f ON gf.floor_id = f.id " +
            "WHERE g.name LIKE :keyword AND g.is_deleted = FALSE AND (COALESCE(:tagId) IS NULL OR t.id IN (:tagId)) " +
            "AND (COALESCE(:floorId) IS NULL OR f.id IN (:floorId)) GROUP BY g.id ")
    List<Map<String, Object>> getGroupByNameLike(@Param("tagId") List<Integer> tagId, @Param("keyword") String keyword, @Param("floorId") List<Integer> floorId);

    @Query(nativeQuery = true, value = " " +
            "SELECT " +
            "   o.store_id AS storeId, " +
            "   p.id AS productId, " +
            "   p.name AS productName, " +
            "   p.price AS productPrice, " +
            "   p.remark AS productRemark, " +
            "   opt.name AS optionName, " +
            "   opt.price AS optionPrice, " +
            "   COUNT( opt.id ) AS itemCount " +
            "FROM " +
            "   order_ o " +
            "   JOIN order_user ou ON ou.order_id = o.id " +
            "   JOIN option_ opt ON opt.id = o.option_id " +
            "   JOIN product p ON p.id = opt.product_id " +
            "WHERE " +
            "   ou.user_id = :userId " +
            "   AND o.group_id = :groupId " +
            "GROUP BY " +
            "   p.name, opt.id ")
    List<Map<String, Object>> getOrderListByGroupAndUser(@Param("groupId") int groupId, @Param("userId") int userId);

    @Query(value = "SELECT new com.tgfc.tw.entity.model.response.GroupUserOrderResponse(g.id, gs.id, ol, op, " +
            " COUNT(ohu.id) )" +
            " FROM Group g" +
            " LEFT JOIN g.storeList gs" +
            " LEFT JOIN gs.productList ol" +
            " LEFT JOIN ol.optionList op" +
            " LEFT JOIN op.orderList oh" +
            " INNER JOIN oh.userList ohu ON ohu.id = :userId" +
            " WHERE g.id = :groupId" +
            " AND oh.group.id = :groupId" +
            " GROUP BY oh.id" +
            " ORDER BY ol.name, op.id ")
    List<GroupUserOrderResponse> getUserOrderListByGroup(@Param("groupId") int groupId, @Param("userId") int userId);

    @Query(value = "SELECT g FROM Group g LEFT JOIN g.storeList gs WHERE gs.id = :id ")
    List<Group> storeUsing(@Param("id") int id);

    @Query(value = "SELECT new com.tgfc.tw.entity.model.response.StoreResponse(s.id,s.name) FROM Group g " +
            "LEFT JOIN g.storeList s " +
            "WHERE g.id = ?1 ")
    List<StoreResponse> getStoreNameList(int groupId);

    @Query(value = "SELECT SUM(product.price)+SUM(option_.price) FROM groups  " +
            "JOIN order_ ON groups.id=order_.group_id " +
            "JOIN option_ ON order_.option_id=option_.id " +
            "JOIN product ON option_.product_id=product.id " +
            "JOIN order_user ON order_.id=order_user.order_id " +
            "WHERE groups.id=:groupId AND product.store_id=:storeId AND order_user.user_id=:userId ", nativeQuery = true)
    int getUserStoreTotalPris(@Param("groupId") int groupId, @Param("storeId") int storeId, @Param("userId") int userId);

//    @Query(value = "SELECT product.store_id FROM groups  " +
//            "JOIN order_ ON groups.id=order_.group_id  " +
//            "JOIN option_ ON order_.option_id=option_.id " +
//            "JOIN product ON option_.product_id=product.id " +
//            "JOIN order_user ON order_.id=order_user.order_id " +
//            "WHERE groups.id=:groupId AND order_user.user_id=:userId " +
//            "GROUP BY product.store_id ", nativeQuery = true)
//    List<Integer> getStoreIdBy(@Param("groupId") int groupId, @Param("userId") int userId);

    @Query(value = " SELECT product.store_id AS store_id,user_pay.pay AS pay FROM groups  " +
            " JOIN order_ ON groups.id=order_.group_id  " +
            " JOIN option_ ON order_.option_id=option_.id " +
            " JOIN product ON option_.product_id=product.id " +
            " JOIN order_user ON order_.id=order_user.order_id " +
            " LEFT JOIN  user_pay ON order_user.user_id=user_pay.user_id AND product.store_id=user_pay.store_id AND groups.id=user_pay.group_id " +
            " WHERE groups.id=:groupId AND order_user.user_id=:userId AND (user_pay.pay_status=false or user_pay.pay_status is null) " +
            " GROUP BY product.store_id", nativeQuery = true)
    List<Map<String, Object>> getStoreIdBy(@Param("groupId") int groupId, @Param("userId") int userId);

    @Query(value = "SELECT g.id AS id, g.name AS name,g.end_time AS endTime,g.is_locked AS isLocked,gt.team_id =:teamId AS teamId from group_team gt " +
            "JOIN groups g ON gt.group_id = g.id WHERE team_id =:teamId AND is_locked = 0 AND end_time>NOW()", nativeQuery = true)
    List<Map<String, Object>> getAllTeamGroupList(@Param("teamId") int teamId);

    @Query(value = "SELECT g FROM Group g WHERE g.endTime <= CURRENT_TIMESTAMP AND g.isLocked = false AND g.isDeleted = false ")
    List<Group> getExpireGroups();

    @Query(value = "SELECT g.id FROM Group g WHERE g.isLocked = true AND g.isDeleted = false ")
    List<Integer> getExpireGroupsId();

    @Query(value = "SELECT * FROM groups g WHERE g.id = :groupId AND g.is_deleted = :isDeleted"
            , nativeQuery = true)
    Group getGroupByIdAndIsDeleted(@Param("groupId") int groupId, @Param("isDeleted") boolean isDeleted);

    @Query(value = "SELECT * FROM groups g WHERE g.is_deleted = false" +
            " AND CURRENT_TIMESTAMP > start_time" +
            " AND CURRENT_DATE < g.open_date", nativeQuery = true)
    List<Group> getByOnOpen();
}
