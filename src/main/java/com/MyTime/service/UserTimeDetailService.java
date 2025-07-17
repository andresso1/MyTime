package com.MyTime.service;

import com.MyTime.entity.UserTime;
import com.MyTime.entity.UserTimeDetail;
import com.MyTime.repository.UserTimeDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserTimeDetailService {

    @Autowired
    UserTimeDetailRepository userTimeDetailRepository;

    public void save(UserTimeDetail userTimeDetail) {
        userTimeDetailRepository.save(userTimeDetail);
    }

    public UserTimeDetail getById(long id) {
        return userTimeDetailRepository.getByUserTimeDetailId(id);
    }

    public boolean existsByUserTimeStatusAndProjectIdIn(String status, List<Long> ids) {
        return userTimeDetailRepository.existsByUserTimeStatusAndProjectIdIn(status, ids);
    }

    public List<UserTimeDetail> ListByUserTimeStatusAndProjectIdIn(String status, List<Long> ids) {
        return userTimeDetailRepository.findByUserTimeStatusAndProjectIdIn(status, ids);
    }

    public boolean existsByUserTimeStatusAndUserTimeUserUserId(String status, Integer id) {
        return userTimeDetailRepository.existsByUserTimeStatusAndUserTimeUserUserId(status, id);
    }

    public List<UserTimeDetail> ListByUserTimeStatusAndUserTimeUserUserId(String status, Integer id) {
        return userTimeDetailRepository.findByUserTimeStatusAndUserTimeUserUserId(status, id);
    }

    public void deleteByUserTimeUserTimeId(Long userTimeId) {
        userTimeDetailRepository.deleteByUserTimeUserTimeId(userTimeId);
    }
}
