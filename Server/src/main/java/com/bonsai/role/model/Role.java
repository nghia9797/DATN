package com.bonsai.role.model;

import com.bonsai.core.annotation.TableColumn;
import com.bonsai.core.annotation.TableName;
import com.bonsai.core.dao.BonsaiEntity;
import com.bonsai.operation.model.Operation;

import java.util.List;

@TableName(name = "roles", hasCheckTime = false)
public class Role extends BonsaiEntity {
    @TableColumn(name = "name")
    public String name;

    @TableColumn(name = "description")
    public String description;

    @TableColumn(name = "operation_ids")
    public List<Long> operationIds;

    public List<Operation> operations;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Long> getOperationIds() {
        return operationIds;
    }

    public void setOperationIds(List<Long> operationIds) {
        this.operationIds = operationIds;
    }
}
