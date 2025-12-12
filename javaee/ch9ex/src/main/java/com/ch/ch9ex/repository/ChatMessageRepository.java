package com.ch.ch9ex.repository;

import com.ch.ch9ex.entity.ChatMessage;

/**
 * 聊天消息存储接口
 * 模拟数据访问层（DAO/Repository）
 *
 * 在实际项目中，这可能是：
 * - JPA Repository：操作数据库
 * - MongoDB Repository：操作 NoSQL 数据库
 * - Redis Template：操作缓存
 * - 消息队列模板：发送到 RabbitMQ/Kafka
 *
 * 在单元测试中，这个接口会被 Mock 掉，不需要真实的存储
 *
 * @author 实训9作业
 * @version 1.0
 */
public interface ChatMessageRepository {

    /**
     * 保存聊天消息
     *
     * @param message 要保存的消息
     * @return 保存成功返回 true，失败返回 false
     */
    boolean save(ChatMessage message);

    /**
     * 根据 ID 查找消息
     *
     * @param id 消息ID
     * @return 找到的消息，如果不存在返回 null
     */
    ChatMessage findById(String id);

    /**
     * 删除消息
     *
     * @param id 消息ID
     * @return 删除成功返回 true，失败返回 false
     */
    boolean deleteById(String id);

    /**
     * 获取消息总数
     *
     * @return 消息总数
     */
    long count();
}
