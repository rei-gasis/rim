package xxup.oracle.apps.per.rim.server;

import oracle.apps.fnd.framework.server.OAViewObjectImpl;
import oracle.jbo.Row;

public class RIMVOParent extends OAViewObjectImpl {
    public RIMVOParent() {
    }
    
    public void initNewRecord() {
         
        setMaxFetchSize(0);
        executeQuery();
        Row row = createRow();
        insertRow(row);
        row.setNewRowState(Row.STATUS_INITIALIZED);

    }

    
//    public void initTranPS(String itemKey) {
//        setWhereClauseParams(null);
//        setWhereClause("item_key = :1");
//        setWhereClauseParam(0, itemKey);
//        executeQuery();
//    }

    public void initExist(String item_key) {
        setWhereClauseParams(null);
        setWhereClause("item_key = :1");
        setWhereClauseParam(0, item_key);
        executeQuery();
    }
}
