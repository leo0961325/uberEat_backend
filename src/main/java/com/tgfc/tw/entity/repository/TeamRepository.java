package com.tgfc.tw.entity.repository;

import com.tgfc.tw.entity.model.po.Option;
import com.tgfc.tw.entity.model.po.Team;
import com.tgfc.tw.entity.model.response.team.TeamGetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.tgfc.tw.entity.model.response.TeamResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
//    Optional<Team> findByName(String name);

    List<Team> findByName(String name);

    @Query(value = "SELECT new com.tgfc.tw.entity.model.response.team.TeamGetResponse(t) " +
            "FROM Team t " +
            "WHERE t.name LIKE :keyword AND t.isDeleted = false ")
    Page<TeamGetResponse> getTeamByKeyword(@Param("keyword") String keyword, Pageable pageable);


    @Query(value = " SELECT t FROM Team t WHERE t.id IN (:teamIdList)")
    List<Team> getByIdList(@Param("teamIdList") List<Integer> teamIdList);

    @Query(value = " SELECT new com.tgfc.tw.entity.model.response.TeamResponse(t) FROM Team t" +
            " WHERE t.isDeleted = false")
    List<TeamResponse> getAll();

    @Query(value = " SELECT new com.tgfc.tw.entity.model.response.team.TeamResponse(t) FROM Team t ")
    List<com.tgfc.tw.entity.model.response.team.TeamResponse> getAllTeam();

    List<Team> findAllByIsDeletedFalse();

    @Query(value = " SELECT new com.tgfc.tw.entity.model.response.team.TeamResponse(t) FROM Team t WHERE t.isDeleted = false")
    List<com.tgfc.tw.entity.model.response.team.TeamResponse> getAllNotDeleted();

    @Query(value = "SELECT t.id AS id, t.name AS name, gt.group_id AS groupId FROM team t JOIN group_team gt ON gt.team_id = t.id ", nativeQuery = true)
    List<Map<String, Object>> getAllGroupTeams();

    @Query(value = " SELECT team.id AS teamID,team.`name` AS teamName FROM `user` " +
            "LEFT JOIN user_team ON user_team.user_id = `user`.id  " +
            "LEFT JOIN team ON team.id = user_team.team_id " +
            "WHERE user.id = :userId AND team.is_deleted = false", nativeQuery = true)
    List<Map<String, Object>> getTeamByUserId(@Param("userId") int id);

    @Query(value = " SELECT team.id AS teamID,team.name AS teamName FROM user  " +
            "JOIN user_team ON user_team.user_id = user.id  " +
            "JOIN team ON team.id = user_team.team_id " +
            "WHERE user.id = :userId AND team.is_deleted = false", nativeQuery = true)
    List<Map<String, Object>> getTeamListByUserId(@Param("userId") int id);

    @Query(value = " DELETE FROM user_team" +
            " WHERE team_id = :teamId", nativeQuery = true)
    void delUserTeam(@Param("teamId") int teamId);
}
