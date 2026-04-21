package com.javaweb.repository.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.utils.ConnectionJDBCUtil;
import com.javaweb.utils.NumberUtil;
import com.javaweb.utils.StringUtil;

@Repository
public class BuildingRepositoryImpl implements BuildingRepository {

	// DATABASE PROCESSING
	// Xử lý các câu lệnh JOIN bảng
	public static void joinTable(Map<String, Object> params, List<String> typeCode, StringBuilder sql) {
		// 1. join vs table assignment building (Check null an toàn)
		Object staffIdObj = params.get("staffId");
		if (staffIdObj != null && StringUtil.checkString(staffIdObj.toString())) {
			sql.append(" INNER JOIN assignmentbuilding as ab ON b.id = ab.buildingid ");
		}

		// 2. join vs table buildingRent type vs rentType
		if (typeCode != null && !typeCode.isEmpty()) {
			sql.append(" INNER JOIN buildingrenttype as br ON b.id = br.buildingid ");
			sql.append(" INNER JOIN renttype as rt ON rt.id = br.renttypeid ");
		}

		// 3. join vs table rentArea (Sửa đúng tên biến areaTo và check null an toàn)
		Object rentAreaFromObj = params.get("areaFrom");
		Object rentAreaToObj = params.get("areaTo");

		boolean hasAreaFrom = rentAreaFromObj != null && StringUtil.checkString(rentAreaFromObj.toString());
		boolean hasAreaTo = rentAreaToObj != null && StringUtil.checkString(rentAreaToObj.toString());

		if (hasAreaFrom || hasAreaTo) {
			sql.append(" INNER JOIN rentarea ra ON ra.buildingid = b.id ");
		}
	}

	// Xử lý các query tìm kiếm cơ bản (Nằm chung trong bảng building)
	public static void queryNormal(Map<String, Object> params, StringBuilder where) {
		for (Map.Entry<String, Object> it : params.entrySet()) {
			// Bỏ qua các trường phải xử lý đặc biệt
			if (!it.getKey().equals("staffId") && !it.getKey().equals("typeCode") && !it.getKey().startsWith("area")
					&& !it.getKey().startsWith("rentPrice")) {
				// Rào chắn null cực mạnh trước khi .toString()
				if (it.getValue() != null) {
					String value = it.getValue().toString();
					if (StringUtil.checkString(value)) {
						// Nếu là số thì dùng =
						if (NumberUtil.isNumber(value)) {
							where.append(" and b." + it.getKey() + " = " + value);
						}
						// Nếu là chữ thì dùng LIKE
						else {
							where.append(" and b." + it.getKey() + " like '%" + value + "%' ");
						}
					}
				}
			}
		}
	}

	// Xử lý các query tìm kiếm đặc biệt (Khoảng giá, diện tích, id...)
	public static void querySpecial(Map<String, Object> params, List<String> typeCode, StringBuilder where) {
		// 1. Lọc theo nhân viên phụ trách
		Object staffIdObj = params.get("staffId");
		if (staffIdObj != null && StringUtil.checkString(staffIdObj.toString())) {
			where.append(" and ab.staffid = " + staffIdObj.toString());
		}
		// 2. Lọc theo khoảng diện tích thuê (Từ - Đến)
		Object rentAreaFromObj = params.get("areaFrom");
		if (rentAreaFromObj != null && StringUtil.checkString(rentAreaFromObj.toString())) {
			where.append(" and ra.value >= " + rentAreaFromObj.toString());
		}
		Object rentAreaToObj = params.get("areaTo");
		if (rentAreaToObj != null && StringUtil.checkString(rentAreaToObj.toString())) {
			where.append(" and ra.value <= " + rentAreaToObj.toString());
		}
		// 3. Lọc theo khoảng giá thuê (Từ - Đến)
		Object rentPriceFromObj = params.get("rentPriceFrom");
		if (rentPriceFromObj != null && StringUtil.checkString(rentPriceFromObj.toString())) {
			where.append(" and b.rentprice >= " + rentPriceFromObj.toString());
		}
		Object rentPriceToObj = params.get("rentPriceTo");
		if (rentPriceToObj != null && StringUtil.checkString(rentPriceToObj.toString())) {
			where.append(" and b.rentprice <= " + rentPriceToObj.toString());
		}
		// 4. Lọc theo loại hình tòa nhà (Dùng Java 8 Stream API cho xịn)
		if (typeCode != null && !typeCode.isEmpty()) {
			String joinedCodes = typeCode.stream().map(item -> "'" + item + "'") // Kẹp nháy đơn vào từng item
					.collect(Collectors.joining(",")); // Nối với nhau bằng dấu phẩy
			where.append(" and rt.code in (" + joinedCodes + ")");
		}
	}

	@Override
	public List<BuildingEntity> findAll(Map<String, Object> params, List<String> typeCode) {
		// 1. Build phần SELECT và FROM
		StringBuilder sql = new StringBuilder(
				"SELECT b.id, b.name, b.districtid, b.street, b.ward, b.numberofbasement, b.floorarea, b.rentprice, b.managername, b.managerphonenumber, b.servicefee, b.brokeragefee\n"
						+ "from building b ");
		// 2. Nối các phần JOIN
		joinTable(params, typeCode, sql);

		// 3. Build phần WHERE
		StringBuilder where = new StringBuilder("\nwhere 1 = 1 ");
		queryNormal(params, where);
		querySpecial(params, typeCode, where);

		// 4. Ghép toàn bộ SQL lại và chốt hạ bằng GROUP BY
		sql.append(where);
		sql.append(" group by b.id");

		List<BuildingEntity> result = new ArrayList<>();

		// Thực thi câu lệnh (Có tự động đóng Connection nhờ try-with-resources)
		try (Connection conn = ConnectionJDBCUtil.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql.toString())) {

			System.out.println("Connected database successfully...");

			// Map dữ liệu từ ResultSet vào Object
			while (rs.next()) {
				BuildingEntity buildingEntity = new BuildingEntity();
				buildingEntity.setId(rs.getLong("id"));
				buildingEntity.setName(rs.getString("name"));
				buildingEntity.setWard(rs.getString("ward"));
				buildingEntity.setDistrictid(rs.getLong("districtid"));
				buildingEntity.setStreet(rs.getString("street"));
				buildingEntity.setFloorArea(rs.getLong("floorarea"));
				buildingEntity.setRentPrice(rs.getLong("rentprice"));
				buildingEntity.setServiceFee(rs.getString("servicefee"));
				buildingEntity.setBrokerageFee(rs.getLong("brokeragefee"));
				buildingEntity.setManagerName(rs.getString("managername"));
				buildingEntity.setManagerPhoneNumber(rs.getString("managerphonenumber"));
				buildingEntity.setNumberOfBasement(rs.getInt("numberofbasement"));

				result.add(buildingEntity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Connected database failed...");
		}
		return result;
	}
}