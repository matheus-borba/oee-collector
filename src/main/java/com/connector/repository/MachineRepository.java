package com.connector.repository;

import java.util.List;
import java.util.Optional;

import com.connector.dto.MachineDTO;
import com.connector.model.Machine;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
@ApplicationScoped
public class MachineRepository implements PanacheRepositoryBase<Machine, Integer>, IMachineRepository {

	@Override
	@Transactional
	public void save(MachineDTO dto) {
		Machine machine = dto.toEntity();

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
	public void deleteByMachineId(Integer id) {
		deleteById(id);
	}
}
