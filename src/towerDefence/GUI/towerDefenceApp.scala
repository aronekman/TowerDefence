package towerDefence.GUI

import towerDefence.constants._
import towerDefence.Game
import towerDefence.GUI._
import java.io._
import scala.swing._
import javax.swing._
import javax.imageio._
import java.awt.image._
import java.awt.event._

object towerDefenceApp extends SimpleSwingApplication {
  
  val testMap = new File("./Maps/testMap.txt")
  val grassImage = ImageIO.read(new File("./Pics/Background.png"))
  val roadImage = ImageIO.read(new File("./Pics/Road.png"))
  val game = new Game
  towerDefenceWorld.loader(testMap)
  game.enemies = towerDefenceWorld.enemies
  towerDefenceWorld.roadCalculator

  def top = new MainFrame {
    this.title = "Tower Defence"
    this.resizable = false
    this.preferredSize = new Dimension(viewWidth,viewHeight)
    
    def draw(g: Graphics2D, pos: (Int, Int), pic: Image) = {
      g.drawImage(pic, pos._1, pos._2, null)
    }
    
    val map = new Panel {
     override def paintComponent(g: Graphics2D) {
       towerDefenceWorld.grass.map(draw(g, _, grassImage))
       towerDefenceWorld.road.map(draw(g, _, roadImage))
       game.spawnedEnemies.map(x => draw(g, x.pos, x.image))
     }
    }
    
    contents = map
    
    val movement = new ActionListener {
      def actionPerformed(e: ActionEvent) = {
        game.spawnedEnemies.map(_.move)
        map.repaint()
        game.timePasses()
      }
    }
       
    val timer = new Timer(1000/ticksPerSecond, movement)
    timer.start()
  }
  

}
