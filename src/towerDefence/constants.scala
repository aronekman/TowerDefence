package towerDefence

import java.io._
import javax.imageio.ImageIO
import javax.swing.ImageIcon

object constants {
  
  val viewWidth = 1000
  val viewHeight = 750
  val squaresInRow = 20
  val squaresInColumn = 15
  val squareWidth = viewWidth/squaresInRow
  val squareHeight = viewHeight/squaresInColumn
  val enemySpeed = 1
  val gameSpeed = 1
  val enemyDelay = 20
  val ticksPerSecond = 200
  
  def distanceBetweenPoints(a: (Int, Int), b: (Int, Int)) = {
    Math.sqrt((a._2 - b._2) * (a._2 - b._2) + (a._1 - b._1) * (a._1 - b._1))
  }
}