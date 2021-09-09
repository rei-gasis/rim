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
import oracle.apps.fnd.framework.server.OAViewRowImpl;
import oracle.apps.fnd.framework.webui.OAControllerImpl;
import oracle.apps.fnd.framework.webui.OAPageContext;
import oracle.apps.fnd.framework.webui.OAWebBeanConstants;
import oracle.apps.fnd.framework.webui.TransactionUnitHelper;
import oracle.apps.fnd.framework.webui.beans.OAWebBean;

import oracle.apps.fnd.framework.webui.beans.form.OASubmitButtonBean;
import oracle.apps.fnd.framework.webui.beans.layout.OAHeaderBean;
import oracle.apps.fnd.framework.webui.beans.nav.OALinkBean;
import oracle.apps.fnd.framework.webui.beans.nav.OAPageButtonBarBean;

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
    String urlParam = pageContext.getParameter("urlParam");

    
    OAWebBean rootWB = pageContext.getRootWebBean();

    OAPageButtonBarBean pageBtnBar = 
        (OAPageButtonBarBean)rootWB.findChildRecursive("PageButtonRN");
        
    if(pageBtnBar != null){
        OASubmitButtonBean saveBtn = (OASubmitButtonBean)pageBtnBar.findChildRecursive("Save");
        OASubmitButtonBean cancelBtn = (OASubmitButtonBean)pageBtnBar.findChildRecursive("Cancel");        
        
        if(RIMHelper.C_CREATE_AC.equals(actionFromURL) ||
         RIMHelper.C_UPDATE_AC.equals(actionFromURL) ||
         RIMHelper.C_CLOSE_AC.equals(actionFromURL) || 
         RIMHelper.C_SFL_AC.equals(actionFromURL) ||
         actionFromURL == null
          ){
            cancelBtn.setRendered(true);
        } else {
            cancelBtn.setRendered(false);
        }

        if(RIMHelper.C_CREATE_AC.equals(actionFromURL) ||
          RIMHelper.C_SFL_AC.equals(actionFromURL)
          ) {
            saveBtn.setRendered(true);
        } else {
            saveBtn.setRendered(false);
        }


    }

//    /*Include attachment tab on viewing*/
   
    

    OAApplicationModule am = (OAApplicationModule)pageContext.getApplicationModule(webBean);

      if("yes".equals(backUsed)){
          am.invokeMethod("LoadExistMainAreaIntInTable");
          am.invokeMethod("LoadExistProjImpactInTable");
          am.invokeMethod("LoadExistDevGoalInTable");

          return; 
      }

      


      if (RIMHelper.C_CREATE_AC.equals(actionFromURL) || actionFromURL == null) {
           am.invokeMethod("initVOForNewRequest");

           // pageContext.writeDiagnostics(this, "initVOForNewRequest", 1);

      } else if (RIMHelper.C_UPDATE_AC.equals(actionFromURL) || RIMHelper.C_CLOSE_AC.equals(actionFromURL)) {
            // pageContext.writeDiagnostics(this, "Entered update", 1); 


            Serializable[] initTranRecParams = { itemKey };
            am.invokeMethod("initTranRec", initTranRecParams);


        } else if(RIMHelper.C_RFC_AC.equals(actionFromURL) || 
                  RIMHelper.C_ACCTG_AC.equals(actionFromURL) ||
                  RIMHelper.C_FOR_CLSOUT_AC.equals(actionFromURL) ||
                  RIMHelper.C_DON_FIN_REP_AC.equals(actionFromURL) ||
                  RIMHelper.C_CLS_REP_VAL_AC.equals(actionFromURL) ||
                  RIMHelper.C_CLOSED_AC.equals(actionFromURL) ||
                  RIMHelper.C_COMPLETED_AC.equals(actionFromURL) ||
                  RIMHelper.C_SFL_AC.equals(actionFromURL)
                  ){
            pageContext.writeDiagnostics(this, "updaterec", 1); 
            String pItemKey = pageContext.getParameter("pItemKey");

            Serializable[] updateRecParams = { pItemKey, actionFromURL };
            am.invokeMethod("updateRec", updateRecParams);
            // am.invokeMethod("initExistPubVO");            

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
    // String actionParam = pageContext.getParameter("urlParam");
    String error = "";
    String actionFromURL = pageContext.getParameter("urlParam");
    List errMsg = new ArrayList();


    // if (pageContext.getParameter("Save") != null) {
    //   pageContext.writeDiagnostics(this, "you entered Save", 1);
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

           if (row.getAttribute("BriefDescription") == null) {
               errMsg.add(new OAException("Brief Description is required", 
                                          OAException.ERROR));
           }

           // if (row.getAttribute("MainAreaInterest") == null) {
           //     errMsg.add(new OAException("Main Area of Interest is required", 
           //                                OAException.ERROR));
           // }

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

           /*Validate extension*/
          String validateExtension = (String) am.invokeMethod("validateExtension");
          pageContext.writeDiagnostics(this, "validateExtension: " + validateExtension, 1);
           if("EXT".equals(validateExtension) || 
              "OLD".equals(validateExtension) || 
              "NEW".equals(validateExtension) ||
              actionFromURL.equals(RIMHelper.C_RFC_AC)
              )
           {
            
           }else if(!"".equals(validateExtension)){ // do not validate RFC
                errMsg.add(new OAException(validateExtension, 
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
             * Non-UP Team members check
             * */
            OAViewObject membersVo = 
                (OAViewObject)am.findViewObject("RIMOtherTeamMembersEOVO1");

            int memberCount = 0;
            if (membersVo != null) {
                for (Row rowi: membersVo.getAllRowsInRange()) {
                    if (rowi.getAttribute("FullName") != null) {
                        memberCount++;
                    }
                }
            }

            if (memberCount <= 0) {
                errMsg.add(new OAException("Please fill at least one Non-UP Team Member", 
                                           OAException.ERROR));
            }

            /*Validate if Main Area Interest  is empty*/
            OAViewObject intVO = 
                (OAViewObject)am.findViewObject("RIMMainAreaIntEOVO1");


            selectedRows = null;

            if (intVO != null) {
                selectedRows = intVO.getFilteredRows("Selected", "Y");
            }

            if (selectedRows.length <= 0) {
                errMsg.add(new OAException("Please select at least one Main Area of Interest", 
                                           OAException.ERROR));
            }


            /*Validate if Main Area Interest  is empty*/
            OAViewObject goalVO = 
                (OAViewObject)am.findViewObject("RIMDevGoalEOVO1");


            selectedRows = null;

            if (goalVO != null) {
                selectedRows = goalVO.getFilteredRows("Selected", "Y");
            }

            if (selectedRows.length <= 0) {
                errMsg.add(new OAException("Please select at least one Sustainable Development Goal", 
                                           OAException.ERROR));
            }

            
          }

          /***
          * Fiscal Info validations
          ***/

         OAViewObject fiscVO = (OAViewObject)am.findViewObject("RIMFiscalDetailsEOVO1");
         if (fiscVO != null) {
            pageContext.writeDiagnostics(this, "fisc found", 1);
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

               if (fiscRow.getAttribute("FundingAgency") == null) {
                   errMsg.add(new OAException("Funding Agency is required", 
                                              OAException.ERROR));
               }
            }

          }


          /*Validate consistency of currency in Funding*/
          OAViewObject fundVO = (OAViewObject)am.findViewObject("RIMFundingEOVO1");

          if(fundVO != null) {
            fundVO.reset();
            String prevCurrency = "";
            int itr = 1;
            while(fundVO.hasNext()){
              
              Row fundRow = (OAViewRowImpl) fundVO.next();
              String currency = fundRow.getAttribute("Currency") != null ? fundRow.getAttribute("Currency").toString() : "";

              //skip first iteration due to no comparison
              if(itr == 1){
                itr++;
                prevCurrency = currency;
                continue;
              }

              if(!prevCurrency.equals(currency)){
                pageContext.writeDiagnostics(this, "inconsistent", 1);
                errMsg.add(new OAException("Currencies on Funding source is inconsistent", 
                                                OAException.ERROR));
                break;
              }

              prevCurrency = currency;
              itr++;

            }
          }

          /*Validate if funding source is entered by Accounting */
           if(RIMHelper.C_ACCTG_AC.equals(actionFromURL)){
              if(fundVO != null) {
                fundVO.reset();
                int fundCtr = 0;
                while(fundVO.hasNext()){
                  Row fundRow = fundVO.next();
                  if (fundRow.getAttribute("Description") != null) {                  
                    fundCtr++;
                  }
                }

                if (fundCtr == 0) {
                     errMsg.add(new OAException("Funding source is required", 
                                                OAException.ERROR));
                  }
                
                
              }
           }



          OAViewObject mileVO = (OAViewObject)am.findViewObject("RIMMilestonesEOVO1");
          if(mileVO != null) {
            mileVO.reset();
            int mileCtr = 0;
            int percCtr = 0;
            while(mileVO.hasNext()){
              Row mileRow = mileVO.next();
              if (mileRow.getAttribute("Milestone") != null) {
                mileCtr++;
              }

              if (mileRow.getAttribute("CompletionPercentage") != null) {
                percCtr++;
              }
            }

            if (mileCtr == 0) {
               errMsg.add(new OAException("Milestone is required", 
                                          OAException.ERROR));
            }


            if (percCtr == 0) {
               errMsg.add(new OAException("Milestone % Completion is required", 
                                          OAException.ERROR));
            } 
          }

         // if (mileVO != null) {

         //    Row mileRow = mileVO.first();

         //    if (mileRow.getAttribute("Milestone") == null) {
         //         errMsg.add(new OAException("Milestone is required", 
         //                                    OAException.ERROR));
         //     }

         //     if (mileRow.getAttribute("CompletionPercentage") == null) {
         //         errMsg.add(new OAException("Milestone % Completion is required", 
         //                                    OAException.ERROR));
         //     }
         //  }

          
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
                 RIMHelper.C_CLOSE_AC.equals(actionFromURL)  ||
                 RIMHelper.C_SFL_AC.equals(actionFromURL)
              ){
                String assignmentId = "";              
                String constituentUnit = "";              
                String transactionNo = "";

                Serializable[] initApproversParams = new String[4];
                row = null;
                Row fiscRow = null;


                if (mainVo != null) {

                    mainVo.reset();
                    row = mainVo.next();


                    //Item key will be initialize on creation of approver list
                    //Only be created if not yet initialized
                    if (row.getAttribute("ItemKey") == null || 
                        (RIMHelper.C_SFL_AC.equals(actionFromURL) && row.getAttribute("ItemKey") != null)
                        ) {

                        fiscVO = (OAViewObject) am.findViewObject("RIMFiscalDetailsEOVO1");
                        

                        if(fiscVO != null){
                          fiscVO.reset();
                          fiscRow = fiscVO.next();
                          
                          constituentUnit = fiscRow.getAttribute("ConstituentUnit") != null ? fiscRow.getAttribute("ConstituentUnit").toString() : "";

                        }
                        

                        if (!constituentUnit.equals("") && row.getAttribute("AssignmentId") != null) {
                            assignmentId = row.getAttribute("AssignmentId").toString();
                            transactionNo = row.getAttribute("TransactionNo").toString();
                            initApproversParams[0] = assignmentId;
                            initApproversParams[1] = constituentUnit;
                            initApproversParams[2] = transactionNo;
                            initApproversParams[3] = actionFromURL;

                            // pageContext.writeDiagnostics(this, "assignmentId:" + assignmentId, 1);
                            // pageContext.writeDiagnostics(this, "TransactionNo:" + row.getAttribute("TransactionNo").toString(), 1);
                        }

                        

                        itemKey = 
                                  (String)am.invokeMethod("initApprovers", initApproversParams);

                        pageContext.writeDiagnostics(this, "assignmentId: " + assignmentId, 1);
                        pageContext.writeDiagnostics(this, "constituentUnit: " + constituentUnit, 1);
                        pageContext.writeDiagnostics(this, "transactionNo: " + transactionNo, 1);
                        // pageContext.writeDiagnostics(this, "itemKey: " + itemKey, 1);

                        row.setAttribute("ItemKey", itemKey);
                        pageContext.writeDiagnostics(this, "itemKey" + itemKey, 1);
                        pageContext.writeDiagnostics(this, "initApprovers", 1);

                        

                    } else {
                        if (mainVo != null) {
                            row = mainVo.getCurrentRow();

                            if (row.getAttribute("ItemKey") != null) {
                                itemKey = row.getAttribute("ItemKey").toString();
                            }

                        }

                    }
                }

              // } else if(RIMHelper.C_SFL_AC.equals(actionFromURL)) {
              //     if(mainVo != null) {
              //       mainVo.reset();
              //       row = mainVo.next();
              //       String assignmentId = row.getAttribute("AssignmentId") != null ? row.getAttribute("AssignmentId").toString() : "";
              //       String transactionNo = row.getAttribute("TransactionNo") != null ?  row.getAttribute("TransactionNo").toString() : "";

              //       if(!assignmentId.equals("")){
              //         am.invokeMethod("initApprovers", new Serializable[]{ assignmentId, transactionNo, actionFromURL });
              //       }
              //     }
              } else { //not in CREATE, UPDATE, CLOSE

                itemKey = row.getAttribute("ItemKey").toString();

              }


          } catch (Exception ex) {
              throw new OAException("Unable to set approver list: " + 
                                    ex);
          }

          //fix for sp code disp on ACCTG_UPD tran    
          if(actionFromURL.equals(RIMHelper.C_ACCTG_AC)){
            OAViewObject tfiscVO = (OAViewObject)am.findViewObject("RIMFiscalDetailsEOVO1");
            if(tfiscVO != null){
              
              Row fRow = fiscVO.getCurrentRow();

              if(fRow != null){
                

              
                String strSpCodeDisp = "";
                String strFundControllerDisp = "";
                String fundingData = "";
                if(fRow.getAttribute("SpCodeDisp")!= null){
                  strSpCodeDisp = fRow.getAttribute("SpCodeDisp").toString();  
                }

                if(fRow.getAttribute("FundControllerDisp") != null){
                  strFundControllerDisp = fRow.getAttribute("FundControllerDisp").toString();
                }

                //Funding Source
                if(fundVO != null) {
                  fundVO.reset();
                  StringBuilder sb = new StringBuilder();

                  //reset
                  am.invokeMethod("resetFundAcctgInfo", new Serializable[]{ itemKey });
                  

                  while(fundVO.hasNext()){
                      Row fundRow = (OAViewRowImpl) fundVO.next();

                      String strDescription = fundRow.getAttribute("Description") != null ? fundRow.getAttribute("Description").toString() : "";
                      if(!strDescription.equals("")){
                          sb.setLength(0);

                          String strFundingSource = fundRow.getAttribute("FundingSource") != null ? fundRow.getAttribute("FundingSource").toString() : "xxx";      
                          String strStartDate = fundRow.getAttribute("StartDate") != null ? fundRow.getAttribute("StartDate").toString() : "xxx";
                          pageContext.writeDiagnostics(this, "strStartDate: " + strStartDate, 1);
                          String strEndDate = fundRow.getAttribute("EndDate") != null ? fundRow.getAttribute("EndDate").toString() : "xxx";
                          String strAmount = fundRow.getAttribute("Amount") != null ? fundRow.getAttribute("Amount").toString() : "xxx";
                          String strCurrency = fundRow.getAttribute("Currency") != null ? fundRow.getAttribute("Currency").toString() : "xxx";

                          pageContext.writeDiagnostics(this, "strDescription: " + strDescription, 1);

                          sb.append(strFundingSource + ";");
                          sb.append(strDescription + ";");
                          sb.append(strStartDate + ";");
                          sb.append(strEndDate + ";");
                          sb.append(strAmount + ";");
                          sb.append(strCurrency + ";");

                          Serializable []storeFundAcctParams = { itemKey, sb.toString()};
                          am.invokeMethod("storeFundAcctgInfo", storeFundAcctParams);
                          pageContext.writeDiagnostics(this, "storeFund" + sb.toString(), 1);
                      }
                  }
                  

                }


                if(!"".equals(strSpCodeDisp) || !"".equals(strFundControllerDisp)){

                  Serializable []storeAcctParams = { itemKey, strSpCodeDisp, strFundControllerDisp};
                  am.invokeMethod("storeAcctgInfo", storeAcctParams);
                   pageContext.writeDiagnostics(this, "storeAcctgInfo", 1);
                }

              }
            }
          }
            

          try{
            //set approval status to handle end date extension
            

            am.invokeMethod("saveDetails", new Serializable[]{ actionFromURL }); //store child tables  

            if(RIMHelper.C_CREATE_AC.equals(actionFromURL) || 
               RIMHelper.C_UPDATE_AC.equals(actionFromURL) ||
               RIMHelper.C_CLOSE_AC.equals(actionFromURL) || 
               RIMHelper.C_SFL_AC.equals(actionFromURL) || 
               RIMHelper.C_RFC_AC.equals(actionFromURL) ||
               RIMHelper.C_CLOSED_AC.equals(actionFromURL)
              ){

              
              
              pageContext.writeDiagnostics(this, "commitTransaction", 1);
              am.invokeMethod("commitTransaction");


              
            } 
            
          }catch(Exception ex){
            throw new OAException("Error saving records: " + ex);
          }
          

          pageContext.writeDiagnostics(this, "forwardedIK:" + itemKey, 1);


          pageContext.forwardImmediately("OA.jsp?page=/xxup/oracle/apps/per/rim/webui/RIMReviewPG&pTransactionNo=" + 
                                               strTransactionNo + "&urlParam=" + 
                                               actionFromURL + "&pItemKey=" + 
                                               itemKey, null, 
                                               OAWebBeanConstants.KEEP_MENU_CONTEXT, 
                                               null, null, true, 
                                               OAWebBeanConstants.ADD_BREAD_CRUMB_SAVE);



        } else if (pageContext.getParameter("Cancel") != null) {
          
          if(RIMHelper.C_CREATE_AC.equals(actionFromURL) ||
             RIMHelper.C_UPDATE_AC.equals(actionFromURL) ||
             RIMHelper.C_CLOSE_AC.equals(actionFromURL) || 
             RIMHelper.C_SFL_AC.equals(actionFromURL) ||
             actionFromURL == null
              ) {

              pageContext.writeDiagnostics(this, "cancelling transaction", 1);
        
              // Serializable [] delRecParams = { pItemKey };
              // am.invokeMethod("delRecords", delRecParams);
              am.invokeMethod("rollbackTran", null);
          
          }

          

            TransactionUnitHelper.endTransactionUnit(pageContext, 
                                                     "PSCreateTxn");

            pageContext.forwardImmediately("OA.jsp?page=/xxup/oracle/apps/per/rim/webui/RIMSummaryPG", 
                                           null, 
                                           OAWebBeanConstants.KEEP_MENU_CONTEXT, 
                                           null, null, false, 
                                           OAWebBeanConstants.ADD_BREAD_CRUMB_SAVE);
        } else if (pageContext.getParameter("Save") != null) {

          OAViewObject mainVo = (OAViewObject)am.findViewObject("RIMHeaderEOVO1");
          Row row = mainVo.getCurrentRow();
          String strTransactionNo = "";
          if (mainVo != null) {
            // pageContext.writeDiagnostics(this, "SFL", 1);

            if (row.getAttribute("TransactionNo") != null) {
                strTransactionNo = row.getAttribute("TransactionNo").toString();
            }

            row.setAttribute("ApprovalStatus", "Saved For Later");
            row.setAttribute("ItemKey", "RIM-" + strTransactionNo); //reference in summary PG

            am.invokeMethod("saveDetails", new Serializable[]{ RIMHelper.C_SFL_AC });
            am.invokeMethod("commitTransaction");
          }
          

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
