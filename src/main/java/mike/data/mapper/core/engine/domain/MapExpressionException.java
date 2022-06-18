package mike.data.mapper.core.engine.domain;

public class MapExpressionException extends RuntimeException {

    private static final long serialVersionUID = -6181158816164270362L;

    public MapExpressionException(String message, Object... args) {
	super(String.format(message, args));
    }
    
    public MapExpressionException(Throwable ex, String message, Object... args) {
	super(String.format(message, args), ex);
    }

}
