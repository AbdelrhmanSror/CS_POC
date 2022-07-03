package com.example.content_server.constant;

public enum Status {
    NewCustomer("عميل جديد"),
    CurrentCustomer_NotUpdated("عميل حالي :بطاقه غير محدثه"),
    CurrentCustomer_Updated("عميل حالي :بطاقه محدثه"),
    REJECTED("مرفوض");
    private final String status;

    Status(String status) {
        this.status = status;

    }

    public String getStatus() {
        return status;
    }
}
