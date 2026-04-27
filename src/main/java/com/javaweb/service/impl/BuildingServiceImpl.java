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
        // Gọi hàm findAll từ Custom Repository
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

		if (id != null) {
            // Spring Data JPA trả về Optional, phải dùng orElseThrow để bóc tách an toàn
			buildingEntity = buildingRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy tòa nhà với ID: " + id));
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

		// Lấy District bằng findById mặc định của Spring
		DistrictEntity districtEntity = districtRepository.findById(dto.getDistrictId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Quận!"));
		buildingEntity.setDistrictEntity(districtEntity);

		buildingRepository.save(buildingEntity);
	}

	@Override
	@Transactional
	public void deleteBuilding(Long id) {
        // Dùng hàm deleteById mặc định của JpaRepository
		buildingRepository.deleteById(id);
	}
}