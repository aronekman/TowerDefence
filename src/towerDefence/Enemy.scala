package towerDefence

import constants._
import towerDefence._
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File

abstract class Enemy(game: Game) {
  var HP: Int
  var reward: Int
  var startPos: (Int, Int) = (game.spawnPoint._1, game.spawnPoint._2)
  var posX: Int = startPos._1
  var posY: Int = startPos._2
  def pos: (Int, Int) = (posX, posY)
  var travelDistance: Int = 0
  var speed = enemySpeed
  var wayPoint: Int = 0
  def nextWayPoint: (Int, Int) = game.path(wayPoint)
  var direction: String = {
    if (startPos._1 < 50) {
      "right"
    } else if (startPos._2 < 50) {
      "down"
    } else if (startPos._1 > viewWidth - 50) {
      "left"
    } else {
      "up"
    }
  }
  val rightImage: BufferedImage
  val leftImage: BufferedImage
  val downImage: BufferedImage
  val upImage: BufferedImage
  
  var image: BufferedImage = {
    direction match {
      case "right" => rightImage
      case "left"  => leftImage
      case "down"  => downImage
      case "up"    => upImage
    }
  }

  
  def isAlive: Boolean = HP > 0
  
  def takeDamage(DMG: Int) = {
    this.HP -= DMG
  }
    
  def changeDirection = {
    wayPoint += 1
    if (nextWayPoint._1 > posX) {
      direction = "right"
      posX += speed
      image = rightImage
    } else if (nextWayPoint._1 < posX && distanceBetweenPoints(game.path(wayPoint - 1), pos) < speed) {
      direction = "left"
      posX -= speed
      image = leftImage
    } else if (nextWayPoint._2 > posY && distanceBetweenPoints(game.path(wayPoint - 1), pos) < speed) {
      direction = "down"
      posY += speed
      image = downImage
    } else {
      direction = "up"
      posY -= speed
      image = upImage
    }   
  }
  
  def move = {
    if (distanceBetweenPoints(nextWayPoint, pos) < speed) {
      this.changeDirection
    } else {
        direction match {
          case "right" => posX += speed
          case "down"  => posY += speed
          case "left"  => posX -= speed
          case "up"    => posY -= speed
      }
    }
  }
}

class BasicEnemy(game: Game) extends Enemy(game) {
  var HP: Int = 50
  var reward: Int = 3
  val rightImage = ImageIO.read(new File("./Pics/basicEnemyRight.png"))
  val leftImage  = ImageIO.read(new File("./Pics/basicEnemyLeft.png"))
  val downImage  = ImageIO.read(new File("./Pics/basicEnemyDown.png"))
  val upImage    = ImageIO.read(new File("./Pics/basicEnemyUp.png"))
}
