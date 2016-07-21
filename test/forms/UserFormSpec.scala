package forms

import config.UnitSpec

class UserFormSpec extends UnitSpec{

  def testForAnError(userId: String, password: String, errorId: String) = {
    UserForm.userForm
      .bind(Map("userId" -> userId, "password" -> password))
      .errors
      .head
      .message mustBe errorId
  }

  "userForm" must "validate a good userId/password combination" in {
    UserForm.userForm.bind(Map("userId" -> "user", "password" -> "password"))
      .hasErrors mustBe false
  }
  it must "not validate an empty userId field" in {
    val error = UserForm.userForm.bind(Map("userId" -> "", "password" -> ""))
      .errors
      .head
    error.key mustBe "userId"
    error.message mustBe "error.required"
  }
  it must "not validate an empty password field" in {
    val error = UserForm.userForm.bind(Map("userId" -> "user", "password" -> ""))
      .errors
      .head
    error.key mustBe "password"
    error.message mustBe "error.required"
  }
  it must "not validate a password of lenghth 5 or less" in {
    testForAnError("user", "pass", "login.validation.length")
  }
  it must "not validate bad credentials" in {
    testForAnError("user", "p2ssword", "login.validation.credentials")
  }
  it must "not validate a userId that does not exist in the database" in {
    testForAnError("notAUser", "password", "login.validation.userId")
  }
}
