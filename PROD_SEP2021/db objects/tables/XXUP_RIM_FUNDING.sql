
DROP TABLE xxup.XXUP_RIM_FUNDING;

CREATE TABLE xxup.XXUP_RIM_FUNDING
(   
    rim_fund_id NUMBER(15)
   ,line_no NUMBER(2) 
   ,transaction_no NUMBER(15) NOT NULL
   ,item_key VARCHAR2(100)
   ,funding_source VARCHAR2(30)
   ,description VARCHAR2(4000)
   ,amount NUMBER(17, 2) 
   ,currency VARCHAR2(3) 
   ,start_date DATE
   ,end_date DATE
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
   ,CONSTRAINT rim_fund_pk PRIMARY KEY(rim_fund_id)  
);

CREATE SYNONYM XXUP_RIM_FUNDING FOR xxup.XXUP_RIM_FUNDING;