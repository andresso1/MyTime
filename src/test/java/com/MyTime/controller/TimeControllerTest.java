package com.MyTime.controller;

import com.MyTime.dto.UserTimeDetailsDayDto;
import com.MyTime.dto.SetStatusTimeDto;
import com.MyTime.dto.UserTimeDetailDto;
import com.MyTime.dto.UserTimeDto;
import com.MyTime.entity.User;
import com.MyTime.entity.UserTime;
import com.MyTime.entity.UserTimeDetail;
import com.MyTime.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TimeControllerTest {

    @Mock
    private UserTimeService userTimeService;
    @Mock
    private UserTimeDetailService userTimeDetailService;
    @Mock
    private UserTaskService userTaskService;
    @Mock
    private UserService userService;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private TimeController timeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void listById() {
        when(userTimeService.existsByIdAndStatus(1L, "DRAFT")).thenReturn(true);
        when(userTimeService.getById(1L)).thenReturn(new UserTime());

        ResponseEntity<UserTime> response = timeController.listById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void listDetailDraftByUserId() {
        when(userTimeDetailService.existsByUserTimeStatusAndUserTimeUserUserId("DRAFT", 1)).thenReturn(true);
        when(userTimeDetailService.ListByUserTimeStatusAndUserTimeUserUserId("DRAFT", 1)).thenReturn(Collections.emptyList());

        ResponseEntity<List<UserTimeDetail>> response = timeController.listDetailDraftByUserId(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void listActiveByUserId() {
        when(userTimeService.existsByUserUserIdAndStatus(1, "DRAFT")).thenReturn(true);
        when(userTimeService.listByUserIdByStatusDraft(1)).thenReturn(new ArrayList<UserTime>());

        ResponseEntity<List<UserTime>> response = timeController.listActiveByUserId(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void approvalsListActiveByUserId() {
        when(userTaskService.listByUserId(1)).thenReturn(Collections.emptyList());
        when(userTimeDetailService.existsByUserTimeStatusAndProjectIdIn(any(), any())).thenReturn(true);
        when(userTimeDetailService.ListByUserTimeStatusAndProjectIdIn(any(), any())).thenReturn(new ArrayList<UserTimeDetail>());

        ResponseEntity<List<UserTimeDetail>> response = timeController.approvalsListActiveByUserId(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void changeStatus() throws MessagingException, IOException {
        SetStatusTimeDto statusTimeDto = new SetStatusTimeDto();
        statusTimeDto.setUserTimeId(1L);
        statusTimeDto.setStatus("APPROVED");

        UserTime userTime = new UserTime();
        userTime.setUser(new User());

        when(userTimeService.existsById(1L)).thenReturn(true);
        when(userTimeService.getById(1L)).thenReturn(userTime);
        when(userService.getById(any())).thenReturn(new User());

        ResponseEntity<?> response = timeController.changeStatus(statusTimeDto, mock(BindingResult.class));

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void create() {
        UserTimeDto userTimeDto = new UserTimeDto();
        userTimeDto.setUserId(1);

        when(userTimeService.existsByUserUserIdAndStatus(1, "DRAFT")).thenReturn(false);
        when(userService.getById(1)).thenReturn(new User());

        ResponseEntity<?> response = timeController.create(userTimeDto, mock(BindingResult.class));

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createDetail() {
        UserTimeDetailDto userTimeDetailDto = new UserTimeDetailDto();
        UserTime userTimeInDto = new UserTime();
        userTimeInDto.setUserTimeId(1L);
        userTimeDetailDto.setUserTime(userTimeInDto);
        userTimeDetailDto.setUserTimeDetailsDayDto(new ArrayList<UserTimeDetailsDayDto>());

        UserTime mockedUserTime = new UserTime();
        mockedUserTime.setUserTimeId(1L);
        when(userTimeService.getById(anyLong())).thenReturn(mockedUserTime);

        ResponseEntity<?> response = timeController.createDetail(userTimeDetailDto, mock(BindingResult.class));

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
