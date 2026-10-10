package com.aimanage.app

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.VpnService
import android.os.Build

/** Capability check only: does not start a VPN or claim packet filtering. */
data class FirewallReadiness(
 val needsUserConsent:Boolean,
 val vpnConflictPossible:Boolean,
 val supportedAndroid:Boolean,
 val packetFilterImplemented:Boolean,
 val notes:List<String>
)
object FirewallReadinessPolicy {
 fun inspect(context:Context):FirewallReadiness {
  val needsConsent=runCatching { VpnService.prepare(context)!=null }.getOrDefault(true)
  return FirewallReadiness(
   needsUserConsent=needsConsent,
   vpnConflictPossible=true,
   supportedAndroid=Build.VERSION.SDK_INT>=29,
   packetFilterImplemented=false,
   notes=listOf(
    "AImanage has no active packet-filtering VPN service.",
    "Android VPN authorization is required before a future firewall can run.",
    "Starting a local VPN may replace an existing VPN connection.",
    "No traffic is intercepted, forwarded, or blocked by this readiness check."
   )
  )
 }
}
