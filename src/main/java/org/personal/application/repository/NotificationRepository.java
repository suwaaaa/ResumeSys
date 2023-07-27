package org.personal.application.repository;

import org.personal.application.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserIdAndReadStatus(Integer userId, Boolean readStatus);

    List<Notification> findByUserId(Integer userId);

    @Modifying
    @Query("UPDATE Notification n SET n.readStatus = true WHERE n.id = :notificationId")
    void markAsRead(Integer notificationId);


//    接收通知：创建一个Notification对象，然后调用notificationRepository.save(notification)。
//    查看通知：调用notificationRepository.findByUserId(userId)来获取所有与特定用户相关的通知。
//    删除通知：找到要删除的Notification，然后调用notificationRepository.deleteById(notificationId)。
//    标记已读：找到要标记为已读的Notification，然后调用notificationRepository.markAsRead(notificationId)。
}
