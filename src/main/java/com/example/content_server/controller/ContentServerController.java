package com.example.content_server.controller;

import com.example.content_server.constant.DocumentFolder;
import com.example.content_server.constant.NodeType;
import com.example.content_server.models.Node;
import com.example.content_server.models.poc.WorkFlowPocAttribute;
import com.example.content_server.repository.WorkFlowPocRepository;
import com.example.content_server.service.AuthenticationService;
import com.example.content_server.service.CategoryService;
import com.example.content_server.service.NodeService;
import com.example.content_server.service.WorkflowService;
import com.example.content_server.utility.Utilities;
import com.example.content_server.utility.XmlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Element;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.example.content_server.constant.Constants.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ContentServerController {

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

    @PostMapping("/InitiatePocWorkflow/{inputDocId}")
    public void InitiatePocWorkflow(@PathVariable(name = "inputDocId") Long inputDocId) {
        //initiating workflow
        String processWorkFlowId = workflowService.initiate(WORKFLOW_ID);

        String workFlowAttachmentFolderId = workflowService.getWorkFlowAttachmentFolderId(processWorkFlowId);
        moveInputDocumentToWorkFlowAttachmentFolder(workFlowAttachmentFolderId);
        //getting the attribute values of the category set on the node that match the name inserted on workflow attribute or return null
        Element element = XmlParser.getParsedXML();
        String name = element.getElementsByTagName("الاسم").item(0).getTextContent();
        String idNumber = element.getElementsByTagName("الرقم_القومى").item(0).getTextContent();
        String address = element.getElementsByTagName("العنوان").item(0).getTextContent();
        downloadInputDocumentFromServerToLocal(inputDocId, name);
        WorkFlowPocAttribute contentServerPocCategoryAttribute = nodeService.getNodeWithName(authenticationService.getToken(USER_NAME, PASSWORD), DocumentFolder.Approved.getFolderId(), name).getWorkFlowPocAttribute();
        setStatusBasedOnCondition(processWorkFlowId, address, contentServerPocCategoryAttribute);
        updateWorkFlowDataBasedOnDataOnXml(processWorkFlowId, name, idNumber, address);


        System.out.println("name:" + name + " idNumber " + idNumber + "  address" + address + " work flow id" + WORKFLOW_ID + " process work flow id" + processWorkFlowId + " workFlowAttachmentFolderId" + workFlowAttachmentFolderId);

    }

    private void updateWorkFlowDataBasedOnDataOnXml(String processWorkFlowId, String name, String idNumber, String address) {
        workFlowPocRepository.updateCustomerName(Long.parseLong(processWorkFlowId), name);
        workFlowPocRepository.updateIdNumber(Long.parseLong(processWorkFlowId), idNumber);
        workFlowPocRepository.updateResidence(Long.parseLong(processWorkFlowId), address);
    }

    private void setStatusBasedOnCondition(String processWorkFlowId, String address, WorkFlowPocAttribute contentServerPocCategoryAttribute) {
        if (isNotExist(contentServerPocCategoryAttribute)) {
            workFlowPocRepository.updateStatus(Long.parseLong(processWorkFlowId), "عميل جديد");
        } else if (isAddressTheSame(address, contentServerPocCategoryAttribute)) {
            workFlowPocRepository.updateStatus(Long.parseLong(processWorkFlowId), "عميل حالي :بطاقه غير محدثه");
        } else {
            workFlowPocRepository.updateStatus(Long.parseLong(processWorkFlowId), "عميل حالي :بطاقه محدثه");

        }
    }

    private void downloadInputDocumentFromServerToLocal(@PathVariable(name = "inputDocId") Long inputDocId, String name) {
        Path path = Paths.get("F:\\abdelrhman\\ocr\\input" + "\\" + name + ".pdf");
        try {
            Files.write(path, nodeService.downloadNode(authenticationService.getToken(USER_NAME, PASSWORD), inputDocId).getByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void moveInputDocumentToWorkFlowAttachmentFolder(String workFlowAttachmentFolderId) {
        List<String> docsIdFromInputFolder = nodeService.getSubNode(authenticationService.getToken(USER_NAME, PASSWORD), INPUT_FOLDER_ID).getIds();
        if (!docsIdFromInputFolder.isEmpty())
            nodeService.moveNode(authenticationService.getToken(USER_NAME, PASSWORD), docsIdFromInputFolder.get(0), workFlowAttachmentFolderId);
    }

    private boolean isAddressTheSame(String address, WorkFlowPocAttribute contentServerPocCategoryAttribute) {
        return address.equals(contentServerPocCategoryAttribute.getResidence());
    }

    private boolean isNotExist(WorkFlowPocAttribute contentServerPocCategoryAttribute) {
        return contentServerPocCategoryAttribute == null;
    }


    @PostMapping("/PocApproved/{documentID}")
    public void createNodeAndApplyCategory(@PathVariable(name = "documentID") String docId) {
        Long workFlowId = workFlowPocRepository.getWorkFlowId();
        //getting the attribute of the workflow requested from the database server
        WorkFlowPocAttribute workFlowPocAttribute = Utilities.getWorkFlowPocAttributes(workFlowPocRepository.findAllWorkFlowWIthId(workFlowId));
        Node node = getNodeWithNameInTheApprovedFolder(workFlowPocAttribute);

        //get the name of the attachment document.
        String docName = getNameOfAttachmentDocument(docId);
        // updating the name of the attachment to avoid conflict of names.
        updatingNameOfAttachment(docId, docName);

        //check if the node already exist or not,if it exists so we update the category, otherwise we create a new node and  apply  category
        if (node.isExist()) {
            updatingCategoryOnCustomerFolderAndMoveAttachmentToIt(docId, workFlowPocAttribute, node);

        } else {
            createCustomerFolderInApprovedFolderAndApplyCategoryToItAndMoveAttachmentToIt(docId, workFlowPocAttribute);
        }
    }

    private void createCustomerFolderInApprovedFolderAndApplyCategoryToItAndMoveAttachmentToIt(@PathVariable(name = "documentID") String docId, WorkFlowPocAttribute workFlowPocAttribute) {
        //create folder with the same name of the customer name
        nodeService.createNode(authenticationService.getToken(USER_NAME, PASSWORD), NodeType.FOLDER.getNodeTypeId(), DocumentFolder.Approved.getFolderId(), workFlowPocAttribute.getCustomerName());
        String newNodeId = nodeService.getNodeWithName(authenticationService.getToken(USER_NAME, PASSWORD), DocumentFolder.Approved.getFolderId(), workFlowPocAttribute.getCustomerName()).getId();
        //apply category on the newly created node
        categoryService.applyPocCategory(authenticationService.getToken(USER_NAME, PASSWORD), newNodeId, workFlowPocAttribute);
        //moving the attachment to the location of the newly created folder.
        nodeService.moveNode(authenticationService.getToken(USER_NAME, PASSWORD), docId, newNodeId);
    }

    private void updatingCategoryOnCustomerFolderAndMoveAttachmentToIt(@PathVariable(name = "documentID") String docId, WorkFlowPocAttribute workFlowPocAttribute, Node node) {
        categoryService.updateCategory(authenticationService.getToken(USER_NAME, PASSWORD), node.getId(), workFlowPocAttribute);
        //moving the attachment to the the folder
        nodeService.moveNode(authenticationService.getToken(USER_NAME, PASSWORD), docId, node.getId());
    }

    private void updatingNameOfAttachment(@PathVariable(name = "documentID") String docId, String docName) {
        nodeService.renameNode(authenticationService.getToken(USER_NAME, PASSWORD), docId, docName + "_" + docId);
    }

    private String getNameOfAttachmentDocument(@PathVariable(name = "documentID") String docId) {
        return nodeService.getNodeWitID(authenticationService.getToken(USER_NAME, PASSWORD), docId).getName();
    }

    private Node getNodeWithNameInTheApprovedFolder(WorkFlowPocAttribute workFlowPocAttribute) {
        return nodeService.getNodeWithName(authenticationService.getToken(USER_NAME, PASSWORD), DocumentFolder.Approved.getFolderId(), workFlowPocAttribute.getCustomerName());
    }


    @GetMapping(value = "/getNode")
    public Node getNodeId(@RequestParam("parentNodeId") String parentNodeId,
                          @RequestParam("nodeName") String nodeName) {
        return nodeService.getNodeWithName(authenticationService.getToken("Admin", "Asset99a"), parentNodeId, nodeName);
    }

}
