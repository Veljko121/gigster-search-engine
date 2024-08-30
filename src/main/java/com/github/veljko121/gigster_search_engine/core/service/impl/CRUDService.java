package com.github.veljko121.gigster_search_engine.core.service.impl;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.github.veljko121.gigster_search_engine.core.service.ICRUDService;

public abstract class CRUDService<T, TRequestDTO, TResponseDTO, TUpdateRequestDTO, ID> implements ICRUDService<T, TRequestDTO, TResponseDTO, TUpdateRequestDTO, ID> {

    private ElasticsearchRepository<T, ID> repository;

    public CRUDService(ElasticsearchRepository<T, ID> repository) {
        super();
        this.repository = repository;
    }

    public TResponseDTO findById(ID id) throws NoSuchElementException {
        return mapToResponseDTO(findByIdDomain(id));
    }

    protected T findByIdDomain(ID id) throws NoSuchElementException {
        return repository.findById(id).orElseThrow();
    }

    public Iterable<TResponseDTO> findAll() {
        return mapToResponseDTOs(findAllDomain());
    }

    protected Iterable<T> findAllDomain() {
        return repository.findAll();
    } 

    public Iterable<TResponseDTO> findAllByIds(Iterable<ID> ids) {
        return mapToResponseDTOs(findAllByIdsDomain(ids));
    }

    public Iterable<T> findAllByIdsDomain(Iterable<ID> ids) {
        return repository.findAllById(ids);
    }

    public TResponseDTO save(TRequestDTO requestDTO) {
        return mapToResponseDTO(saveDomain(requestDTO));
    }

    protected T saveDomain(TRequestDTO requestDTO) {
        return repository.save(mapToDomain(requestDTO));
}

    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public TResponseDTO update(ID id, TUpdateRequestDTO updatedEntityRequestDTO) {
        return mapToResponseDTO(updateDomain(id, updatedEntityRequestDTO));
    }

    public T updateDomain(ID id, TUpdateRequestDTO updatedEntityRequestDTO) {
        return repository.save(mapUpdatedFieldsToDomain(findByIdDomain(id), updatedEntityRequestDTO));
    }

    protected abstract TResponseDTO mapToResponseDTO(T entity);

    protected abstract T mapToDomain(TRequestDTO requestDTO);

    protected abstract T mapUpdatedFieldsToDomain(T entity, TUpdateRequestDTO updatedEntityRequestDTO);

    protected Iterable<TResponseDTO> mapToResponseDTOs(Iterable<T> entities) {
        var responseDTOs = StreamSupport.stream(entities.spliterator(), false).map(this::mapToResponseDTO).collect(Collectors.toList());
        return responseDTOs;
    }

    protected Iterable<T> mapToDomains(Iterable<TRequestDTO> requestDTOs) {
        var entities = StreamSupport.stream(requestDTOs.spliterator(), false).map(this::mapToDomain).collect(Collectors.toList());
        return entities;
    }
    
}
