package xxup.oracle.apps.per.rim.webui;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.ResultSet;

//import java.util.Date;
import oracle.jbo.domain.Date;
import oracle.apps.fnd.common.VersionInfo;
import oracle.apps.fnd.framework.OAApplicationModule;
import oracle.apps.fnd.framework.OAException;
import oracle.apps.fnd.framework.OAViewObject;
import oracle.apps.fnd.framework.webui.OAControllerImpl;
import oracle.apps.fnd.framework.webui.OAPageContext;
import oracle.apps.fnd.framework.webui.OAWebBeanConstants;
import oracle.apps.fnd.framework.webui.beans.OAWebBean;

import oracle.jbo.Row;

import xxup.oracle.apps.per.rim.lov.server.RIMProjectStatusVOImpl;
import xxup.oracle.apps.per.rim.server.RIMHelper;

public class RIMBasicInfoSubCO extends OAControllerImpl {
    
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

      pageContext.writeDiagnostics(this, "BasicTabCO", 1);

        OAApplicationModule am = 
            (OAApplicationModule)pageContext.getApplicationModule(webBean);

        Connection conn = 
            pageContext.getApplicationModule(webBean).getOADBTransaction().getJdbcConnection();

      String backUsed = pageContext.getParameter("backUsed");
      String actionFromURL = pageContext.getParameter("urlParam");
      pageContext.writeDiagnostics(this, "actionFromURL: " + actionFromURL, 1);
      String workflowType = "";
      

        /*on init, hide some fields*/
        OAViewObject mainVO = 
            (OAViewObject)am.findViewObject("RIMHeaderEOVO1");

        if( mainVO != null){
          mainVO.reset();
          Row mRow = mainVO.next();

          

          //set transaction No 
          String transactionNo = "";
          try {
              // pageContext.writeDiagnostics(this, row.getAttribute("PositionId").toString(), 1);
              transactionNo = String.format("%010d", Integer.parseInt(mRow.getAttribute("TransactionNo").toString()));

              mRow.setAttribute("TransactionNoDisp", transactionNo);
              mRow.setAttribute("RenderResearchType", Boolean.FALSE);
              mRow.setAttribute("RenderMainAreaIntOthers", Boolean.FALSE);

              
          } catch (Exception ex) {
              throw new OAException("Setting transactionNo: " + ex);
          }
          
          // actionFromURL = "CREATE";
          // Serializable []limitParams = { actionFromURL };
          // am.invokeMethod("limitProjectStatus", limitParams);

          // if(RIMHelper.C_RFC_AC.equals(actionFromURL)){  //prevent additional extension (end date)
          //   mRow.setAttribute("WorkflowType", RIMHelper.C_RFC_AC);
          // } else if(RIMHelper.C_ACCTG_AC.equals(actionFromURL) ||
          //         RIMHelper.C_FOR_CLSOUT_AC.equals(actionFromURL) ||
          //         RIMHelper.C_DON_FIN_REP_AC.equals(actionFromURL) ||
          //         RIMHelper.C_CLS_REP_VAL_AC.equals(actionFromURL) ||
          //         RIMHelper.C_CLOSED_AC.equals(actionFromURL) ||
          //         RIMHelper.C_COMPLETED_AC.equals(actionFromURL)       
          //   ){  //prevent additional extension)
          //   mRow.setAttribute("WorkflowType", actionFromURL);
          // }
          
          workflowType = mRow.getAttribute("WorkflowType") != null ? mRow.getAttribute("WorkflowType").toString() : "";
          if(RIMHelper.C_CREATE_AC.equals(actionFromURL) ||
             RIMHelper.C_SFL_AC.equals(actionFromURL) ||
             (RIMHelper.C_RFC_AC.equals(actionFromURL) && workflowType.equals(RIMHelper.C_CREATE_AC)) || //enable basic info fields on RFC + Submit
             "yes".equals(backUsed) && (RIMHelper.C_CREATE_AC.equals(actionFromURL) || RIMHelper.C_RFC_AC.equals(actionFromURL))
            ){
            
            
            
            mRow.setAttribute("DisableBasicInfo", false);


              //@TEST: default values
            // mRow.setAttribute("ResearchTitle", "Research 1");
            // mRow.setAttribute("ResearchType", "Descriptive Research");
            // mRow.setAttribute("ProjectImpactDesc", "Desc");
            // // mRow.setAttribute("MainAreaInterest", "Chemistry");
            // mRow.setAttribute("BriefDescription", "BriefDescription");
            // // mRow.setAttribute("ProjectLeaderDisp", "Gasis, Mr. Winson Rei Dalida");
            // // mRow.setAttribute("ProjectLeaderId", 31532);
            // mRow.setAttribute("PositionName", "Senior ICT Assistant");
            // mRow.setAttribute("AssignmentId", 67829);
            // mRow.setAttribute("StartDate", new Date());
            // mRow.setAttribute("EndDate", new Date());
            // mRow.setAttribute("ProjectStatus", "Execution and Control");
            


            // OAViewObject membersVO = 
            //   (OAViewObject)am.findViewObject("RIMTeamMembersEOVO1");

            // if( membersVO != null){
            //   membersVO.reset();
            //   Row memRow = membersVO.next();    

            //   memRow.setAttribute("FullName", "Gasis, Mr. Winson Rei Dalida");
            //   memRow.setAttribute("Position", "Senior ICT Assistant");
            //   memRow.setAttribute("Organization", "UPS UP Information Technology Development Center");
            //   memRow.setAttribute("Attribute2", 31532);

            // }

            // // OAViewObject otherMembersVO = 
            // //   (OAViewObject)am.findViewObject("RIMOtherTeamMembersEOVO1");

            // // if( otherMembersVO != null){
            // //   otherMembersVO.reset();
            // //   Row memRow = otherMembersVO.next();    

            // //   memRow.setAttribute("FullName", "Mem1");
            // //   // memRow.setAttribute("Position", "Senior ICT Assistant");
            // //   // memRow.setAttribute("Organization", "UPS UP Information Technology Development Center");

            // // }


            // OAViewObject fiscVO = 
            //     (OAViewObject)am.findViewObject("RIMFiscalDetailsEOVO1");
            // //@TEST: init fiscal
            // if( fiscVO != null){
            //   fiscVO.reset();
            //   Row fRow = fiscVO.next();

            //   fRow.setAttribute("ConstituentUnit", "UP System");
            //   fRow.setAttribute("ResponsibilityCenterDisp", "UPS System Budget Office");
            //   fRow.setAttribute("ResponsibilityCenter", "SA03006001");
            //   fRow.setAttribute("FundingAgency", "Fundz");

            // }


            // OAViewObject mileVO = 
            //     (OAViewObject)am.findViewObject("RIMMilestonesEOVO1");
            // //@TEST: init fiscal
            // if( mileVO != null){
            //   mileVO.reset();
            //   Row mileRow = mileVO.next();

            //   mileRow.setAttribute("Milestone", "First Milestone");
            //   mileRow.setAttribute("CompletionPercentage", "10");

            // }
            //END TEST


            //auto-select ProjectLeader onInit
            String strProjectLeaderId = "";
            String strProjectLeaderDisp = "";
            try {
                // pageContext.writeDiagnostics(this, row.getAttribute("PositionId").toString(), 1);
                String Query = 
                    "SELECT person_id, full_name " + "FROM per_all_people_f papf " + 
                    "WHERE SYSDATE BETWEEN effective_start_date AND effective_end_date " + 
                    "AND person_id = fnd_global.employee_id ";


                PreparedStatement stmt = conn.prepareStatement(Query);
                // stmt.setString(1, row.getAttribute("PositionId").toString());
                // stmt.setString(1, project_id);  
                for (ResultSet resultset = stmt.executeQuery(); resultset.next(); 
                ) {

                    strProjectLeaderId = resultset.getString("person_id");
                    strProjectLeaderDisp = resultset.getString("full_name");

                }

                mRow.setAttribute("ProjectLeaderId", strProjectLeaderId);
                mRow.setAttribute("ProjectLeaderDisp", strProjectLeaderDisp);
                
            } catch (Exception ex) {
                throw new OAException("Exception" + ex);
            }

          }else {
            
            mRow.setAttribute("DisableBasicInfo", true); 


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
      OAApplicationModule am = (OAApplicationModule)pageContext.getApplicationModule(webBean);
      
      OAViewObject mainVO = (OAViewObject) am.findViewObject("RIMHeaderEOVO1");

      if (pageContext.isLovEvent()) {

          
          String lovInputId = pageContext.getLovInputSourceId();
          Row row = mainVO.getCurrentRow();

          if ("ResearchType".equals(lovInputId)) {
              if(row.getAttribute("ResearchType") == null)
                return;

              String showOthersField = "Others".equals(row.getAttribute("ResearchType").toString()) ? "Y" : "N";

              if ("Y".equals(showOthersField))
                  row.setAttribute("RenderResearchType", true);
              else
                  row.setAttribute("RenderResearchType", false);
          } 
          // else if ("MainAreaInterest".equals(lovInputId)) {
          //   if(row.getAttribute("MainAreaInterest") == null)
          //       return;

          //     String showOthersField = "Others".equals(row.getAttribute("MainAreaInterest").toString()) ? "Y" : "N";

          //     if ("Y".equals(showOthersField))

          //         row.setAttribute("RenderMainAreaIntOthers", true);
          //     else 
          //         row.setAttribute("RenderMainAreaIntOthers", false);
              
          // } 
          // else if ("FullName1".equals(lovInputId)) {
          //     pageContext.writeDiagnostics(this, "entered mem", 1);
          //     OAViewObject membersVO = (OAViewObject) am.findViewObject("RIMTeamMembersEOVO1");
          //     Row mRow = membersVO.getCurrentRow();

          //     if(mRow.getAttribute("FullName") != null){
          //       pageContext.writeDiagnostics(this, "FullName: " + mRow.getAttribute("FullName").toString(), 1);
          //       if(mRow.getAttribute("Attribute2") != null)
          //         pageContext.writeDiagnostics(this, "PersonId: " + mRow.getAttribute("Attribute2").toString(), 1);
          //     }
              
          // }

      }

      if ("RenderMainAreaIntOthers".equals(pageContext.getParameter(OAWebBeanConstants.EVENT_PARAM))) {

            String eventRowSourceParam = 
                pageContext.getParameter(EVENT_SOURCE_ROW_REFERENCE);
            Row row = am.findRowByRef(eventRowSourceParam);

            //            pageContext.writeDiagnostics(this, eventRowSourceParam, 1);

            String selectedActivity = row.getAttribute("MainAreaInterest").toString();
            String isSelected = row.getAttribute("Selected").toString();

            Row mainRow = mainVO.getCurrentRow();


            if ("Y".equals(isSelected) && "Others".equals(selectedActivity)) {

                mainRow.setAttribute("RenderMainAreaIntOthers", Boolean.TRUE);

            } else if ("N".equals(isSelected) && 
                       "Others".equals(selectedActivity)) {
                mainRow.setAttribute("RenderMainAreaIntOthers", Boolean.FALSE);
            }

        }

    }
}
