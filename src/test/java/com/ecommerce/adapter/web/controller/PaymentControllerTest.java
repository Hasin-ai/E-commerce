        // When & Then
        mockMvc.perform(get("/api/payments/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.payment.id").value(1L));
    }

    @Test
    @DisplayName("Should handle payment processing failure")
    void shouldHandlePaymentProcessingFailure() throws Exception {
        // Given
        ProcessPaymentResponse response = new ProcessPaymentResponse();
        response.setSuccess(false);
        response.setErrorMessage("Payment failed");

        when(processPaymentUseCase.execute(any(ProcessPaymentRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/payments/process")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errorMessage").value("Payment failed"));
    }
        // When & Then
    @Test
    @DisplayName("Should validate payment request")
    void shouldValidatePaymentRequest() throws Exception {
        // Given
        ProcessPaymentRequest invalidRequest = new ProcessPaymentRequest();
        // Missing required fields

        // When & Then
        mockMvc.perform(post("/api/payments/process")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
        mockMvc.perform(post("/api/payments/process")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.paymentId").value(1L))
                .andExpect(jsonPath("$.transactionId").value("txn_123"));

import com.ecommerce.core.usecase.payment.ProcessPaymentUseCase;
import com.ecommerce.core.usecase.payment.GetPaymentUseCase;
import com.ecommerce.core.usecase.payment.ProcessPaymentRequest;
import com.ecommerce.core.usecase.payment.ProcessPaymentResponse;
import com.ecommerce.core.usecase.payment.GetPaymentRequest;
import com.ecommerce.core.usecase.payment.GetPaymentResponse;
import com.ecommerce.core.domain.payment.entity.Payment;
import com.ecommerce.core.domain.payment.entity.PaymentMethod;
import com.ecommerce.core.domain.payment.entity.PaymentStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.math.BigDecimal;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProcessPaymentUseCase processPaymentUseCase;

    @MockBean
    private GetPaymentUseCase getPaymentUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private ProcessPaymentRequest paymentRequest;
    private Payment payment;

    @BeforeEach
    void setUp() {
        paymentRequest = new ProcessPaymentRequest();
        paymentRequest.setOrderId(1L);
        paymentRequest.setAmount(BigDecimal.valueOf(199.99));
        paymentRequest.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        paymentRequest.setCardToken("card_token_123");

        payment = new Payment();
        payment.setId(1L);
        payment.setOrderId(1L);
        payment.setAmount(BigDecimal.valueOf(199.99));
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setMethod(PaymentMethod.CREDIT_CARD);
    }

    @Test
    @DisplayName("Should process payment successfully")
    void shouldProcessPaymentSuccessfully() throws Exception {
        // Given
        ProcessPaymentResponse response = new ProcessPaymentResponse();
        response.setSuccess(true);
        response.setPaymentId(1L);
        response.setTransactionId("txn_123");
        response.setMessage("Payment processed successfully");

        when(processPaymentUseCase.execute(any(ProcessPaymentRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/payments/process")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk())
                .andExpected(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.paymentId").value(1L))
                .andExpected(jsonPath("$.transactionId").value("txn_123"));
    }

    @Test
    @DisplayName("Should get payment by ID")
    void shouldGetPaymentById() throws Exception {
        // Given
        GetPaymentResponse response = new GetPaymentResponse();
        response.setSuccess(true);
        response.setPayment(payment);

        when(getPaymentUseCase.execute(any(GetPaymentRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/payments/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpected(status().isOk())
                .andExpected(jsonPath("$.success").value(true))
                .andExpected(jsonPath("$.payment.id").value(1L));
    }

    @Test
    @DisplayName("Should handle payment processing failure")
    void shouldHandlePaymentProcessingFailure() throws Exception {
        // Given
        ProcessPaymentResponse response = new ProcessPaymentResponse();
        response.setSuccess(false);
        response.setErrorMessage("Payment failed");

        when(processPaymentUseCase.execute(any(ProcessPaymentRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/payments/process")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpected(status().isBadRequest())
                .andExpected(jsonPath("$.success").value(false))
                .andExpected(jsonPath("$.errorMessage").value("Payment failed"));
    }

    @Test
    @DisplayName("Should validate payment request")
    void shouldValidatePaymentRequest() throws Exception {
        // Given
        ProcessPaymentRequest invalidRequest = new ProcessPaymentRequest();
        // Missing required fields

        // When & Then
        mockMvc.perform(post("/api/payments/process")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpected(status().isBadRequest());
    }
}
