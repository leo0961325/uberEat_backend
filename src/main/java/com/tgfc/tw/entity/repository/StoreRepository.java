package com.tgfc.tw.entity.repository;

import com.tgfc.tw.entity.model.po.Store;
import com.tgfc.tw.entity.model.response.StoreGetOneResponse;
import com.tgfc.tw.entity.model.response.StoreInfoResponse;
import com.tgfc.tw.entity.model.response.StoreResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {

    Store getById(int id);

    List<Store> getByIdIn(List<Integer> ids);

    List<Store> findAll();

    @Query(value = "SELECT s FROM Store s")
    List<StoreResponse> findAllForStoreResponse();

    @Query(value = "SELECT * FROM store WHERE store.name = :storeName", nativeQuery = true)
    Store getByStoreName(@Param("storeName") String storeName);

    @Query(value = "SELECT new com.tgfc.tw.entity.model.response.StoreGetOneResponse(s) FROM Store s WHERE s.id = :id")
    StoreGetOneResponse getStore(@Param("id") int id);

    @Query(value = "select store_id as sid,group_id as gid from group_store where group_id=:groupId",nativeQuery = true)
    List<Map<String,Object>> getGroupStoreByGroupId(@Param("groupId") int groupId);

    @Query(value = "select gs.store_id as sid, s.name as storeName,s.tel as storeTel, s.address as storeAddress, s.remark as remark from groups g " +
            "left join group_store gs  on g.id=gs.group_id " +
            "left join store s  on gs.store_id=s.id " +
            "where g.id=:groupId and (COALESCE(:storeIdList) is null or gs.store_id in (:storeIdList))",nativeQuery = true)
    List<Map<String,Object>> getGroupStoreDataByGroupId(@Param("groupId") int groupId,@Param("storeIdList") List<Integer> storeIdList);

    @Query(value = "select gs.store_id as sid, sp.id as id, sp.name as storePicName,sp.url as storePictureUrl from groups g " +
            "left join group_store gs  on g.id=gs.group_id " +
            "left join store s  on gs.store_id=s.id " +
            "left join store_picture sp on s.id=sp.store_id " +
            "where g.id=:groupId",nativeQuery = true)
    List<Map<String,Object>> getGroupStorePicByGroupId(@Param("groupId") int groupId);

    @Query(value = "select gs.store_id as sid, t.id as id, t.name as name from groups g " +
            "left join group_store gs  on g.id=gs.group_id " +
            "left join store s  on gs.store_id=s.id " +
            "left join store_tag st on s.id=st.store_id " +
            "left join tag t on t.id=st.tag_id " +
            "where g.id=:groupId",nativeQuery = true)
    List<Map<String,Object>> getGroupStoreTagByGroupId(@Param("groupId") int groupId);

    @Query(nativeQuery = true,value = "SELECT " +
            "s.id AS id," +
            "s.name AS name, " +
            "gs.group_id AS groupId " +
            "FROM store s " +
            "LEFT JOIN group_store gs ON gs.store_id = s.id " +
            "JOIN groups g ON gs.group_id = g.id " )
    List<Map<String,Object>> getALlStoreByGroup();

    @Query(nativeQuery = true,value = "SELECT " +
            "sp.id AS id," +
            "sp.url AS url," +
            "sp.name AS name," +
            "s.id AS storeId " +
            "FROM store_picture sp " +
            "JOIN store s on sp.store_id = s.id;   ")
    List<Map<String,Object>> getAllGroupPic();
    @Query(value = "SELECT s.id AS storeId, s.name AS storeName, s.address AS storeAddress, " +
            "s.remark AS storeRemark, s.tel AS storeTel, t.id AS tagId, t.name AS tagName, " +
            "sp.id AS storePictureId, sp.name AS storePictureName, sp.url AS storePictureUrl " +
            "FROM store s " +
            "LEFT JOIN store_picture sp ON sp.store_id = s.id " +
            "LEFT JOIN store_tag st ON st.store_id = s.id " +
            "LEFT JOIN tag t ON t.id = st.tag_id " +
            "LEFT JOIN store_review sr ON s.id = sr.store_id " +
            "WHERE s.id = :storeId ", nativeQuery = true)
    List<Map<String, Object>> getStoreById(@Param("storeId") int storeId);

    @Query(value = "SELECT s.id AS storeId, s.name AS storeName, s.address AS storeAddress, " +
            "s.remark AS storeRemark, s.tel AS storeTel, t.id AS tagId, t.name AS tagName, " +
            "sp.id AS storePictureId, sp.name AS storePictureName, sp.url AS storePictureUrl,COUNT(sr.review) AS totalUsers,AVG(sr.review) AS rank " +
            "FROM store s " +
            "LEFT JOIN store_picture sp ON sp.store_id = s.id " +
            "LEFT JOIN store_tag st ON st.store_id = s.id " +
            "LEFT JOIN tag t ON t.id = st.tag_id " +
            "LEFT JOIN store_review sr ON s.id = sr.store_id " +
            "WHERE s.id = :storeId ", nativeQuery = true)
    List<Map<String, Object>> getStoreByIdV2_1(@Param("storeId") int storeId);

    @Deprecated
    @Query(value = "SELECT s.id AS id, s.name AS storeName, s.tel AS storeTel, s.remark AS remark, " +
            "COUNT(g.id) canDelete, s.is_deleted AS isDelete " +
            "FROM store s " +
            "LEFT JOIN group_store gs ON gs.store_id = s.id " +
            "LEFT JOIN groups g ON g.id = gs.group_id " +
            "LEFT JOIN store_tag st ON st.store_id = s.id " +
            "LEFT JOIN tag t ON t.id = st.tag_id " +
            "WHERE s.name LIKE :keyword " +
            "AND (COALESCE(:tagIdList) IS NULL OR t.id IN (:tagIdList)) " +
            "GROUP BY s.id " +
            "ORDER BY s.id ",
            countQuery = "SELECT COUNT(g.id) " +
                    "FROM store s " +
                    "LEFT JOIN group_store gs ON gs.store_id = s.id " +
                    "LEFT JOIN groups g ON g.id = gs.group_id " +
                    "LEFT JOIN store_tag st ON st.store_id = s.id " +
                    "LEFT JOIN tag t ON t.id = st.tag_id " +
                    "WHERE s.name LIKE :keyword " +
                    "AND (COALESCE(:tagIdList) IS NULL OR t.id IN (:tagIdList)) " +
                    "GROUP BY s.id ", nativeQuery = true)
    Page<Map<String, Object>> getStoreByKeyword(@Param("keyword") String keyword, @Param("tagIdList") List<Integer> tagIdList, Pageable pageable);

    @Query(value = "SELECT s.id AS id, s.name AS storeName, s.tel AS storeTel, s.remark AS remark, " +
            "COUNT(g.id) canDelete, s.is_deleted AS isDelete " +
            "FROM store s " +
            "LEFT JOIN group_store gs ON gs.store_id = s.id " +
            "LEFT JOIN groups g ON g.id = gs.group_id " +
            "LEFT JOIN store_tag st ON st.store_id = s.id " +
            "LEFT JOIN tag t ON t.id = st.tag_id " +
            "WHERE s.name LIKE :keyword " +
            "AND s.is_deleted = false " +
            "AND (COALESCE(:tagIdList) IS NULL OR t.id IN (:tagIdList)) " +
            "GROUP BY s.id " +
            "ORDER BY s.id ",
            countQuery = "SELECT COUNT(g.id) " +
                    "FROM store s " +
                    "LEFT JOIN group_store gs ON gs.store_id = s.id " +
                    "LEFT JOIN groups g ON g.id = gs.group_id " +
                    "LEFT JOIN store_tag st ON st.store_id = s.id " +
                    "LEFT JOIN tag t ON t.id = st.tag_id " +
                    "WHERE s.name LIKE :keyword " +
                    "AND s.is_deleted = false " +
                    "AND (COALESCE(:tagIdList) IS NULL OR t.id IN (:tagIdList)) " +
                    "GROUP BY s.id ", nativeQuery = true)
    Page<Map<String, Object>> getNewStoreByKeyword(@Param("keyword") String keyword, @Param("tagIdList") List<Integer> tagIdList, Pageable pageable);

    @Query(value = "SELECT store.* FROM product JOIN store on product.store_id=store.id WHERE product.id = :productId", nativeQuery = true)
    Store getByProductId(@Param("productId") int productId);

    @Query(value = "SELECT store.* FROM option_ join product on option_.product_id=product.id JOIN store on product.store_id=store.id WHERE option_.id = :optionId", nativeQuery = true)
    Store getStoreByOptionId(@Param("optionId") int optionId);

    @Query(value = "SELECT s.id AS storeId, s.name AS storeName FROM store s LEFT JOIN group_store gs ON s.id = gs.store_id LEFT JOIN groups g ON gs.group_id = g.id WHERE g.id = :groupId", nativeQuery = true)
    List<Map<String, Object>> getStoresByGroupId(@Param("groupId") int groupId);

    boolean existsByNameAndIsDeleted(String name, boolean isDeleted);

    Optional<Store> findByIdAndIsDeleted(int storeId, boolean isDeleted);

    boolean existsByIdAndIsDeleted(int storeId,boolean isDeleted);
}