package cm.framework.ds.hibernate.utils;


import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableObject;
import org.hibernate.criterion.CriteriaSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import cm.framework.ds.hibernate.utils.QueryUtils.OperatorEnum;


public class QueryBuilder implements IQueryBuilder<QueryBuilder> {

	private static final Logger log = LoggerFactory.getLogger(QueryBuilder.class);
	private String namedQuery;
	private String litteralQuery;
	private StringBuilder select = new StringBuilder();
	private StringBuilder from = new StringBuilder();
	private StringBuilder where = new StringBuilder();
	private StringBuilder orderBy = new StringBuilder();
	private Stack<StringBuilder> wheres = new Stack();
	private Map<String, Object> parameters = new LinkedHashMap();
	private boolean useCriteria = false;
	private boolean useCache = false;


	//private PageBy pageBy;


	private CriteriaSpecification criteria;

	public QueryBuilder() {
	}

	public QueryBuilder(String selectStr, String fromStr) {
		if (StringUtils.isNotEmpty(selectStr)) {
			this.select.append(selectStr);
		}

		if (StringUtils.isNotEmpty(fromStr)) {
			this.from.append(fromStr);
		}

	}

	public boolean isUseCriteria() {
		return this.useCriteria;
	}

	public QueryBuilder setUseCriteria(boolean useCriteria) {
		this.useCriteria = useCriteria;
		return this;
	}

	public boolean isUseCache() {
		return this.useCache;
	}

	public QueryBuilder setUseCache(boolean useCache) {
		this.useCache = useCache;
		return this;
	}


	public String getNamedQuery() {
		return this.namedQuery;
	}

	public QueryBuilder setNamedQuery(String namedQuery) {
		this.namedQuery = namedQuery;
		return this;
	}

	public StringBuilder getSelect() {
		return this.select;
	}

	public StringBuilder getFrom() {
		return this.from;
	}

	public StringBuilder getWhere() {
		return this.where;
	}

	public StringBuilder getOrderBy() {
		return this.orderBy;
	}

	public Map<String, Object> getParameters() {
		return this.parameters;
	}

	public QueryBuilder addParameter(String name, Object value) {
		this.parameters.put(name, value);
		return this;
	}



	public QueryBuilder contains(String alias, String propertyName, String value, boolean caseInsensitive) {
		return this.like(alias, propertyName, value, true, true, caseInsensitive);
	}

	public QueryBuilder startWith(String alias, String propertyName, String value, boolean caseInsensitive) {
		return this.like(alias, propertyName, value, false, true, caseInsensitive);
	}

	public QueryBuilder endWith(String alias, String propertyName, String value, boolean caseInsensitive) {
		return this.like(alias, propertyName, value, true, false, caseInsensitive);
	}

	public final QueryBuilder between(String alias, String propertyName, Object lower, Object upper) {
		this.checkAppendOperator((OperatorEnum)null);
		MutableObject<String> parameterNameLow = new MutableObject();
		MutableObject<String> parameterNameHigh = new MutableObject();
		this.appendProperty(this.where, alias, propertyName).append(" between ").append(" ").append(this.appendParameter(propertyName, true, parameterNameLow)).append(" and ").append(" ").append(this.appendParameter(propertyName, false, parameterNameHigh));
		this.parameters.put(parameterNameLow.getValue(), lower);
		this.parameters.put(parameterNameHigh.getValue(), upper);
		if (log.isDebugEnabled()) {
			log.debug(this.where.toString());
		}

		return this;
	}

	public QueryBuilder like(String alias, String propertyName, String searchFor, boolean before, boolean after, boolean caseInsensitive) {
		this.checkAppendOperator((OperatorEnum)null);
		MutableObject<String> parameterName = new MutableObject();
		if (caseInsensitive) {
			this.where.append("upper(");
		}

		this.appendProperty(this.where, alias, propertyName);
		if (caseInsensitive) {
			this.where.append(")");
		}

		this.where.append(" like ").append(this.appendParameter(propertyName, (Boolean)null, parameterName));
		this.parameters.put(parameterName.getValue(), SQLUtils.forLike(searchFor, caseInsensitive,before, after));
		if (log.isDebugEnabled()) {
			log.debug(this.where.toString());
		}

		return this;
	}

	public final QueryBuilder betweenOptional(String alias, String propertyName, Object lower, Object upper, boolean includeLower, boolean includeUpper) {
		if (lower == null && upper == null) {
			return this;
		} else {
			this.checkAppendOperator((OperatorEnum)null);
			this.openBracket();
			MutableObject parameterName;
			if (lower != null) {
				parameterName = new MutableObject();
				this.appendProperty(this.where, alias, propertyName).append(includeLower ? ">=" : ">").append(this.appendParameter(propertyName, true, parameterName));
				this.parameters.put((String) parameterName.getValue(), lower);
			}

			if (upper != null) {
				if (lower != null) {
					this.checkAppendOperator(OperatorEnum.AND);
				}

				parameterName = new MutableObject();
				this.appendProperty(this.where, alias, propertyName).append(includeUpper ? "<=" : "<").append(this.appendParameter(propertyName, false, parameterName));
				this.parameters.put((String) parameterName.getValue(), upper);
			}

			this.closeBracket();
			if (log.isDebugEnabled()) {
				log.debug(this.where.toString());
			}

			return this;
		}
	}

	public final QueryBuilder less(String alias, String propertyName, Object value, boolean equal) {

		commonCompare(alias, propertyName, value,equal, OperatorEnum.LSS);

		return this;
	}

	public final QueryBuilder lessThan(String alias, String propertyName, Object value) {
		return this.less(alias, propertyName, value, false);
	}

	public QueryBuilder lessThanOrEqual(String alias, String propertyName, Object value) {
		return this.less(alias, propertyName, value, true);
	}

	public final QueryBuilder greater(String alias, String propertyName, Object value, boolean equal) {

		commonCompare(alias, propertyName, value,equal, OperatorEnum.GTR);

		return this;
	}

	public QueryBuilder greaterThan(String alias, String propertyName, Object value) {
		return this.greater(alias, propertyName, value, false);
	}

	public QueryBuilder greaterThanOrEqual(String alias, String propertyName, Object value) {
		return this.greater(alias, propertyName, value, true);
	}

	public final QueryBuilder equal(String alias, String propertyName, Object value) {

		commonInEqual(alias, propertyName,value,OperatorEnum.NEQ);

		return this;
	}

	public final QueryBuilder notEqual(String alias, String propertyName, Object value) {

		commonInEqual(alias, propertyName,value,OperatorEnum.EQ);

		return this;
	}

	public final QueryBuilder in(String alias, String propertyName, Object value) {

		commonInEqual(alias, propertyName,value,OperatorEnum.IN);

		return this;
	}

	public final QueryBuilder notIn(String alias, String propertyName, Object value) {

		commonInEqual(alias, propertyName,value,OperatorEnum.NI);

		return this;
	}

	public final QueryBuilder isNull(String alias, String propertyName) {
		this.checkAppendOperator((OperatorEnum)null);
		this.appendProperty(this.where, alias, propertyName).append(" is null ");
		if (log.isDebugEnabled()) {
			log.debug(this.where.toString());
		}

		return this;
	}

	public final QueryBuilder notNull(String alias, String propertyName) {
		this.checkAppendOperator((QueryUtils.OperatorEnum)null);
		this.appendProperty(this.where, alias, propertyName).append(" is not null ");
		if (log.isDebugEnabled()) {
			log.debug(this.where.toString());
		}

		return this;
	}

	public QueryBuilder checkAppendAnd() {
		return this.checkAppendOperator(QueryUtils.OperatorEnum.AND);
	}

	public QueryBuilder checkAppendOr() {
		return this.checkAppendOperator(QueryUtils.OperatorEnum.OR);
	}

	private QueryBuilder checkAppendOperator(QueryUtils.OperatorEnum condition) {
		if (this.where.length() == 0) {
			return this;
		} else {
			String whereStr = this.where.toString();
			if (!whereStr.endsWith(" ( ") && !whereStr.endsWith(" and ") && !whereStr.endsWith(" or ")) {
				if (condition == null) {
					condition = QueryUtils.OperatorEnum.AND;
				}

				switch(condition) {
					case AND:
						this.where.append(" and ");
						break;
					case OR:
						this.where.append(" or ");
				}

				return this;
			} else {
				return this;
			}
		}
	}


	private void commonCompare(String alias, String propertyName, Object value, boolean equal, OperatorEnum operatorEnum){

		this.checkAppendOperator((OperatorEnum)null);
		MutableObject<String> parameterName = new MutableObject();

		switch (operatorEnum){

			case GTR:
				this.appendProperty(this.where, alias, propertyName).append(equal ? ">=" : ">").append(this.appendParameter(propertyName, (Boolean)null, parameterName));
			break;

			case LSS:
				this.appendProperty(this.where, alias, propertyName).append(equal ? "<=" : "<").append(this.appendParameter(propertyName, (Boolean)null, parameterName));

				break;
		}

		this.parameters.put(parameterName.getValue(), value);
		if (log.isDebugEnabled()) {
			log.debug(this.where.toString());
		}

	}



	private void  commonInEqual(String alias, String propertyName, Object value, OperatorEnum operatorEnum){

		this.checkAppendOperator((OperatorEnum)null);

		MutableObject<String> parameterName = new MutableObject();


		switch (operatorEnum){

			case NI:
				this.appendProperty(this.where, alias, propertyName)
						.append(" not in ")
						.append(" ( ")
						.append(this.appendParameter(propertyName, (Boolean)null, parameterName))
						.append(" ) ");

				break;

			case IN:
				this.appendProperty(this.where, alias, propertyName).append(" in ")
						.append(" ( ").append(this.appendParameter(propertyName, (Boolean)null, parameterName))
						.append(" ) ");
				break;

			case EQ:

				this.appendProperty(this.where, alias, propertyName).append("=")
						.append(this.appendParameter(propertyName, (Boolean)null, parameterName));

				break;

			case NEQ:
				this.appendProperty(this.where, alias, propertyName).append("!=")
						.append(this.appendParameter(propertyName, (Boolean)null, parameterName));

				break;

		}

		this.parameters.put(parameterName.getValue(), value);
		if (log.isDebugEnabled()) {
			log.debug(this.where.toString());
		}
	}

	private StringBuilder appendProperty(StringBuilder query, String alias, String propertyName) {
		return query.append(alias).append(".").append(propertyName);
	}

	private StringBuilder appendParameter(String propertyName, Boolean isLower, MutableObject<String> parameterName) {
		StringBuilder parameterNameTmp = new StringBuilder();
		parameterNameTmp.append(StringUtils.remove(propertyName, "."));
		if (isLower != null) {
			parameterNameTmp.append(isLower ? "Low" : "High");
		}

		if (this.parameters.containsKey(parameterNameTmp.toString())) {
			for(int i = 0; i < 1000; ++i) {
				String paramSuffix = String.valueOf(i);
				if (!this.parameters.containsKey(parameterNameTmp.toString() + paramSuffix)) {
					parameterNameTmp.append(paramSuffix);
					break;
				}
			}
		}

		parameterName.setValue(parameterNameTmp.toString());
		StringBuilder condition = new StringBuilder();
		return condition.append(":").append((String)parameterName.getValue());
	}

	public final QueryBuilder openBracket() {
		this.where.append(" ( ");
		return this;
	}

	public final QueryBuilder closeBracket() {
		this.where.append(" ) ");
		return this;
	}

	public final QueryBuilder openSubWhere() {
		this.checkAppendOperator((OperatorEnum)null);
		this.where.append(" ( ");
		this.wheres.push(this.where);
		this.where = new StringBuilder();
		return this;
	}

	public final QueryBuilder closeSubWhere() {
		this.where = ((StringBuilder)this.wheres.pop()).append(this.where).append(" ) ");
		return this;
	}

	public String createLitteralQuery() {
		if (StringUtils.isNotEmpty(this.getLitteralQuery())) {
			return this.getLitteralQuery();
		} else {
			StringBuilder query = new StringBuilder();
			if (StringUtils.isNotEmpty(this.select)) {
				query.append(this.select).append(" ").append(this.from);
			} else {
				query.append(this.from);
			}

			if (StringUtils.isNotEmpty(this.where)) {
				query.append(" where ").append(this.where);
			}

			if (StringUtils.isNotEmpty(this.orderBy)) {
				query.append(" order by ").append(this.orderBy);
			}

			if (log.isDebugEnabled()) {
				log.debug("Query:{}", query);
				Iterator i$ = this.parameters.entrySet().iterator();

				while(i$.hasNext()) {
					Map.Entry<String, Object> entry = (Map.Entry)i$.next();
					log.debug("Parametro:{}-{}", entry.getKey(), entry.getValue());
				}
			}

			return query.toString();
		}
	}

	public <T> T createQuery() {
		throw new RuntimeException("not yet implemented");
	}

	public <T> T createCriteria() {
		throw new RuntimeException("not yet implemented");
	}

	public final QueryBuilder addJoin(JoinEnum joinType, boolean isFetch, String entityAlias, String property, String alias) {
		switch(joinType) {
			case LEFT:
				this.from.append(" ").append(" left join ");
				break;
			case RIGHT:
				this.from.append(" ").append(" right join ");
				break;
			case INNER:
				this.from.append(" ").append(" inner join ");
		}

		if (isFetch) {
			this.from.append(" ").append("fetch");
		}

		this.from.append(" ").append(entityAlias).append(".").append(property);
		if (StringUtils.isNotEmpty(alias)) {
			this.from.append(" as ").append(alias);
		}

		return this;
	}

	public final QueryBuilder addOrderBy(String entityAlias, String property, Boolean asc) {
		if (this.orderBy.length() > 0) {
			this.orderBy.append(",");
		}

		this.orderBy.append(entityAlias).append(".").append(property).append(" ").append(BooleanUtils.isFalse(asc) ? "desc" : "asc");
		return this;
	}


	public String getLitteralQuery() {
		return this.litteralQuery;
	}

	public QueryBuilder setLiteralQuery(String litteralQuery) {
		this.litteralQuery = litteralQuery;
		return this;
	}

	public CriteriaSpecification getCriteria() {
		return this.criteria;
	}

	public QueryBuilder setCriteria(CriteriaSpecification criteria) {
		this.criteria = criteria;
		this.setUseCriteria(true);
		return this;
	}

	/*public HQLBuilder setPage(PageBy pageBy) {
		this.pageBy = pageBy;
		return this;
	}

	public PageBy getPage() {
		return this.pageBy;
	}*/
	public QueryBuilder and() {
		this.checkAppendAnd();
		return this;
	}

	public QueryBuilder or() {
		this.checkAppendOr();
		return this;
	}
}

