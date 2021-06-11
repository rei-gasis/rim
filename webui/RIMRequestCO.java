/*===========================================================================+
 |   Copyright (c) 2001, 2005 Oracle Corporation, Redwood Shores, CA, USA    |
 |                         All rights reserved.                              |
 +===========================================================================+
 |  HISTORY                                                                  |
 +===========================================================================*/
package xxup.oracle.apps.per.rim.webui;

import java.io.Serializable;

import com.sun.java.util.collections.ArrayList;
import com.sun.java.util.collections.List;

import oracle.apps.fnd.common.MessageToken;
import oracle.apps.fnd.common.VersionInfo;
import oracle.apps.fnd.framework.OAApplicationModule;
import oracle.apps.fnd.framework.OAException;
import oracle.apps.fnd.framework.OAViewObject;
import oracle.apps.fnd.framework.webui.OAControllerImpl;
import oracle.apps.fnd.framework.webui.OAPageContext;
import oracle.apps.fnd.framework.webui.OAWebBeanConstants;
import oracle.apps.fnd.framework.webui.TransactionUnitHelper;
import oracle.apps.fnd.framework.webui.beans.OAWebBean;

import oracle.jbo.Row;

import xxup.oracle.apps.per.rim.server.RIMHelper;

/**
 * Controller for ...
 */
public class RIMRequestCO extends OAControllerImpl
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

    String backUsed = pageContext.getParameter("backUsed");
    String actionFromURL = pageContext.getParameter("urlParam");
    String transactionNo = pageContext.getParameter("pTransactionNo");
    String itemKey = pageContext.getParameter("pItemKey");

    
    pageContext.writeDiagnostics(this, "RequestCO > itemKey" + itemKey, 1);

    OAApplicationModule am = (OAApplicationModule)pageContext.getApplicationModule(webBean);

      if("yes".equals(backUsed)){
//          OAViewObject projImpVO = (OAViewObject) am.findViewObject("RIMProjImpactEOVO1");
//            String test = "";
//          if (projImpVO != null){
//            if(projImpVO.getRowCount() >= 1){
//              Serializable [] projImpParams = { test };
//              am.invokeMethod("LoadExistProjImpactInTable", projImpParams);
//            }
//
//          }


          return; 
      }


      if (RIMHelper.C_CREATE_AC.equals(actionFromURL) || actionFromURL == null) {
           am.invokeMethod("initVOForNewRequest");

           // System.out.println("initVOForNewRequest");

      } else if (RIMHelper.C_UPDATE_AC.equals(actionFromURL) || RIMHelper.C_CLOSE_AC.equals(actionFromURL)) {
            // System.out.println("Entered update"); 


            Serializable[] initTranRecParams = { itemKey };
            am.invokeMethod("initTranRec", initTranRecParams);


        } else if(RIMHelper.C_RFC_AC.equals(actionFromURL) || 
                  RIMHelper.C_ACCTG_AC.equals(actionFromURL) ||
                  RIMHelper.C_FOR_CLSOUT_AC.equals(actionFromURL) ||
                  RIMHelper.C_FINAL_FIN_REP_PS.equals(actionFromURL) ||
                  RIMHelper.C_CLS_REP_VAL_AC.equals(actionFromURL) ||
                  RIMHelper.C_CLOSED_AC.equals(actionFromURL) ||
                  RIMHelper.C_COMPLETED_AC.equals(actionFromURL) 
                  
                  ){
            System.out.println("updaterec"); 
            String pItemKey = pageContext.getParameter("pItemKey");

            Serializable[] updateRecParams = { pItemKey };
            am.invokeMethod("updateRec", updateRecParams);
            // am.invokeMethod("initExistPubVO");            

        } 
        // else if(RIMHelper.C_ACCTG_AC.equals(actionFromURL)
        //   )

        // {
            

        
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
    // String actionParam = pageContext.getParameter("urlParam");
    String error = "";
    String actionFromURL = pageContext.getParameter("urlParam");
    List errMsg = new ArrayList();


    // if (pageContext.getParameter("Save") != null) {
    //   System.out.println("you entered Save");
    //   return;
    // }


    if (pageContext.getParameter("Next") != null) {

        String strTransactionNo = "";
        String strProjectName = "";
        

        /*Validate basic Info*/
        OAViewObject mainVo = (OAViewObject)am.findViewObject("RIMHeaderEOVO1");
        Row row = mainVo.getCurrentRow();
        if (mainVo != null) {
            
            if (row.getAttribute("TransactionNo") != null) {
                strTransactionNo = row.getAttribute("TransactionNo").toString();
            }

           if (row.getAttribute("AssignmentId") == null) {
               errMsg.add(new OAException("Assignment Position is required", 
                                          OAException.ERROR));
           }


           if (row.getAttribute("ResearchType") == null) {
               errMsg.add(new OAException("Type of Research is required", 
                                          OAException.ERROR));
           }

           if(row.getAttribute("ResearchType") != null ){
            if ("Others".equals(row.getAttribute("ResearchType").toString()) && row.getAttribute("ResearchTypeOthers") == null) {
                 errMsg.add(new OAException("You have selected Type of Research (Others), please specify your input.", 
                                            OAException.ERROR));
             } 
           }
           

           if (row.getAttribute("ResearchTitle") == null) {
               errMsg.add(new OAException("Research Title is required", 
                                          OAException.ERROR));
           }

           if (row.getAttribute("MainAreaInterest") == null) {
               errMsg.add(new OAException("Main Area of Interest is required", 
                                          OAException.ERROR));
           }

           if (row.getAttribute("ProjectImpactDesc") == null) {
               errMsg.add(new OAException("Project Impact Description is required", 
                                          OAException.ERROR));
           }

           if (row.getAttribute("ProjectLeaderId") == null) {
               errMsg.add(new OAException("Project Leader is required", 
                                          OAException.ERROR));
           }

           if (row.getAttribute("StartDate") == null) {
               errMsg.add(new OAException("MOA / Contract Start date is required", 
                                          OAException.ERROR));
           }

           if (row.getAttribute("EndDate") == null) {
               errMsg.add(new OAException("MOA / Contract End date is required", 
                                          OAException.ERROR));
           }


           if (row.getAttribute("ProjectStatus") == null) {
               errMsg.add(new OAException("Project Status is required", 
                                          OAException.ERROR));
           }


           /*
            * Project Impact
           */

           /*Validate if subject area of interest is empty*/
            OAViewObject projImpVO = 
                (OAViewObject)am.findViewObject("RIMProjImpactEOVO1");


            Row selectedRows[] = null;

            if (projImpVO != null) {
                selectedRows = projImpVO.getFilteredRows("Selected", "Y");
            }

            if (selectedRows.length <= 0) {
                errMsg.add(new OAException("Please select at least one Project Impact", 
                                           OAException.ERROR));
            }

           
            /*
             * Team members check
             * */
            OAViewObject membersVo = 
                (OAViewObject)am.findViewObject("RIMTeamMembersEOVO1");

            int memberCount = 0;
            if (membersVo != null) {
                for (Row rowi: membersVo.getAllRowsInRange()) {
                    if (rowi.getAttribute("FullName") != null) {
                        memberCount++;
                    }
                }
            }

            if (memberCount <= 0) {
                errMsg.add(new OAException("Please fill at least one Team Member", 
                                           OAException.ERROR));
            }

            
          }

          /***
          * Fiscal Info validations
          ***/

         OAViewObject fiscVO = (OAViewObject)am.findViewObject("RIMFiscalDetailsEOVO1");
         if (fiscVO != null) {
            System.out.println("fisc found");
            Row fiscRow = fiscVO.getCurrentRow();

            if(fiscRow != null){

              
              if (fiscRow.getAttribute("ConstituentUnit") == null) {
                   errMsg.add(new OAException("Constituent Unit is required", 
                                              OAException.ERROR));
               }

               if (fiscRow.getAttribute("ResponsibilityCenter") == null) {
                   errMsg.add(new OAException("Responsibility Center is required", 
                                              OAException.ERROR));
               }
            }

            

             // if (fiscRow.getAttribute("FundingAgency") == null) {
             //     errMsg.add(new OAException("Funding  Center is required", 
             //                                OAException.ERROR));
             // }
          }



          OAViewObject mileVO = (OAViewObject)am.findViewObject("RIMMilestonesEOVO1");
         if (mileVO != null) {

            Row mileRow = mileVO.first();

            if (mileRow.getAttribute("Milestone") == null) {
                 errMsg.add(new OAException("Milestone is required", 
                                            OAException.ERROR));
             }

             if (mileRow.getAttribute("CompletionPercentage") == null) {
                 errMsg.add(new OAException("Milestone % Completion is required", 
                                            OAException.ERROR));
             }
          }

          
          /**
          * Trigger Validations
          **/
          if (errMsg.size() >= 1) {
              MessageToken[] tokens = 
              { new MessageToken("ERROR_MSG", error) };
              OAException.raiseBundledOAException((List)errMsg);

              throw new OAException("XXUP", "UP_HR_PS_FORM_VALID_MSG", 
                                    tokens, OAException.ERROR, null);
          }


          
          String itemKey = "";
          try {

              if(RIMHelper.C_CREATE_AC.equals(actionFromURL) ||
                 RIMHelper.C_UPDATE_AC.equals(actionFromURL) ||
                 RIMHelper.C_CLOSE_AC.equals(actionFromURL) 
              ){
                String assignmentId = "";              
                String transactionNo = "";

                Serializable[] initApproversParams = new String[3];
                row = null;


                if (mainVo != null) {

                    mainVo.reset();
                    row = mainVo.next();


                    //Item key will be initialize on creation of approver list
                    //Only be created if not yet initialized
                    if (row.getAttribute("ItemKey") == null) {
                        if (row.getAttribute("AssignmentId") != null) {
                            assignmentId = 
                                    row.getAttribute("AssignmentId").toString();
                            transactionNo = 
                                    row.getAttribute("TransactionNo").toString();
                            initApproversParams[0] = assignmentId;
                            initApproversParams[1] = transactionNo;
                            initApproversParams[2] = actionFromURL;

                            // pageContext.writeDiagnostics(this, "assignmentId:" + assignmentId, 1);
                            // pageContext.writeDiagnostics(this, "TransactionNo:" + row.getAttribute("TransactionNo").toString(), 1);
                        }

                        

                        itemKey = 
                                (String)am.invokeMethod("initApprovers", initApproversParams);

                        pageContext.writeDiagnostics(this, "assignmentId: " + assignmentId, 1);
                        pageContext.writeDiagnostics(this, "transactionNo: " + transactionNo, 1);
                        // pageContext.writeDiagnostics(this, "itemKey: " + itemKey, 1);

                        row.setAttribute("ItemKey", itemKey);
                        System.out.println("initApprovers");

                        

                    } else {
                        if (mainVo != null) {
                            row = mainVo.getCurrentRow();

                            if (row.getAttribute("ItemKey") != null) {
                                itemKey = row.getAttribute("ItemKey").toString();
                            }

                        }

                    }
                }

              }else { //not in CREATE, UPDATE, CLOSE

                itemKey = row.getAttribute("ItemKey").toString();

              }

              pageContext.writeDiagnostics(this, "ItemKey: " + itemKey, 1);


          } catch (Exception ex) {
              throw new OAException("Unable to set approver list: " + 
                                    ex);
          }

          //fix for sp code disp on ACCTG_UPD tran    
            OAViewObject tfiscVO = (OAViewObject)am.findViewObject("RIMFiscalDetailsEOVO1");
            if(tfiscVO != null){
              
              Row fRow = fiscVO.getCurrentRow();

              if(fRow != null){
                

              
                String strSpCodeDisp = "";
                String strFundControllerDisp = "";
                if(fRow.getAttribute("SpCodeDisp")!= null){
                  strSpCodeDisp = fRow.getAttribute("SpCodeDisp").toString();  
                }

                if(fRow.getAttribute("FundControllerDisp") != null){
                  strFundControllerDisp = fRow.getAttribute("FundControllerDisp").toString();
                }
                if(!"".equals(strSpCodeDisp) || !"".equals(strFundControllerDisp)){

                  Serializable []storeAcctParams = { itemKey, strSpCodeDisp, strFundControllerDisp };
                   System.out.println("storeAcctgInfo");
                }

              }

              
            }

          try{
            row.setAttribute("ApprovalStatus", "For Approval");
            am.invokeMethod("saveDetails"); //store child tables  

            if(RIMHelper.C_CREATE_AC.equals(actionFromURL) || 
               RIMHelper.C_UPDATE_AC.equals(actionFromURL) ||
               RIMHelper.C_CLOSE_AC.equals(actionFromURL) 
              ){
              System.out.println("commitTransaction");
              am.invokeMethod("commitTransaction");
              
            } 
            
          }catch(Exception ex){
            throw new OAException("Error saving records: " + ex);
          }
          

          // pageContext.writeDiagnostics(this, "forwardedIK:" + itemKey, 1);


          pageContext.forwardImmediately("OA.jsp?page=/xxup/oracle/apps/per/rim/webui/RIMReviewPG&pTransactionNo=" + 
                                               strTransactionNo + "&urlParam=" + 
                                               actionFromURL + "&pItemKey=" + 
                                               itemKey, null, 
                                               OAWebBeanConstants.KEEP_MENU_CONTEXT, 
                                               null, null, true, 
                                               OAWebBeanConstants.ADD_BREAD_CRUMB_SAVE);



        } else if (pageContext.getParameter("Cancel") != null) {

            TransactionUnitHelper.endTransactionUnit(pageContext, 
                                                     "PSCreateTxn");

            pageContext.forwardImmediately("OA.jsp?page=/xxup/oracle/apps/per/rim/webui/RIMSummaryPG", 
                                           null, 
                                           OAWebBeanConstants.KEEP_MENU_CONTEXT, 
                                           null, null, false, 
                                           OAWebBeanConstants.ADD_BREAD_CRUMB_SAVE);
        }
        
    
  }

//  public String initApprovers(){
    
//  }

}
