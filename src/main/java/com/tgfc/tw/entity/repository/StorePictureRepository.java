package com.tgfc.tw.entity.repository;

import com.tgfc.tw.entity.model.po.StorePicture;
import com.tgfc.tw.entity.model.response.FloorResponse;
import com.tgfc.tw.entity.model.response.orderHistory.OrderHistoryStorePicResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface StorePictureRepository extends CrudRepository<StorePicture, Integer> {

    List<StorePicture> getUrlByStoreId(int storeId);

    List<StorePicture> getByStoreId(int storeId);

    @Query(value = "UPDATE store_picture SET store_id = :storeId WHERE id= :picId ",nativeQuery = true)
    List<FloorResponse> uploadStoreIdByPicId(@Param(value = "storeId") Integer storeId, @Param(value = "picId") Integer picId);

    @Query(value = "SELECT * FROM store_picture WHERE store_picture.url like :storeName ", nativeQuery = true)
    StorePicture findByUrl(@Param("storeName") String storeName);

    List<StorePicture> getAllByIdIn(@Param("picIds") Integer[] picIds);

    @Query(value = "SELECT * FROM store_picture WHERE store_picture.id like :picId ", nativeQuery = true)
    StorePicture getById(@Param("picId") int picId);

    @Query(value = "SELECT sp.id AS pictureId, sp.name AS pictureName, sp.url AS pictureUrl, sp.store_id AS storeId " +
            "FROM store_picture sp " +
            "WHERE sp.store_id IN (:storeIdList) " +
            "ORDER BY sp.store_id, sp.id", nativeQuery = true)
    List<Map<String,Object>> getPictureByStoreId(@Param("storeIdList") List<Integer> storeIdList);

    @Query(value = " SELECT new com.tgfc.tw.entity.model.response.orderHistory.OrderHistoryStorePicResponse(sp) FROM StorePicture sp WHERE sp.store.id = :storeId ")
    List<OrderHistoryStorePicResponse> getStorePic(@Param("storeId") int storeId);
}
