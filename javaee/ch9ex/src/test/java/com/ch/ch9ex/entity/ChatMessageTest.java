package com.ch.ch9ex.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ChatMessage 实体类的单元测试
 *
 * 测试目标：不启动 Spring 容器，纯 Java 测试
 * 测试重点：验证实体类的 Getter/Setter 方法、构造函数、equals/hashCode 等
 *
 * JUnit 5 核心注解说明：
 * - @Test：标记这是一个测试方法
 * - @DisplayName：为测试方法提供可读性更好的显示名称
 * - @BeforeEach：在每个测试方法执行前都会执行，用于初始化测试数据
 *
 * @version 1.0
 */
@DisplayName("ChatMessage 实体类测试")
class ChatMessageTest {

    // 测试数据：在多个测试方法中复用
    private String testId;
    private String testSender;
    private String testContent;
    private LocalDateTime testCreateTime;

    /**
     * 在每个测试方法执行前运行
     * 用于初始化测试数据，确保每个测试都是独立的
     *
     * @BeforeEach 注解的方法会在每个 @Test 方法之前执行
     */
    @BeforeEach
    void setUp() {
        // 准备测试数据
        testId = "MSG001";
        testSender = "张三";
        testContent = "你好，这是一条测试消息！";
        testCreateTime = LocalDateTime.of(2024, 12, 13, 10, 30, 0);
    }

    /**
     * 测试无参构造函数
     * 验证能够成功创建对象
     */
    @Test
    @DisplayName("测试无参构造函数")
    void testNoArgsConstructor() {
        // 1. 执行：创建对象
        ChatMessage message = new ChatMessage();

        // 2. 断言：验证对象不为空
        assertNotNull(message, "使用无参构造函数创建的对象不应该为 null");
    }

    /**
     * 测试全参构造函数
     * 验证能够通过构造函数正确设置所有字段
     */
    @Test
    @DisplayName("测试全参构造函数")
    void testAllArgsConstructor() {
        // 1. 执行：使用全参构造函数创建对象
        ChatMessage message = new ChatMessage(testId, testSender, testContent, testCreateTime);

        // 2. 断言：验证所有字段都被正确设置
        assertNotNull(message, "对象不应该为 null");
        assertEquals(testId, message.getId(), "ID 应该与构造函数参数一致");
        assertEquals(testSender, message.getSender(), "发送者应该与构造函数参数一致");
        assertEquals(testContent, message.getContent(), "内容应该与构造函数参数一致");
        assertEquals(testCreateTime, message.getCreateTime(), "创建时间应该与构造函数参数一致");
    }

    /**
     * 测试 Setter 和 Getter 方法
     * 这是最核心的测试，验证设置的值能够正确获取
     *
     * 测试步骤：
     * 1. 创建对象
     * 2. 使用 Setter 设置值
     * 3. 使用 Getter 获取值
     * 4. 断言获取的值与设置的值一致
     */
    @Test
    @DisplayName("测试 Getter 和 Setter 方法")
    void testGetterAndSetter() {
        // 1. 准备：创建空对象
        ChatMessage message = new ChatMessage();

        // 2. 执行：使用 Setter 设置所有字段
        message.setId(testId);
        message.setSender(testSender);
        message.setContent(testContent);
        message.setCreateTime(testCreateTime);

        // 3. 断言：使用 Getter 验证每个字段都被正确设置
        assertEquals(testId, message.getId(), "getId() 应该返回通过 setId() 设置的值");
        assertEquals(testSender, message.getSender(), "getSender() 应该返回通过 setSender() 设置的值");
        assertEquals(testContent, message.getContent(), "getContent() 应该返回通过 setContent() 设置的值");
        assertEquals(testCreateTime, message.getCreateTime(), "getCreateTime() 应该返回通过 setCreateTime() 设置的值");
    }

    /**
     * 测试 equals 方法
     * 验证两个内容相同的对象应该被判断为相等
     */
    @Test
    @DisplayName("测试 equals 方法 - 相同内容应该相等")
    void testEquals_SameContent() {
        // 1. 准备：创建两个内容完全相同的对象
        ChatMessage message1 = new ChatMessage(testId, testSender, testContent, testCreateTime);
        ChatMessage message2 = new ChatMessage(testId, testSender, testContent, testCreateTime);

        // 2. 断言：两个对象应该相等
        assertEquals(message1, message2, "内容相同的两个对象应该被判断为相等");

        // 3. 断言：相等的对象应该有相同的 hashCode
        assertEquals(message1.hashCode(), message2.hashCode(), "相等的对象应该有相同的 hashCode");
    }

    /**
     * 测试 equals 方法
     * 验证内容不同的对象应该被判断为不相等
     */
    @Test
    @DisplayName("测试 equals 方法 - 不同内容应该不相等")
    void testEquals_DifferentContent() {
        // 1. 准备：创建两个内容不同的对象
        ChatMessage message1 = new ChatMessage(testId, testSender, testContent, testCreateTime);
        ChatMessage message2 = new ChatMessage("MSG002", "李四", "不同的内容", testCreateTime);

        // 2. 断言：两个对象应该不相等
        assertNotEquals(message1, message2, "内容不同的两个对象应该不相等");
    }

    /**
     * 测试 equals 方法的自反性
     * 一个对象应该等于它自己
     */
    @Test
    @DisplayName("测试 equals 方法 - 对象应该等于自己")
    void testEquals_Reflexive() {
        // 1. 准备：创建对象
        ChatMessage message = new ChatMessage(testId, testSender, testContent, testCreateTime);

        // 2. 断言：对象应该等于自己
        assertEquals(message, message, "对象应该等于自己（自反性）");
    }

    /**
     * 测试 equals 方法处理 null
     * 对象不应该等于 null
     */
    @Test
    @DisplayName("测试 equals 方法 - 对象不应该等于 null")
    void testEquals_Null() {
        // 1. 准备：创建对象
        ChatMessage message = new ChatMessage(testId, testSender, testContent, testCreateTime);

        // 2. 断言：对象不应该等于 null
        assertNotEquals(message, null, "对象不应该等于 null");
    }

    /**
     * 测试 toString 方法
     * 验证 toString 能够返回包含关键信息的字符串
     */
    @Test
    @DisplayName("测试 toString 方法")
    void testToString() {
        // 1. 准备：创建对象
        ChatMessage message = new ChatMessage(testId, testSender, testContent, testCreateTime);

        // 2. 执行：调用 toString 方法
        String result = message.toString();

        // 3. 断言：toString 返回的字符串应该包含所有关键字段信息
        assertNotNull(result, "toString() 不应该返回 null");
        assertTrue(result.contains(testId), "toString() 应该包含 ID");
        assertTrue(result.contains(testSender), "toString() 应该包含发送者");
        assertTrue(result.contains(testContent), "toString() 应该包含内容");
    }

    /**
     * 测试字段为 null 的情况
     * 验证即使字段为 null，对象也能正常工作
     */
    @Test
    @DisplayName("测试字段为 null 的情况")
    void testNullFields() {
        // 1. 准备：创建字段为 null 的对象
        ChatMessage message = new ChatMessage(null, null, null, null);

        // 2. 断言：对象应该能正常创建
        assertNotNull(message, "即使字段为 null，对象也应该能创建");
        assertNull(message.getId(), "ID 应该为 null");
        assertNull(message.getSender(), "发送者应该为 null");
        assertNull(message.getContent(), "内容应该为 null");
        assertNull(message.getCreateTime(), "创建时间应该为 null");
    }
}
