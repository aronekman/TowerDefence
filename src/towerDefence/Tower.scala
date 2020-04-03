package towerDefence

import towerDefence._
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File
import scala.math._
import java.awt.geom.AffineTransform

abstract class Tower(val pos: (Int, Int), game: Game) {
  val DMG: Int
  val cost: Int
  val range: Int
  var target: Option[Enemy] = None
  val fireRate: Int
  val towerImage: BufferedImage
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
     def angleCalcaluator: Double = {
       if (enemyPos._1 >= pos._1 && enemyPos._2 < pos._2) {
         Math.atan((enemyPos._1 - pos._1).toDouble/(pos._2 - enemyPos._2)).toDegrees
       } else if (enemyPos._1 > pos._1 && enemyPos._2 >= pos._2) {
          90.0 + Math.atan((enemyPos._2 - pos._2).toDouble/(enemyPos._1 - pos._1)).toDegrees
       } else if (enemyPos._1 <= pos._1 && enemyPos._2 > pos._2) {
         180.0 +  Math.atan((pos._1 - enemyPos._1).toDouble/(enemyPos._2 - pos._2)).toDegrees
       } else {
         270.0 + Math.atan((pos._2 - enemyPos._2).toDouble/(pos._1 - enemyPos._1)).toDegrees
       }
     }
     
     var w = towerImage.getWidth
     var h = towerImage.getHeight
     
     var rotated = new BufferedImage(w, h, towerImage.getType)
     var g = rotated.createGraphics()
     g.rotate(Math.toRadians(angleCalcaluator), w/2, h/2)
     g.drawImage(towerImage, null, 0, 0)
     g.dispose()
     image = rotated
  }
 
  
}

class basicTower(pos: (Int, Int), game: Game) extends Tower(pos, game) {
  val DMG = 20
  val cost = constants.basicTowerCost
  val range = 200
  val fireRate = 50
  val towerImage = ImageIO.read(new File("./Pics/basicTower.png"))
  var image = towerImage
  
}

/*class archer(pos: (Int, Int)) extends Tower(pos) {
  var DMG = 50
  var cost = 50
  var range = 100
  var image = ???
}  */