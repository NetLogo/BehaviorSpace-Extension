// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.extensions.bspace

object BehaviorSpaceExtensionTests {
  val testFiles = Seq("tests.txt").map(x => new java.io.File(x).getCanonicalFile)
}

class BehaviorSpaceExtensionTests extends org.nlogo.headless.TestLanguage(BehaviorSpaceExtensionTests.testFiles) {
  System.setProperty("org.nlogo.preferHeadless", "true")
}
