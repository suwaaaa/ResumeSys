package org.personal.application.repository;

import org.personal.application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    User findByEmail(String email);

    User findByPhone(String phone);

    //该方法可用于检查用户名是否已被使用
    boolean existsByUsername(String username);

    //以下两个方法可用于找回密码功能
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}
