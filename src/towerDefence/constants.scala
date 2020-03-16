package towerDefence

import java.io._
import javax.imageio.ImageIO
import javax.swing.ImageIcon

object constants {
  
  var viewWidth = 1000
  var viewHeight = 750
  var squaresInRow = 20
  var squaresInColumn = 15
  var squareWidth = viewWidth/squaresInRow
  var squareHeight = viewHeight/squaresInColumn
  var enemySpeed = 1
  var gameSpeed = 1
  var enemyDelay = 20
  var ticksPerSecond = 200
  var startHP = 20
  
  
  def distanceBetweenPoints(a: (Int, Int), b: (Int, Int)) = {
    Math.sqrt((a._2 - b._2) * (a._2 - b._2) + (a._1 - b._1) * (a._1 - b._1))
  }
}