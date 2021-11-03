create or replace TRIGGER xxup.xxup_rim_header_cls_au
FOR UPDATE ON xxup_rim_header
COMPOUND TRIGGER

    lv_tran_no xxup_rim_header.transaction_no%TYPE;
    l_rc NUMBER;


   TYPE t_rec_t IS TABLE OF xxup_rim_header%ROWTYPE  INDEX BY PLS_INTEGER; 
   t_rec t_rec_t; 
   
   lv_orig_item_key xxup_rim_header.item_key%TYPE;

    AFTER EACH ROW
    IS
    BEGIN
        IF UPDATING THEN
            IF (
                :NEW.approval_status = 'Done' AND
                SUBSTR(:NEW.item_key,1,1) = 'C') THEN
                
                
                t_rec(t_rec.COUNT+1).item_key := :NEW.item_key;
                t_rec(t_rec.COUNT).assignment_id := :NEW.assignment_id;
                t_rec(t_rec.COUNT).research_type := :NEW.research_type;
                t_rec(t_rec.COUNT).research_title := :NEW.research_title;
                t_rec(t_rec.COUNT).research_title2 := :NEW.research_title2;
                t_rec(t_rec.COUNT).research_title3 := :NEW.research_title3;
                t_rec(t_rec.COUNT).brief_description := :NEW.brief_description;
                t_rec(t_rec.COUNT).project_impact_desc := :NEW.project_impact_desc;
                t_rec(t_rec.COUNT).project_leader_id := :NEW.project_leader_id;
                t_rec(t_rec.COUNT).start_date := :NEW.start_date;
                t_rec(t_rec.COUNT).actual_end_date := :NEW.actual_end_date;
                t_rec(t_rec.COUNT).project_status := :NEW.project_status;
                t_rec(t_rec.COUNT).project_remarks := :NEW.project_remarks;
                t_rec(t_rec.COUNT).approval_status := :NEW.approval_status;
                t_rec(t_rec.COUNT).research_type_spec := :NEW.research_type_spec;
                t_rec(t_rec.COUNT).stud_bs_no := :NEW.stud_bs_no;
                t_rec(t_rec.COUNT).stud_ms_no := :NEW.stud_ms_no;
                t_rec(t_rec.COUNT).stud_phd_no := :NEW.stud_phd_no;
                t_rec(t_rec.COUNT).attribute1 := :NEW.attribute1;
                t_rec(t_rec.COUNT).attribute2 := :NEW.attribute2;
                t_rec(t_rec.COUNT).attribute3 := :NEW.attribute3;
                t_rec(t_rec.COUNT).attribute4 := :NEW.attribute4;
                t_rec(t_rec.COUNT).attribute5 := :NEW.attribute5;
                t_rec(t_rec.COUNT).last_update_date := :NEW.last_update_date;
                t_rec(t_rec.COUNT).last_updated_by := :NEW.last_updated_by;
                t_rec(t_rec.COUNT).last_update_login := :NEW.last_update_login;
                
                
            END IF;
        END IF;
    END AFTER EACH ROW;

    AFTER STATEMENT
    IS
    BEGIN
--        dbms_output.put_line('t_rec.COUNT:' || t_rec.COUNT);
        FOR idx IN 1..t_rec.COUNT LOOP

            SELECT transaction_no                                               
            INTO lv_tran_no                                                                                                                   
            FROM xxup_rim_header                                                
            WHERE item_key = t_rec(idx).item_key;

            UPDATE xxup_rim_header main
            SET (project_status
                ,approval_status
                ,assignment_id
                ,research_type
                ,research_title
                ,research_title2
                ,research_title3
                ,brief_description
                ,project_impact_desc
                ,project_leader_id
                ,start_date
                ,actual_end_date
                ,project_remarks
                ,research_type_spec
                ,stud_bs_no
                ,stud_ms_no
                ,stud_phd_no
                ,attribute1
                ,attribute2
                ,attribute3
                ,attribute4
                ,attribute5
                ,last_update_date
                ,last_updated_by
                ,last_update_login)
             =                                                                
            (SELECT 'Completed'
                   ,'Closed'
                   ,t_rec(idx).assignment_id
                    ,t_rec(idx).research_type
                    ,t_rec(idx).research_title
                    ,t_rec(idx).research_title2
                    ,t_rec(idx).research_title3
                    ,t_rec(idx).brief_description
                    ,t_rec(idx).project_impact_desc
                    ,t_rec(idx).project_leader_id
                    ,t_rec(idx).start_date
                    ,t_rec(idx).actual_end_date
                    ,t_rec(idx).project_remarks
                    ,t_rec(idx).research_type_spec
                    ,t_rec(idx).stud_bs_no
                    ,t_rec(idx).stud_ms_no
                    ,t_rec(idx).stud_phd_no
                    ,t_rec(idx).attribute1
                    ,t_rec(idx).attribute2
                    ,t_rec(idx).attribute3
                    ,t_rec(idx).attribute4
                    ,t_rec(idx).attribute5
                    ,t_rec(idx).last_update_date
                    ,t_rec(idx).last_updated_by
                    ,t_rec(idx).last_update_login
            FROM xxup_rim_header tr                                         
            WHERE tr.item_key = t_rec(idx).item_key                                   
            )                                                               
            WHERE main.approval_status = 'Approved'                             
            AND main.transaction_no = lv_tran_no;
            
            --query updated approval status
            SELECT item_key
            INTO lv_orig_item_key
            FROM xxup_rim_header hd                           
            WHERE hd.approval_status = 'Closed' --    
            AND hd.transaction_no = lv_tran_no;
            
            UPDATE xxup_rim_fiscal_details                                      
            SET                                                                 
            (                                                                   
            --rim_fisc_id                                                       
            transaction_no                                                      
            ,constituent_unit                                                   
            ,responsibility_center                                              
            ,collaborating_agency                                               
            ,total_amount                                                       
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
            ,total_amount                                                       
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
                          AND tr.item_key = t_rec(idx).item_key                        
                          )                                                     
            )                                                                   
            WHERE item_key = lv_orig_item_key;     
                                  
            --
            DELETE FROM xxup_rim_team_members main                              
            WHERE item_key = lv_orig_item_key;   

            INSERT INTO xxup_rim_team_members main                              
            SELECT xxup_rim_members_seq.nextval,                                
                line_no,                                                        
                transaction_no,                                                 
                'RIM-' || lv_tran_no item_key,                                  
                full_name,                                                      
                position,                                                       
                organization,                                                   
                project_role,                                                   
                end_date,                                                       
                attribute1,                                                     
                attribute2,                                                     
                attribute3,                                                     
                attribute4,                                                     
                attribute5,                                                     
                last_update_date,                                               
                last_updated_by,                                                
                last_update_login,                                              
                created_by,                                                     
                creation_date                                                   
            FROM xxup_rim_team_members tr                                       
            WHERE item_key = t_rec(idx).item_key;

            --                                                                  
            DELETE FROM xxup_rim_funding main                                   
            WHERE item_key = lv_orig_item_key;      

            INSERT INTO xxup_rim_funding main                                   
            SELECT xxup_rim_funding_seq.nextval,                                
                line_no,                                                        
                transaction_no,                                                 
                'RIM-' || lv_tran_no item_key,                                  
                funding_source,                                                 
                description,                                                    
                amount,                                                         
                currency,
                start_date,                                                     
                end_date,                                                       
                attribute1,                                                     
                attribute2,                                                     
                attribute3,                                                     
                attribute4,                                                     
                attribute5,                                                     
                last_update_date,                                               
                last_updated_by,                                                
                last_update_login,                                              
                created_by,                                                     
                creation_date                                                                                            
            FROM xxup_rim_funding tr                                            
            WHERE item_key = t_rec(idx).item_key;                                                       

            --                                                                  
            DELETE FROM xxup_rim_milestones main                                
            WHERE item_key = lv_orig_item_key;         


            INSERT INTO xxup_rim_milestones main                                
            SELECT xxup_rim_milestone_seq.nextval,                              
                transaction_no,                                                 
                'RIM-' || lv_tran_no item_key,                                  
                line_no,                                                        
                milestone,                                                      
                completion_percentage,                                          
                budget_released_date,                                           
                remarks,                                                        
                attribute1,                                                     
                attribute2,                                                     
                attribute3,                                                     
                attribute4,                                                     
                attribute5,                                                     
                last_update_date,                                               
                last_updated_by,                                                
                last_update_login,                                              
                created_by,                                                     
                creation_date                                                   
            FROM xxup_rim_milestones tr                                         
            WHERE item_key = t_rec(idx).item_key;                                         

            --                                                                  
            DELETE FROM xxup_rim_proj_impact main                               
            WHERE item_key = lv_orig_item_key;          


            INSERT INTO xxup_rim_proj_impact main                               
            SELECT  xxup_rim_proj_impact_seq.nextval,                           
                    line_no,                                                    
                    transaction_no,                                             
                    'RIM-' || lv_tran_no item_key,                              
                    project_impact,                                             
                    selected,                                                   
                    attribute1,                                                 
                    attribute2,                                                 
                    attribute3,                                                 
                    attribute4,                                                 
                    attribute5,                                                 
                    last_update_date,                                           
                    last_updated_by,                                            
                    last_update_login,                                          
                    created_by,                                                 
                    creation_date                                               
            FROM xxup_rim_proj_impact tr                                        
            WHERE item_key = t_rec(idx).item_key;                                         



            DELETE FROM xxup_rim_main_area_int main                             
            WHERE item_key = lv_orig_item_key;            

            INSERT INTO xxup_rim_main_area_int main                             
            SELECT xxup_rim_main_area_int_seq.nextval,                          
                line_no,                                                        
                transaction_no,                                                 
                'RIM-' || lv_tran_no item_key,                                  
                main_area_interest,                                             
                selected,                                                       
                attribute1,                                                     
                attribute2,                                                     
                attribute3,                                                     
                attribute4,                                                     
                attribute5,                                                     
                last_update_date,                                               
                last_updated_by,                                                
                last_update_login,                                              
                created_by,                                                     
                creation_date                                                   
            FROM xxup_rim_main_area_int tr                                      
            WHERE item_key = t_rec(idx).item_key;                                         



            DELETE FROM xxup_rim_dev_goal main                                  
            WHERE item_key = lv_orig_item_key;       

            INSERT INTO xxup_rim_dev_goal main                                  
            SELECT xxup_rim_dev_goal_seq.nextval,                               
                line_no,                                                        
                transaction_no,                                                 
                'RIM-' || lv_tran_no item_key,                                  
                development_goal,                                               
                selected,                                                       
                attribute1,                                                     
                attribute2,                                                     
                attribute3,                                                     
                attribute4,                                                     
                attribute5,                                                     
                last_update_date,                                               
                last_updated_by,                                                
                last_update_login,                                              
                created_by,                                                     
                creation_date                                                   
            FROM xxup_rim_dev_goal tr                                           
            WHERE item_key = t_rec(idx).item_key;                                         

            --                                                           
            DELETE FROM xxup_rim_end_dt_ext main                                
            WHERE item_key = lv_orig_item_key;           


            INSERT INTO xxup_rim_end_dt_ext main                                
            SELECT xxup_rim_end_dt_ext_seq.nextval,                             
                line_no,                                                        
                transaction_no,                                                 
                'RIM-' || lv_tran_no item_key,                                  
                end_date,                                                       
                attribute1,                                                     
                attribute2,                                                     
                attribute3,                                                     
                attribute4,                                                     
                attribute5,                                                     
                last_update_date,                                               
                last_updated_by,                                                
                last_update_login,                                              
                created_by,                                                     
                creation_date                                                   
            FROM xxup_rim_end_dt_ext tr                                         
            WHERE item_key = t_rec(idx).item_key;
            
        END LOOP;
    END AFTER STATEMENT;




END xxup_rim_header_cls_au;
/
CREATE SYNONYM xxup_rim_header_cls_au FOR xxup.xxup_rim_header_cls_au;