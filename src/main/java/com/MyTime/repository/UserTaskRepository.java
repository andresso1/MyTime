package com.MyTime.repository;

import com.MyTime.entity.UserTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserTaskRepository extends JpaRepository<UserTask, Long> {

    //List<UserTask> findByUserId(Integer id);

    boolean existsByUserId(Integer id);

    List<UserTask> findByUserId(Integer id);

    @Query(value = "delete from user_task where user_id=:userIdToDelete",nativeQuery = true)
    @Modifying
    @Transactional
    void deleteByUserId(@Param("userIdToDelete") Integer userId);
}
