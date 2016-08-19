package forms

import connectors.UserDatabaseConnector
import models.User
import play.api.data.Form
import play.api.data.Forms._

class UserForm(userDatabaseConnector: UserDatabaseConnector){
  val userForm = Form(
    mapping(
      "userId" -> nonEmptyText,
      "password" -> nonEmptyText
    )(User.apply)
    // $COVERAGE-OFF$
    (User.unapply)
    // $COVERAGE-ON$
  )
}
