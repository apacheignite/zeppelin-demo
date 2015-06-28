package org.apache.ignite.zeppelin;

import org.apache.ignite.*;
import org.apache.ignite.configuration.*;

public class Node {
    public static void main(String[] args) {
        IgniteConfiguration cfg = new IgniteConfiguration();

        ConnectorConfiguration connCfg = new ConnectorConfiguration();

        connCfg.setIdleTimeout(200000);

        cfg.setConnectorConfiguration(connCfg);

        Ignition.start(cfg);
    }
}
