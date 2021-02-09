package com.tgfc.tw.entity.repository;

import com.tgfc.tw.entity.model.po.Order;
import com.tgfc.tw.entity.model.po.User;
import com.tgfc.tw.entity.model.response.orderHistory.OrderHistoryCountByUserResponse;
import com.tgfc.tw.entity.model.response.orderHistory.TotalCalcResponse;
import com.tgfc.tw.entity.model.response.user.UserListByRemoveOrderResponseV2_1;
import com.tgfc.tw.entity.model.response.user.UserListResponse;
import com.tgfc.tw.entity.model.response.user.UserNameResponse;
import com.tgfc.tw.entity.model.response.user.UserNameResponseV2_1;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderHistoryRepository extends CrudRepository<Order, Integer> {

    @Query(value = "SELECT order_history_id FROM order_user  WHERE order_history_id = :id AND user_id = :userId LIMIT 1", nativeQuery = true)
    Integer findOrderHistoryUser(@Param("id") int Integer, @Param("userId") int userId);

    @Query(value = "DELETE ou FROM order_user ou " +
            "LEFT JOIN order_ o ON o.id = ou.order_id " +
            "LEFT JOIN option_ op ON op.id = o.option_id " +
            "WHERE op.product_id = :orderId " +
            "AND o.group_id = :groupId " +
            "AND ou.user_id = :userId ", nativeQuery = true)
    Order delOrderHistory(@Param("orderId") Integer orderId, @Param("groupId") int groupId, @Param("userId") int userId);

    @Query(value = "SELECT o FROM Order o " +
            "LEFT JOIN o.userList ou " +
            "LEFT JOIN o.option op " +
            "WHERE op.product.id = :orderId " +
            "AND o.group.id = :groupId " +
            "AND ou.id = :userId ")
    List<Order> getDelOrderHistory(@Param("orderId") Integer orderId, @Param("groupId") int groupId, @Param("userId") int userId);

    @Query(value = "SELECT * FROM order_  WHERE option_id = :optionId AND group_id = :groupId ", nativeQuery = true)
    Optional<Order> findOrderHistory(@Param("optionId") int optionId, @Param("groupId") int groupId);

    @Query(value = "SELECT new com.tgfc.tw.entity.model.response.orderHistory.OrderHistoryCountByUserResponse" +
            "(u.name,p.id, p.name, p.price, op.name, op.price, COUNT(u.id), (p.price + op.price)*COUNT(u.id), u.id,p.store.id) " +
            "FROM User u JOIN u.orderList o " +
            "JOIN o.option op " +
            "JOIN op.product p " +
            "WHERE o.group.id = :groupId AND u.id = :userId " +
            "GROUP BY CONCAT(u.id,p.id)" +
            "ORDER BY u.id ")
    List<OrderHistoryCountByUserResponse> findOrder(@Param("groupId") int groupId, @Param("userId") int userId);

    @Query(value = "SELECT COUNT(ou.user_id) FROM order_ AS o LEFT JOIN  order_user AS ou ON o.id = ou.order_id WHERE o.option_id= :orderId", nativeQuery = true)
    Integer getOrderUserAmount(@Param("orderId") int orderId);

    @Query(value = "SELECT new com.tgfc.tw.entity.model.response.user.UserListResponse(ou) " +
            "FROM Order o " +
            "JOIN o.userList ou " +
            "JOIN o.option op " +
            "WHERE op.product.id = ?1 AND o.group.id = ?2 " +
            "GROUP BY ou.name " +
            "ORDER BY ou.name ")
    List<UserListResponse> getOrderUserList(int orderId, int groupId);

	@Query(value = "SELECT new com.tgfc.tw.entity.model.response.user.UserListByRemoveOrderResponseV2_1(ou) " +
			"FROM Order o " +
			"JOIN o.userList ou " +
			"JOIN o.option op " +
			"WHERE op.product.id = ?1 AND o.group.id = ?2 " +
			"GROUP BY ou.name " +
			"ORDER BY ou.name")
	List<UserListByRemoveOrderResponseV2_1> getOrderUserListV2_1(int productId, int groupId);

    @Query(value = "SELECT new com.tgfc.tw.entity.model.response.user.UserNameResponse(ou) " +
            "FROM Order o " +
            "JOIN o.userList ou " +
            "WHERE o.group.id = :groupId " +
            "GROUP BY ou.id ")
    List<UserNameResponse> getOrderUserByGroup(@Param("groupId") int groupId);

    @Query(value = "SELECT new com.tgfc.tw.entity.model.response.user.UserNameResponseV2_1(ou) " +
            "FROM Order o " +
            "JOIN o.userList ou " +
            "WHERE o.group.id = :groupId " +
            "GROUP BY ou.id ")
    List<UserNameResponseV2_1> getOrderUserByGroupV2_1(@Param("groupId") int groupId);

    @Query(value = "SELECT new com.tgfc.tw.entity.model.response.orderHistory.TotalCalcResponse(COUNT(o.id), SUM(op.price + p.price)) " +
            "FROM Order o " +
            "INNER JOIN o.option op " +
            "INNER JOIN op.product p " +
            "INNER JOIN o.userList ou " +
            "WHERE o.group.id = :groupId " +
            "AND p.store.id = :storeId")
    TotalCalcResponse getTotalCalc(@Param("groupId") int groupId, @Param("storeId") int storeId);

}
