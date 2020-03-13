package towerDefence.GUI

import java.io._
import towerDefence.GUI._
import towerDefence._
import scala.swing._
import constants._

object towerDefenceWorld {

  var grass: Seq[(Int, Int)] = Seq()
  var road: Seq[(Int, Int)] = Seq()
  var spawnPoint: (Int, Int) = _
  var base: (Int, Int) = _
  var enemies: Seq[Enemy] = Seq()
  var path: Seq[(Int, Int)] = Seq()
  
  def loader(input: File) = {
    gameReader.loadGame(new FileReader(input))
  }
  
  def addGrass(position: (Int, Int)) = {
    grass = grass :+ position
  }
  def addRoad(position: (Int, Int)) = {
    road = road :+ position
  }
  
  def addSpawnPoint(position: (Int, Int)) = {
    spawnPoint = position
  }
  
  def addBase(position: (Int, Int)) = {
    base = position
  }
  
  def addEnemy(enemyType: String, howMany: Int) = {
    for (_ <- 0 until howMany)
      enemyType match {
        case "basicEnemy" => enemies = enemies :+ new basicEnemy 
      }
  }
  
  def roadCalculator = {
    var grid = road
    path = path :+ road.minBy(distanceBetweenPoints(_, spawnPoint))
    grid = grid.filterNot(_ == path.last)
      while (path.size != road.size) {
        path = path :+ grid.minBy(distanceBetweenPoints(_, path.last))
        grid = grid.filter(_ != path.last)
      }
  }
}