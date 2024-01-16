// (C) Uri Wilensky. https://github.com/NetLogo/NetLogo

package org.nlogo.extensions.bspace

import org.nlogo.api.{ Argument, Context, Reporter }
import org.nlogo.core.LogoList
import org.nlogo.core.Syntax._

object GetOutputMetric extends Reporter {
  override def getSyntax = {
    reporterSyntax(right = List(StringType, StringType, NumberType), ret = ListType | StringType)
  }

  override def report(args: Array[Argument], context: Context): LogoList = {
    val file = scala.io.Source.fromFile(args(0).getString)

    val lines = file.getLines.dropWhile(x => !x.split(",")(0).contains("final value") &&
                                             !x.split(",")(0).contains("all run data"))
    val headers = lines.next.split(",")
    val runs = headers.count(BehaviorSpaceExtension.removeQuotes(_) == "[step]")
    val index = headers.indexWhere(BehaviorSpaceExtension.removeQuotes(_) == args(1).getString)

    var result = List[String]()

    if (index < 0)
      BehaviorSpaceExtension.nameError(context, "noMetric", args(1).getString)
    else if (args(2).getIntValue <= 0 || args(2).getIntValue > runs)
      BehaviorSpaceExtension.nameError(context, "noRun", args(2).getIntValue.toString)
    else {
      result = lines.map(x => BehaviorSpaceExtension.removeQuotes(x.split(",")((headers.length - 1) / runs *
                              (args(2).getIntValue - 1) + index))).toList
    }

    file.close

    return LogoList.fromList(result)
  }
}
