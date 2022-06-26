package com.example.content_server.models.poc;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * model represents the workflow schema in the content server database sql server
 */
@Entity
@Table(name = "WFAttrData")
public class WorkFlowPoc {

    @EmbeddedId
    private PrimaryKeyPoc myKey;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "WF_ValDate")
    private Date WF_ValDate;

    @Column(name = "WF_ValStr")
    private String WF_ValStr;

    @Column(name = "WF_ValLong")
    private String WF_ValLong;

    @Column(name = "WF_ValInt")
    private Integer WF_ValInt;

    public WorkFlowPoc() {

    }

    public String getWF_ValStr() {
        return WF_ValStr;
    }

    public void setWF_ValStr(String WF_ValStr) {
        this.WF_ValStr = WF_ValStr;
    }

    public Date getWF_ValDate() {
        return WF_ValDate;
    }

    public void setWF_ValDate(Date WF_ValDate) {
        this.WF_ValDate = WF_ValDate;
    }

    public PrimaryKeyPoc getMyKey() {
        return myKey;
    }

    public void setMyKey(PrimaryKeyPoc myKey) {
        this.myKey = myKey;
    }

    public String getWF_ValLong() {
        return WF_ValLong;
    }

    public Integer getWF_ValInt() {
        return WF_ValInt;
    }

    public void setWF_ValInt(Integer WF_ValInt) {
        this.WF_ValInt = WF_ValInt;
    }

    public void setWF_ValLong(String WF_ValLong) {
        this.WF_ValLong = WF_ValLong;
    }
}
