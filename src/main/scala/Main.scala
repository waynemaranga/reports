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

  def buildPrompt(data: List[SoilTest]): String =
    s"""You are a civil engineer. Generate a report from the following soil test results:
       |
       |${data.map(d => s"Sample ${d.sampleId}: Plastic=${d.plasticContent}, CBR(S)=${d.cbrSoaked}, CBR(U)=${d.cbrUnsoaked}, UCS=${d.ucs}, Atterberg=${d.atterbergLimit}").mkString("\n")}
       |
       |Provide interpretation and recommendations.
     """.stripMargin

  def run: IO[Unit] = {
    val aiCfg = config.getConfig("openai")
    
    for {
      // Fetch soil test data
      data <- SoilRepository.fetchData(transactor)
      _ <- IO.println(s"Retrieved ${data.size} soil test records")
      
      // Generate report prompt and call OpenAI
      prompt = buildPrompt(data)
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