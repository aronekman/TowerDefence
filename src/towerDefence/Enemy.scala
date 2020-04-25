package towerDefence

import constants._
import towerDefence.constants._
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File

abstract class Enemy(game: Game) {
  var HP: Int
  var reward: Int
  var startPos: (Int, Int) = (game.spawnPoint._1, game.spawnPoint._2)
  var posX: Double = startPos._1.toDouble
  var posY: Double = startPos._2.toDouble
  def pos: (Int, Int) = (posX.toInt, posY.toInt)
  var travelDistance: Double = 0
  var speed: Double
  var wayPoint: Int = 0
  var isInBase: Boolean = false
  var nextWayPoint: (Int, Int) = _
  
  //antaa seuraavan wayPointin, siis mihin suuntaan vihollinen pitäisi liikkua
  def wayPointCalculator: Option[(Int, Int)] = {
    if (wayPoint < game.path.size) {
      Option(game.path(wayPoint))
    } else {
      None
    }
  } 
  
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

  //true jos tällä vihollisella vielä on HP:ta
  def isAlive: Boolean = HP > 0
  
  //tältä vihollislta häviää DMG, verran HP:ta
  def takeDamage(DMG: Int) = {
    this.HP -= DMG
  }
  
  //muuttaa suuntaa ja kuvaa
  def changeDirection = {
    posX = nextWayPoint._1.toDouble
    posY = nextWayPoint._2.toDouble
    wayPoint += 1
    wayPointCalculator match {
      case Some((x,y)) => {
        nextWayPoint = (x,y)
        var xDist = x - posX
        var yDist = y - posY
        if (xDist > yDist && Distance(posY, nextWayPoint._2) < speed) {
          direction = "right"
          posX += speed
          image = rightImage
        } else if (xDist < yDist && Distance(posY, nextWayPoint._2) < speed) {
          direction = "left"
          posX -= speed
          image = leftImage
        } else if (yDist > xDist && Distance(posX, nextWayPoint._1) < speed) {
          direction = "down"
          posY += speed
          image = downImage
        } else {
          direction = "up"
          posY -= speed
          image = upImage
        }   
      }
      case None => {
        isInBase = true
      }
    }
  }
  
  //liikuttaa vihollista
  def move = {
    travelDistance += speed
    direction match {
      case "right" => {
        if (Distance(posX, nextWayPoint._1) < speed) {
          this.changeDirection
        } else {
           posX += speed
        }
      }
      case "down"  => {
        if (Distance(posY, nextWayPoint._2) < speed) {
          this.changeDirection
        } else {
           posY += speed
        }
      }
      case "left"  => {
        if (Distance(posX, nextWayPoint._1) < speed) {
          this.changeDirection
        } else {
           posX -= speed
        }
      }
      case "up" => {
        if (Distance(posY, nextWayPoint._2) < speed) {
          this.changeDirection
        } else {
           posY -= speed
        }
      }
    }
  }
  
  private def Distance(from: Double, to: Int): Double = {
    Math.abs(to - from)
  }
  
}

class Enemy1(game: Game) extends Enemy(game) {
  var HP: Int = enemy1HP
  var reward: Int = enemy1revard
  var speed = (enemyBaseSpeed*enemy1SpeedMultiplier)
  val rightImage = ImageIO.read(new File("./Pics/enemy1Right.png"))
  val leftImage  = ImageIO.read(new File("./Pics/enemy1Left.png"))
  val downImage  = ImageIO.read(new File("./Pics/enemy1Down.png"))
  val upImage    = ImageIO.read(new File("./Pics/enemy1Down.png"))
}

class Enemy2(game: Game) extends Enemy(game) {
  var HP: Int = enemy2HP
  var reward: Int = enemy2revard
  var speed = (enemyBaseSpeed*enemy2SpeedMultiplier)
  val rightImage = ImageIO.read(new File("./Pics/enemy2Right.png"))
  val leftImage  = ImageIO.read(new File("./Pics/enemy2Left.png"))
  val downImage  = ImageIO.read(new File("./Pics/enemy2Down.png"))
  val upImage    = ImageIO.read(new File("./Pics/enemy2Down.png"))
}

class Enemy3(game: Game) extends Enemy(game) {
  var HP: Int = enemy3HP
  var reward: Int = enemy3revard
  var speed = (enemyBaseSpeed*enemy3SpeedMultiplier)
  val rightImage = ImageIO.read(new File("./Pics/enemy3Right.png"))
  val leftImage  = ImageIO.read(new File("./Pics/enemy3Left.png"))
  val downImage  = ImageIO.read(new File("./Pics/enemy3Down.png"))
  val upImage    = ImageIO.read(new File("./Pics/enemy3Down.png"))
}

class Enemy4(game: Game) extends Enemy(game) {
  var HP: Int = enemy4HP
  var reward: Int = enemy4revard
  var speed = (enemyBaseSpeed*enemy4SpeedMultiplier)
  val rightImage = ImageIO.read(new File("./Pics/enemy4Right.png"))
  val leftImage  = ImageIO.read(new File("./Pics/enemy4Left.png"))
  val downImage  = ImageIO.read(new File("./Pics/enemy4Down.png"))
  val upImage    = ImageIO.read(new File("./Pics/enemy4Up.png"))
}
