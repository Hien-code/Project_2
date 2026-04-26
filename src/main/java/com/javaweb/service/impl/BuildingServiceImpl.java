package com.javaweb.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.javaweb.builder.BuildingSearchBuilder;
import com.javaweb.converter.BuildingDTOConverter;
import com.javaweb.converter.BuildingSearchBuilderConverter;
import com.javaweb.model.BuildingDTO;
import com.javaweb.model.BuildingRequestDTO;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.DistrictRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.DistrictEntity;
import com.javaweb.service.BuildingService;

@Service
public class BuildingServiceImpl implements BuildingService {

	@Autowired
	private BuildingRepository buildingRepository;
	
	@Autowired
	private DistrictRepository districtRepository;

	@Autowired
	private BuildingDTOConverter buildingDTOConverter;

	@Autowired
	private BuildingSearchBuilderConverter buildingSearchBuilderConverter;

	@Override
	public List<BuildingDTO> findAll(Map<String, Object> params, List<String> typeCode) {
		BuildingSearchBuilder buildingSearchBuilder = buildingSearchBuilderConverter.toBuildingSearchBuilder(params, typeCode);
		List<BuildingEntity> buildingEntities = buildingRepository.findAll(buildingSearchBuilder);
		
		List<BuildingDTO> result = new ArrayList<>();
		for (BuildingEntity item : buildingEntities) {
			result.add(buildingDTOConverter.toBuildingDTO(item));
		}
		return result;
	}

	@Override
	@Transactional
	public void saveOrUpdate(BuildingRequestDTO dto, Long id) {
		BuildingEntity buildingEntity = new BuildingEntity();
		
		if (id != null) { // Update
			buildingEntity = buildingRepository.findById(id);
		}

		// Mapping dữ liệu
		buildingEntity.setName(dto.getName());
		buildingEntity.setStreet(dto.getStreet());
		buildingEntity.setWard(dto.getWard());
		buildingEntity.setNumberOfBasement(dto.getNumberOfBasement());
		buildingEntity.setFloorArea(dto.getFloorArea());
		buildingEntity.setRentPrice(dto.getRentPrice());
		buildingEntity.setServiceFee(dto.getServiceFee());
		buildingEntity.setBrokerageFee(dto.getBrokerageFee());
		buildingEntity.setManagerName(dto.getManagerName());
		buildingEntity.setManagerPhoneNumber(dto.getManagerPhoneNumber());
		
		// Tìm District bằng JPA
		DistrictEntity districtEntity = districtRepository.findNameById(dto.getDistrictId());
		buildingEntity.setDistrictEntity(districtEntity);

		// Lưu xuống DB
		buildingRepository.save(buildingEntity);
	}

	@Override
	@Transactional
	public void deleteBuilding(Long id) {
		buildingRepository.deleteById(id);
	}
}