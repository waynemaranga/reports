// DirectJDBCDriver.scala
package db

import cats.effect.IO
import java.sql.{Connection, DriverManager, ResultSet}

case class MyData(id: Int, name: String)

object DirectJDBCDriver {
  def getConnection(url: String, username: String, password: String): IO[Connection] = IO {
    Class.forName("com.mysql.cj.jdbc.Driver")
    DriverManager.getConnection(url, username, password)
  }
  
  def fetchMySQLData(config: DatabaseConfig): IO[List[MyData]] = {
    val acquireConnection = getConnection(config.url, config.user, config.password)
    
    val program = for {
      connection <- acquireConnection
      _ <- IO.println("Connected to MySQL database!")
      
      result <- IO {
        val statement = connection.createStatement()
        val resultSet = statement.executeQuery("SELECT * FROM my_table")
        
        val builder = List.newBuilder[MyData]
        while (resultSet.next()) {
          val id = resultSet.getInt("id")
          val name = resultSet.getString("name")
          builder += MyData(id, name)
        }
        builder.result()
      }
      
      _ <- IO {
        if (connection != null) connection.close()
      }
    } yield result
    
    // Add error handling
    program.handleErrorWith { error =>
      IO.println(s"Database error: ${error.getMessage}") *> 
      IO.pure(List.empty[MyData])
    }
  }
}

// Configuration case class
case class DatabaseConfig(url: String, user: String, password: String)