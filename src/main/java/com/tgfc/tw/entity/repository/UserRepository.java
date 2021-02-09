package com.tgfc.tw.entity.repository;

import com.tgfc.tw.entity.model.po.User;
import com.tgfc.tw.entity.model.response.user.PageUserListResponse;
import com.tgfc.tw.entity.model.response.user.UserListResponse;
import com.tgfc.tw.entity.model.response.user.UserNameResponse;
import com.tgfc.tw.entity.model.response.user.UserNameResponseV2_1;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> getById(int id);
    Optional<User> findByUsername(String username);
    List<User> getByIdIn(List<Integer> ids);
    List<User> findAll();

    @Query(value = "SELECT * FROM user WHERE id = ?1",nativeQuery = true)
    User getUserById(int id);

    Optional<User> findById(String account);

    @Query(value = "SELECT new com.tgfc.tw.entity.model.response.user.PageUserListResponse(u) FROM User u " +
            " LEFT JOIN u.teamList ut " +
            " WHERE (u.username LIKE :keyword OR u.name LIKE :keyword OR u.englishName LIKE :keyword) " +
            " AND u.id <> 1" +
            " AND ( COALESCE( :teamIdList,0) IS 0 OR ut.id IN (:teamIdList)) " +
            " GROUP BY u.id " +
            " ORDER BY u.username ASC ")
    Page<PageUserListResponse> findAllByKeywordV2_1(@Param("keyword") String keyword,@Param("teamIdList") List<Integer> teamIdList, Pageable pageable);

    @Query(value = "SELECT new com.tgfc.tw.entity.model.response.user.PageUserListResponse(u) FROM User u " +
            " LEFT JOIN u.teamList ut " +
            " WHERE (u.username LIKE :keyword OR u.name LIKE :keyword OR u.englishName LIKE :keyword) " +
            " AND u.id <> 1" +
            " AND ( COALESCE( :teamIdList,0) IS 0 OR ut.id IN (:teamIdList)) " +
            " GROUP BY u.id " +
            " ORDER BY u.username ASC ")
    List<PageUserListResponse> getAll(@Param("keyword") String keyword,@Param("teamIdList") List<Integer> teamIdList);

    @Deprecated
    @Query(value = "SELECT u FROM User u " +
            " WHERE (u.username LIKE :keyword OR u.name LIKE :keyword OR u.englishName LIKE :keyword)" +
            " AND u.id <> 1" +
            " ORDER BY u.username ASC")
    Page<UserListResponse> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT new com.tgfc.tw.entity.model.response.user.UserNameResponse(u.id,u.floor.id,u.name,u.englishName) FROM User u " +
            "LEFT JOIN u.orderList uo " +
            "LEFT JOIN uo.group g " +
            "WHERE (g.id = :groupId OR :groupId IS NULL) AND u.id IS NOT NULL AND u.id <> 1 "+
            "GROUP BY u.id "+
            "ORDER BY u.id"
    )
    List<UserNameResponse> getAllUserNameList(@Param("groupId") Integer groupId);

    @Query(value = "SELECT new com.tgfc.tw.entity.model.response.user.UserNameResponseV2_1(u) FROM User u " +
            "LEFT JOIN u.orderList uo " +
            "LEFT JOIN uo.group g " +
            "WHERE (g.id = :groupId OR :groupId IS NULL) AND u.id IS NOT NULL AND u.id <> 1 "+
            "GROUP BY u.id "+
            "ORDER BY u.id"
    )
    List<UserNameResponseV2_1> getAllUserNameListV2_1(@Param("groupId") Integer groupId);

    @Query(value = "SELECT * FROM user WHERE floor_id = :floorId", nativeQuery = true)
    List<User> findUserByFloor(@Param("floorId") int floorId);

    @Query(nativeQuery = true, value = " SELECT u.id AS id, u.name AS name,u.username AS userName, u.english_name AS englishName,gu.group_id AS groupId  from group_user gu JOIN user u ON gu.user_id = u.id ")
    List<Map<String,Object>> getAllGroupUserList();

    @Query(nativeQuery = true, value = " " +
            "SELECT " +
            "   o.store_id AS storeId, " +
            "   p.id AS productId, " +
            "   p.name AS productName, " +
            "   p.price AS productPrice, " +
            "   opt.name AS optionName, " +
            "   g.open_date AS openDate, " +
            "   opt.price AS optionPrice, " +
            "   s.name AS storeName, " +

            "   g.id AS gId, " +
            "   COUNT( opt.id ) AS itemCount " +
            "FROM " +
            "   order_ o " +
            "   JOIN order_user ou ON ou.order_id = o.id " +
            "   JOIN option_ opt ON opt.id = o.option_id " +
            "   JOIN product p ON p.id = opt.product_id " +
            "   JOIN groups g ON g.id = o.group_id " +
            "   JOIN store s ON s.id = p.store_id "+
            "WHERE " +
            "   ou.user_id = :userId AND (g.open_date between :startTime AND :endTime OR :startTime IS NULL && :endTime IS NULL) AND g.is_deleted = false " +
            "GROUP BY " +
            "   p.name, opt.id, g.id ORDER BY g.open_date, p.id ")
    List<Map<String, Object>> getUserOrderRecord(@Param("userId") int userId , @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Query(nativeQuery = true, value = " " +
            "SELECT " +
            "   o.store_id AS storeId, " +
            "   p.id AS productId, " +
            "   p.name AS productName, " +
            "   p.price AS productPrice, " +
            "   opt.name AS optionName, " +
            "   g.open_date AS openDate, " +
            "   opt.price AS optionPrice, " +
            "   s.name AS storeName, " +

            "   g.id AS gId, " +
            "   COUNT( opt.id ) AS itemCount " +
            "FROM " +
            "   order_ o " +
            "   JOIN order_user ou ON ou.order_id = o.id " +
            "   JOIN option_ opt ON opt.id = o.option_id " +
            "   JOIN product p ON p.id = opt.product_id " +
            "   JOIN groups g ON g.id = o.group_id " +
            "   JOIN store s ON s.id = p.store_id "+
            "WHERE " +
            "   ou.user_id = :userId AND (g.open_date between :startTime AND :endTime OR :startTime IS NULL && :endTime IS NULL) " +
            "GROUP BY " +
            "   p.name, opt.id, g.id ORDER BY g.open_date, p.id ")
    List<Map<String, Object>> getUserOrderRecordDay(@Param("userId") int userId , @Param("startTime") Date startTime, @Param("endTime") Date endTime);


    @Query(nativeQuery = true, value = " " +
            "SELECT " +
            "   o.store_id AS storeId, " +
            "   p.id AS productId, " +
            "   p.name AS productName, " +
            "   p.price AS productPrice, " +
            "   opt.name AS optionName, " +
            "   g.open_date AS openDate, " +
            "   opt.price AS optionPrice, " +
            "   g.id AS gId, " +
            "   COUNT( opt.id ) AS itemCount " +
            "FROM " +
            "   order_ o " +
            "   JOIN order_user ou ON ou.order_id = o.id " +
            "   JOIN option_ opt ON opt.id = o.option_id " +
            "   JOIN product p ON p.id = opt.product_id " +
            "   JOIN groups g ON g.id = o.group_id " +
            "WHERE " +
            "   ou.user_id = :userId AND g.open_date between :startTime AND :endTime " +
            "GROUP BY " +
            "  g.id ORDER BY g.open_date ")
    List<Map<String, Object>> getUserOrderRecordWeek(@Param("userId") int userId , @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Query(nativeQuery = true, value = " " +
   " SELECT " +
   " g.open_date AS openDate, " +
   " g.id AS groupId, " +
   " g.name AS groupName, " +
   " g.is_locked AS isLocked, " +
   " g.start_time AS startTime, " +
   " g.end_time AS endTime " +
   " FROM " +
    " order_ o " +
    " JOIN order_user ou ON ou.order_id = o.id " +
    " JOIN option_ opt ON opt.id = o.option_id " +
    " JOIN product p ON p.id = opt.product_id " +
    " JOIN groups g ON g.id = o.group_id " +
     " WHERE " +
    " ou.user_id = :userId AND (g.open_date >= :startTime AND g.open_date < :endTime OR :startTime IS NULL && :endTime IS NULL)  " +
    " GROUP BY " +
    " g.id ,g.open_date ORDER BY g.open_date ",
    countQuery = "SELECT" +
            " g.open_date AS openDate, " +
            " g.id AS groupId, " +
            " g.name AS groupName, " +
            " g.is_locked AS isLocked, " +
            " g.start_time AS startTime, " +
            " g.end_time AS endTime " +
            " FROM " +
            " order_ o " +
            " JOIN order_user ou ON ou.order_id = o.id " +
            " JOIN option_ opt ON opt.id = o.option_id " +
            " JOIN product p ON p.id = opt.product_id " +
            " JOIN groups g ON g.id = o.group_id" +
            " WHERE " +
            " ou.user_id = :userId AND (g.open_date >= :startTime AND g.open_date < :endTime OR :startTime IS NULL && :endTime IS NULL) " +
            " GROUP BY " +
            " g.id ,g.open_date ORDER BY g.open_date")
    Page<Map<String, Object>> getUserRecord(@Param("userId") int userId, @Param("startTime") Date startTime, @Param("endTime") Date endTime, Pageable pageable);

    @Query(nativeQuery = true, value = " " +
            " SELECT COUNT(*) " +
            " FROM " +
            " ( " +
            " SELECT g.id " +
            " FROM " +
            " order_ o " +
            " JOIN order_user ou ON ou.order_id = o.id " +
            " JOIN option_ opt ON opt.id = o.option_id " +
            " JOIN product p ON p.id = opt.product_id " +
            " JOIN groups g ON g.id = o.group_id " +
            " WHERE " +
            " ou.user_id = :userId AND (g.open_date between :startTime AND :endTime OR :startTime IS NULL && :endTime IS NULL)  " +
            " GROUP BY " +
            " g.id ,g.open_date " +
            " ) as tb" )
    int getUserRecordCount(@Param("userId") int userId, @Param("startTime") Date startTime, @Param("endTime") Date endTime);


    @Query(nativeQuery = true, value = " " +
            "SELECT " +
            "   o.store_id AS storeId, " +


            "   g.open_date AS openDate, " +

            "   s.name AS storeName, " +

            "   g.id AS gId, " +
            "   COUNT( opt.id ) AS itemCount " +
            "FROM " +
            "   order_ o " +
            "   JOIN order_user ou ON ou.order_id = o.id " +
            "   JOIN option_ opt ON opt.id = o.option_id " +
            "   JOIN product p ON p.id = opt.product_id " +
            "   JOIN groups g ON g.id = o.group_id " +
            "   JOIN store s ON s.id = p.store_id "+
            "WHERE " +
            "   ou.user_id = :userId AND (g.open_date between :startTime AND :endTime OR :startTime IS NULL && :endTime IS NULL)   " +
            "GROUP BY " +
            " o.store_id, g.id ORDER BY s.id ")
    List<Map<String, Object>> getUserOrderRecordStore(@Param("userId") int userId , @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    @Query(value = "SELECT u.id AS userId,u.username AS userName,u.english_name AS englishName,u.`name` AS name,ut.team_id AS teamId" +
            " FROM `user` u LEFT JOIN `user_team` ut ON ut.user_id = u.id" +
            " WHERE u.id IN (:teamIdList)", nativeQuery = true)
    List<Map<String, Object>> getByTeamId(@Param("teamIdList") List<Integer> storeIdList);

    @Query(value = "SELECT DISTINCT user.id AS userId ,user.`name` AS name,`user`.english_name AS englishName FROM `user` " +
            "LEFT JOIN user_team ON user_team.user_id = `user`.id  " +
            "LEFT JOIN team ON team.id = user_team.team_id WHERE `user`.id  != 1 ORDER BY `user`.id ",nativeQuery = true)
    List<Map<String, Object>> getAllUser();

    @Query(value = "SELECT user.id AS id,user.username AS userName,user.`name` AS name,user.english_name AS englishName " +
            "FROM `user` " +
            "LEFT JOIN user_team ON user_team.user_id = `user`.id " +
            "LEFT JOIN team ON team.id = user_team.team_id " +
            "WHERE team.id = :teamId",nativeQuery = true)
    List<Map<String, Object>> getUserByTeamId(@Param("teamId") int teamId);
}
