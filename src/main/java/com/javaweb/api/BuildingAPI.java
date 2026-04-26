package com.javaweb.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.javaweb.model.BuildingDTO;
import com.javaweb.model.BuildingRequestDTO;
import com.javaweb.service.BuildingService;

@RestController
@RequestMapping("/api/building")
public class BuildingAPI {
	
	@Autowired
	private BuildingService buildingService;

	@GetMapping
	public List<BuildingDTO> getBuilding(@RequestParam Map<String, Object> params,
			@RequestParam(name = "typeCode", required = false) List<String> typeCode) {
		List<BuildingDTO> buildings = buildingService.findAll(params, typeCode);
		return buildings;
	}
	
	@PostMapping
	public void createBuilding(@RequestBody BuildingRequestDTO buildingRequestDTO) {
		buildingService.saveOrUpdate(buildingRequestDTO, null);
	}
	
	@PutMapping("/{id}")
	public void editBuilding(@PathVariable Long id, @RequestBody BuildingRequestDTO buildingRequestDTO) {
		buildingService.saveOrUpdate(buildingRequestDTO, id);
	}
	
	@DeleteMapping("/{id}")
	public void deleteBuilding(@PathVariable Long id) {
		buildingService.deleteBuilding(id);
	}
}