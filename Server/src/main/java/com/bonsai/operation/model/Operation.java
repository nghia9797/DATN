package com.bonsai.operation.model;

import com.bonsai.core.annotation.TableColumn;
import com.bonsai.core.annotation.TableName;
import com.bonsai.core.dao.BonsaiEntity;

@TableName(name = "operations", hasCheckTime = false)
public class Operation extends BonsaiEntity {
    @TableColumn(name = "alias")
    public String alias;

    @TableColumn(name = "name")
    public String name;

    @TableColumn(name = "action")
    public String action;

    @TableColumn(name = "description")
    public String description;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
