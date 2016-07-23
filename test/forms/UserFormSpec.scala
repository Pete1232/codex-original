package forms

import config.UnitSpec
import services.MockLoginService

class UserFormSpec extends UnitSpec{

  val userForm = new UserForm(new MockLoginService).userForm

  def testForAnError(userId: String, password: String, errorId: String) = {
    userForm
      .bind(Map("userId" -> userId, "password" -> password))
      .errors
      .head
      .message mustBe errorId
  }

  "userForm" must "validate a good userId/password combination" in {
    userForm.bind(Map("userId" -> "user", "password" -> "password"))
      .hasErrors mustBe false
  }
  it must "not validate an empty userId field" in {
    val error = userForm.bind(Map("userId" -> "", "password" -> ""))
      .errors
      .head
    error.key mustBe "userId"
    error.message mustBe "error.required"
  }
  it must "not validate an empty password field" in {
    val error = userForm.bind(Map("userId" -> "user", "password" -> ""))
      .errors
      .head
    error.key mustBe "password"
    error.message mustBe "error.required"
  }
  it must "not validate bad credentials" in {
    testForAnError("user", "p2ssword", "login.validation.credentials")
  }
  it must "not validate a userId that does not exist in the database" in {
    testForAnError("notAUser", "password", "login.validation.credentials")
  }
  it must "not validate userId or password if the userId is empty" in {
    val errors = userForm
      .bind(Map("userId" -> "", "password" -> "password"))
      .errors
    errors.foreach {error =>
      error.message must not be "login.validation.userId"
      error.message must not be "login.validation.credentials"
    }
  }
  it must "not validate userId or password if the password is empty" in {
    val errors = userForm
      .bind(Map("userId" -> "user", "password" -> ""))
      .errors
    errors.foreach {error =>
      error.message must not be "login.validation.userId"
      error.message must not be "login.validation.credentials"
    }
  }
}
