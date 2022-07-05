package mike.data.mapper.core.engine.domain;

import java.util.List;
import java.util.Set;

import org.springframework.expression.Expression;

public class ValueExpression {

    private final String expression;
    
    private final List<Expression> expressions;
    private final List<String> preparedExpressions;
    private final Set<String> referencedFields;
    
    public static ValueExpression of(String expression) {
	ValueExpressionCompiler expressionCompiler = new ValueExpressionCompiler();
	List<Expression> expressions = expressionCompiler.compile(expression);
	return new ValueExpression(expression, expressions, 
		expressionCompiler.getPreparedExpressions(), expressionCompiler.getReferencedFields());
    }
    
    private ValueExpression(
	    String expression,
	    List<Expression> expressions, List<String> preparedExpressions, Set<String> referencedFields) {
	this.expression = expression;
	this.expressions = expressions;
	this.preparedExpressions = preparedExpressions;
	this.referencedFields = referencedFields;
    }
    
    public List<Expression> getExpressions() {
        return expressions;
    }

    public String getExpression() {
        return expression;
    }

    public List<String> getPreparedExpressions() {
        return preparedExpressions;
    }

    public Set<String> getReferencedFields() {
        return referencedFields;
    }

    @Override
    public String toString() {
	return String.format(
		"ValueExpression [expression=%s, preparedExpressions=%s, referencedFields=%s]",
		expression, preparedExpressions, referencedFields);
    }
}
