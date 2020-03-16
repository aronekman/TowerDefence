package towerDefence

import towerDefence._
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File

abstract class Tower(val pos: (Int, Int), game: Game) {
  var DMG: Int
  var cost: Int
  var range: Int
  var target: Option[Enemy] = None
  var image: BufferedImage
  var fireRate: Int
  
  
  var counter = fireRate
  def doDamage = {
    target match {
      case Some(enemy) =>  {
        if (counter == fireRate) {
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
}

class basicTower(pos: (Int, Int), game: Game) extends Tower(pos, game) {
  var DMG = 100
  var cost = 100
  var range = 200
  var fireRate = 200
  var image = ImageIO.read(new File("./Pics/basicTower.png"))

}

/*class archer(pos: (Int, Int)) extends Tower(pos) {
  var DMG = 50
  var cost = 50
  var range = 100
  var image = ???
}  */