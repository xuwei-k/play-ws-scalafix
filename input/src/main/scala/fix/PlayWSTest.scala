/*
rule = PlayWS
 */
package fix

import play.api.libs.ws.{ WSClient, WSRequest, WSResponse }
import play.api.libs.ws.ahc.{ AhcWSClient, AhcWSRequest, AhcWSResponse }

trait PlayWSTest {
  def a1: WSClient
  def a2: WSRequest
  def a3: WSResponse

  def a4: AhcWSClient
  def a5: AhcWSRequest
  def a6: AhcWSResponse

  a3.json
  a6.json

  a3.xml
  a6.xml
}
