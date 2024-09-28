package com.connector.repository;

import java.util.List;
import java.util.Optional;

import com.connector.dto.ProductionDTO;
import com.connector.model.Machine;
import com.connector.model.Production;

public interface IProductionRepository {

	void save(ProductionDTO dto);

	List<Production> listProductionWithoutOEE();

	Optional<Production> findByProductionId(Integer id);

	List<Production> findByMachine(Machine machine);

	List<Production> listAllProductions();

	List<Production> listByIdMachine(Integer machineId);

	void deleteProductionById(Integer id);
}
