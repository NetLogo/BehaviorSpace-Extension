import org.nlogo.build.NetLogoExtension

enablePlugins(NetLogoExtension)

name       := "bspace"
version    := "1.0.0"
isSnapshot := true

scalaVersion          := "3.7.0"
Compile / scalaSource := baseDirectory.value / "src" / "main"
scalacOptions        ++= Seq("-deprecation", "-unchecked", "-Xfatal-warnings", "-encoding", "us-ascii", "-release", "11")

netLogoExtName      := "bspace"
netLogoClassManager := "org.nlogo.extensions.bspace.BehaviorSpaceExtension"
netLogoVersion      := "7.0.0-beta2"
