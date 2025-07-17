package com.MyTime.repository;

import com.MyTime.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUserName(String userName);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    List<User> findByCompanyCompanyId(Integer companyId);

    boolean existsByUserId(Integer id);

    User getByUserId(Integer userId);

    boolean existsByEmailAndStatus(String emailRequest, String active);

    User getByEmailAndStatus(String email, String active);

    boolean existsByExpirationTokenBeforeAndToken(Date now, String token);



    User findByExpirationTokenBeforeAndToken(Date now, String token);
}
