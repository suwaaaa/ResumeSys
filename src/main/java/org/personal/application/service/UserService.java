package org.personal.application.service;

import org.personal.application.entity.PasswordResetToken;
import org.personal.application.entity.User;
import org.personal.application.repository.UserRepository;
import org.personal.application.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    private final JavaMailSender mailSender;

    public UserService(UserRepository userRepository,
                       JwtTokenUtil jwtTokenUtil,
                       PasswordEncoder passwordEncoder,
                       EmailService emailService,
                       JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.mailSender = mailSender;
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }
    public User findByEmail(String userEmail){
        return userRepository.findByEmail(userEmail);
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


    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        //To do ...
        //passwordTokenRepository.save(myToken);
    }

    // 密码找回，根据邮箱或电话号码，这里仅展示根据邮箱找回
    public void retrievePassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("No user found with this email");
        }
        // Send password to the user's email
    }

    public void sendPasswordResetMail(String to, String token) {
        SimpleMailMessage passwordResetEmail = new SimpleMailMessage();
        String appUrl = "http://localhost:8080";
        String resetUrl = appUrl + "/reset?token=" + token;
        passwordResetEmail.setFrom("support@your-domain.com");
        passwordResetEmail.setTo(to);
        passwordResetEmail.setSubject("Password Reset Request");
        passwordResetEmail.setText("To reset your password, click the link below:\n" + resetUrl);
        mailSender.send(passwordResetEmail);
    }

    // 用户注销，这通常在web应用中是销毁session或者使得token失效，在这里不做具体实现
    public void logout(String token) {
        // No operations for a simple demo, usually invalidate the session or token in real projects.
//        localStorage.removeItem('token');

    }

    // 从JWT解析出User信息
//    public User getUserFromToken(String token) {
//        return jwtTokenUtil.getUserFromToken(token);
//    }


    public void forgotPassword(String email) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));
        if (!userOptional.isPresent()) {
            throw new RuntimeException("User not found with email: " + email);
        }
        User user = userOptional.get();
        String resetToken = UUID.randomUUID().toString();
//        user.setResetToken(resetToken);

        // Save user's reset token to verify it later
        userRepository.save(user);

        // Send email to user with the reset token
        String subject = "Password reset request";
        String text = "To reset your password, click the link below:\n" +
                "http://www.example.com/reset-password?token=" + resetToken;
        emailService.sendSimpleMessage(email, subject, text);
    }
}
