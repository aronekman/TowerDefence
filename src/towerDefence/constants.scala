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
  
  var ticksPerSecond = 200
  var startHP = 20
  var startMoney = 1000
  
  var enemyBaseSpeed = 2
  var enemyDelay = 50
  
  var enemy1SpeedMultiplier = 1.0
  var enemy1HP = 50
  var enemy1revard = 3
  
  var enemy2SpeedMultiplier = 0.75
  var enemy2HP = 200
  var enemy2revard = 5
  
  var enemy3SpeedMultiplier = 1.5
  var enemy3HP = 80
  var enemy3revard = 6
  
  var t1DMG = 15
  var t1Cost = 50
  var t1range = 200
  var t1FR = 50
  var t1u1Cost = 100
  var t1u1DMG = 100
  var t1u1FR = 100
  
  var t2DMG = 80
  var t2Cost = 100
  var t2range = 400
  var t2FR = 250
  var t2u1Cost = 75
  var t2u1DMG = 40
  var t2u1FR = 75
  var t2u2Cost = 125
  var t2u2DMG = 25
  var t2u2FR = 100
  
}