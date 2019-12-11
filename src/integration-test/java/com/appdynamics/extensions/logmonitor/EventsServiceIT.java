package com.appdynamics.extensions.logmonitor;


import com.appdynamics.extensions.conf.processor.ConfigProcessor;
import com.appdynamics.extensions.eventsservice.EventsServiceDataManager;
import com.appdynamics.extensions.yml.YmlReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Map;

public class EventsServiceIT {
    private EventsServiceDataManager eventsServiceDataManager;

    @Before
    public void setup() {
        File configFile = new File("src/integration-test/resources/conf/config.yml");
        Map<String, ?> config = YmlReader.readFromFileAsMap(configFile);
        config = ConfigProcessor.process(config);
        Map<String, Object> eventsServiceParameters = (Map) config.get("eventsServiceParameters");
        eventsServiceParameters.put("globalAccountName", System.getenv("GLOBAL_ACCOUNT_NAME"));
        eventsServiceDataManager = new EventsServiceDataManager(eventsServiceParameters);
    }

    @Test
    public void testWhetherSchemaIsCreated() {
        Assert.assertTrue(eventsServiceDataManager.retrieveSchema("logSchema").contains("logDisplayName"));
    }

    @Test
    public void testWhetherEventsArePublished() {
        Assert.assertTrue(eventsServiceDataManager.querySchema("select * from logschema").contains("results"));
    }
}
