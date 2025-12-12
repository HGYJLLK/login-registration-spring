package com.ch.ch9ex.entity;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 聊天消息实体类
 * 用于表示聊天系统中的一条消息
 *
 * @version 1.0
 */
@Getter
public class ChatMessage {

    /**
     * 消息唯一标识符
     * -- GETTER --
     *  获取消息ID
     *
     * @return 消息ID

     */
    private String id;

    /**
     * 消息发送者
     * -- GETTER --
     *  获取发送者
     *
     * @return 发送者名称

     */
    private String sender;

    /**
     * 消息内容
     * -- GETTER --
     *  获取消息内容
     *
     * @return 消息内容

     */
    private String content;

    /**
     * 消息创建时间
     * -- GETTER --
     *  获取创建时间
     *
     * @return 创建时间

     */
    private LocalDateTime createTime;

    /**
     * 无参构造函数
     * JPA/Hibernate 等框架需要无参构造函数
     */
    public ChatMessage() {
    }

    /**
     * 全参构造函数
     * 方便快速创建对象
     *
     * @param id 消息ID
     * @param sender 发送者
     * @param content 消息内容
     * @param createTime 创建时间
     */
    public ChatMessage(String id, String sender, String content, LocalDateTime createTime) {
        this.id = id;
        this.sender = sender;
        this.content = content;
        this.createTime = createTime;
    }

    // ==================== Getter 方法 ====================

    // ==================== Setter 方法 ====================

    /**
     * 设置消息ID
     * @param id 消息ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 设置发送者
     * @param sender 发送者名称
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * 设置消息内容
     * @param content 消息内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 设置创建时间
     * @param createTime 创建时间
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    // ==================== 重写 Object 方法 ====================

    /**
     * 重写 equals 方法
     * 用于对象比较，基于 id 字段判断
     *
     * @param o 要比较的对象
     * @return 如果两个对象相等返回 true，否则返回 false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessage that = (ChatMessage) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(sender, that.sender) &&
                Objects.equals(content, that.content) &&
                Objects.equals(createTime, that.createTime);
    }

    /**
     * 重写 hashCode 方法
     * 与 equals 方法配合使用
     *
     * @return 对象的哈希码
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, sender, content, createTime);
    }

    /**
     * 重写 toString 方法
     * 用于调试和日志输出
     *
     * @return 对象的字符串表示
     */
    @Override
    public String toString() {
        return "ChatMessage{" +
                "id='" + id + '\'' +
                ", sender='" + sender + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
