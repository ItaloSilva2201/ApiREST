package com.example.demo.module.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ClienteResponseDto {

    private UUID id;
    private String nombreCompleto;
}

