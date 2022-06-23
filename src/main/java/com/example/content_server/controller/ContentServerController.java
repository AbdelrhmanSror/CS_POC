package com.example.content_server.controller;

import com.example.content_server.constant.DocumentFolder;
import com.example.content_server.constant.NodeType;
import com.example.content_server.models.Node;
import com.example.content_server.models.poc.WorkFlowPocAttribute;
import com.example.content_server.repository.WorkFlowPocRepository;
import com.example.content_server.service.*;
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
    private PermissionService permissionService;
    @Autowired
    private WorkflowService workflowService;

    @PostMapping("/InitiatePocWorkflow/{inputDocId}")
    public void InitiatePocWorkflow(@PathVariable(name = "inputDocId") Long inputDocId) {
        //intiating workflow
        String processWorkFlowId = workflowService.initiate(WORKFLOW_ID);
        String workFlowAttachmentId = workflowService.getWorkFlowAttachmentFolderId(processWorkFlowId);
        List<String> docsIdFromInputFolder = nodeService.getSubNode(authenticationService.getToken(USER_NAME, PASSWORD), INPUT_FOLDER_ID).getIds();
        if (!docsIdFromInputFolder.isEmpty())
            nodeService.moveNode(authenticationService.getToken(USER_NAME, PASSWORD), docsIdFromInputFolder.get(0), workFlowAttachmentId);
        //getting the attribute values of the category set on the node that match the name inserted on workflow attribute or return null
        Element element = XmlParser.getParsedXML();
        String name = element.getElementsByTagName("الاسم").item(0).getTextContent();
        String idNumber = element.getElementsByTagName("الرقم_القومى").item(0).getTextContent();
        String address = element.getElementsByTagName("العنوان").item(0).getTextContent();
        Path path = Paths.get("F:\\abdelrhman\\ocr\\input" + "\\" + name + ".pdf");
        try {
            Files.write(path, nodeService.downloadNode(authenticationService.getToken(USER_NAME, PASSWORD), inputDocId).getByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(name + " " + idNumber + " " + address);
        WorkFlowPocAttribute contentServerPocCategoryAttribute = nodeService.getNodeWithName(authenticationService.getToken(USER_NAME, PASSWORD), DocumentFolder.Approved.getFolderId(), name).getWorkFlowPocAttribute();
        if (isNotExist(contentServerPocCategoryAttribute)) {
            workFlowPocRepository.updateStatus(Long.parseLong(processWorkFlowId), "عميل جديد");
        } else if (isAddressTheSame(address, contentServerPocCategoryAttribute)) {
            workFlowPocRepository.updateStatus(Long.parseLong(processWorkFlowId), "عميل حالي :بطاقه غير محدثه");
        } else {
            workFlowPocRepository.updateStatus(Long.parseLong(processWorkFlowId), "عميل حالي :بطاقه محدثه");

        }
        workFlowPocRepository.updateCustomerName(Long.parseLong(processWorkFlowId), name);
        workFlowPocRepository.updateIdNumber(Long.parseLong(processWorkFlowId), idNumber);
        workFlowPocRepository.updateResidence(Long.parseLong(processWorkFlowId), address);


        System.out.println("name:" + name + " idNumber " + idNumber + "  address" + address + " work flow id" + WORKFLOW_ID + " process work flow id" + processWorkFlowId + " workFlowAttachmentId" + workFlowAttachmentId);

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
        Node node = nodeService.getNodeWithName(authenticationService.getToken(USER_NAME, PASSWORD), DocumentFolder.Approved.getFolderId(), workFlowPocAttribute.getCustomerName());

        //get the name of the attachment document.
        String docName = nodeService.getNodeWitID(authenticationService.getToken(USER_NAME, PASSWORD), docId).getName();
        // updating the name of the attachment to avoid conflict of names.
        nodeService.renameNode(authenticationService.getToken(USER_NAME, PASSWORD), docId, docName + "_" + docId);

        //check if the node already exist or not,if it exists so we update the category, otherwise we create a new node and  apply  category
        if (node.isExist()) {
            categoryService.updateCategory(authenticationService.getToken(USER_NAME, PASSWORD), node.getId(), workFlowPocAttribute);
            //moving the attachment to the the folder
            nodeService.moveNode(authenticationService.getToken(USER_NAME, PASSWORD), docId, node.getId());

        } else {
            //create folder with the same name of the customer name
            nodeService.createNode(authenticationService.getToken(USER_NAME, PASSWORD), NodeType.FOLDER.getNodeTypeId(), DocumentFolder.Approved.getFolderId(), workFlowPocAttribute.getCustomerName());
            String newNodeId = nodeService.getNodeWithName(authenticationService.getToken(USER_NAME, PASSWORD), DocumentFolder.Approved.getFolderId(), workFlowPocAttribute.getCustomerName()).getId();
            //apply category on the newly created node
            categoryService.applyPocCategory(authenticationService.getToken(USER_NAME, PASSWORD), newNodeId, workFlowPocAttribute);
            //moving the attachment to the location of the newly created folder.
            nodeService.moveNode(authenticationService.getToken(USER_NAME, PASSWORD), docId, newNodeId);
        }
    }


    @GetMapping(value = "/getNode")
    public Node getNodeId(@RequestParam("parentNodeId") String parentNodeId,
                          @RequestParam("nodeName") String nodeName) {
        return nodeService.getNodeWithName(authenticationService.getToken("Admin", "Asset99a"), parentNodeId, nodeName);
    }

}
