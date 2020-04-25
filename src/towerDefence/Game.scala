package towerDefence

import towerDefence._
import constants._
import towerDefence.GUI.{gameReader, CorruptedTowerDefenceFileException}
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
  
  // true jos peli on hävitty
  def isLost: Boolean = HP <= 0
  
  //true jos peli on voitettu
  def isWon: Boolean = {
    (wave == allEnemies.length - 1) && waveEnemies.isEmpty && spawnedEnemies.isEmpty && !isLost
  }

  //lataa input tason
  def loadGame(input: File) = {
    gameReader.loadGame(new FileReader(input), this)
    this.roadCalculator
  }
  
  //jos peli ei ole hävitty ja tämän aallon kaikki viholliset ei ole vielä kartalla tämä metodi lisää vihollisia
  def spawnEnemies = {
    if (!waveEnemies.isEmpty && !isLost) {
      if (counter%enemyDelay == 0) {
        var enemy = waveEnemies.head
        enemy.nextWayPoint = path.head
        spawnedEnemies = spawnedEnemies :+ enemy
        waveEnemies = waveEnemies.tail
        counter += 1
      } else {
        counter += 1
      }
    }
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
  
  //lisää tornin kentälle annettuun sijaintiin
  def addTower(tower: String, pos: (Int, Int)) = {
    tower match {
      case "tower1" => {
        towers = towers :+ new tower1(pos, this)
        money -= constants.t1Cost
      }
      case "tower2" => {
        towers = towers :+ new tower2(pos, this)
        money -= constants.t2Cost
      }
      case "tower3" => {
        towers = towers :+ new tower3(pos, this)
        money -= constants.t3Cost
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
  
  //jos löytyy vihollinen joka on päässyt maaliin pelaajalta häviää yksi HP
  def loseHP = {
   spawnedEnemies.find(_.isInBase) match {
     case Some(enemy) => {
       spawnedEnemies = spawnedEnemies.filter(_ != enemy)
       HP -= 1
     }
     case None =>
   }
  }
  
  //jos tämän aallon kaikki viholliset on tapettu peli pysähtyy ja seuraavan aallon viholliset valitaan
  def waveChanger: Unit = {
    if ((spawnedEnemies.isEmpty && waveEnemies.isEmpty) && !this.isWon && !this.isLost) {
      running = false
      waveOver = true
      wave += 1
      waveEnemies = allEnemies(wave)
      allEnemies.map(_.map(enemy => enemy.HP += (enemy.HP*constants.enemyHpMultiplier).toInt))
    } else if (this.isWon) {
      running = false
    } else if (this.isLost) {
      running = false
    }
  }
  
  def timePasses() = {
    spawnedEnemies.map(_.move)
    spawnedEnemies = spawnedEnemies.sortBy(_.travelDistance)(Ordering[Double].reverse)
    towers.map(_.findTarget)
    towers.map(_.doDamage)
    spawnedEnemies.map(getMoney(_))
    spawnedEnemies = spawnedEnemies.filter(_.isAlive)
    this.loseHP
    this.spawnEnemies
    this.waveChanger
  }
  
  //järjestää reitin osat oikeaaseen järjestykseen
  def roadCalculator = {
    if (!road.isEmpty) {
      var grid = road.map(_._2)
      path = path :+ grid.minBy(distanceBetweenPoints(_, spawnPoint))
      grid = grid.filterNot(_ == path.last)
      while (path.size < road.size) {
        path = path :+ grid.minBy(distanceBetweenPoints(_, path.last))
        grid = grid.filter(_ != path.last)
      }
    } else {
      throw new CorruptedTowerDefenceFileException("Road not defined")
    }
  }
  
  private def distanceBetweenPoints(a: (Int, Int), b: (Int, Int)) = {
    Math.sqrt((a._2 - b._2) * (a._2 - b._2) + (a._1 - b._1) * (a._1 - b._1))
  }
  
}