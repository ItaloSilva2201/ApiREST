package com.example.demo.services;

import com.example.demo.module.dto.request.ClienteRequestDto;
import com.example.demo.module.dto.response.ClienteResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteService {
    ClienteResponseDto crear(ClienteRequestDto dto, String traceId);
    Page<ClienteResponseDto> listarTodos(Pageable pageable);
}
