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

object towerDefenceApp extends SimpleSwingApplication {
  
  val testMap = new File("./Maps/testMap.txt")
  val grassImage = ImageIO.read(new File("./Pics/Background.png"))
  val roadImage = ImageIO.read(new File("./Pics/Road.png"))
  val towerSquareImage = ImageIO.read(new File("./Pics/towerSquare.png"))
  val game = new Game
  game.loader(testMap, game)
  game.roadCalculator
  var guiWidth = 120
  var guiHeight = 80

  def top = new MainFrame {
    this.title = "Tower Defence"
    this.resizable = false
    this.preferredSize = new Dimension(constants.viewWidth + guiWidth,constants.viewHeight + guiHeight) 
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
    
    
    val startButton = new Button("start")
    val basicTowerButton = new Button("buy\nBasicTower")
    basicTowerButton.visible = false
      
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
      
      val eastPanel = new BoxPanel(Orientation.Vertical) {
        preferredSize = new Dimension(guiWidth, constants.viewHeight)
        background = Color.white
        border = Swing.MatteBorder(0, 8, 0, 0, Color.darkGray)
        contents += startButton
        contents += Swing.HGlue
        contents += basicTowerButton
      }
      
      val southPanel = new BoxPanel(Orientation.Horizontal) {
        preferredSize = new Dimension(constants.viewWidth + guiWidth, guiHeight)
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
                basicTowerButton.visible = true
                clickedSquare = Some((x,y))
              }
              case None =>  basicTowerButton.visible = false
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
              basicTowerButton.visible = false
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
