package com.bhos.ticketbackend.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ApiResponseError {
    private List<String> message;
    private HttpStatus status;
    private LocalDateTime localDateTime;
}
