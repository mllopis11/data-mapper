package mike.data.mapper.core.engine.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import mike.bootstrap.utilities.exceptions.ApplicationException;
import mike.data.mapper.core.mapping.domain.FieldType;

public class FieldValueFactory {

    private FieldValueFactory() {}
    
    public static FieldValue of(String name, Object objValue) {

	var rawValue = objValue != null ? String.valueOf(objValue) : "";
	
	if ( objValue instanceof String ) {
	    return new FieldValue(FieldType.CHAR, name, rawValue, objValue);
	} else if ( objValue instanceof LocalDate ) {
	    return new FieldValue(FieldType.DATE, name, rawValue, objValue);
	} else if ( objValue instanceof Long || objValue instanceof Integer) {
	    return new FieldValue(FieldType.NUM, name, rawValue, objValue);
	} else if ( objValue instanceof Double || objValue instanceof BigDecimal) {
	    return new FieldValue(FieldType.REAL, name, rawValue, objValue);
	} else {
	    String objType = objValue == null ? null : objValue.getClass().getSimpleName();
	    throw new ApplicationException("Unsupported value type '%s' for field '%s'", objType, name);
	} 
    }
    
    public static FieldValue of(FieldType type, String name, String rawValue) {
	Object objValue = switch(type) {
		case CHAR -> rawValue;
		case DATE -> rawValue.isBlank() ? null : LocalDate.parse(rawValue, DateTimeFormatter.BASIC_ISO_DATE);
		case NUM, INT -> rawValue.isBlank() ? 0 : Long.parseLong(rawValue);
		case REAL -> rawValue.isBlank() ? Double.valueOf(0) : Double.valueOf(rawValue);
		};
	
	return new FieldValue(type, name, rawValue, objValue);
    }
}
