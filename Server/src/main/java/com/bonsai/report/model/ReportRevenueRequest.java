package com.bonsai.report.model;

public class ReportRevenueRequest {
    public Long treeType;
    public Long from;
    public Long to;

    public Long getTreeType() {
        return treeType;
    }

    public void setTreeType(Long treeType) {
        this.treeType = treeType;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }
}
