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
        // ARRANGE
        String traceId = "test-trace-123";
        String clienteJsonEsperado = "{\"id\":\"123\",\"nombre\":\"Test\"}";
        Cliente cliente = new Cliente();
        cliente.setId(UUID.randomUUID());

        when(objectMapper.writeValueAsString(any(Cliente.class))).thenReturn(clienteJsonEsperado);

        // ACT
        asyncService.procesarEnvioExterno(cliente, traceId);

        // ASSERT
        verify(objectMapper, times(1)).writeValueAsString(cliente);
    }

    // =========================================================
    // 2. FLUJO DE ERROR (Cubre el código del catch {})
    // =========================================================
    @Test
    void procesarEnvioExterno_debeManejarCualquierExcepcionDeObjectMapper() throws Exception {
        // ARRANGE
        String traceId = "test-error-456";
        Cliente cliente = new Cliente();

        // Simulación: Forzar una RuntimeException, que será atrapada por el "catch (Exception e)"
        // No necesitamos ninguna importación especial, solo la RuntimeException estándar de Java.
        when(objectMapper.writeValueAsString(any(Cliente.class)))
                .thenThrow(new RuntimeException("Error simulado de serialización"));

        // ACT
        // El método se ejecuta y debería ir al bloque catch
        asyncService.procesarEnvioExterno(cliente, traceId);

        // ASSERT
        // Verificamos que se intentó la serialización (lo que desencadenó el error)
        verify(objectMapper, times(1)).writeValueAsString(cliente);

        // El test pasa si el código no lanza la excepción al exterior, confirmando que el catch funcionó.
    }

}