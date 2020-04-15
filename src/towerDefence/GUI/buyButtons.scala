package towerDefence.GUI

import scala.swing._
import towerDefence.constants
import javax.imageio._
import java.io.File

object buyButtons {
  val playButton = new Button("Start") {
    minimumSize = new Dimension(constants.guiWidth, 50)
    maximumSize = new Dimension(constants.guiWidth, 50)
    preferredSize = new Dimension(constants.guiWidth, 50)
  }
  
  val exitToMenu = new Button("Exit") {
    minimumSize = new Dimension(constants.guiWidth, 50)
    maximumSize = new Dimension(constants.guiWidth, 50)
    preferredSize = new Dimension(constants.guiWidth, 50)
  }
  val buyLabel = new Label("Buy:") {
    this.font = new Font("Calibri", 0, 25)
    setLabelSize(this)
    visible = false
  }
  
  val upgradeLabel = new Label("upgrade:") {
    this.font = new Font("Calibri", 0, 25)
    setLabelSize(this)
    visible = false
  }
  
  val tower1Button = new Button() {
    val towerModel = 1
    val image = ImageIO.read(new File("./Pics/tower1.png"))
    override def paintComponent(g: Graphics2D) {
      g.drawImage(image, 30, 0, null)
    }
    setButtonSize(this)
    visible = false
  }
  
  val tower1Cost = new Label("cost: " + constants.t1Cost.toString() + "$") {
    val towerModel = 1
    this.font = new Font("Calibri", 0, 20)
    setLabelSize(this)
    visible = false
  }
  
  val tower1UpgButton = new Button() {
    val upg = 1
    val towerModel = 1
    val image = ImageIO.read(new File("./Pics/tower1upg1.png"))
    override def paintComponent(g: Graphics2D) {
      g.drawImage(image, 30, 0, null)
    }
    setButtonSize(this)
    visible = false
  }
  
  val tower1UpgCost = new Label("cost: " + constants.t1u1Cost.toString() + "$") {
    val upg = 1
    val towerModel = 1
    this.font = new Font("Calibri", 0, 20)
    setLabelSize(this)
    visible = false
  }

    val tower2Button = new Button() {
    val towerModel = 2
    val image = ImageIO.read(new File("./Pics/tower2.png"))
    override def paintComponent(g: Graphics2D) {
      g.drawImage(image, 30, 0, null)
    }
    setButtonSize(this)
    visible = false
  }
  
  val tower2Cost = new Label("cost: " + constants.t2Cost.toString() + "$") {
    val towerModel = 2
    this.font = new Font("Calibri", 0, 20)
    setLabelSize(this)
    visible = false
  }
  
  val tower2Upg1Button = new Button() {
    val upg = 1
    val towerModel = 2
    val image = ImageIO.read(new File("./Pics/tower2upg1.png"))
    override def paintComponent(g: Graphics2D) {
      g.drawImage(image, 30, 0, null)
    }
    setButtonSize(this)
    visible = false
  }
   
  val tower2Upg1Cost = new Label("cost: " + constants.t2u1Cost.toString() + "$") {
    val upg = 1
    val towerModel = 2
    this.font = new Font("Calibri", 0, 20)
    setLabelSize(this)
    visible = false
  }
  
  val tower2Upg2Button = new Button() {
    val upg = 2
    val towerModel = 2
    val image = ImageIO.read(new File("./Pics/tower2upg2.png"))
    override def paintComponent(g: Graphics2D) {
      g.drawImage(image, 30, 0, null)
    }
    setButtonSize(this)
    visible = false
  }
  
  val tower2Upg2Cost = new Label("cost: " + constants.t2u2Cost.toString() + "$") {
    val upg = 2
    val towerModel = 2
    this.font = new Font("Calibri", 0, 20)
    setLabelSize(this)
    visible = false
  }

    val tower3Button = new Button() {
    val towerModel = 3
    val image = ImageIO.read(new File("./Pics/tower3.png"))
    override def paintComponent(g: Graphics2D) {
      g.drawImage(image, 30, 0, null)
    }
    setButtonSize(this)
    visible = false
  }
  
  val tower3Cost = new Label("cost: " + constants.t3Cost.toString() + "$") {
    val towerModel = 3
    this.font = new Font("Calibri", 0, 20)
    setLabelSize(this)
    visible = false
  }
  
  val tower3Upg1Button = new Button() {
    val upg = 1
    val towerModel = 3
    val image = ImageIO.read(new File("./Pics/tower3upg1.png"))
    override def paintComponent(g: Graphics2D) {
      g.drawImage(image, 30, 0, null)
    }
    setButtonSize(this)
    visible = false
  }
   
  val tower3Upg1Cost = new Label("cost: " + constants.t3u1Cost.toString() + "$") {
    val upg = 1
    val towerModel = 3
    this.font = new Font("Calibri", 0, 20)
    setLabelSize(this)
    visible = false
  }
  
  val tower3Upg2Button = new Button() {
    val upg = 2
    val towerModel = 3
    val image = ImageIO.read(new File("./Pics/tower3upg2.png"))
    override def paintComponent(g: Graphics2D) {
      g.drawImage(image, 30, 0, null)
    }
    setButtonSize(this)
    visible = false
  }
  
  val tower3Upg2Cost = new Label("cost: " + constants.t3u2Cost.toString() + "$") {
    val upg = 2
    val towerModel = 3
    this.font = new Font("Calibri", 0, 20)
    setLabelSize(this)
    visible = false
  }  
  val fullyUpgraded = new Label("Fully upgraded") {
    this.font = new Font("Calibri", 0, 18)
    setLabelSize(this)
    visible = false
  }
  
  def setButtonSize(button: Button) = {
    button.minimumSize = new Dimension(constants.guiWidth, 60)
    button.maximumSize = new Dimension(constants.guiWidth, 60)
    button.preferredSize = new Dimension(constants.guiWidth, 60)
  }
  
  def setLabelSize(label: Label) = {
    label.minimumSize = new Dimension(constants.guiWidth, 40)
    label.maximumSize = new Dimension(constants.guiWidth, 40)
    label.preferredSize = new Dimension(constants.guiWidth, 40)
  }
  def setOwnButtonSize(button: Button, width: Int, height: Int) = {
    button.minimumSize = new Dimension(width, height)
    button.maximumSize = new Dimension(width, height)
    button.preferredSize = new Dimension(width, height)
  }
}