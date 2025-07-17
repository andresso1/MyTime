package com.MyTime.controller;

import com.MyTime.dto.Message;
import com.MyTime.dto.SetStatusTimeDto;
import com.MyTime.dto.UserTimeDetailDto;
import com.MyTime.dto.UserTimeDto;
import com.MyTime.entity.User;
import com.MyTime.entity.UserTask;
import com.MyTime.entity.UserTime;
import com.MyTime.entity.UserTimeDetail;
import com.MyTime.enums.TimeStatus;
import com.MyTime.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/time")
@CrossOrigin(origins = "*")
public class TimeController {

    @Autowired
    UserTimeService userTimeService;

    @Autowired
    UserTimeDetailService userTimeDetailService;

    @Autowired
    UserTaskService userTaskService;

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    private @Autowired HttpServletRequest request;


    @GetMapping("/detail/{id}")
    public ResponseEntity<UserTime> listById(@PathVariable("id") long id) {
        if (!userTimeService.existsByIdAndStatus(id, "DRAFT"))
            return new ResponseEntity(new Message("User not have assigned nothing pending"), HttpStatus.FOUND);
        UserTime list = userTimeService.getById(id);
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/listDetail/{id}")
    public ResponseEntity<List<UserTimeDetail>> listDetailDraftByUserId(@PathVariable("id") Integer id) {

        if (!userTimeDetailService.existsByUserTimeStatusAndUserTimeUserUserId(String.valueOf(TimeStatus.DRAFT),id))
            return new ResponseEntity(new Message("Detail not found for userTime:"+id), HttpStatus.NOT_FOUND);

        List<UserTimeDetail> list = userTimeDetailService.ListByUserTimeStatusAndUserTimeUserUserId(String.valueOf(TimeStatus.DRAFT),id);

        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<List<UserTime>> listActiveByUserId(@PathVariable("id") Integer id) {
        if (!userTimeService.existsByUserUserIdAndStatus(id, "DRAFT"))
            return new ResponseEntity(new Message("User not have assigned nothing pending"), HttpStatus.FOUND);
        List<UserTime> list = userTimeService.listByUserIdByStatusDraft(id);
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/approvalsList/{id}")
    public ResponseEntity<List<UserTimeDetail>> approvalsListActiveByUserId(@PathVariable("id") int id) {

        List<UserTask> listTask = userTaskService.listByUserId(id);

        List<Long> ids = new ArrayList<Long>();

        listTask.forEach(userTask -> {
            ids.add(userTask.getTask().getProject().getProjectId().longValue());
        });
        List<Long> newListIds = ids.stream()
                .distinct()
                .collect(Collectors.toList());

        if (!userTimeDetailService.existsByUserTimeStatusAndProjectIdIn(String.valueOf(TimeStatus.SUBMITTED),newListIds))
            return new ResponseEntity(new Message("projects assigned with nothing pending"), HttpStatus.FOUND);

        List<UserTimeDetail> list = userTimeDetailService.ListByUserTimeStatusAndProjectIdIn(String.valueOf(TimeStatus.SUBMITTED),newListIds);

        return new ResponseEntity(list, HttpStatus.OK);
    }

    @PostMapping("/changeStatus")
    public ResponseEntity<?> changeStatus(@Valid @RequestBody SetStatusTimeDto statusTimeDto, BindingResult bindingResult) throws MessagingException, IOException {
        if (bindingResult.hasErrors())
            return new ResponseEntity(new Message("error in fields"), HttpStatus.BAD_REQUEST);
        if (!userTimeService.existsById(statusTimeDto.getUserTimeId()))
            return new ResponseEntity(new Message("timesheet not exist"), HttpStatus.NOT_FOUND);

        UserTime userTime = userTimeService.getById(statusTimeDto.getUserTimeId());

        User user = userService.getById(userTime.getUser().getUserId());

        if (statusTimeDto.getStatus().contains("REFUSE")) {
            userTime.setStatus(String.valueOf(TimeStatus.DRAFT));
            emailService.sendEmailEvent(
                    user.getEmail(),
                    "Activities Rejected ",
                    contentRefuseActivitiesHTML(user.getName())
            );

        } else {
            userTime.setStatus(String.valueOf(TimeStatus.APPROVED));
        }

        userTime.setNotes(statusTimeDto.getNotes());
        userTimeService.save(userTime);

        if (statusTimeDto.getStatus().contains("REFUSE")) {
            return new ResponseEntity(new Message("Timesheet Refused"), HttpStatus.OK);
        }

        return new ResponseEntity(new Message("Timesheet Approved"), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody UserTimeDto userTimeDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity(new Message("error in fields"), HttpStatus.BAD_REQUEST);
        if (userTimeService.existsByUserUserIdAndStatus(userTimeDto.getUserId(), "DRAFT"))
            return new ResponseEntity(new Message("User have assigned pending"), HttpStatus.NOT_FOUND);
        User user = userService.getById(userTimeDto.getUserId());

        UserTime userTime = new UserTime(user, userTimeDto.getName(),
                userTimeDto.getStatus(), userTimeDto.getNotes(), userTimeDto.getWeek(),
                userTimeDto.getMonth(), userTimeDto.getYear());

        userTimeService.save(userTime);

        return new ResponseEntity(new Message("New Timesheet Assigned"), HttpStatus.OK);
    }


    @PostMapping("/createDetail")
    public ResponseEntity<?> createDetail(@Valid @RequestBody UserTimeDetailDto userTimeDetailDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity(new Message("error in fields"), HttpStatus.BAD_REQUEST);
        System.out.println("userTimeDetailDto = " + userTimeDetailDto);

        UserTime userTime = userTimeService.getById(userTimeDetailDto.getUserTime().getUserTimeId());

        userTimeDetailService.deleteByUserTimeUserTimeId(userTimeDetailDto.getUserTime().getUserTimeId());
        userTimeDetailDto.getUserTimeDetailsDayDto().forEach(
                userTimeDetailsDayDto -> {

                    UserTimeDetail usd = new UserTimeDetail(
                            userTimeDetailsDayDto.getProjectId(),
                            userTimeDetailsDayDto.getTaskId(),
                            userTimeDetailsDayDto.getTypeId(),
                            userTimeDetailsDayDto.getSat(),
                            userTimeDetailsDayDto.getSun(),
                            userTimeDetailsDayDto.getMon(),
                            userTimeDetailsDayDto.getTus(),
                            userTimeDetailsDayDto.getWen(),
                            userTimeDetailsDayDto.getTru(),
                            userTimeDetailsDayDto.getFry(),
                            userTime,
                            null
                    );
                    userTimeDetailService.save(usd);
                }

        );

        userTime.setStatus(String.valueOf(TimeStatus.SUBMITTED));
        userTimeService.save(userTime);

        return new ResponseEntity(new Message("New Timesheet Submitted"), HttpStatus.OK);
    }

    private static String contentRefuseActivitiesHTML(String userName) {
        StringBuilder sb = new StringBuilder();

        sb.append("<html>\n");
        sb.append("<head>\n");
        sb.append("  <style>\n");
        sb.append("    body { font-family: 'Arial', sans-serif; }\n");
        sb.append("    .container { max-width: 600px; margin: 0 auto; padding: 20px; }\n");
        sb.append("    h2 { color: #333; }\n");
        sb.append("    p { color: #666; }\n");
        sb.append("    .footer { margin-top: 20px; color: #999; }\n");
        sb.append("  </style>\n");
        sb.append("</head>\n");
        sb.append("<body>\n");
        sb.append("<div class=\"container\">\n");
        sb.append("  <h2>Activities Rejected </h2>\n");
        sb.append("  <p>Dear  ").append(userName).append(",</p>\n");
        sb.append("  <p>We regret to inform you that your submitted activity has been rejected. After careful review, it was determined that the activity does not meet the requirements established.</p>\n");
        sb.append("  <p>If you have any questions or would like further clarification, please don't hesitate to contact your manager team.</p>\n");
        sb.append("  <p>Thank you for your understanding.</p>\n");
        sb.append("  <p>Best regards, The Team at ").append("Mytime.com").append("</p>\n");
        sb.append("  <div class=\"footer\">\n");
        sb.append("    <p>© 2023 ").append("Mytime.com").append(". All rights reserved.</p>\n");
        sb.append("  </div>\n");
        sb.append("</div>");
        sb.append("</body>\n");
        sb.append("</html>");

        return sb.toString();
    }

}
