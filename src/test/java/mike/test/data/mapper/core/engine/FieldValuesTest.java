package mike.test.data.mapper.core.engine;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import mike.data.mapper.core.engine.domain.MapExpressionException;
import mike.data.mapper.core.engine.domain.FieldType;
import mike.data.mapper.core.engine.domain.FieldValueFactory;
import mike.data.mapper.core.engine.domain.FieldValues;

@DisplayName("FieldValues")
class FieldValuesTest {

    @Test
    void should_throw_mapper_exception_when_unsupported_value_object_type() {
	
	assertThatExceptionOfType(MapExpressionException.class)
		.isThrownBy( () -> FieldValueFactory.of("FOO", true));
	
	assertThatExceptionOfType(MapExpressionException.class)
		.isThrownBy( () -> FieldValueFactory.of("FOO", null));
    }
    
    @Test
    void should_return_foovalue_when_raw_value_of_type() {
	FieldValues fooValues = FieldValues.of(
		List.of(FieldValueFactory.of(FieldType.CHAR, "FD_CHAR", "My_Value"), 
			FieldValueFactory.of(FieldType.DATE, "FD_DATE", "20210112"),
			FieldValueFactory.of(FieldType.DATE, "FD_DATE_EMPTY", ""),
			FieldValueFactory.of(FieldType.NUMBER, "FD_NUM", "+54321"),
			FieldValueFactory.of(FieldType.NUMBER, "FD_NUM_EMPTY", ""),
			FieldValueFactory.of(FieldType.NUMBER, "FD_NUM_LONG", "1234567890000"),
			FieldValueFactory.of(FieldType.NUMBER, "FD_INT", "-1234"),
			FieldValueFactory.of(FieldType.REAL, "FD_REAL", "-987.25"),
			FieldValueFactory.of(FieldType.REAL, "FD_REAL_EMPTY", ""))
		);
	
	assertThat(fooValues.getValues()).hasSize(9);
	assertThat(fooValues.getVariables()).isEmpty();
    }
    
    @Test
    void should_return_foovalue_when_supported_value_object_type() {
	
	FieldValues fooValues = FieldValues.of(
		List.of(FieldValueFactory.of("FD_CHAR", "My_Value"), 
			FieldValueFactory.of("FD_DATE", LocalDate.of(2021, 1, 12)),
			FieldValueFactory.of("FD_LONG", -54321L),
			FieldValueFactory.of("FD_INTEGER", 1234),
			FieldValueFactory.of("FD_DOUBLE", 987.25),
			FieldValueFactory.of("FD_DECIMAL", BigDecimal.valueOf(1200.659)))
		);
	
	fooValues.setVariable(FieldValueFactory.of("LC_TEST", "TEST"));
	
	assertThat(fooValues.getValues()).hasSize(6);
	assertThat(fooValues.getVariables()).hasSize(1);
	
    }
}
