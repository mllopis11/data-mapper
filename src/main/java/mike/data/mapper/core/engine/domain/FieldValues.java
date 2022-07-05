package mike.data.mapper.core.engine.domain;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import mike.bootstrap.utilities.helpers.PreConditions;

/**
 * Record values. 
 * 
 * @author Mike
 */
public class FieldValues {

    private final Map<String, FieldValue> values; 
    
    public static FieldValues of(FieldValue... values) {
	return new FieldValues(Stream.of(values));
    }
    
    public static FieldValues of(List<FieldValue> values) {
	return new FieldValues(values.stream());
    }
    
    private FieldValues(Stream<FieldValue> values) {
	this.values = values.collect(Collectors.toMap(FieldValue::getName, Function.identity()));
    }

    /**
     * @return the record field values 
     */
    public Map<String, FieldValue> getValues() {
        return values;
    }
    
    /**
     * Add or replace a record field value.
     * 
     * @param value the field value to set (not null)
     */
    public void setValue(FieldValue value) {
	PreConditions.notNull(value, "FieldValues::setValue: value required");
	this.values.put(value.getName(), value);
    }

    /**
     * @param name field name
     * @return the record field value if exists.
     */
    public Optional<FieldValue> getValue(String name) {
	return Optional.ofNullable(values.get(name));
    }

    @Override
    public String toString() {
	return String.format("FooValues [values=%s]", values);
    }
}
