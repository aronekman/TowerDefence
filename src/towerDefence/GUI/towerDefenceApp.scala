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


  def top = new MainFrame {
    this.title = "Tower Defence"
    this.resizable = false
    this.preferredSize = new Dimension(constants.viewWidth + constants.guiWidth + 20,constants.viewHeight + constants.guiHeight + 55) 
 
    var clickedSquare: Option[(Int,Int)] = None
    var clickedTower: Option[Tower] = None
    
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
    
    val money = new Label("Money: " + game.money.toString() + "$") {
      this.font = new Font("Calibri", 0, 36)
      this.foreground = Color.black
    }
    
    
    val playButton = buyButtons.playButton
    val buyLabel = buyButtons.buyLabel
    val upgradeLabel = buyButtons.upgradeLabel
    
    val tower1Button = buyButtons.tower1Button
    val tower1Cost = buyButtons.tower1Cost
    val tower2Button = buyButtons.tower2Button
    val tower2Cost = buyButtons.tower2Cost
    
    val allTowerButtons = Seq(buyLabel, tower1Button, tower1Cost, tower2Button, tower2Cost)
    
    val tower1upgButton = buyButtons.tower1UpgButton
    val tower1UpgCost = buyButtons.tower1UpgCost
    val tower2upg1Button = buyButtons.tower2Upg1Button
    val tower2Upg1Cost = buyButtons.tower2Upg1Cost    
    val tower2upg2Button = buyButtons.tower2Upg2Button
    val tower2Upg2Cost = buyButtons.tower2Upg2Cost
    val fullyUpgraded = buyButtons.fullyUpgraded
    
    val allUpgButtons = Seq(tower1upgButton, tower2upg1Button, tower2upg2Button)
    val allUpgLabels = Seq(upgradeLabel, tower1UpgCost, tower2Upg1Cost, tower2Upg2Cost, fullyUpgraded)
    val allUpg = allUpgButtons.zip(allUpgLabels.tail.init)
    
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
        preferredSize = new Dimension(constants.guiWidth, constants.viewHeight)
        border = Swing.MatteBorder(0, 8, 0, 0, Color.darkGray)
        contents += playButton
        
        contents += buyLabel
        contents += tower1Button
        contents += tower1Cost
        contents += tower2Button
        contents += tower2Cost
        
        contents += upgradeLabel
        contents += tower1upgButton
        contents += tower1UpgCost
        contents += tower2upg1Button
        contents += tower2Upg1Cost
        contents += tower2upg2Button
        contents += tower2Upg2Cost
        contents += fullyUpgraded
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
              case Some(position) => {
                game.towers.find(x => x.pos == position) match {
                  case Some(tower) => {
                    upgButtons(tower)
                    clickedTower = Some(tower)
                    clickedSquare = None
                  }
                  case None => {
                    allTowerButtons.map(_.visible = true)
                    allUpgButtons.map(_.visible = false)
                    allUpgLabels.map(_.visible = false)
                    clickedSquare = Some(position)
                    clickedTower = None
                  }
                }
              }
              case None =>  {
                clickedSquare = None
                clickedTower = None
                allTowerButtons.map(_.visible = false)
                allUpgButtons.map(_.visible = false)
                allUpgLabels.map(_.visible = false)
              }
            }
          }
        }
  
      listenTo(playButton) 
      listenTo(tower1Button)
      listenTo(tower2Button)
      allUpgButtons.map(listenTo(_))
      reactions += {
        case ButtonClicked(b) => {
          if (b == playButton) {
            if (game.waveOver) {
              b.text = "pause"
              game.running = true
              game.waveOver = false
              wave.text = "Wave: %d".format(game.wave + 1)
            } else if (game.running) {
              b.text = "start"
              game.running = false
            } else {
              b.text = "pause"
              game.running = true
            }
            playButton.revalidate()
            playButton.repaint()
          } else if (b == tower1Button) {
            if (constants.t1Cost <= game.money) {
              game.addTower("tower1", clickedSquare.get)
              clickedSquare = None
              allTowerButtons.map(_.visible = false)
              setMoney()
            }
          } else if (b == tower2Button) {
            if (constants.t2Cost <= game.money) {
              game.addTower("tower2", clickedSquare.get)
              clickedSquare = None
              allTowerButtons.map(_.visible = false)
              setMoney()
            }
          } else {
            clickedTower match {
              case Some(tower) => {
                if (game.money >= tower.nextUpgrade.get._2) {
                  tower.upgrade
                  setMoney()
                  allUpgButtons.map(_.visible = false)
                  allUpgLabels.map(_.visible = false)
                }
              }
              case None =>
            }
          }
        }
      }
      
    } 
    
    private def upgButtons(tower: Tower) {
      allUpg.find(x => x._1.towerModel == tower.model && x._1.upg - 1 == tower.upgradeNr) match {
        case Some((b,l)) => {
          allUpgButtons.map(_.visible = false)
          allUpgLabels.map(_.visible = false)
          allTowerButtons.map(_.visible = false)
          upgradeLabel.visible = true
          b.visible = true
          l.visible = true
        }
        case None => {
          allUpgButtons.map(_.visible = false)
          allUpgLabels.map(_.visible = false)
          allTowerButtons.map(_.visible = false)
          fullyUpgraded.visible = true
        }
      }
    } 
     
    private def setHP() = {
        hp.text = "HP: %d".format(game.HP)
      }
    private def setMoney() = {
      money.text = "Money: %d$".format(game.money)
      money.repaint()
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
        } else if (game.waveOver){
          map.repaint()
          playButton.text = "next wave"
        } else {
          map.repaint()
          playButton.text = "start"
        }
      }
    }
     
    val timer = new Timer(1000/constants.ticksPerSecond, movement)
    timer.start()
  }

}
