package com.connector.service;

import com.connector.model.AuditionRequestResponse;
import com.connector.repository.IAuditionRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AuditionService {

	@Inject
	IAuditionRepository repository;

	@Transactional
	public void saveAudition(AuditionRequestResponse audition) {
		repository.save(audition);
	}
}
