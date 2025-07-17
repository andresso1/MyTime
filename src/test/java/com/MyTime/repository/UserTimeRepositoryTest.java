package com.MyTime.repository;

import com.MyTime.entity.User;
import com.MyTime.entity.UserTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserTimeRepositoryTest {

    @Autowired
    private UserTimeRepository userTimeRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user1;
    private User user2;
    private UserTime userTime1;
    private UserTime userTime2;

    @BeforeEach
    void setUp() {
        user1 = new User("User One", "userone", "userone@example.com", "password", "ACTIVE");
        user2 = new User("User Two", "usertwo", "usertwo@example.com", "password", "ACTIVE");
        entityManager.persist(user1);
        entityManager.persist(user2);

        userTime1 = new UserTime(user1, "Week 1", "DRAFT", "Notes 1", 1, 1, 2023);
        entityManager.persist(userTime1);

        userTime2 = new UserTime(user1, "Week 2", "SUBMITTED", "Notes 2", 2, 1, 2023);
        entityManager.persist(userTime2);

        entityManager.flush();
    }

    @Test
    void findByUserUserId_Found() {
        Optional<UserTime> foundUserTime = userTimeRepository.findByUserUserId(user1.getUserId());
        assertTrue(foundUserTime.isPresent());
        assertEquals(userTime1.getUserTimeId(), foundUserTime.get().getUserTimeId());
    }

    @Test
    void findByUserUserId_NotFound() {
        Optional<UserTime> foundUserTime = userTimeRepository.findByUserUserId(user2.getUserId());
        assertFalse(foundUserTime.isPresent());
    }

    @Test
    void existsByUserUserIdAndStatus_True() {
        boolean exists = userTimeRepository.existsByUserUserIdAndStatus(user1.getUserId(), "DRAFT");
        assertTrue(exists);
    }

    @Test
    void existsByUserUserIdAndStatus_False() {
        boolean exists = userTimeRepository.existsByUserUserIdAndStatus(user2.getUserId(), "DRAFT");
        assertFalse(exists);
    }

    @Test
    void getByUserTimeId() {
        UserTime foundUserTime = userTimeRepository.getByUserTimeId(userTime1.getUserTimeId());
        assertNotNull(foundUserTime);
        assertEquals(userTime1.getUserTimeId(), foundUserTime.getUserTimeId());
    }

    @Test
    void existsById_True() {
        boolean exists = userTimeRepository.existsById(userTime1.getUserTimeId());
        assertTrue(exists);
    }

    @Test
    void existsById_False() {
        boolean exists = userTimeRepository.existsById(999L);
        assertFalse(exists);
    }

    @Test
    void findByUserUserIdAndStatus() {
        List<UserTime> userTimes = userTimeRepository.findByUserUserIdAndStatus(user1.getUserId(), "DRAFT");
        assertNotNull(userTimes);
        assertEquals(1, userTimes.size());
        assertEquals(userTime1.getUserTimeId(), userTimes.get(0).getUserTimeId());
    }

    @Test
    void existsByUserTimeIdAndStatus_True() {
        boolean exists = userTimeRepository.existsByUserTimeIdAndStatus(userTime1.getUserTimeId(), "DRAFT");
        assertTrue(exists);
    }

    @Test
    void existsByUserTimeIdAndStatus_False() {
        boolean exists = userTimeRepository.existsByUserTimeIdAndStatus(userTime1.getUserTimeId(), "SUBMITTED");
        assertFalse(exists);
    }

    @Test
    void saveUserTime() {
        UserTime newUserTime = new UserTime(user2, "Week 3", "APPROVED", "Notes 3", 3, 1, 2023);
        UserTime savedUserTime = userTimeRepository.save(newUserTime);

        assertNotNull(savedUserTime.getUserTimeId());
        assertEquals("Week 3", savedUserTime.getName());

        Optional<UserTime> found = userTimeRepository.findById(savedUserTime.getUserTimeId());
        assertTrue(found.isPresent());
        assertEquals("Week 3", found.get().getName());
    }

    @Test
    void deleteUserTime() {
        userTimeRepository.deleteById(userTime1.getUserTimeId());
        entityManager.flush();

        Optional<UserTime> found = userTimeRepository.findById(userTime1.getUserTimeId());
        assertFalse(found.isPresent());
    }

    @Test
    void findAllUserTimes() {
        List<UserTime> userTimes = userTimeRepository.findAll();
        assertNotNull(userTimes);
        assertEquals(2, userTimes.size());
    }
}
