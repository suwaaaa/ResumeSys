package org.personal.application.repository;

import org.personal.application.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {
    List<Chat> findBySenderIdOrReceiverId(Integer senderId, Integer receiverId);

    List<Chat> findByReceiverId(Integer receiverId);


    /*
    发表评论：首先创建一个Chat对象，然后调用chatRepository.save(chat)，之后为这个Chat创建对应的Message并调用messageRepository.save(message)。
    查看评论：调用chatRepository.findByReceiverId(receiverId)来获取所有与特定用户相关的聊天，然后遍历这些聊天并调用messageRepository.findByChatId(chatId)来获取每个聊天的所有消息。
    删除评论：找到要删除的Message或者Chat，然后调用messageRepository.deleteById(messageId)或者chatRepository.deleteById(chatId)。
    回复评论：为对应的Chat创建一个新的Message并调用messageRepository.save(message)。
    */
}
