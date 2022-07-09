package mike.data.mapper.core.mapping.domain;

import javax.validation.constraints.Min;

import mike.bootstrap.utilities.helpers.PreConditions;

public record MapField(@Min(1) int id, MapFieldName fieldName, MapFieldType fieldType, String description, String optValue) {

    public static MapField of(int id, RawField rawField) {
	PreConditions.test(id > 0, "MapField: id must be greater than zero");
	PreConditions.notNull(rawField, "MapField: raw field not provided");
	
	var fieldName = MapFieldName.of(rawField.name());
	var fieldType = MapFieldType.of(rawField.type());
	return new MapField(id, fieldName, fieldType, rawField.description(), rawField.optValue());
    }
    
    public String name() {
	return this.fieldName.name();
    }
    
    public FieldType type() {
	return this.fieldType.type();
    }

    public boolean isOptional() {
	return ! this.isRequired();
    }
    
    public boolean isRequired() {
	return this.optValue == null;
    }

    @Override
    public String toString() {
	return String.format("Field [id=%s, %s, %s, optValue=%s, required=%s, description=%s]", 
		id, fieldName, fieldType, optValue, this.isRequired(), description);
    }
}
