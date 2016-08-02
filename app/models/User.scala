package models

case class User(userId: String, password: String)

case class DatabaseUser(userId: String, password: Array[Byte], salt: Array[Byte])
