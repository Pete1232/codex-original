package models

import play.api.data.Mapping
import play.api.data.Forms._
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}

case class User(userId: String, password: String) {
  def validate = {
    password.length > 5
  }
  val passwordCheckConstraint: Constraint[User] = Constraint("constraints.passwordcheck")({
    user =>
      val errors = user.validate match {
        case false => Seq(ValidationError("Password is all numbers"))
        case _ => Nil
      }
      if (errors.isEmpty) {
        Valid
      } else {
        Invalid(errors)
      }
  })
}