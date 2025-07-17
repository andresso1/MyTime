package com.MyTime.service;

import com.MyTime.entity.User;
import com.MyTime.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    @Autowired
    UserRepository userRepository;

    public Optional<User> getByUserName(String userName){
        return userRepository.findByUserName(userName);
    }

    public boolean existsByUserName(String userName){
        return userRepository.existsByUserName(userName);
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public void save(User user){
        userRepository.save(user);
    }

    public List<User> listByCompanyId(Integer companyId) {
        return userRepository.findByCompanyCompanyId(companyId);
    }

    public User getById(Integer userId) {
        return userRepository.getByUserId(userId);
    }

    public boolean existsById(int id) {
        return userRepository.existsByUserId(id);
    }

    public boolean existsByEmailAndStatus(String emailRequest) {
        return userRepository.existsByEmailAndStatus(emailRequest, "ACTIVE");
    }

    public User getByEmailAndStatus(String email) {
        return userRepository.getByEmailAndStatus(email, "ACTIVE");
    }

    public boolean existsByGreaterThanEqualExpirationTokenAndToken(Date now, String token) {

        return userRepository.existsByExpirationTokenBeforeAndToken(now, token);
    }

    public User getByGreaterThanEqualExpirationTokenAndToken(Date now, String token) {
        return userRepository.findByExpirationTokenBeforeAndToken(now, token);
    }
}
