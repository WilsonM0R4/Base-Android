package com.allegra.handyuvisa.async;

import com.squareup.otto.Bus;

/**
 * Created by victor on 22/02/15.
 * com.allem.allemevent.async
 */
public class MyBus {

    private static Bus BUS = new Bus();

    public static Bus getInstance() {
        return BUS;
    }
}