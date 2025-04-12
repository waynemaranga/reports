// Main.scala
import cats.effect._
import com.typesafe.config.ConfigFactory
import doobie._
import io.github.cdimascio.dotenv.Dotenv
import ai.OpenAIClient
import db._
import java.nio.file.{Files, Paths}
import report.HtmlReport

object Main extends IOApp.Simple {
  // Load configuration
  val config = {
    // Load environment variables from .env file first
    Dotenv.load()
    ConfigFactory.load()
  }

  // Create database transactor from config
  val transactor = {
    val dbCfg = config.getConfig("db")
    Transactor.fromDriverManager[IO](
      "org.postgresql.Driver",
      dbCfg.getString("url"),
      dbCfg.getString("user"),
      dbCfg.getString("password")
    )
  }
  
  // MySQL config for direct JDBC
  val mysqlConfig = {
    val mysqlCfg = config.getConfig("mysql")
    DatabaseConfig(
      mysqlCfg.getString("url"),
      mysqlCfg.getString("user"),
      mysqlCfg.getString("password")
    )
  }

  def buildPrompt(data: List[SoilTest], additionalData: List[MyData]): String =
    s"""You are a civil engineer. Generate a report from the following soil test results:
       |
       |${data.map(d => s"Sample ${d.sampleId}: Plastic=${d.plasticContent}, CBR(S)=${d.cbrSoaked}, CBR(U)=${d.cbrUnsoaked}, UCS=${d.ucs}, Atterberg=${d.atterbergLimit}").mkString("\n")}
       |
       |Additional reference data:
       |${additionalData.map(d => s"Reference ID: ${d.id}, Name: ${d.name}").mkString("\n")}
       |
       |Provide interpretation and recommendations.
     """.stripMargin

  def run: IO[Unit] = {
    val aiCfg = config.getConfig("openai")
    
    for {
      // Fetch soil test data using Doobie
      soilData <- SoilRepository.fetchData(transactor)
      _ <- IO.println(s"Retrieved ${soilData.size} soil test records")
      
      // Fetch additional data using direct JDBC
      additionalData <- DirectJDBCDriver.fetchMySQLData(mysqlConfig)
      _ <- IO.println(s"Retrieved ${additionalData.size} additional records via JDBC")
      
      // Generate report prompt and call OpenAI
      prompt = buildPrompt(soilData, additionalData)
      reportResult <- OpenAIClient.generateReport(
        prompt, 
        aiCfg.getString("apiKey"), 
        aiCfg.getString("model")
      )
      
      // Process the result
      _ <- reportResult match {
        case Left(err) =>
          IO.println(s"Error generating report: $err")
          
        case Right(report) => 
          for {
            _ <- IO.println(s"\n--- Report Preview ---\n${report.take(200)}...")
            html = HtmlReport.toHtml("Soil Test Report", report)
            path = Paths.get("report.html")
            _ <- IO(Files.writeString(path, html))
            _ <- IO.println(s"\n✅ Report written to ${path.toAbsolutePath}")
          } yield ()
      }
    } yield ()
  }
}