package towerDefence.GUI

import towerDefence.{constants, Game, Tower}
import towerDefence.GUI._
import java.io.File
import scala.swing._
import javax.swing.Timer
import javax.imageio._
import java.awt.event.{ActionListener, ActionEvent}
import scala.swing.event._
import java.awt.{Color, Rectangle}
import javax.swing.ImageIcon

object towerDefenceApp extends SimpleSwingApplication {
  
  val testMap = new File("./Maps/testMap.txt")
  val grassImage = ImageIO.read(new File("./Pics/Background.png"))
  val roadImage = ImageIO.read(new File("./Pics/Road.png"))
  val towerSquareImage = ImageIO.read(new File("./Pics/towerSquare.png"))
  val game = new Game
  game.loader(testMap, game)
  game.roadCalculator


  def top = new MainFrame {
    this.title = "Tower Defence"
    this.resizable = false
    this.preferredSize = new Dimension(constants.viewWidth + constants.guiWidth + 20,constants.viewHeight + constants.guiHeight + 55) 
  //  this.minimumSize = new Dimension(constants.viewWidth + 89,constants.viewHeight + constants.squareHeight * 2)
  //  this.maximumSize = new Dimension(constants.viewWidth + 89,constants.viewHeight + constants.squareHeight * 2)
    var clickedSquare: Option[(Int,Int)] = None
    
    def draw(g: Graphics2D, pos: (Int, Int), pic: Image): Unit = {
      g.drawImage(pic, pos._1, pos._2, null)
    }
    
    def drawHP(g: Graphics2D, pos: (Int, Int), hp: Int) = {
      g.drawString(hp.toString(), pos._1+ constants.squareWidth/4, pos._2 + constants.squareHeight/4)
    }
    
    val hp = new Label("HP: " + game.HP.toString()) {
      this.font = new Font("Calibri", 0, 36)
      this.foreground = Color.red
    }
    
    val wave = new Label("Wave: " + (game.wave + 1).toString()) {
      this.font = new Font("Calibri", 0, 36)
      this.foreground = Color.black
    }
    
    val money = new Label("Money: " + game.money.toString()) {
      this.font = new Font("Calibri", 0, 36)
      this.foreground = Color.black
    }
    
    
    val startButton = buttons.startButton
    val basicTowerButton = buttons.basicTowerButton
    val basicTowerCost = buttons.basicTowerCost
    
    val allButtons = Seq(basicTowerButton, basicTowerCost)
      
    val map = new BorderPanel {
      override def paintComponent(g: Graphics2D) {
        game.grass.map(x => draw(g, x._2, grassImage))
        game.road.map(x => draw(g, x._2, roadImage))
        game.towerSquare.map(x => draw(g, x._2, towerSquareImage))
        game.towers.map(x => draw(g, x.pos, x.image))
        game.spawnedEnemies.map(x => draw(g, x.pos, x.image))
        game.spawnedEnemies.map(x => drawHP(g, x.pos, x.HP))
      }
      
      border = Swing.MatteBorder(8, 8, 8, 8, Color.darkGray)
       val upImage = ImageIO.read(new File("./Pics/basicTowerUp.png"))
      val eastPanel = new BoxPanel(Orientation.Vertical) {
        preferredSize = new Dimension(constants.guiWidth, constants.viewHeight)
        border = Swing.MatteBorder(0, 8, 0, 0, Color.darkGray)
        contents += startButton
        // contents += Swing.HGlue
        //contents += Swing.HStrut(50)
        contents += basicTowerButton
        contents += basicTowerCost

      }
      
      val southPanel = new BoxPanel(Orientation.Horizontal) {
        preferredSize = new Dimension(constants.viewWidth + constants.guiWidth, constants.guiHeight)
        border = Swing.MatteBorder(8, 0, 0, 0, Color.darkGray)
        contents += hp
        contents += Swing.HGlue
        contents += wave
        contents += Swing.HGlue
        contents += money
      }
      
      add(southPanel, BorderPanel.Position.South)
      add(eastPanel, BorderPanel.Position.East)
      
      listenTo(mouse.clicks)
        reactions += {
          case e: MouseClicked => {
            game.towerSquare.find(x => x._1.contains(e.point)).map(_._2) match {
              case Some((x,y)) => {
                allButtons.map(_.visible = true)
                clickedSquare = Some((x,y))
              }
              case None =>  allButtons.map(_.visible = false)
            }
          }
        }
  
      listenTo(startButton) 
      listenTo(basicTowerButton)
      reactions += {
        case ButtonClicked(b) => {
          if (b == startButton) {
            if (game.running) {
              b.text = "start"
              game.running = false
            } else {
              b.text = "pause"
              game.running = true
              wave.text = "Wave: %d".format(game.wave + 1)
            }
            startButton.revalidate()
            startButton.repaint()
          } 
          
          if (b == basicTowerButton) {
            if (constants.basicTowerCost <= game.money) {
              game.addTower("basicTower", clickedSquare.get)
              clickedSquare = None
              allButtons.map(_.visible = false)
              setMoney()
              money.repaint()
            }
          }
        }
      }
      
    }    
    
    private def setHP() = {
        hp.text = "HP: %d".format(game.HP)
      }
    private def setMoney() = {
      money.text = "Money: %d".format(game.money)
    }
    private def setWave() = {
      wave.text = "Wave: %d".format(game.wave)
    }
     
    contents = map 
    
    val movement = new ActionListener {
      def actionPerformed(e: ActionEvent) = {
        if (game.running) {
          map.repaint()
          game.timePasses()
          setHP()
          setMoney()
        } else {
          map.repaint()
          startButton.text = "start"
        }
      }
    }
     
    val timer = new Timer(1000/constants.ticksPerSecond, movement)
    timer.start()
  }
  

}

object buttons {
  val startButton = new Button("start") {
    minimumSize = new Dimension(constants.guiWidth, 50)
    maximumSize = new Dimension(constants.guiWidth, 50)
    preferredSize = new Dimension(constants.guiWidth, 50)
  }
  val image = ImageIO.read(new File("./Pics/basicTowerUp.png"))
  val basicTowerButton = new Button("200") {
    
    val image = ImageIO.read(new File("./Pics/basicTowerUp.png"))
    override def paintComponent(g: Graphics2D) {
       g.drawImage(image, 30, 0, null)
    }
    minimumSize = new Dimension(constants.guiWidth, 60)
    maximumSize = new Dimension(constants.guiWidth, 60)
    preferredSize = new Dimension(constants.guiWidth, 60)
    visible = false
  }
  val basicTowerCost = new Label("cost: " + constants.basicTowerCost.toString()) {
    this.font = new Font("Calibri", 0, 20)
    minimumSize = new Dimension(constants.guiWidth, 40)
    maximumSize = new Dimension(constants.guiWidth, 40)
    preferredSize = new Dimension(constants.guiWidth, 40)
    visible = false
    }
}