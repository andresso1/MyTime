package com.MyTime.controller;

import com.MyTime.dto.*;
import com.MyTime.entity.Company;
import com.MyTime.entity.User;
import com.MyTime.security.jwt.JwtProvider;

import com.MyTime.entity.Rol;
import com.MyTime.enums.RolName;
import com.MyTime.service.EmailService;
import com.MyTime.service.RolService;
import com.MyTime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;
    @Autowired
    RolService rolService;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    EmailService emailService;

    private @Autowired HttpServletRequest request;

    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevo(@Valid @RequestBody NuevoUsuario nuevoUsuario, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Message("error in fields or erroneous email"), HttpStatus.BAD_REQUEST);
        if(userService.existsByUserName(nuevoUsuario.getNombreUsuario()))
            return new ResponseEntity(new Message("Name really exists"), HttpStatus.BAD_REQUEST);
        if(userService.existsByEmail(nuevoUsuario.getEmail()))
            return new ResponseEntity(new Message("Email really exists"), HttpStatus.BAD_REQUEST);

        Company company = new Company(nuevoUsuario.getNameCompany(),  nuevoUsuario.getAddress(),  nuevoUsuario.getPhone(),  nuevoUsuario.getNit());

        User usuario =
                new User(nuevoUsuario.getNombre(), nuevoUsuario.getNombreUsuario(), nuevoUsuario.getEmail(),
                        passwordEncoder.encode(nuevoUsuario.getPassword()), "ACTIVE");
        Set<Rol> roles = new HashSet<>();
        roles.add(rolService.getByRolName(RolName.ROLE_USER).get());
        if(nuevoUsuario.getRoles().contains("admin"))
            roles.add(rolService.getByRolName(RolName.ROLE_ADMIN).get());
        usuario.setRoles(roles);

        usuario.setCompany(company);

        userService.save(usuario);

        return new ResponseEntity(new Message("user saved"), HttpStatus.CREATED);
    }

    @PostMapping("/resetpassword")
    public ResponseEntity<?> resetpassword(@Valid @RequestBody ResetPasswordDto dto, BindingResult bindingResult)  {

        Date date = new Date();
        if (bindingResult.hasErrors())
            return new ResponseEntity(new Message("error in fields or erroneous email"), HttpStatus.BAD_REQUEST);
        if (!userService.existsByGreaterThanEqualExpirationTokenAndToken(date, dto.getToken()))
            return new ResponseEntity(new Message("Token is expired"), HttpStatus.BAD_REQUEST);

        User user = userService.getByGreaterThanEqualExpirationTokenAndToken(date, dto.getToken());

        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        return new ResponseEntity(new Message("Password updated successful"), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return new ResponseEntity(new Message("campos mal puestos"), HttpStatus.BAD_REQUEST);
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);
        Integer orgId = jwtProvider.getCompanyIdFromToken(jwt);
        Integer userId = jwtProvider.getUserIdFromToken(jwt);
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();

        JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities(), orgId, userId);
        return new ResponseEntity(jwtDto, HttpStatus.OK);
    }

    @GetMapping("/recoveryPassword/{emailRequest}")
    public ResponseEntity<?> recoveryPassword(@PathVariable("emailRequest") String emailRequest) throws MessagingException, IOException {
        if (!userService.existsByEmail(emailRequest))
            return new ResponseEntity(new Message("The Email not exists"), HttpStatus.BAD_REQUEST);
        if (!userService.existsByEmailAndStatus(emailRequest))
            return new ResponseEntity(new Message("Your account is inactive, ask the administrator"), HttpStatus.BAD_REQUEST);

        User user = userService.getByEmailAndStatus(emailRequest);
        sendPasswordRecoveryEmail(user, request.getServerName());
        return new ResponseEntity(new Message("check your email, Password request sent for:" + emailRequest), HttpStatus.OK);
    }

    private void sendPasswordRecoveryEmail(User user, String serverName) throws MessagingException, IOException {
        String resetToken = generateResetToken();
        storeResetToken(user, resetToken);
        emailService.sendEmailEvent(user.getEmail(), "Reset Password ", contentResetPasswordHTML(serverName, user.getName(), resetToken));
    }

    private static String generateResetToken() {
        // Generar un token único (puedes usar UUID o cualquier otro método)
        return UUID.randomUUID().toString();
    }

    void storeResetToken(User user, String resetToken) {
        // Almacena el token junto con la fecha de expiración en la base de datos

        Date expirationDate = new Date(System.currentTimeMillis() + 600000);

        user.setToken(resetToken);

        user.setExpirationToken(expirationDate);

        userService.save(user);
        System.out.println("Token almacenado en la base de datos para el usuario " + user.getEmail());
    }

    private static String contentResetPasswordHTML(String pathUrl,String userName, String token) {
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
        sb.append("  <h2>Forgot Password</h2>\n");
        sb.append("  <p>Hello ").append(userName).append(",</p>\n");
        sb.append("  <p>We received a request that you've forgotten your password. If you didn't make this request, you can disregard this email.</p>\n");
        sb.append("  <p>To reset your password, click on the following link:</p>\n");
        sb.append("  <a href=\"http://").append(pathUrl+"/recovery-password-token/").append(token).append("\" class=\"cta-button\">Reset Password</a>\n");
        sb.append("  <p>If the link doesn't work, copy and paste it into your browser.</p>\n");
        sb.append("  <p>This link will expire in 1 hour for security reasons.</p>\n");
        sb.append("  <p>Thank you,</p>\n");
        sb.append("  <p>The Team at ").append("Mytime.com.co").append("</p>\n");
        sb.append("  <div class=\"footer\">\n");
        sb.append("    <p>© 2023 ").append("Mytime.com.co").append(". All rights reserved.</p>\n");
        sb.append("  </div>\n");
        sb.append("</div>");
        sb.append("</body>\n");
        sb.append("</html>");

        return sb.toString();
    }

    //crea iun nuevo metodo para esta clase




}
