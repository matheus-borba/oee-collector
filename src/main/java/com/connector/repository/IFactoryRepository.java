package com.connector.repository;

import java.util.List;
import java.util.Optional;

import com.connector.dto.FactoryDTO;
import com.connector.model.Factory;

public interface IFactoryRepository {

	void save(FactoryDTO factory);

	Optional<Factory> findByFactoryId(Integer id);

	List<Factory> listAllFactories();

	List<Factory> listAllActiveFactories();

	void deleteByFactoryId(Integer id);
}
