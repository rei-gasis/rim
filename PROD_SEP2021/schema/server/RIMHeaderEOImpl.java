package xxup.oracle.apps.per.rim.schema.server;

import oracle.apps.fnd.framework.server.OADBTransaction;
import oracle.apps.fnd.framework.server.OAEntityDefImpl;
import oracle.apps.fnd.framework.server.OAEntityImpl;

import oracle.jbo.AttributeList;
import oracle.jbo.Key;
import oracle.jbo.RowIterator;
import oracle.jbo.domain.Date;
import oracle.jbo.domain.Number;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.EntityDefImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class RIMHeaderEOImpl extends OAEntityImpl {
    public static final int TRANSACTIONNO = 0;
    public static final int RIMFISCALDETAILSEO = 28;
    public static final int RIMMILESTONESEO = 29;
    public static final int RIMFUNDINGEO = 30;
    public static final int RIMPROJIMPACTEO = 31;
    public static final int RIMMEMBERSEO = 32;
    public static final int ASSIGNMENTID = 1;
    public static final int RESEARCHTYPE = 2;
    public static final int RESEARCHTITLE = 3;
    public static final int RESEARCHTITLE2 = 4;
    public static final int RESEARCHTITLE3 = 5;
    public static final int BRIEFDESCRIPTION = 6;
    public static final int PROJECTIMPACTDESC = 7;
    public static final int PROJECTLEADERID = 8;
    public static final int STARTDATE = 9;
    public static final int ACTUALENDDATE = 10;
    public static final int PROJECTSTATUS = 11;
    public static final int PROJECTREMARKS = 12;
    public static final int ITEMKEY = 13;
    public static final int APPROVALSTATUS = 14;
    public static final int ATTRIBUTE1 = 15;
    public static final int ATTRIBUTE2 = 16;
    public static final int ATTRIBUTE3 = 17;
    public static final int ATTRIBUTE4 = 18;
    public static final int ATTRIBUTE5 = 19;
    public static final int LASTUPDATEDATE = 20;
    public static final int LASTUPDATEDBY = 21;
    public static final int LASTUPDATELOGIN = 22;
    public static final int CREATEDBY = 23;
    public static final int CREATIONDATE = 24;
    public static final int RESEARCHTYPESPEC = 25;
    public static final int STUDBSNO = 26;
    public static final int STUDMSNO = 27;
    public static final int STUDPHDNO = 28;


    private static OAEntityDefImpl mDefinitionObject;

    /**This is the default constructor (do not remove)
     */
    public RIMHeaderEOImpl() {
    }


    /**Retrieves the definition object for this instance class.
     */
    public static synchronized EntityDefImpl getDefinitionObject() {
        if (mDefinitionObject == null) {
            mDefinitionObject = 
                    (OAEntityDefImpl)EntityDefImpl.findDefObject("xxup.oracle.apps.per.rim.schema.server.RIMHeaderEO");
        }
        return mDefinitionObject;
    }

    /**Gets the attribute value for TransactionNo, using the alias name TransactionNo
     */
    public Number getTransactionNo() {
        return (Number)getAttributeInternal(TRANSACTIONNO);
    }

    /**Sets <code>value</code> as the attribute value for TransactionNo
     */
    public void setTransactionNo(Number value) {
        setAttributeInternal(TRANSACTIONNO, value);
    }

    /**Gets the attribute value for AssignmentId, using the alias name AssignmentId
     */
    public Number getAssignmentId() {
        return (Number)getAttributeInternal(ASSIGNMENTID);
    }

    /**Sets <code>value</code> as the attribute value for AssignmentId
     */
    public void setAssignmentId(Number value) {
        setAttributeInternal(ASSIGNMENTID, value);
    }

    /**Gets the attribute value for ResearchType, using the alias name ResearchType
     */
    public String getResearchType() {
        return (String)getAttributeInternal(RESEARCHTYPE);
    }

    /**Sets <code>value</code> as the attribute value for ResearchType
     */
    public void setResearchType(String value) {
        setAttributeInternal(RESEARCHTYPE, value);
    }

    /**Gets the attribute value for ResearchTitle, using the alias name ResearchTitle
     */
    public String getResearchTitle() {
        return (String)getAttributeInternal(RESEARCHTITLE);
    }

    /**Sets <code>value</code> as the attribute value for ResearchTitle
     */
    public void setResearchTitle(String value) {
        setAttributeInternal(RESEARCHTITLE, value);
    }

    /**Gets the attribute value for ResearchTitle2, using the alias name ResearchTitle2
     */
    public String getResearchTitle2() {
        return (String)getAttributeInternal(RESEARCHTITLE2);
    }

    /**Sets <code>value</code> as the attribute value for ResearchTitle2
     */
    public void setResearchTitle2(String value) {
        setAttributeInternal(RESEARCHTITLE2, value);
    }

    /**Gets the attribute value for ResearchTitle3, using the alias name ResearchTitle3
     */
    public String getResearchTitle3() {
        return (String)getAttributeInternal(RESEARCHTITLE3);
    }

    /**Sets <code>value</code> as the attribute value for ResearchTitle3
     */
    public void setResearchTitle3(String value) {
        setAttributeInternal(RESEARCHTITLE3, value);
    }

    /**Gets the attribute value for BriefDescription, using the alias name BriefDescription
     */
    public String getBriefDescription() {
        return (String)getAttributeInternal(BRIEFDESCRIPTION);
    }

    /**Sets <code>value</code> as the attribute value for BriefDescription
     */
    public void setBriefDescription(String value) {
        setAttributeInternal(BRIEFDESCRIPTION, value);
    }


    /**Gets the attribute value for ProjectImpactDesc, using the alias name ProjectImpactDesc
     */
    public String getProjectImpactDesc() {
        return (String)getAttributeInternal(PROJECTIMPACTDESC);
    }

    /**Sets <code>value</code> as the attribute value for ProjectImpactDesc
     */
    public void setProjectImpactDesc(String value) {
        setAttributeInternal(PROJECTIMPACTDESC, value);
    }

    /**Gets the attribute value for ProjectLeaderId, using the alias name ProjectLeaderId
     */
    public Number getProjectLeaderId() {
        return (Number)getAttributeInternal(PROJECTLEADERID);
    }

    /**Sets <code>value</code> as the attribute value for ProjectLeaderId
     */
    public void setProjectLeaderId(Number value) {
        setAttributeInternal(PROJECTLEADERID, value);
    }

    /**Gets the attribute value for StartDate, using the alias name StartDate
     */
    public Date getStartDate() {
        return (Date)getAttributeInternal(STARTDATE);
    }

    /**Sets <code>value</code> as the attribute value for StartDate
     */
    public void setStartDate(Date value) {
        setAttributeInternal(STARTDATE, value);
    }


    /**Gets the attribute value for ActualEndDate, using the alias name ActualEndDate
     */
    public Date getActualEndDate() {
        return (Date)getAttributeInternal(ACTUALENDDATE);
    }

    /**Sets <code>value</code> as the attribute value for ActualEndDate
     */
    public void setActualEndDate(Date value) {
        setAttributeInternal(ACTUALENDDATE, value);
    }

    /**Gets the attribute value for ProjectStatus, using the alias name ProjectStatus
     */
    public String getProjectStatus() {
        return (String)getAttributeInternal(PROJECTSTATUS);
    }

    /**Sets <code>value</code> as the attribute value for ProjectStatus
     */
    public void setProjectStatus(String value) {
        setAttributeInternal(PROJECTSTATUS, value);
    }

    /**Gets the attribute value for ProjectRemarks, using the alias name ProjectRemarks
     */
    public String getProjectRemarks() {
        return (String)getAttributeInternal(PROJECTREMARKS);
    }

    /**Sets <code>value</code> as the attribute value for ProjectRemarks
     */
    public void setProjectRemarks(String value) {
        setAttributeInternal(PROJECTREMARKS, value);
    }

    /**Gets the attribute value for ItemKey, using the alias name ItemKey
     */
    public String getItemKey() {
        return (String)getAttributeInternal(ITEMKEY);
    }

    /**Sets <code>value</code> as the attribute value for ItemKey
     */
    public void setItemKey(String value) {
        setAttributeInternal(ITEMKEY, value);
    }

    /**Gets the attribute value for ApprovalStatus, using the alias name ApprovalStatus
     */
    public String getApprovalStatus() {
        return (String)getAttributeInternal(APPROVALSTATUS);
    }

    /**Sets <code>value</code> as the attribute value for ApprovalStatus
     */
    public void setApprovalStatus(String value) {
        setAttributeInternal(APPROVALSTATUS, value);
    }

    /**Gets the attribute value for Attribute1, using the alias name Attribute1
     */
    public String getAttribute1() {
        return (String)getAttributeInternal(ATTRIBUTE1);
    }

    /**Sets <code>value</code> as the attribute value for Attribute1
     */
    public void setAttribute1(String value) {
        setAttributeInternal(ATTRIBUTE1, value);
    }

    /**Gets the attribute value for Attribute2, using the alias name Attribute2
     */
    public String getAttribute2() {
        return (String)getAttributeInternal(ATTRIBUTE2);
    }

    /**Sets <code>value</code> as the attribute value for Attribute2
     */
    public void setAttribute2(String value) {
        setAttributeInternal(ATTRIBUTE2, value);
    }

    /**Gets the attribute value for Attribute3, using the alias name Attribute3
     */
    public String getAttribute3() {
        return (String)getAttributeInternal(ATTRIBUTE3);
    }

    /**Sets <code>value</code> as the attribute value for Attribute3
     */
    public void setAttribute3(String value) {
        setAttributeInternal(ATTRIBUTE3, value);
    }

    /**Gets the attribute value for Attribute4, using the alias name Attribute4
     */
    public String getAttribute4() {
        return (String)getAttributeInternal(ATTRIBUTE4);
    }

    /**Sets <code>value</code> as the attribute value for Attribute4
     */
    public void setAttribute4(String value) {
        setAttributeInternal(ATTRIBUTE4, value);
    }

    /**Gets the attribute value for Attribute5, using the alias name Attribute5
     */
    public String getAttribute5() {
        return (String)getAttributeInternal(ATTRIBUTE5);
    }

    /**Sets <code>value</code> as the attribute value for Attribute5
     */
    public void setAttribute5(String value) {
        setAttributeInternal(ATTRIBUTE5, value);
    }

    /**Gets the attribute value for LastUpdateDate, using the alias name LastUpdateDate
     */
    public Date getLastUpdateDate() {
        return (Date)getAttributeInternal(LASTUPDATEDATE);
    }

    /**Sets <code>value</code> as the attribute value for LastUpdateDate
     */
    public void setLastUpdateDate(Date value) {
        setAttributeInternal(LASTUPDATEDATE, value);
    }

    /**Gets the attribute value for LastUpdatedBy, using the alias name LastUpdatedBy
     */
    public Number getLastUpdatedBy() {
        return (Number)getAttributeInternal(LASTUPDATEDBY);
    }

    /**Sets <code>value</code> as the attribute value for LastUpdatedBy
     */
    public void setLastUpdatedBy(Number value) {
        setAttributeInternal(LASTUPDATEDBY, value);
    }

    /**Gets the attribute value for LastUpdateLogin, using the alias name LastUpdateLogin
     */
    public Number getLastUpdateLogin() {
        return (Number)getAttributeInternal(LASTUPDATELOGIN);
    }

    /**Sets <code>value</code> as the attribute value for LastUpdateLogin
     */
    public void setLastUpdateLogin(Number value) {
        setAttributeInternal(LASTUPDATELOGIN, value);
    }

    /**Gets the attribute value for CreatedBy, using the alias name CreatedBy
     */
    public Number getCreatedBy() {
        return (Number)getAttributeInternal(CREATEDBY);
    }

    /**Sets <code>value</code> as the attribute value for CreatedBy
     */
    public void setCreatedBy(Number value) {
        setAttributeInternal(CREATEDBY, value);
    }

    /**Gets the attribute value for CreationDate, using the alias name CreationDate
     */
    public Date getCreationDate() {
        return (Date)getAttributeInternal(CREATIONDATE);
    }

    /**Sets <code>value</code> as the attribute value for CreationDate
     */
    public void setCreationDate(Date value) {
        setAttributeInternal(CREATIONDATE, value);
    }

    /**Gets the attribute value for ResearchTypeSpec, using the alias name ResearchTypeSpec
     */
    public String getResearchTypeSpec() {
        return (String)getAttributeInternal(RESEARCHTYPESPEC);
    }

    /**Sets <code>value</code> as the attribute value for ResearchTypeSpec
     */
    public void setResearchTypeSpec(String value) {
        setAttributeInternal(RESEARCHTYPESPEC, value);
    }

    /**Add attribute defaulting logic in this method.
     */
    public void create(AttributeList attributeList) {
        super.create(attributeList);
        
         if (getTransactionNo() == null) {
            OADBTransaction tr = getOADBTransaction();
            setTransactionNo(tr.getSequenceValue("XXUP.XXUP_RIM_TRANSACTION_NO_SEQ"));
        }
    }

    /**getAttrInvokeAccessor: generated method. Do not modify.
     */
    protected Object getAttrInvokeAccessor(int index, 
                                           AttributeDefImpl attrDef) throws Exception {
        switch (index) {
        case TRANSACTIONNO:
            return getTransactionNo();
        case ASSIGNMENTID:
            return getAssignmentId();
        case RESEARCHTYPE:
            return getResearchType();
        case RESEARCHTITLE:
            return getResearchTitle();
        case RESEARCHTITLE2:
            return getResearchTitle2();
        case RESEARCHTITLE3:
            return getResearchTitle3();
        case BRIEFDESCRIPTION:
            return getBriefDescription();
        case PROJECTIMPACTDESC:
            return getProjectImpactDesc();
        case PROJECTLEADERID:
            return getProjectLeaderId();
        case STARTDATE:
            return getStartDate();
        case ACTUALENDDATE:
            return getActualEndDate();
        case PROJECTSTATUS:
            return getProjectStatus();
        case PROJECTREMARKS:
            return getProjectRemarks();
        case ITEMKEY:
            return getItemKey();
        case APPROVALSTATUS:
            return getApprovalStatus();
        case ATTRIBUTE1:
            return getAttribute1();
        case ATTRIBUTE2:
            return getAttribute2();
        case ATTRIBUTE3:
            return getAttribute3();
        case ATTRIBUTE4:
            return getAttribute4();
        case ATTRIBUTE5:
            return getAttribute5();
        case LASTUPDATEDATE:
            return getLastUpdateDate();
        case LASTUPDATEDBY:
            return getLastUpdatedBy();
        case LASTUPDATELOGIN:
            return getLastUpdateLogin();
        case CREATEDBY:
            return getCreatedBy();
        case CREATIONDATE:
            return getCreationDate();
        case RESEARCHTYPESPEC:
            return getResearchTypeSpec();
        case STUDBSNO:
            return getStudBsNo();
        case STUDMSNO:
            return getStudMsNo();
        case STUDPHDNO:
            return getStudPhdNo();
        default:
            return super.getAttrInvokeAccessor(index, attrDef);
        }
    }

    /**setAttrInvokeAccessor: generated method. Do not modify.
     */
    protected void setAttrInvokeAccessor(int index, Object value, 
                                         AttributeDefImpl attrDef) throws Exception {
        switch (index) {
        case TRANSACTIONNO:
            setTransactionNo((Number)value);
            return;
        case ASSIGNMENTID:
            setAssignmentId((Number)value);
            return;
        case RESEARCHTYPE:
            setResearchType((String)value);
            return;
        case RESEARCHTITLE:
            setResearchTitle((String)value);
            return;
        case RESEARCHTITLE2:
            setResearchTitle2((String)value);
            return;
        case RESEARCHTITLE3:
            setResearchTitle3((String)value);
            return;
        case BRIEFDESCRIPTION:
            setBriefDescription((String)value);
            return;
        case PROJECTIMPACTDESC:
            setProjectImpactDesc((String)value);
            return;
        case PROJECTLEADERID:
            setProjectLeaderId((Number)value);
            return;
        case STARTDATE:
            setStartDate((Date)value);
            return;
        case ACTUALENDDATE:
            setActualEndDate((Date)value);
            return;
        case PROJECTSTATUS:
            setProjectStatus((String)value);
            return;
        case PROJECTREMARKS:
            setProjectRemarks((String)value);
            return;
        case ITEMKEY:
            setItemKey((String)value);
            return;
        case APPROVALSTATUS:
            setApprovalStatus((String)value);
            return;
        case ATTRIBUTE1:
            setAttribute1((String)value);
            return;
        case ATTRIBUTE2:
            setAttribute2((String)value);
            return;
        case ATTRIBUTE3:
            setAttribute3((String)value);
            return;
        case ATTRIBUTE4:
            setAttribute4((String)value);
            return;
        case ATTRIBUTE5:
            setAttribute5((String)value);
            return;
        case LASTUPDATEDATE:
            setLastUpdateDate((Date)value);
            return;
        case LASTUPDATEDBY:
            setLastUpdatedBy((Number)value);
            return;
        case LASTUPDATELOGIN:
            setLastUpdateLogin((Number)value);
            return;
        case CREATEDBY:
            setCreatedBy((Number)value);
            return;
        case CREATIONDATE:
            setCreationDate((Date)value);
            return;
        case RESEARCHTYPESPEC:
            setResearchTypeSpec((String)value);
            return;
        case STUDBSNO:
            setStudBsNo((Number)value);
            return;
        case STUDMSNO:
            setStudMsNo((Number)value);
            return;
        case STUDPHDNO:
            setStudPhdNo((Number)value);
            return;
        default:
            super.setAttrInvokeAccessor(index, value, attrDef);
            return;
        }
    }

    /**Gets the associated entity oracle.jbo.RowIterator
     */
    public RowIterator getRIMFiscalDetailsEO() {
        return (RowIterator)getAttributeInternal(RIMFISCALDETAILSEO);
    }

    /**Gets the associated entity oracle.jbo.RowIterator
     */
    public RowIterator getRIMMilestonesEO() {
        return (RowIterator)getAttributeInternal(RIMMILESTONESEO);
    }

    /**Gets the associated entity oracle.jbo.RowIterator
     */
    public RowIterator getRIMFundingEO() {
        return (RowIterator)getAttributeInternal(RIMFUNDINGEO);
    }

    /**Gets the associated entity oracle.jbo.RowIterator
     */
    public RowIterator getRIMProjImpactEO() {
        return (RowIterator)getAttributeInternal(RIMPROJIMPACTEO);
    }

    /**Gets the associated entity oracle.jbo.RowIterator
     */
    public RowIterator getRIMMembersEO() {
        return (RowIterator)getAttributeInternal(RIMMEMBERSEO);
    }

    /**Gets the attribute value for StudBsNo, using the alias name StudBsNo
     */
    public Number getStudBsNo() {
        return (Number)getAttributeInternal(STUDBSNO);
    }

    /**Sets <code>value</code> as the attribute value for StudBsNo
     */
    public void setStudBsNo(Number value) {
        setAttributeInternal(STUDBSNO, value);
    }

    /**Gets the attribute value for StudMsNo, using the alias name StudMsNo
     */
    public Number getStudMsNo() {
        return (Number)getAttributeInternal(STUDMSNO);
    }

    /**Sets <code>value</code> as the attribute value for StudMsNo
     */
    public void setStudMsNo(Number value) {
        setAttributeInternal(STUDMSNO, value);
    }

    /**Gets the attribute value for StudPhdNo, using the alias name StudPhdNo
     */
    public Number getStudPhdNo() {
        return (Number)getAttributeInternal(STUDPHDNO);
    }

    /**Sets <code>value</code> as the attribute value for StudPhdNo
     */
    public void setStudPhdNo(Number value) {
        setAttributeInternal(STUDPHDNO, value);
    }

    /**Creates a Key object based on given key constituents
     */
    public static Key createPrimaryKey(Number transactionNo, String itemKey) {
        return new Key(new Object[]{transactionNo, itemKey});
    }
}
