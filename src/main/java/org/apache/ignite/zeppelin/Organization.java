package org.apache.ignite.zeppelin;

import java.io.Serializable;
import org.apache.ignite.cache.query.annotations.QuerySqlField;

public class Organization implements Serializable {
    @QuerySqlField(index = true)
    private long id;

    @QuerySqlField(index = true)
    private String name;

    public Organization(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }
}
