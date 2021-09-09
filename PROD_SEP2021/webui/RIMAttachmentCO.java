/*===========================================================================+
 |   Copyright (c) 2001, 2005 Oracle Corporation, Redwood Shores, CA, USA    |
 |                         All rights reserved.                              |
 +===========================================================================+
 |  HISTORY                                                                  |
 +===========================================================================*/
package xxup.oracle.apps.per.rim.webui;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.util.Dictionary;

import oracle.apps.fnd.common.VersionInfo;
import oracle.apps.fnd.framework.OAApplicationModule;
import oracle.apps.fnd.framework.OAException;
import oracle.apps.fnd.framework.webui.OAControllerImpl;
import oracle.apps.fnd.framework.webui.OAPageContext;
import oracle.apps.fnd.framework.webui.beans.OAWebBean;
import oracle.apps.fnd.framework.webui.beans.layout.OAAttachmentTableBean;
import oracle.apps.fnd.framework.webui.beans.message.OAMessageAttachmentLinkBean;

import xxup.oracle.apps.per.rim.server.RIMHelper;

/**
 * Controller for ...
 */
public class RIMAttachmentCO extends OAControllerImpl
{
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

    // String actionFromURL = pageContext.getParameter("urlParam");
    String transactionNo = pageContext.getParameter("pTransactionNo");
    String actionFromURL = pageContext.getParameter("urlParam");
    String itemKey = pageContext.getParameter("pItemKey");

    OAApplicationModule am = pageContext.getApplicationModule(webBean);
    Connection conn = pageContext.getApplicationModule(webBean).getOADBTransaction().getJdbcConnection();
    Serializable[] params = { transactionNo };
    am.invokeMethod("setAttachments", params);

    String strApprovalStatus = "";
    boolean isForClosing = false;
    try {
        
        String strQuery = 
            "SELECT action " +
            "FROM xxup_per_ps_action_history hist " +
            "WHERE item_key = '" + itemKey + "' " +
            "AND approver_no = (SELECT MIN(approver_no) " +
                                "FROM xxup_per_ps_action_history hist_1 " +
                                "WHERE action IN ('Pending' " +
                                               ",'For Accounting Info Update' " +
                                               ",'Prepare Financial Reports' " +
                                               ",'Validate Closeout Reports' " +
                                               ",'For Closing' " +
                                               ",'For Completion' " +
                                              ") " +
                                "AND hist_1.item_key = hist.item_key " +
                               ")";

        pageContext.writeDiagnostics(this, strQuery, 1);
        PreparedStatement stmt = conn.prepareStatement(strQuery);
        
        for (ResultSet resultset = stmt.executeQuery(); resultset.next(); 
        ) {
            strApprovalStatus = resultset.getString("action");
        }

        pageContext.writeDiagnostics(this, "strApprovalStatus: " + strApprovalStatus, 1);
        if(strApprovalStatus.equals("For Closing")){
          isForClosing = true;
        }

        
        
    } catch (Exception ex) {
        throw new OAException("Exception" + ex);
    }



    OAWebBean rootWB = pageContext.getRootWebBean();    
    OAMessageAttachmentLinkBean attachmentLink = (OAMessageAttachmentLinkBean)rootWB.findChildRecursive("RIMAttachment");
    if(RIMHelper.C_CREATE_AC.equals(actionFromURL) ||
     RIMHelper.C_UPDATE_AC.equals(actionFromURL) ||
     RIMHelper.C_CLOSE_AC.equals(actionFromURL) || 
     RIMHelper.C_SFL_AC.equals(actionFromURL) ||
     RIMHelper.C_ACCTG_AC.equals(actionFromURL) ||
     RIMHelper.C_RFC_AC.equals(actionFromURL) ||
     isForClosing || //attach by Accounting
     RIMHelper.C_CLOSED_AC.equals(actionFromURL) //attach by Research office


      ){
//          attachmentRN.setRendered(true);
       pageContext.writeDiagnostics(this, "att", 1);
      //Allow ADD attachment on Accounting
      if(RIMHelper.C_ACCTG_AC.equals(actionFromURL) ||
         RIMHelper.C_CLOSED_AC.equals(actionFromURL) ||
         isForClosing
        ) {
         

         if (attachmentLink != null) {
             Dictionary[] entityMaps = attachmentLink.getEntityMappings();
             // entityMaps[0].remove("insertAllowed");
             entityMaps[0].put("insertAllowed", true);
             entityMaps[0].put("updateAllowed", false);
             entityMaps[0].put("deleteAllowed", false);
         }
      }else {
          Dictionary[] entityMaps = attachmentLink.getEntityMappings();
          // entityMaps[0].remove("insertAllowed");
          entityMaps[0].put("insertAllowed", true);
          entityMaps[0].put("updateAllowed", true);
          entityMaps[0].put("deleteAllowed", true);
      }

    }else { //if not submission, view mode only
        if (attachmentLink != null) {
            pageContext.writeDiagnostics(this, "noatt", 1);
            Dictionary[] entityMaps = attachmentLink.getEntityMappings();
            // entityMaps[0].remove("insertAllowed");
            entityMaps[0].put("insertAllowed", false);
            entityMaps[0].put("updateAllowed", false);
            entityMaps[0].put("deleteAllowed", false);
        }
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
  }

}
