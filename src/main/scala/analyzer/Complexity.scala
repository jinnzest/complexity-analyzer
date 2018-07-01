package analyzer

object Complexity extends Enumeration {
  val O1 = Value("O(1)")
  val OLogN = Value("O(Log(N)")
  val On = Value("O(N)")
  val OnLogN = Value("O(N*Log(N))")
  val On2 = Value("O(N^2)")
  val O2n = Value("O(2^N)")
}
