package com.MyTime.service;

import com.MyTime.entity.UserTime;
import com.MyTime.repository.UserTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserTimeService {

    @Autowired
    UserTimeRepository userTimeRepository;

    public Optional<UserTime> listByUserId(int id) {
        return userTimeRepository.findByUserUserId(id);
    }

    public List<UserTime> listByUserIdByStatusDraft(Integer id) {
        return userTimeRepository.findByUserUserIdAndStatus(id, "DRAFT");
    }
    public boolean existsByUserUserIdAndStatus(Integer id, String status) {
        return userTimeRepository.existsByUserUserIdAndStatus(id, status);
    }

    public boolean existsByIdAndStatus(long id, String status) {
        return userTimeRepository.existsByUserTimeIdAndStatus(id, status);
    }

    public void save(UserTime userTime) {
        userTimeRepository.save(userTime);
    }

    public UserTime getById(long id) {
        return userTimeRepository.getByUserTimeId(id);
    }

    public boolean existsById(Long userTimeId) {
        return userTimeRepository.existsById(userTimeId);
    }
}
