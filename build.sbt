import org.nlogo.build.NetLogoExtension

enablePlugins(NetLogoExtension)

name       := "BehaviorSpaceExtension"
version    := "1.0.0"
isSnapshot := true

scalaVersion          := "2.12.12"
Compile / scalaSource := baseDirectory.value / "src" / "main"
scalacOptions        ++= Seq("-deprecation", "-unchecked", "-Xfatal-warnings", "-encoding", "us-ascii", "-release", "11")

netLogoExtName      := "bspace"
netLogoClassManager := "org.nlogo.extensions.bspace.BehaviorSpaceExtension"
netLogoVersion      := "6.4.0"
