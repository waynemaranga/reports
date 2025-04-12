package ai

import io.circe.generic.auto._
import sttp.client3._
import sttp.client3.circe._
import cats.effect._

object AzureOpenAIClient {
  case class Message(role: String, content: String)
  case class RequestPayload(messages: List[Message])
  case class Choice(message: Message)
  case class OpenAIResponse(choices: List[Choice])

  def generateReport(prompt: String, apiKey: String, endpoint: String, deploymentName: String, apiVersion: String): IO[Either[String, String]] = IO {
    val backend = HttpURLConnectionBackend()
    val url = uri"$endpoint/openai/deployments/$deploymentName/chat/completions?api-version=$apiVersion"

    val req = basicRequest
      .body(RequestPayload(List(Message("user", prompt))))
      .post(url)
      .header("api-key", apiKey)
      .header("Content-Type", "application/json")
      .response(asJson[OpenAIResponse])

    req.send(backend).body match {
      case Right(r) if r.choices.nonEmpty => Right(r.choices.head.message.content)
      case Right(_) => Left("No response choices returned from Azure OpenAI")
      case Left(e)  => Left(e.getMessage)
    }
  }
}
