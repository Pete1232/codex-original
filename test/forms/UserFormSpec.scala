package forms

import config.UnitSpec

class UserFormSpec extends UnitSpec{
  "userForm" must "validate a good userId/password combination" in {
    User.userForm.bind(Map("userId" -> "user", "password" -> "password")).hasErrors mustBe false
  }
  it must "not validate an empty userId field" in {
    User.userForm.bind(Map("userId" -> "", "password" -> "pass")).hasErrors mustBe true
  }
  it must "not validate an empty password field" in {
    User.userForm.bind(Map("userId" -> "user", "password" -> "")).hasErrors mustBe true
  }
  it must "not validate a password of lenghth 5 or less" in {
    User.userForm.bind(Map("userId" -> "user", "password" -> "pass")).hasErrors mustBe true
  }
}
