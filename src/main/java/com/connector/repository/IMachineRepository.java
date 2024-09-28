package com.connector.repository;

import java.util.List;
import java.util.Optional;

import com.connector.dto.MachineDTO;
import com.connector.model.Machine;

public interface IMachineRepository {

	void save(MachineDTO dto);

	Optional<Machine> findByMachineId(Integer id);

	List<Machine> listAllMachines();

	void deleteByMachineId(Integer id);
}
