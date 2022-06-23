package com.example.content_server.models.poc;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class PrimaryKeyPoc implements Serializable {
    @Column(name = "WF_ID", nullable = false)
    private Long WF_ID;

    @Column(name = "WF_AttrID", nullable = false)
    private Integer WF_AttrID;

    public Long getWF_ID() {
        return WF_ID;
    }

    public void setWF_ID(Long WF_ID) {
        this.WF_ID = WF_ID;
    }

    public Integer getWF_AttrID() {
        return WF_AttrID;
    }

    public void setWF_AttrID(Integer WF_AttrID) {
        this.WF_AttrID = WF_AttrID;
    }
}