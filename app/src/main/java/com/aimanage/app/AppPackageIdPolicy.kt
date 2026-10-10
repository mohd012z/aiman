package com.aimanage.app

/** Strict, local syntax check for Android package IDs supplied by a user. */
object AppPackageIdPolicy {
 private val pattern=Regex("[A-Za-z_][A-Za-z0-9_]*(\\.[A-Za-z_][A-Za-z0-9_]*)+")
 fun valid(value:String):Boolean=pattern.matches(value.trim())
}
