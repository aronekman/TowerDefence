package towerDefence

import towerDefence._
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File
import scala.math._
import java.awt.geom.AffineTransform

abstract class Tower(val pos: (Int, Int), game: Game) {
  var DMG: Int
  val cost: Int
  val range: Int
  var target: Option[Enemy] = None
  var fireRate: Int
  var towerImage: BufferedImage
  var image: BufferedImage
  // (image, cost, DMG upgrade, firerate upgrade)
  var upgrades: Seq[(BufferedImage, Int, Int, Int)]
  var nextUpgrade: Option[(BufferedImage, Int, Int, Int)]
  var upgradeNr: Int = 0
  val model: Int

  
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
  
  def upgrade = {
    nextUpgrade match {
      case Some((nextImage, cost, dmgUpg, fireRateUpg)) => {
        image = nextImage
        towerImage = nextImage
        upgradeNr += 1
        DMG += dmgUpg
        game.money -= cost
        fireRate -= fireRateUpg
        counter = fireRate
        if (upgradeNr < upgrades.size) {
          nextUpgrade = Option(upgrades(upgradeNr))
        } else {
          nextUpgrade = None
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

class tower1(pos: (Int, Int), game: Game) extends Tower(pos, game) {
  var DMG = 20
  val cost = constants.tower1Cost
  val range = 200
  var fireRate = 50
  var towerImage = ImageIO.read(new File("./Pics/tower1.png"))
  var image = towerImage
  val upg1 = ImageIO.read(new File("./Pics/tower1upg1.png"))
  var upgrades = Seq((upg1, 100, 20, 15))
  var nextUpgrade = Option(upgrades(0))
  val model: Int = 1
}

class tower2(pos: (Int, Int), game: Game) extends Tower(pos, game) {
  var DMG = 80
  val cost = constants.tower2Cost
  val range = 500
  var fireRate = 400
  var towerImage = ImageIO.read(new File("./Pics/tower2.png"))
  var image = towerImage
  val upg1 = ImageIO.read(new File("./Pics/tower2upg1.png"))
  val upg2 = ImageIO.read(new File("./Pics/tower2upg2.png"))
  var upgrades = Seq((upg1, 75, 25, 50),(upg2, 125, 35, 75))
  var nextUpgrade = Option(upgrades(0))
  val model: Int = 2
}  