package mike.data.mapper.core.mapping;

public record MapFieldType(FieldType type, int scale, String format) {

    public static MapFieldType of(String expression) {
	return MapFieldTypeExpression.of(expression);
    }
}
