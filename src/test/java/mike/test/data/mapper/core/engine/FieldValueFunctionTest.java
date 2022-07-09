package mike.test.data.mapper.core.engine;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import mike.data.mapper.core.engine.domain.FieldValue;
import mike.data.mapper.core.engine.domain.FieldValueFactory;
import mike.data.mapper.core.engine.domain.FieldValues;
import mike.data.mapper.core.mapping.domain.FieldType;

class FieldValueFunctionTest {

    private static final Logger log = LoggerFactory.getLogger(FieldValueFunctionTest.class);
    
    private static final StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
    private static final SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
    
    @ParameterizedTest
    @CsvSource({ "11;B  ;C3, 'C3B  11'" })
    void should_return_joined_values_when(String values, String expected) {
	
	String[] items = values.split(";");
	List<FieldValue> inValues = IntStream.range(0, items.length).boxed()
		.map(ii -> FieldValueFactory.of(FieldType.CHAR, "FD_" + (ii+1), items[ii]))
		.collect(Collectors.toUnmodifiableList());
	
	FieldValues fieldValues = FieldValues.of(inValues);
	
	Expression expression = this.compileCondition("FD_3", "FD_2", "FD_1");
	String joinValues = expression.getValue(evaluationContext, fieldValues, String.class);
	
	log.debug("Joined: '{}' -> '{}'", values, joinValues);
	
	assertThat(joinValues).isEqualTo(expected);
    }
    
    private Expression compileCondition(String... fieldNames) {
	String expression = Stream.of(fieldNames)
		.map(n -> String.format("values['%s'].getRawValue()", n))
		.collect(Collectors.joining("+"));
	
	return spelExpressionParser.parseExpression(expression);
    }
}

