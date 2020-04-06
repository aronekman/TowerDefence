package towerDefence

import towerDefence._
import constants._
import towerDefence.GUI.{gameReader}
import java.io._
import java.awt.Rectangle

class Game {
  
  var towers: Seq[Tower] = Seq()
  var spawnedEnemies: Seq[Enemy] = Seq()
  var allEnemies: Seq[Seq[Enemy]] = Seq()
  var counter = 0
  var wave = 0
  var HP = constants.startHP
  var money: Int = constants.startMoney
  var base: (Int, Int) = _
  var grass: Seq[(Rectangle,(Int, Int))] = Seq()
  var road: Seq[(Rectangle,(Int, Int))] = Seq()
  var towerSquare: Seq[(Rectangle,(Int, Int))]= Seq()
  var spawnPoint: (Int, Int) = _
  var path: Seq[(Int, Int)] = Seq()
  

   
  var waveEnemies: Seq[Enemy] = Seq()
  
  var running: Boolean = false
  var waveOver: Boolean = false
  
  def isLost: Boolean = HP <= 0
  def isWon: Boolean = {
    (wave == allEnemies.length - 1) && waveEnemies.isEmpty && spawnedEnemies.isEmpty && !isLost
  }
  
  def spawnEnemies = {
    if (!waveEnemies.isEmpty && !isLost) {
      if (counter%enemyDelay == 0) {
        spawnedEnemies = spawnedEnemies :+ waveEnemies(0)
        waveEnemies = waveEnemies.tail
        counter += 1
      } else {
        counter += 1
      }
    }
  }
  
  def loader(input: File, game: Game) = {
    gameReader.loadGame(new FileReader(input), game)
  }
  
   def addGrass(position: (Int, Int)) = {
    grass = grass :+ (new Rectangle(position._1, position._2, squareWidth, squareHeight), position)
  }
   
  def addRoad(position: (Int, Int)) = {
    road = road :+ (new Rectangle(position._1, position._2, squareWidth, squareHeight), position)
  }
  
  def addTowerSquare(position: (Int,Int)) = {
    towerSquare = towerSquare :+ (new Rectangle(position._1, position._2, squareWidth, squareHeight), position)
  }
  
  def addSpawnPoint(position: (Int, Int)) = {
    spawnPoint = position
  }
  
  def addTower(tower: String, pos: (Int, Int)) = {
    tower match {
      case "tower1" => {
        towers = towers :+ new tower1(pos, this)
        money -= constants.tower1Cost
      }
      case"tower2" => {
        towers = towers :+ new tower2(pos, this)
        money -= constants.tower2Cost
      }
      case _ =>
    }
  }
  
  def addEnemy(enemies: Seq[Enemy]) = {
    allEnemies = allEnemies :+ enemies
    if(allEnemies.size == 1) {
      waveEnemies = allEnemies(0)
    }
  }
  def addBase(position: (Int, Int)) = {
    base = position
  }
  
  def getMoney(enemy: Enemy) = {
    if (!enemy.isAlive) {
      money += enemy.reward
    }
  }
  
  def loseHP = {
    if (!spawnedEnemies.isEmpty && constants.distanceBetweenPoints(spawnedEnemies.head.pos, path.last) <= constants.enemySpeed) {
      spawnedEnemies = spawnedEnemies.drop(1)
      HP -= 1
    }
  }
  
  def waveChanger = {
    if ((spawnedEnemies.isEmpty && waveEnemies.isEmpty) && !this.isWon && !this.isLost) {
      running = false
      waveOver = true
      wave += 1
      waveEnemies = allEnemies(wave)
    } else if (this.isWon) {
      running = false
    } else if (this.isLost) {
      running = false
    }
  }
  
  def timePasses() = {
    spawnedEnemies.map(_.move)
    spawnedEnemies.map(_.travelDistance += 1)
    spawnedEnemies.sortBy(_.travelDistance)
    towers.map(_.findTarget)
    towers.map(_.doDamage)
    spawnedEnemies.map(getMoney(_))
    spawnedEnemies = spawnedEnemies.filter(_.isAlive)
    this.loseHP
    this.spawnEnemies
    this.waveChanger
  }
  
  def roadCalculator = {
    var grid = road.map(_._2)
    path = path :+ road.map(_._2).minBy(distanceBetweenPoints(_, spawnPoint))
    grid = grid.filterNot(_ == path.last)
    while (path.size < road.size) {
      path = path :+ grid.minBy(distanceBetweenPoints(_, path.last))
      grid = grid.filter(_ != path.last)
      }
  }
  
}