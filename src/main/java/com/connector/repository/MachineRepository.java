package com.connector.repository;

import java.util.List;
import java.util.Optional;

import com.connector.dto.MachineDTO;
import com.connector.model.Factory;
import com.connector.model.Machine;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
@ApplicationScoped
public class MachineRepository implements PanacheRepositoryBase<Machine, Integer>, IMachineRepository {

	@Inject
	IFactoryRepository factoryRepository;

	@Override
	@Transactional
	public void save(MachineDTO dto) {
		Optional<Factory> factory = this.factoryRepository.findByFactoryId(dto.getFactoryId());
		Machine machine = dto.toEntity();
		machine.setFactory(factory.get());

		log.infov("Processing Machine id {0}", machine.getId());
		Optional<Machine> opt = this.findByIdOptional(machine.getId());
		opt.ifPresentOrElse(existingMachine -> {
			existingMachine.setLocation(machine.getLocation());
			existingMachine.setStatus(machine.getStatus());
			existingMachine.setOeePercentage(machine.getOeePercentage());
			this.persistAndFlush(existingMachine);
		}, () -> {
			this.persist(machine);
		});
	}

	@Override
	@Transactional
	public Optional<Machine> findByMachineId(Integer id) {
		return this.findByIdOptional(id);
	}

	@Override
	@Transactional
	public List<Machine> listAllMachines() {
		return this.listAll();
	}

	@Override
	@Transactional
	public List<Machine> listByFactoryId(Factory factory) {
		return list("factory = ?1", factory);
	}

	@Override
	@Transactional
	public void deleteByMachineId(Integer id) {
		deleteById(id);
	}
}
