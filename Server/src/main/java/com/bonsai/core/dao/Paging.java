package com.bonsai.core.dao;

public class Paging {
    private long page;
    private int pageSize;

    public Paging(long page, int pageSize){
        this.page = page;
        this.pageSize = pageSize;
    }

    public long getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }
}
