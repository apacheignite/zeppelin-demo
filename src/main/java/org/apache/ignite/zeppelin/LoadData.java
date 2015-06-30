package org.apache.ignite.zeppelin;

import com.google.gson.*;
import org.apache.commons.lang.time.*;
import org.apache.ignite.*;
import org.apache.ignite.cache.affinity.*;
import org.apache.ignite.configuration.*;

import java.io.*;

public class LoadData {
    private static final Gson GSON = new Gson();

    private static final String ORG_CACHE = "Organizations";

    public static void main(String[] args) throws IOException {
        Ignition.setClientMode(true);

        try (Ignite ignite = Ignition.start()) {
            CacheConfiguration<Long, Organization> orgCacheCfg = new CacheConfiguration<>(ORG_CACHE);

            orgCacheCfg.setIndexedTypes(Long.class, Organization.class);

            IgniteCache<Long, Organization> orgCache = ignite.getOrCreateCache(orgCacheCfg);

            CacheConfiguration<AffinityKey<Long>, Person> personCacheCfg = new CacheConfiguration<>();

            personCacheCfg.setIndexedTypes(AffinityKey.class, Person.class);

            IgniteCache<AffinityKey<Long>, Person> personCache = ignite.getOrCreateCache(personCacheCfg);

            orgCache.clear();
            personCache.clear();

            System.out.println("Cleared caches.");

            StopWatch sw = new StopWatch();

            sw.start();

            try (IgniteDataStreamer<Long, Organization> orgStreamer = ignite.dataStreamer(ORG_CACHE)) {
                try (BufferedReader reader = new BufferedReader(new FileReader("organizations.json"))) {
                    String line;

                    int i = 0;

                    while ((line = reader.readLine()) != null) {
                        Organization org = GSON.fromJson(line, Organization.class);

                        orgStreamer.addData(org.getId(), org);

                        if (i > 0 && i % 10000 == 0)
                            System.out.println("Loaded " + i + " organizations.");

                        i++;
                    }
                }
            }

            sw.stop();

            System.out.println("Organizations [size=" + orgCache.size() + ", time=" + sw + ']');

            sw.reset();
            sw.start();

            try (IgniteDataStreamer<AffinityKey<Long>, Person> personStreamer = ignite.dataStreamer(null)) {
                try (BufferedReader reader = new BufferedReader(new FileReader("persons.json"))) {
                    String line;

                    int i = 0;

                    while ((line = reader.readLine()) != null) {
                        Person person = GSON.fromJson(line, Person.class);

                        personStreamer.addData(new AffinityKey<>(person.getId(), person.getOrgId()), person);

                        if (i > 0 && i % 10000 == 0)
                            System.out.println("Loaded " + i + " persons.");

                        i++;
                    }
                }
            }

            sw.stop();

            System.out.println("Persons [size=" + personCache.size() + ", time=" + sw + ']');
        }
    }
}
