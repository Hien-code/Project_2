package com.javaweb.repository.impl;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import com.javaweb.repository.RentAreaRepository;
import com.javaweb.repository.entity.RentAreaEntity;

@Repository
public class RentAreaRepositoryImpl implements RentAreaRepository {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<RentAreaEntity> getValueByBuildingId(Long id) {
		// Dùng JPQL (JPA Query Language) thay cho SQL Native
		String jpql = "SELECT r FROM RentAreaEntity r WHERE r.buildingEntity.id = :id";
		return entityManager.createQuery(jpql, RentAreaEntity.class)
				.setParameter("id", id)
				.getResultList();
	}
}