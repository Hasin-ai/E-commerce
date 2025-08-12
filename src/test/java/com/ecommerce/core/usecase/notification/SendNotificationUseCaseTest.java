package com.ecommerce.core.usecase.notification;

import com.ecommerce.core.domain.notification.entity.Notification;
import com.ecommerce.core.domain.notification.entity.UserNotification;
import com.ecommerce.core.domain.notification.repository.NotificationRepository;
import com.ecommerce.core.domain.notification.service.EmailService;
import com.ecommerce.core.domain.notification.valueobject.NotificationType;
import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class SendNotificationUseCaseTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private SendNotificationUseCase sendNotificationUseCase;

    private SendNotificationRequest request;
    private User user;
    private Notification notification;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        notification = new Notification();
        notification.setId(1L);
        notification.setTitle("Test Notification");
        notification.setMessage("This is a test notification");
        notification.setType(NotificationType.ORDER_CONFIRMATION);

        request = new SendNotificationRequest();
        request.setUserId(1L);
        request.setTitle("Test Notification");
        request.setMessage("This is a test notification");
        request.setType(NotificationType.ORDER_CONFIRMATION);
    }

    @Test
    @DisplayName("Should send notification successfully")
    void shouldSendNotificationSuccessfully() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        // When
        SendNotificationResponse response = sendNotificationUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(1L, response.getNotificationId());
        verify(userRepository).findById(1L);
        verify(notificationRepository).save(any(Notification.class));
        verify(emailService).sendEmail(eq(user.getEmail()), eq("Test Notification"), eq("This is a test notification"));
    }

    @Test
    @DisplayName("Should fail when user not found")
    void shouldFailWhenUserNotFound() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        SendNotificationResponse response = sendNotificationUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("User not found", response.getErrorMessage());
        verify(userRepository).findById(1L);
        verify(notificationRepository, never()).save(any(Notification.class));
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Should handle email sending failure gracefully")
    void shouldHandleEmailSendingFailureGracefully() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        doThrow(new RuntimeException("Email service unavailable"))
            .when(emailService).sendEmail(anyString(), anyString(), anyString());

        // When
        SendNotificationResponse response = sendNotificationUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess()); // Notification saved even if email fails
        assertEquals(1L, response.getNotificationId());
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    @DisplayName("Should validate notification request")
    void shouldValidateNotificationRequest() {
        // Given
        request.setTitle(null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        SendNotificationResponse response = sendNotificationUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Title is required", response.getErrorMessage());
        verify(notificationRepository, never()).save(any(Notification.class));
    }

    @Test
    @DisplayName("Should send different notification types")
    void shouldSendDifferentNotificationTypes() {
        // Given
        request.setType(NotificationType.PAYMENT_CONFIRMATION);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        // When
        SendNotificationResponse response = sendNotificationUseCase.execute(request);

        // Then
        assertNotNull(response);
        assertTrue(response.isSuccess());
        verify(notificationRepository).save(argThat(n -> 
            n.getType() == NotificationType.PAYMENT_CONFIRMATION));
    }
}
