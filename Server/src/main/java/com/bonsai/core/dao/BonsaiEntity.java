package com.bonsai.core.dao;

import com.bonsai.core.annotation.CheckTime;
import com.bonsai.core.annotation.ID;
import com.bonsai.core.annotation.TableColumn;

public class BonsaiEntity {
    @ID
    @TableColumn(name="id")
    public Long id;
    @CheckTime
    @TableColumn(name = "created")
    public Long created;
    @CheckTime
    @TableColumn(name = "updated")
    public Long updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }
}
