/*===========================================================================+
 |   Copyright (c) 2001, 2005 Oracle Corporation, Redwood Shores, CA, USA    |
 |                         All rights reserved.                              |
 +===========================================================================+
 |  HISTORY                                                                  |
 +===========================================================================*/
package xxup.oracle.apps.per.rim.webui;

import java.io.Serializable;

import java.util.Dictionary;

import oracle.apps.fnd.common.MessageToken;
import oracle.apps.fnd.common.VersionInfo;
import oracle.apps.fnd.framework.OAApplicationModule;
import oracle.apps.fnd.framework.OAException;
import oracle.apps.fnd.framework.OAViewObject;
import oracle.apps.fnd.framework.webui.OAControllerImpl;
import oracle.apps.fnd.framework.webui.OADialogPage;
import oracle.apps.fnd.framework.webui.OAPageContext;
import oracle.apps.fnd.framework.webui.OAWebBeanConstants;
import oracle.apps.fnd.framework.webui.TransactionUnitHelper;
import oracle.apps.fnd.framework.webui.beans.OAWebBean;
import oracle.apps.fnd.framework.webui.beans.form.OASubmitButtonBean;
import oracle.apps.fnd.framework.webui.beans.layout.OAFlowLayoutBean;
import oracle.apps.fnd.framework.webui.beans.layout.OAHeaderBean;
import oracle.apps.fnd.framework.webui.beans.layout.OAHideShowHeaderBean;
import oracle.apps.fnd.framework.webui.beans.layout.OASubTabLayoutBean;
import oracle.apps.fnd.framework.webui.beans.message.OAMessageAttachmentLinkBean;
import oracle.apps.fnd.framework.webui.beans.message.OAMessageLayoutBean;
import oracle.apps.fnd.framework.webui.beans.nav.OAButtonBean;
import oracle.apps.fnd.framework.webui.beans.nav.OAPageButtonBarBean;

import oracle.jbo.Row;

import xxup.oracle.apps.per.rim.server.RIMHelper;

/**
 * Controller for ...
 */
public class RIMReviewCO extends OAControllerImpl
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

    
    String transactionNo = pageContext.getParameter("ptransactionNo");
        String actionFromURL = pageContext.getParameter("urlParam");
        // String pItemKey = "";


    OAApplicationModule am = pageContext.getApplicationModule(webBean);
     
    
        /*Handle RFC
               * change button
               * */
        OAWebBean rootWB = pageContext.getRootWebBean();

        OAPageButtonBarBean pageBtnBar = 
            (OAPageButtonBarBean)rootWB.findChildRecursive("PageButtonRN");

        OASubmitButtonBean submitBtn = (OASubmitButtonBean)pageBtnBar.findChildRecursive("Submit");
        OASubmitButtonBean cancelBtn = (OASubmitButtonBean)pageBtnBar.findChildRecursive("Cancel");


        String urlParam = pageContext.getParameter("urlParam");

        if (RIMHelper.C_RFC_AC.equals(urlParam) || 
            RIMHelper.C_ACCTG_AC.equals(urlParam) 

            ) {
            submitBtn = (OASubmitButtonBean)pageBtnBar.findChildRecursive("Submit");
            submitBtn.setText("Update");
        } else if (RIMHelper.C_VIEW_AC.equals(urlParam)) {
            

            submitBtn.setRendered(false);
            cancelBtn.setRendered(false);

            OAHeaderBean histHdr = (OAHeaderBean) rootWB.findChildRecursive("ActionHistoryHRN");
            if(histHdr != null)
              histHdr.setRendered(Boolean.FALSE);
            
        }

        if(RIMHelper.C_CREATE_AC.equals(actionFromURL) ||
         RIMHelper.C_UPDATE_AC.equals(actionFromURL) ||
         RIMHelper.C_CLOSE_AC.equals(actionFromURL) || 
         RIMHelper.C_SFL_AC.equals(actionFromURL) 
          ){
          
          cancelBtn.setRendered(true);
        }else{
          cancelBtn.setRendered(false);
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

    //ignore actions from attachment functions
    if(("oaAddAttachment".equals(pageContext.getParameter(EVENT_PARAM))) ||
       ("oaGotoAttachments".equals(pageContext.getParameter(EVENT_PARAM))) ||
       ("oaUpdateAttachment".equals(pageContext.getParameter(EVENT_PARAM))) ||
       ("oaDeleteAttachment".equals(pageContext.getParameter(EVENT_PARAM))) ||
       ("oaViewAttachment".equals(pageContext.getParameter(EVENT_PARAM))) ||
       ("AddInlineAttachment".equals(pageContext.getParameter(EVENT_PARAM))) ||
       ("DeleteInlineAttachment".equals(pageContext.getParameter(EVENT_PARAM)))
      ){
        return;
    }

    OAApplicationModule am = (OAApplicationModule)pageContext.getApplicationModule(webBean);

    String transactionNo = pageContext.getParameter("pTransactionNo");
    String urlParam = pageContext.getParameter("urlParam");
    String pItemKey = pageContext.getParameter("pItemKey");
    String srcMenu = pageContext.getParameter("srcMenu");
    

    Serializable[] params = { transactionNo };

    
    if (pageContext.getParameter("Back") != null) {

        if ("VIEW".equals(urlParam)) {

            TransactionUnitHelper.endTransactionUnit(pageContext, 
                                                     "PSCreateTxn");

            pageContext.forwardImmediately("OA.jsp?page=/xxup/oracle/apps/per/rim/webui/RIMSummaryPG" +
                                           "&srcMenu=" + srcMenu,
                                           null, 
                                           OAWebBeanConstants.KEEP_MENU_CONTEXT, 
                                           null, null, false, 
                                           OAWebBeanConstants.ADD_BREAD_CRUMB_SAVE);
        

        } else {

            am.invokeMethod("returnNonMemberVO");

            TransactionUnitHelper.endTransactionUnit(pageContext, 
                                                     "PSCreateTxn");

            pageContext.forwardImmediately("OA.jsp?page=/xxup/oracle/apps/per/rim/webui/RIMRequestPG" + 
                                           "&ptransactionNo=" + transactionNo + 
                                           "&urlParam=" + urlParam +
                                           "&backUsed=" + "yes" +
                                           "&" + OASubTabLayoutBean.OA_SELECTED_SUBTAB_IDX + "=" + 0,
                                           null, 
                                           OAWebBeanConstants.KEEP_MENU_CONTEXT, 
                                           null, null, true, 
                                           OAWebBeanConstants.ADD_BREAD_CRUMB_SAVE);
        }

    } else if (pageContext.getParameter("DialogOk") != null) {

        if(RIMHelper.C_CREATE_AC.equals(urlParam) || 
           RIMHelper.C_UPDATE_AC.equals(urlParam) ||
           RIMHelper.C_CLOSE_AC.equals(urlParam) || 
           RIMHelper.C_SFL_AC.equals(urlParam)
          ){
          pageContext.forwardImmediately("OA.jsp?page=/xxup/oracle/apps/per/rim/webui/RIMSummaryPG", 
                                       null, 
                                       OAWebBeanConstants.KEEP_MENU_CONTEXT, 
                                       null, null, false, 
                                       OAWebBeanConstants.ADD_BREAD_CRUMB_NO);  
        }else{
          pageContext.forwardImmediately("OA.jsp?OAFunc=OANEWHOMEPAGE", 
                                       null, 
                                       OAWebBeanConstants.KEEP_MENU_CONTEXT, 
                                       null, null, false, 
                                       OAWebBeanConstants.ADD_BREAD_CRUMB_NO);  
        }
        

    } else if (urlParam.equals(RIMHelper.C_RFC_AC)) {
        
        OAViewObject vo = (OAViewObject)am.findViewObject("RIMHeaderEOVO1");
        // String pItemKey = pageContext.getParameter("pItemKey");
        Serializable[] reviewTranParams = { pItemKey };
        am.invokeMethod("reviewTran", reviewTranParams);
        
        
        vo.reset();
        Row row = vo.next();
//            Row row = vo.getCurrentRow();

        if(vo.getRowCount() == 1){
            String researchTitle = row.getAttribute("ResearchTitle").toString();

            MessageToken[] tokens = { new MessageToken("MODULE_TITLE", RIMHelper.C_MODULE_TITLE) 
                                     ,new MessageToken("APPLICATION_TITLE", researchTitle)  
            };

            OAException resubmitMessage = 
                new OAException("XXUP", "UP_HR_RESUBMIT_MSG", tokens, 
                                OAException.INFORMATION, null);
                
            OADialogPage dialogPage = 
            new OADialogPage(OAException.INFORMATION, resubmitMessage, 
                             null, "", null);


            dialogPage.setOkButtonToPost(true);
            dialogPage.setOkButtonLabel("Ok");
            dialogPage.setOkButtonItemName("DialogOk");

            dialogPage.setPostToCallingPage(true);

            Serializable[] resubmitParams = { pItemKey };
            am.invokeMethod("resubmitTran", resubmitParams);

            am.invokeMethod("commitTransaction");


            pageContext.redirectToDialogPage(dialogPage);
            
            
            
        }

        


        //OADialogPage dialogPage = new OADialogPage(()  


        //pageContext.forwardImmediately("OA.jsp?page=/oracle/apps/fnd/framework/navigate/webui/NewHomePG",
        
    } else if (pageContext.getParameter("Submit") != null) {
      if(urlParam.equals(RIMHelper.C_CREATE_AC) ||
         urlParam.equals(RIMHelper.C_UPDATE_AC) ||
         urlParam.equals(RIMHelper.C_CLOSE_AC) ||
         urlParam.equals(RIMHelper.C_SFL_AC)
      ) {
        OAViewObject vo = 
            (OAViewObject)am.findViewObject("RIMHeaderEOVO1");

        vo.reset();
        Row row = vo.next();  

        String pTransactionNo = row.getAttribute("TransactionNo").toString();
//        String pItemKey = row.getAttribute("ItemKey").toString();

        String wfProcess = "";
        String projectStatus = row.getAttribute("ProjectStatus").toString();

        pageContext.writeDiagnostics(this, "projectStatus: " + projectStatus, 1);
        //determine workflow process
        
        if(RIMHelper.C_CREATE_AC.equals(urlParam) || RIMHelper.C_SFL_AC.equals(urlParam)){
          wfProcess = RIMHelper.C_CREATE_AC;
        } else if(RIMHelper.C_CLOSE_AC.equals(urlParam)){
          am.invokeMethod("commitTransaction");

          // if(RIMHelper.C_CLOSEOUT_PS.equals(projectStatus)){
          wfProcess = RIMHelper.C_CLOSE_AC;
          // }
            
        } else if(RIMHelper.C_UPDATE_AC.equals(urlParam)) {
          am.invokeMethod("commitTransaction");
          wfProcess = RIMHelper.C_UPDATE_AC;
        }


        pageContext.writeDiagnostics(this, "wfProcess: " + wfProcess, 1);

        if(!"".equals(wfProcess)){
          Serializable [] initWFParams = { pTransactionNo, pItemKey, wfProcess };

          /*start workflow*/
          String resInitWf = (String)am.invokeMethod("initWF",initWFParams);


          if(!"Y".equals(resInitWf)){
            throw new OAException("Initiating workflow error occured!");
          }

          pageContext.writeDiagnostics(this, "WF Started", 1);

          String researchTitle = row.getAttribute("ResearchTitle").toString();
          MessageToken[] tokens = 
          { new MessageToken("APPLICATION_TITLE", researchTitle) };

          OAException confirmMessage = 
              new OAException("XXUP", "UP_HR_CREATE_APPL_CONF_MSG", tokens, 
                              OAException.INFORMATION, null);

          OADialogPage dialogPage = 
              new OADialogPage(OAException.INFORMATION, confirmMessage, null, 
                               "", null);


          dialogPage.setOkButtonToPost(true);
          dialogPage.setOkButtonLabel("Ok");
          dialogPage.setOkButtonItemName("DialogOk");

          dialogPage.setPostToCallingPage(true);


          pageContext.redirectToDialogPage(dialogPage);
        }
      } else if (RIMHelper.C_ACCTG_AC.equals(urlParam)) {
        
        OAViewObject vo = (OAViewObject)am.findViewObject("RIMHeaderEOVO1");
        pageContext.writeDiagnostics(this, "Acctg submit!", 1);

        vo.reset();
        Row row = vo.next();
//            Row row = vo.getCurrentRow();

        // if(vo.getRowCount() == 1){
            String researchTitle = row.getAttribute("ResearchTitle").toString();
            
            pageContext.writeDiagnostics(this, "researchTitle: " + researchTitle, 1);

            MessageToken[] tokens = { new MessageToken("MODULE_TITLE", RIMHelper.C_MODULE_TITLE) 
                                     ,new MessageToken("APPLICATION_TITLE", researchTitle)  
            };

            OAException updAcctgMessage = 
                new OAException("XXUP", "UP_RIM_UPD_ACCTG_MSG", tokens, 
                                OAException.INFORMATION, null);
                
            OADialogPage dialogPage = 
            new OADialogPage(OAException.INFORMATION, updAcctgMessage, 
                             null, "", null);


            dialogPage.setOkButtonToPost(true);
            dialogPage.setOkButtonLabel("Ok");
            dialogPage.setOkButtonItemName("DialogOk");

            dialogPage.setPostToCallingPage(true);

            Serializable[] updAcctgParams = { pItemKey };
            am.invokeMethod("updateAcctgInfo", updAcctgParams);

            am.invokeMethod("commitTransaction");

            pageContext.redirectToDialogPage(dialogPage);
            
      }else if(RIMHelper.C_FOR_CLSOUT_AC.equals(urlParam) ||
            RIMHelper.C_DON_FIN_REP_AC.equals(urlParam) ||
            RIMHelper.C_CLS_REP_VAL_AC.equals(urlParam) ||
            RIMHelper.C_CLOSED_AC.equals(urlParam) ||
            RIMHelper.C_COMPLETED_PS.equals(urlParam)
        ){

        OAViewObject vo = 
            (OAViewObject)am.findViewObject("RIMHeaderEOVO1");

        vo.reset();
        Row row = vo.next();  

        
        String strProjectStatus = row.getAttribute("ProjectStatus").toString();


        pageContext.writeDiagnostics(this, "throw dialog for proj status", 1);
        MessageToken[] tokens = 
        { new MessageToken("PROJ_STATUS", strProjectStatus) };

        pageContext.writeDiagnostics(this, "strProjectStatus: " + strProjectStatus, 1);
        OAException confirmMessage = 
            new OAException("XXUP", "UP_RIM_CLS_PROJ_STAT_MSG", tokens, 
                            OAException.INFORMATION, null);

        OADialogPage dialogPage = 
            new OADialogPage(OAException.INFORMATION, confirmMessage, null, 
                             "", null);


        dialogPage.setOkButtonToPost(true);
        dialogPage.setOkButtonLabel("Ok");
        dialogPage.setOkButtonItemName("DialogOk");

        dialogPage.setPostToCallingPage(true);

        Serializable[] updateParams = { pItemKey };
        am.invokeMethod("updateProjStatus", updateParams);

        am.invokeMethod("commitTransaction");

        
        pageContext.redirectToDialogPage(dialogPage); 
        
      }

    } else if (pageContext.getParameter("Cancel") != null) {

        pageContext.writeDiagnostics(this, "cancelling transaction", 1);
        
        Serializable [] delRecParams = { pItemKey };
        am.invokeMethod("delRecords", delRecParams);
        am.invokeMethod("rollbackTran", null);

        TransactionUnitHelper.endTransactionUnit(pageContext, 
                                                 "PSCreateTxn");


        pageContext.forwardImmediately("OA.jsp?page=/xxup/oracle/apps/per/rim/webui/RIMSummaryPG", 
                                       null, 
                                       OAWebBeanConstants.KEEP_MENU_CONTEXT, 
                                       null, null, false, 
                                       OAWebBeanConstants.ADD_BREAD_CRUMB_SAVE);
    }
  }

}
