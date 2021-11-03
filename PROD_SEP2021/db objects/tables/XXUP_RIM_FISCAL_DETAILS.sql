DROP TABLE xxup.XXUP_RIM_FISCAL_DETAILS;

CREATE TABLE xxup.XXUP_RIM_FISCAL_DETAILS
(   
    rim_fisc_id NUMBER(15)
   ,transaction_no NUMBER(15) NOT NULL
   ,constituent_unit VARCHAR2(30)
   ,responsibility_center VARCHAR2(140)
   ,collaborating_agency VARCHAR2(250)
   ,funding_agency VARCHAR2(250)
   ,sp_code VARCHAR2(20)
   ,total_amount NUMBER(17,2)
   ,fund_controller_id NUMBER(10)
   ,far_excluded VARCHAR2(1)
   ,item_key VARCHAR2(100)    
   ,ATTRIBUTE1        VARCHAR2(140)  
   ,ATTRIBUTE2        VARCHAR2(140)       
   ,ATTRIBUTE3        VARCHAR2(140)  
   ,ATTRIBUTE4        VARCHAR2(140)  
   ,ATTRIBUTE5        VARCHAR2(140)  
   ,LAST_UPDATE_DATE  DATE           
   ,LAST_UPDATED_BY   NUMBER(15)     
   ,LAST_UPDATE_LOGIN NUMBER(15)    
   ,CREATED_BY        NUMBER(15)
   ,CREATION_DATE     DATE
   ,CONSTRAINT rim_fisc_pk PRIMARY KEY(rim_fisc_id)
);


CREATE SYNONYM XXUP_RIM_FISCAL_DETAILS FOR xxup.XXUP_RIM_FISCAL_DETAILS;