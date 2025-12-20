package com.example.demo.services;

import com.example.demo.module.dto.request.ClienteRequestDto;
import com.example.demo.module.dto.response.ClienteResponseDto;
import com.example.demo.module.entity.Cliente;
import com.example.demo.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClienteServiceImpl implements ClienteService{

    private final ClienteRepository clienteRepository;
    private final AsyncService asyncService;

    @Override
    public ClienteResponseDto crear(ClienteRequestDto dto, String traceId) {
        log.info("Iniciando creación de cliente: {} {}", dto.getNombre(), dto.getApellidoPaterno());

        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setApellidoPaterno(dto.getApellidoPaterno());
        cliente.setApellidoMaterno(dto.getApellidoMaterno());

        cliente = clienteRepository.save(cliente);

        // Disparar proceso asíncrono
        asyncService.procesarEnvioExterno(cliente, traceId);

        return new ClienteResponseDto(
                cliente.getId(),
                formatearNombreCompleto(cliente)
        );
    }

    @Override
    public Page<ClienteResponseDto> listarTodos(Pageable pageable) {
        return clienteRepository.findAll(pageable)
                .map(c -> new ClienteResponseDto(
                        c.getId(),
                        formatearNombreCompleto(c)
                ));
    }

    // Método privado para evitar repetir código (DRY)
    private String formatearNombreCompleto(Cliente c) {
        return String.format("%s %s %s",
                c.getNombre(),
                c.getApellidoPaterno(),
                c.getApellidoMaterno()).trim();
    }
}
