package analyzer

object ComplexityAnalyzer {

  def extractNumbers(numbers: String): List[Double] = {
    numbers.trim.split(raw"\s+").map(_.toDouble).toList
  }

  def analyze(measurements: List[Double]): Complexity.Value = {
    import Complexity._

    val lastMeasurement = measurements.last

    val refM = (1 to measurements.size).map(_.toDouble)

    def calcDeviation(refAndReal: (Double, Double)) = {
      val (ref, real) = refAndReal
      if (real > lastMeasurement) 0.0
      else (ref - real).abs
    }

    def findMaxDeviation(ideal: ComplexityRef) = {
      val scale = measurements.last / ideal.ref.last
      ideal.ref
        .map(_ * scale)
        .zip(measurements)
        .map(calcDeviation)
        .max
    }

    val complexitiesRef = ComplexityRef(O1, refM.map(_ => 10.0)) ::
      ComplexityRef(OLogN, refM.map(scala.math.log)) ::
      ComplexityRef(On, refM.map(identity)) ::
      ComplexityRef(On2, refM.map(v => v * v)) ::
      ComplexityRef(O2n, refM.map(v => scala.math.pow(2, v))) ::
      ComplexityRef(OnLogN, refM.map(v => v * scala.math.log(v))) :: Nil

    complexitiesRef.minBy(findMaxDeviation).complexity
  }

  def main(args: Array[String]): Unit = {
    val measurementsString = scala.io.StdIn.readLine()
    if (measurementsString.nonEmpty) println(analyze(extractNumbers(measurementsString)))
    else println("empty input")
  }
}