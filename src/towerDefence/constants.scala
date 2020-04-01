package towerDefence

import java.io._
import javax.imageio.ImageIO
import javax.swing.ImageIcon

object constants {
  
  var viewWidth = 1000
  var viewHeight = 750
  var guiWidth = 120
  var guiHeight = 80
  var squaresInRow = 20
  var squaresInColumn = 15
  var squareWidth = viewWidth/squaresInRow
  var squareHeight = viewHeight/squaresInColumn
  var enemySpeed = 1
  var enemyDelay = 50
  var ticksPerSecond = 200
  var startHP = 20
  var startMoney = 150
  
  
  var basicTowerCost = 50
  
  
  def distanceBetweenPoints(a: (Int, Int), b: (Int, Int)) = {
    Math.sqrt((a._2 - b._2) * (a._2 - b._2) + (a._1 - b._1) * (a._1 - b._1))
  }
  def xDistance(from: (Int, Int), to: (Int, Int)): Int = {
    to._1 - from._1
  }
  def yDistance(from: (Int, Int), to: (Int, Int)): Int = {
    to._2 - from._2
  }
}