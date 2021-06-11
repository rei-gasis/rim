create or replace PACKAGE xxup_rim_wf_pkg
IS
    PROCEDURE init_wf(p_tran_no IN VARCHAR2
                     ,p_item_key  IN VARCHAR2
                     ,p_process IN VARCHAR2 DEFAULT 'CREATE'
                     );

    PROCEDURE set_attributes(itemtype  IN VARCHAR2
                            ,l_itemkey IN VARCHAR2
                            ,actid     IN VARCHAR2
                            ,funcmode  IN VARCHAR2
                            ,resultout OUT VARCHAR2);
    PROCEDURE init_approvers(p_assignment_id IN VARCHAR2
                            ,p_tran_no        IN VARCHAR2
                            ,p_action        IN VARCHAR2
                            ,p_item_key      OUT VARCHAR2);

    PROCEDURE update_status(itemtype  IN VARCHAR2
                            ,l_itemkey IN VARCHAR2
                            ,actid     IN VARCHAR2
                            ,funcmode  IN VARCHAR2
                            ,resultout OUT VARCHAR2);
                            

    PROCEDURE resubmit(p_item_key VARCHAR2);

    FUNCTION create_item_key(p_tran_no        IN VARCHAR2
                            ,p_action        IN VARCHAR2                
                            )
    RETURN VARCHAR2;
    
--    PROCEDURE upd_acctg_info(itemtype  IN VARCHAR2
--                            ,l_itemkey IN VARCHAR2
--                            ,actid     IN VARCHAR2
--                            ,funcmode  IN VARCHAR2
--                            ,resultout OUT VARCHAR2);
    PROCEDURE upd_acctg_info(p_item_key  IN VARCHAR2);
    
    
    PROCEDURE upd_proj_stat(p_item_key VARCHAR2
                            ,p_proj_status VARCHAR2
                            );
                            
    PROCEDURE del_records(p_item_key  IN VARCHAR2);
    
    
    PROCEDURE store_acctg_info(p_item_key VARCHAR2
                            ,p_sp_code_disp VARCHAR2
                            ,p_fund_controller_disp VARCHAR2
                            );
END xxup_rim_wf_pkg;