package com.tgfc.tw.entity.model.request;

import javax.validation.constraints.Size;

public class PageRequest {

    int page=0;
    int pageSize=0;

    @Size
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }



}
