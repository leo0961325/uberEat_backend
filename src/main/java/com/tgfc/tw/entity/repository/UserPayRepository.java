package com.tgfc.tw.entity.repository;


import com.tgfc.tw.entity.model.po.UserPay;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserPayRepository extends CrudRepository<UserPay, Integer> {

    UserPay getById(int id);

    @Query(value = "SELECT * FROM user_pay WHERE group_id = :groupId AND user_id =:userId AND store_id =:storeId", nativeQuery = true)
    UserPay getByGroupIdaAndUserId(@Param("groupId") int groupId, @Param("userId") int userId, @Param("storeId") int storeId);

    @Query(value = "SELECT * FROM user_pay WHERE group_id = :groupId", nativeQuery = true)
    List<UserPay> getByGroupId(@Param("groupId") int groupId);

    @Query(value = "SELECT up FROM UserPay up" +
            " JOIN up.payDetailList pd" +
            " WHERE up.user.id = :userId" +
            " AND pd.groupId = :groupId" +
            " AND up.debit = :debit ")
    UserPay getByGroupOnGoing(@Param("userId") int userId, @Param("groupId") int groupId, @Param("debit") boolean debit);

    @Query(value = "SELECT up FROM UserPay up" +
            " JOIN up.payDetailList pd" +
            " WHERE up.user.id = :userId" +
            " AND pd.groupId = :groupId" +
            " AND up.debit = :debit " +
            " GROUP BY up.id")
    List<UserPay> getByGroupAndUser(@Param("userId") int userId, @Param("groupId") int groupId, @Param("debit") boolean debit);

    @Query(value = "SELECT SUM(Case WHEN u.status = true THEN u.pay ELSE -u.pay END) FROM user_pay u  WHERE u.user_id = :memberId", nativeQuery = true)
    Integer getBalance(@Param("memberId") int memberId);

    @Query(value = "SELECT SUM(Case WHEN u.status = true THEN u.pay ELSE -u.pay END) FROM user_pay u  WHERE u.user_id = :memberId AND u.debit = true", nativeQuery = true)
    Integer getActualBalance(@Param("memberId") int memberId);

    @Query(value = "SELECT SUM(Case WHEN u.status = true THEN u.pay ELSE -u.pay END) FROM user_pay u", nativeQuery = true)
    List<Integer> getAllUserBalance();

    @Query(value = "SELECT * FROM user_pay WHERE user_id = :userId", nativeQuery = true)
    List<UserPay> getByUserId(@Param("userId") int userId, Pageable pageable);

    @Query(value = "SELECT p.store_id AS storeId, p.store_name AS storeName, p.pay_id AS payId FROM  pay_detail p Left JOIN user_pay u on u.id = p.pay_id WHERE u.user_id = :userId", nativeQuery = true)
    List<Map<String, Object>> getStoreByUserId(@Param("userId") int userId);

    @Query(value = "SELECT p.store_id AS storeId, p.store_name AS storeName, p.product_name AS productName, " +
            " p.product_price AS productPrice, p.option_name AS optionName, p.option_price AS optionPrice, " +
            " p.count AS productCount, p.pay_id AS payId, p.total_price AS totalPrice " +
            " FROM pay_detail p LEFT JOIN user_pay u on u.id = p.pay_id WHERE u.user_id = :userId", nativeQuery = true)
    List<Map<String, Object>> getPayDetailByUserId(@Param("userId") int userId);

    @Query(value = "SELECT count(*) AS totalCount FROM user_pay WHERE user_id = :userId", nativeQuery = true)
    int getTotalCount(@Param("userId") int userId);

    @Query(value = " UPDATE user_pay up JOIN pay_detail pd ON up.id = pd.pay_id SET up.debit = true WHERE pd.group_id IN (:groupIdList) AND up.debit = false ;", nativeQuery = true)
    void changeDebitStatus(@Param("groupIdList") List<Integer> groupIdList);

    @Query(value = "SELECT up.* FROM user_pay up" +
            " JOIN pay_detail pd ON up.id = pd.pay_id" +
            " WHERE pd.group_id = :groupId " +
            " GROUP BY up.id"
            , nativeQuery = true)
    List<UserPay> getUserPayByGroupId(@Param("groupId") int groupId);

    @Query(value = "SELECT up.* FROM user_pay up" +
            " JOIN pay_detail pd ON pd.pay_id = up.id" +
            " WHERE pd.option_id = :optionId" +
            " AND pd.group_id = :groupId", nativeQuery = true)
    List<UserPay> getByOptionAndGroup(@Param("optionId") int optionId, @Param("groupId") int groupId);

}
