package com.example.demo.service;

import com.example.demo.module.entity.Cliente;
import com.example.demo.services.AsyncServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsyncServiceTest {

    @InjectMocks
    private AsyncServiceImpl asyncService;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    void procesarEnvioExterno_debeLlamarAlObjectMapperParaSerializar() throws Exception {
        String traceId = "test-trace-123";
        String clienteJsonEsperado = "{\"id\":\"123\",\"nombre\":\"Test\"}";
        Cliente cliente = new Cliente();
        cliente.setId(UUID.randomUUID());

        when(objectMapper.writeValueAsString(any(Cliente.class))).thenReturn(clienteJsonEsperado);
        asyncService.procesarEnvioExterno(cliente, traceId);
        verify(objectMapper, times(1)).writeValueAsString(cliente);
    }

    @Test
    void procesarEnvioExterno_debeManejarCualquierExcepcionDeObjectMapper() throws Exception {
        String traceId = "test-error-456";
        Cliente cliente = new Cliente();

        when(objectMapper.writeValueAsString(any(Cliente.class)))
                .thenThrow(new RuntimeException("Error simulado de serialización"));
        asyncService.procesarEnvioExterno(cliente, traceId);
        verify(objectMapper, times(1)).writeValueAsString(cliente);

    }

}