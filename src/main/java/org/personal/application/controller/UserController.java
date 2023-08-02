package org.personal.application.controller;

import org.personal.application.entity.User;
import org.personal.application.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 注册
    @RequestMapping(value = "/auth/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody User user) {
        if(userService.existsByUsername(user)) {
            return new ResponseEntity<>("Error: Username is already taken!", HttpStatus.BAD_REQUEST);
        }
        if(userService.existsByEmail(user)){
            return new ResponseEntity<>("Error: Email is already in use!", HttpStatus.BAD_REQUEST);
        }
        userService.register(user);
        return ResponseEntity.ok().body("User registered successfully!");
    }


    // 登录
    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody User user) {
        String loginUserToken = userService.login(user.getUsername(), user.getPassword());
        if (loginUserToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        return ResponseEntity.ok().body(loginUserToken);
    }

    // 找回密码,根据邮箱
    @PostMapping("/auth/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> requestMap) {
        String userEmail = requestMap.get("email");
        userService.retrievePassword(userEmail);
        return ResponseEntity.ok().body("A password reset link has been sent to your email address");
    }
    //找回密码后需要删除token，重新登录
    @PostMapping("/auth/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> requestMap) {
        String token = requestMap.get("token");
        String newPassword = requestMap.get("password");
        return userService.resetPassword(token,newPassword);
    }


    // 注销
    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization") String token) {
        String tokenValue = token.replace("Bearer ", "").trim();
        userService.logout(tokenValue);
        return ResponseEntity.ok().build();
    }
}

