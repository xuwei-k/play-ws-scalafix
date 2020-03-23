package fix

import play.api.libs.ws.{ StandaloneWSClient, StandaloneWSRequest, StandaloneWSResponse }
import play.api.libs.ws.JsonBodyReadables._
import play.api.libs.ws.XMLBodyReadables._
import play.api.libs.ws.ahc.{ StandaloneAhcWSClient, StandaloneAhcWSRequest, StandaloneAhcWSResponse }

trait PlayWSTest {
  def a1: StandaloneWSClient
  def a2: StandaloneWSRequest
  def a3: StandaloneWSResponse

  def a4: StandaloneAhcWSClient
  def a5: StandaloneAhcWSRequest
  def a6: StandaloneAhcWSResponse

  a3.body[play.api.libs.json.JsValue]
  a6.body[play.api.libs.json.JsValue]

  a3.body[scala.xml.Elem]
  a6.body[scala.xml.Elem]
}
