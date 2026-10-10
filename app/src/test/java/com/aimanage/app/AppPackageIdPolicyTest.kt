package com.aimanage.app

import org.junit.Assert.*
import org.junit.Test

class AppPackageIdPolicyTest {
 @Test fun acceptsValidPackageNames() {
  assertTrue(AppPackageIdPolicy.valid("com.example.app"))
  assertTrue(AppPackageIdPolicy.valid("org.example_app.Service2"))
 }
 @Test fun rejectsInvalidPackageNames() {
  assertFalse(AppPackageIdPolicy.valid(""))
  assertFalse(AppPackageIdPolicy.valid("com"))
  assertFalse(AppPackageIdPolicy.valid("com..app"))
  assertFalse(AppPackageIdPolicy.valid("com/example/app"))
  assertFalse(AppPackageIdPolicy.valid("com.example app"))
  assertFalse(AppPackageIdPolicy.valid("1com.example"))
 }
}
