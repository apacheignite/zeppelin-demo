package org.apache.ignite.zeppelin;

import org.apache.ignite.cache.query.annotations.*;

import java.io.*;

public class Organization implements Serializable {
    @QuerySqlField(index = true)
    private long id;

    @QuerySqlField
    private String name;

    public Organization(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }
}
