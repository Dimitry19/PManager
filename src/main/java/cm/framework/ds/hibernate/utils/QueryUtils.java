package cm.framework.ds.hibernate.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class QueryUtils {
	private static final Logger log = LoggerFactory.getLogger(QueryUtils.class);
	public static final String SPACE = " ";
	public static final String DOT = ".";
	public static final String LIKE = " like ";
	public static final String EQUAL = "=";
	public static final String NOT_EQUAL = "!=";
	public static final String BETWEEN = " between ";
	public static final String AND = " and ";
	public static final String OR = " or ";
	public static final String COLON = ":";
	public static final String COMMA = ",";
	public static final String LOW = "Low";
	public static final String HIGH = "High";
	public static final String LESS = "<";
	public static final String LESS_EQUAL = "<=";
	public static final String GREATER = ">";
	public static final String GREATER_EQUAL = ">=";
	public static final String IN = " in ";
	public static final String NOT_IN = " not in ";
	public static final String LEFT_BRACKET = " ( ";
	public static final String RIGHT_BRACKET = " ) ";
	public static final String IS_NULL = " is null ";
	public static final String NOT_NULL = " is not null ";
	public static final String WHERE = " where ";
	public static final String ORDER_BY = " order by ";
	public static final String LEFT_JOIN = " left join ";
	public static final String RIGHT_JOIN = " right join ";
	public static final String INNER_JOIN = " inner join ";
	public static final String FETCH = "fetch";
	public static final String ALIAS = " as ";
	public static final String ASC = "asc";
	public static final String DESC = "desc";

	public static QueryUtils getInstance() {
		return QueryUtils.LazyHolder.service;
	}

	public QueryUtils() {
	}

	public final Map<String, Object> like(StringBuilder query, String alias, String propertyName, String searchFor, boolean before, boolean after, boolean forCaseInsensitive) {
		return this.like(query, alias, propertyName, searchFor, before, after, forCaseInsensitive, QueryUtils.OperatorEnum.AND);
	}

	public final Map<String, Object> like(StringBuilder query, String alias, String propertyName, String searchFor, boolean before, boolean after, boolean forCaseInsensitive, QueryUtils.OperatorEnum condition) {
		Map<String, Object> parameters = new LinkedHashMap();
		this.checkAppendOperator(query, condition);
		MutableObject<String> parameterName = new MutableObject();
		this.appendProperty(query, alias, propertyName).append(" like ").append(this.appendParameter(propertyName, (Boolean)null, parameterName));
		parameters.put(parameterName.getValue(), SQLUtils.forLike(searchFor, before, after, forCaseInsensitive));
		if (log.isDebugEnabled()) {
			log.debug(query.toString());
		}

		return parameters;
	}

	public final Map<String, Object> between(StringBuilder query, String alias, String propertyName, Object lower, Object upper) {
		return this.between(query, alias, propertyName, lower, upper, QueryUtils.OperatorEnum.AND);
	}

	public final Map<String, Object> between(StringBuilder query, String alias, String propertyName, Object lower, Object upper, QueryUtils.OperatorEnum condition) {
		Map<String, Object> parameters = new LinkedHashMap();
		this.checkAppendOperator(query, condition);
		MutableObject<String> parameterNameLow = new MutableObject();
		MutableObject<String> parameterNameHigh = new MutableObject();
		this.appendProperty(query, alias, propertyName).append(" between ").append(" ").append(this.appendParameter(propertyName, true, parameterNameLow)).append(" and ").append(" ").append(this.appendParameter(propertyName, false, parameterNameHigh));
		parameters.put(parameterNameLow.getValue(), lower);
		parameters.put(parameterNameHigh.getValue(), upper);
		if (log.isDebugEnabled()) {
			log.debug(query.toString());
		}

		return parameters;
	}

	public final Map<String, Object> betweenOptional(StringBuilder query, String alias, String propertyName, Object lower, Object upper, boolean includeLower, boolean includeUpper) {
		return this.betweenOptional(query, alias, propertyName, lower, upper, includeLower, includeUpper, QueryUtils.OperatorEnum.AND);
	}

	public final Map<String, Object> betweenOptional(StringBuilder query, String alias, String propertyName, Object lower, Object upper, boolean includeLower, boolean includeUpper, QueryUtils.OperatorEnum condition) {
		Map<String, Object> parameters = new LinkedHashMap();
		if (lower == null && upper == null) {
			return parameters;
		} else {
			this.checkAppendOperator(query, condition);
			this.openBracket(query);
			MutableObject parameterName;
			if (lower != null) {
				parameterName = new MutableObject();
				this.appendProperty(query, alias, propertyName).append(includeLower ? ">=" : ">").append(this.appendParameter(propertyName, true, parameterName));
				parameters.put((String) parameterName.getValue(), lower);
			}

			if (upper != null) {
				if (lower != null) {
					this.checkAppendOperator(query, QueryUtils.OperatorEnum.AND);
				}

				parameterName = new MutableObject();
				this.appendProperty(query, alias, propertyName).append(includeUpper ? "<=" : "<").append(this.appendParameter(propertyName, false, parameterName));
				parameters.put((String) parameterName.getValue(), upper);
			}

			this.closeBracket(query);
			if (log.isDebugEnabled()) {
				log.debug(query.toString());
			}

			return parameters;
		}
	}

	public final Map<String, Object> less(StringBuilder query, String alias, String propertyName, Object value, boolean equal) {
		return this.less(query, alias, propertyName, value, equal, QueryUtils.OperatorEnum.AND);
	}

	public final Map<String, Object> less(StringBuilder query, String alias, String propertyName, Object value, boolean equal, QueryUtils.OperatorEnum condition) {
		Map<String, Object> parameters = new LinkedHashMap();
		this.checkAppendOperator(query, condition);
		MutableObject<String> parameterName = new MutableObject();
		this.appendProperty(query, alias, propertyName).append(equal ? "<=" : "<").append(this.appendParameter(propertyName, (Boolean)null, parameterName));
		parameters.put(parameterName.getValue(), value);
		if (log.isDebugEnabled()) {
			log.debug(query.toString());
		}

		return parameters;
	}

	public final Map<String, Object> greater(StringBuilder query, String alias, String propertyName, Object value, boolean equal) {
		return this.greater(query, alias, propertyName, value, equal, QueryUtils.OperatorEnum.AND);
	}

	public final Map<String, Object> greater(StringBuilder query, String alias, String propertyName, Object value, boolean equal, QueryUtils.OperatorEnum condition) {
		Map<String, Object> parameters = new LinkedHashMap();
		this.checkAppendOperator(query, condition);
		MutableObject<String> parameterName = new MutableObject();
		this.appendProperty(query, alias, propertyName).append(equal ? ">=" : ">").append(this.appendParameter(propertyName, (Boolean)null, parameterName));
		parameters.put(parameterName.getValue(), value);
		if (log.isDebugEnabled()) {
			log.debug(query.toString());
		}

		return parameters;
	}

	public final Map<String, Object> equal(StringBuilder query, String alias, String propertyName, Object value) {
		return this.equal(query, alias, propertyName, value, QueryUtils.OperatorEnum.AND);
	}

	public final Map<String, Object> equal(StringBuilder query, String alias, String propertyName, Object value, QueryUtils.OperatorEnum condition) {
		Map<String, Object> parameters = new LinkedHashMap();
		this.checkAppendOperator(query, condition);
		MutableObject<String> parameterName = new MutableObject();
		this.appendProperty(query, alias, propertyName).append("=").append(this.appendParameter(propertyName, (Boolean)null, parameterName));
		parameters.put(parameterName.getValue(), value);
		if (log.isDebugEnabled()) {
			log.debug(query.toString());
		}

		return parameters;
	}

	public final Map<String, Object> notEqual(StringBuilder query, String alias, String propertyName, Object value) {
		return this.notEqual(query, alias, propertyName, value, QueryUtils.OperatorEnum.AND);
	}

	public final Map<String, Object> notEqual(StringBuilder query, String alias, String propertyName, Object value, QueryUtils.OperatorEnum condition) {
		Map<String, Object> parameters = new LinkedHashMap();
		this.checkAppendOperator(query, condition);
		MutableObject<String> parameterName = new MutableObject();
		this.appendProperty(query, alias, propertyName).append("!=").append(this.appendParameter(propertyName, (Boolean)null, parameterName));
		parameters.put(parameterName.getValue(), value);
		if (log.isDebugEnabled()) {
			log.debug(query.toString());
		}

		return parameters;
	}

	public final Map<String, Object> in(StringBuilder query, String alias, String propertyName, Object value) {
		return this.in(query, alias, propertyName, value, QueryUtils.OperatorEnum.AND);
	}

	public final Map<String, Object> in(StringBuilder query, String alias, String propertyName, Object value, QueryUtils.OperatorEnum condition) {
		Map<String, Object> parameters = new LinkedHashMap();
		this.checkAppendOperator(query, condition);
		MutableObject<String> parameterName = new MutableObject();
		this.appendProperty(query, alias, propertyName).append(" in ").append(" ( ").append(this.appendParameter(propertyName, (Boolean)null, parameterName)).append(" ) ");
		parameters.put(parameterName.getValue(), value);
		if (log.isDebugEnabled()) {
			log.debug(query.toString());
		}

		return parameters;
	}

	public final Map<String, Object> notIn(StringBuilder query, String alias, String propertyName, Object value) {
		return this.notIn(query, alias, propertyName, value, QueryUtils.OperatorEnum.AND);
	}

	public final Map<String, Object> notIn(StringBuilder query, String alias, String propertyName, Object value, QueryUtils.OperatorEnum condition) {
		Map<String, Object> parameters = new LinkedHashMap();
		this.checkAppendOperator(query, condition);
		MutableObject<String> parameterName = new MutableObject();
		this.appendProperty(query, alias, propertyName).append(" not in ").append(" ( ").append(this.appendParameter(propertyName, (Boolean)null, parameterName)).append(" ) ");
		parameters.put(parameterName.getValue(), value);
		if (log.isDebugEnabled()) {
			log.debug(query.toString());
		}

		return parameters;
	}

	public final Map<String, Object> isNull(StringBuilder query, String alias, String propertyName) {
		return this.isNull(query, alias, propertyName, QueryUtils.OperatorEnum.AND);
	}

	public final Map<String, Object> isNull(StringBuilder query, String alias, String propertyName, QueryUtils.OperatorEnum condition) {
		Map<String, Object> parameters = new LinkedHashMap();
		this.checkAppendOperator(query, condition);
		this.appendProperty(query, alias, propertyName).append(" is null ");
		if (log.isDebugEnabled()) {
			log.debug(query.toString());
		}

		return parameters;
	}

	public final Map<String, Object> notNull(StringBuilder query, String alias, String propertyName) {
		return this.notNull(query, alias, propertyName, QueryUtils.OperatorEnum.AND);
	}

	public final Map<String, Object> notNull(StringBuilder query, String alias, String propertyName, QueryUtils.OperatorEnum condition) {
		Map<String, Object> parameters = new LinkedHashMap();
		this.checkAppendOperator(query, condition);
		this.appendProperty(query, alias, propertyName).append(" is not null ");
		if (log.isDebugEnabled()) {
			log.debug(query.toString());
		}

		return parameters;
	}

	public StringBuilder checkAppendAnd(StringBuilder query) {
		return this.checkAppendOperator(query, QueryUtils.OperatorEnum.AND);
	}

	public StringBuilder checkAppendOr(StringBuilder query) {
		return this.checkAppendOperator(query, QueryUtils.OperatorEnum.OR);
	}

	private StringBuilder checkAppendOperator(StringBuilder query, QueryUtils.OperatorEnum condition) {
		if (query.length() == 0) {
			return query;
		} else {
			switch(condition) {
				case AND:
					query.append(" and ");
					break;
				case OR:
					query.append(" or ");
			}

			return query;
		}
	}

	private StringBuilder appendProperty(StringBuilder query, String alias, String propertyName) {
		return query.append(alias).append(".").append(propertyName);
	}

	private StringBuilder appendParameter(String propertyName, Boolean isLower, MutableObject<String> parameterName) {
		StringBuilder parameter = new StringBuilder();
		parameterName.setValue(StringUtils.remove(propertyName, "."));
		parameter.append(":").append((String)parameterName.getValue());
		if (isLower == null) {
			return parameter;
		} else {
			parameterName.setValue((String)parameterName.getValue() + (isLower ? "Low" : "High"));
			return parameter.append(isLower ? "Low" : "High");
		}
	}

	public final StringBuilder openBracket(StringBuilder query) {
		return query.append(" ").append(" ( ").append(" ");
	}

	public final StringBuilder closeBracket(StringBuilder query) {
		return query.append(" ").append(" ) ").append(" ");
	}

	public final StringBuilder openSubWhere(StringBuilder query) {
		query.append(" ").append(" ( ").append(" ");
		return new StringBuilder();
	}

	public final StringBuilder closeSubWhere(StringBuilder query, StringBuilder subWhere) {
		return query.append(subWhere).append(" ").append(" ) ").append(" ");
	}

	public String composeQuery(StringBuilder select, StringBuilder from, StringBuilder where, StringBuilder orderBy) {
		StringBuilder query = new StringBuilder();
		if (StringUtils.isNotEmpty(select)) {
			query.append(select).append(" ").append(from);
		} else {
			query.append(from);
		}

		if (StringUtils.isNotEmpty(where)) {
			query.append(" where ").append(where);
		}

		if (StringUtils.isNotEmpty(orderBy)) {
			query.append(" order by ").append(orderBy);
		}

		return query.toString();
	}

	public final StringBuilder addJoin(StringBuilder query, QueryUtils.JoinEnum joinType, boolean isFetch, String entityAlias, String property, String alias) {
		switch(joinType) {
			case LEFT:
				query.append(" ").append(" left join ");
				break;
			case RIGHT:
				query.append(" ").append(" right join ");
				break;
			case INNER:
				query.append(" ").append(" inner join ");
		}

		if (isFetch) {
			query.append(" ").append("fetch");
		}

		query.append(" ").append(entityAlias).append(".").append(property);
		if (StringUtils.isNotEmpty(alias)) {
			query.append(" as ").append(alias);
		}

		return query;
	}

	private static class LazyHolder {
		static final QueryUtils service = new QueryUtils();

		private LazyHolder() {
		}
	}

	public static enum JoinEnum {
		LEFT,
		INNER,
		RIGHT;

		private JoinEnum() {
		}
	}

	public static enum OperatorEnum {
		AND,
		OR;

		private OperatorEnum() {
		}
	}
}
