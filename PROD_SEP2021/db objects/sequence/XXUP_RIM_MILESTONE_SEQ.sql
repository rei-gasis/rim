DROP SEQUENCE XXUP.xxup_rim_milestone_seq;

CREATE SEQUENCE XXUP.XXUP_RIM_MILESTONE_SEQ
MINVALUE 1 
MAXVALUE 9999999999999999999999999999 
INCREMENT BY 1 
START WITH 1
CACHE 20 NOORDER NOCYCLE;

CREATE SYNONYM xxup_rim_milestone_seq FOR XXUP.xxup_rim_milestone_seq;