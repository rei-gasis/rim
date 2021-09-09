/*===========================================================================+
 |   Copyright (c) 2001, 2005 Oracle Corporation, Redwood Shores, CA, USA    |
 |                         All rights reserved.                              |
 +===========================================================================+
 |  HISTORY                                                                  |
 +===========================================================================*/
package xxup.oracle.apps.per.rim.webui;

import java.sql.Connection;

import oracle.apps.fnd.common.VersionInfo;
import oracle.apps.fnd.framework.OAApplicationModule;
import oracle.apps.fnd.framework.webui.OAControllerImpl;
import oracle.apps.fnd.framework.webui.OAPageContext;
import oracle.apps.fnd.framework.webui.beans.OAWebBean;

import xxup.oracle.apps.per.rim.server.RIMHelper;

/**
 * Controller for ...
 */
public class RIMPubInfoSubCO extends OAControllerImpl
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

//    pageContext.writeDiagnostics(this, "Pub RN", 1);
//
//    OAApplicationModule am = 
//            (OAApplicationModule)pageContext.getApplicationModule(webBean);
//
//      String actionFromURL = pageContext.getParameter("urlParam");
////      pageContext.writeDiagnostics(this, "actionFromURL: " + actionFromURL, 1);
//  
//
//      if(!RIMHelper.C_CREATE_AC.equals(actionFromURL)){
//          am.invokeMethod("initExistPubVO");
//      }
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
