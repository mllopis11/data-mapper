package mike.test.data.mapper.core.engine;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import mike.data.mapper.core.engine.domain.FieldValueFactory;
import mike.data.mapper.core.engine.domain.FieldValues;
import mike.data.mapper.core.engine.domain.ValueExpression;
import mike.data.mapper.core.mapping.FieldType;

/**
 * SPeL Date Comparison.
 * <p> 
 * From SPeL documentation on Relational Operators:
 * <pre>
 * Any value is always greater than null (X > null is always true) and is ever less than nothing (X < null is always false).
 * </pre>
 * 
 * @author Mike
 */
@DisplayName("FooValueDate")
class FieldValueDateTest {

    private static final Logger log = LoggerFactory.getLogger(FieldValueDateTest.class);
    
    private static final StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
    
    @BeforeAll
    static void init() {
	evaluationContext.setVariable("GV_ASOFDATE", LocalDate.of(2021, 1, 15));
    }
    
    @ParameterizedTest
    @CsvSource({ "'', false", "20210130, true", "20210115, true", "20210114, false" })
    void should_return_expected_boolean_when_asofdate_ge_20210115(String asOfDate, boolean expected) {
	
	var dateValue = FieldValueFactory.of(FieldType.DATE, "AS_OF_DATE", asOfDate);
	var dateValues = FieldValues.of(dateValue);
	var dateExpression = ValueExpression.of("$AS_OF_DATE ge #GV_ASOFDATE");

	log.debug("ValueExpressions: {} [fields: {}]", dateExpression.getPreparedExpressions(), dateExpression.getReferencedFields());
	
	var eval = this.apply(dateExpression, dateValues);
	
	assertThat(eval).isEqualTo(expected);
    }
    
    @ParameterizedTest
    @CsvSource({ "'', true", "20210130, false", "20210115, true", "20210114, true" })
    void should_return_expected_boolean_when_asofdate_le_20210115(String asOfDate, boolean expected) {
	
	var dateValue = FieldValueFactory.of(FieldType.DATE, "AS_OF_DATE", asOfDate);
	var dateValues = FieldValues.of(dateValue);
	var dateExpression = ValueExpression.of("$AS_OF_DATE le #GV_ASOFDATE");

	log.debug("ValueExpressions: {} [fields: {}]", dateExpression.getPreparedExpressions(), dateExpression.getReferencedFields());
	
	var eval = this.apply(dateExpression, dateValues);
	
	assertThat(eval).isEqualTo(expected);
    }
    
    private boolean apply(ValueExpression ve, FieldValues fv) {
	return ve.getExpressions().stream()
		.map(exp -> exp.getValue(evaluationContext, fv, Boolean.class))
		.filter(rv -> rv != null)
		.findFirst()
		.orElse(Boolean.FALSE);
    }
}

