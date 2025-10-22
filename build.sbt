import org.nlogo.build.NetLogoExtension

enablePlugins(NetLogoExtension)

name       := "bspace"
version    := "0.1.1"
isSnapshot := true

scalaVersion          := "3.7.0"
Compile / scalaSource := baseDirectory.value / "src" / "main"
scalacOptions        ++= Seq("-deprecation", "-unchecked", "-Xfatal-warnings", "-encoding", "us-ascii", "-release", "11", "-Wunused:linted")

netLogoExtName      := "bspace"
netLogoClassManager := "org.nlogo.extensions.bspace.BehaviorSpaceExtension"
netLogoVersion      := "7.0.1"
