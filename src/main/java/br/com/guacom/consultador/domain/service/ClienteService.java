package br.com.guacom.consultador.domain.service;

import br.com.guacom.consultador.application.dto.ClienteDTO;
import br.com.guacom.consultador.application.form.ClienteForm;
import br.com.guacom.consultador.domain.entity.Cliente;
import br.com.guacom.consultador.domain.exceptions.ResourceNotFoundException;
import br.com.guacom.consultador.domain.repository.CidadeRepository;
import br.com.guacom.consultador.domain.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class ClienteService {
    private final ClienteRepository clienteRepository;
    private final CidadeRepository cidadeRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public ClienteDTO save(ClienteForm clienteForm) {
        Cliente cliente = modelMapper.map(clienteForm, Cliente.class);

        cidadeRepository.findById(clienteForm.getIdCidade()).orElseThrow(() -> new ResourceNotFoundException("Cidade não encontrada"));

        return convertClienteToClienteDTO(clienteRepository.save(cliente));
    }

    public ClienteDTO findById(Long id) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(thrownResourceNotFound());

        return convertClienteToClienteDTO(cliente);
    }

    public ClienteDTO findByNome(String nome) {
        Cliente cliente = clienteRepository.findByNome(nome).orElseThrow(thrownResourceNotFound());

        return convertClienteToClienteDTO(cliente);
    }

    public ClienteDTO updateNomeById(Long id, String nome) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(thrownResourceNotFound());

        clienteRepository.updateNomeById(id, nome);

        cliente = clienteRepository.findByNome(cliente.getNome()).get();

        return convertClienteToClienteDTO(cliente);
    }

    public Long deleteById(Long id) {
        Cliente cliente = clienteRepository.findById(id).orElseThrow(thrownResourceNotFound());
        clienteRepository.delete(cliente);

        return id;
    }

    private Supplier<ResourceNotFoundException> thrownResourceNotFound() {
        return () -> new ResourceNotFoundException("Cliente não encontrado");
    }

    private ClienteDTO convertClienteToClienteDTO(Cliente cliente) {
        return modelMapper.map(cliente, ClienteDTO.class);
    }
}