package com.connector.repository;

import java.util.List;
import java.util.Optional;

import com.connector.dto.ProductionDTO;
import com.connector.model.Machine;
import com.connector.model.Production;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
@ApplicationScoped
public class ProductionRepository implements PanacheRepositoryBase<Production, Integer>, IProductionRepository {

	@Inject
	IMachineRepository machineRepository;

	@Override
	@Transactional
	public void save(ProductionDTO dto) {

		log.infov("Processing Machine id {0}", dto.getId());
		Optional<Machine> machine = machineRepository.findByMachineId(dto.getMachineId());

		if (machine.isPresent()) {
			Optional<Production> opt = this.findByIdOptional(dto.getId());

			opt.ifPresentOrElse(existingProduction -> {
				existingProduction.setShift(dto.getShift());
				existingProduction.setOeePercentage(dto.getOeePercentage());
				this.persistAndFlush(existingProduction);
			}, () -> {
				Production production = dto.toEntity();
				production.setMachine(machine.get());
				this.persist(production);
			});
		} else {
			log.errorv("Failed to save Production id {0}. Machine not found", dto.getId());
		}
	}

	@Override
	@Transactional
	public List<Production> listProductionWithoutOEE() {
		return list("oeePercentage is null");
	}

	@Override
	@Transactional
	public Optional<Production> findByProductionId(Integer id) {
		return this.findByIdOptional(id);
	}

	@Override
	@Transactional
	public List<Production> findByMachine(Machine machine) {
		return list("machine = ?1", machine);
	}

	@Override
	@Transactional
	public List<Production> listAllProductions() {
		return listAll();
	}

	@Override
	@Transactional
	public List<Production> listByIdMachine(Integer machineId) {
		Optional<Machine> machine = machineRepository.findByMachineId(machineId);	
		return findByMachine(machine.get());
	}

	@Override
	@Transactional
	public void deleteProductionById(Integer id) {
		deleteById(id);
	}
}
