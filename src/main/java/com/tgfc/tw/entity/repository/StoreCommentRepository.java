//package com.tgfc.tw.entity.repository;
//
//import com.tgfc.tw.entity.model.po.StoreComment;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.CrudRepository;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Map;
//
//@Repository
//public interface StoreCommentRepository extends CrudRepository<StoreComment,Integer> {
//
//    @Query(value = "SELECT sc.id AS commentId, sc.message AS message, " +
//            "sc.store_id AS storeId, sc.user_id AS userId " +
//            "FROM store_comment sc " +
//            "WHERE sc.store_id IN (:storeIdList) " +
//            "ORDER BY sc.store_id, sc.user_id", nativeQuery = true)
//    List<Map<String, Object>> getByStoreId(@Param("storeIdList") List<Integer> storeIdList);
//}
