package towerDefence

import java.io._
import javax.imageio.ImageIO
import javax.swing.ImageIcon

object constants {
  
  var viewWidth = 1000
  var viewHeight = 750
  var guiWidth = 120
  var guiHeight = 80
  var totalWidth = viewWidth + guiWidth + 20
  var totalHeight = viewHeight + guiHeight + 55
  var squaresInRow = 20
  var squaresInColumn = 15
  var squareWidth = viewWidth/squaresInRow
  var squareHeight = viewHeight/squaresInColumn
  
  var ticksPerSecond = 200
  var startHP = 20
  var startMoney = 500
  
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
  
  var enemy4SpeedMultiplier = 0.5
  var enemy4HP = 2000
  var enemy4revard = 20
  
  var t1DMG = 15
  var t1Cost = 50
  var t1range = 200
  var t1FR = 100
  var t1u1Cost = 100
  var t1u1DMG = 100
  var t1u1FR = 40
  
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
  
  var t3DMG = 50
  var t3Cost = 250
  var t3range = 200
  var t3FR = 100
  var t3u1Cost = 100
  var t3u1DMG = 30
  var t3u1FR = 25
  var t3u2Cost = 150
  var t3u2DMG = 40
  var t3u2FR = 20
  
}