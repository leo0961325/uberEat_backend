package com.tgfc.tw.entity.repository;

import com.tgfc.tw.entity.model.po.OrderPass;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderPassRepository extends CrudRepository<OrderPass, Integer> {

    @Query(value = "SELECT user_id " +
                   "FROM order_user " +
                   "WHERE order_history_id IN (SELECT id " +
                                              "FROM order_ " +
                                              "WHERE option_id IN (SELECT id " +
                                                                 "FROM option_ " +
                                                                 "WHERE order_id IN (SELECT id FROM order_list WHERE store_id = :storeId)))" +
                                              "AND group_id = :groupId)"
    , nativeQuery = true)
    List<Integer> userAddItems(@Param("groupId") int groupId, @Param("storeId") int storeId);

    @Query(value = "SELECT * FROM order_pass WHERE group_id = :groupId AND store_id = :storeId AND user_id = :userId", nativeQuery = true)
    Optional<OrderPass> orderPassExist(@Param("groupId") int groupId, @Param("storeId") int storeId, @Param("userId") int userId);

    @Query(value = "DELETE FROM order_pass WHERE id = :id", nativeQuery = true)
    void removeOrderPass(@Param("id") int id);
}
