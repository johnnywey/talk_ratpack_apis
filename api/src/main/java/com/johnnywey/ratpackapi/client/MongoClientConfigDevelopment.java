package com.johnnywey.ratpackapi.client;

import com.johnnywey.flipside.failable.Fail;
import com.johnnywey.flipside.failable.Failable;
import com.johnnywey.flipside.failable.Failed;
import com.johnnywey.flipside.failable.Success;
import de.caluga.morphium.MorphiumConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class MongoClientConfigDevelopment implements MongoClientConfig {
    private final Logger log = LoggerFactory.getLogger(MongoClientConfig.class);

    private final String host = "localhost";
    private final Integer port = 27017;
    private final String database = "ratpack-test";

    @Override
    public String getHost() {
        return this.host;
    }

    @Override
    public Integer getPort() {
        return this.port;
    }

    @Override
    public String getDatabase() {
        return this.database;
    }

    @Override
    public Failable<MorphiumConfig> toConfig() {
        MorphiumConfig config = new MorphiumConfig();
        List<String> hostPortString = new ArrayList<>();
        hostPortString.add(getHost() + ":" + getPort());
        try {
            config.setHosts(hostPortString);

        } catch (UnknownHostException e) {
            log.error("Could not create mongo config: [" + e.getMessage() + "]");
            return new Failed<>(Fail.INTERNAL_ERROR, e.getMessage());
        }
        config.setDatabase(getDatabase());
        return new Success<>(config);
    }
}
