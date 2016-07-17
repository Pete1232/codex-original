package models

case class User(userId: String, password: String) {
  def validate(password: String) = {
    password.length > 5
  }
}