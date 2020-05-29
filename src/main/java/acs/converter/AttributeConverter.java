package acs.converter;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class AttributeConverter {
	private ObjectMapper objectMapper;
	
	public AttributeConverter() {
		this.objectMapper = new ObjectMapper();
	}
	
	public <T> T toAttribute(Object object, Class<T> type) {
		try {
			return this.objectMapper.readValue(objectMapper.writeValueAsString(object), type);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
