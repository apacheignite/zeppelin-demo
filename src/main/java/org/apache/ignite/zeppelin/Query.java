package org.apache.ignite.zeppelin;

import com.google.gson.*;
import org.apache.ignite.*;
import org.apache.ignite.cache.affinity.*;
import org.apache.ignite.cache.query.*;
import org.apache.ignite.lang.*;

import java.io.*;

public class Query {
    private static final Gson GSON = new Gson();

    private static final String ORG_CACHE = "Organizations";

    public static void main(String[] args) throws IOException {
        Ignition.setClientMode(true);

        try (Ignite ignite = Ignition.start()) {
            IgniteCache<Long, Organization> orgCache = ignite.cache(ORG_CACHE);
            IgniteCache<AffinityKey<IgniteUuid>, Person> cache = ignite.cache(null);

            System.out.println(orgCache.size());
            System.out.println(cache.size());

            System.out.println("Query 1:");

            String sql =
                "SELECT p.name, m.name, o.name " +
                "FROM Person p, Person m, \"" + ORG_CACHE + "\".Organization o " +
                "WHERE p.managerId = m.id " +
                "AND p.orgId = o.id " +
                "AND o.id = ? " +
                "ORDER BY p.name";

            query(cache, sql);

            System.out.println("Query 2:");

            sql =
                "SELECT o.name as Organization, avg(p.salary) as Salary " +
                "FROM Person p, \"Organizations\".Organization o " +
                "WHERE p.orgId = o.id " +
                "AND p.managerId is null " +
                "GROUP BY o.name " +
                "ORDER BY Salary " +
                "LIMIT 100";

            query(cache, sql);
        }
    }

    private static void query(IgniteCache<AffinityKey<IgniteUuid>, Person> cache, String sql) {
        for (int i = 0; i < 3; i++) {
            long s = System.currentTimeMillis();

            cache.query(new SqlFieldsQuery(sql, false).setArgs(10)).getAll();

            long d = System.currentTimeMillis() - s;

            System.out.println("Time: " + d);
        }
    }
}
