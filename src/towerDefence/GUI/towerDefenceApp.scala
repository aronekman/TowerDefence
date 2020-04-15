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
  
  val grassImage = ImageIO.read(new File("./Pics/Background.png"))
  val roadImage = ImageIO.read(new File("./Pics/Road.png"))
  val towerSquareImage = ImageIO.read(new File("./Pics/towerSquare.png"))
  val spawnImage = ImageIO.read(new File("./Pics/enemySpawnPoint.png"))
  val baseImage = ImageIO.read(new File("./Pics/base.png"))
  var game = new Game
  var chosenMap: Option[File] = None

  def top = new MainFrame {
    this.title = "Tower Defence"
    this.resizable = false
    this.preferredSize = new Dimension(constants.totalWidth, constants.totalHeight) 
 
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
    private def startGame(map: File): Unit = {
      chosenMap = Some(map)
      game = new Game
      game.loadGame(map)
      gui.contents -= mapChoiseMenu
      gui.contents += gameScreen
      continueButton.visible = true
      setMoney()
      setHP()
      setWave()
      allTowerButtons.map(_.visible = false)
      allUpgButtons.map(_.visible = false)
      allUpgLabels.map(_.visible = false)
      gui.revalidate()
      gui.repaint()
      timer.start()
    } 
    
    private def menuToMapChooser(): Unit = {
      gui.contents -= menu
      gui.contents += mapChoiseMenu
      gui.revalidate()
      gui.repaint()
    }
    private def mapChooserToMenu(): Unit = {
      gui.contents += menu
      gui.contents -= mapChoiseMenu
      gui.revalidate()
      gui.repaint()
    }
    
    private def continueGame(): Unit = {
      gui.contents += gameScreen
      gui.contents -= menu
      gui.revalidate()
      gui.repaint()
      timer.start()
      game.running = false
    }
    
    private def exitToMenu(): Unit = {
      gui.contents -= gameScreen
      gui.contents += menu
      gui.revalidate()
      gui.repaint()
      timer.stop()
    }
    
    private def restartGame(): Unit = {
      chosenMap match {
        case Some(map) => {
          gui.contents -= gameOverScreen
          this.startGame(map)
        }
        case None => gui.contents -= gameOverScreen
        gui.contents += menu
      }
    }
    val continueButton = new Button("Continue") {
      buyButtons.setOwnButtonSize(this, 150, 50)
      this.visible = false
    }
    
    val menu = new BorderPanel {
      val startGameButton = new Button("Start new game") {
        buyButtons.setOwnButtonSize(this, 150, 50)
      }
      
      val exitGameButton = new Button("Exit game") {
        buyButtons.setOwnButtonSize(this, 150, 50)
      }
      
      val guideButton = new Button("Guide") {
        buyButtons.setOwnButtonSize(this, 150, 50)
      }
      
      override def paintComponent(g: Graphics2D) {
        for {
          x <- 0 until constants.totalWidth by constants.squareWidth
          y <- 0 until constants.totalHeight by constants.squareHeight
        }
        draw(g, (x,y), grassImage)
      }
      border = Swing.MatteBorder(8, 8, 8, 8, Color.darkGray)
      
      val startPanel = new BoxPanel(Orientation.Vertical) {
        this.opaque = false
        contents += Swing.VStrut(constants.totalHeight/2 - 80)
        contents += startGameButton
        contents += continueButton
        contents += guideButton
        contents += exitGameButton
      }
      
      val emptyPanel = new BoxPanel(Orientation.Horizontal) {
        this.preferredSize = new Dimension(constants.totalWidth/2 - 90, constants.totalHeight)
        this.opaque = false
      }
      
      add(emptyPanel, BorderPanel.Position.West)
      add(startPanel, BorderPanel.Position.Center)
      
      listenTo(startGameButton)
      listenTo(exitGameButton)
      listenTo(continueButton)
      listenTo(guideButton)
      reactions += {
        case ButtonClicked(b) => {
          if (b == startGameButton) {
            menuToMapChooser()
          } else if (b == exitGameButton) {
            System.exit(0)
          } else if (b == continueButton) {
            continueGame()
          } else if (b == guideButton) {
            guideMenu()
          }
        }
      }
    }
    
    private def guideMenu(): Unit = {
      if (gui.contents.contains(menu)) {
        gui.contents -= menu
        gui.contents += guide
      } else {
        gui.contents -= guide
        gui.contents += menu
      }
      gui.revalidate()
      gui.repaint()
    }
    
    val guide = new BorderPanel {
      border = Swing.MatteBorder(8, 8, 8, 8, Color.darkGray)
      val backButton = new Button("Back") {
        buyButtons.setOwnButtonSize(this, 100, 50)
      }
      val guideImage = ImageIO.read(new File("./Pics/guide.png"))
      
      override def paintComponent(g: Graphics2D) {
        draw(g, (0,0), guideImage)
      }
      val buttonPanel = new BoxPanel(Orientation.Horizontal) {
        this.opaque = false
        contents += Swing.HStrut(constants.totalWidth/2 - 75)
        contents += backButton
      }
      add(buttonPanel, BorderPanel.Position.South) 
      listenTo(backButton) 
      reactions += {
        case ButtonClicked(b) => {
          if (b == backButton) {
            guideMenu()
          }
        }
      }
    }
    
    val mapChoiseMenu = new BorderPanel {
      border = Swing.MatteBorder(8, 8, 8, 8, Color.darkGray)
      override def paintComponent(g: Graphics2D) {
        for {
          x <- 0 until constants.totalWidth by constants.squareWidth
          y <- 0 until constants.totalHeight by constants.squareHeight
        }
        draw(g, (x,y), grassImage)
      }
      val chooseMap = new Label("Choose map:") {
        this.font = new Font("calibri", 0, 28)
      }
      
      val easyMap = new Button("Easy") {
        buyButtons.setOwnButtonSize(this, 150, 50)
        val map = new File("./Maps/easyMap.txt")
      }
      
      val mediumMap = new Button("Medium") {
        buyButtons.setOwnButtonSize(this, 150, 50)
        val map = new File("./Maps/mediumMap.txt")
      }
      
      val hardMap = new Button("Hard") {
        buyButtons.setOwnButtonSize(this, 150, 50)
        val map = new File("./Maps/hardMap.txt")
      }
      
      val extremeMap = new Button("Extreme") {
        buyButtons.setOwnButtonSize(this, 150, 50)
        val map = new File("./Maps/extremeMap.txt")
      }
      
      val back = new Button("Back") {
        buyButtons.setOwnButtonSize(this, 150, 50)
      }
      val allMapButtons = Seq(easyMap, mediumMap, hardMap, extremeMap)
      
      val panel = new BoxPanel(Orientation.Vertical) {
        this.opaque = false
        contents += Swing.VStrut(constants.totalHeight/2 - 100)
        contents += chooseMap
        contents += easyMap
        contents += mediumMap
        contents += hardMap
        contents += extremeMap
        contents += back
      }
      
      val emptyPanel = new BoxPanel(Orientation.Horizontal) {
        this.preferredSize = new Dimension(constants.totalWidth/2 - 90, constants.totalHeight)
        this.opaque = false
      }
      
      add(emptyPanel, BorderPanel.Position.West)
      add(panel, BorderPanel.Position.Center)
      allMapButtons.map(listenTo(_))
      listenTo(back)
      reactions += {
        case ButtonClicked(b) => {
          if (b == back) {
            mapChooserToMenu
          } else {
            allMapButtons.find(_ == b) match {
              case Some(button) => {
                startGame(button.map)
              }
              case None =>
            }
          }
        }
      }
    }
    
    val playButton = buyButtons.playButton
    val exitButton = buyButtons.exitToMenu
    val buyLabel = buyButtons.buyLabel
    val upgradeLabel = buyButtons.upgradeLabel
    
    val tower1Button = buyButtons.tower1Button
    val tower1Cost = buyButtons.tower1Cost
    val tower2Button = buyButtons.tower2Button
    val tower2Cost = buyButtons.tower2Cost
    val tower3Button = buyButtons.tower3Button
    val tower3Cost = buyButtons.tower3Cost
    
    val allTowerButtons = Seq(buyLabel, tower1Button, tower1Cost, tower2Button, tower2Cost, tower3Button, tower3Cost)
    
    val tower1upgButton = buyButtons.tower1UpgButton
    val tower1UpgCost = buyButtons.tower1UpgCost
    val tower2upg1Button = buyButtons.tower2Upg1Button
    val tower2Upg1Cost = buyButtons.tower2Upg1Cost    
    val tower2upg2Button = buyButtons.tower2Upg2Button
    val tower2Upg2Cost = buyButtons.tower2Upg2Cost
    val tower3upg1Button = buyButtons.tower3Upg1Button
    val tower3Upg1Cost = buyButtons.tower3Upg1Cost    
    val tower3upg2Button = buyButtons.tower3Upg2Button
    val tower3Upg2Cost = buyButtons.tower3Upg2Cost
    val fullyUpgraded = buyButtons.fullyUpgraded
    
    val allUpgButtons = Seq(tower1upgButton, tower2upg1Button, tower2upg2Button, tower3upg1Button, tower3upg2Button)
    val allUpgLabels = Seq(upgradeLabel, tower1UpgCost, tower2Upg1Cost, tower2Upg2Cost, tower3Upg1Cost, tower3Upg2Cost, fullyUpgraded)
    val allUpg = allUpgButtons.zip(allUpgLabels.tail.init)
    
    val gameScreen = new BorderPanel {
      override def paintComponent(g: Graphics2D) {
        game.grass.map(x => draw(g, x._2, grassImage))
        game.path.tail.init.map(x => draw(g, x, roadImage))
        draw(g, game.path.head, spawnImage)
        draw(g, game.path.last, baseImage)
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
        contents += tower3Button
        contents += tower3Cost
        
        contents += upgradeLabel
        contents += tower1upgButton
        contents += tower1UpgCost
        contents += tower2upg1Button
        contents += tower2Upg1Cost
        contents += tower2upg2Button
        contents += tower2Upg2Cost
        contents += tower3upg1Button
        contents += tower3Upg1Cost
        contents += tower3upg2Button
        contents += tower3Upg2Cost
        contents += fullyUpgraded
        
        contents += Swing.VGlue
        contents += exitButton
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
      listenTo(tower3Button)
      listenTo(exitButton)
      allUpgButtons.map(listenTo(_))
      reactions += {
        case ButtonClicked(b) => {
          if (b == playButton) {
            if (game.waveOver) {
              b.text = "Pause"
              game.running = true
              game.waveOver = false
              wave.text = "Wave: %d".format(game.wave + 1)
            } else if (game.running) {
              b.text = "Start"
              game.running = false
            } else {
              b.text = "Pause"
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
          } else if (b == tower3Button) {
            if (constants.t3Cost <= game.money) {
              game.addTower("tower3", clickedSquare.get)
              clickedSquare = None
              allTowerButtons.map(_.visible = false)
              setMoney()
            }
          } else if (b == exitButton) {
            exitToMenu()
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
    
    private def showGameOverScreen(): Unit = {
      gui.contents -= gameScreen
      gui.contents += gameOverScreen
      continueButton.visible = false
      gui.revalidate()
      gui.repaint()
      timer.stop()
      waveLabel.text = "you made it\n to  wave %d".format(game.wave + 1)
    }
    
    private def exitFromGameOver(): Unit = {
      gui.contents -= gameOverScreen
      gui.contents += menu
      gui.revalidate()
      gui.repaint()
    }
          
    val waveLabel = new TextArea {
      this.font = new Font("calibri", 0, 30)
      this.opaque = false
      this.editable = false
      this.preferredSize = new Dimension(200,80)
      this.maximumSize = new Dimension(200,80)
      this.minimumSize = new Dimension(200,80)
      this.xLayoutAlignment = -50
    }
    
    val gameOverScreen = new BorderPanel {
      border = Swing.MatteBorder(8, 8, 8, 8, Color.darkGray)
      override def paintComponent(g: Graphics2D) {
        for {
          x <- 0 until constants.totalWidth by constants.squareWidth
          y <- 0 until constants.totalHeight by constants.squareHeight
        }
        draw(g, (x,y), grassImage)
      } 
      
      val gameOverLabel = new Label("Game Over") {
        this.font = new Font("calibri", 0, 33)
        this.foreground = Color.red
      }
      
      val exitButton = new Button("Exit") {
        buyButtons.setOwnButtonSize(this, 150, 50)
      }
      
      val restartButton = new Button("Restart") {
        buyButtons.setOwnButtonSize(this, 150, 50)
      }
      
      val gameOverExitButton = new Button("Exit") {
        buyButtons.setOwnButtonSize(this, 150, 50)
      }
      
      val panel = new BoxPanel(Orientation.Vertical) {
        this.opaque = false
        contents += Swing.VStrut(constants.totalHeight/2 - 120)
        contents += gameOverLabel
        contents += waveLabel
        contents += restartButton
        contents += gameOverExitButton
      }
      
      val emptyPanel = new BoxPanel(Orientation.Vertical) {
        this.preferredSize = new Dimension(constants.totalWidth/2 - 90, constants.totalHeight)
        this.opaque = false
      }
      
      add(emptyPanel, BorderPanel.Position.West)
      add(panel, BorderPanel.Position.Center)
      
      listenTo(restartButton)
      listenTo(gameOverExitButton)
      reactions += {
        case ButtonClicked(b) => {
          if (b == restartButton) {
            restartGame()
          } else if (b == gameOverExitButton) {
            exitFromGameOver()
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
      if (game.wave > 0) {
        wave.text = "Wave: %d".format(game.wave)
      } else {
        wave.text = "Wave: 1"
      }
    }
    
    val gui = new BoxPanel(Orientation.Vertical) {
      contents += menu
    }
     
    contents = gui 
    
    val movement = new ActionListener {
      def actionPerformed(e: ActionEvent) = {
        if (game.running) {
          gameScreen.repaint()
          game.timePasses()
          setHP()
          setMoney()
        } else if (game.waveOver){
          gameScreen.repaint()
          playButton.text = "Next wave"
        } else if (game.isLost) {
          showGameOverScreen
        } else {
          gameScreen.repaint()
          playButton.text = "Start"
        }
      }
    }
     
    val timer = new Timer(1000/constants.ticksPerSecond, movement)
  }

}
