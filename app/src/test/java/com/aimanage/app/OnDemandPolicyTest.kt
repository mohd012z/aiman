package com.aimanage.app

import org.junit.Assert.*
import org.junit.Test

class OnDemandPolicyTest {
    @Test fun thermalPressureSuspendsDiagnostics() {
        val decision = OnDemandPolicy.decide(DemandContext(DemandMode.PERFORMANCE, true, true, false, AppImportance.ORDINARY))
        assertFalse(decision.runOptionalDiagnostics)
    }
    @Test fun criticalBackgroundAppNotFlaggedForReview() {
        val decision = OnDemandPolicy.decide(DemandContext(DemandMode.BALANCED, false, false, false, AppImportance.CRITICAL))
        assertFalse(decision.suggestedReview)
    }
    @Test fun ecoDefersDiagnostics() {
        val decision = OnDemandPolicy.decide(DemandContext(DemandMode.ECO, true, false, false, AppImportance.ORDINARY))
        assertFalse(decision.runOptionalDiagnostics)
    }
}
