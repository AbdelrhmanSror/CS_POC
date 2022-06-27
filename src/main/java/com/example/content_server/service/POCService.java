package com.example.content_server.service;

import com.example.content_server.constant.DocumentFolder;
import com.example.content_server.constant.NodeType;
import com.example.content_server.constant.Status;
import com.example.content_server.models.Node;
import com.example.content_server.models.OcrCustomer;
import com.example.content_server.models.poc.WorkFlowPocAttribute;
import com.example.content_server.repository.CustomerRepository;
import com.example.content_server.repository.WorkFlowPocRepository;
import com.example.content_server.utility.Utilities;
import com.example.content_server.utility.XmlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.HttpServerErrorException;
import org.w3c.dom.Element;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

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
    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private CustomerRepository customerRepository;


    public void InitiatePocWorkflow(Integer inputDocId, Integer workFlowId, Integer attachmentFolderId) {
        if (workFlowId != 0 && attachmentFolderId != 0) {
            // nodeService.callWebReport(authenticationService.getToken(ADMIN_USER_NAME,ADMIN_PASSWORD),820759);
            //initiating workflow
            System.out.println("process  =" + workFlowId);
            System.out.println("workflow attachment folder id =" + attachmentFolderId);
            //getting the attribute values of the category set on the node that match the name inserted on workflow attribute or return null
            Element element = XmlParser.getParsedXML();
            String name = element.getElementsByTagName("الاسم").item(0).getTextContent();
            String idNumber = element.getElementsByTagName("الرقم_القومى").item(0).getTextContent();
            String address = element.getElementsByTagName("العنوان").item(0).getTextContent();
            Optional<OcrCustomer> ocrCustomerFromDatabase = customerRepository.findById(Long.valueOf(idNumber));
            OcrCustomer ocrCustomerFromXml = new OcrCustomer();
            ocrCustomerFromXml.setAddress(address);
            ocrCustomerFromXml.setCustomerName(address);
            ocrCustomerFromXml.setIdNumber(Long.valueOf(idNumber));

            System.out.println("name:" + name + " idNumber " + idNumber + "  address" + address + " work flow id" + WORKFLOW_ID + " process work flow id" + workFlowId + " workFlowAttachmentFolderId" + attachmentFolderId);
            System.out.println("inputDocID from web report =  " + inputDocId + " inputDocID from calling api   " + nodeService.getSubNode(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), INPUT_FOLDER_ID).getId());
            // WorkFlowPocAttribute contentServerPocCategoryAttribute = nodeService.getNodeWithName(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), Integer.valueOf(DocumentFolder.Approved.getFolderId()), name).getWorkFlowPocAttribute();


            setStatusBasedOnCondition(Long.valueOf(workFlowId), ocrCustomerFromXml, ocrCustomerFromDatabase);
            updateWorkFlowDataBasedOnDataOnXml(Long.valueOf(workFlowId), name, idNumber, address);
            downloadInputDocumentFromServerToLocal(inputDocId, name);

            //nodeService.copyNode(authenticationService.getToken(ABE_BRANCH_MAKER_USER_NAME,ABE_BRANCH_MAKER_PASSWORD),workFlowAttachmentFolderId,inputDocId);
            //  moveInputDocumentToWorkFlowAttachmentFolder(workFlowAttachmentFolderId, inputDocId);

        }
    }


    private void updateWorkFlowDataBasedOnDataOnXml(Long processWorkFlowId, String name, String idNumber, String address) {
        workFlowPocRepository.updateCustomerName(processWorkFlowId, name);
        workFlowPocRepository.updateIdNumber(processWorkFlowId, idNumber);
        workFlowPocRepository.updateResidence(processWorkFlowId, address);
        workFlowPocRepository.updateBirthDate(processWorkFlowId, Utilities.getBirthDateUsingIdNumber(idNumber));
    }

    private void setStatusBasedOnCondition(Long processWorkFlowId, OcrCustomer ocrCustomerXml, Optional<OcrCustomer> ocrCustomerDatabase) {
        if (!ocrCustomerDatabase.isPresent()) {
            workFlowPocRepository.updateStatus(processWorkFlowId, Status.NewCustomer.getStatus());
        } else if (ocrCustomerDatabase.get().getAddress().equals(ocrCustomerXml.getAddress())) {
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

    public void createNodeAndApplyCategory(Integer docId, Integer workFlowId) {
        System.out.println("document id from create and apply =" + docId);
        //getting the attribute of the workflow requested from the database server
        WorkFlowPocAttribute workFlowPocAttribute = Utilities.getWorkFlowPocAttributes(workFlowPocRepository.findAllWorkFlowWIthId(workFlowId));
        OcrCustomer ocrCustomerFromXml = new OcrCustomer();
        ocrCustomerFromXml.setAddress(workFlowPocAttribute.getResidence());
        ocrCustomerFromXml.setCustomerName(workFlowPocAttribute.getCustomerName());
        ocrCustomerFromXml.setIdNumber(Long.valueOf(workFlowPocAttribute.getIdNumber()));
        System.out.println("before " + ocrCustomerFromXml);
        customerRepository.save(ocrCustomerFromXml);

        //get the name of the attachment document.
        String docName = getNameOfAttachmentDocument(docId);


        // updating the name of the attachment to avoid conflict of names.
        //updatingNameOfAttachment(docId, docName);
        updatingCategoryOnCustomerFolderAndMoveAttachmentToItOnlyIfCustomerExists(docId, workFlowPocAttribute);
        createCustomerFolderInApprovedFolderAndApplyCategoryToItAndMoveAttachmentToItOnlyIfCustomerNotExists(docId, workFlowPocAttribute);
    }

    private void moveInputDocumentToWorkFlowAttachmentFolder(Integer workFlowAttachmentFolderId, Integer inputDocId) {
        System.out.println("doc id from input folder " + inputDocId);
        try {
            nodeService.moveNode(authenticationService.getToken(ABE_BRANCH_MAKER_USER_NAME, ABE_BRANCH_MAKER_PASSWORD), inputDocId, workFlowAttachmentFolderId);

        } catch (HttpServerErrorException e) {
            e.printStackTrace();
        }


    }

    private boolean isAddressTheSame(String address, WorkFlowPocAttribute contentServerPocCategoryAttribute) {
        return address.equals(contentServerPocCategoryAttribute.getResidence());
    }

    private boolean isNotExist(WorkFlowPocAttribute contentServerPocCategoryAttribute) {
        return contentServerPocCategoryAttribute == null;
    }


    private void createCustomerFolderInApprovedFolderAndApplyCategoryToItAndMoveAttachmentToItOnlyIfCustomerNotExists(Integer docId, WorkFlowPocAttribute workFlowPocAttribute) {
        if (workFlowPocAttribute.getStatus().equals(Status.NewCustomer.getStatus())) {
            //create folder with the same name of the customer name
            nodeService.createNode(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), NodeType.FOLDER.getNodeTypeId(), Integer.valueOf(DocumentFolder.Approved.getFolderId()), workFlowPocAttribute.getCustomerName());
            Integer newNodeId = nodeService.getNodeWithName(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), Integer.valueOf(DocumentFolder.Approved.getFolderId()), workFlowPocAttribute.getCustomerName()).getId();
            //apply category on the newly created node
            categoryService.applyPocCategory(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), newNodeId, workFlowPocAttribute);
            //moving the attachment to the location of the newly created folder.
            nodeService.moveNode(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), docId, newNodeId);
        }
    }

    private void updatingCategoryOnCustomerFolderAndMoveAttachmentToItOnlyIfCustomerExists(Integer docId, WorkFlowPocAttribute workFlowPocAttribute) {
        if (workFlowPocAttribute.getStatus().equals(Status.CurrentCustomer_Updated.getStatus()) || workFlowPocAttribute.getStatus().equals(Status.CurrentCustomer_NotUpdated.getStatus())) {
            Node node = getNodeWithNameInTheApprovedFolder(workFlowPocAttribute.getCustomerName());
            //getting the attribute values of the category set on the node that match the name inserted on workflow attribute or return null
            categoryService.updateCategory(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), node.getId(), workFlowPocAttribute);
            //moving the attachment to the the folder only in case if the address has changed
            System.out.println("workFlowPocAttribute=" + workFlowPocAttribute.getResidence() + "   address in database" + workFlowPocAttribute.getResidence());

            if (workFlowPocAttribute.getStatus().equals(Status.CurrentCustomer_Updated.getStatus())) {
                System.out.println("adreess has chnaged ");
                nodeService.moveNode(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), docId, node.getId());

            }
        }
    }

    private void updatingNameOfAttachment(@PathVariable(name = "documentID") Integer docId, String docName) {
        nodeService.renameNode(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), docId, docName + "_" + docId);
    }

    private String getNameOfAttachmentDocument(@PathVariable(name = "documentID") Integer docId) {
        return nodeService.getNodeWitID(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), docId).getName();
    }

    private Node getNodeWithNameInTheApprovedFolder(String name) {
        return nodeService.getNodeWithName(authenticationService.getToken(ADMIN_USER_NAME, ADMIN_PASSWORD), Integer.valueOf(DocumentFolder.Approved.getFolderId()), name);
    }

}
