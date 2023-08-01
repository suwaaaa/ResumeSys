package org.personal.application.controller;

import org.personal.application.entity.User;
import org.personal.application.service.UserService;
import org.personal.application.utils.JwtTokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

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
    @RequestMapping(value = "/api/auth/register", method = RequestMethod.POST)
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
    @RequestMapping(value = "/api/auth/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody User user) {
        String loginUserToken = userService.login(user.getUsername(), user.getPassword());
        if (loginUserToken == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        return ResponseEntity.ok().body(loginUserToken);
    }

    // 找回密码
    @PostMapping("/api/auth/changePassword")
    public ResponseEntity<?> forgotPassword(@RequestBody User user) {
//        userService.forgotPassword(email);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/api/auth/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> requestMap) {
        String userEmail = requestMap.get("email");

        // 步骤 1: 检查用户是否存在
        User user = userService.findByEmail(userEmail);
        if (user == null) {
            return ResponseEntity.badRequest().body("There is no user with this email address");
        }

        // 步骤 2: 创建密码重置令牌并存储到数据库
        String resetToken = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, resetToken);

        // 步骤 3: 发送包含重置链接的邮件到用户邮箱
        // 注意：真实应用中需要构造一个包含重置链接的邮件并发送给用户。重置链接通常会包含令牌作为参数，
        // 并且应指向客户端应用的密码重置页面。在这个示例中，我们只是简单地打印出这个链接。
        String appUrl = "http://localhost:8080";
        String resetUrl = appUrl + "/reset?token=" + resetToken;
        System.out.println("Reset Password link: " + resetUrl);

        return ResponseEntity.ok().body("A password reset link has been sent to your email address");
    }
    @PostMapping("/api/auth/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> requestMap) {
        String token = requestMap.get("token");
        String newPassword = requestMap.get("password");

//        // 步骤 1: 从数据库查找令牌
//        PasswordResetToken resetToken = userService.getPasswordResetToken(token);
//        if (resetToken == null) {
//            return ResponseEntity.badRequest().body("Invalid or expired password reset token");
//        }
//
//        // 步骤 2: 更新用户密码
//        User user = resetToken.getUser();
//        user.setPassword(passwordEncoder.encode(newPassword));
//        userService.save(user);
//
//        // 步骤 3: 删除令牌
//        userService.deletePasswordResetToken(resetToken);

        return ResponseEntity.ok().body("Password reset successfully");
    }


    // 注销
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization") String token) {
        String tokenValue = token.replace("Bearer ", "").trim();
        userService.logout(tokenValue);
        return ResponseEntity.ok().build();
    }
}

