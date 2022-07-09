package mike.data.mapper.core.mapping.domain;

import mike.bootstrap.utilities.helpers.Offset;

public record MapFieldName(String name, Offset offset) {

    public static MapFieldName of(String expression) {
	return MapFieldNameExpression.of(expression);
    }
}
