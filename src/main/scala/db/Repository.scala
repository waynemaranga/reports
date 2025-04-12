package db

import doobie._
import doobie.implicits._
import cats.effect._

case class SoilTest(sampleId: String, plasticContent: Float, cbrSoaked: Float, cbrUnsoaked: Float, ucs: Float, atterbergLimit: Float)

object SoilRepository {
  def fetchData(transactor: Transactor[IO]): IO[List[SoilTest]] = {
    sql"""SELECT sample_id, plastic_content, cbr_soaked, cbr_unsoaked, ucs, atterberg_limit FROM soil_tests"""
      .query[SoilTest]
      .to[List]
      .transact(transactor)
  }
}
