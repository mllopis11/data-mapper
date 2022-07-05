package mike.data.mapper.core.mapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum FieldType {

    CHAR("(^\\((?<format>\\$-?)\\)$)?", String.class, "$-"),
    DATE("(^\\((?<format>[yMd/\\-]{1,10})\\)$)?", LocalDate.class, "yyyyMMdd"),
    INT("(^\\((?<format>\\$[\\-0]?)\\)$)?", Long.class, "$"),
    NUM("(^\\((?<scale>[0-9]|1[0-9])(,\\s?(?<format>\\$\\+?[\\-0]?))?\\)$)", BigDecimal.class, "$"),
    REAL("(^\\((?<scale>[0-9]|1[0-9])(,\\s?(?<format>\\$\\+?[\\-0]?))?\\)$)", BigDecimal.class, "$");
    
    private static final Logger log = LoggerFactory.getLogger(FieldType.class);
    
    private final Pattern optionPattern;
    private final Class<?> valueClass;
    private final String defaultFormat;
    
    private FieldType(String optionRegexp, Class<?> valueClass, String defaultFormat) {
	this.optionPattern = Pattern.compile(optionRegexp);
	this.valueClass = valueClass;
	this.defaultFormat = defaultFormat;
    }

    public Pattern getOptionPattern() {
        return optionPattern;
    }

    public Class<?> getValueClass() {
        return valueClass;
    }

    public String getDefaultFormat() {
        return defaultFormat;
    }
    
    public boolean hasScaleOption() {
        return optionPattern.pattern().contains("<scale>");
    }
    
    public Optional<Matcher> getOptionMatcher(String options) {
	return Optional.of(this.getOptionPattern().matcher(options)).filter(Matcher::matches);
    }
    
    public static Optional<FieldType> from(String type) {
	
	FieldType fieldType = null;
	
	try {
	    fieldType = FieldType.valueOf(type);
	} catch (IllegalArgumentException iae) {
	    log.trace("[ERROR] FieldType::from: type={} (reason: {})", type, iae.getMessage(), iae);
	}
	
	return Optional.ofNullable(fieldType);
    }
}
