package com.javaweb.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.javaweb.model.BuildingDTO;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.repository.entity.RentAreaEntity;

//Lấy Thực thể từ DB ra "trang điểm" thành DTO để trả về cho Client.
@Component
public class BuildingDTOConverter {

	@Autowired
	private ModelMapper modelMapper;

	// Handle BuildingEntity --> BuildingDTO
	public BuildingDTO toBuildingDTO(BuildingEntity item) {
		BuildingDTO building = modelMapper.map(item, BuildingDTO.class);
		// Entity
		building.setAddress(item.getStreet() + "-" + item.getWard() + "-" + item.getDistrictEntity().getName());
		List<RentAreaEntity> rentAreas = item.getRentAreaEntities();
		String areaResult = rentAreas.stream().map(it -> it.getValue().toString()).collect(Collectors.joining(" , "));
		building.setRentArea(areaResult);
		return building;
	}

}