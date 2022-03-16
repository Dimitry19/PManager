package cm.framework.ds.hibernate.utils;

import java.util.Map;

public interface IQueryBuilder<T extends IQueryBuilder> {
	//T setPage(PageBy var1);

	//PageBy getPage();

	T addOrderBy(String var1, String var2, Boolean var3);

	Map<String, Object> getParameters();

	T addParameter(String var1, Object var2);

	T like(String var1, String var2, String var3, boolean var4, boolean var5, boolean var6);

	T contains(String var1, String var2, String var3, boolean var4);

	T startWith(String var1, String var2, String var3, boolean var4);

	T endWith(String var1, String var2, String var3, boolean var4);

	T between(String var1, String var2, Object var3, Object var4);

	T betweenOptional(String var1, String var2, Object var3, Object var4, boolean var5, boolean var6);

	T lessThan(String var1, String var2, Object var3);

	T lessThanOrEqual(String var1, String var2, Object var3);

	T greaterThan(String var1, String var2, Object var3);

	T greaterThanOrEqual(String var1, String var2, Object var3);

	T equal(String var1, String var2, Object var3);

	T notEqual(String var1, String var2, Object var3);

	T in(String var1, String var2, Object var3);

	T notIn(String var1, String var2, Object var3);

	T isNull(String var1, String var2);

	T notNull(String var1, String var2);

	T openBracket();

	T closeBracket();

	T openSubWhere();

	T closeSubWhere();

	T addJoin(IQueryBuilder.JoinEnum var1, boolean var2, String var3, String var4, String var5);

	T and();

	T or();

	public static enum JoinEnum {
		LEFT,
		INNER,
		RIGHT;

		private JoinEnum() {
		}
	}
}