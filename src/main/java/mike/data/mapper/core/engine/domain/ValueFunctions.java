package mike.data.mapper.core.engine.domain;

import org.apache.commons.lang3.StringUtils;

public class ValueFunctions {

    String fctTrim(String value) {
	return value.strip();
    }
    
    String fctRTrim(String value) {
	return value.stripTrailing();
    }
    
    String fctLTrim(String value) {
	return value.stripLeading();
    }
    
    String fctRPad(String value, int len, String padWith) {
	return StringUtils.rightPad(value, len, padWith);
    }
    
    String fctLPad(String value, int len, String padWith) {
	return StringUtils.leftPad(value, len, padWith);
    }
    
    String fctOffset(String value, int startAt, int endAt) {
	return StringUtils.substring(value, startAt, endAt);
    }
}
