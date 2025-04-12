import java.sql.{Connection, DriverManager, ResultSet}

object JDBCExample {
  def main(args: Array[String]): Unit = {
    val url = "jdbc:mysql://localhost:3306/my_database"
    val username = "my_user"
    val password = "my_password"

    var connection: Connection = null

    try {
      // Load the JDBC driver
      Class.forName("com.mysql.cj.jdbc.Driver")

      // Establish the connection
      connection = DriverManager.getConnection(url, username, password)
      println("Connected to the database!")

      // Create a statement and execute a query
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery("SELECT * FROM my_table")

      // Iterate through the result set
      while (resultSet.next()) {
        val id = resultSet.getInt("id")
        val name = resultSet.getString("name")
        println(s"ID: $id, Name: $name")
      }
    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      if (connection != null) connection.close()
    }
  }
}
