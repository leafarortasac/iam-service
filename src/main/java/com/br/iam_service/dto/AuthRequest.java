package com.br.iam_service.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String clientId;
    private String clientSecret;
}