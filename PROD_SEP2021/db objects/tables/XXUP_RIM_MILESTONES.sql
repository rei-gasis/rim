DROP TABLE xxup.XXUP_RIM_MILESTONES;

CREATE TABLE xxup.XXUP_RIM_MILESTONES
(   
    rim_milestone_id NUMBER(15)
   ,transaction_no NUMBER(15) NOT NULL
   ,item_key VARCHAR2(100)
   ,line_no NUMBER(2) 
   ,milestone VARCHAR2(255 BYTE) NOT NULL 
   ,completion_percentage NUMBER(3)  NOT NULL
   ,budget_released_date DATE
   ,remarks VARCHAR2(200)
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
   ,CONSTRAINT rim_mile_pk PRIMARY KEY(rim_milestone_id)
   ,CONSTRAINT rim_mile_perc_chk CHECK (completion_percentage BETWEEN 0 AND 100)
   
);


CREATE SYNONYM XXUP_RIM_MILESTONES FOR xxup.XXUP_RIM_MILESTONES;