package com.ch.ch9ex.service;

// 1. 导入业务类 (解决 ChatMessageSenderService 报红)
import com.ch.ch9ex.entity.ChatMessage;
import com.ch.ch9ex.repository.ChatMessageRepository;
import com.ch.ch9ex.service.ChatMessageSenderService;

// 2. 导入 JUnit 测试注解
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

// 3. 导入 Mockito 和 ArgumentCaptor (解决 ArgumentCaptor 报红)
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

// 4. 导入 Spring Boot 测试注解 (解决 Autowired 报红)
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

// 5. 导入其他工具类
import java.time.LocalDateTime;

// 6. 导入静态断言方法 (解决 assertEquals, verify 等报红)
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * ChatMessageSenderService 业务类的单元测试
 */
@SpringBootTest
@DisplayName("ChatMessageSenderService 业务类测试")
class ChatMessageSenderServiceTest {

    @Autowired  // 👈 现在这个应该能识别了
    private ChatMessageSenderService chatMessageSenderService;

    @MockBean
    private ChatMessageRepository chatMessageRepository;

    private ChatMessage testMessage;

    @BeforeEach
    void setUp() {
        testMessage = new ChatMessage(
                "MSG001",
                "张三",
                "这是一条测试消息",
                LocalDateTime.of(2024, 12, 13, 10, 30, 0)
        );
    }

    @Test
    @DisplayName("测试发送消息成功 - 验证 Repository 被正确调用")
    void testSendMessage_Success() {
        when(chatMessageRepository.save(testMessage)).thenReturn(true);
        boolean result = chatMessageSenderService.sendMessage(testMessage);
        verify(chatMessageRepository, times(1)).save(eq(testMessage));
        assertTrue(result, "发送成功应该返回 true");
    }

    @Test
    @DisplayName("测试发送消息失败 - Repository 返回 false")
    void testSendMessage_Failure() {
        when(chatMessageRepository.save(testMessage)).thenReturn(false);
        boolean result = chatMessageSenderService.sendMessage(testMessage);
        verify(chatMessageRepository, times(1)).save(testMessage);
        assertFalse(result, "发送失败应该返回 false");
    }

    @Test
    @DisplayName("测试发送消息 - 使用 ArgumentCaptor 捕获参数")
    void testSendMessage_WithArgumentCaptor() {
        // 👇 现在这个 ArgumentCaptor 应该能识别了
        ArgumentCaptor<ChatMessage> messageCaptor = ArgumentCaptor.forClass(ChatMessage.class);
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(true);

        chatMessageSenderService.sendMessage(testMessage);

        verify(chatMessageRepository, times(1)).save(messageCaptor.capture());

        ChatMessage capturedMessage = messageCaptor.getValue();
        assertEquals(testMessage, capturedMessage, "传递的消息对象应该与预期一致");
    }

    @Test
    @DisplayName("测试发送 null 消息 - 应该抛出异常")
    void testSendMessage_NullMessage() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> chatMessageSenderService.sendMessage(null),
                "传入 null 消息应该抛出 IllegalArgumentException"
        );
        assertEquals("消息对象不能为空", exception.getMessage());
        verify(chatMessageRepository, never()).save(any());
    }

    @Test
    @DisplayName("测试发送消息 - 发送者为空应该抛出异常")
    void testSendMessage_NullSender() {
        ChatMessage invalidMessage = new ChatMessage("MSG002", null, "内容", LocalDateTime.now());
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> chatMessageSenderService.sendMessage(invalidMessage)
        );
        assertEquals("消息的发送者和内容不能为空", exception.getMessage());
        verify(chatMessageRepository, never()).save(any());
    }

    @Test
    @DisplayName("测试消息发送失败 - Repository 抛出异常")
    void testSendMessage_RepositoryThrowsException() {
        when(chatMessageRepository.save(any(ChatMessage.class)))
                .thenThrow(new RuntimeException("数据库连接失败"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> chatMessageSenderService.sendMessage(testMessage)
        );
        assertEquals("数据库连接失败", exception.getMessage());
        verify(chatMessageRepository, times(1)).save(any(ChatMessage.class));
    }

    @Test
    @DisplayName("测试多次发送消息")
    void testSendMessage_MultipleTimes() {
        ChatMessage message1 = new ChatMessage("MSG001", "张三", "消息1", LocalDateTime.now());
        ChatMessage message2 = new ChatMessage("MSG002", "李四", "消息2", LocalDateTime.now());
        ChatMessage message3 = new ChatMessage("MSG003", "王五", "消息3", LocalDateTime.now());

        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(true);

        chatMessageSenderService.sendMessage(message1);
        chatMessageSenderService.sendMessage(message2);
        chatMessageSenderService.sendMessage(message3);

        verify(chatMessageRepository, times(3)).save(any(ChatMessage.class));
    }

    @Test
    @DisplayName("测试查找消息")
    void testFindMessage() {
        when(chatMessageRepository.findById("MSG001")).thenReturn(testMessage);
        ChatMessage result = chatMessageSenderService.findMessage("MSG001");
        verify(chatMessageRepository, times(1)).findById("MSG001");
        assertEquals(testMessage, result);
    }

    @Test
    @DisplayName("测试删除消息")
    void testDeleteMessage() {
        when(chatMessageRepository.deleteById("MSG001")).thenReturn(true);
        boolean result = chatMessageSenderService.deleteMessage("MSG001");
        verify(chatMessageRepository, times(1)).deleteById("MSG001");
        assertTrue(result);
    }

    @Test
    @DisplayName("测试获取消息总数")
    void testGetMessageCount() {
        when(chatMessageRepository.count()).thenReturn(100L);
        long count = chatMessageSenderService.getMessageCount();
        verify(chatMessageRepository, times(1)).count();
        assertEquals(100L, count);
    }
}