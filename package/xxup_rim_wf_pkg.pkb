create or replace PACKAGE BODY xxup_rim_wf_pkg
IS
    c_ik_prefix CONSTANT VARCHAR2(3) := 'RIM';
    c_create_ac CONSTANT VARCHAR2(20) := 'CREATE';
    c_update_ac CONSTANT VARCHAR2(20) := 'UPDATE';
    c_close_ac CONSTANT VARCHAR2(20) := 'CLOSE';
    c_acctg_ac CONSTANT VARCHAR2(20) := 'ACCTG_UPD';
    c_for_clsout_ac CONSTANT VARCHAR2(50) := 'FOR_CLOSEOUT';
    c_clsd_out_ac CONSTANT VARCHAR2(50) := 'CLOSED_OUT';
    c_don_fin_rep_ac CONSTANT VARCHAR2(50) := 'DONE_FIN_REP';
    c_cls_rep_val_ac CONSTANT VARCHAR2(50) := 'CLOSE_REP_VALIDATED';
    c_closed_ac CONSTANT VARCHAR2(50) := 'CLOSED';
    c_completed_ac CONSTANT VARCHAR2(50) := 'COMPLETED';
    


    c_acctg_upd_ac CONSTANT VARCHAR2(50) := 'For Accounting Info Update';
    c_prep_fin_rep_ac CONSTANT VARCHAR2(50) := 'Prepare Financial Reports';
    c_val_cls_rep_ac CONSTANT VARCHAR2(50) := 'Validate Closeout Reports';
    c_for_closing_ac CONSTANT VARCHAR2(50) := 'For Closing';
    c_for_completion_ac CONSTANT VARCHAR2(50) := 'For Completion';



    c_for_clsout_ps CONSTANT VARCHAR2(50) := 'For Closeout';
    c_clsd_out_ps CONSTANT VARCHAR2(50) := 'Closed Out';
    c_don_fin_rep_ps CONSTANT VARCHAR2(50) := 'Final Financial Reports Provided';
    c_cls_rep_val_ps CONSTANT VARCHAR2(50) := 'Closeout Report Validated';
    c_closed_ps CONSTANT VARCHAR2(50) := 'Closed';
    c_completed_ps CONSTANT VARCHAR2(50) := 'Completed';


    c_module_title CONSTANT VARCHAR2(50) := 'Research Information Module';

    c_item_type VARCHAR2(100) := 'RIMWF';
    c_create_prc VARCHAR2(140) := 'XXUP_RIM_PRC'; 
    c_update_prc VARCHAR2(140) := 'XXUP_RIM_UPD_PRC';                                                          
    c_close_prc VARCHAR2(140) := 'XXUP_RIM_CLS_PRC'; 

    c_acctg_msg VARCHAR2(20) := 'ACCTG_MSG';
    c_for_clsout_msg VARCHAR2(20) := 'FOR_CLOSEOUT_MSG';
    c_don_fin_rep_msg VARCHAR2(20) := 'FINAL_FIN_REP_MSG';
    c_cls_rep_val_msg VARCHAR2(20) := 'CLSOUT_REP_VAL_MSG';
    c_closed_msg VARCHAR2(20) := 'CLOSED_MSG';
    c_complete_msg VARCHAR2(20) := 'COMPLETE_RIM_MSG';

    FUNCTION create_item_key(p_tran_no        IN VARCHAR2
                            ,p_action        IN VARCHAR2                
                            )
    RETURN VARCHAR2
    IS
        lv_item_key wf_notifications.item_key%TYPE;

        lv_exist VARCHAR2(1) := 'N';
        e_exists EXCEPTION;

        lv_last_update_item_key wf_notifications.item_key%TYPE;
        ln_last_update_ctr VARCHAR2(1000);
        lv_action_prefix VARCHAR2(1) := '';

    BEGIN   
        IF p_action = c_create_ac THEN

            lv_item_key := c_ik_prefix || '-' || p_tran_no;
            RETURN lv_item_key;
--        ELSIF p_action = c_close_ac THEN
--
--            lv_item_key := 'C-' || c_ik_prefix || '-' || p_tran_no;
--            RETURN lv_item_key;

        ELSIF p_action = c_update_ac THEN
            /*Get last update transaction's item key*/

            BEGIN
                SELECT item_key
                INTO lv_last_update_item_key
                FROM (
                    SELECT item_key
                    FROM wf_items
                    WHERE 1=1
                    AND item_type = c_item_type
                    AND item_key LIKE 'U%' || p_tran_no || '%' --
                    ORDER BY begin_date DESC
                )
                WHERE ROWNUM = 1
                ;

                SELECT SUBSTR(lv_last_update_item_key,1,1)
                INTO lv_action_prefix
                FROM DUAL;

                IF lv_action_prefix = 'U' THEN
                    /*increment update item key*/
                    SELECT MAX(SUBSTR(lv_last_update_item_key,-1,1))
                    INTO ln_last_update_ctr
                    FROM xxup.xxup_rim_header
                    WHERE transaction_no = p_tran_no
                    AND ROWNUM = 1;


                    RETURN 'U-' || c_ik_prefix || '-' || p_tran_no || '-' || to_char(ln_last_update_ctr + 1);

                ELSE --no last update transaction
                    RETURN 'U-' || c_ik_prefix || '-' || p_tran_no || '-1';
                END IF;

            EXCEPTION 
                WHEN OTHERS THEN
                    NULL;
             END;

             RETURN 'U-' || c_ik_prefix || '-' || p_tran_no || '-1';


        ELSIF p_action = c_close_ac THEN
            /*Get last update transaction's item key*/

            BEGIN

                SELECT item_key
                INTO lv_last_update_item_key
                FROM (
                    SELECT item_key
                    FROM wf_items
                    WHERE 1=1
                    AND item_type = c_item_type
                    AND item_key LIKE 'C%' || p_tran_no || '%'
                    ORDER BY begin_date DESC
                )
                WHERE ROWNUM = 1
                ;

                SELECT SUBSTR(lv_last_update_item_key,1,1)
                INTO lv_action_prefix
                FROM DUAL;

                IF lv_action_prefix = 'C' THEN
                    /*increment update item key*/
                    SELECT MAX(SUBSTR(lv_last_update_item_key,-1,1))
                    INTO ln_last_update_ctr
                    FROM xxup.xxup_rim_header
                    WHERE transaction_no = p_tran_no
                    AND ROWNUM = 1;


                    RETURN 'C-' || c_ik_prefix || '-' || p_tran_no || '-' || to_char(ln_last_update_ctr + 1);

                ELSE --no last update transaction
                    RETURN 'C-' || c_ik_prefix || '-' || p_tran_no || '-1';
                END IF;

            EXCEPTION 
                WHEN OTHERS THEN
                    NULL;
             END;

             RETURN 'C-' || c_ik_prefix  || '-' || p_tran_no || '-1';
        END IF;




    EXCEPTION

        WHEN OTHERS THEN
            raise_application_error(-20101, 'Unable to create item key for this transaction');
    END create_item_key;

    PROCEDURE set_owner_details(p_assignment_id IN NUMBER
                               ,p_emp_id       OUT NUMBER
                               ,p_emp_name     OUT VARCHAR2
                               ,p_emp_pos_name OUT VARCHAR2
                               ,p_emp_org_name OUT VARCHAR2
                               )
    IS   

    BEGIN
        SELECT  papf.full_name
              ,papf.person_id
              ,(SELECT ppd.segment1
                FROM per_position_definitions ppd
                    ,per_all_positions pap
                 WHERE pap.position_id = paaf.position_id
                 AND   ppd.position_definition_id = pap.position_definition_id) POSITION_NAME
              ,(SELECT haou.name
                FROM per_position_definitions ppd
                    ,per_all_positions pap
                    ,hr_all_organization_units haou
                 WHERE pap.position_id = paaf.position_id
                 AND   ppd.position_definition_id = pap.position_definition_id
                 AND   haou.organization_id = ppd.segment2) ORGANIZATION_NAME
        INTO p_emp_name
            ,p_emp_id
            ,p_emp_pos_name
            ,p_emp_org_name
        FROM per_all_assignments_f paaf
            ,per_all_people_f papf
        WHERE paaf.person_id = papf.person_id
        AND SYSDATE BETWEEN paaf.effective_start_date AND paaf.effective_end_date
        AND SYSDATE BETWEEN papf.effective_start_date AND papf.effective_end_date
        AND paaf.assignment_id = p_assignment_id;


    EXCEPTION
      WHEN no_data_found THEN
          raise_application_error(-20101, 'Encountered error on Set Initiator details - data not found!');
      WHEN too_many_rows THEN
          raise_application_error(-20101, 'Encountered error on Set Initiator details - too many row fetched!');
      WHEN OTHERS THEN
          raise_application_error(-20101, 'Encountered error on Set Initiator details - ' || SUBSTR(SQLERRM,0,200));
    END;


    PROCEDURE set_owner_details(p_item_key      IN  VARCHAR2
                               ,p_emp_id       OUT NUMBER
                               ,p_emp_name     OUT VARCHAR2
                               ,p_emp_pos_name OUT VARCHAR2
                               ,p_emp_org_name OUT VARCHAR2
                               )
    IS    

    BEGIN
      SELECT  papf.full_name
              ,papf.person_id
              ,(SELECT ppd.segment1
                FROM per_position_definitions ppd
                    ,per_all_positions pap
                 WHERE pap.position_id = paaf.position_id
                 AND   ppd.position_definition_id = pap.position_definition_id) POSITION_NAME
              ,(SELECT haou.name
                FROM per_position_definitions ppd
                    ,per_all_positions pap
                    ,hr_all_organization_units haou
                 WHERE pap.position_id = paaf.position_id
                 AND   ppd.position_definition_id = pap.position_definition_id
                 AND   haou.organization_id = ppd.segment2) ORGANIZATION_NAME
      INTO p_emp_name
          ,p_emp_id
          ,p_emp_pos_name
          ,p_emp_org_name
        FROM per_all_assignments_f paaf
            ,per_all_people_f papf
            ,xxup.xxup_rim_header rim            
            ,fnd_user fu
        WHERE paaf.person_id = papf.person_id
        AND papf.person_id = fu.employee_id
        AND rim.created_by = fu.user_id
        AND SYSDATE BETWEEN paaf.effective_start_date AND paaf.effective_end_date
        AND SYSDATE BETWEEN papf.effective_start_date AND papf.effective_end_date
        AND primary_flag = 'Y'
        AND rim.item_key = p_item_key;

    EXCEPTION
        WHEN no_data_found THEN
          raise_application_error(-20101, 'Encountered error on Set Initiator details - data not found!');
        WHEN too_many_rows THEN
          raise_application_error(-20101, 'Encountered error on Set Initiator details - too many row fetched!');
        WHEN OTHERS THEN
          raise_application_error(-20101, 'Encountered error on Set Initiator details - ' || SUBSTR(SQLERRM,0,200));

    END set_owner_details;


    PROCEDURE init_wf(p_tran_no IN VARCHAR2
                     ,p_item_key  IN VARCHAR2
                     ,p_process IN VARCHAR2
                     )
    IS

    lv_item_key VARCHAR2(100) := p_item_key;
    lv_wf_prc VARCHAR2(30) := c_create_prc;

    BEGIN 

        IF p_process = c_create_ac THEN
            lv_wf_prc := c_create_prc;
        ELSIF p_process = c_update_ac THEN
            lv_wf_prc := c_update_prc;
        ELSIF p_process = c_close_ac THEN
            lv_wf_prc := c_close_prc;
        END IF;


        wf_engine.createprocess(c_item_type
                               ,lv_item_key
                               ,lv_wf_prc
                               );

        wf_engine.setitemowner(c_item_type, lv_item_key, fnd_global.user_name);

        wf_engine.setitemattrtext(c_item_type
                                 ,lv_item_key
                                 ,'OWNER'
                                 ,fnd_global.user_name
                                 );

        wf_engine.setitemattrtext(c_item_type
                                 ,lv_item_key
                                 ,'TRANSACTION_NO'
                                 ,p_tran_no
                                 );

        wf_engine.startprocess(c_item_type, lv_item_key);
    
        UPDATE xxup.xxup_rim_header
        SET approval_status = 'For Approval'
        WHERE item_key = lv_item_key;

        UPDATE xxup.xxup_per_ps_action_history hist
        SET action_date = SYSDATE
        WHERE item_key = lv_item_key
        AND action = 'Submit';


        COMMIT;

    EXCEPTION
      WHEN OTHERS THEN
        raise_application_error(-20101, 'Encountered error on init_wf - ' || SUBSTR(SQLERRM,0,200));

    END init_wf;


    PROCEDURE set_attributes(itemtype  IN VARCHAR2
                            ,l_itemkey IN VARCHAR2
                            ,actid     IN VARCHAR2
                            ,funcmode  IN VARCHAR2
                            ,resultout OUT VARCHAR2)
    IS
        lv_fyi_title VARCHAR2(1000);
        lv_owner VARCHAR2(100);
        lv_acctg_staff fnd_user.user_name%TYPE;
        lv_res_off_staff fnd_user.user_name%TYPE;
        lv_tran_no VARCHAR2(100);

        lv_research_title VARCHAR2(150);

        lv_approver_name VARCHAR2(150);


        lv_sup_name VARCHAR2(100);
        ln_sup_person_id NUMBER;
        ln_sup_assignment_id NUMBER;
        lv_sup_pos_name VARCHAR2(150);

        lv_emp_name VARCHAR2(100);
        ln_emp_id NUMBER;
        lv_emp_pos_name VARCHAR2(150);
        lv_emp_org_name VARCHAR2(150);


        ln_total_approver_count NUMBER;                    
        ln_approver_ctr NUMBER := 0;
        lv_appr_user_name VARCHAR2(100);

        lv_error VARCHAR2(2000);
        lv_action VARCHAR2(50);


    BEGIN


        lv_owner := wf_engine.getitemattrtext(itemtype
                                            ,l_itemkey
                                            ,'OWNER'
                                            );

        SELECT transaction_no
              ,DECODE(SUBSTR(item_key,1,1), 'U', c_update_ac, 'C', c_close_ac, c_create_ac) action
        INTO lv_tran_no
            ,lv_action
        FROM xxup_rim_header hd
        WHERE item_key = l_itemkey;

--        lv_tran_no := wf_engine.getitemattrtext(itemtype
--                                            ,l_itemkey
--                                            ,'TRANSACTION_NO'
--                                            );

        IF funcmode = 'RUN' THEN

            BEGIN
                SELECT research_title
                INTO lv_research_title
                FROM xxup.xxup_rim_header hd
                WHERE item_key = l_itemkey;
            EXCEPTION
                WHEN OTHERS THEN
                    raise_application_error(-20101, 'Error getting Research Title');
            END;

            /*Set 1st approver notification attributes*/
            BEGIN
                SELECT full_name
                INTO lv_approver_name
                FROM hr.per_all_people_f papf
                WHERE SYSDATE BETWEEN papf.effective_start_date AND papf.effective_end_date
                  AND person_type_id = 1126
                  AND papf.person_id = (SELECT to_employee_id
                                        FROM xxup.xxup_per_ps_action_history
                                        WHERE action = 'Pending' 
                                        AND approver_no = 2 --
                                        AND item_key = l_itemkey);
            EXCEPTION
                WHEN OTHERS THEN
                    raise_application_error(-20101, 'Error getting approver name');
            END;

            BEGIN

                BEGIN
                    SELECT user_name
                    INTO lv_appr_user_name
                    FROM fnd_user
                    WHERE employee_id = (SELECT to_employee_id
                                        FROM xxup.xxup_per_ps_action_history
                                        WHERE item_key = l_itemkey
                                        AND approver_no = 2 --
                                        AND action = 'Pending');

                EXCEPTION
                    WHEN OTHERS THEN
                        raise_application_error(-20101, 'Error getting approver username');
                END;


                BEGIN 

                    /*Set employee details*/

                    set_owner_details(p_item_key     => l_itemkey
                                     ,p_emp_id       => ln_emp_id
                                     ,p_emp_name     => lv_emp_name
                                     ,p_emp_pos_name => lv_emp_pos_name
                                     ,p_emp_org_name => lv_emp_org_name
                    );




                EXCEPTION
                    WHEN OTHERS THEN
                        NULL;
                END;

                --set attribute for SUBMIT Action
                IF lv_action = c_create_ac THEN

                    BEGIN

                        SELECT user_name
                            INTO lv_acctg_staff
                            FROM fnd_user
                            WHERE employee_id = (SELECT to_employee_id
                                                FROM xxup.xxup_per_ps_action_history
                                                WHERE item_key = l_itemkey
                                                AND action = c_acctg_upd_ac);
                    EXCEPTION
                        WHEN OTHERS THEN
                            raise_application_error(-20101, 'Error getting accounting staff username');
                    END;
                END IF;

                --set attribute for CLOSE Action
                IF lv_action = c_close_ac THEN
                    BEGIN

                        SELECT user_name
                            INTO lv_acctg_staff
                            FROM fnd_user
                            WHERE employee_id = (SELECT to_employee_id
                                                FROM xxup.xxup_per_ps_action_history
                                                WHERE item_key = l_itemkey
                                                AND action = c_prep_fin_rep_ac);

                    EXCEPTION
                        WHEN OTHERS THEN
                            raise_application_error(-20101, 'Error getting accounting staff username');
                    END;

                    BEGIN

                        SELECT user_name
                            INTO lv_res_off_staff
                            FROM fnd_user
                            WHERE employee_id = (SELECT to_employee_id
                                                FROM xxup.xxup_per_ps_action_history
                                                WHERE item_key = l_itemkey
                                                AND action = c_for_completion_ac);

                    EXCEPTION
                        WHEN OTHERS THEN
                            raise_application_error(-20101, 'Error getting research office staff username');
                    END;
                END IF;


--                wf_engine.setitemattrtext(itemtype
--                                         ,l_itemkey
--                                         ,'TRANSACTION_NO'
--                                         ,lv_tran_no
--                                        );

                wf_engine.setitemattrtext(itemtype
                                         ,l_itemkey
                                         ,'FROM'
                                         ,fnd_global.user_name
                                        );


                wf_engine.setitemattrtext(itemtype
                                        ,l_itemkey
                                        ,'APPROVER'
                                        ,lv_appr_user_name);


                wf_engine.setitemattrtext(itemtype
                                        ,l_itemkey
                                        ,'TITLE'
                                        ,c_module_title || ' - '
                                      || lv_research_title
                                      || ' submitted by '
                                      || lv_emp_name    
                                      || ' needs your Approval'
                                         );

                wf_engine.setitemattrtext(itemtype
                                        ,l_itemkey
                                        ,'APPR_BODY_RN'
                                        ,'JSP:/OA_HTML/OA.jsp?OAFunc=XXUP_RIM_APPR_DETAILS_RN&pSequenceNo='
                                      || lv_tran_no
                                      || '&urlParam=Approval'
                                        || '&pItemKey='
                                        || l_itemkey
                                        );

                --@TODO: Attachment region
                -- wf_engine.setitemattrtext(itemtype
                --                         ,l_itemkey
                --                         ,'ATTACHMENT_RN'
                --                         ,'JSP:/OA_HTML/OA.jsp?OAFunc=XXUP_HR_PS_INST_ATT_REV_RN&pSequenceNo='
                --                         || lv_tran_no
                --                         || '&pItemKey='
                --                         || l_itemkey
                --                         );


                wf_engine.setitemattrtext(itemtype
                                        ,l_itemkey
                                        ,'#HISTORY'
                                        ,'JSP:/OA_HTML/OA.jsp?OAFunc=XXUP_HR_PS_INST_HIST_RN&pSequenceNo='
                                        || lv_tran_no
                                        || '&pItemKey='
                                        || l_itemkey
                                        );                        


                wf_engine.setitemattrtext(itemtype
                                        ,l_itemkey
                                        ,'APPROVER_COUNTER'
                                        ,2); --1 is reserved for submitter


                wf_engine.setitemattrtext(itemtype
                                        ,l_itemkey
                                        ,'APPROVER_NAME'
                                        ,lv_approver_name);


            END;

            BEGIN
                SELECT COUNT(1)
                INTO ln_total_approver_count
                FROM xxup.xxup_per_ps_action_history psah
                WHERE item_key = l_itemkey
                AND action = 'Pending';

                wf_engine.setitemattrtext(itemtype
                                        ,l_itemkey
                                        ,'TOTAL_APPROVER_COUNT'
                                        ,ln_total_approver_count);

            EXCEPTION 
                WHEN OTHERS THEN
                      wf_engine.setitemattrtext(itemtype
                                        ,l_itemkey
                                        ,'TOTAL_APPROVER_COUNT'
                                        ,0);

            END;


            wf_engine.setitemattrtext(itemtype
                                     ,l_itemkey
                                     ,'FROM'
                                     ,fnd_global.user_name
                                    );


            wf_engine.setitemattrtext(itemtype
                                    ,l_itemkey
                                    ,'FYI_TITLE'
                                    ,c_module_title || ' - '
                                    || lv_research_title
                                    || ' has been submitted for approval of '
                                    || lv_approver_name
                                    );


            wf_engine.setitemattrtext(itemtype
                                     ,l_itemkey
                                     ,'FYI_BODY_RN'
                                     ,'JSP:/OA_HTML/OA.jsp?OAFunc=XXUP_RIM_APPR_DETAILS_RN&pSequenceNo=' 
                                    || lv_tran_no
                                    || '&pItemKey='
                                    || l_itemkey
                                    );

            wf_engine.setitemattrtext(itemtype
                                     ,l_itemkey
                                     ,'PROJECT_NAME'
                                     ,lv_research_title
                                    );


            wf_engine.setitemattrtext(itemtype
                                     ,l_itemkey
                                     ,'OWNER_NAME'
                                     ,lv_emp_name
                                    );

            wf_engine.setitemattrtext(itemtype
                                     ,l_itemkey
                                     ,'ACCTG_STAFF'
                                     ,lv_acctg_staff
                                    );
            wf_engine.setitemattrtext(itemtype
                                     ,l_itemkey
                                     ,'RIM_OFC_STAFF'
                                     ,lv_res_off_staff
                                    );                        



            wf_engine.setitemattrtext(itemtype
                                     ,l_itemkey
                                     ,'UPD_INFO_URL'
                                     ,'JSP:/OA_HTML/OA.jsp?OAFunc=XXUP_RIM_REQ_PG' || 
                                      '&urlParam='    || c_acctg_ac ||
                                      '&pSequenceNo=' || lv_tran_no ||
                                      '&pItemKey='    || l_itemkey
                                    );

            wf_engine.setitemattrtext(itemtype
                                     ,l_itemkey
                                     ,'CLOSE_STATUS_URL'
                                     ,'JSP:/OA_HTML/OA.jsp?OAFunc=XXUP_RIM_REQ_PG' || 
                                      '&urlParam='    || c_for_clsout_ac ||
                                      '&pSequenceNo=' || lv_tran_no ||
                                      '&pItemKey='    || l_itemkey
                                    );                        





            resultout := '';

        END IF;




    EXCEPTION
      WHEN OTHERS THEN
        lv_error := SQLERRM;

        raise_application_error(-20101, 'error setting up attributes: ' || SQLERRM);


    END set_attributes;

    PROCEDURE init_approvers(p_assignment_id IN VARCHAR2
                            ,p_tran_no       IN VARCHAR2
                            ,p_action        IN VARCHAR2
                            ,p_item_key      OUT VARCHAR2
                            )
    IS
        lv_sup_name VARCHAR2(100);
        ln_sup_person_id NUMBER;
        ln_sup_assignment_id NUMBER;
        lv_sup_pos_name VARCHAR2(150);
        lv_sup_org_name VARCHAR2(150);

        is_principal_unit VARCHAR2(1) := 'N';

        lv_emp_name VARCHAR2(100);
        ln_emp_id NUMBER;
        lv_emp_pos_name VARCHAR2(150);
        lv_emp_org_name VARCHAR2(150);
        ln_emp_asg_id NUMBER;   


        --lv_owner VARCHAR2(100);
        lv_tran_no VARCHAR2(100);


        ln_from_id NUMBER;
        lv_from_name VARCHAR2(100);
        lv_from_pos_name VARCHAR2(150);
        lv_from_org_name VARCHAR2(150);


        ln_to_id NUMBER;
        lv_to_name VARCHAR2(100);
        lv_to_pos_name VARCHAR2(150);
        lv_to_org_name VARCHAR2(150);

        ln_total_approver_count NUMBER;                    
        ln_approver_ctr NUMBER := 0;
        lv_appr_user_name VARCHAR2(100);

        ln_res_recipient_id NUMBER;
        ln_acctg_recipient_id NUMBER;
        lv_rim_org_name hr_all_organization_units.name%TYPE := '';

        lv_exist VARCHAR2(1) := 'N';

        lv_error VARCHAR2(1000) := '';

        lv_item_key wf_notifications.item_key%TYPE;



    BEGIN

        /*create item key*/

          lv_item_key := create_item_key(p_tran_no        => p_tran_no
                                        ,p_action        => NVL(p_action, c_create_ac)
                         );

            INSERT INTO test_tbl
            VALUES(lv_item_key, CURRENT_TIMESTAMP);


        BEGIN
          SELECT 'Y'
          INTO lv_exist
          FROM xxup.xxup_per_ps_action_history
          WHERE ROWNUM = 1
          AND item_key = lv_item_key;

            IF lv_exist = 'Y' THEN
                DELETE
                  FROM xxup.xxup_per_ps_action_history
                  WHERE 1=1
                  AND item_key = lv_item_key;

            END IF;
        EXCEPTION
            WHEN no_data_found THEN
                NULL;
        END;






        BEGIN

--            lv_tran_no := wf_engine.getitemattrtext(c_item_type
--                                                    ,lv_item_key
--                                                    ,'TRANSACTION_NO');

            /*Set employee details*/
            set_owner_details(p_assignment_id  => p_assignment_id
                             ,p_emp_id       => ln_emp_id
                             ,p_emp_name     => lv_emp_name
                             ,p_emp_pos_name => lv_emp_pos_name
                             ,p_emp_org_name => lv_emp_org_name
            );


            /*Submit employee action line*/
            ln_approver_ctr := ln_approver_ctr + 1;

            INSERT INTO xxup.xxup_per_ps_action_history (
                            sequence_no,
                            approver_no,
                            to_employee_id,
                            to_employee_name,
                            to_position_name,
                            to_org_name,
                            action,
                            item_key
                        ) VALUES (
                            lv_tran_no,
                            ln_approver_ctr,
                            ln_emp_id,
                            lv_emp_name,
                            lv_emp_pos_name,
                            lv_emp_org_name,
                            'Submit',
                            lv_item_key
                        );



          EXCEPTION
              WHEN OTHERS THEN
                    ROLLBACK;
                  raise_application_error(-20101,'Error init approver line');

          END;

         ln_from_id := ln_emp_id;
         lv_from_name := lv_emp_name;
         lv_from_pos_name := lv_emp_pos_name;
         lv_from_org_name := lv_emp_org_name;


        IF p_action = c_create_ac THEN


            /*Set Research Office Recipient approver*/        
            ln_approver_ctr := ln_approver_ctr + 1;

            BEGIN
                SELECT name
                INTO lv_rim_org_name
                FROM hr_all_organization_units haou
                    ,hr_soft_coding_keyflex hsck
                    ,per_all_assignments_f paaf
                WHERE haou.organization_id = to_number(hsck.segment1)
                AND paaf.soft_coding_keyflex_id = hsck.soft_coding_keyflex_id
                AND SYSDATE BETWEEN paaf.effective_start_date AND paaf.effective_end_date
                AND paaf.assignment_id = p_assignment_id;
            EXCEPTION
                WHEN OTHERS THEN
                    raise_application_error(-20101, 'Encountered error on Initializing approvers - Org not found!');
            END;


            SELECT apps.xxup_hrms_utilities_pkg.get_up_approver(lv_rim_org_name,
                                                                'UP_HRD_APPROVAL_ROLES',
                                                                'RIM Research Office',
                                                                trunc(sysdate)) hr
             INTO ln_res_recipient_id
             FROM DUAL;



             /*Additional check if the recipient is the same as creator*/
            IF ln_res_recipient_id <>ln_emp_id
            THEN
                BEGIN
                 SELECT papf.full_name
                       ,papf.person_id
                       ,(SELECT ppd.segment1
                         FROM per_position_definitions ppd
                             ,per_all_positions pap
                         WHERE pap.position_id = paaf.position_id
                         AND   ppd.position_definition_id = pap.position_definition_id) POSITION_NAME
                      ,(SELECT haou.name
                        FROM per_position_definitions ppd
                            ,per_all_positions pap
                            ,hr_all_organization_units haou
                         WHERE pap.position_id = paaf.position_id
                         AND   ppd.position_definition_id = pap.position_definition_id
                         AND   haou.organization_id = ppd.segment2) ORGANIZATION_NAME
                 INTO lv_to_name
                     ,ln_to_id
                     ,lv_to_pos_name
                     ,lv_to_org_name
                 FROM per_all_assignments_f paaf
                     ,per_all_people_f papf
                 WHERE paaf.person_id = papf.person_id
                 AND SYSDATE BETWEEN paaf.effective_start_date AND paaf.effective_end_date
                 AND SYSDATE BETWEEN papf.effective_start_date AND papf.effective_end_date
                 AND papf.person_id = ln_res_recipient_id;
                EXCEPTION
                    WHEN no_data_found THEN
                        raise_application_error(-20101, 'Encountered error on Initializing approvers - Research Office Recipient not properly setup');
                    WHEN too_many_rows THEN
                        raise_application_error(-20101, 'Encountered error on Initializing approvers - Too many Research Office Recipient defined');
                    WHEN OTHERS THEN
                        raise_application_error(-20101, 'Encountered error on Initializing approvers - Unable to retrieve Research Office Recipient data');
                END;


                INSERT INTO xxup.xxup_per_ps_action_history (
                            item_key,
                            sequence_no,
                            approver_no,
                            from_employee_id,
                            from_employee_name,
                            from_position_name,
                            from_org_name,
                            to_employee_id,
                            to_employee_name,
                            to_position_name,
                            to_org_name,
                            action               
                        ) VALUES (
                            lv_item_key,
                            lv_tran_no,
                            ln_approver_ctr,
                            ln_from_id,
                            lv_from_name,
                            lv_from_pos_name,
                            lv_from_org_name,
                            ln_to_id,
                            lv_to_name,
                            lv_to_pos_name,
                            lv_to_org_name,
                            'Pending'
                        );

            END IF;

            ln_from_id := ln_to_id;
            lv_from_name := lv_to_name;
            lv_from_pos_name := lv_to_pos_name;
            lv_from_org_name := lv_to_org_name;





            /*Set Accounting Office Recipient approver*/        
            ln_approver_ctr := ln_approver_ctr + 1;

            SELECT apps.xxup_hrms_utilities_pkg.get_up_approver(lv_rim_org_name,
                                                                'UP_HRD_APPROVAL_ROLES',
                                                                'RIM Accounting Office',
                                                                trunc(sysdate)) hr
             INTO ln_acctg_recipient_id
             FROM DUAL;


             /*Additional check if the recipient is the same as creator*/
            IF ln_acctg_recipient_id <> ln_emp_id
            THEN
                BEGIN
                 SELECT papf.full_name
                       ,papf.person_id
                       ,(SELECT ppd.segment1
                         FROM per_position_definitions ppd
                             ,per_all_positions pap
                         WHERE pap.position_id = paaf.position_id
                         AND   ppd.position_definition_id = pap.position_definition_id) POSITION_NAME
                      ,(SELECT haou.name
                        FROM per_position_definitions ppd
                            ,per_all_positions pap
                            ,hr_all_organization_units haou
                         WHERE pap.position_id = paaf.position_id
                         AND   ppd.position_definition_id = pap.position_definition_id
                         AND   haou.organization_id = ppd.segment2) ORGANIZATION_NAME
                 INTO lv_to_name
                     ,ln_to_id
                     ,lv_to_pos_name
                     ,lv_to_org_name
                 FROM per_all_assignments_f paaf
                     ,per_all_people_f papf
                 WHERE paaf.person_id = papf.person_id
                 AND SYSDATE BETWEEN paaf.effective_start_date AND paaf.effective_end_date
                 AND SYSDATE BETWEEN papf.effective_start_date AND papf.effective_end_date
                 AND papf.person_id = ln_acctg_recipient_id;
                EXCEPTION
                    WHEN no_data_found THEN
                        raise_application_error(-20101, 'Encountered error on Initializing approvers - Accounting Office Recipient not properly setup');
                    WHEN too_many_rows THEN
                        raise_application_error(-20101, 'Encountered error on Initializing approvers - Too many Accounting Office Recipient defined');
                    WHEN OTHERS THEN
                        raise_application_error(-20101, 'Encountered error on Initializing approvers - Unable to retrieve Accounting Office Recipient data');
                END;


                INSERT INTO xxup.xxup_per_ps_action_history (
                            item_key,
                            sequence_no,
                            approver_no,
                            from_employee_id,
                            from_employee_name,
                            from_position_name,
                            from_org_name,
                            to_employee_id,
                            to_employee_name,
                            to_position_name,
                            to_org_name,
                            action               
                        ) VALUES (
                            lv_item_key,
                            lv_tran_no,
                            ln_approver_ctr,
                            ln_from_id,
                            lv_from_name,
                            lv_from_pos_name,
                            lv_from_org_name,
                            ln_to_id,
                            lv_to_name,
                            lv_to_pos_name,
                            lv_to_org_name,
                            c_acctg_upd_ac
                        );

            END IF;

            p_item_key :=  lv_item_key; 
            COMMIT;


        ELSIF p_action = c_update_ac THEN
            /*Set Research Office Recipient approver*/        
            ln_approver_ctr := ln_approver_ctr + 1;

            BEGIN
                SELECT name
                INTO lv_rim_org_name
                FROM hr_all_organization_units haou
                    ,hr_soft_coding_keyflex hsck
                    ,per_all_assignments_f paaf
                WHERE haou.organization_id = to_number(hsck.segment1)
                AND paaf.soft_coding_keyflex_id = hsck.soft_coding_keyflex_id
                AND SYSDATE BETWEEN paaf.effective_start_date AND paaf.effective_end_date
                AND paaf.assignment_id = p_assignment_id;
            EXCEPTION
                WHEN OTHERS THEN
                    raise_application_error(-20101, 'Encountered error on Initializing approvers - Org not found!');
            END;


            SELECT apps.xxup_hrms_utilities_pkg.get_up_approver(lv_rim_org_name,
                                                                'UP_HRD_APPROVAL_ROLES',
                                                                'RIM Research Office',
                                                                trunc(sysdate)) hr
             INTO ln_res_recipient_id
             FROM DUAL;



             /*Additional check if the recipient is the same as creator*/
            IF ln_res_recipient_id <>ln_emp_id
            THEN
                BEGIN
                 SELECT papf.full_name
                       ,papf.person_id
                       ,(SELECT ppd.segment1
                         FROM per_position_definitions ppd
                             ,per_all_positions pap
                         WHERE pap.position_id = paaf.position_id
                         AND   ppd.position_definition_id = pap.position_definition_id) POSITION_NAME
                      ,(SELECT haou.name
                        FROM per_position_definitions ppd
                            ,per_all_positions pap
                            ,hr_all_organization_units haou
                         WHERE pap.position_id = paaf.position_id
                         AND   ppd.position_definition_id = pap.position_definition_id
                         AND   haou.organization_id = ppd.segment2) ORGANIZATION_NAME
                 INTO lv_to_name
                     ,ln_to_id
                     ,lv_to_pos_name
                     ,lv_to_org_name
                 FROM per_all_assignments_f paaf
                     ,per_all_people_f papf
                 WHERE paaf.person_id = papf.person_id
                 AND SYSDATE BETWEEN paaf.effective_start_date AND paaf.effective_end_date
                 AND SYSDATE BETWEEN papf.effective_start_date AND papf.effective_end_date
                 AND papf.person_id = ln_res_recipient_id;
                EXCEPTION
                    WHEN no_data_found THEN
                        raise_application_error(-20101, 'Encountered error on Initializing approvers - Research Office Recipient not properly setup');
                    WHEN too_many_rows THEN
                        raise_application_error(-20101, 'Encountered error on Initializing approvers - Too many Research Office Recipient defined');
                    WHEN OTHERS THEN
                        raise_application_error(-20101, 'Encountered error on Initializing approvers - Unable to retrieve Research Office Recipient data');
                END;


                INSERT INTO xxup.xxup_per_ps_action_history (
                            item_key,
                            sequence_no,
                            approver_no,
                            from_employee_id,
                            from_employee_name,
                            from_position_name,
                            from_org_name,
                            to_employee_id,
                            to_employee_name,
                            to_position_name,
                            to_org_name,
                            action               
                        ) VALUES (
                            lv_item_key,
                            lv_tran_no,
                            ln_approver_ctr,
                            ln_from_id,
                            lv_from_name,
                            lv_from_pos_name,
                            lv_from_org_name,
                            ln_to_id,
                            lv_to_name,
                            lv_to_pos_name,
                            lv_to_org_name,
                            'Pending'
                        );

            END IF;

            ln_from_id := ln_to_id;
            lv_from_name := lv_to_name;
            lv_from_pos_name := lv_to_pos_name;
            lv_from_org_name := lv_to_org_name;


            /*Set Accounting Office Recipient approver*/        
            ln_approver_ctr := ln_approver_ctr + 1;

            SELECT apps.xxup_hrms_utilities_pkg.get_up_approver(lv_rim_org_name,
                                                                'UP_HRD_APPROVAL_ROLES',
                                                                'RIM Accounting Office',
                                                                trunc(sysdate)) hr
             INTO ln_acctg_recipient_id
             FROM DUAL;



             /*Additional check if the recipient is the same as creator*/
            IF ln_acctg_recipient_id <> ln_emp_id
            THEN
                BEGIN
                 SELECT papf.full_name
                       ,papf.person_id
                       ,(SELECT ppd.segment1
                         FROM per_position_definitions ppd
                             ,per_all_positions pap
                         WHERE pap.position_id = paaf.position_id
                         AND   ppd.position_definition_id = pap.position_definition_id) POSITION_NAME
                      ,(SELECT haou.name
                        FROM per_position_definitions ppd
                            ,per_all_positions pap
                            ,hr_all_organization_units haou
                         WHERE pap.position_id = paaf.position_id
                         AND   ppd.position_definition_id = pap.position_definition_id
                         AND   haou.organization_id = ppd.segment2) ORGANIZATION_NAME
                 INTO lv_to_name
                     ,ln_to_id
                     ,lv_to_pos_name
                     ,lv_to_org_name
                 FROM per_all_assignments_f paaf
                     ,per_all_people_f papf
                 WHERE paaf.person_id = papf.person_id
                 AND SYSDATE BETWEEN paaf.effective_start_date AND paaf.effective_end_date
                 AND SYSDATE BETWEEN papf.effective_start_date AND papf.effective_end_date
                 AND papf.person_id = ln_acctg_recipient_id;
                EXCEPTION
                    WHEN no_data_found THEN
                        raise_application_error(-20101, 'Encountered error on Initializing approvers - Accounting Office Recipient not properly setup');
                    WHEN too_many_rows THEN
                        raise_application_error(-20101, 'Encountered error on Initializing approvers - Too many Accounting Office Recipient defined');
                    WHEN OTHERS THEN
                        raise_application_error(-20101, 'Encountered error on Initializing approvers - Unable to retrieve Accounting Office Recipient data');
                END;


                INSERT INTO xxup.xxup_per_ps_action_history (
                            item_key,
                            sequence_no,
                            approver_no,
                            from_employee_id,
                            from_employee_name,
                            from_position_name,
                            from_org_name,
                            to_employee_id,
                            to_employee_name,
                            to_position_name,
                            to_org_name,
                            action               
                        ) VALUES (
                            lv_item_key,
                            lv_tran_no,
                            ln_approver_ctr,
                            ln_from_id,
                            lv_from_name,
                            lv_from_pos_name,
                            lv_from_org_name,
                            ln_to_id,
                            lv_to_name,
                            lv_to_pos_name,
                            lv_to_org_name,
                            'Pending'--
                        );



            END IF;

            p_item_key :=  lv_item_key; 
            COMMIT;

        ELSIF p_action = c_close_ac THEN
            /*Set Research Office Recipient approver*/        
            ln_approver_ctr := ln_approver_ctr + 1;

            BEGIN
                SELECT name
                INTO lv_rim_org_name
                FROM hr_all_organization_units haou
                    ,hr_soft_coding_keyflex hsck
                    ,per_all_assignments_f paaf
                WHERE haou.organization_id = to_number(hsck.segment1)
                AND paaf.soft_coding_keyflex_id = hsck.soft_coding_keyflex_id
                AND SYSDATE BETWEEN paaf.effective_start_date AND paaf.effective_end_date
                AND paaf.assignment_id = p_assignment_id;
            EXCEPTION
                WHEN OTHERS THEN
                    raise_application_error(-20101, 'Encountered error on Initializing approvers - Org not found!');
            END;


            SELECT apps.xxup_hrms_utilities_pkg.get_up_approver(lv_rim_org_name,
                                                                'UP_HRD_APPROVAL_ROLES',
                                                                'RIM Research Office',
                                                                trunc(sysdate)) hr
             INTO ln_res_recipient_id
             FROM DUAL;



             /*Additional check if the recipient is the same as creator*/
            IF ln_res_recipient_id <>ln_emp_id
            THEN
                BEGIN
                 SELECT papf.full_name
                       ,papf.person_id
                       ,(SELECT ppd.segment1
                         FROM per_position_definitions ppd
                             ,per_all_positions pap
                         WHERE pap.position_id = paaf.position_id
                         AND   ppd.position_definition_id = pap.position_definition_id) POSITION_NAME
                      ,(SELECT haou.name
                        FROM per_position_definitions ppd
                            ,per_all_positions pap
                            ,hr_all_organization_units haou
                         WHERE pap.position_id = paaf.position_id
                         AND   ppd.position_definition_id = pap.position_definition_id
                         AND   haou.organization_id = ppd.segment2) ORGANIZATION_NAME
                 INTO lv_to_name
                     ,ln_to_id
                     ,lv_to_pos_name
                     ,lv_to_org_name
                 FROM per_all_assignments_f paaf
                     ,per_all_people_f papf
                 WHERE paaf.person_id = papf.person_id
                 AND SYSDATE BETWEEN paaf.effective_start_date AND paaf.effective_end_date
                 AND SYSDATE BETWEEN papf.effective_start_date AND papf.effective_end_date
                 AND papf.person_id = ln_res_recipient_id;
                EXCEPTION
                    WHEN no_data_found THEN
                        raise_application_error(-20101, 'Encountered error on Initializing approvers - Research Office Recipient not properly setup');
                    WHEN too_many_rows THEN
                        raise_application_error(-20101, 'Encountered error on Initializing approvers - Too many Research Office Recipient defined');
                    WHEN OTHERS THEN
                        raise_application_error(-20101, 'Encountered error on Initializing approvers - Unable to retrieve Research Office Recipient data');
                END;


                INSERT INTO xxup.xxup_per_ps_action_history (
                            item_key,
                            sequence_no,
                            approver_no,
                            from_employee_id,
                            from_employee_name,
                            from_position_name,
                            from_org_name,
                            to_employee_id,
                            to_employee_name,
                            to_position_name,
                            to_org_name,
                            action               
                        ) VALUES (
                            lv_item_key,
                            lv_tran_no,
                            ln_approver_ctr,
                            ln_from_id,
                            lv_from_name,
                            lv_from_pos_name,
                            lv_from_org_name,
                            ln_to_id,
                            lv_to_name,
                            lv_to_pos_name,
                            lv_to_org_name,
                            'Pending'
                        );

            END IF;

            ln_from_id := ln_to_id;
            lv_from_name := lv_to_name;
            lv_from_pos_name := lv_to_pos_name;
            lv_from_org_name := lv_to_org_name;


            /*Set Accounting Office Recipient approver*/        
            ln_approver_ctr := ln_approver_ctr + 1;

            SELECT apps.xxup_hrms_utilities_pkg.get_up_approver(lv_rim_org_name,
                                                                'UP_HRD_APPROVAL_ROLES',
                                                                'RIM Accounting Office',
                                                                trunc(sysdate)) hr
             INTO ln_acctg_recipient_id
             FROM DUAL;



             /*Additional check if the recipient is the same as creator*/
            IF ln_acctg_recipient_id <> ln_emp_id
            THEN
                BEGIN
                 SELECT papf.full_name
                       ,papf.person_id
                       ,(SELECT ppd.segment1
                         FROM per_position_definitions ppd
                             ,per_all_positions pap
                         WHERE pap.position_id = paaf.position_id
                         AND   ppd.position_definition_id = pap.position_definition_id) POSITION_NAME
                      ,(SELECT haou.name
                        FROM per_position_definitions ppd
                            ,per_all_positions pap
                            ,hr_all_organization_units haou
                         WHERE pap.position_id = paaf.position_id
                         AND   ppd.position_definition_id = pap.position_definition_id
                         AND   haou.organization_id = ppd.segment2) ORGANIZATION_NAME
                 INTO lv_to_name
                     ,ln_to_id
                     ,lv_to_pos_name
                     ,lv_to_org_name
                 FROM per_all_assignments_f paaf
                     ,per_all_people_f papf
                 WHERE paaf.person_id = papf.person_id
                 AND SYSDATE BETWEEN paaf.effective_start_date AND paaf.effective_end_date
                 AND SYSDATE BETWEEN papf.effective_start_date AND papf.effective_end_date
                 AND papf.person_id = ln_acctg_recipient_id;
                EXCEPTION
                    WHEN no_data_found THEN
                        raise_application_error(-20101, 'Encountered error on Initializing approvers - Accounting Office Recipient not properly setup');
                    WHEN too_many_rows THEN
                        raise_application_error(-20101, 'Encountered error on Initializing approvers - Too many Accounting Office Recipient defined');
                    WHEN OTHERS THEN
                        raise_application_error(-20101, 'Encountered error on Initializing approvers - Unable to retrieve Accounting Office Recipient data');
                END;


                INSERT INTO xxup.xxup_per_ps_action_history (
                            item_key,
                            sequence_no,
                            approver_no,
                            from_employee_id,
                            from_employee_name,
                            from_position_name,
                            from_org_name,
                            to_employee_id,
                            to_employee_name,
                            to_position_name,
                            to_org_name,
                            action               
                        ) VALUES (
                            lv_item_key,
                            lv_tran_no,
                            ln_approver_ctr,
                            ln_from_id,
                            lv_from_name,
                            lv_from_pos_name,
                            lv_from_org_name,
                            ln_to_id,
                            lv_to_name,
                            lv_to_pos_name,
                            lv_to_org_name,
                            'Pending'--
                        );


            ln_from_id := ln_to_id;
            lv_from_name := lv_to_name;
            lv_from_pos_name := lv_to_pos_name;
            lv_from_org_name := lv_to_org_name;

            ln_approver_ctr := ln_approver_ctr + 1; 

            --Financial Reports provided
            INSERT INTO xxup.xxup_per_ps_action_history (
                            item_key,
                            sequence_no,
                            approver_no,
                            from_employee_id,
                            from_employee_name,
                            from_position_name,
                            from_org_name,
                            to_employee_id,
                            to_employee_name,
                            to_position_name,
                            to_org_name,
                            action               
                        ) VALUES (
                            lv_item_key,
                            lv_tran_no,
                            ln_approver_ctr,
                            ln_from_id,
                            lv_from_name,
                            lv_from_pos_name,
                            lv_from_org_name,
                            ln_to_id,
                            lv_to_name,
                            lv_to_pos_name,
                            lv_to_org_name,
                            c_prep_fin_rep_ac
                        );

            ln_approver_ctr := ln_approver_ctr + 1;             
            --Closeout reports validate
            INSERT INTO xxup.xxup_per_ps_action_history (
                            item_key,
                            sequence_no,
                            approver_no,
                            from_employee_id,
                            from_employee_name,
                            from_position_name,
                            from_org_name,
                            to_employee_id,
                            to_employee_name,
                            to_position_name,
                            to_org_name,
                            action               
                        ) VALUES (
                            lv_item_key,
                            lv_tran_no,
                            ln_approver_ctr,
                            ln_from_id,
                            lv_from_name,
                            lv_from_pos_name,
                            lv_from_org_name,
                            ln_to_id,
                            lv_to_name,
                            lv_to_pos_name,
                            lv_to_org_name,
                            c_val_cls_rep_ac
                        ); 


             ln_approver_ctr := ln_approver_ctr + 1; 

            --For Closing (Acctg)
            INSERT INTO xxup.xxup_per_ps_action_history (
                            item_key,
                            sequence_no,
                            approver_no,
                            from_employee_id,
                            from_employee_name,
                            from_position_name,
                            from_org_name,
                            to_employee_id,
                            to_employee_name,
                            to_position_name,
                            to_org_name,
                            action               
                        ) VALUES (
                            lv_item_key,
                            lv_tran_no,
                            ln_approver_ctr,
                            ln_from_id,
                            lv_from_name,
                            lv_from_pos_name,
                            lv_from_org_name,
                            ln_to_id,
                            lv_to_name,
                            lv_to_pos_name,
                            lv_to_org_name,
                            c_for_closing_ac
                        ); 


            ln_approver_ctr := ln_approver_ctr + 1; 
            --For Closing (RIM office)
            SELECT apps.xxup_hrms_utilities_pkg.get_up_approver(lv_rim_org_name,
                                                                'UP_HRD_APPROVAL_ROLES',
                                                                'RIM Research Office',
                                                                trunc(sysdate)) hr
             INTO ln_res_recipient_id
             FROM DUAL;


            BEGIN
             SELECT papf.full_name
                   ,papf.person_id
                   ,(SELECT ppd.segment1
                     FROM per_position_definitions ppd
                         ,per_all_positions pap
                     WHERE pap.position_id = paaf.position_id
                     AND   ppd.position_definition_id = pap.position_definition_id) POSITION_NAME
                  ,(SELECT haou.name
                    FROM per_position_definitions ppd
                        ,per_all_positions pap
                        ,hr_all_organization_units haou
                     WHERE pap.position_id = paaf.position_id
                     AND   ppd.position_definition_id = pap.position_definition_id
                     AND   haou.organization_id = ppd.segment2) ORGANIZATION_NAME
             INTO lv_to_name
                 ,ln_to_id
                 ,lv_to_pos_name
                 ,lv_to_org_name
             FROM per_all_assignments_f paaf
                 ,per_all_people_f papf
             WHERE paaf.person_id = papf.person_id
             AND SYSDATE BETWEEN paaf.effective_start_date AND paaf.effective_end_date
             AND SYSDATE BETWEEN papf.effective_start_date AND papf.effective_end_date
             AND papf.person_id = ln_res_recipient_id;
            EXCEPTION
                WHEN no_data_found THEN
                    raise_application_error(-20101, 'Encountered error on Initializing approvers - Research Office Recipient not properly setup');
                WHEN too_many_rows THEN
                    raise_application_error(-20101, 'Encountered error on Initializing approvers - Too many Research Office Recipient defined');
                WHEN OTHERS THEN
                    raise_application_error(-20101, 'Encountered error on Initializing approvers - Unable to retrieve Research Office Recipient data');
            END;


            INSERT INTO xxup.xxup_per_ps_action_history (
                        item_key,
                        sequence_no,
                        approver_no,
                        from_employee_id,
                        from_employee_name,
                        from_position_name,
                        from_org_name,
                        to_employee_id,
                        to_employee_name,
                        to_position_name,
                        to_org_name,
                        action               
                    ) VALUES (
                        lv_item_key,
                        lv_tran_no,
                        ln_approver_ctr,
                        ln_from_id,
                        lv_from_name,
                        lv_from_pos_name,
                        lv_from_org_name,
                        ln_to_id,
                        lv_to_name,
                        lv_to_pos_name,
                        lv_to_org_name,
                        c_for_completion_ac
                    );


            END IF;




            p_item_key :=  lv_item_key; 
            COMMIT;    

        END IF;

    EXCEPTION
        WHEN OTHERS THEN
            raise_application_error(-20100, 'init_approvers error: ' || SQLERRM);

    END init_approvers;


    PROCEDURE resubmit(p_item_key VARCHAR2)
    IS

      lv_tran_no VARCHAR2(100);
      ln_nid NUMBER;
      lv_research_title VARCHAR2(200);
      lv_owner VARCHAR2(100);


      lv_emp_name VARCHAR2(100);
      ln_emp_id NUMBER;
      lv_emp_pos_name VARCHAR2(150);
      lv_emp_org_name VARCHAR2(150);


      ln_approver_counter NUMBER;
      ln_update_ctr NUMBER;
      ln_total_approver_count NUMBER;

      lv_error VARCHAR2(2000);

      lv_item_key wf_notifications.item_key%TYPE;
    BEGIN
        lv_item_key := p_item_key;


      --get notification id

      BEGIN
      SELECT notification_id
            ,recipient_role
      INTO ln_nid
          ,lv_owner
      FROM wf_notifications
      WHERE message_type LIKE c_item_type
      AND message_name LIKE 'RFC_MSG'
      AND INSTR(context, ':' || lv_item_key || ':') > 1 --added colons to get exact itemkey
      AND status = 'OPEN'; 

      EXCEPTION
        WHEN OTHERS THEN
              raise_application_error(-20101, 'error on getting notification: ' || SQLERRM);
      END;

      BEGIN

      set_owner_details(p_item_key     => lv_item_key
                       ,p_emp_id       => ln_emp_id
                       ,p_emp_name     => lv_emp_name
                       ,p_emp_pos_name => lv_emp_pos_name
                       ,p_emp_org_name => lv_emp_org_name
      );
      EXCEPTION
        WHEN OTHERS THEN
            raise_application_error(-20101, 'error on setting owner details: ' || SQLERRM);
      END;


      IF ln_nid IS NOT NULL THEN


          lv_research_title := wf_engine.getitemattrtext(c_item_type
                                                      ,lv_item_key
                                                      ,'PROJECT_NAME');



          ln_approver_counter := wf_engine.getitemattrnumber(c_item_type
                                                            ,lv_item_key
                                                           ,'APPROVER_COUNTER');

          wf_engine.setitemattrtext(c_item_type
                                   ,lv_item_key
                                   ,'TITLE'
                                   ,c_module_title || ' - '
                                   || lv_research_title
                                   || ' has been corrected by '
                                   || lv_emp_name ||
                                      ' and requires your approval'
                                    );


          wf_notification.setattrtext(nid    => ln_nid
                                     ,aname  => 'RESULT'
                                     ,avalue => 'RESUBMIT');

          BEGIN
              wf_notification.respond(nid       =>  ln_nid
                                     ,responder =>  lv_owner);

          EXCEPTION
            WHEN OTHERS THEN
              raise_application_error(-20105, 'Error encountered on Resubmit:  get and set attributes: ' || SQLERRM);
          END;


          BEGIN

          UPDATE xxup.xxup_per_ps_action_history hist
          SET action = 'Resubmit'
             ,action_date = SYSDATE
          WHERE item_key = lv_item_key
          AND approver_no = (SELECT MAX(approver_no)
                             FROM XXUP.XXUP_PER_PS_ACTION_HISTORY hist_1
                             WHERE hist_1.item_key = hist.item_key
                             AND action = 'Pending'); /*get line number of employee*/

--          COMMIT;

          EXCEPTION
            WHEN OTHERS THEN
              raise_application_error(-20101, 'error on update resubmit: ' || SQLERRM);
          END;


          /*1. get all pending approvers and increment their approver no/sequence by 1  
          2. insert record for the approver (pending approval)
          */

--          BEGIN
--          UPDATE xxup.xxup_per_ps_action_history
--          SET approver_no = approver_no + 1
--          WHERE action = 'Pending'
--          
--          AND sequence_no = p_sequence_no;  
--          
--          EXCEPTION
--            WHEN OTHERS THEN
--              raise_application_error(-20101, 'error on updating approver no: ' || SQLERRM);
--          END;

          BEGIN
              INSERT INTO xxup.xxup_per_ps_action_history(sequence_no,
                                                          approver_no,
                                                          from_employee_id,
                                                          from_employee_name,
                                                          from_position_name,
                                                          from_org_name,
                                                          to_employee_id,
                                                          to_employee_name,
                                                          to_position_name,
                                                          to_org_name,
                                                          action,
                                                          item_key
                                                         )
              SELECT sequence_no
                    ,to_number(approver_no) + 2
                    ,ln_emp_id
                    ,lv_emp_name
                    ,lv_emp_pos_name
                    ,lv_emp_org_name
                    ,to_employee_id
                    ,to_employee_name
                    ,to_position_name
                    ,to_org_name
                    ,'Pending'
                    ,lv_item_key
              FROM xxup.xxup_per_ps_action_history hist
              WHERE item_key = lv_item_key
              AND approver_no = (SELECT MAX(approver_no)
                                 FROM XXUP.XXUP_PER_PS_ACTION_HISTORY hist_1
                                 WHERE hist_1.item_key = hist.item_key
                                 AND action = 'Return for correction'); /*get latest return for correction*/

          EXCEPTION
            WHEN OTHERS THEN
              raise_application_error(-20101, 'Error on resubmit -> insert: ' || SQLERRM);
          END;


--          ln_approver_counter := ln_approver_counter + 1;
--
--          wf_engine.setitemattrnumber(c_item_type
--                                     ,lv_item_key
--                                     ,'APPROVER_COUNTER'
--                                     ,ln_approver_counter);


--          SELECT COUNT(1)
--          INTO ln_total_approver_count
--          FROM xxup.xxup_per_ps_action_history
--          WHERE sequence_no = p_sequence_no;
--
--          wf_engine.setitemattrnumber('XXUPPSWF'
--                                     ,p_sequence_no
--                                     ,'TOTAL_APPROVER_COUNT'
--                                     ,ln_total_approver_count);   



          COMMIT;


      END IF;



    EXCEPTION 
      WHEN OTHERS THEN 

        raise_application_error(-20100, 'resubmit error: ' || SQLERRM);


    END resubmit; 





    PROCEDURE update_status(itemtype  IN VARCHAR2
                            ,l_itemkey IN VARCHAR2
                            ,actid     IN VARCHAR2
                            ,funcmode  IN VARCHAR2
                            ,resultout OUT VARCHAR2)
    IS

    lv_tran_no VARCHAR2(100);

    lv_appr_user_name VARCHAR2(100);
    lv_research_title VARCHAR2(150);

    lv_ntf_result VARCHAR2(30);
    ln_nid NUMBER := wf_engine.context_nid;

    ln_approver_counter NUMBER;
    ln_total_approver_count NUMBER;
    lv_completed_approval VARCHAR2(1) := 'N';

    ln_cur_approver_no NUMBER;

    lv_approver_name VARCHAR2(150);
    lv_approver_emp_id NUMBER;

    ln_update_ctr NUMBER; --update approver no on RFC


    lv_emp_name VARCHAR2(100);
    ln_emp_id NUMBER;
    lv_emp_pos_name VARCHAR2(150);
    lv_emp_org_name VARCHAR2(150);

    ln_to_id NUMBER;
    lv_to_name VARCHAR2(100);
    lv_to_pos_name VARCHAR2(150);
    lv_to_org_name VARCHAR2(150);

    l_new_approver_id         wf_roles.orig_system_id%TYPE;
    l_origsys                  wf_roles.orig_system%TYPE;

    lv_error VARCHAR2(1000);
    lv_action_prefix VARCHAR2(1) := 'C';


    lv_appr_status xxup.xxup_per_ps_action_history.action%TYPE;
    lv_close_ac VARCHAR2(50);
    lv_prev_proj_status xxup.xxup_rim_header.project_status%TYPE;

    BEGIN


        IF funcmode = 'RESPOND' THEN

            lv_ntf_result := wf_notification.getattrtext(ln_nid, 'RESULT');


            ln_approver_counter := wf_engine.getitemattrnumber(itemtype
                                                             ,l_itemkey
                                                             ,'APPROVER_COUNTER');

            INSERT INTO test_tbl VALUES('lv_ntf_result: ' || lv_ntf_result, CURRENT_TIMESTAMP);


            BEGIN 
                SELECT transaction_no
                INTO lv_tran_no
                FROM xxup.xxup_rim_header
                WHERE item_key = l_itemkey;

            EXCEPTION
                WHEN OTHERS THEN
                    raise_application_error(-20101, 'Error getting Sequence no');
            END;


            lv_research_title := wf_engine.getitemattrtext(itemtype
                                                 ,l_itemkey
                                                 ,'PROJECT_NAME');

            /*Will update transaction and exit this procedure once done */                  
            IF lv_ntf_result = c_acctg_ac THEN                                
                BEGIN

                  UPDATE xxup.xxup_per_ps_action_history hist
                      SET action = 'Updated Fiscal Info'
                         ,action_date = SYSDATE
                      WHERE item_key = l_itemkey
                      AND approver_no = (SELECT MAX(approver_no)
                                         FROM XXUP.XXUP_PER_PS_ACTION_HISTORY hist_1
                                         WHERE hist_1.item_key = hist.item_key
                                         AND action = c_acctg_upd_ac); 

   
                      UPDATE xxup.xxup_rim_header
                      SET approval_status = 'Approved'
                      WHERE item_key = l_itemkey;

                  EXCEPTION
                    WHEN OTHERS THEN
                      raise_application_error(-20101, 'error on update fiscal info: ' || SQLERRM);
                  END;


                BEGIN

                  wf_engine.setitemattrtext(c_item_type
                            ,l_itemkey
                            ,'FYI_TITLE'
                            ,c_module_title || ' - '
                            || lv_research_title
                            || ' has been approved by Accounting Office'
                            || ' and has been completed'
                            );


                EXCEPTION
                  WHEN OTHERS THEN
                    lv_error := SQLERRM;
                    raise_application_error(-20101, 'Update status: ' || lv_error);
                END;

                resultout := c_acctg_ac;

                INSERT INTO test_tbl VALUES('done update acctg', CURRENT_TIMESTAMP);
                RETURN;

            ELSIF lv_ntf_result IN (c_for_clsout_ac
                                    ,c_don_fin_rep_ac
                                    ,c_cls_rep_val_ac
                                    ,c_closed_ac
                                    ,c_completed_ac
                                    )
            THEN
                INSERT INTO test_tbl VALUES('closing..', CURRENT_TIMESTAMP);
                
                    CASE 
                        /*1. Update approval status on history
                          2. Assign new close action
                          3. Query for last project status
                        */
                    
                        WHEN lv_ntf_result = c_for_clsout_ac THEN
                            lv_appr_status := c_clsd_out_ps;
                            lv_close_ac := c_clsd_out_ac;
                            lv_prev_proj_status := NULL; --for closeout is initial
                            
                            INSERT INTO test_tbl VALUES('cls:1', CURRENT_TIMESTAMP);

                        WHEN lv_ntf_result = c_don_fin_rep_ac THEN
                            lv_appr_status := c_don_fin_rep_ps;
                            lv_close_ac := c_don_fin_rep_ac;
                            lv_prev_proj_status := c_prep_fin_rep_ac;
                            
                            INSERT INTO test_tbl VALUES('cls:2', CURRENT_TIMESTAMP);
                            
                        WHEN lv_ntf_result = c_cls_rep_val_ac THEN
                            lv_appr_status := c_cls_rep_val_ps;
                            lv_close_ac := c_cls_rep_val_ac;
                            lv_prev_proj_status := c_val_cls_rep_ac; 
                            
                            INSERT INTO test_tbl VALUES('cls:3', CURRENT_TIMESTAMP);  
                            
                        WHEN lv_ntf_result = c_closed_ac THEN
                            lv_appr_status := c_closed_ps;
                            lv_close_ac := c_closed_ac;
                            lv_prev_proj_status := c_for_closing_ac; 
                            
                            INSERT INTO test_tbl VALUES('cls:4', CURRENT_TIMESTAMP);  
                            
                        WHEN lv_ntf_result = c_completed_ac THEN
--                            lv_appr_status := c_closed_ps;
--                            lv_close_ac := c_closed_ac;
--                            lv_prev_proj_status := c_for_closing_ac; 
--                            
                            INSERT INTO test_tbl VALUES('cls:5', CURRENT_TIMESTAMP); 
                            
                            UPDATE xxup_rim_header main
                            SET (approval_status
                                ,project_status
                               ,last_updated_by
                               ,last_update_date
                               )
                               = 
                               (SELECT c_closed_ps
                                      ,c_completed_ps
                                        ,last_updated_by
                                        ,last_update_date
                                FROM xxup_rim_header tr
                                WHERE tr.item_key = l_itemkey
                               )
                            WHERE main.approval_status = 'Approved'
                            AND main.transaction_no = lv_tran_no;
                            
                            
                            DELETE FROM xxup_rim_header
                            WHERE item_key = l_itemkey;
--                            del_records(l_itemkey);
                            resultout := c_completed_ac;
                            
                            
                            RETURN; --exit workflow
                            
                    END CASE;

                BEGIN
                  UPDATE xxup.xxup_per_ps_action_history hist
                      SET action = lv_appr_status
                         ,action_date = SYSDATE
                      WHERE item_key = l_itemkey
                      AND approver_no = (SELECT MAX(approver_no)
                                         FROM XXUP.XXUP_PER_PS_ACTION_HISTORY hist_1
                                         WHERE hist_1.item_key = hist.item_key
                                         AND action = lv_prev_proj_status); 

--                      UPDATE xxup.xxup_rim_header
--                      SET approval_status = lv_appr_status
--                      WHERE item_key = l_itemkey;   
                    
                  EXCEPTION
                    WHEN OTHERS THEN
                      raise_application_error(-20101, 'error on updating proj status info: ' || SQLERRM);
                  END;

                BEGIN
                  wf_engine.setitemattrtext(c_item_type
                            ,l_itemkey
                            ,'FYI_TITLE'
                            ,c_module_title || ' - '
                            || lv_research_title
                            || ' has been ' || c_closed_ps
                            );

                  wf_engine.setitemattrtext(itemtype
                                     ,l_itemkey
                                     ,'CLOSE_STATUS_URL'
                                     ,'JSP:/OA_HTML/OA.jsp?OAFunc=XXUP_RIM_REQ_PG' || 
                                      '&urlParam='    || lv_close_ac ||
                                      '&pSequenceNo=' || lv_tran_no ||
                                      '&pItemKey='    || l_itemkey
                                    );          


                EXCEPTION
                  WHEN OTHERS THEN
                    lv_error := SQLERRM;
                    raise_application_error(-20101, 'Close project: ' || lv_error);
                END;

                INSERT INTO test_tbl VALUES('exiting closing...', CURRENT_TIMESTAMP);  
                resultout := lv_close_ac;
                RETURN;
                
                INSERT INTO test_tbl VALUES('exited', CURRENT_TIMESTAMP);  
            END IF;

            BEGIN

              SELECT MIN(approver_no)
              INTO ln_cur_approver_no
              FROM xxup.xxup_per_ps_action_history
              WHERE item_key = l_itemkey
              AND action = 'Pending';

            EXCEPTION
              WHEN OTHERS THEN
                raise_application_error(-20101, 'Error getting latest approver');
            END;


            BEGIN

            /*Set employee details*/
            set_owner_details(p_item_key     => l_itemkey
                             ,p_emp_id       => ln_emp_id
                             ,p_emp_name     => lv_emp_name
                             ,p_emp_pos_name => lv_emp_pos_name
                             ,p_emp_org_name => lv_emp_org_name
            );


            EXCEPTION
                WHEN OTHERS THEN
                    lv_error := SQLERRM;
                    raise_application_error(-20105, 'error getting employee details: ' || lv_error);
            END;




            BEGIN

                SELECT user_name
                     ,(SELECT full_name
                       FROM per_all_people_f papf
                       WHERE person_id = employee_id
                       AND SYSDATE BETWEEN effective_start_date AND effective_end_date
                       AND person_type_id = 1126)
                INTO lv_appr_user_name
                    ,lv_approver_name
                FROM fnd_user
                WHERE employee_id = (SELECT to_employee_id
                                                   FROM xxup.xxup_per_ps_action_history pah
                                                   WHERE item_key = l_itemkey
                                                   AND approver_no = ln_cur_approver_no
                                                   AND action = 'Pending');

            EXCEPTION
              WHEN OTHERS THEN
                raise_application_error(-20101, 'Error getting latest approver user name');
            END;




            BEGIN


              SELECT COUNT(DISTINCT to_employee_name)
              INTO ln_total_approver_count
              FROM xxup.xxup_per_ps_action_history
              WHERE item_key = l_itemkey
              AND action IN ('Pending','Return for correction','Reassign');

            EXCEPTION
              WHEN OTHERS THEN
                raise_application_error(-20101, 'Error getting total approver count');
            END;



                IF lv_ntf_result = 'RFC' THEN

                    wf_engine.setitemattrtext(itemtype
                                          ,l_itemkey
                                          ,'RFC_URL'
                                          ,'JSP:/OA_HTML/OA.jsp?OAFunc=XXUP_RIM_REQ_PG&urlParam=RFC' ||
                                          '&pSequenceNo=' || lv_tran_no ||
                                          '&pItemKey=' || l_itemkey
                                          );            

                    wf_engine.setitemattrtext(itemtype
                                          ,l_itemkey
                                          ,'TITLE'
                                          ,c_module_title || ' - ' ||  
                                          lv_research_title|| 
                                          ' has been returned for correction by '|| 
                                          lv_approver_name
                                           );

                    wf_engine.setitemattrtext(itemtype
                                          ,l_itemkey
                                          ,'FROM'
                                          ,lv_appr_user_name);



                    UPDATE xxup.xxup_per_ps_action_history hist
                    SET action = 'Return for correction'
                       ,action_date = SYSDATE
                       ,note = wf_engine.context_user_comment
                    WHERE item_key = l_itemkey
                    AND approver_no = ln_cur_approver_no;
--                                        (SELECT MAX(approver_no)
--                                         FROM XXUP.XXUP_PER_PS_ACTION_HISTORY hist_1
--                                         WHERE hist_1.item_key = hist.item_key
--                                         AND action = 'Pending');



                    /*1. get all pending approvers and increment their approver no/sequence by 1  
                      2. insert record for the owner (pending resubmission)
                    */


--                    UPDATE xxup.xxup_per_ps_action_history
--                    SET approver_no = approver_no + 1
--                    WHERE action = 'Pending'
--                    
--                    AND sequence_no = lv_tran_no;

                    BEGIN 
                    INSERT INTO xxup.xxup_per_ps_action_history(sequence_no,
                                                                approver_no,
                                                                from_employee_id,
                                                                from_employee_name,
                                                                from_position_name,
                                                                from_org_name,
                                                                to_employee_id,
                                                                to_employee_name,
                                                                to_position_name,
                                                                to_org_name,
                                                                action,
                                                                item_key
                                                               )
                    SELECT sequence_no
                          ,ln_cur_approver_no + 1
                          ,to_employee_id
                          ,to_employee_name
                          ,to_position_name
                          ,to_org_name
                          ,ln_emp_id
                          ,lv_emp_name
                          ,lv_emp_pos_name
                          ,lv_emp_org_name
                          ,'Pending'
                          ,l_itemkey
                    FROM xxup.xxup_per_ps_action_history hist
                    WHERE item_key = l_itemkey
                    AND approver_no = (SELECT MAX(approver_no)
                                       FROM XXUP.XXUP_PER_PS_ACTION_HISTORY hist_1
                                       WHERE hist_1.item_key = hist.item_key
                                       AND action IN ('Resubmit', 'Pending', 'Submit')); /*get latest employee resubmission*/


                  EXCEPTION
                    WHEN OTHERS THEN
                      lv_error := SQLERRM;
                      raise_application_error(-20105, 'error inserting action history record: ' || lv_error);
                  END;

                ELSIF lv_ntf_result = 'APPROVE' THEN

                    INSERT INTO test_tbl VALUES('upd, appr', CURRENT_TIMESTAMP);

                    wf_engine.setitemattrtext(itemtype
                                             ,l_itemkey
                                             ,'FYI_TITLE'
                                             ,c_module_title || ' - '
                                             || lv_research_title
                                             || ' has been approved by '
                                             || lv_approver_name
                                            );
                    -- notification for next approver
                    wf_engine.setitemattrtext(itemtype
                                            ,l_itemkey
                                            ,'TITLE'
                                            ,c_module_title || ' - '
                                          || lv_research_title
                                          || ' submitted by '
                                          || lv_emp_name
                                          || ' needs your Approval'
                                             );

                    UPDATE XXUP.XXUP_PER_PS_ACTION_HISTORY
                    SET action = 'Approved'
                       ,action_date = SYSDATE
                       ,note = wf_engine.context_user_comment
                    WHERE item_key = l_itemkey
                    AND approver_no = ln_approver_counter;

                ELSIF lv_ntf_result = 'REJECT' THEN

                    wf_engine.setitemattrtext(itemtype
                                             ,l_itemkey
                                             ,'FYI_TITLE'
                                             ,c_module_title || ' - '
                                             || lv_research_title
                                             || ' has been rejected by '
                                             || lv_approver_name
                                             );

                    UPDATE XXUP.XXUP_PER_PS_ACTION_HISTORY
                    SET action = 'Rejected'
                       ,action_date = SYSDATE
                       ,note = wf_engine.context_user_comment
                    WHERE item_key = l_itemkey
                    AND approver_no = ln_approver_counter;

                    --remove next line of approvers
                    DELETE FROM xxup.xxup_per_ps_action_history
                    WHERE item_key = l_itemkey
                    AND approver_no > ln_approver_counter;

                ELSIF funcmode = 'FORWARD' THEN

                    --get forwarded employee person id (l_new_approver_id
                    wf_directory.getroleorigsysinfo(wf_engine.context_new_role,
                                                    l_origsys,
                                                    l_new_approver_id
                                                   );


                    wf_engine.setitemattrtext(itemtype
                                             ,l_itemkey
                                             ,'TITLE'
                                             ,c_module_title || ' - '
                                             || lv_research_title
                                             || ' has been reassigned to you by '
                                             || lv_approver_name
                                             || ' for approval'
                                             );


                    UPDATE xxup.xxup_per_ps_action_history
                    SET action = 'Reassign'
                        ,action_date = SYSDATE
                        ,note = wf_engine.context_user_comment
                    WHERE item_key = l_itemkey
                    AND approver_no = ln_cur_approver_no;



                     /*1. get all pending approvers and increment their approver no/sequence by 1 
                       2. insert record for the owner (pending pending approval to new approver)  */

        --             UPDATE xxup.xxup_per_ps_action_history
        --             SET approver_no = approver_no + 1
        --             WHERE action = 'Pending'
        --             AND sequence_no = lv_tran_no
        --             ;



                     /*Get details of reassigned employee*/
                     BEGIN

                       SELECT papf.full_name
                             ,papf.person_id
                             ,(SELECT ppd.segment1
                               FROM per_position_definitions ppd
                                   ,per_all_positions pap
                               WHERE pap.position_id = paaf.position_id
                               AND   ppd.position_definition_id = pap.position_definition_id) POSITION_NAME
                            ,(SELECT haou.name
                              FROM per_position_definitions ppd
                                  ,per_all_positions pap
                                  ,hr_all_organization_units haou
                               WHERE pap.position_id = paaf.position_id
                               AND   ppd.position_definition_id = pap.position_definition_id
                               AND   haou.organization_id = ppd.segment2) ORGANIZATION_NAME
                       INTO lv_to_name
                           ,ln_to_id
                           ,lv_to_pos_name
                           ,lv_to_org_name
                       FROM per_all_assignments_f paaf
                           ,per_all_people_f papf
                       WHERE paaf.person_id = papf.person_id
                       AND SYSDATE BETWEEN paaf.effective_start_date AND paaf.effective_end_date
                       AND SYSDATE BETWEEN papf.effective_start_date AND papf.effective_end_date
                       AND primary_flag = 'Y'
                       AND papf.person_id = l_new_approver_id;

                     EXCEPTION
                        WHEN OTHERS THEN
                          lv_error := SQLERRM;
                          raise_application_error(-20105, 'Error on getting details of reassigned employee: ' || lv_error);
                     END;


                     INSERT INTO xxup.xxup_per_ps_action_history(sequence_no,
                                                                 approver_no,
                                                                 from_employee_id,
                                                                 from_employee_name,
                                                                 from_position_name,
                                                                 from_org_name,
                                                                 to_employee_id,
                                                                 to_employee_name,
                                                                 to_position_name,
                                                                 to_org_name,
                                                                 action,
                                                                 item_key

                                                                 )
                     SELECT sequence_no
                           ,to_number(approver_no) + 1
                           ,to_employee_id
                           ,to_employee_name
                           ,to_position_name
                           ,to_org_name
                           ,ln_to_id
                           ,lv_to_name
                           ,lv_to_pos_name
                           ,lv_to_org_name
                           ,'Pending'
                           ,l_itemkey
                     FROM xxup.xxup_per_ps_action_history
                     WHERE item_key = l_itemkey
                     AND approver_no = ln_cur_approver_no
                     ;         


        --            ln_approver_counter := ln_approver_counter + 1;
        ----            
        ----            
        --            wf_engine.setitemattrnumber(itemtype
        --                                        ,l_itemkey
        --                                        ,'APPROVER_COUNTER'
        --                                        ,ln_approver_counter);




                END IF;




                --exit workflow and complete, if no 'Pending' approval remaining
                  BEGIN
                      SELECT 'N'
                      INTO lv_completed_approval
                      FROM xxup.xxup_per_ps_action_history
                      WHERE action IN ('Pending'
                                      ,c_prep_fin_rep_ac
                                      ,c_val_cls_rep_ac 
                                      ,c_for_closing_ac 
                                      ,c_for_completion_ac 
                                      )
                      AND item_key = l_itemkey
                      AND ROWNUM = 1;
                  EXCEPTION
                      WHEN no_data_found THEN
                          lv_completed_approval := 'Y';
                       WHEN OTHERS THEN
                          lv_completed_approval := 'N';


                  END;





                  IF lv_completed_approval = 'Y' THEN
                    INSERT INTO test_tbl VALUES('lv_completed_approval: ' || lv_completed_approval, CURRENT_TIMESTAMP);
                      IF lv_ntf_result = 'APPROVE' THEN


                        BEGIN
                                UPDATE xxup.xxup_rim_header
                                  SET approval_status = 'Approved'
                                  WHERE item_key = l_itemkey;


                                  UPDATE xxup.xxup_per_ps_action_history
                                  SET action = 'Approved'
                                     ,action_date = SYSDATE
                                     ,note = wf_engine.context_user_comment
                                  WHERE item_key = l_itemkey
                                  AND approver_no = ln_cur_approver_no; 

                        SELECT SUBSTR(l_itemkey,1,1)
                        INTO lv_action_prefix
                        FROM DUAL;
                        
                        
                        IF lv_action_prefix = 'U' THEN

                            INSERT INTO test_tbl VALUES('lv_action_prefix: ' || lv_action_prefix, CURRENT_TIMESTAMP);

                            UPDATE xxup_rim_header main
                                SET (transaction_no
                                ,assignment_id
                                ,research_type
                                ,research_title
                                ,research_title2
                                ,research_title3
                                ,brief_description
                                ,main_area_interest
                                ,project_impact_desc
                                ,project_leader_id
                                ,start_date
                                ,end_date
                                ,actual_end_date
                                ,project_status
                                ,project_remarks
                                --,item_key
                                ,approval_status
                                ,attribute1
                                ,attribute2
                                ,attribute3
                                ,attribute4
                                ,attribute5
                                ,last_update_date
                                ,last_updated_by
                                ,last_update_login
                                --,created_by
                                --,creation_date
                                ,research_type_spec) =
                                (SELECT transaction_no
                                ,assignment_id
                                ,research_type
                                ,research_title
                                ,research_title2
                                ,research_title3
                                ,brief_description
                                ,main_area_interest
                                ,project_impact_desc
                                ,project_leader_id
                                ,start_date
                                ,end_date
                                ,actual_end_date
                                ,project_status
                                ,project_remarks
                                --,item_key
                                ,'Approved'
                                ,attribute1
                                ,attribute2
                                ,attribute3
                                ,attribute4
                                ,attribute5
                                ,last_update_date
                                ,last_updated_by
                                ,last_update_login
                                --,created_by
                                --,creation_date
                                ,research_type_spec
                                FROM xxup_rim_header tr
                                WHERE item_key = l_itemkey
                                )
                                WHERE main.approval_status = 'Approved'
                                AND main.transaction_no = lv_tran_no;

                                DELETE FROM xxup_rim_header tr
                                WHERE item_key = l_itemkey;


                                UPDATE xxup_rim_fiscal_details
                                SET 
                                (
                                --rim_fisc_id
                                transaction_no
                                ,constituent_unit
                                ,responsibility_center
                                ,collaborating_agency
                                ,funding_agency
                                ,sp_code
                                ,fund_controller_id
                                ,far_excluded
                                --,item_key
                                ,attribute1
                                ,attribute2
                                ,attribute3
                                ,attribute4
                                ,attribute5
                                ,last_update_date
                                ,last_updated_by
                                ,last_update_login
                                --,created_by
                                --,creation_date
                                ) =
                                (SELECT 
                                -- rim_fisc_id
                                transaction_no
                                ,constituent_unit
                                ,responsibility_center
                                ,collaborating_agency
                                ,funding_agency
                                ,sp_code
                                ,fund_controller_id
                                ,far_excluded
                                --,item_key
                                ,attribute1
                                ,attribute2
                                ,attribute3
                                ,attribute4
                                ,attribute5
                                ,last_update_date
                                ,last_updated_by
                                ,last_update_login
                                --,created_by
                                --,creation_date
                                FROM xxup_rim_fiscal_details tr
                                WHERE tr.item_key = (SELECT item_key 
                                              FROM xxup_rim_header hd 
                                              WHERE 1=1
                                              AND hd.item_key = tr.item_key
                                              AND tr.item_key = l_itemkey
                                              )
                                )
                                WHERE item_key = (SELECT item_key 
                                                  FROM xxup_rim_header hd
                                                  WHERE hd.approval_status = 'Approved'
                                                    AND hd.transaction_no = lv_tran_no);

                                DELETE FROM xxup_rim_fiscal_details tr
                                WHERE item_key = l_itemkey;

                                UPDATE xxup_rim_publication
                                SET (
                                transaction_no
                                ,is_published
                                ,distribution_mode
                                ,has_award
                                ,award_title
                                ,award_date
                                ,conferring_body
                                ,points
                                ,post_act_eval_rating
                                ,remarks
                                ,attribute1
                                ,attribute2
                                ,attribute3
                                ,attribute4
                                ,attribute5
                                ,last_update_date
                                ,last_updated_by
                                ,last_update_login
                                ) =
                                (
                                SELECT 
                                transaction_no
                                ,is_published
                                ,distribution_mode
                                ,has_award
                                ,award_title
                                ,award_date
                                ,conferring_body
                                ,points
                                ,post_act_eval_rating
                                ,remarks
                                ,attribute1
                                ,attribute2
                                ,attribute3
                                ,attribute4
                                ,attribute5
                                ,last_update_date
                                ,last_updated_by
                                ,last_update_login
                                FROM xxup_rim_publication tr
                                WHERE tr.item_key = (SELECT item_key 
                                                  FROM xxup_rim_header hd 
                                                  WHERE 1=1
                                                  AND hd.item_key = tr.item_key
                                                  AND tr.item_key = l_itemkey
                                                  )
                                )
                                WHERE item_key = (SELECT item_key
                                                  FROM xxup_rim_header hd
                                                  WHERE hd.approval_status = 'Approved'
                                                    AND hd.transaction_no = lv_tran_no);

                                DELETE FROM xxup_rim_publication tr
                                WHERE item_key = l_itemkey;

                                --
                                DELETE FROM xxup_rim_team_members main
                                WHERE item_key = (SELECT item_key
                                                  FROM xxup_rim_header hd
                                                  WHERE hd.approval_status = 'Approved'
                                                    AND hd.transaction_no = lv_tran_no);

                                UPDATE xxup_rim_team_members
                                SET item_key = (SELECT item_key
                                                  FROM xxup_rim_header hd
                                                  WHERE hd.approval_status = 'Approved'
                                                    AND hd.transaction_no = lv_tran_no)
                                WHERE item_key = l_itemkey;

                                --
                                DELETE FROM xxup_rim_funding main
                                WHERE item_key = (SELECT item_key
                                                  FROM xxup_rim_header hd
                                                  WHERE hd.approval_status = 'Approved'
                                                    AND hd.transaction_no = lv_tran_no);

                                UPDATE xxup_rim_funding
                                SET item_key = (SELECT item_key
                                                  FROM xxup_rim_header hd
                                                  WHERE hd.approval_status = 'Approved'
                                                    AND hd.transaction_no = lv_tran_no)
                                WHERE item_key = l_itemkey;

                                --
                                DELETE FROM xxup_rim_milestones main
                                WHERE item_key = (SELECT item_key
                                                  FROM xxup_rim_header hd
                                                  WHERE hd.approval_status = 'Approved'
                                                    AND hd.transaction_no = lv_tran_no);

                                UPDATE xxup_rim_milestones
                                SET item_key = (SELECT item_key
                                                  FROM xxup_rim_header hd
                                                  WHERE hd.approval_status = 'Approved'
                                                    AND hd.transaction_no = lv_tran_no)
                                WHERE item_key = l_itemkey;

                                --
                                DELETE FROM xxup_rim_proj_impact main
                                WHERE item_key = (SELECT item_key
                                                  FROM xxup_rim_header hd
                                                  WHERE hd.approval_status = 'Approved'
                                                    AND hd.transaction_no = lv_tran_no);

                                UPDATE xxup_rim_proj_impact
                                SET item_key = (SELECT item_key
                                                  FROM xxup_rim_header hd
                                                  WHERE hd.approval_status = 'Approved'
                                                    AND hd.transaction_no = lv_tran_no)
                                WHERE item_key = l_itemkey;
                            END IF;

                            INSERT INTO test_tbl VALUES('updated: ', CURRENT_TIMESTAMP);

                        EXCEPTION
                            WHEN OTHERS THEN
                                ROLLBACK;
                                raise_application_error(-20101, 'Update status, Error updating records!');
                        END;

                        BEGIN


                          wf_engine.setitemattrtext(itemtype
                                    ,l_itemkey
                                    ,'FYI_TITLE'
                                    ,c_module_title || ' - '
                                    || lv_research_title
                                    || ' has been approved by '
                                    || lv_approver_name
                                    || ' and has been completed'
                                    );


                           INSERT INTO test_tbl VALUES('workflow done ', CURRENT_TIMESTAMP); 

                            --workflow done, exit proc
                            RETURN;

--                                INSERT INTO xxup_ps_test_tbl
--                                VALUES('FYI_TITLE set ');
                        EXCEPTION
                          WHEN OTHERS THEN
                            lv_error := SQLERRM;
                            raise_application_error(-20101, 'Update status: ' || lv_error);
                        END;



                      ELSIF lv_ntf_result = 'REJECT' THEN

--                                INSERT INTO xxup_ps_test_tbl
--                                VALUES('rejected!');

                          wf_engine.setitemattrtext(itemtype
                                    ,l_itemkey
                                    ,'FYI_TITLE'
                                    ,c_module_title || ' - '
                                    || lv_research_title
                                    || ' has been rejected by '
                                    || lv_approver_name
                                    );

--                          UPDATE xxup.xxup_rim_header
--                          SET approval_status = 'Rejected'
--                          WHERE item_key = l_itemkey;

                          UPDATE xxup.xxup_per_ps_action_history
                          SET action = 'Rejected'
                             ,action_date = SYSDATE
                             ,note = wf_engine.context_user_comment
                          WHERE item_key = l_itemkey
                          AND approver_no = ln_cur_approver_no
                          ;

                      END IF;

            END IF; --completed approval

            IF lv_ntf_result IN ('APPROVE', 'REJECT','RFC') THEN
            BEGIN
                INSERT INTO test_tbl VALUES('upd, still here', CURRENT_TIMESTAMP);
                wf_engine.setitemattrtext(itemtype
                                     ,l_itemkey
                                     ,'RESULT'
                                     ,lv_ntf_result);

                resultout := lv_ntf_result;

                --previous approver
                wf_engine.setitemattrtext(itemtype
                                         ,l_itemkey
                                         ,'FROM'
                                         ,lv_appr_user_name);


                ln_approver_counter := ln_approver_counter + 1;
                wf_engine.setitemattrnumber(itemtype
                                         ,l_itemkey
                                         ,'APPROVER_COUNTER'
                                         ,ln_approver_counter);

                SELECT user_name
                      ,(SELECT full_name
                        FROM per_all_people_f papf
                        WHERE person_id = employee_id
                        AND SYSDATE BETWEEN papf.effective_start_date AND papf.effective_end_date
                        )
                INTO lv_appr_user_name
                    ,lv_approver_name
                FROM fnd_user
                WHERE employee_id = (SELECT to_employee_id
                                     FROM xxup.xxup_per_ps_action_history pah
                                     WHERE item_key = l_itemkey     
                                     AND approver_no = ln_approver_counter);                  




                IF lv_ntf_result IN ('APPROVE', 'REJECT') THEN
                    BEGIN
                        wf_engine.setitemattrtext(itemtype
                                                 ,l_itemkey
                                                 ,'APPROVER_NAME'
                                                 ,lv_approver_name);


                        wf_engine.setitemattrtext(itemtype
                                           ,l_itemkey
                                           ,'APPROVER'
                                           ,lv_appr_user_name);            



                     EXCEPTION 
                        WHEN OTHERS THEN
                          raise_application_error(-20101, 'Encountered error on Update status - set attributes: ' || SUBSTR(SQLERRM,0,200));
                    END;
                END IF;



                EXCEPTION
              WHEN OTHERS THEN
                lv_error := SQLERRM;
                raise_application_error(-20101, 'setting attributes: ' || lv_error);
             END;
            END IF;



          END IF;

        EXCEPTION
          WHEN OTHERS THEN
            lv_error := SQLERRM;
            raise_application_error(-20101, 'update status' ||  lv_error);
    END update_status;

    PROCEDURE upd_acctg_info(p_item_key  IN VARCHAR2)
    IS

      lv_tran_no VARCHAR2(100);
      ln_nid NUMBER;
      lv_research_title VARCHAR2(200);
      lv_owner VARCHAR2(100);
      lv_acctg_staff fnd_user.user_name%TYPE;
      lv_acctg_staff_name per_all_people_f.full_name%TYPE;


      lv_emp_name VARCHAR2(100);
      ln_emp_id NUMBER;
      lv_emp_pos_name VARCHAR2(150);
      lv_emp_org_name VARCHAR2(150);


      ln_approver_counter NUMBER;
      ln_update_ctr NUMBER;
      ln_total_approver_count NUMBER;

      lv_error VARCHAR2(2000);

      lv_item_key wf_notifications.item_key%TYPE;
      lv_msg_name VARCHAR2(20);

    BEGIN
        lv_item_key := p_item_key;



      --get notification id
      BEGIN
      SELECT notification_id
            ,recipient_role
      INTO ln_nid
          ,lv_acctg_staff
      FROM wf_notifications
      WHERE message_type LIKE c_item_type
      AND message_name LIKE c_acctg_msg
      AND INSTR(context, ':' || lv_item_key || ':') > 1 --added colons to get exact itemkey
      AND status = 'OPEN'; 

      EXCEPTION
        WHEN OTHERS THEN
              raise_application_error(-20101, 'error on getting notification: ' || SQLERRM);
      END;

      BEGIN

      set_owner_details(p_item_key     => lv_item_key
                       ,p_emp_id       => ln_emp_id
                       ,p_emp_name     => lv_emp_name
                       ,p_emp_pos_name => lv_emp_pos_name
                       ,p_emp_org_name => lv_emp_org_name
      );
      EXCEPTION
        WHEN OTHERS THEN
            raise_application_error(-20101, 'error on setting owner details: ' || SQLERRM);
      END;


      IF ln_nid IS NOT NULL THEN


          lv_research_title := wf_engine.getitemattrtext(c_item_type
                                                      ,lv_item_key
                                                      ,'PROJECT_NAME');



          ln_approver_counter := wf_engine.getitemattrnumber(c_item_type
                                                            ,lv_item_key
                                                           ,'APPROVER_COUNTER');

--          lv_acctg_staff := wf_engine.getitemattrtext(c_item_type
--                                                    ,lv_item_key
--                                                    ,'ACCTG_STAFF');

        BEGIN
            SELECT (SELECT full_name
                   FROM per_all_people_f papf
                   WHERE person_id = employee_id
                   AND SYSDATE BETWEEN effective_start_date AND effective_end_date
                   AND person_type_id = 1126)
                    INTO lv_acctg_staff_name
            FROM fnd_user
            WHERE user_name = lv_acctg_staff;
        EXCEPTION
            WHEN OTHERS THEN
                raise_application_error(-20101, 'Error fetching Accounting staff name');
        END;




          BEGIN

            wf_engine.setitemattrtext(c_item_type
                                   ,lv_item_key
                                   ,'TITLE'
                                   ,c_module_title || ' - '
                                   || lv_research_title
                                   || '- Fiscal Info has been updated and RIM Transaction has been completed'
                                    );


              wf_notification.setattrtext(nid    => ln_nid
                                         ,aname  => 'RESULT'
                                         ,avalue => c_acctg_ac);



          EXCEPTION
            WHEN OTHERS THEN
              raise_application_error(-20105, 'Error encountered on Upd Acctg info:  get and set attributes: ' || SQLERRM);
          END;

          BEGIN
            wf_notification.respond(nid       =>  ln_nid
                                   ,responder =>  lv_acctg_staff);
          EXCEPTION
            WHEN OTHERS THEN
              raise_application_error(-20105, 'Error encountered sending response' || SQLERRM);
          END;

      END IF;



    EXCEPTION 
      WHEN OTHERS THEN 

        raise_application_error(-20100, 'upd_acctg_info error: ' || SQLERRM);


    END upd_acctg_info; 

    PROCEDURE upd_proj_stat(p_item_key  IN VARCHAR2
                           ,p_proj_status VARCHAR2)
    IS

      lv_tran_no VARCHAR2(100);
      ln_nid NUMBER;
      lv_research_title VARCHAR2(200);
      lv_owner VARCHAR2(100);
      lv_approver fnd_user.user_name%TYPE;
--      lv_acctg_staff_name per_all_people_f.full_name%TYPE;


      lv_emp_name VARCHAR2(100);
      ln_emp_id NUMBER;
      lv_emp_pos_name VARCHAR2(150);
      lv_emp_org_name VARCHAR2(150);


      ln_approver_counter NUMBER;
      ln_update_ctr NUMBER;
      ln_total_approver_count NUMBER;

      lv_error VARCHAR2(2000);

      lv_item_key wf_notifications.item_key%TYPE;
      lv_msg_name VARCHAR2(50);
      lv_result VARCHAR2(50);  


    BEGIN
        lv_item_key := p_item_key;


      CASE 
        WHEN p_proj_status = c_for_clsout_ps THEN
            lv_msg_name := c_for_clsout_msg;
            lv_result := c_clsd_out_ac;
--            lv_result := ;    

        WHEN p_proj_status = c_don_fin_rep_ps THEN
            lv_msg_name := c_don_fin_rep_msg;
            lv_result := c_don_fin_rep_ac;

        WHEN p_proj_status = c_cls_rep_val_ps THEN
            lv_msg_name := c_cls_rep_val_msg;
            lv_result := c_cls_rep_val_ac;

        WHEN p_proj_status = c_closed_ps THEN
            lv_msg_name := c_closed_msg;
            lv_result := c_closed_ac;

        WHEN p_proj_status = c_completed_ps THEN
            lv_msg_name := c_complete_msg;
            lv_result := c_completed_ac;
            
      END CASE;

      INSERT INTO test_tbl VALUES('lv_msg_name: ' || lv_msg_name, CURRENT_TIMESTAMP);

      --get notification id
      BEGIN
      SELECT notification_id
            ,recipient_role
      INTO ln_nid
          ,lv_approver
      FROM wf_notifications
      WHERE message_type LIKE c_item_type
      AND message_name LIKE lv_msg_name
      AND INSTR(context, ':' || lv_item_key || ':') > 1 --added colons to get exact itemkey
      AND status = 'OPEN'; 

      EXCEPTION
        WHEN OTHERS THEN
              raise_application_error(-20101, 'error on getting notification: ' || SQLERRM);
      END;

      BEGIN

      set_owner_details(p_item_key     => lv_item_key
                       ,p_emp_id       => ln_emp_id
                       ,p_emp_name     => lv_emp_name
                       ,p_emp_pos_name => lv_emp_pos_name
                       ,p_emp_org_name => lv_emp_org_name
      );
      EXCEPTION
        WHEN OTHERS THEN
            raise_application_error(-20101, 'error on setting owner details: ' || SQLERRM);
      END;


      IF ln_nid IS NOT NULL THEN
            INSERT INTO test_tbl VALUES('cls: notif found', CURRENT_TIMESTAMP);

          lv_research_title := wf_engine.getitemattrtext(c_item_type
                                                      ,lv_item_key
                                                      ,'PROJECT_NAME');



          ln_approver_counter := wf_engine.getitemattrnumber(c_item_type
                                                            ,lv_item_key
                                                           ,'APPROVER_COUNTER');

--          lv_acctg_staff := wf_engine.getitemattrtext(c_item_type
--                                                    ,lv_item_key
--                                                    ,'ACCTG_STAFF');

--        BEGIN
--            SELECT (SELECT full_name
--                   FROM per_all_people_f papf
--                   WHERE person_id = employee_id
--                   AND SYSDATE BETWEEN effective_start_date AND effective_end_date
--                   AND person_type_id = 1126)
--                    INTO lv_acctg_staff_name
--            FROM fnd_user
--            WHERE user_name = lv_acctg_staff;
--        EXCEPTION
--            WHEN OTHERS THEN
--                raise_application_error(-20101, 'Error fetching Accounting staff name');
--        END;




          BEGIN

--            wf_engine.setitemattrtext(c_item_type
--                                   ,lv_item_key
--                                   ,'TITLE'
--                                   ,c_module_title || ' - '
--                                   || lv_research_title
--                                   || '- Fiscal Info has been updated and RIM Transaction has been completed'
--                                    );
            INSERT INTO test_tbl VALUES('result: ' || lv_result, CURRENT_TIMESTAMP);

              wf_notification.setattrtext(nid    => ln_nid
                                         ,aname  => 'RESULT'
                                         ,avalue => lv_result);



          EXCEPTION
            WHEN OTHERS THEN
              raise_application_error(-20105, 'Error encountered on Update Project status:  get and set attributes: ' || SQLERRM);
          END;

          BEGIN
            wf_notification.respond(nid       =>  ln_nid
                                   ,responder =>  lv_approver);
          EXCEPTION
            WHEN OTHERS THEN
               ROLLBACK;
               INSERT INTO test_tbl VALUES('lv_approver: ' || lv_approver, CURRENT_TIMESTAMP);
              raise_application_error(-20105, 'Error encountered sending response' || SQLERRM);
          END;

      END IF;



    EXCEPTION 
      WHEN OTHERS THEN 
        
        raise_application_error(-20100, 'upd_proj_stat error: ' || SQLERRM);


    END upd_proj_stat; 

    PROCEDURE del_records(p_item_key  IN VARCHAR2)
    IS

    BEGIN
        DELETE FROM xxup_rim_header
        WHERE item_key = p_item_key;

        DELETE FROM xxup_rim_fiscal_details
        WHERE item_key = p_item_key;

        DELETE FROM xxup_rim_publication
        WHERE item_key = p_item_key;


        DELETE FROM xxup_rim_team_members 
        WHERE item_key = p_item_key;


        DELETE FROM xxup_rim_funding 
        WHERE item_key = p_item_key;

        DELETE FROM xxup_rim_milestones 
        WHERE item_key = p_item_key;

        DELETE FROM xxup_rim_proj_impact 
        WHERE item_key = p_item_key;
        
        DELETE FROM xxup_per_ps_action_history 
        WHERE item_key = p_item_key;

        COMMIT;
    EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            raise_application_error(-20101, 'Error deleting records');


    END del_records;


    PROCEDURE store_acctg_info(p_item_key VARCHAR2
                            ,p_sp_code_disp VARCHAR2
                            ,p_fund_controller_disp VARCHAR2
                            )
     IS
     BEGIN
        --reset
        DELETE 
        FROM xxup.xxup_rim_acctg_info
        WHERE item_key = p_item_key;

        INSERT INTO xxup.xxup_rim_acctg_info 
        VALUES(p_item_key, p_sp_code_disp, p_fund_controller_disp);

        COMMIT;



     EXCEPTION
        WHEN OTHERS THEN
            ROLLBACK;
            raise_application_error(-20101, 'Error storing accounting info');

     END store_acctg_info;

END xxup_rim_wf_pkg;