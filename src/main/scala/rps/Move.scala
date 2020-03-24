package rps

import io.buildo.enumero.annotations.indexedEnum
import io.buildo.enumero.CaseEnumSerialization
import io.buildo.enumero.CaseEnumIndex

@indexedEnum trait Move {
  type Index = String

  object Rock { "0" }
  object Paper { "1" }
  object Scissors { "2" }
}
