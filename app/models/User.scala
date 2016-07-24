package models

import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}

case class User(userId: String, password: String) {
  def validate = {
    password.length > 5
  }
}

object User {
  val passwordCheckConstraint: Constraint[User] = Constraint("constraints.passwordcheck")({
    user =>
      val errors = user.validate match {
        case false => Seq(ValidationError("login.validation.length"))
        case _ => Nil
      }
      if (errors.isEmpty) {
        Valid
      } else {
        Invalid(errors)
      }
  })
}