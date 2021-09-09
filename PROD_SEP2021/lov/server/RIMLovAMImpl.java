package xxup.oracle.apps.per.rim.lov.server;

import oracle.apps.fnd.framework.server.OAApplicationModuleImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class RIMLovAMImpl extends OAApplicationModuleImpl {
    /**This is the default constructor (do not remove)
     */
    public RIMLovAMImpl() {
    }


    /**Sample main for debugging Business Components code using the tester.
     */
    public static void main(String[] args) {
        launchTester("xxup.oracle.apps.per.rim.lov.server", /* package name */
      "RIMLovAMLocal" /* Configuration Name */);
    }

    /**Container's getter for RIMMainAreaIntVO1
     */
    public RIMMainAreaIntVOImpl getRIMMainAreaIntVO1() {
        return (RIMMainAreaIntVOImpl)findViewObject("RIMMainAreaIntVO1");
    }

    /**Container's getter for RIMProjectImpactVO1
     */
    public RIMProjectImpactVOImpl getRIMProjectImpactVO1() {
        return (RIMProjectImpactVOImpl)findViewObject("RIMProjectImpactVO1");
    }

    /**Container's getter for RIMYesNoVO1
     */
    public RIMYesNoVOImpl getRIMYesNoVO1() {
        return (RIMYesNoVOImpl)findViewObject("RIMYesNoVO1");
    }

    /**Container's getter for RIMProjectStatusVO1
     */
    public RIMProjectStatusVOImpl getRIMProjectStatusVO1() {
        return (RIMProjectStatusVOImpl)findViewObject("RIMProjectStatusVO1");
    }

    /**Container's getter for RIMRespCenterVO1
     */
    public RIMRespCenterVOImpl getRIMRespCenterVO1() {
        return (RIMRespCenterVOImpl)findViewObject("RIMRespCenterVO1");
    }


    /**Container's getter for RIMCurrencyVO1
     */
    public RIMCurrencyVOImpl getRIMCurrencyVO1() {
        return (RIMCurrencyVOImpl)findViewObject("RIMCurrencyVO1");
    }

    /**Container's getter for RIMDistributionModeVO1
     */
    public RIMDistributionModeVOImpl getRIMDistributionModeVO1() {
        return (RIMDistributionModeVOImpl)findViewObject("RIMDistributionModeVO1");
    }

    /**Container's getter for RIMSPCodeVO1
     */
    public RIMSPCodeVOImpl getRIMSPCodeVO1() {
        return (RIMSPCodeVOImpl)findViewObject("RIMSPCodeVO1");
    }

    /**Container's getter for RIMResearchTypeVO1
     */
    public RIMResearchTypeVOImpl getRIMResearchTypeVO1() {
        return (RIMResearchTypeVOImpl)findViewObject("RIMResearchTypeVO1");
    }

    /**Container's getter for PerPositionsVO1
     */
    public PerPositionsVOImpl getPerPositionsVO1() {
        return (PerPositionsVOImpl)findViewObject("PerPositionsVO1");
    }
    
    public void initPosVO(String pPersonId){
        PerPositionsVOImpl posVO = getPerPositionsVO1();
        
        posVO.initPosVO(pPersonId);
        
        
    }
    
    
    public void limitProjectStatus(String pUrl){
        RIMProjectStatusVOImpl pVO = getRIMProjectStatusVO1();

        pVO.limitProjectStatus(pUrl);

//        pageContext.writeDiagnostics(this, "LovAM", 1);
        
    }

    /**Container's getter for RIMFundingVO1
     */
    public RIMFundingVOImpl getRIMFundingVO1() {
        return (RIMFundingVOImpl)findViewObject("RIMFundingVO1");
    }


    /**Container's getter for RIMCUVO1
     */
    public RIMCUVOImpl getRIMCUVO1() {
        return (RIMCUVOImpl)findViewObject("RIMCUVO1");
    }

    /**Container's getter for PerEmpVO1
     */
    public PerEmpVOImpl getPerEmpVO1() {
        return (PerEmpVOImpl)findViewObject("PerEmpVO1");
    }

    /**Container's getter for PerOrgVO1
     */
    public PerOrgVOImpl getPerOrgVO1() {
        return (PerOrgVOImpl)findViewObject("PerOrgVO1");
    }

    /**Container's getter for PerPosVO1
     */
    public PerPosVOImpl getPerPosVO1() {
        return (PerPosVOImpl)findViewObject("PerPosVO1");
    }

    /**Container's getter for RIMDevGoalVO1
     */
    public RIMDevGoalVOImpl getRIMDevGoalVO1() {
        return (RIMDevGoalVOImpl)findViewObject("RIMDevGoalVO1");
    }
}
