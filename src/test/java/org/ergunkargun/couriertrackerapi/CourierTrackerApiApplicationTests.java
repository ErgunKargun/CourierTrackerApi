package org.ergunkargun.couriertrackerapi;

import org.ergunkargun.couriertrackerapi.observe.CourierLogEventListener;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class CourierTrackerApiApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        var stores = applicationContext.getBeansOfType(CourierLogEventListener.class);
        Assertions.assertEquals(5, stores.entrySet().size());
    }

}
