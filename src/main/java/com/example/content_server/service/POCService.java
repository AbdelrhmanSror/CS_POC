package com.example.content_server.service;

import com.example.content_server.ContentServerApplication;
import com.example.content_server.constant.DocumentFolder;
import com.example.content_server.constant.NodeType;
import com.example.content_server.constant.Status;
import com.example.content_server.models.Node;
import com.example.content_server.models.ocr.ApprovedOcrCustomer;
import com.example.content_server.models.ocr.OcrCustomer;
import com.example.content_server.models.ocr.RejectedOcrCustomer;
import com.example.content_server.models.workflow.WorkFlowPocAttribute;
import com.example.content_server.repository.ApprovedCustomerRepository;
import com.example.content_server.repository.RejectedOcrCustomerRepository;
import com.example.content_server.repository.WorkFlowPocRepository;
import com.example.content_server.utility.Utilities;
import com.example.content_server.utility.XmlParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.client.HttpServerErrorException;
import org.w3c.dom.Element;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.example.content_server.constant.Constants.*;

public class POCService {

    @Autowired
    private NodeService nodeService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private WorkFlowPocRepository workFlowPocRepository;
    private static final Logger logger = LoggerFactory.getLogger(ContentServerApplication.class);
    @Autowired
    private ApprovedCustomerRepository approvedCustomerRepository;
    @Autowired
    private RejectedOcrCustomerRepository rejectedOcrCustomerRepository;

    public void InitiatePocWorkflow(Integer inputDocId, Integer workFlowId, Integer attachmentFolderId) {
        if (workFlowId != 0 && attachmentFolderId != 0) {
            //initiating workflow
            logger.info("process  =" + workFlowId);
            logger.info("workflow attachment folder id =" + attachmentFolderId);
            //getting the attribute values of the category set on the node that match the name inserted on workflow attribute or return null
            Element element = XmlParser.getParsedXML();
            String name = element.getElementsByTagName("الاسم").item(0).getTextContent();
            String idNumber = element.getElementsByTagName("الرقم_القومى").item(0).getTextContent();
            String address = element.getElementsByTagName("العنوان").item(0).getTextContent();
            ApprovedOcrCustomer approvedOcrCustomerFromDatabase = approvedCustomerRepository.findById(Long.valueOf(idNumber)).orElse(null);
            OcrCustomer ocrCustomerFromXml = new ApprovedOcrCustomer();
            ocrCustomerFromXml.setAddress(address);
            ocrCustomerFromXml.setCustomerName(address);
            ocrCustomerFromXml.setIdNumber(Long.valueOf(idNumber));

            logger.info("name:" + name + " idNumber " + idNumber + "  address" + address + " work flow id" + WORKFLOW_ID + " process work flow id" + workFlowId + " workFlowAttachmentFolderId" + attachmentFolderId);
            logger.info("inputDocID from web report =  " + inputDocId + " inputDocID from calling api   " + nodeService.getSubNode(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), INPUT_FOLDER_ID).getId());

            setStatusBasedOnCondition(Long.valueOf(workFlowId), ocrCustomerFromXml, approvedOcrCustomerFromDatabase);
            updateWorkFlowDataBasedOnDataOnXml(Long.valueOf(workFlowId), name, idNumber, address);
            //downloadInputDocumentFromServerToLocal(inputDocId, name);
        }
    }


    private void updateWorkFlowDataBasedOnDataOnXml(Long processWorkFlowId, String name, String idNumber, String address) {
        workFlowPocRepository.updateCustomerName(processWorkFlowId, name);
        workFlowPocRepository.updateIdNumber(processWorkFlowId, idNumber);
        workFlowPocRepository.updateResidence(processWorkFlowId, address);
        workFlowPocRepository.updateBirthDate(processWorkFlowId, Utilities.getBirthDateUsingIdNumber(idNumber));
    }

    private void setStatusBasedOnCondition(Long processWorkFlowId, OcrCustomer ocrCustomerXml, OcrCustomer ocrCustomerDatabase) {
        if (ocrCustomerDatabase == null) {
            workFlowPocRepository.updateStatus(processWorkFlowId, Status.NewCustomer.getStatus());
        } else if (ocrCustomerDatabase.getAddress().equals(ocrCustomerXml.getAddress())) {
            workFlowPocRepository.updateStatus(processWorkFlowId, Status.CurrentCustomer_NotUpdated.getStatus());
        } else {
            workFlowPocRepository.updateStatus(processWorkFlowId, Status.CurrentCustomer_Updated.getStatus());

        }
    }

    private void downloadInputDocumentFromServerToLocal(Integer inputDocId, String name) {
        Path path = Paths.get("F:\\abdelrhman\\ocr\\input" + "\\" + name + "_" + inputDocId + ".pdf");
        try {
            ByteArrayResource byteArrayResource = nodeService.downloadNode(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), inputDocId);
            System.out.println("byte array resource +" + byteArrayResource);
            Files.write(path, byteArrayResource.getByteArray());
        } catch (IOException | HttpServerErrorException e) {
            e.printStackTrace();
        }
    }

    public void saveID(Integer docId, Integer workFlowId) {
        System.out.println("document id from create and apply =" + docId);
        //getting the attribute of the workflow requested from the database server
        WorkFlowPocAttribute workFlowPocAttribute = Utilities.getWorkFlowPocAttributes(workFlowPocRepository.findAllWorkFlowWIthId(workFlowId));
        //workFlowPocAttribute.setBirthDate(Utilities.getDateFormatString(Utilities.getBirthDateUsingIdNumber(workFlowPocAttribute.getIdNumber()), "yyyy-MM-dd"));
        ApprovedOcrCustomer ocrCustomer = new ApprovedOcrCustomer();
        ocrCustomer.setAddress(workFlowPocAttribute.getResidence());
        ocrCustomer.setCustomerName(workFlowPocAttribute.getCustomerName());
        ocrCustomer.setIdNumber(Long.valueOf(workFlowPocAttribute.getIdNumber()));

        updatingCategoryOnCustomerFolderAndMoveAttachmentToItOnlyIfCustomerExists(docId, workFlowPocAttribute, ocrCustomer);
        createCustomerFolderAndApplyCategoryToItAndMoveAttachmentToItOnlyIfCustomerNotExists(docId, workFlowPocAttribute, ocrCustomer);
        approvedCustomerRepository.save(ocrCustomer);

    }


    private void createCustomerFolderAndApplyCategoryToItAndMoveAttachmentToItOnlyIfCustomerNotExists(Integer docId, WorkFlowPocAttribute workFlowPocAttribute, ApprovedOcrCustomer approvedOcrCustomer) {
        if (workFlowPocAttribute.getStatus().equals(Status.NewCustomer.getStatus())) {
            //create folder with the same name of the customer name
            nodeService.createNode(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), NodeType.FOLDER.getNodeTypeId(), Integer.valueOf(DocumentFolder.Approved.getFolderId()), workFlowPocAttribute.getCustomerName());
            Integer newNodeId = nodeService.getNodeWithName(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), Integer.valueOf(DocumentFolder.Approved.getFolderId()), workFlowPocAttribute.getCustomerName()).getId();
            //apply category on the newly created node
            categoryService.applyPocCategory(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), newNodeId, workFlowPocAttribute);
            //moving the attachment to the location of the newly created folder.
            //renaming the doc name so it be the idNumber_numberOfExistence
            nodeService.renameNode(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), docId, workFlowPocAttribute.getIdNumber() + "_" + approvedOcrCustomer.getExistenceTimes());
            nodeService.moveNode(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), docId, newNodeId);
            approvedOcrCustomer.setExistenceTimes(0);

        }
    }

    private void updatingCategoryOnCustomerFolderAndMoveAttachmentToItOnlyIfCustomerExists(Integer docId, WorkFlowPocAttribute workFlowPocAttribute, ApprovedOcrCustomer approvedOcrCustomer) {
        if (workFlowPocAttribute.getStatus().equals(Status.CurrentCustomer_Updated.getStatus()) || workFlowPocAttribute.getStatus().equals(Status.CurrentCustomer_NotUpdated.getStatus())) {
            Node node = getNodeWithNameInFolder(workFlowPocAttribute.getCustomerName(), Integer.parseInt(DocumentFolder.Approved.getFolderId()));
            //getting the attribute values of the category set on the node that match the name inserted on workflow attribute or return null
            categoryService.updateCategory(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), node.getId(), workFlowPocAttribute);
            //moving the attachment to the the folder only in case if the address has changed
            System.out.println("workFlowPocAttribute=" + workFlowPocAttribute.getResidence() + "   address in database" + workFlowPocAttribute.getResidence());

            if (workFlowPocAttribute.getStatus().equals(Status.CurrentCustomer_Updated.getStatus())) {
                approvedOcrCustomer.setExistenceTimes(1 + approvedCustomerRepository.findRepetitionTimes(approvedOcrCustomer.getIdNumber()));
                System.out.println("adreess has chnaged ");
                //renaming the doc name so it be the idNumber_numberOfExistence
                nodeService.renameNode(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), docId, workFlowPocAttribute.getIdNumber() + "_" + approvedOcrCustomer.getExistenceTimes());
                nodeService.moveNode(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), docId, node.getId());

            }
        }
    }


    private Node getNodeWithNameInFolder(String name, int folderId) {
        return nodeService.getNodeWithName(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), folderId, name);
    }

    public void archiveID(Integer docId, Integer workFlowId) {
        System.out.println("document id from create and apply =" + docId);
        //getting the attribute of the workflow requested from the database server
        WorkFlowPocAttribute workFlowPocAttribute = Utilities.getWorkFlowPocAttributes(workFlowPocRepository.findAllWorkFlowWIthId(workFlowId));
        workFlowPocAttribute.setStatus(Status.REJECTED.getStatus());
        // workFlowPocAttribute.setBirthDate(Utilities.getDateFormatString(Utilities.getBirthDateUsingIdNumber(workFlowPocAttribute.getIdNumber()), "yyyy-MM-dd"));
        logger.info(workFlowPocAttribute.toString());
        RejectedOcrCustomer rejectedOcrCustomerFromXml = new RejectedOcrCustomer();
        rejectedOcrCustomerFromXml.setAddress(workFlowPocAttribute.getResidence());
        rejectedOcrCustomerFromXml.setCustomerName(workFlowPocAttribute.getCustomerName());
        rejectedOcrCustomerFromXml.setIdNumber(Long.valueOf(workFlowPocAttribute.getIdNumber()));
        if (!rejectedOcrCustomerRepository.existsById(rejectedOcrCustomerFromXml.getIdNumber())) {
            nodeService.createNode(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), NodeType.FOLDER.getNodeTypeId(), Integer.valueOf(DocumentFolder.Rejected.getFolderId()), workFlowPocAttribute.getCustomerName());
            Integer newNodeId = nodeService.getNodeWithName(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), Integer.valueOf(DocumentFolder.Rejected.getFolderId()), workFlowPocAttribute.getCustomerName()).getId();
            categoryService.applyPocCategory(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), newNodeId, workFlowPocAttribute);
            rejectedOcrCustomerFromXml.setExistenceTimes(0);
            //renaming the doc name so it be the idNumber_numberOfExistence
            nodeService.renameNode(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), docId, workFlowPocAttribute.getIdNumber() + "_" + rejectedOcrCustomerFromXml.getExistenceTimes());
            nodeService.moveNode(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), docId, newNodeId);
        } else {
            Node node = getNodeWithNameInFolder(workFlowPocAttribute.getCustomerName(), Integer.parseInt(DocumentFolder.Rejected.getFolderId()));
            logger.info(node.toString());
            categoryService.updateCategory(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), node.getId(), workFlowPocAttribute);
            logger.info("adreess has chnaged " + rejectedOcrCustomerFromXml);
            if (!rejectedOcrCustomerFromXml.getAddress().equals(rejectedOcrCustomerRepository.findById(rejectedOcrCustomerFromXml.getIdNumber()).get().getAddress())) {
                rejectedOcrCustomerFromXml.setExistenceTimes(1 + rejectedOcrCustomerRepository.findRepetitionTimes(rejectedOcrCustomerFromXml.getIdNumber()));
                //renaming the doc name so it be the idNumber_numberOfExistence
                nodeService.renameNode(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), docId, workFlowPocAttribute.getIdNumber() + "_" + rejectedOcrCustomerFromXml.getExistenceTimes());
                nodeService.moveNode(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), docId, node.getId());
            }
        }
        rejectedOcrCustomerRepository.save(rejectedOcrCustomerFromXml);


    }

}
