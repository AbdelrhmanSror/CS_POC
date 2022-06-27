package com.example.content_server.repository;

import com.example.content_server.models.poc.PrimaryKeyPoc;
import com.example.content_server.models.poc.WorkFlowPoc;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public interface WorkFlowPocRepository extends CrudRepository<WorkFlowPoc, PrimaryKeyPoc> {

    @Query(value = "select WF_ID,WF_AttrID,WF_ValDate,WF_ValStr,WF_ValLong,WF_ValInt from WFAttrData where WF_ID=:id", nativeQuery = true)
    List<WorkFlowPoc> findAllWorkFlowWIthId(@Param("id") long id);

    @Modifying
    @Transactional
    @Query(value = "update  WFAttrData set WF_ValStr=:status where WF_ID=:wf_id and WF_AttrID=12 ", nativeQuery = true)
    void updateStatus(@Param("wf_id") long id, @Param("status") String status);

    @Modifying
    @Transactional
    @Query(value = "update  WFAttrData set WF_ValDate=:birthDate where WF_ID=:wf_id and WF_AttrID=13 ", nativeQuery = true)
    void updateBirthDate(@Param("wf_id") long id, @Param("birthDate") Date birthDate);

    @Modifying
    @Transactional
    @Query(value = "update  WFAttrData set WF_ValStr=:customerName where WF_ID=:wf_id and WF_AttrID=2 ", nativeQuery = true)
    void updateCustomerName(@Param("wf_id") long id, @Param("customerName") String customerName);

    @Modifying
    @Transactional
    @Query(value = "update  WFAttrData set WF_ValStr=:idNumber where WF_ID=:wf_id and WF_AttrID=3 ", nativeQuery = true)
    void updateIdNumber(@Param("wf_id") long id, @Param("idNumber") String idNumber);

    @Modifying
    @Transactional
    @Query(value = "update  WFAttrData set WF_ValStr=:residence where WF_ID=:wf_id and WF_AttrID=5 ", nativeQuery = true)
    void updateResidence(@Param("wf_id") long id, @Param("residence") String residence);


    @Query(value = "select  TOP (1) SubWork_WorkID from WSubWork where SubWork_Title='POC_workflow' order by SubWork_DateInitiated desc", nativeQuery = true)
    Long getWorkFlowId();

    @Query(value = "with WF_CTE (WF_PROCESS)\n" +
            "AS \n" +
            "(select   TOP (1) SubWork_WorkID from WSubWork where SubWork_Title='POC_workflow'  order by SubWork_DateInitiated desc)\n" +
            "(select Data_UserData  from WData,WF_CTE where Data_Type = 1 AND Data_SubType = 1 AND Data_WorkID =WF_PROCESS)\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n", nativeQuery = true)
    Integer getWorkFlowAttachmentFolderId(@Param("workFlowProcessId") Long workFlowProcessId);


}
