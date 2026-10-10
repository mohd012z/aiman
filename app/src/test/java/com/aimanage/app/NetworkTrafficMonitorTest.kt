package com.aimanage.app

import org.junit.Assert.*
import org.junit.Test

class NetworkTrafficMonitorTest {
    private fun sample(time: Long, rx: Long?, tx: Long?) =
        NetworkTrafficSnapshot(time,rx,tx,true,false,false)

    @Test fun computesOwnAppBytesPerSecond() {
        val rate=NetworkTrafficMonitor.rate(sample(1000,1000,2000),sample(3000,5000,3000))
        assertEquals(2000L,rate?.downBytesPerSecond)
        assertEquals(500L,rate?.upBytesPerSecond)
    }

    @Test fun handlesUnavailableOrResetCounters() {
        assertNull(NetworkTrafficMonitor.rate(sample(1000,null,100),sample(2000,200,300)))
        assertNull(NetworkTrafficMonitor.rate(sample(1000,300,100),sample(2000,200,300)))
        assertNull(NetworkTrafficMonitor.rate(sample(1000,100,100),sample(1000,200,200)))
    }
}
