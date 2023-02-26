package com.bonsai.tree.model;

import com.bonsai.core.annotation.TableColumn;
import com.bonsai.core.annotation.TableName;
import com.bonsai.core.dao.BonsaiEntity;

import java.util.List;

@TableName(name = "trees", hasCheckTime = true)
public class Tree extends BonsaiEntity {
    @TableColumn(name = "code")
    public String code;

    @TableColumn(name = "name")
    public String name;

    @TableColumn(name = "cost")
    public Long cost;

    @TableColumn(name = "discount")
    public Integer discount;

    @TableColumn(name = "description")
    public String description;

    @TableColumn(name = "count")
    public Long count;

    @TableColumn(name = "type_id")
    public Long typeId;

    @TableColumn(name = "images")
    public List<String> images;

    public TreeType treeType;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
