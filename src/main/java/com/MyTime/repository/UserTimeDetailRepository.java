package com.MyTime.repository;


import com.MyTime.entity.UserTime;
import com.MyTime.entity.UserTimeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTimeDetailRepository extends JpaRepository<UserTimeDetail, Long> {

    UserTimeDetail getByUserTimeDetailId(long id);

    boolean existsByUserTimeStatusAndProjectIdIn(String status, List<Long> ids);

    List<UserTimeDetail> findByUserTimeStatusAndProjectIdIn(String status, List<Long> ids);

    boolean existsByUserTimeStatusAndUserTimeUserUserId(String status, Integer id);

    List<UserTimeDetail> findByUserTimeStatusAndUserTimeUserUserId(String status, Integer id);

    void deleteByUserTimeUserTimeId(Long userTimeId);
}
