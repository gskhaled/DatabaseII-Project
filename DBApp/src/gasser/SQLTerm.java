package gasser;

public class SQLTerm {
	public String tableName;
	public String colName;
	public String operator;
	public Object objValue;

	public SQLTerm(String tn, String cn, String op, Object ov) {
		this.tableName = tn;
		this.colName = cn;
		this.operator = op;
		this.objValue = ov;
	}
}
