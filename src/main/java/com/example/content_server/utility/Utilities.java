package com.example.content_server.utility;

import com.example.content_server.models.poc.WorkFlowPoc;
import com.example.content_server.models.poc.WorkFlowPocAttribute;

import java.util.List;

public class Utilities {

    public static WorkFlowPocAttribute getWorkFlowPocAttributes(List<WorkFlowPoc> workFlowPoc) {

        WorkFlowPocAttribute attribute = new WorkFlowPocAttribute();
        for (WorkFlowPoc flowPoc : workFlowPoc) {
            if (isCustomerNameSelected(flowPoc)) {
                attribute.setCustomerName(flowPoc.getWF_ValStr());
            } else if (isIdNumberSelected(flowPoc)) {
                attribute.setIdNumber(flowPoc.getWF_ValStr());
            } else if (isResidenceSelected(flowPoc)) {
                attribute.setResidence(flowPoc.getWF_ValStr());
            } else if (isAccountCreationBranchSelected(flowPoc)) {
                attribute.setAccountCreationBranch(flowPoc.getWF_ValStr());
            } else if (isOrderSerialNumberSelected(flowPoc)) {
                attribute.setOrderSerialNumber(flowPoc.getWF_ValStr());
            } else if (isCustomerAnnualIncomeSelected(flowPoc)) {
                attribute.setCustomerAnnualIncome(flowPoc.getWF_ValStr());
            } else if (isApproximateMonthlyDepositSelected(flowPoc)) {
                attribute.setApproximateMonthlyDeposit(flowPoc.getWF_ValStr());
            } else if (isNotesSelected(flowPoc)) {
                attribute.setNotes(flowPoc.getWF_ValLong());
            } else if (isStatusSelected(flowPoc)) {
                attribute.setStatus(flowPoc.getWF_ValStr());
            } else if (isBirthDateSelected(flowPoc)) {
                attribute.setBirthDate(flowPoc.getWF_ValDate());
            } else if (isRequestReceivedDateSelected(flowPoc)) {
                attribute.setRequestReceivedDate(flowPoc.getWF_ValDate());
            }
        }

        return attribute;

    }

    private static boolean isRequestReceivedDateSelected(WorkFlowPoc workFlowPoc2) {
        return workFlowPoc2.getMyKey().getWF_AttrID() == 14;
    }

    private static boolean isBirthDateSelected(WorkFlowPoc workFlowPoc2) {
        return workFlowPoc2.getMyKey().getWF_AttrID() == 13;
    }

    private static boolean isStatusSelected(WorkFlowPoc workFlowPoc2) {
        return workFlowPoc2.getMyKey().getWF_AttrID() == 12;
    }

    private static boolean isNotesSelected(WorkFlowPoc workFlowPoc2) {
        return workFlowPoc2.getMyKey().getWF_AttrID() == 11;
    }

    private static boolean isApproximateMonthlyDepositSelected(WorkFlowPoc workFlowPoc2) {
        return workFlowPoc2.getMyKey().getWF_AttrID() == 10;
    }

    private static boolean isCustomerAnnualIncomeSelected(WorkFlowPoc workFlowPoc2) {
        return workFlowPoc2.getMyKey().getWF_AttrID() == 9;
    }

    private static boolean isOrderSerialNumberSelected(WorkFlowPoc workFlowPoc2) {
        return workFlowPoc2.getMyKey().getWF_AttrID() == 8;
    }

    private static boolean isAccountCreationBranchSelected(WorkFlowPoc workFlowPoc2) {
        return workFlowPoc2.getMyKey().getWF_AttrID() == 6;
    }

    private static boolean isResidenceSelected(WorkFlowPoc workFlowPoc2) {
        return workFlowPoc2.getMyKey().getWF_AttrID() == 5;
    }

    private static boolean isIdNumberSelected(WorkFlowPoc workFlowPoc) {
        return workFlowPoc.getMyKey().getWF_AttrID() == 3;
    }

    private static boolean isCustomerNameSelected(WorkFlowPoc workFlowPoc) {
        return workFlowPoc.getMyKey().getWF_AttrID() == 2;
    }


}
