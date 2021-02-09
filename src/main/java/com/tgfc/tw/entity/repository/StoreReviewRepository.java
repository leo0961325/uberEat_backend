package com.tgfc.tw.entity.repository;


import com.tgfc.tw.entity.model.po.StoreReview;
import com.tgfc.tw.entity.model.response.RankMessageResponse;
import com.tgfc.tw.entity.model.response.UserStoreReviewResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface StoreReviewRepository extends CrudRepository<StoreReview, Integer> {

    @Query(value = "SELECT * FROM store_review WHERE store_id = ?1 AND user_id = ?2", nativeQuery = true)
    Optional<StoreReview> findReview(int storeId, int userIdServer);

    @Query(value = "SELECT sr.id AS reviewId, sr.review AS review, " +
            "sr.store_id AS storeId, sr.user_id AS userId " +
            "FROM store_review sr " +
            "WHERE sr.store_id IN (:storeIdList) " +
            "ORDER BY sr.store_id, sr.user_id", nativeQuery = true)
    List<Map<String, Object>> getByStoreId(@Param("storeIdList") List<Integer> storeIdList);

    @Query(value = " SELECT new com.tgfc.tw.entity.model.response.RankMessageResponse(sr) FROM StoreReview sr WHERE sr.store.id = :storeId ORDER BY sr.date DESC " )
    List<RankMessageResponse> getAllByStoreId(@Param("storeId") Integer storeId);

    @Query(value = "SELECT new com.tgfc.tw.entity.model.response.UserStoreReviewResponse(sr) FROM StoreReview sr WHERE sr.store.id = :storeId AND sr.user.id = :userId ")
    UserStoreReviewResponse getByUserId(@Param("storeId") Integer storeId,@Param("userId") Integer userId);

    Optional<StoreReview> findByStoreId(@Param("storeId") Integer storeId);

    @Query(value = "SELECT sr.store_id AS storeId, SUM(sr.review) AS review, count(sr.review) AS totalUsers FROM store_review sr GROUP BY sr.store_id", nativeQuery = true)
    List<Map<String, Object>> getReview(@Param("storeIdList") List<Integer> storeIdList);

    @Query(value = "SELECT sr.store_id AS storeId, SUM(sr.review) AS review, count(sr.review) AS totalUsers FROM store_review sr WHERE sr.store_id =:storeId ", nativeQuery = true)
    List<Map<String, Object>> getSingleReview(@Param("storeId") int storeId);

}
