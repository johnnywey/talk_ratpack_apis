package com.johnnywey.ratpackapi.client;

import com.johnnywey.flipside.failable.Failable;
import de.caluga.morphium.MorphiumConfig;

public interface MongoClientConfig {
    public String getHost();

    public Integer getPort();

    public String getDatabase();

    public Failable<MorphiumConfig> toConfig();
}
