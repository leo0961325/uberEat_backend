package com.tgfc.tw.entity.repository;

import com.tgfc.tw.entity.model.po.Floor;
import com.tgfc.tw.entity.model.response.FloorResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface FloorRepository extends CrudRepository<Floor, Integer> {
    Optional<Floor> getById(int id);
    List<FloorResponse> getAllResponseBy();

    List<Floor> getByIdIn(List<Integer> ids);

    @Query(value = "SELECT new com.tgfc.tw.entity.model.response.FloorResponse(f) FROM Floor f")
    List<FloorResponse> getAllFloor();

    @Query(nativeQuery = true,value = " SELECT f.id AS id, f.name AS name, gf.group_id AS groupId FROM floor f JOIN group_floor gf ON gf.floor_id = f.id ")
    List<Map<String,Object>> getAllGroupFloor();
}
