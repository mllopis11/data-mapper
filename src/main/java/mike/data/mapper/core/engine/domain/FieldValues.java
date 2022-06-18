package mike.data.mapper.core.engine.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FieldValues {

    private final Map<String, FieldValue> values; 
    private final Map<String, FieldValue> variables = new HashMap<>();
    
    public static FieldValues of(FieldValue value) {
	return new FieldValues(List.of(value));
    }
    
    public static FieldValues of(List<FieldValue> values) {
	return new FieldValues(values);
    }
    
    private FieldValues(List<FieldValue> values) {
	this.values = values.stream()
		.collect(Collectors.toUnmodifiableMap(FieldValue::getName, Function.identity()));
    }

    public Map<String, FieldValue> getValues() {
        return values;
    }

    public Optional<FieldValue> getValue(String key) {
	return Optional.ofNullable(values.get(key));
    }
    
    public Map<String, FieldValue> getVariables() {
        return variables;
    }

    public void setVariable(FieldValue value) {
	this.variables.put(value.getName(), value);
    }
    
    public Optional<FieldValue> getVariable(String key) {
	return Optional.ofNullable(variables.get(key));
    }

    @Override
    public String toString() {
	return String.format("FooValues [values=%s, variables=%s]", values, variables);
    }
}
