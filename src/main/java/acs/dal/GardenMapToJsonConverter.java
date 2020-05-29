package acs.dal;

import java.util.Map;

import javax.persistence.AttributeConverter;

import com.fasterxml.jackson.databind.ObjectMapper;

import acs.data.FacilityType;

public class GardenMapToJsonConverter implements AttributeConverter<Map<FacilityType,String>,String>{
	private ObjectMapper jackson;
	
	public GardenMapToJsonConverter() {
		this.jackson = new ObjectMapper();
	}

	@Override
	public String convertToDatabaseColumn(Map<FacilityType, String> map) {
		try {
			return this.jackson.writeValueAsString(map);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Map<FacilityType, String> convertToEntityAttribute(String json) {
		try {
			return this.jackson.readValue(json, Map.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
}
