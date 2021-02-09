package com.tgfc.tw.entity.repository;

import com.tgfc.tw.entity.model.po.Option;
import com.tgfc.tw.entity.model.response.OptionCountResponse;
import com.tgfc.tw.entity.model.response.OptionResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public interface OptionRepository extends CrudRepository<Option, Integer> {

    boolean existsByNameAndPriceAndProductId(String name, int price, int productId);

    @Query(value = "SELECT new com.tgfc.tw.entity.model.response.OptionResponse(op) FROM Option op WHERE op.product.id = :productId")
    List<OptionResponse> getByProductId(@Param("productId") int productId);

    @Query(value = "SELECT new com.tgfc.tw.entity.model.response.OptionResponse(op) FROM Option op WHERE op.id = :optionId ")
    OptionResponse getById(@Param("optionId") int optionId);

    @Query(nativeQuery = true , value = "" +
            "SELECT opt.id AS id," +
            "   opt.name AS name, " +
            "   opt.price AS price," +
            "   p.price AS productPrice, " +
            "CASE " +
            "   WHEN o.id IS NULL THEN 0 " +
            "   ELSE (CASE " +
            "           WHEN ou.user_id IS NULL THEN 0 " +
            "           ELSE COUNT(opt.id) " +
            "           END) " +
            "   END AS orderCount " +
            "FROM " +
            "   option_ opt " +
            "   LEFT JOIN order_ o ON o.option_id = opt.id AND o.group_id = :groupId " +
            "   LEFT JOIN order_user ou ON ou.order_id = o.id " +
            "   LEFT JOIN product p ON p.id = opt.product_id " +
            "WHERE " +
            "   p.id = :productId " +
            "GROUP BY " +
            "   opt.id ")
    List<Map<String,Object>> getByGroup(@Param("groupId") int groupId, @Param("productId") int productId);

    @Query(value = "SELECT * FROM option_ where product_id = :productId ORDER BY id ASC LIMIT 1 ", nativeQuery = true)
    Option getOptionByProductId(@Param("productId") int productId);
}
