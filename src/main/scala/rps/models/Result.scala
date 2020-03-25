package rps.models

import io.buildo.enumero.annotations.enum

@enum trait Result {
  object Win
  object Lose
  object Draw
}