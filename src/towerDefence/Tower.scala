package towerDefence

import towerDefence._
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File
import scala.math._

abstract class Tower(val pos: (Int, Int), game: Game) {
  var DMG: Int
  var cost: Int
  var range: Int
  var target: Option[Enemy] = None
  var fireRate: Int
  val rightImage: BufferedImage
  val upImage: BufferedImage
  val leftImage: BufferedImage
  val downImage: BufferedImage
  var image: BufferedImage
  
  
  var counter = fireRate
  def doDamage = {
    target match {
      case Some(enemy) =>  {
        if (counter == fireRate) {
          changeImage(enemy.pos)
          enemy.takeDamage(DMG)
          counter = 0
        } else {
          counter  += 1
        }
      }
      case None =>
    }
  }
  
  def findTarget = {
    var inRange = game.spawnedEnemies.filter(x => constants.distanceBetweenPoints(x.pos, pos) <= range)
    if (inRange.isEmpty) {
      target = None
    } else {
      target = Some(inRange.maxBy(_.travelDistance))
    }
  }
  
  def changeImage(enemyPos: (Int, Int)) = {
    if (abs(constants.yDistance(pos, enemyPos)) > abs(constants.xDistance(pos, enemyPos))) {
      if (constants.yDistance(pos, enemyPos) < 0) {
        image = upImage
      } else {
        image = downImage
      }  
    } else {
      if (constants.xDistance(pos, enemyPos) < 0) {
        image = leftImage
      } else {
        image = rightImage
      }
    }
  }
  
}

class basicTower(pos: (Int, Int), game: Game) extends Tower(pos, game) {
  var DMG = 20
  var cost = constants.basicTowerCost
  var range = 200
  var fireRate = 50
  val rightImage = ImageIO.read(new File("./Pics/basicTowerRight.png"))
  val leftImage = ImageIO.read(new File("./Pics/basicTowerLeft.png"))
  val upImage = ImageIO.read(new File("./Pics/basicTowerUp.png"))
  val downImage = ImageIO.read(new File("./Pics/basicTowerDown.png"))
  var image = upImage
}

/*class archer(pos: (Int, Int)) extends Tower(pos) {
  var DMG = 50
  var cost = 50
  var range = 100
  var image = ???
}  */