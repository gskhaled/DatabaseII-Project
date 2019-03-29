package team_1;

public class SQLTerm {
	public String _strTableName;
	public String _strColName;
	public String _strOperator;
	public Object _objValue;

	public SQLTerm(String tn, String cn, String op, Object ov) {
		this._strTableName = tn;
		this._strColName = cn;
		this._strOperator = op;
		this._objValue = ov;
	}

	public SQLTerm() {

	}
}
