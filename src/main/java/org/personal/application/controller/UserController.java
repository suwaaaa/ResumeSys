package org.personal.application.controller;

import org.personal.application.entity.User;
import org.personal.application.service.UserService;
import org.personal.application.utils.JwtTokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    private final JwtTokenUtil jwtTokenUtil;

    public UserController(UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    // 注册
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        userService.register(user);
        return ResponseEntity.ok().build();
    }

    // 登录
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        String loginUserToken = userService.login(user.getUsername(), user.getPassword());
        if (loginUserToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        String token = jwtTokenUtil.generateToken((UserDetails) user);
        return ResponseEntity.ok(token);
    }

    // 找回密码
    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody String email) {
//        userService.forgotPassword(email);
        return ResponseEntity.ok().build();
    }

    // 注销
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization") String token) {
        String tokenValue = token.replace("Bearer ", "").trim();
        userService.logout(tokenValue);
        return ResponseEntity.ok().build();
    }
}

