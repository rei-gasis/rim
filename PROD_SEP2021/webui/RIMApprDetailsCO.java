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
import oracle.apps.fnd.framework.webui.beans.layout.OAFlowLayoutBean;
import oracle.apps.fnd.framework.webui.beans.layout.OAHeaderBean;
import oracle.apps.fnd.framework.webui.beans.layout.OAHideShowHeaderBean;
import oracle.apps.fnd.framework.webui.beans.message.OAMessageAttachmentLinkBean;
import oracle.apps.fnd.framework.webui.beans.message.OAMessageLayoutBean;
import oracle.apps.fnd.framework.webui.beans.nav.OAButtonBean;
import oracle.apps.fnd.framework.webui.beans.nav.OAPageButtonBarBean;
import oracle.apps.fnd.wf.worklist.webui.NtfDetailsControlsCO;

import xxup.oracle.apps.per.rim.server.RIMHelper;

/**
 * Controller for ...
 */
public class RIMApprDetailsCO extends OAControllerImpl
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


    // String transactionNo = pageContext.getParameter("pTransactionNo");
   String pItemKey = pageContext.getParameter("pItemKey");
   String actionFromURL = pageContext.getParameter("urlParam");
   

    // pItemKey = "RIM-2061";

    OAApplicationModule am = pageContext.getApplicationModule(webBean);

    pageContext.writeDiagnostics(this, "ApprDetailsCO > Itemkey: " + pItemKey, 1); 
    Serializable[] reviewTranParams = { pItemKey };
    am.invokeMethod("reviewTran", reviewTranParams);
    
   /*Set Funding table*/
    OAWebBean rootWB = pageContext.getRootWebBean();
    OAHideShowHeaderBean fiscalShs = (OAHideShowHeaderBean)rootWB.findChildRecursive("FiscalInfoSHS");
    OAMessageLayoutBean revFundMLM = null;
    OAMessageLayoutBean fundMLM = null;
    if(fiscalShs != null){
      fundMLM = (OAMessageLayoutBean) fiscalShs.findChildRecursive("FundingMLM");
      revFundMLM = (OAMessageLayoutBean) fiscalShs.findChildRecursive("RevFundingMLM");
       
    }
    
    if(RIMHelper.C_ACCTG_AC.equals(actionFromURL)){
      revFundMLM.setRendered(Boolean.TRUE);
      fundMLM.setRendered(Boolean.FALSE);

    }else {
      revFundMLM.setRendered(Boolean.FALSE);
      fundMLM.setRendered(Boolean.TRUE);
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
