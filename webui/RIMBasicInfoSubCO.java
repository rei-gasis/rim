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

      System.out.println("BasicTabCO");

        OAApplicationModule am = 
            (OAApplicationModule)pageContext.getApplicationModule(webBean);

        Connection conn = 
            pageContext.getApplicationModule(webBean).getOADBTransaction().getJdbcConnection();

        // String backUsed = pageContext.getParameter("backUsed");
      String actionFromURL = pageContext.getParameter("urlParam");
      System.out.println("actionFromURL: " + actionFromURL);      

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
              
          } catch (Exception ex) {
              throw new OAException("Setting transactionNo: " + ex);
          }
          
          // actionFromURL = "CREATE";
          // Serializable []limitParams = { actionFromURL };
          // am.invokeMethod("limitProjectStatus", limitParams);


          if(RIMHelper.C_CREATE_AC.equals(actionFromURL)){
            
            

            mRow.setAttribute("DisableBasicInfo", false);


              //@TEST: default values
            // mRow.setAttribute("ResearchTitle", "Research 1");
            // mRow.setAttribute("ResearchType", "Descriptive Research");
            // mRow.setAttribute("ProjectImpactDesc", "Desc");
            // mRow.setAttribute("MainAreaInterest", "Chemistry");
            // mRow.setAttribute("ProjectLeaderDisp", "Gasis, Mr. Winson Rei Dalida");
            // mRow.setAttribute("PositionName", "Senior ICT Assistant");
            // mRow.setAttribute("AssignmentId", 67829);
            // mRow.setAttribute("StartDate", new Date());
            // mRow.setAttribute("EndDate", new Date());
            // mRow.setAttribute("ProjectStatus", "Execution and Control");
            // mRow.setAttribute("ProjectLeaderId", 31532);


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

            // //   OAViewObject othermembersVO = 
            // //   (OAViewObject)am.findViewObject("RIMOtherTeamMembersVO1");

            // // if( othermembersVO != null){
            // //   othermembersVO.reset();
            // //   Row memRow = othermembersVO.next();    

            // //   memRow.setAttribute("FullName", "Gasis, Mr. Winson Rei Dalida");
            // //   memRow.setAttribute("Position", "Senior ICT Assistant");
            // //   memRow.setAttribute("Organization", "UPS UP Information Technology Development Center");

            // //   memRow.setAttribute("Attribute1", "Non-UP");

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

          // }else{
            
          //   mRow.setAttribute("DisableBasicInfo", true);
          // }

          
          

          
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
          //     System.out.println("entered mem");
          //     OAViewObject membersVO = (OAViewObject) am.findViewObject("RIMTeamMembersEOVO1");
          //     Row mRow = membersVO.getCurrentRow();

          //     if(mRow.getAttribute("FullName") != null){
          //       System.out.println("FullName: " + mRow.getAttribute("FullName").toString());
          //       if(mRow.getAttribute("Attribute2") != null)
          //         System.out.println("PersonId: " + mRow.getAttribute("Attribute2").toString());
          //     }
              
          // }

      }

      if ("RenderMainAreaIntOthers".equals(pageContext.getParameter(OAWebBeanConstants.EVENT_PARAM))) {

            String eventRowSourceParam = 
                pageContext.getParameter(EVENT_SOURCE_ROW_REFERENCE);
            Row row = am.findRowByRef(eventRowSourceParam);

            //            System.out.println(eventRowSourceParam);

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
