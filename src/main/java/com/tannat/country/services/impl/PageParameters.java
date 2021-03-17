package com.tannat.country.services.impl;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class PageParameters {
    private final String limit;
    private final Integer offset;
    private final Integer sortBy;

    public PageParameters() {
        this(null, null, null);
    }

    public PageParameters(Integer pageNumber, Integer pageSize, Integer sortBy) {
        if (pageNumber != null && pageSize != null && pageNumber >= 0 && pageSize > 0) {
            this.limit = " limit " + pageSize;
            this.offset = pageNumber * pageSize;
        } else {
            this.limit = "";
            this.offset = 0;
        }
        this.sortBy = sortBy == null ? 1 : sortBy;
    }
}
