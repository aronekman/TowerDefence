package towerDefence.GUI

import towerDefence.constants._
import towerDefence.Game
import o1._

object towerDefenceApp extends App {
  
  val scenery = rectangle(ViewWidth, ViewHeight, Green)
  val game = new Game
  
  val gui = new View(game, "Tower Defence") {
    val background = scenery
    def makePic = {
      this.background
    }
    override def isDone = game.isLost
  }
  gui.start()
}