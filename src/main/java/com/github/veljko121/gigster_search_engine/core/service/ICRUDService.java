package com.github.veljko121.gigster_search_engine.core.service;

import java.util.NoSuchElementException;

public interface ICRUDService<T, TRequestDTO, TResponseDTO, TUpdateRequestDTO, ID> {

    TResponseDTO findById(ID id) throws NoSuchElementException;

    Iterable<TResponseDTO> findAll();

    Iterable<TResponseDTO> findAllByIds(Iterable<ID> ids);

    TResponseDTO save(TRequestDTO reqeustDTO);

    TResponseDTO update(ID id, TUpdateRequestDTO updatedEntityRequestDTO);
    
    void deleteById(ID id);

}
