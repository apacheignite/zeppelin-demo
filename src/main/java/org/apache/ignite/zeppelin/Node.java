package org.apache.ignite.zeppelin;

import org.apache.ignite.*;

public class Node {
    public static void main(String[] args) {
        Ignition.start("ignite.xml");
    }
}
