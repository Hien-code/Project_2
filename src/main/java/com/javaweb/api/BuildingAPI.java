package com.javaweb.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.javaweb.customException.FieldRequiredException;
import com.javaweb.model.BuildingDTO;
import com.javaweb.service.BuildingService;

@RestController
public class BuildingAPI {
	@Autowired
	private BuildingService buildingService;

	@GetMapping(value = "/api/building/")
	public List<BuildingDTO> getBuilding(@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "districtid", required = false) Long districtId) {
		List<BuildingDTO> result = buildingService.findAll(name, districtId);
		return result;
	}

//	@RequestMapping(value = "/api/building/", method = RequestMethod.GET)
//	public void getBuilding(
//			@RequestParam(value = "name", required = false) String nameBuilding,
//			@RequestParam(value = "numberOfBasements", required = false) Integer numberOfBasements,
//			@RequestParam(value = "ward", required = false) String ward) {
//		System.out.println(nameBuilding + " " + numberOfBasements + " " + ward);
//	}
//	@GetMapping(value = "/api/building/")
//	public Object getBuilding(@RequestParam(value = "name", required = false) String nameBuilding,
//			@RequestParam(value = "numberOfBasements", required = false) Integer numberOfBasements,
//			@RequestParam(value = "ward", required = false) String ward) {
//		try {
//			System.out.println(5/1);
//		} catch (Exception e) {
//			ErrorResponseDTO error = new ErrorResponseDTO();
//			error.setError(e.getMessage());
//			List<String> details = new ArrayList<String>();
//			details.add("Số nguyên không thể chia cho 0");
//			error.setDetail(details);
//			return error;
//		}
//		BuildingDTO result = new BuildingDTO();
//		result.setName(nameBuilding);
//		result.setNumberOfBasement(numberOfBasements);
//		result.setWard(ward);
//		return result;
//	}

	// Hàm trả về cho FE
//	@GetMapping(value = "/api/building/")
//	public List<BuildingDTO> getBuilding(@RequestParam(value = "name", required = false) String name,
//			@RequestParam(value = "numberOfBasements", required = false) Integer numberOfBasements,
//			@RequestParam(value = "ward", required = false) String ward){
//		List<BuildingDTO> buildings = new ArrayList<BuildingDTO>();
//		BuildingDTO buildingDTO1 = new BuildingDTO();
//		buildingDTO1.setName("Res 3");
//		buildingDTO1.setNumberOfBasement(10);
//		buildingDTO1.setStreet("Quan 7");
//		buildingDTO1.setStreet("Tan Hiep Hung");
//		BuildingDTO buildingDTO2 = new BuildingDTO();
//		buildingDTO2.setName("Tro");
//		buildingDTO2.setNumberOfBasement(2);
//		buildingDTO2.setStreet("Thanh Loc 27");
//		buildingDTO2.setWard("Quan 12");
//		buildings.add(buildingDTO1);
//		buildings.add(buildingDTO2);
//		return buildings;
//	}

//	@RequestMapping(value ="/api/building/", method = RequestMethod.POST)
//	public void postBuilding(@RequestBody Map<String, String> params) {
//		System.out.println(params);
//	}

//	@RequestMapping(value = "/api/building/", method = RequestMethod.POST)
//	public void postBuilding(@RequestBody BuildingDTO buildingDTO) {
//		System.out.println("oke");
//	}

//	@DeleteMapping(value = "/api/building/{id}")
//	public void deleteBuilding(@PathVariable Integer id, @PathVariable String name) {
//		System.out.println("Đã xóa sp có id " + id);
//	}

	public void validate(BuildingDTO buildingDTO) {
		if (buildingDTO.getName() == null || buildingDTO.getName().equals("")
				|| buildingDTO.getNumberOfBasement() == null) {
			throw new FieldRequiredException("number or name is null");
		}
	}

//	@PostMapping(value = "/api/building/")
//	public Object test(@RequestBody BuildingDTO buildingDTO) {
//		validate(buildingDTO);
//		return null;
//	}

//	@PostMapping(value = "/api/building/")
//	public Object getBuiBuilding(@RequestBody String buiding) {
//		return buiding;
//	}
}