package mike.test.data.mapper.core.mapping;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Pattern;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import mike.bootstrap.utilities.exceptions.ApplicationException;
import mike.bootstrap.utilities.helpers.StringValue;
import mike.bootstrap.utilities.helpers.Strings;
import mike.data.mapper.core.mapping.domain.FieldType;
import mike.data.mapper.core.mapping.domain.MapFieldType;

@DisplayName("MapFieldType")
class MapFieldTypeTest {

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = "   ")
    void should_throw_IllegalArgumentException_when_type_expression_null_or_empty(String expr) {
	
	assertThatIllegalArgumentException()
		.isThrownBy(() -> MapFieldType.of(expr))
		.withMessageContaining("FieldType:", expr);;
    }
    
    @ParameterizedTest
    @ValueSource(strings = { 
	    "FOO", "CHAR()", "CHAR(#-)",
	    "DATE()", "DATE(dd MM yyyy)", "DATE(aaMMdd)",
	    "INT()", "INT(#)", "INT($#)", "INT($-1)",
	    "NUM()", "NUM(a, $)", "NUM($)", "NUM(1, $a)",
	    "REAL()", "REAL(a, $)", "REAL($)", "REAL(1, $a)"
    })
    void should_throw_ApplicationException_when_type_expression_mismatch(String expr) {
	
	assertThatExceptionOfType(ApplicationException.class)
		.isThrownBy(() -> MapFieldType.of(expr))
		.withMessageContaining("FieldType:", expr);;
    }
    
    @ParameterizedTest
    @ValueSource(strings = { "CHAR", "CHAR($-)", "CHAR($)" })
    void should_return_CHAR_FieldType_when_type_expression(String expr) {
	
	var pattern = Pattern.compile("CHAR|\\(|\\)");
	var format = StringValue.of(expr).sanitize(pattern, Strings.EMPTY).blankAs("$-").value();

	var mapFieldType = MapFieldType.of(expr);
	
	assertThat(mapFieldType.format()).isEqualTo(format);
	assertThat(mapFieldType.type()).isEqualTo(FieldType.CHAR);
	assertThat(mapFieldType.type().getValueClass()).isSameAs(String.class);
    }
    
    @ParameterizedTest
    @ValueSource(strings = { "DATE", "DATE(yyyy-MM-dd)", "DATE(dd/MM/yy)" })
    void should_return_DATE_FieldType_when_type_expression(String expr) {
	var pattern = Pattern.compile("DATE|\\(|\\)");
	var format = StringValue.of(expr).sanitize(pattern, Strings.EMPTY).blankAs("yyyyMMdd").value();
	
	var mapFieldType = MapFieldType.of(expr);
	
	assertThat(mapFieldType.format()).isEqualTo(format);
	assertThat(mapFieldType.type()).isEqualTo(FieldType.DATE);
	assertThat(mapFieldType.type().getValueClass()).isSameAs(LocalDate.class);
    }
    
    @ParameterizedTest
    @ValueSource(strings = { "INT", "INT($)", "INT($0)", "INT($-)" })
    void should_return_INT_FieldType_when_type_expression(String expr) {
	var pattern = Pattern.compile("INT|\\(|\\)");
	var format = StringValue.of(expr).sanitize(pattern, Strings.EMPTY).blankAs("$").value();
	
	var mapFieldType = MapFieldType.of(expr);
	
	assertThat(mapFieldType.format()).isEqualTo(format);
	assertThat(mapFieldType.type()).isEqualTo(FieldType.INT);
	assertThat(mapFieldType.type().getValueClass()).isSameAs(Long.class);
    }
    
    @ParameterizedTest
    @ValueSource(strings = { 
	    "NUM(0)", "NUM(0, $)", "NUM(0, $+)", "NUM(0, $0)", "NUM(0, $+0)", "NUM(0, $-)", "NUM(0, $+-)" })
    void should_return_NUM_FieldType_when_type_expression(String expr) {
	var pattern = Pattern.compile("NUM|\\(|\\)");
	var options = StringValue.of(expr).sanitize(pattern, Strings.EMPTY).value().split(", ", 2);
	
	var scale = Strings.toInteger(options[0]);
	var format = options.length > 1 ? options[1] : "$";
	
	var mapFieldType = MapFieldType.of(expr);
	
	assertThat(mapFieldType.scale()).isEqualTo(scale);
	assertThat(mapFieldType.format()).isEqualTo(format);
	assertThat(mapFieldType.type()).isEqualTo(FieldType.NUM);
	assertThat(mapFieldType.type().getValueClass()).isSameAs(BigDecimal.class);
    }
    
    @ParameterizedTest
    @ValueSource(strings = { 
	    "REAL(3)", "REAL(3, $)", "REAL(3, $+)", "REAL(3, $0)", "REAL(3, $+0)", "REAL(3, $-)", "REAL(3, $+-)" })
    void should_return_REAL_FieldType_when_type_expression(String expr) {
	var pattern = Pattern.compile("REAL|\\(|\\)");
	var options = StringValue.of(expr).sanitize(pattern, Strings.EMPTY).value().split(", ", 2);
	
	var scale = Strings.toInteger(options[0]);
	var format = options.length > 1 ? options[1] : "$";
	
	var mapFieldType = MapFieldType.of(expr);
	
	assertThat(mapFieldType.scale()).isEqualTo(scale);
	assertThat(mapFieldType.format()).isEqualTo(format);
	assertThat(mapFieldType.type()).isEqualTo(FieldType.REAL);
	assertThat(mapFieldType.type().getValueClass()).isSameAs(BigDecimal.class);
    }
}
