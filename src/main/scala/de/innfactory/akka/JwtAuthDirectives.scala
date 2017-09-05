package de.innfactory.akka

import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.directives.{BasicDirectives, HeaderDirectives, RouteDirectives}
import com.nimbusds.jwt.JWTClaimsSet
import de.innfactory.akka.jwt._

trait JwtAuthDirectives {

  import BasicDirectives._
  import HeaderDirectives._
  import RouteDirectives._

  def authenticate: Directive1[(JwtToken, JWTClaimsSet)] = {
    optionalHeaderValueByName("Authorization").flatMap { token =>
      jwtValidator.validate(JwtToken(token.getOrElse(""))) match {
        case Left(error) => reject
        case Right(result) => provide(result)
      }
    }
  }

  protected val jwtValidator: JwtValidator
}