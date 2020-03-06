package towerDefence

import constants._
import towerDefence.GUI.towerDefenceWorld

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
  def nextWayPoint: (Int, Int) = towerDefenceWorld.wayPoints(wayPoint)
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
  
  def isAlive: Boolean = HP > 0
  
  def takeDamage(DMG: Int) = {
    this.HP -= DMG
  }
    
  def changePosition = {
    wayPoint += 1
    if (nextWayPoint._1 > posX && distanceBetweenPoints(nextWayPoint, pos) < speed) {
      direction = "right"
      posX += speed
    } else if (nextWayPoint._1 < posX && distanceBetweenPoints(nextWayPoint, pos) < speed) {
      direction = "left"
      posX -= speed
    } else if (nextWayPoint._2 > posY && distanceBetweenPoints(nextWayPoint, pos) < speed) {
      direction = "down"
      posY += speed
    } else {
      direction = "up"
      posY -= speed
    }   
  }
  
  def move = {
    if (distanceBetweenPoints(nextWayPoint, pos) < speed) {
      this.changePosition
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
  
}