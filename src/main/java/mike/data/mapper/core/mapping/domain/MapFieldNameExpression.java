package mike.data.mapper.core.mapping.domain;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mike.bootstrap.utilities.exceptions.ApplicationException;
import mike.bootstrap.utilities.helpers.Offset;
import mike.bootstrap.utilities.helpers.PreConditions;
import mike.bootstrap.utilities.helpers.Strings;

class MapFieldNameExpression {

    private static final Pattern namePattern = 
	    Pattern.compile("(?<name>[A-Z0-9_]{3,64})(\\[(?<offset>(?<pos>[1-9][0-9]{0,3}):(?<len>[1-9][0-9]{0,3}))\\]$)?");
    
    private MapFieldNameExpression() {} 
    
    static MapFieldName of(String expression) {
	PreConditions.notBlank(expression, "FieldName: no such expression");
	
	var nameMatcher = Optional.of(namePattern.matcher(expression))
		.filter(Matcher::matches)
		.orElseThrow(() -> new ApplicationException("FieldName: invalid name expression (expression: %s)", expression));

	return new MapFieldName(nameMatcher.group("name"), fieldOffset(nameMatcher));
    }
    
    private static Offset fieldOffset(Matcher nameMatcher) {
	return Optional.ofNullable(nameMatcher.group("offset"))
			.map(os -> { 
			    var pos = Strings.toInteger(nameMatcher.group("pos"));
			    var len = Strings.toInteger(nameMatcher.group("len"));
			    return Offset.of(pos, len); 
			})
			.orElseGet(Offset::of);
			
    }
}
