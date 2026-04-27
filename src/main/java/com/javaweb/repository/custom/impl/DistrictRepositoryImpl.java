package com.javaweb.repository.custom.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import com.javaweb.repository.DistrictRepository;
import com.javaweb.repository.entity.DistrictEntity;

@Repository
public class DistrictRepositoryImpl {

	@PersistenceContext
	private EntityManager entityManager;


	public DistrictEntity findNameById(Long id) {
		// JPA thuần: Dùng EntityManager để tìm Entity theo ID
		return entityManager.find(DistrictEntity.class, id);
	}
}