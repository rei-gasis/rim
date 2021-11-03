
DROP TABLE xxup.XXUP_RIM_DEV_GOAL;

CREATE TABLE xxup.XXUP_RIM_DEV_GOAL
(   
    rim_dev_goal_id NUMBER(15)
   ,line_no NUMBER(2) 
   ,transaction_no NUMBER(15) NOT NULL
   ,item_key VARCHAR2(100)
   ,development_goal VARCHAR2(240)
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
   ,CONSTRAINT rim_dev_goal_pk PRIMARY KEY(rim_dev_goal_id)  
);

CREATE SYNONYM XXUP_RIM_DEV_GOAL FOR xxup.XXUP_RIM_DEV_GOAL;