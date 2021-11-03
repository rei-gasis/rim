DROP TABLE xxup.xxup_rim_header;

CREATE TABLE xxup.xxup_rim_header
(
    transaction_no NUMBER(15) NOT NULL
   ,assignment_id NUMBER(10) 
   ,research_type VARCHAR2(240) 
   ,research_type_spec VARCHAR2(4000)
   ,research_title VARCHAR2(250) 
   ,research_title2 VARCHAR2(250)
   ,research_title3 VARCHAR2(250)
   ,brief_description VARCHAR2(4000)
   ,project_impact_desc VARCHAR2(4000) 
   ,project_leader_id NUMBER 
   ,start_date DATE 
   ,actual_end_date DATE
   ,project_status VARCHAR2(50) 
   ,project_remarks VARCHAR2(2000)
   ,stud_bs_no NUMBER
   ,stud_ms_no NUMBER
   ,stud_phd_no NUMBER
   ,item_key          VARCHAR2(100)
   ,approval_status   VARCHAR2(50)
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
   ,CONSTRAINT rim_hd_pk PRIMARY KEY(transaction_no, item_key)
);

CREATE SYNONYM xxup_rim_header FOR xxup.xxup_rim_header;
/
