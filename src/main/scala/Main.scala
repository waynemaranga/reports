import cats.effect._
import com.typesafe.config.ConfigFactory
import doobie._
import io.github.cdimascio.dotenv.Dotenv
import ai.OpenAIClient
import db._
import java.nio.file.{Files, Paths}
import report.HtmlReport

object Main extends IOApp.Simple {
  val dotenv = Dotenv.load()
  val config = ConfigFactory.load()

  val dbCfg = config.getConfig("db")
  val aiCfg = config.getConfig("openai")

  val transactor = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    dbCfg.getString("url"),
    dbCfg.getString("user"),
    dbCfg.getString("password")
  )

  def buildPrompt(data: List[SoilTest]): String =
    s"""You are a civil engineer. Generate a report from the following soil test results:
       |
       |${data.map(d => s"Sample ${d.sampleId}: Plastic=${d.plasticContent}, CBR(S)=${d.cbrSoaked}, CBR(U)=${d.cbrUnsoaked}, UCS=${d.ucs}, Atterberg=${d.atterbergLimit}").mkString("\n")}
       |
       |Provide interpretation and recommendations.
     """.stripMargin

  def run: IO[Unit] = for {
    data   <- SoilRepository.fetchData(transactor)
    prompt = buildPrompt(data)
    result = OpenAIClient.generateReport(prompt, aiCfg.getString("apiKey"), aiCfg.getString("model"))
    _      <- IO.println(result.fold(err => s"Error: $err", report => s"\n--- Report ---\n$report"))
  } yield () // inside `run`
result match {
  case Left(err) =>
    IO.println(s"Error: $err")

  case Right(report) =>
    val html = HtmlReport.toHtml("Soil Test Report", report)
    val path = Paths.get("report.html")
    IO(Files.writeString(path, html)) *> IO.println(s"\n✅ Report written to ${path.toAbsolutePath}")
}

}
