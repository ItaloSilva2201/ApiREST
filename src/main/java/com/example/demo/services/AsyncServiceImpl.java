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
    @Async
    public void procesarEnvioExterno(Cliente cliente, String traceId) {
        try {
            Thread.sleep(2000);

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
