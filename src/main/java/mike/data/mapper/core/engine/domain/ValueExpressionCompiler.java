package mike.data.mapper.core.engine.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

class ValueExpressionCompiler {

    private static final Pattern fieldNamePattern = Pattern.compile("(\\$[0-9A-Z_]{3,64})");
    
    private final SpelExpressionParser sep = new SpelExpressionParser();
    
    private Set<String> referencedFields;
    private List<String> preparedExpressions;
    
    List<Expression> compile(String expression) {
	this.referencedFields = this.parseReferencedFields(expression);

	this.preparedExpressions = Stream.of(expression.split(";"))
		.map(expr -> this.prepare(expression, referencedFields))
		.collect(Collectors.toList());
	
	return this.preparedExpressions.stream()
			.map(expr -> sep.parseExpression(expr))
			.collect(Collectors.toList());
    }
    
    Set<String> getReferencedFields() {
	return this.referencedFields;
    }
    
    List<String>  getPreparedExpressions() {
	return this.preparedExpressions;
    }
    
    private String prepare(String expression, Set<String> fieldNameRefs) {
	String expr = expression.strip();
	
	for ( String fd : fieldNameRefs ) {
	    String spelfieldRef = this.buildSpelExpression(fd);
	    expr = expr.replaceAll("\\" + fd, spelfieldRef);
	}
	
	expr.replaceAll("(?i)@RPAD", "#fctRPad");
	expr.replaceAll("(?i)@LPAD", "#fctLPad");
	expr.replaceAll("(?i)@TRIM", "#fctTrim");
	expr.replaceAll("(?i)@LTRIM", "#fctLTrim");
	expr.replaceAll("(?i)@RTRIM", "#fctRTrim");
	
	return expr;
    }
    
    private Set<String> parseReferencedFields(String expression) {
	Matcher matcher = fieldNamePattern.matcher(expression);
	
	final Set<String> fieldNames = new HashSet<>();
	
	while ( matcher.find() ) {
	    IntStream.range(0, matcher.groupCount())
	    	.forEach(ii -> fieldNames.add(matcher.group(ii)));
	}
	
	return fieldNames;
    }
    
    private String buildSpelExpression(String fieldName) {
	return String.format("values['%s'].getObjValue()", fieldName.replaceFirst("\\$", ""));
    }
}
