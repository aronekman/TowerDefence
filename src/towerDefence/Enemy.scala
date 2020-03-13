package towerDefence

import constants._
import towerDefence.GUI.towerDefenceWorld
import towerDefence._
import java.awt.image._
import javax.imageio._
import java.io._

abstract class Enemy {
  var maxHP: Int
  var HP: Int = maxHP
  var reward: Int
  var startPos: (Int, Int) = (towerDefenceWorld.spawnPoint._1, towerDefenceWorld.spawnPoint._2)
  var posX: Int = startPos._1
  var posY: Int = startPos._2
  def pos: (Int, Int) = (posX, posY)
  var travelDistance: Int = 0
  var speed = enemySpeed
  var wayPoint: Int = 0
  def nextWayPoint: (Int, Int) = towerDefenceWorld.path(wayPoint)
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
    } else if (nextWayPoint._1 < posX && distanceBetweenPoints(towerDefenceWorld.path(wayPoint - 1), pos) < speed) {
      direction = "left"
      posX -= speed
      image = leftImage
    } else if (nextWayPoint._2 > posY && distanceBetweenPoints(towerDefenceWorld.path(wayPoint - 1), pos) < speed) {
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

class basicEnemy extends Enemy {
  var maxHP: Int = 100
  var reward: Int = 5
  val rightImage = ImageIO.read(new File("./Pics/basicEnemyRight.png"))
  val leftImage  = ImageIO.read(new File("./Pics/basicEnemyLeft.png"))
  val downImage  = ImageIO.read(new File("./Pics/basicEnemyDown.png"))
  val upImage    = ImageIO.read(new File("./Pics/basicEnemyUp.png"))
}