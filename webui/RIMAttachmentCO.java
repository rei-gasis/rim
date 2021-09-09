/*===========================================================================+
 |   Copyright (c) 2001, 2005 Oracle Corporation, Redwood Shores, CA, USA    |
 |                         All rights reserved.                              |
 +===========================================================================+
 |  HISTORY                                                                  |
 +===========================================================================*/
package xxup.oracle.apps.per.rim.webui;

import java.io.Serializable;

import java.util.Dictionary;

import oracle.apps.fnd.common.VersionInfo;
import oracle.apps.fnd.framework.OAApplicationModule;
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

    OAApplicationModule am = pageContext.getApplicationModule(webBean);
    Serializable[] params = { transactionNo };
    am.invokeMethod("setAttachments", params);
        // }


    OAWebBean rootWB = pageContext.getRootWebBean();    
    OAMessageAttachmentLinkBean attachmentLink = (OAMessageAttachmentLinkBean)rootWB.findChildRecursive("RIMAttachment");
    if(RIMHelper.C_CREATE_AC.equals(actionFromURL) ||
     RIMHelper.C_UPDATE_AC.equals(actionFromURL) ||
     RIMHelper.C_CLOSE_AC.equals(actionFromURL) || 
     RIMHelper.C_SFL_AC.equals(actionFromURL) ||
     RIMHelper.C_ACCTG_AC.equals(actionFromURL) ||
     RIMHelper.C_RFC_AC.equals(actionFromURL)
      ){
//          attachmentRN.setRendered(true);
       System.out.println("att");
      //Allow ADD attachment on Accounting
      if(RIMHelper.C_ACCTG_AC.equals(actionFromURL)) {
         

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
            System.out.println("noatt");
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
