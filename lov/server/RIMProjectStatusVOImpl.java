package xxup.oracle.apps.per.rim.lov.server;

import oracle.apps.fnd.framework.server.OAViewObjectImpl;

import xxup.oracle.apps.per.rim.server.RIMHelper;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class RIMProjectStatusVOImpl extends OAViewObjectImpl {
    /**This is the default constructor (do not remove)
     */
    public RIMProjectStatusVOImpl() {
    }

    public void limitProjectStatus(String pProcess){
      // setMaxFetchSize(0);
     setWhereClause(null);
     setWhereClause("1<>2");

     System.out.println("pProcess");
     System.out.println("project_status IN ('" + RIMHelper.C_EXEC_PS + "')");
     // if(RIMHelper.C_CREATE_AC.equals(pProcess)){
     // 	   pageContext.writeDiagnostics(this, RIMHelper.C_EXEC_PS, 1);
     //     setWhereClause("project_status IN ('" + RIMHelper.C_EXEC_PS + "')");
     // }
  
    executeQuery(); 
  }
}
