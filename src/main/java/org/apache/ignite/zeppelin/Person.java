package org.apache.ignite.zeppelin;

import org.apache.ignite.cache.query.annotations.*;

import java.io.*;

public class Person implements Serializable {
    @QuerySqlField(index = true)
    private long id;

    @QuerySqlField
    private String name;

    @QuerySqlField(index = true)
    private long orgId;

    @QuerySqlField(index = true)
    private Long managerId;

    @QuerySqlField(index = true)
    private int salary;

    public Person(long id, String name, int orgId, Long managerId, int salary) {
        this.id = id;
        this.name = name;
        this.orgId = orgId;
        this.managerId = managerId;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public long getOrgId() {
        return orgId;
    }
}
