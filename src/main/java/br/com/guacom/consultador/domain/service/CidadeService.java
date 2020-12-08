package br.com.guacom.consultador.domain.service;

import br.com.guacom.consultador.application.dto.CidadeDTO;
import br.com.guacom.consultador.application.form.CidadeForm;
import br.com.guacom.consultador.domain.entity.Cidade;
import br.com.guacom.consultador.domain.exceptions.ResourceNotFoundException;
import br.com.guacom.consultador.domain.repository.CidadeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class CidadeService {
    private final CidadeRepository cidadeRepository;
    private final ModelMapper modelMapper;

    @Transactional
    public CidadeDTO save(CidadeForm cidadeForm) {
        Cidade cidade = modelMapper.map(cidadeForm, Cidade.class);

        return convertCidadeToCidadeDTO(cidadeRepository.save(cidade));
    }

    public CidadeDTO findByEstado(String estado) {
        Cidade cidade = cidadeRepository.findByEstado(estado).orElseThrow(thrownResourceNotFound());

        return convertCidadeToCidadeDTO(cidade);
    }

    public CidadeDTO findById(Long id) {
        Cidade cidade = cidadeRepository.findById(id).orElseThrow(thrownResourceNotFound());

        return convertCidadeToCidadeDTO(cidade);
    }

    public CidadeDTO findByNome(String nome) {
        Cidade cidade = cidadeRepository.findByNome(nome).orElseThrow(thrownResourceNotFound());

        return convertCidadeToCidadeDTO(cidade);
    }

    private Supplier<ResourceNotFoundException> thrownResourceNotFound() {
        return () -> new ResourceNotFoundException("Cidade não encontrada");
    }

    private CidadeDTO convertCidadeToCidadeDTO(Cidade cidade) {
        return modelMapper.map(cidade, CidadeDTO.class);
    }
}
