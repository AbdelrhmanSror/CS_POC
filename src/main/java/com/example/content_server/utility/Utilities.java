package com.example.content_server.utility;

import com.example.content_server.models.poc.WorkFlowPoc;
import com.example.content_server.models.poc.WorkFlowPocAttribute;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Utilities {

    public static Date getBirthDateUsingIdNumber(String idNumber) {
        String month = idNumber.substring(3, 5);
        String year = idNumber.substring(1, 3);
        String day = idNumber.substring(5, 7);
/*
        2022-06-28 00:00:00.000
*/
        String centuryCode = idNumber.substring(0, 1);
        if (centuryCode.equals("2")) {
            year = "19" + year;

        } else if (centuryCode.equals("3")) {
            year = "20" + year;

        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(year + "-" + month + "-" + day);
        } catch (ParseException e) {
            return null;
        }
        //return year+"-"+month+"-"+day
    }

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
                attribute.setOrderSerialNumber(flowPoc.getWF_ValInt());
            } else if (isCustomerAnnualIncomeSelected(flowPoc)) {
                attribute.setCustomerAnnualIncome(flowPoc.getWF_ValInt());
            } else if (isApproximateMonthlyDepositSelected(flowPoc)) {
                attribute.setApproximateMonthlyDeposit(flowPoc.getWF_ValInt());
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
        return workFlowPoc2.getMyKey().getWF_AttrID() == 17;
    }

    private static boolean isCustomerAnnualIncomeSelected(WorkFlowPoc workFlowPoc2) {
        return workFlowPoc2.getMyKey().getWF_AttrID() == 16;
    }

    private static boolean isOrderSerialNumberSelected(WorkFlowPoc workFlowPoc2) {
        return workFlowPoc2.getMyKey().getWF_AttrID() == 15;
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
