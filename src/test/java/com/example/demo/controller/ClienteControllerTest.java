package com.example.demo.controller;

import com.example.demo.module.dto.request.ClienteRequestDto;
import com.example.demo.module.dto.response.ClienteResponseDto;
import com.example.demo.services.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @InjectMocks
    private ClienteController clienteController;

    @Mock
    private ClienteService clienteService;

    private final String CONSUMER_ID = "SMP-TEST";
    private final String TRACE_PARENT = "00-test-trace-id";
    private final String DEVICE_TYPE = "WEB";
    private final String DEVICE_ID = "pc-desktop";

    private ClienteRequestDto requestDto;
    private ClienteResponseDto responseDto;

    @BeforeEach
    void setUp() {
        requestDto = new ClienteRequestDto("Juan", "Perez", "Lopez");
        responseDto = new ClienteResponseDto(UUID.randomUUID(), "Juan Perez Lopez");
    }

    @Test
    void crearCliente_debeRetornar201YResponseDto() {
        when(clienteService.crear(any(ClienteRequestDto.class), eq(TRACE_PARENT))).thenReturn(responseDto);

        ResponseEntity<ClienteResponseDto> responseEntity = clienteController.crearCliente(
                CONSUMER_ID, TRACE_PARENT, DEVICE_TYPE, DEVICE_ID, requestDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode(), "Debe devolver 201 CREATED");
        assertNotNull(responseEntity.getBody());
        assertEquals(responseDto.getNombreCompleto(), responseEntity.getBody().getNombreCompleto());
        verify(clienteService).crear(requestDto, TRACE_PARENT);
    }

    @Test
    void listarClientes_debeRetornar200YPaginaDeClientes() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ClienteResponseDto> paginaSimulada = new PageImpl<>(Collections.singletonList(responseDto), pageable, 1);

        when(clienteService.listarTodos(any(Pageable.class))).thenReturn(paginaSimulada);

        ResponseEntity<Page<ClienteResponseDto>> responseEntity = clienteController.listarClientes(
                CONSUMER_ID, pageable);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "Debe devolver 200 OK");
        assertNotNull(responseEntity.getBody());
        assertEquals(1, responseEntity.getBody().getTotalElements());
        verify(clienteService).listarTodos(pageable);
    }
}