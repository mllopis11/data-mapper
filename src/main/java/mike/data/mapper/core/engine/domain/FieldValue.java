package mike.data.mapper.core.engine.domain;

import mike.data.mapper.core.mapping.FieldType;

public class FieldValue {

    private final FieldType type;
    private final String name;
    private final String rawValue;
    private final Object objValue;

    protected FieldValue(FieldType type, String name, String rawValue, Object objValue) {
	this.type = type;
	this.name = name;
	this.rawValue = rawValue;
	this.objValue = objValue;
    }

    public FieldType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getRawValue() {
        return rawValue;
    }

    public Object getObjValue() {
        return objValue;
    }

    @Override
    public String toString() {
	return String.format("FooValue [type=%s, name=%s, rawValue=%s, objValue=%s]", type, name, rawValue, objValue);
    }
}
