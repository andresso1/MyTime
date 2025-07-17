package com.MyTime.repository;

import com.MyTime.entity.User;
import com.MyTime.entity.UserTime;
import com.MyTime.entity.UserTimeDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserTimeDetailRepositoryTest {

    @Autowired
    private UserTimeDetailRepository userTimeDetailRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user;
    private UserTime userTime1;
    private UserTime userTime2;
    private UserTimeDetail userTimeDetail1;
    private UserTimeDetail userTimeDetail2;
    private UserTimeDetail userTimeDetail3;

    @BeforeEach
    void setUp() {
        user = new User("Test User", "testuser", "test@example.com", "password", "ACTIVE");
        entityManager.persist(user);

        userTime1 = new UserTime(user, "Week 1", "DRAFT", "Notes 1", 1, 1, 2023);
        entityManager.persist(userTime1);

        userTime2 = new UserTime(user, "Week 2", "SUBMITTED", "Notes 2", 2, 1, 2023);
        entityManager.persist(userTime2);

        userTimeDetail1 = new UserTimeDetail(100L, 10L, "TYPE1", 8, 0, 8, 8, 8, 8, 8, userTime1, "Detail Notes 1");
        userTimeDetail2 = new UserTimeDetail(200L, 20L, "TYPE2", 8, 8, 0, 8, 8, 8, 8, userTime1, "Detail Notes 2");
        userTimeDetail3 = new UserTimeDetail(100L, 10L, "TYPE3", 8, 0, 8, 8, 8, 8, 8, userTime2, "Detail Notes 3");

        entityManager.persist(userTimeDetail1);
        entityManager.persist(userTimeDetail2);
        entityManager.persist(userTimeDetail3);
        entityManager.flush();
    }

    @Test
    void getByUserTimeDetailId() {
        UserTimeDetail foundDetail = userTimeDetailRepository.getByUserTimeDetailId(userTimeDetail1.getUserTimeDetailId());
        assertNotNull(foundDetail);
        assertEquals(userTimeDetail1.getUserTimeDetailId(), foundDetail.getUserTimeDetailId());
    }

    @Test
    void existsByUserTimeStatusAndProjectIdIn_True() {
        List<Long> projectIds = Arrays.asList(100L, 200L);
        boolean exists = userTimeDetailRepository.existsByUserTimeStatusAndProjectIdIn("DRAFT", projectIds);
        assertTrue(exists);
    }

    @Test
    void existsByUserTimeStatusAndProjectIdIn_False() {
        List<Long> projectIds = Arrays.asList(300L);
        boolean exists = userTimeDetailRepository.existsByUserTimeStatusAndProjectIdIn("DRAFT", projectIds);
        assertFalse(exists);
    }

    @Test
    void findByUserTimeStatusAndProjectIdIn() {
        List<Long> projectIds = Arrays.asList(100L, 200L);
        List<UserTimeDetail> details = userTimeDetailRepository.findByUserTimeStatusAndProjectIdIn("DRAFT", projectIds);
        assertNotNull(details);
        assertEquals(2, details.size());
        assertTrue(details.stream().anyMatch(d -> d.getUserTimeDetailId().equals(userTimeDetail1.getUserTimeDetailId())));
        assertTrue(details.stream().anyMatch(d -> d.getUserTimeDetailId().equals(userTimeDetail2.getUserTimeDetailId())));
    }

    @Test
    void existsByUserTimeStatusAndUserTimeUserUserId_True() {
        boolean exists = userTimeDetailRepository.existsByUserTimeStatusAndUserTimeUserUserId("DRAFT", user.getUserId());
        assertTrue(exists);
    }

    @Test
    void existsByUserTimeStatusAndUserTimeUserUserId_False() {
        boolean exists = userTimeDetailRepository.existsByUserTimeStatusAndUserTimeUserUserId("APPROVED", user.getUserId());
        assertFalse(exists);
    }

    @Test
    void findByUserTimeStatusAndUserTimeUserUserId() {
        List<UserTimeDetail> details = userTimeDetailRepository.findByUserTimeStatusAndUserTimeUserUserId("DRAFT", user.getUserId());
        assertNotNull(details);
        assertEquals(2, details.size());
        assertTrue(details.stream().anyMatch(d -> d.getUserTimeDetailId().equals(userTimeDetail1.getUserTimeDetailId())));
        assertTrue(details.stream().anyMatch(d -> d.getUserTimeDetailId().equals(userTimeDetail2.getUserTimeDetailId())));
    }

    @Test
    void deleteByUserTimeUserTimeId() {
        userTimeDetailRepository.deleteByUserTimeUserTimeId(userTime1.getUserTimeId());
        entityManager.flush();

        List<UserTimeDetail> remainingDetails = userTimeDetailRepository.findByUserTimeStatusAndUserTimeUserUserId("DRAFT", user.getUserId());
        assertTrue(remainingDetails.isEmpty());
    }

    @Test
    void saveUserTimeDetail() {
        UserTimeDetail newDetail = new UserTimeDetail(300L, 30L, "TYPE4", 1, 1, 1, 1, 1, 1, 1, userTime1, "New Detail");
        UserTimeDetail savedDetail = userTimeDetailRepository.save(newDetail);

        assertNotNull(savedDetail.getUserTimeDetailId());
        assertEquals("New Detail", savedDetail.getNotes());

        UserTimeDetail found = userTimeDetailRepository.getByUserTimeDetailId(savedDetail.getUserTimeDetailId());
        assertNotNull(found);
        assertEquals("New Detail", found.getNotes());
    }

    @Test
    void findById() {
        Optional<UserTimeDetail> found = userTimeDetailRepository.findById(userTimeDetail1.getUserTimeDetailId());
        assertTrue(found.isPresent());
        assertEquals(userTimeDetail1.getUserTimeDetailId(), found.get().getUserTimeDetailId());
    }

    @Test
    void findAll() {
        List<UserTimeDetail> allDetails = userTimeDetailRepository.findAll();
        assertNotNull(allDetails);
        assertEquals(3, allDetails.size());
    }

    @Test
    void deleteById() {
        userTimeDetailRepository.deleteById(userTimeDetail1.getUserTimeDetailId());
        entityManager.flush();

        Optional<UserTimeDetail> found = userTimeDetailRepository.findById(userTimeDetail1.getUserTimeDetailId());
        assertFalse(found.isPresent());
    }
}
