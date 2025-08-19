package com.ecommerce.adapter.notification;

import com.ecommerce.core.domain.notification.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.MailSender;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Map;
import java.util.HashMap;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private MailSender mailSender;

    @InjectMocks
    private EmailServiceImpl emailServiceImpl;

    @BeforeEach
    void setUp() {
        // Setup can be done here if needed
    }

    @Test
    @DisplayName("Should send email successfully")
    void shouldSendEmailSuccessfully() {
        // Given
        String to = "test@example.com";
        String subject = "Test Subject";
        String body = "Test message body";

        // When
        assertDoesNotThrow(() -> {
            emailServiceImpl.sendEmail(to, subject, body);
        });

        // Then
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Should handle email sending failure")
    void shouldHandleEmailSendingFailure() {
        // Given
        String to = "test@example.com";
        String subject = "Test Subject";
        String body = "Test message body";

        doThrow(new RuntimeException("Mail server unavailable"))
            .when(mailSender).send(any(SimpleMailMessage.class));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            emailServiceImpl.sendEmail(to, subject, body);
        });
    }

    @Test
    @DisplayName("Should validate email parameters")
    void shouldValidateEmailParameters() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            emailServiceImpl.sendEmail(null, "subject", "body");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            emailServiceImpl.sendEmail("test@example.com", null, "body");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            emailServiceImpl.sendEmail("test@example.com", "subject", null);
        });
    }

    @Test
    @DisplayName("Should send HTML email")
    void shouldSendHtmlEmail() {
        // Given
        String to = "test@example.com";
        String subject = "HTML Email";
        String htmlBody = "<h1>Welcome!</h1><p>Thank you for joining us.</p>";

        // When
        assertDoesNotThrow(() -> {
            emailServiceImpl.sendHtmlEmail(to, subject, htmlBody);
        });

        // Then
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("Should send email with template")
    void shouldSendEmailWithTemplate() {
        // Given
        String to = "test@example.com";
        String templateName = "welcome";
        Map<String, Object> variables = new HashMap<>();
        variables.put("username", "testuser");
        variables.put("activationLink", "http://example.com/activate");

        // When
        assertDoesNotThrow(() -> {
            emailServiceImpl.sendTemplatedEmail(to, templateName, variables);
        });

        // Then
        verify(mailSender).send(any(SimpleMailMessage.class));
    }
}
