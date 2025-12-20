package com.example.demo.service;

import com.example.demo.module.dto.request.ClienteRequestDto;
import com.example.demo.module.dto.response.ClienteResponseDto;
import com.example.demo.module.entity.Cliente;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.services.AsyncService;
import com.example.demo.services.ClienteServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private AsyncService asyncService;

    @Test
    void crear_debeGuardarClienteYActivarProcesoAsincrono() {
        String traceId = "test-trace-123";
        UUID clienteId = UUID.randomUUID();

        ClienteRequestDto requestDto = new ClienteRequestDto("Italo", "Silva", "Polo");

        Cliente clienteGuardado = new Cliente();
        clienteGuardado.setId(clienteId);
        clienteGuardado.setNombre(requestDto.getNombre());
        clienteGuardado.setApellidoPaterno(requestDto.getApellidoPaterno());
        clienteGuardado.setApellidoMaterno(requestDto.getApellidoMaterno());

        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteGuardado);

        ClienteResponseDto response = clienteService.crear(requestDto, traceId);

        assertNotNull(response, "La respuesta no debe ser nula.");
        assertEquals(clienteId, response.getId(), "El ID debe ser el generado.");
        assertEquals("Italo Silva Polo", response.getNombreCompleto(), "El nombre debe estar formateado.");

        verify(clienteRepository).save(any(Cliente.class));
        verify(asyncService).procesarEnvioExterno(clienteGuardado, traceId);
    }

    @Test
    void listarTodos_debeDevolverUnaPaginaDeClienteResponseDto() {
        Pageable pageable = PageRequest.of(0, 10);

        Cliente cliente1 = new Cliente();
        cliente1.setId(UUID.randomUUID());
        cliente1.setNombre("Ana");
        cliente1.setApellidoPaterno("Perez");
        cliente1.setApellidoMaterno("Mendez");

        List<Cliente> clientes = Arrays.asList(cliente1);
        Page<Cliente> paginaSimulada = new PageImpl<>(clientes, pageable, clientes.size());

        when(clienteRepository.findAll(pageable)).thenReturn(paginaSimulada);

        Page<ClienteResponseDto> resultado = clienteService.listarTodos(pageable);

        assertNotNull(resultado, "La página de resultados no debe ser nula.");
        assertEquals(1, resultado.getTotalElements());

        ClienteResponseDto dto = resultado.getContent().get(0);
        assertEquals("Ana Perez Mendez", dto.getNombreCompleto(), "El DTO debe tener el nombre formateado.");

        verify(clienteRepository).findAll(pageable);
    }

    @Test
    void onCreate_debeInicializarFechaYEstadoAntesDePersistir() {
        Cliente cliente = new Cliente();

        cliente.onCreate();

        assertTrue(cliente.isEstado(), "El estado debe ser verdadero después de onCreate().");

        assertNotNull(cliente.getFechaCreacion(), "La fecha de creación no debe ser nula después de onCreate().");
    }
}