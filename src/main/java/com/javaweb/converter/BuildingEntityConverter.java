package com.javaweb.converter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.javaweb.model.BuildingRequestDTO;
import com.javaweb.repository.entity.BuildingEntity;

//Xử lý data từ JSON đúc thành Thực thể (Entity) để lưu DB
@Component
public class BuildingEntityConverter {

	@Autowired
	private ModelMapper modelMapper;

	// Xử lý BuildingRequestDTO --> BuildingEntity (Dùng cho Thêm mới / Cập nhật)
	public BuildingEntity toBuildingEntity(BuildingRequestDTO dto, BuildingEntity buildingEntity) {
		modelMapper.map(dto, buildingEntity);
		return buildingEntity;
	}
}