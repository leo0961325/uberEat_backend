package com.tgfc.tw.entity.repository;

import com.tgfc.tw.entity.model.po.Tag;
import com.tgfc.tw.entity.model.response.TagResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {

    List<TagResponse> findAllBy();

    @Query(value = "SELECT * FROM tag WHERE name = ?1",nativeQuery = true)
    Optional<Tag> findByName(String name);

    Tag getById(int id);

    @Query(value = "SELECT * FROM tag t LEFT JOIN store_tag st ON st.tag_id = t.id WHERE st.store_id IN ?1 GROUP BY t.id",nativeQuery = true)
    List<Tag> getByStoreId(Set<Integer> storeIdList);

    List<Tag> getAllByIdIn(@Param("TagIds") Integer[] tagIds);

    @Query(nativeQuery = true,value = " " +
            "SELECT " +
            "   t.id AS id, " +
            "   t.name AS name, " +
            "   store_id AS storeId " +
            "FROM " +
            "   tag t " +
            "   JOIN store_tag st on t.id = st.tag_id " +
            "ORDER BY " +
            "   t.id ")
    List<Map<String,Object>> getAllTags();


    @Query(value = "SELECT t.id AS tagId, t.name AS tagName, st.store_id AS storeId " +
            "FROM store_tag st " +
            "LEFT JOIN tag t ON t.id = st.tag_id " +
            "WHERE st.store_id IN (:storeIdList) " +
            "ORDER BY st.store_id, t.id", nativeQuery = true)
    List<Map<String,Object>> getTagByStoreId(@Param("storeIdList") List<Integer> storeIdList);
}
