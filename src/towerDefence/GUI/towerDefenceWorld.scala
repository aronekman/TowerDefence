package towerDefence.GUI

import java.io._
import towerDefence.GUI._
import towerDefence._
import scala.swing._

object towerDefenceWorld {

  var grass: Seq[(Int, Int)] = Seq()
  var road: Seq[(Int, Int)] = Seq()
  var spawnPoint: (Int, Int) = _
  var base: (Int, Int) = _
  var wayPoints: Seq[(Int, Int)] = Seq()
  var enemies: Seq[Enemy] = Seq()
  
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
  
  def addWayPoint(position: (Int, Int)) = {
    wayPoints = wayPoints :+ position
  }
  
  def addEnemy(enemyType: String, howMany: Int) = {
    for (_ <- 0 until howMany)
      enemyType match {
        case "basicEnemy" => enemies = enemies :+ new basicEnemy 
      }
  }
  
}