package mike.test.data.mapper.core.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import mike.bootstrap.utilities.exceptions.ApplicationException;
import mike.data.mapper.core.mapping.MapFieldName;

@DisplayName("MapFieldName")
class MapFieldNameTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = "   ")
    void should_throw_IllegalArgumentException_when_name_expression_null_or_empty(String expr) {
	
	assertThatIllegalArgumentException()
		.isThrownBy(() -> MapFieldName.of(expr))
		.withMessageContaining("FieldName:", expr);
    }
    
    @ParameterizedTest
    @ValueSource(strings = { 
	    "FD", "FD_CHaR", "FD CHAR", "FD-CHAR",
	    "[]", "FD_CHAR[]", "FD_CHAR[1]", "FD_CHAR[:1]", "FD_CHAR[1:]", "FD_CHAR[:]", 
	    "FD_CHAR[0:0]", "FD_CHAR[0:1]", "FD_CHAR[1:0]"
    })
    void should_throw_ApplicationException_when_name_expression_mismatch(String expr) {
	
	assertThatExceptionOfType(ApplicationException.class)
		.isThrownBy(() -> MapFieldName.of(expr))
		.withMessageContaining(expr);
    }
    
    @ParameterizedTest
    @ValueSource(strings = "FD_CHAR[1:2]")
    void should_return_FieldName_when_name_expression(String expr) {
	
	var mapFieldName = MapFieldName.of(expr);
	
	assertThat(mapFieldName.name()).isEqualTo("FD_CHAR");
	
	assertThat(mapFieldName.offset()).isNotNull().satisfies(os -> {
	   assertThat(os.position()).isEqualTo(1);
	   assertThat(os.length()).isEqualTo(2);
	   assertThat(os.startAt()).isZero();
	   assertThat(os.endAt()).isEqualTo(2);
	   assertThat(os.toString()).startsWith("Offset [");
	});
    }
}
