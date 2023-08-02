package org.personal.application.service;

import org.personal.application.entity.PasswordResetToken;
import org.personal.application.entity.User;
import org.personal.application.repository.PasswordResetTokenRepository;
import org.personal.application.repository.UserRepository;
import org.personal.application.utils.JwtTokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final JavaMailSender mailSender;

    public UserService(UserRepository userRepository,
                       JwtTokenUtil jwtTokenUtil,
                       PasswordEncoder passwordEncoder,
                       PasswordResetTokenRepository passwordResetTokenRepository,
                       JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }
    public boolean existsByUsername(User user) {
        return userRepository.existsByUsername(user.getUsername());
    }
    public boolean existsByEmail(User user) {
        return userRepository.existsByEmail(user.getEmail());
    }
    // 用户注册
    public void register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    // 用户登录，返回User对象以便于获取用户信息，实际开发中更可能的是返回Token
    public String login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return jwtTokenUtil.generateToken((UserDetails) user);
        }
        throw new IllegalArgumentException("Invalid username or password");
    }

    // 密码找回，根据邮箱或电话号码，这里仅展示根据邮箱找回
    public void retrievePassword(String email) {
        // 步骤 1: 检查用户是否存在
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found with email: " + email);
        }
        User user = userOptional.get();
        String resetToken = UUID.randomUUID().toString();
        // 步骤 2: 创建密码重置令牌并存储到数据库
        createPasswordResetTokenForUser(user, resetToken);
        // 步骤 3: 发送包含重置链接的邮件到用户邮箱
        sendPasswordResetMail(email,resetToken);
    }
    public void createPasswordResetTokenForUser(User user, String token) {//创建密码重置令牌并存储到数据库
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(myToken);
    }

    public void sendPasswordResetMail(String to, String token) {//发送包含重置链接的邮件到用户邮箱
        SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
        String appUrl = "http://localhost:8080";
        String resetUrl = appUrl + "/reset?token=" + token;
        passwordResetEmail.setFrom("1297822261@qq.com");
        passwordResetEmail.setTo(to);
        passwordResetEmail.setSubject("Password Reset Request");
        passwordResetEmail.setText("To reset your password, click the link below:\n" + resetUrl);
        mailSender.send(passwordResetEmail);
    }

    /*
    // 注销用户

    */
    public void logout(String token) {
        System.out.println(token);
        //用户注销，这通常在web应用中是销毁session或者使得token失效
//        function logout() {
//            // 删除存储的JWT
//            localStorage.removeItem('token');
//        }
    }

    public ResponseEntity<String> resetPassword(String token, String newPassword){
        // 步骤 1: 从数据库查找令牌
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);
        if (resetToken == null) {
            return ResponseEntity.badRequest().body("Invalid or expired password reset token");
        }
        // 步骤 2: 更新用户密码
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        // 步骤 3: 删除令牌
        passwordResetTokenRepository.delete(resetToken);
        return ResponseEntity.ok().body("Password reset successfully");
    }

}
