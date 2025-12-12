package com.ch.ch9ex.service;

import com.ch.ch9ex.entity.ChatMessage;
import com.ch.ch9ex.repository.ChatMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 聊天消息发送服务类
 * 负责处理聊天消息的业务逻辑
 *
 * 这是一个典型的业务层（Service Layer）类，包含业务逻辑
 * 在单元测试中，我们需要 Mock 掉 ChatMessageRepository 依赖
 *
 * @version 1.0
 */
@Service  // Spring 的服务层注解，标记这是一个业务逻辑类，会被自动扫描并注册为 Bean
public class ChatMessageSenderService {

    /**
     * 日志记录器
     * 使用 SLF4J 门面 + Logback 实现
     */
    private static final Logger logger = LoggerFactory.getLogger(ChatMessageSenderService.class);

    /**
     * 聊天消息存储库
     * 用于持久化消息（数据库、缓存、消息队列等）
     *
     * 注意：在单元测试中，这个依赖会被 @MockBean 替换为 Mock 对象
     * 这样测试就不需要真的连接数据库或其他外部系统
     */
    @Autowired  // 自动注入依赖，由 Spring 容器管理
    private ChatMessageRepository chatMessageRepository;

    /**
     * 发送聊天消息
     *
     * 业务逻辑：
     * 1. 参数校验
     * 2. 记录发送日志
     * 3. 调用 Repository 保存消息
     * 4. 根据结果记录成功或失败日志
     *
     * @param message 要发送的聊天消息对象
     * @return 发送成功返回 true，失败返回 false
     * @throws IllegalArgumentException 如果消息为 null
     */
    public boolean sendMessage(ChatMessage message) {
        // 参数校验：确保消息对象不为空
        if (message == null) {
            logger.error("尝试发送空消息！");
            throw new IllegalArgumentException("消息对象不能为空");
        }

        // 参数校验：确保必填字段不为空
        if (message.getSender() == null || message.getContent() == null) {
            logger.error("消息的发送者或内容为空：{}", message);
            throw new IllegalArgumentException("消息的发送者和内容不能为空");
        }

        // 记录发送前的日志
        logger.info("准备发送消息：{}", message);

        try {
            // 核心业务逻辑：调用 Repository 保存消息
            boolean success = chatMessageRepository.save(message);

            if (success) {
                // 记录发送成功日志
                logger.info("消息发送成功！消息ID：{}，发送者：{}", message.getId(), message.getSender());
            } else {
                // 记录发送失败日志
                logger.warn("消息发送失败！消息：{}", message);
            }

            return success;

        } catch (Exception e) {
            // 异常处理：记录错误日志
            logger.error("消息发送过程中发生异常！消息：{}，错误原因：{}", message, e.getMessage(), e);
            throw e;  // 重新抛出异常，让调用者处理
        }
    }

    /**
     * 查找消息
     *
     * @param id 消息ID
     * @return 找到的消息，如果不存在返回 null
     */
    public ChatMessage findMessage(String id) {
        logger.debug("查找消息，ID：{}", id);
        return chatMessageRepository.findById(id);
    }

    /**
     * 删除消息
     *
     * @param id 消息ID
     * @return 删除成功返回 true，失败返回 false
     */
    public boolean deleteMessage(String id) {
        logger.info("删除消息，ID：{}", id);
        return chatMessageRepository.deleteById(id);
    }

    /**
     * 获取消息总数
     *
     * @return 消息总数
     */
    public long getMessageCount() {
        long count = chatMessageRepository.count();
        logger.debug("当前消息总数：{}", count);
        return count;
    }
}
