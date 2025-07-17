package com.MyTime.service;

import com.MyTime.entity.UserTime;
import com.MyTime.entity.UserTimeDetail;
import com.MyTime.repository.UserTimeDetailRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserTimeDetailServiceTest {

    @Mock
    private UserTimeDetailRepository userTimeDetailRepository;

    @InjectMocks
    private UserTimeDetailService userTimeDetailService;

    private UserTimeDetail userTimeDetail1;
    private UserTimeDetail userTimeDetail2;
    private UserTime userTime;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        userTime = new UserTime();
        userTime.setUserTimeId(1L);
        userTime.setStatus("DRAFT");

        userTimeDetail1 = new UserTimeDetail();
        userTimeDetail1.setUserTimeDetailId(1L);
        userTimeDetail1.setUserTime(userTime);
        userTimeDetail1.setProjectId(100L);

        userTimeDetail2 = new UserTimeDetail();
        userTimeDetail2.setUserTimeDetailId(2L);
        userTimeDetail2.setUserTime(userTime);
        userTimeDetail2.setProjectId(200L);
    }

    @Test
    void save() {
        userTimeDetailService.save(userTimeDetail1);

        verify(userTimeDetailRepository, times(1)).save(userTimeDetail1);
    }

    @Test
    void getById() {
        when(userTimeDetailRepository.getByUserTimeDetailId(1L)).thenReturn(userTimeDetail1);

        UserTimeDetail foundDetail = userTimeDetailService.getById(1L);

        assertNotNull(foundDetail);
        assertEquals(1L, foundDetail.getUserTimeDetailId());
    }

    @Test
    void existsByUserTimeStatusAndProjectIdIn_True() {
        List<Long> projectIds = Arrays.asList(100L, 200L);
        when(userTimeDetailRepository.existsByUserTimeStatusAndProjectIdIn("DRAFT", projectIds)).thenReturn(true);

        boolean exists = userTimeDetailService.existsByUserTimeStatusAndProjectIdIn("DRAFT", projectIds);

        assertTrue(exists);
    }

    @Test
    void existsByUserTimeStatusAndProjectIdIn_False() {
        List<Long> projectIds = Arrays.asList(300L);
        when(userTimeDetailRepository.existsByUserTimeStatusAndProjectIdIn("DRAFT", projectIds)).thenReturn(false);

        boolean exists = userTimeDetailService.existsByUserTimeStatusAndProjectIdIn("DRAFT", projectIds);

        assertFalse(exists);
    }

    @Test
    void ListByUserTimeStatusAndProjectIdIn() {
        List<Long> projectIds = Arrays.asList(100L, 200L);
        when(userTimeDetailRepository.findByUserTimeStatusAndProjectIdIn("DRAFT", projectIds)).thenReturn(Arrays.asList(userTimeDetail1, userTimeDetail2));

        List<UserTimeDetail> details = userTimeDetailService.ListByUserTimeStatusAndProjectIdIn("DRAFT", projectIds);

        assertNotNull(details);
        assertEquals(2, details.size());
    }

    @Test
    void existsByUserTimeStatusAndUserTimeUserUserId_True() {
        when(userTimeDetailRepository.existsByUserTimeStatusAndUserTimeUserUserId("DRAFT", 1)).thenReturn(true);

        boolean exists = userTimeDetailService.existsByUserTimeStatusAndUserTimeUserUserId("DRAFT", 1);

        assertTrue(exists);
    }

    @Test
    void existsByUserTimeStatusAndUserTimeUserUserId_False() {
        when(userTimeDetailRepository.existsByUserTimeStatusAndUserTimeUserUserId("DRAFT", 1)).thenReturn(false);

        boolean exists = userTimeDetailService.existsByUserTimeStatusAndUserTimeUserUserId("DRAFT", 1);

        assertFalse(exists);
    }

    @Test
    void ListByUserTimeStatusAndUserTimeUserUserId() {
        when(userTimeDetailRepository.findByUserTimeStatusAndUserTimeUserUserId("DRAFT", 1)).thenReturn(Arrays.asList(userTimeDetail1, userTimeDetail2));

        List<UserTimeDetail> details = userTimeDetailService.ListByUserTimeStatusAndUserTimeUserUserId("DRAFT", 1);

        assertNotNull(details);
        assertEquals(2, details.size());
    }

    @Test
    void deleteByUserTimeUserTimeId() {
        doNothing().when(userTimeDetailRepository).deleteByUserTimeUserTimeId(1L);

        userTimeDetailService.deleteByUserTimeUserTimeId(1L);

        verify(userTimeDetailRepository, times(1)).deleteByUserTimeUserTimeId(1L);
    }
}
