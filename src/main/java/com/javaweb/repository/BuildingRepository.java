package com.javaweb.repository;

import java.util.List;
import com.javaweb.builder.BuildingSearchBuilder;
import com.javaweb.repository.entity.BuildingEntity;

public interface BuildingRepository {
	List<BuildingEntity> findAll(BuildingSearchBuilder buildingSearchBuilder);
	BuildingEntity findById(Long id);
	void save(BuildingEntity buildingEntity);
	void deleteById(Long id);
}