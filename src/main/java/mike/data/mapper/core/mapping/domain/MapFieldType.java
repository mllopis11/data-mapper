package mike.data.mapper.core.mapping.domain;

public record MapFieldType(FieldType type, int scale, String format) {

    public static MapFieldType of(String expression) {
	return MapFieldTypeExpression.of(expression);
    }
}
