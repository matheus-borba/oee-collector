package com.connector.repository;

import java.util.List;
import java.util.Optional;

import com.connector.dto.FactoryDTO;
import com.connector.model.Factory;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
@ApplicationScoped
public class FactoryRepository implements PanacheRepositoryBase<Factory, Integer>, IFactoryRepository {

	@Override
	@Transactional
	public void save(FactoryDTO dto) {
		Factory factory = dto.toEntity();
		log.infov( "Saving Factory id {0}", factory.getId());
		Optional<Factory> opt = this.findByIdOptional(factory.getId());

		opt.ifPresentOrElse(existingFactory -> {
			existingFactory.setName(factory.getName());
			existingFactory.setLocation(factory.getLocation());
			existingFactory.setManagerName(factory.getManagerName());
			existingFactory.setContactEmail(factory.getContactEmail());
			existingFactory.setPhoneNumber(factory.getPhoneNumber());
			existingFactory.setStatus(factory.getStatus());
			this.persistAndFlush(existingFactory);
		}, () -> {
			this.persist(factory);
		});
	}

	@Override
	@Transactional
	public Optional<Factory> findByFactoryId(Integer id) {
		return this.findByIdOptional(id);
	}

	@Override
	@Transactional
	public List<Factory> listAllFactories() {
		return this.listAll();
	}

	@Override
	@Transactional
	public List<Factory> listAllActiveFactories() {
		return list("status");
	}

	@Override
	@Transactional
	public void deleteByFactoryId(Integer id) {
		deleteById(id);
	}
}
