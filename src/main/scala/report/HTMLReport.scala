package report

object HtmlReport {
  def toHtml(title: String, content: String): String =
    s"""
    |<!DOCTYPE html>
    |<html lang="en">
    |<head>
    |  <meta charset="UTF-8">
    |  <meta name="viewport" content="width=device-width, initial-scale=1.0">
    |  <title>$title</title>
    |  <style>
    |    body { font-family: Arial, sans-serif; margin: 2rem; line-height: 1.6; }
    |    h1 { color: #2c3e50; }
    |    pre { background-color: #f4f4f4; padding: 1rem; border-radius: 5px; white-space: pre-wrap; }
    |  </style>
    |</head>
    |<body>
    |  <h1>$title</h1>
    |  <pre>${content.trim}</pre>
    |</body>
    |</html>
    """.stripMargin
}
