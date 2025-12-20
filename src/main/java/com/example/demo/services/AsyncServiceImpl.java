package com.example.demo.services;

import com.example.demo.module.entity.Cliente;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class AsyncServiceImpl implements AsyncService {

    private final ObjectMapper objectMapper;

    @Override
    @Async // El método corre en un hilo separado
    public void procesarEnvioExterno(Cliente cliente, String traceId) {
        try {
            // Simulación de latencia de red
            Thread.sleep(2000);

            // Conversión a JSON (Requisito técnico)
            String jsonLog = objectMapper.writeValueAsString(cliente);

            log.info("--- SIMULACIÓN SISTEMA EXTERNO ---");
            log.info("Traceparent: {}", traceId);
            log.info("Cuerpo del Mensaje: {}", jsonLog);
            log.info("----------------------------------");

        } catch (Exception e) {
            log.error("Error en el procesamiento asíncrono: {}", e.getMessage());
        }
    }
}
