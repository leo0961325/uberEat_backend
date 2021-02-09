package com.tgfc.tw.entity.repository;

import com.tgfc.tw.entity.model.po.PayDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PayDetailRepository extends JpaRepository<PayDetail, Integer> {

    @Query(value = "DELETE FROM pay_detail" +
            " WHERE pay_id = :userPayId" +
            " AND product_id = :productId", nativeQuery = true)
    void delByPayIdAndProductId(@Param("userPayId") int userPayId, @Param("productId") int productId);

    @Query(value = "DELETE FROM pay_detail" +
            " WHERE id = :id", nativeQuery = true)
    void deleteById(@Param("id") int id);
}
