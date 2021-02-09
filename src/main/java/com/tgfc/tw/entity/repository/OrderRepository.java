package com.tgfc.tw.entity.repository;

import com.tgfc.tw.entity.model.po.Product;
import com.tgfc.tw.entity.model.response.order.OrderResponse;
import com.tgfc.tw.entity.model.response.orderHistory.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Map;

@Repository
public interface OrderRepository extends JpaRepository<Product, Integer> {

    @Query(value = "SELECT new com.tgfc.tw.entity.model.response.order.OrderResponse(o) FROM Product o WHERE o.id = :orderId ")
    OrderResponse getById(@Param("orderId") int orderId);

    @Query(value = "SELECT * " +
            "FROM product  " +
            "WHERE product.store_id = :storeId " +
            "AND product.name LIKE :keyword ",
            countQuery="SELECT COUNT(*) " +
                    "FROM product  " +
                    "WHERE product.store_id = :storeId " +
                    "AND product.name LIKE :keyword ",
                    nativeQuery = true)
    Page<Map<String,Object>> findAllByStoreId(@Param("storeId") int storeId, @Param("keyword") String keyword, Pageable pageable);

    boolean existsByNameAndPriceAndStoreIdAndRemark(String Name, int Price, int Store_id, String remark);

    List<Product> getByStoreId(int Store_id);

    @Modifying
    @Query(value = "DELETE FROM product WHERE product.store_id = :id", nativeQuery = true)
    void deleteAllByStoreId(@Param("id") int store_id);

    @Deprecated
    @Query(value = "SELECT new com.tgfc.tw.entity.model.response.orderHistory.OrderHistoryCountByUserResponse" +
            "(u.englishName,ol.id, ol.name, ol.price, op.name, op.price, COUNT(u.id), (ol.price + op.price)*COUNT(u.id),u.id,ol.store.id) " +
            "FROM User u JOIN u.orderList oh " +
            "JOIN oh.option op " +
            "JOIN op.product ol " +
            "WHERE oh.group.id = :groupId " +
            "AND ol.store.id IN :storeIds " +
            "AND (u.floor.id = :floorId OR :floorId IS NULL) " +
            "AND (u.id = :userId OR :userId IS NULL) " +
            "GROUP BY CONCAT(u.id,op.id)" +
            "ORDER BY ol.name, op.name")
    List<OrderHistoryCountByUserResponse> getUserCount(@Param("groupId") int groupId, @Param("storeIds") List<Integer> storeIds, @Param("floorId") Integer floorId, @Param("userId") Integer userId);

    @Query(value = "SELECT DISTINCT " +
            " p.id AS productId, " +
            " p.NAME AS productName, " +
            " p.price AS productPrice, " +
            " op.id AS optionId, " +
            " op.NAME AS optionName, " +
            " op.price AS optionPrice, " +
            " o.id AS orderId, " +
            " u.id AS userId, " +
            " COUNT(op.id) AS count " +
            " FROM order_ o " +
            " JOIN order_user ou ON ou.order_id = o.id " +
            " JOIN user u ON u.id = ou.user_id " +
            " LEFT JOIN user_team ut ON ut.user_id = u.id " +
            " JOIN option_ op ON o.option_id = op.id " +
            " JOIN product p ON op.product_id = p.id " +
            " WHERE o.group_id = :groupId " +
            " AND p.store_id = :storeId " +
            " AND ( COALESCE(:teamIdList) IS NULL OR ut.team_id IN (:teamIdList) ) " +
            " GROUP BY u.id, o.option_id, ut.team_id " +
            " ORDER BY p.NAME ", nativeQuery = true)
    List<Map<String, Object>> getOrderHistoryV2_1(@Param("groupId") int groupId, @Param("storeId") Integer storeId, @Param("teamIdList") List<Integer> teamIdList);

    @Query(value = "SELECT distinct new com.tgfc.tw.entity.model.response.orderHistory.OrderHistoryCountByUserResponse" +
            "(u.englishName,ol.id, ol.name, ol.price, op.name, op.price, COUNT(u.id), (ol.price + op.price)*COUNT(u.id),u.id,ol.store.id) " +
            "FROM User u JOIN u.orderList oh " +
            "JOIN oh.option op " +
            "JOIN op.product ol " +
            "left JOIN u.teamList ut " +
            "WHERE oh.group.id = :groupId " +
            "AND ol.store.id IN :storeIds " +
            "AND (COALESCE(:teamIdList,0) IS 0 OR ut.id IN (:teamIdList)) " +
            "AND (u.id = :userId OR :userId IS NULL) " +
            "GROUP BY CONCAT(u.id,op.id,ut.id)" +
            "ORDER BY ol.name, op.name")
    List<OrderHistoryCountByUserResponse> getUserCountV2_1(@Param("groupId") int groupId, @Param("storeIds") List<Integer> storeIds, @Param("teamIdList") List<Integer> teamIdList, @Param("userId") Integer userId);

    @Query(value = "SELECT p.id AS productId, p.name AS productName, p.price AS productPrice, " +
            "op.id AS optionId, op.name AS optionName, op.price AS optionPrice, o.id AS orderId, " +
            "COUNT(u.id) AS count " +
            "FROM order_ o " +
            "JOIN order_user ou ON ou.order_id = o.id " +
            "JOIN user u ON u.id = ou.user_id " +
            "JOIN option_ op ON o.option_id = op.id " +
            "JOIN product p ON op.product_id = p.id " +
            "WHERE o.group_id = :groupId " +
            "AND p.store_id = :storeId " +
            "AND (u.floor_id = :floorId OR :floorId IS NULL) " +
            "GROUP BY o.id " +
            "ORDER BY p.name ", nativeQuery = true)
    List<Map<String, Object>> getOrderHistoryBy(@Param("groupId") int groupId, @Param("storeId") Integer storeId, @Param("floorId") Integer floorId);


    @Query(value = "SELECT  product.id AS productId ,user.id AS id ,user.english_name AS englishName,  floor.name AS floor ,  option_.id AS optionId    ,COUNT(user.id) AS count " +
            "FROM  product  " +
            "JOIN option_ ON product.id=option_.product_id " +
            "LEFT JOIN order_ ON option_.id=order_.option_id " +
            "LEFT JOIN order_user ON  order_.id= order_user.order_id " +
            "JOIN user ON  order_user.user_id=user.id " +
            "JOIN floor ON  user.floor_id=floor.id "+
            "WHERE product.store_id= :storeId AND order_.group_id= :groupId " +
            "GROUP BY product.id ,user.id",nativeQuery = true)
    List<Map<String,Object>> getOrderUserByGroupId(@Param("groupId") int groupId, @Param("storeId") Integer storeId);


    @Query(value = "SELECT * FROM (SELECT product.id AS id, product.name AS name, product.remark AS remark, product.price AS order_price, " +
            "SUM(if(order_.group_id =:groupId AND order_user.user_id IS NOT NULL, 1, 0)) AS order_count, " +
            "SUM(if(order_.group_id =:groupId AND order_user.user_id IS NOT NULL, option_.price, 0)) AS option_price, " +
            "SUM(if(order_.group_id =:groupId AND order_user.user_id IS NOT NULL, product.price + option_.price, 0)) AS total, " +
            "SUM(if(order_.group_id =:groupId AND user.id =:userIdServer, 1, 0)) as usering_ordercount, " +
            "GROUP_CONCAT(user.id) FROM product " +
            "JOIN option_ ON product.id = option_.product_id " +
            "LEFT JOIN order_ ON option_.id = order_.option_id " +
            "LEFT JOIN order_user ON order_.id = order_user.order_id " +
            "LEFT JOIN user ON order_user.user_id = user.id " +
            "WHERE product.store_id =:storeId " +
            "GROUP BY product.id HAVING product.name LIKE :keyword) AS tb " +
            "ORDER BY usering_ordercount DESC, order_count DESC "
            ,countQuery="SELECT COUNT(product.id) " +
            "FROM group_store " +
            "JOIN product ON group_store.store_id = product.store_id AND group_store.group_id =:groupId " +
            "WHERE product.store_id =:storeId OR product.name LIKE :keyword " +
            "ORDER BY " +
            "CASE WHEN :userIdServer = -1 THEN NULL END "
            ,nativeQuery = true)
    Page<Map<String,Object>> getOrderHistoryCountBy(@Param("groupId") int groupId, @Param("storeId") Integer storeId, @Param("userIdServer") int userIdServer,
                                                    @Param("keyword") String keyword, Pageable pageable);


    @Query(value = "SELECT " +
            "COUNT(*) AS count, " +
            "SUM(product.price+option_.price)AS total " +
            "FROM  product  " +
            "JOIN option_ ON product.id=option_.product_id " +
            "JOIN order_ ON option_.id=order_.option_id " +
            "JOIN order_user ON  order_.id= order_user.order_id " +
            "JOIN user ON  order_user.user_id=user.id " +
            "WHERE product.store_id=:storeId and order_.group_id=:groupId AND product.name LIKE :keyword"
            ,nativeQuery = true)
    Map<String,Object> getOrderHistoryTotalPriceBy(@Param("groupId") int groupId, @Param("storeId") Integer storeId,@Param("keyword") String keyword);


}