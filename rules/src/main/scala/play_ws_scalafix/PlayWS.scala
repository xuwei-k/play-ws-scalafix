package play_ws_scalafix

import scalafix.v1._
import scala.meta._

class PlayWS extends SemanticRule("PlayWS") {

  override def fix(implicit doc: SemanticDocument): Patch = {
    val replaceClasses = Patch.replaceSymbols(
      "play.api.libs.ws.WSClient" -> "play.api.libs.ws.StandaloneWSClient",
      "play.api.libs.ws.WSRequest" -> "play.api.libs.ws.StandaloneWSRequest",
      "play.api.libs.ws.WSResponse" -> "play.api.libs.ws.StandaloneWSResponse",
      "play.api.libs.ws.ahc.AhcWSClient" -> "play.api.libs.ws.ahc.StandaloneAhcWSClient",
      "play.api.libs.ws.ahc.AhcWSRequest" -> "play.api.libs.ws.ahc.StandaloneAhcWSRequest",
      "play.api.libs.ws.ahc.AhcWSResponse" -> "play.api.libs.ws.ahc.StandaloneAhcWSResponse"
    )

    val replaceMethods = {
      for {
        (oldMethod, newMethod, importPatch) <- Seq(
          (
            "json",
            "body[play.api.libs.json.JsValue]",
            Patch.addGlobalImport(importer"play.api.libs.ws.JsonBodyReadables._")
          ),
          (
            "xml",
            "body[scala.xml.Elem]",
            Patch.addGlobalImport(importer"play.api.libs.ws.XMLBodyReadables._")
          )
        )
        clazz <- Seq(
          "play/api/libs/ws/WSResponse#",
          "play/api/libs/ws/ahc/AhcWSResponse#"
        )
      } yield {
        doc.tree.collect {
          case t: Term.Select if t.symbol.displayName == oldMethod && t.symbol.owner.value == clazz =>
            Patch.replaceTree(t, t.qual.toString + "." + newMethod) + importPatch
        }
      }
    }.flatten.foldLeft(Patch.empty)(_ + _)

    replaceClasses + replaceMethods
  }

}
