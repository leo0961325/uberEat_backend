package com.tgfc.tw.entity.repository;

import com.tgfc.tw.entity.model.po.Role;
import com.tgfc.tw.entity.model.response.FloorResponse;
import com.tgfc.tw.entity.model.response.GroupTeamUserResponse;
import com.tgfc.tw.entity.model.response.RoleResponse;
import com.tgfc.tw.entity.model.response.GroupFloorUserResponse;
import com.tgfc.tw.entity.model.response.user.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    List<Role> getByIdIn(List<Integer> ids);

    @Query(value = "SELECT new com.tgfc.tw.entity.model.response.RoleResponse(r) FROM Role r WHERE r.code <> 'ROLE_SUPER_MANAGER'" )
    List<RoleResponse> findList();

    @Query(value = "SELECT new com.tgfc.tw.entity.model.response.GroupTeamUserResponse(u) FROM User u " +
            "LEFT JOIN u.roleList ur "+
            "WHERE ur.id IN (:roleIdList)"+
            "GROUP BY u.id "+
            "ORDER BY u.id "
    )
    List<GroupTeamUserResponse> getGroupManagerV2_1(@Param("roleIdList") List<Integer> roleIdList);

    @Deprecated
    @Query(value = "SELECT new com.tgfc.tw.entity.model.response.GroupFloorUserResponse(u) FROM User u " +
            "LEFT JOIN u.roleList ur "+
            "WHERE ur.id IN (:roleIdList)"+
            "GROUP BY u.id "+
            "ORDER BY u.id "
    )
    List<GroupFloorUserResponse> getGroupManager(@Param("roleIdList") List<Integer> roleIdList);
}
