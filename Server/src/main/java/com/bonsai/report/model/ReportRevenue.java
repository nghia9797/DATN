package com.bonsai.report.model;

public class ReportRevenue {
    private String treeTypeName;
    private String treeName;
    private String treeCode;
    private Long totalCount;
    private Long totalAmount;

    public String getTreeTypeName() {
        return treeTypeName;
    }

    public void setTreeTypeName(String treeTypeName) {
        this.treeTypeName = treeTypeName;
    }

    public String getTreeName() {
        return treeName;
    }

    public void setTreeName(String treeName) {
        this.treeName = treeName;
    }

    public String getTreeCode() {
        return treeCode;
    }

    public void setTreeCode(String treeCode) {
        this.treeCode = treeCode;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }
}
