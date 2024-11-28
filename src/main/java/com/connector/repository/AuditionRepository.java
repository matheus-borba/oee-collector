package com.connector.repository;

import java.util.Optional;

import com.connector.model.AuditionRequestResponse;
import com.connector.utils.ApoioUtil;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class AuditionRepository implements PanacheRepositoryBase<AuditionRequestResponse, Integer>, IAuditionRepository {

	@Override
	@Transactional
	public void save(AuditionRequestResponse audition) {
		if (ApoioUtil.isNotEmpty(audition.getId())) {
			Optional<AuditionRequestResponse> savedAudition = findByIdOptional(audition.getId());
			if(savedAudition.isPresent()) {
				AuditionRequestResponse entity = savedAudition.get();
				entity.setCreatedAt(entity.getCreatedAt());
				entity.setRequest(entity.getRequest());
				entity.setRequestMethod(entity.getRequestMethod());
				entity.setRequestUrl(entity.getRequestUrl());
				entity.setResponse(entity.getResponse());
				entity.setResponseStatus(entity.getResponseStatus());
				entity.setTookSeconds(entity.getTookSeconds());
				this.persistAndFlush(entity);
			}
		}
		persist(audition);
	}
}
