
DROP TABLE xxup.xxup_rim_proj_impact;

CREATE TABLE xxup.xxup_rim_proj_impact
(   
    rim_proj_impact_id NUMBER(15) NOT NULL
   ,line_no NUMBER(2)  
   ,transaction_no NUMBER(15) NOT NULL
   ,item_key VARCHAR2(100)
   ,project_impact VARCHAR2(250)
   ,selected VARCHAR2(1)
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
   ,CONSTRAINT rim_proj_pk PRIMARY KEY(rim_proj_impact_id)
);

CREATE SYNONYM xxup_rim_proj_impact FOR xxup.xxup_rim_proj_impact;