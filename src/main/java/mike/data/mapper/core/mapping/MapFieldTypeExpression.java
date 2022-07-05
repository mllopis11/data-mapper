package mike.data.mapper.core.mapping;

import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mike.bootstrap.utilities.exceptions.ApplicationException;
import mike.bootstrap.utilities.helpers.PreConditions;
import mike.bootstrap.utilities.helpers.Strings;

class MapFieldTypeExpression {

    private static final Pattern typePattern = Pattern.compile("^([A-Z]{2,10})");
    private static final Pattern optionPattern = Pattern.compile("\\(.*\\)");
    
    private MapFieldTypeExpression() {}
    
    static MapFieldType of(String expression) {
	PreConditions.notBlank(expression, "FieldType: no such expression");
	
	var fieldType = parseFieldType(expression);
	var optionMatcher = optionMatcher(fieldType, expression);
	var format = Optional.ofNullable(optionMatcher.group("format")).orElseGet(fieldType::getDefaultFormat);
	
	var scale = scaleValue(fieldType, optionMatcher);
	
	assertFormat(fieldType, format, expression);
	
	return new MapFieldType(fieldType, scale, format);
    }
    
    private static FieldType parseFieldType(String expression) {
	var fieldType = Strings.sanitize(expression, optionPattern, Strings.EMPTY);
	return FieldType.from(fieldType)
		.orElseThrow(() -> new ApplicationException("FieldType: unknown field type (expression: %s)", expression));
    }
    
    private static Matcher optionMatcher(FieldType fieldType, String expression) {
	var options = Strings.sanitize(expression, typePattern, Strings.EMPTY);
	return fieldType.getOptionMatcher(options)
		.orElseThrow(() -> new ApplicationException("FieldType: option mismatch (expression: %s)", expression));
    }
    
    private static int scaleValue(FieldType fieldType, Matcher optionMatcher) {
	var scale = 0;
	
	if ( fieldType.hasScaleOption() ) {
	    scale = Optional.ofNullable(optionMatcher.group("scale")).map(Integer::parseInt).orElseGet(() -> 0);
	}
	
	return scale;
    }
    
    private static void assertFormat(FieldType fieldType, String format, String expression) {
	if (fieldType == FieldType.DATE) {
	    try {
		DateTimeFormatter.ofPattern(format);
	    } catch(IllegalArgumentException iae) {
		throw new ApplicationException(iae, "FieldType: invalid date format (expression: %s)", expression);
	    }
	}
    }
}
