package com.MyTime.repository;

import org.springframework.stereotype.Repository;

import com.MyTime.entity.UserTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTimeRepository extends JpaRepository<UserTime, Long> {
    Optional<UserTime> findByUserUserId(int id);
    boolean existsByUserUserIdAndStatus(Integer userid, String status);
    UserTime getByUserTimeId(long id);

    boolean existsById(long userTimeId);

    List<UserTime> findByUserUserIdAndStatus(Integer id, String draft);

    boolean existsByUserTimeIdAndStatus(long id, String status);
}
