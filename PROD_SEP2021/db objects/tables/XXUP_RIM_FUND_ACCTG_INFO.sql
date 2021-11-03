DROP TABLE xxup.xxup_rim_fund_acctg_info;

CREATE TABLE xxup.xxup_rim_fund_acctg_info
(
    item_key VARCHAR2(50)
   ,line_no NUMBER(2)
   ,funding_source VARCHAR2(30)
   ,description VARCHAR2(4000)
   ,amount NUMBER(17,2)
   ,currency VARCHAR2(3)
   ,start_date DATE
   ,end_date DATE
);

CREATE SYNONYM xxup_rim_fund_acctg_info FOR xxup.xxup_rim_fund_acctg_info;