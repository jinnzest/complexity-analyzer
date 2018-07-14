package analyzer

object ComplexityAnalyzer {

  def extractNumbers(numbers: String): Seq[Double] = {
    numbers.trim.split(raw"\s+").map(_.toDouble).toSeq
  }

  def analyze(measurements: Seq[Seq[Double]]): Complexity.Value = {
    import Complexity._

    val minSize = measurements.map(_.size).min

    val base = 1.0 / measurements.size

    val meanMeasurements = measurements.tail.foldLeft(measurements.head) { (acc, v) =>
      acc.zip(v).map { case (a, b) => a * b }
    }.map(v => scala.math.pow(v, base))

    val tailMeasurementsMax = meanMeasurements.drop(meanMeasurements.size / 4 * 3).max

    val refM = (1 to minSize).map(_.toDouble)

    def calcDeviation(refAndReal: (Double, Double)) = {
      val (ref, real) = refAndReal
      if (real > tailMeasurementsMax) 0.0
      else (ref - real).abs
    }

    def findMaxDeviation(ideal: ComplexityRef) = {
      val scale = meanMeasurements.drop(meanMeasurements.size / 4 * 3).max / ideal.ref.drop(ideal.ref.size / 4 * 3).max
      ideal.ref
        .map(_ * scale)
        .zip(meanMeasurements)
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
    var measurementsString = scala.io.StdIn.readLine()
    var measurements: List[Seq[Double]] = Nil
    while (measurementsString!=null && measurementsString.nonEmpty) {
      measurements = extractNumbers(measurementsString) :: measurements
      measurementsString = scala.io.StdIn.readLine()
    }
    if (measurements.nonEmpty) println(analyze(measurements))
    else println("empty input")
  }
}