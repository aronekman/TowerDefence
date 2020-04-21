package test

import org.scalatest._
import towerDefence._
import java.io.File

class GameTest extends FlatSpec {
  var game = new Game
  val testMap1 = new File("./Maps/testMap.txt")
  val testMap2 = new File("./Maps/testMap2.txt")
  game.loadGame(testMap1)
  var startHp = 20
  "loading file" should "be correct according to loaded file" in {
    assert(game.money == 500)
    assert(game.HP == startHp)
    assert(game.wave == 0)
    assert(game.allEnemies.size == 2)
  }
  
  val money1 = 500 - constants.t1Cost
  val money2 = 500 - constants.t1Cost - constants.t2Cost
  "buying towers" should "lose money and tower should be added" in {
    val tower1Pos = (constants.squareHeight*4, constants.squareWidth*7)
    val tower2Pos = (constants.squareHeight*5, constants.squareWidth*7)
    game.addTower("tower1", tower1Pos)
    assert(game.money == money1)
    assert(game.towers.exists(_.pos == tower1Pos) && game.towers.size == 1)
    game.addTower("tower2", tower2Pos)
    assert(game.money == money2)
    assert(game.towers.exists(_.pos == tower2Pos) && game.towers.size == 2)
  }

  val money3 = money2 + constants.enemy1revard*2
  "damage to enemies" should "get money and enemies should be killed" in {
  while(!game.waveOver) {
    game.timePasses()
  }
    assert(game.money == money3)
    assert(game.waveEnemies.size == 1)
    assert(game.HP == startHp)
  }

  "winning game" should "win game after all waves are over" in {
   game.waveOver = false
   while(game.spawnedEnemies.isEmpty) {
     game.spawnEnemies
   }
   while(!game.spawnedEnemies.isEmpty) {
     game.timePasses()
   }
   assert(game.isWon)
  }
  val money4 = money3 - constants.t1u1Cost - constants.t2u1Cost + constants.enemy1revard
  "upgrading towers" should "upgrade towers correctly if player has enough money" in {
    game.towers(0).upgrade
    game.towers(1).upgrade
    assert(game.money == money4)
    assert(!game.towers(0).nextUpgrade.isDefined && game.towers(0).upgradeNr == 1)
    assert(game.towers(1).nextUpgrade.isDefined && game.towers(1).upgradeNr == 1)
  }
  
  "losing HP" should "lose HP when enemies reach the base" in {
    game = new Game
    game.loadGame(testMap2)
    startHp = 10
    while(!game.waveOver) {
      game.timePasses()
    }
    assert(game.HP == startHp - 5)
    assert(game.money == 1000)
  } 
  
  "losing game" should "lose game when HP reaches zero" in {
    game.waveOver = false
    while(game.spawnedEnemies.isEmpty) {
     game.spawnEnemies
   }
   while(!game.spawnedEnemies.isEmpty) {
     game.timePasses()
   }
   assert(game.isLost)
  }
}