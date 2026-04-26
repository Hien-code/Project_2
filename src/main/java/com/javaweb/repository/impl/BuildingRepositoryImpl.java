package com.javaweb.repository.impl;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.javaweb.builder.BuildingSearchBuilder;
import com.javaweb.repository.BuildingRepository;
import com.javaweb.repository.entity.BuildingEntity;
import com.javaweb.utils.StringUtil;

@Repository
public class BuildingRepositoryImpl implements BuildingRepository {
	
	@PersistenceContext
	private EntityManager entityManager;

	// 1. Xử lý các câu lệnh JOIN bảng
	public static void joinTable(BuildingSearchBuilder builder, StringBuilder sql) {
		if (builder.getStaffId() != null) {
			sql.append(" INNER JOIN assignmentbuilding as ab ON b.id = ab.buildingid ");
		}
		List<String> typeCode = builder.getTypeCode();
		if (typeCode != null && !typeCode.isEmpty()) {
			sql.append(" INNER JOIN buildingrenttype as br ON b.id = br.buildingid ");
			sql.append(" INNER JOIN renttype as rt ON rt.id = br.renttypeid ");
		}
		if (builder.getAreaFrom() != null || builder.getAreaTo() != null) {
			sql.append(" INNER JOIN rentarea ra ON ra.buildingid = b.id ");
		}
	}

	// 2. Xử lý các query tìm kiếm cơ bản (Dùng Reflection để quét các field trong
	// Builder)
	public static void queryNormal(BuildingSearchBuilder builder, StringBuilder where) {
		try {
			// Lấy tất cả các thuộc tính khai báo trong class BuildingSearchBuilder
			Field[] fields = BuildingSearchBuilder.class.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true); // Cho phép truy cập field private
				String fieldName = field.getName();

				if (!fieldName.equals("staffId") && !fieldName.equals("typeCode") && !fieldName.startsWith("area")
						&& !fieldName.startsWith("rentPrice")) {
					Object value = field.get(builder);
					if (value != null && StringUtil.checkString(value.toString())) {
						if (value instanceof Number) {
							where.append(" AND b." + fieldName.toLowerCase() + " = " + value);
						} else {
							where.append(" AND b." + fieldName.toLowerCase() + " LIKE '%" + value + "%' ");
						}
					}
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	// 3. Xử lý các query tìm kiếm đặc biệt (Khoảng giá, diện tích, id nhân viên...)
	public static void querySpecial(BuildingSearchBuilder builder, StringBuilder where) {
		if (builder.getStaffId() != null) {
			where.append(" AND ab.staffid = " + builder.getStaffId());
		}
		if (builder.getAreaFrom() != null) {
			where.append(" AND ra.value >= " + builder.getAreaFrom());
		}
		if (builder.getAreaTo() != null) {
			where.append(" AND ra.value <= " + builder.getAreaTo());
		}
		if (builder.getRentPriceFrom() != null) {
			where.append(" AND b.rentprice >= " + builder.getRentPriceFrom());
		}
		if (builder.getRentPriceTo() != null) {
			where.append(" AND b.rentprice <= " + builder.getRentPriceTo());
		}
		List<String> typeCode = builder.getTypeCode();
		if (typeCode != null && !typeCode.isEmpty()) {
			String joinedCodes = typeCode.stream().map(item -> "'" + item + "'").collect(Collectors.joining(","));
			where.append(" AND rt.code IN (" + joinedCodes + ")");
		}
	}

	@Override
	public List<BuildingEntity> findAll(BuildingSearchBuilder builder) {
		StringBuilder sql = new StringBuilder(
				"SELECT b.id, b.name, b.districtid, b.street, b.ward, b.numberofbasement, "
						+ "b.floorarea, b.rentprice, b.managername, b.managerphonenumber, b.servicefee, b.brokeragefee "
						+ "FROM building b ");

		// Bước 1: Nối JOIN
		joinTable(builder, sql);

		// Bước 2: Nối WHERE
		StringBuilder where = new StringBuilder("\nWHERE 1 = 1 ");
		queryNormal(builder, where);
		querySpecial(builder, where);

		sql.append(where);
		sql.append(" GROUP BY b.id");

		Query query = entityManager.createNativeQuery(sql.toString(), BuildingEntity.class);
		
		return query.getResultList();
	}

	@Override
	public BuildingEntity findById(Long id) {
		return entityManager.find(BuildingEntity.class, id);
	}

	@Override
	public void save(BuildingEntity buildingEntity) {
		if (buildingEntity.getId() != null) {
			// Có ID -> Update
			entityManager.merge(buildingEntity);
		} else {
			// Không có ID -> Insert
			entityManager.persist(buildingEntity);
		}
	}

	@Override
	public void deleteById(Long id) {
		BuildingEntity entity = entityManager.find(BuildingEntity.class, id);
		if (entity != null) {
			entityManager.remove(entity);
		}
	}
}