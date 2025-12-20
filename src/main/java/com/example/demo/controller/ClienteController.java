package com.example.demo.controller;

import com.example.demo.module.dto.request.ClienteRequestDto;
import com.example.demo.module.dto.response.ClienteResponseDto;
import com.example.demo.services.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping("/crear")
    public ResponseEntity<ClienteResponseDto> crearCliente(
            @RequestHeader("consumerId") String consumerId,
            @RequestHeader("traceparent") String traceparent,
            @RequestHeader("deviceType") String deviceType,
            @RequestHeader("deviceId") String deviceId,
            @Valid @RequestBody ClienteRequestDto request) {

        // El servicio se encargará de la lógica y el proceso asíncrono
        ClienteResponseDto response = clienteService.crear(request, traceparent);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("listar")
    public ResponseEntity<Page<ClienteResponseDto>> listarClientes(
            @RequestHeader("consumerId") String consumerId,
            Pageable pageable) {

        return ResponseEntity.ok(clienteService.listarTodos(pageable));
    }
}
