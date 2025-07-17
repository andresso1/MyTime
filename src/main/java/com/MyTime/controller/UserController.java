package com.MyTime.controller;

import com.MyTime.dto.Message;
import com.MyTime.dto.ResetPasswordDto;
import com.MyTime.dto.UserDto;
import com.MyTime.entity.Company;
import com.MyTime.entity.Rol;
import com.MyTime.entity.User;
import com.MyTime.enums.RolName;
import com.MyTime.security.jwt.JwtProvider;
import com.MyTime.service.EmailService;
import com.MyTime.service.RolService;
import com.MyTime.service.CompanyService;
import com.MyTime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    CompanyService companyService;
    @Autowired
    RolService rolService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    EmailService emailService;

    @GetMapping("/list/{id}")
    public ResponseEntity<List<User>> listByCompanyId(@PathVariable("id") int id) {
        if (!companyService.existsById(id))
            return new ResponseEntity(new Message("company not exist"), HttpStatus.NOT_FOUND);
        List<User> list = userService.listByCompanyId(id);
        return new ResponseEntity(list, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody UserDto userDto, BindingResult bindingResult) throws MessagingException, IOException {
        if (bindingResult.hasErrors())
            return new ResponseEntity(new Message("error in fields or erroneous email"), HttpStatus.BAD_REQUEST);
        if (userService.existsByUserName(userDto.getUserName()))
            return new ResponseEntity(new Message("Name really exists"), HttpStatus.BAD_REQUEST);
        if (userService.existsByEmail(userDto.getEmail()))
            return new ResponseEntity(new Message("Email really exists"), HttpStatus.BAD_REQUEST);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String jwt = jwtProvider.generateToken(authentication);

        Integer orgId = jwtProvider.getCompanyIdFromToken(jwt);

        Company company = companyService.getByCompanyId(orgId);

        User user =
                new User(userDto.getName(), userDto.getUserName(), userDto.getEmail(),
                        passwordEncoder.encode(userDto.getPassword()), "ACTIVE");
        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.getByRolName(RolName.ROLE_USER).get());
        if (userDto.getRoles().contains("admin"))
            roles.add(rolService.getByRolName(RolName.ROLE_ADMIN).get());
        if (userDto.getRoles().contains("approver"))
            roles.add(rolService.getByRolName(RolName.ROLE_APPROVER).get());
        user.setRoles(roles);

        user.setCompany(company);

        user.toString();

        userService.save(user);

        emailService.sendEmailEvent(user.getEmail(), "Welcome to MyTime.com", contentNewUserHTML(userDto.getName(), userDto.getUserName()));

        return new ResponseEntity(new Message("user created"), HttpStatus.OK);
    }

    private static String contentNewUserHTML(String name, String userName) {
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

        // principal content email
        sb.append("<div class=\"container\">\n");
        sb.append("  <h2>Welcome to MyTime.com").append("!</h2>\n");
        sb.append("  <p>Dear ").append(name).append(",</p>\n");
        sb.append("  <p>Thank you for joining MyTime.com ").append(". We're excited to have you on board!</p>\n");
        sb.append("  <p>**Next Steps:**</p>\n");
        sb.append("  <p>1. **Sign In:**</p>\n");
        sb.append("  <p>Use your username :").append(userName).append(" and the password you provided during registration to log in to MyTime.com.</p>\n");
        sb.append("  <p>2. **Explore Features:**</p>\n");
        sb.append("  <p>Discover all the exciting functionalities we've prepared for you. From MyProjects, My Assignment to My Times, we are confident you will find useful and enjoyable tools.</p>\n");
        sb.append("  <p>**Assistance and Support:**</p>\n");
        sb.append("  <p>If you have any questions or encounter any issues, our support team is ready to assist you. Feel free to reach out to us at support@Mytime.com.</p>\n");
        sb.append("</div>\n");

        // Pie de página
        sb.append("<div class=\"footer\">\n");
        sb.append("  <p>© 2023 MyTime.com ").append(". All rights reserved.</p>\n");
        sb.append("</div>\n");

        sb.append("</body>\n");
        sb.append("</html>");
        return sb.toString();
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<User> getById(@PathVariable("id") int id) {
        if (!userService.existsById(id))
            return new ResponseEntity(new Message("not exist"), HttpStatus.NOT_FOUND);
        User user = userService.getById(id);
        return new ResponseEntity(user, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") int id, @RequestBody UserDto userDto) {

        User user = userService.getById(id);

        if (!user.getUserName().equals(userDto.getUserName()) && userService.existsByUserName(userDto.getUserName()))
            return new ResponseEntity(new Message("Name really exists"), HttpStatus.BAD_REQUEST);
        if (!user.getEmail().equals(userDto.getEmail()) && userService.existsByEmail(userDto.getEmail()))
            return new ResponseEntity(new Message("Email really exists"), HttpStatus.BAD_REQUEST);

        user.setUserName(userDto.getUserName());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        if (!userDto.getPassword().equals("")) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        user.setStatus(userDto.getStatus());
        Set<Rol> roles = new HashSet<>();
        System.out.println();

        if (user.getRoles().contains(rolService.getByRolName(RolName.ROLE_ADMIN).get()))
            roles.add(rolService.getByRolName(RolName.ROLE_ADMIN).get());

        if (userDto.getRoles().contains("user"))
            roles.add(rolService.getByRolName(RolName.ROLE_USER).get());

        if (userDto.getRoles().contains("approver"))
            roles.add(rolService.getByRolName(RolName.ROLE_APPROVER).get());

        user.setRoles(roles);

        userService.save(user);

        return new ResponseEntity(new Message("user updated"), HttpStatus.OK);
    }



}
