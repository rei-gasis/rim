package xxup.oracle.apps.per.rim.webui;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.ResultSet;

//import java.util.Date;
import java.util.Map;

import oracle.jbo.domain.Date;
import oracle.apps.fnd.common.VersionInfo;
import oracle.apps.fnd.framework.OAApplicationModule;
import oracle.apps.fnd.framework.OAException;
import oracle.apps.fnd.framework.OAViewObject;
import oracle.apps.fnd.framework.server.OAViewRowImpl;
import oracle.apps.fnd.framework.webui.OAControllerImpl;
import oracle.apps.fnd.framework.webui.OAPageContext;
import oracle.apps.fnd.framework.webui.OAWebBeanConstants;
import oracle.apps.fnd.framework.webui.beans.OAWebBean;

import oracle.jbo.Row;

import xxup.oracle.apps.per.rim.server.RIMHelper;

public class RIMFiscalInfoSubCO extends OAControllerImpl {
    
    public static final String RCS_ID="$Header$";
    public static final boolean RCS_ID_RECORDED =
          VersionInfo.recordClassVersion(RCS_ID, "%packagename%");

    /**
     * Layout and page setup logic for a region.
     * @param pageContext the current OA page context
     * @param webBean the web bean corresponding to the region
     */
    public void processRequest(OAPageContext pageContext, OAWebBean webBean)
    {
      super.processRequest(pageContext, webBean);

      OAApplicationModule am = 
            (OAApplicationModule)pageContext.getApplicationModule(webBean);

      String urlParam = pageContext.getParameter("urlParam");
      OAViewObject fiscVO = (OAViewObject) am.findViewObject("RIMFiscalDetailsEOVO1");
      Row fiscRow = null;
      if(fiscVO != null){
          fiscVO.reset();
          fiscRow = fiscVO.next();
      }

      if(RIMHelper.C_ACCTG_AC.equals(urlParam)){
        

        
        
          if(fiscRow != null){
            fiscRow.setAttribute("DisableAccountingMode", false);  
          }else{
            fiscRow.setAttribute("DisableAccountingMode", true);  
          }
          
        

        
      }else {
        fiscRow.setAttribute("DisableAccountingMode", true);  
      }

      
    }

    /**
     * Procedure to handle form submissions for form elements in
     * a region.
     * @param pageContext the current OA page context
     * @param webBean the web bean corresponding to the region
     */
    public void processFormRequest(OAPageContext pageContext, OAWebBean webBean)
    {
      super.processFormRequest(pageContext, webBean);
      OAApplicationModule am = (OAApplicationModule)pageContext.getApplicationModule(webBean);


      OAViewObject fiscVO = (OAViewObject)am.findViewObject("RIMFiscalDetailsEOVO1");
      OAViewObject fundVO = (OAViewObject)am.findViewObject("RIMFundingEOVO1");


      // if (pageContext.isLovEvent()) {

          
      //     String lovInputId = pageContext.getLovInputSourceId();
      //     Row row = fiscVO.getCurrentRow();

      //     if ("ResponsibilityCenterDisp".equals(lovInputId)) {
      //       String respDisp = row.getAttribute("ResponsibilityCenterDisp").toString();
      //       String resp = row.getAttribute("ResponsibilityCenter").toString();

      //       pageContext.writeDiagnostics(this, "respDisp " + respDisp, 1);
      //       pageContext.writeDiagnostics(this, "resp " + resp, 1);
      //     }

      //     if ("SpCodeDisp".equals(lovInputId)) {
      //       String SpCodeDisp = row.getAttribute("SpCodeDisp").toString();
      //       String SpCode = row.getAttribute("SpCode").toString();

      //       pageContext.writeDiagnostics(this, "SpCodeDisp " + SpCodeDisp, 1);
      //       pageContext.writeDiagnostics(this, "SpCode " + SpCode, 1);
      //     }


      //     // if ("FundControllerDisp".equals(lovInputId)) {
      //     //   String FundControllerDisp = row.getAttribute("FundControllerDisp").toString();
      //     //   String FundControllerId = row.getAttribute("FundControllerId").toString();

      //     //   pageContext.writeDiagnostics(this, "FundControllerDisp " + FundControllerDisp, 1);
      //     //   pageContext.writeDiagnostics(this, "FundControllerId " + FundControllerId, 1);
      //     // }
      // }

     


      if ("EnableFundingAgency".equals(pageContext.getParameter(OAWebBeanConstants.EVENT_PARAM))) {

            Row fiscRow = fiscVO.getCurrentRow();
            // Row fundRow = fundVO.getCurrentRow(); 

            // String eventRowSourceParam = 
            //     pageContext.getParameter(EVENT_SOURCE_ROW_REFERENCE);
            // Row fundRow = am.findRowByRef(eventRowSourceParam);
            fundVO.reset();
            while(fundVO.hasNext()){
              OAViewRowImpl fundRow = (OAViewRowImpl) fundVO.next();

              String rowVal = fundRow.getAttribute("FundingSource") != null ? fundRow.getAttribute("FundingSource").toString() : "";
              pageContext.writeDiagnostics(this, "rowVal: " + rowVal, 1);
              if ("Trust Fund".equals(rowVal)) {
                  fiscRow.setAttribute("DisableFundingAgency", false);
                  return;
              } else {
                  fiscRow.setAttribute("DisableFundingAgency", true);
              }
            }
        } else if("CalculateTotalAmount".equals(pageContext.getParameter(OAWebBeanConstants.EVENT_PARAM))) {
          Row row = fiscVO.getCurrentRow();
          Map<String, String> mFund = (Map<String, String>) am.invokeMethod("totalFundAmount"); 


          row.setAttribute("TotalAmount", mFund.get("non-formatted"));
          row.setAttribute("FmTotalAmount", mFund.get("formatted"));
        }
    }
}
