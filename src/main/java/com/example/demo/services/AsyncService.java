package com.example.demo.services;

import com.example.demo.module.entity.Cliente;

public interface AsyncService {
    void procesarEnvioExterno(Cliente cliente, String traceId);
}
