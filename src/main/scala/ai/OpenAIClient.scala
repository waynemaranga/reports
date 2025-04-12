package ai

import io.circe.generic.auto._
import sttp.client3._
import sttp.client3.circe._

object OpenAIClient {
  case class Message(role: String, content: String)
  case class RequestPayload(model: String, messages: List[Message])
  case class Choice(message: Message)
  case class OpenAIResponse(choices: List[Choice])

  def generateReport(prompt: String, apiKey: String, model: String): Either[String, String] = {
    val backend = HttpURLConnectionBackend()
    val req = basicRequest
      .body(RequestPayload(model, List(Message("user", prompt))))
      .post(uri"https://api.openai.com/v1/chat/completions")
      .header("Authorization", s"Bearer $apiKey")
      .contentType("application/json")
      .response(asJson[OpenAIResponse])

    req.send(backend).body match {
      case Right(r) => Right(r.choices.head.message.content)
      case Left(e)  => Left(e.getMessage)
    }
  }
}
