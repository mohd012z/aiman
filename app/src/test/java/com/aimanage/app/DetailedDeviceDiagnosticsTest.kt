package com.aimanage.app

import org.junit.Assert.*
import org.junit.Test

class DetailedDeviceDiagnosticsTest {
    @Test fun gibHandlesUnavailableAndZero() {
        assertEquals("Unavailable",DetailedDeviceDiagnostics.gib(null))
        assertEquals("0.00 GiB",DetailedDeviceDiagnostics.gib(0))
    }
    @Test fun gibUsesBinaryUnits() {
        assertEquals("1.00 GiB",DetailedDeviceDiagnostics.gib(1073741824L))
        assertEquals("2.50 GiB",DetailedDeviceDiagnostics.gib(2684354560L))
    }
}
