package com.MyTime.service;

import com.MyTime.entity.UserTime;
import com.MyTime.repository.UserTimeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserTimeServiceTest {

    @Mock
    private UserTimeRepository userTimeRepository;

    @InjectMocks
    private UserTimeService userTimeService;

    private UserTime userTime1;
    private UserTime userTime2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        userTime1 = new UserTime();
        userTime1.setUserTimeId(1L);
        userTime1.setStatus("DRAFT");

        userTime2 = new UserTime();
        userTime2.setUserTimeId(2L);
        userTime2.setStatus("APPROVED");
    }

    @Test
    void listByUserId_Found() {
        when(userTimeRepository.findByUserUserId(1)).thenReturn(Optional.of(userTime1));

        Optional<UserTime> foundUserTime = userTimeService.listByUserId(1);

        assertTrue(foundUserTime.isPresent());
        assertEquals(1L, foundUserTime.get().getUserTimeId());
    }

    @Test
    void listByUserId_NotFound() {
        when(userTimeRepository.findByUserUserId(1)).thenReturn(Optional.empty());

        Optional<UserTime> foundUserTime = userTimeService.listByUserId(1);

        assertFalse(foundUserTime.isPresent());
    }

    @Test
    void listByUserIdByStatusDraft() {
        when(userTimeRepository.findByUserUserIdAndStatus(1, "DRAFT")).thenReturn(Arrays.asList(userTime1));

        List<UserTime> userTimes = userTimeService.listByUserIdByStatusDraft(1);

        assertNotNull(userTimes);
        assertEquals(1, userTimes.size());
        assertEquals("DRAFT", userTimes.get(0).getStatus());
    }

    @Test
    void existsByUserUserIdAndStatus_True() {
        when(userTimeRepository.existsByUserUserIdAndStatus(1, "DRAFT")).thenReturn(true);

        boolean exists = userTimeService.existsByUserUserIdAndStatus(1, "DRAFT");

        assertTrue(exists);
    }

    @Test
    void existsByUserUserIdAndStatus_False() {
        when(userTimeRepository.existsByUserUserIdAndStatus(1, "DRAFT")).thenReturn(false);

        boolean exists = userTimeService.existsByUserUserIdAndStatus(1, "DRAFT");

        assertFalse(exists);
    }

    @Test
    void existsByIdAndStatus_True() {
        when(userTimeRepository.existsByUserTimeIdAndStatus(1L, "DRAFT")).thenReturn(true);

        boolean exists = userTimeService.existsByIdAndStatus(1L, "DRAFT");

        assertTrue(exists);
    }

    @Test
    void existsByIdAndStatus_False() {
        when(userTimeRepository.existsByUserTimeIdAndStatus(1L, "DRAFT")).thenReturn(false);

        boolean exists = userTimeService.existsByIdAndStatus(1L, "DRAFT");

        assertFalse(exists);
    }

    @Test
    void save() {
        userTimeService.save(userTime1);

        verify(userTimeRepository, times(1)).save(userTime1);
    }

    @Test
    void getById() {
        when(userTimeRepository.getByUserTimeId(1L)).thenReturn(userTime1);

        UserTime foundUserTime = userTimeService.getById(1L);

        assertNotNull(foundUserTime);
        assertEquals(1L, foundUserTime.getUserTimeId());
    }

    @Test
    void existsById_True() {
        when(userTimeRepository.existsById(1L)).thenReturn(true);

        boolean exists = userTimeService.existsById(1L);

        assertTrue(exists);
    }

    @Test
    void existsById_False() {
        when(userTimeRepository.existsById(1L)).thenReturn(false);

        boolean exists = userTimeService.existsById(1L);

        assertFalse(exists);
    }
}
