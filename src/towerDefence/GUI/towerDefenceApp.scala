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
  var buttonSize = new Dimension(constants.squareWidth *2, constants.squareHeight * 2)

  def top = new MainFrame {
    this.title = "Tower Defence"
    this.resizable = false
    this.preferredSize = new Dimension(constants.viewWidth + 87,constants.viewHeight + constants.squareHeight * 2)
  //  this.minimumSize = new Dimension(constants.viewWidth + 89,constants.viewHeight + constants.squareHeight * 2)
  //  this.maximumSize = new Dimension(constants.viewWidth + 89,constants.viewHeight + constants.squareHeight * 2)
    var clickedSquare: Option[(Int,Int)] = None
    
    def draw(g: Graphics2D, pos: (Int, Int), pic: Image): Unit = {
      g.drawImage(pic, pos._1, pos._2, null)
    }
    
    val map = new BorderPanel {
      override def paintComponent(g: Graphics2D) {
        game.grass.map(x => draw(g, x._2, grassImage))
        game.road.map(x => draw(g, x._2, roadImage))
        game.towerSquare.map(x => draw(g, x._2, towerSquareImage))
        game.towers.map(x => draw(g, x.pos, x.image))
        game.spawnedEnemies.map(x => draw(g, x.pos, x.image))
      }
      
      border = Swing.MatteBorder(0, 0, 0, 0, Color.white)
      val startButton = new Button("start")
      startButton.minimumSize = buttonSize
      val basicTowerButton = new Button("buy\nBasicTower")
      basicTowerButton.visible = false
      basicTowerButton.preferredSize = buttonSize
      
      val buttons = new BoxPanel(Orientation.Vertical) {
        border = Swing.MatteBorder(0, 0, 0, 0, Color.white)
        contents += startButton 
        contents += basicTowerButton
      }
      
      add(buttons, BorderPanel.Position.East)
      
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
      reactions += {
        case ButtonClicked(b) => if (b == startButton) {
            if (game.running) {
              b.text = "start"
              game.running = false
            } else {
              b.text = "pause"
              game.running = true
            }
             startButton.revalidate()
             startButton.repaint()
          }
      }
      
      listenTo(basicTowerButton)
      reactions += {
        case ButtonClicked(b) => if (b == basicTowerButton) {
          game.addTower("basicTower", clickedSquare.get)
          clickedSquare = None
          basicTowerButton.visible = false
        }
      }
    }
    
    
     
    contents = map 
    
    val movement = new ActionListener {
      def actionPerformed(e: ActionEvent) = {
        if (game.running) {
          map.repaint()
          game.timePasses()
        } else {
          map.repaint()
        }
      }
    }
     
    val timer = new Timer(1000/constants.ticksPerSecond, movement)
    timer.start()
  }
  

}
