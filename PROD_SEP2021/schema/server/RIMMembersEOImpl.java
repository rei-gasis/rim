package xxup.oracle.apps.per.rim.schema.server;

import oracle.apps.fnd.framework.server.OADBTransaction;
import oracle.apps.fnd.framework.server.OAEntityDefImpl;
import oracle.apps.fnd.framework.server.OAEntityImpl;

import oracle.jbo.AttributeList;
import oracle.jbo.Key;
import oracle.jbo.domain.Date;
import oracle.jbo.domain.Number;
import oracle.jbo.server.AttributeDefImpl;
import oracle.jbo.server.EntityDefImpl;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Custom code may be added to this class.
// ---    Warning: Do not modify method signatures of generated methods.
// ---------------------------------------------------------------------
public class RIMMembersEOImpl extends OAEntityImpl {
    public static final int RIMMEMBERID = 0;
    public static final int LINENO = 1;
    public static final int TRANSACTIONNO = 2;
    public static final int ITEMKEY = 3;
    public static final int FULLNAME = 4;
    public static final int POSITION = 5;
    public static final int ENDDATE = 6;
    public static final int ORGANIZATION = 7;
    public static final int PROJECTROLE = 8;
    public static final int ATTRIBUTE1 = 9;
    public static final int ATTRIBUTE2 = 10;
    public static final int ATTRIBUTE3 = 11;
    public static final int ATTRIBUTE4 = 12;
    public static final int ATTRIBUTE5 = 13;
    public static final int LASTUPDATEDATE = 14;
    public static final int LASTUPDATEDBY = 15;
    public static final int LASTUPDATELOGIN = 16;
    public static final int CREATEDBY = 17;
    public static final int CREATIONDATE = 18;


    private static OAEntityDefImpl mDefinitionObject;

    /**This is the default constructor (do not remove)
     */
    public RIMMembersEOImpl() {
    }


    /**Retrieves the definition object for this instance class.
     */
    public static synchronized EntityDefImpl getDefinitionObject() {
        if (mDefinitionObject == null) {
            mDefinitionObject = 
                    (OAEntityDefImpl)EntityDefImpl.findDefObject("xxup.oracle.apps.per.rim.schema.server.RIMMembersEO");
        }
        return mDefinitionObject;
    }

    /**Add attribute defaulting logic in this method.
     */
    public void create(AttributeList attributeList) {
        super.create(attributeList);
        
        if (getRimMemberId() == null) {
            OADBTransaction tr = getOADBTransaction();
            setRimMemberId(tr.getSequenceValue("XXUP.XXUP_RIM_MEMBERS_SEQ"));
        }
        
    }

    /**Gets the attribute value for RimMemberId, using the alias name RimMemberId
     */
    public Number getRimMemberId() {
        return (Number)getAttributeInternal(RIMMEMBERID);
    }

    /**Sets <code>value</code> as the attribute value for RimMemberId
     */
    public void setRimMemberId(Number value) {
        setAttributeInternal(RIMMEMBERID, value);
    }

    /**Gets the attribute value for LineNo, using the alias name LineNo
     */
    public Number getLineNo() {
        return (Number)getAttributeInternal(LINENO);
    }

    /**Sets <code>value</code> as the attribute value for LineNo
     */
    public void setLineNo(Number value) {
        setAttributeInternal(LINENO, value);
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

    /**Gets the attribute value for FullName, using the alias name FullName
     */
    public String getFullName() {
        return (String)getAttributeInternal(FULLNAME);
    }

    /**Sets <code>value</code> as the attribute value for FullName
     */
    public void setFullName(String value) {
        setAttributeInternal(FULLNAME, value);
    }

    /**Gets the attribute value for Position, using the alias name Position
     */
    public String getPosition() {
        return (String)getAttributeInternal(POSITION);
    }

    /**Sets <code>value</code> as the attribute value for Position
     */
    public void setPosition(String value) {
        setAttributeInternal(POSITION, value);
    }

    /**Gets the attribute value for Organization, using the alias name Organization
     */
    public String getOrganization() {
        return (String)getAttributeInternal(ORGANIZATION);
    }

    /**Sets <code>value</code> as the attribute value for Organization
     */
    public void setOrganization(String value) {
        setAttributeInternal(ORGANIZATION, value);
    }

    /**Gets the attribute value for ProjectRole, using the alias name ProjectRole
     */
    public String getProjectRole() {
        return (String)getAttributeInternal(PROJECTROLE);
    }

    /**Sets <code>value</code> as the attribute value for ProjectRole
     */
    public void setProjectRole(String value) {
        setAttributeInternal(PROJECTROLE, value);
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

    /**getAttrInvokeAccessor: generated method. Do not modify.
     */
    protected Object getAttrInvokeAccessor(int index, 
                                           AttributeDefImpl attrDef) throws Exception {
        switch (index) {
        case RIMMEMBERID:
            return getRimMemberId();
        case LINENO:
            return getLineNo();
        case TRANSACTIONNO:
            return getTransactionNo();
        case ITEMKEY:
            return getItemKey();
        case FULLNAME:
            return getFullName();
        case POSITION:
            return getPosition();
        case ENDDATE:
            return getEndDate();
        case ORGANIZATION:
            return getOrganization();
        case PROJECTROLE:
            return getProjectRole();
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
        default:
            return super.getAttrInvokeAccessor(index, attrDef);
        }
    }

    /**setAttrInvokeAccessor: generated method. Do not modify.
     */
    protected void setAttrInvokeAccessor(int index, Object value, 
                                         AttributeDefImpl attrDef) throws Exception {
        switch (index) {
        case RIMMEMBERID:
            setRimMemberId((Number)value);
            return;
        case LINENO:
            setLineNo((Number)value);
            return;
        case TRANSACTIONNO:
            setTransactionNo((Number)value);
            return;
        case ITEMKEY:
            setItemKey((String)value);
            return;
        case FULLNAME:
            setFullName((String)value);
            return;
        case POSITION:
            setPosition((String)value);
            return;
        case ENDDATE:
            setEndDate((Date)value);
            return;
        case ORGANIZATION:
            setOrganization((String)value);
            return;
        case PROJECTROLE:
            setProjectRole((String)value);
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
        default:
            super.setAttrInvokeAccessor(index, value, attrDef);
            return;
        }
    }


    /**Gets the attribute value for EndDate, using the alias name EndDate
     */
    public Date getEndDate() {
        return (Date)getAttributeInternal(ENDDATE);
    }

    /**Sets <code>value</code> as the attribute value for EndDate
     */
    public void setEndDate(Date value) {
        setAttributeInternal(ENDDATE, value);
    }

    /**Creates a Key object based on given key constituents
     */
    public static Key createPrimaryKey(Number rimMemberId) {
        return new Key(new Object[]{rimMemberId});
    }
}
