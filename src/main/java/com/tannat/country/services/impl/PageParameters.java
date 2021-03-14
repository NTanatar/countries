package com.tannat.country.services.impl;

import lombok.Getter;

@Getter
public class PageParameters {
    private final Integer limit;
    private final Integer offset;
    private final Integer sortBy;

    public PageParameters(Integer pageNumber, Integer pageSize, Integer sortBy) {
        if (pageNumber != null && pageSize != null && pageNumber >= 0 && pageSize > 0) {
            this.limit = pageSize;
            this.offset = pageNumber * pageSize;
        } else {
            this.limit = 0;
            this.offset = 0;
        }
        this.sortBy = sortBy == null ? 1 : sortBy;
    }
}
