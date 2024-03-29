--trigger
DROP TRIGGER xxup.xxup_rim_header_cls_au;

--sequence
DROP SEQUENCE XXUP.XXUP_RIM_FISCAL_SEQ;
DROP SEQUENCE XXUP.XXUP_RIM_MEMBERS_SEQ;
DROP SEQUENCE XXUP.XXUP_RIM_PROJ_IMPACT_SEQ;
DROP SEQUENCE XXUP.XXUP_RIM_FUNDING_SEQ;
DROP SEQUENCE XXUP.XXUP_RIM_MILESTONE_SEQ;
DROP SEQUENCE XXUP.XXUP_RIM_PUBLICATION_SEQ;
DROP SEQUENCE XXUP.XXUP_RIM_MAIN_AREA_INT_SEQ;
DROP SEQUENCE XXUP.XXUP_RIM_DEV_GOAL_SEQ;
DROP SEQUENCE XXUP.XXUP_RIM_END_DT_EXT_SEQ;
DROP SEQUENCE XXUP.XXUP_RIM_TRANSACTION_NO_SEQ;

DROP TABLE XXUP.XXUP_RIM_PROJ_IMPACT;
DROP TABLE XXUP.XXUP_RIM_TEAM_MEMBERS;
DROP TABLE XXUP.XXUP_RIM_DEV_GOAL;
DROP TABLE XXUP.XXUP_RIM_MAIN_AREA_INT;
DROP TABLE XXUP.XXUP_RIM_END_DT_EXT;
DROP TABLE XXUP.XXUP_RIM_FUND_ACCTG_INFO;
DROP TABLE XXUP.XXUP_RIM_ACCTG_INFO;
DROP TABLE XXUP.XXUP_RIM_MILESTONES;
DROP TABLE XXUP.XXUP_RIM_FUNDING;
DROP TABLE XXUP.XXUP_RIM_HEADER;
DROP TABLE XXUP.XXUP_RIM_FISCAL_DETAILS;

--package
DROP PACKAGE XXUP_RIM_WF_PKG;


--synonyms
DROP SYNONYM xxup_rim_header_cls_au;
DROP SYNONYM XXUP_RIM_FISCAL_SEQ;
DROP SYNONYM XXUP_RIM_MEMBERS_SEQ;
DROP SYNONYM XXUP_RIM_PROJ_IMPACT_SEQ;
DROP SYNONYM XXUP_RIM_FUNDING_SEQ;
DROP SYNONYM XXUP_RIM_MILESTONE_SEQ;
DROP SYNONYM XXUP_RIM_PUBLICATION_SEQ;
DROP SYNONYM XXUP_RIM_MAIN_AREA_INT_SEQ;
DROP SYNONYM XXUP_RIM_DEV_GOAL_SEQ;
DROP SYNONYM XXUP_RIM_END_DT_EXT_SEQ;
DROP SYNONYM XXUP_RIM_TRANSACTION_NO_SEQ;
DROP SYNONYM XXUP_RIM_PROJ_IMPACT;
DROP SYNONYM XXUP_RIM_TEAM_MEMBERS;
DROP SYNONYM XXUP_RIM_DEV_GOAL;
DROP SYNONYM XXUP_RIM_MAIN_AREA_INT;
DROP SYNONYM XXUP_RIM_END_DT_EXT;
DROP SYNONYM XXUP_RIM_FUND_ACCTG_INFO;
DROP SYNONYM XXUP_RIM_ACCTG_INFO;
DROP SYNONYM XXUP_RIM_MILESTONES;
DROP SYNONYM XXUP_RIM_FUNDING;
DROP SYNONYM XXUP_RIM_HEADER;
DROP SYNONYM XXUP_RIM_FISCAL_DETAILS;
