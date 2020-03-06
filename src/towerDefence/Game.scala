package towerDefence

import towerDefence._
import constants._

class Game {
  
  var towers: Array[Tower] = Array()
  var spawnedEnemies: Seq[Enemy] = Seq()
  var enemies: Seq[Enemy] = Seq()
  var counter = 0
  
  def isLost: Boolean = false
  def isWon: Boolean = false
  
  def spawnEnemies = {
    if (counter%enemyDelay == 0) {
      spawnedEnemies = spawnedEnemies :+ enemies(0)
      enemies = enemies.tail
      counter += 1
    } else {
      counter += 1
    }
  }
  
  def timePasses() = {
    spawnedEnemies.map(_.move)
    spawnedEnemies.map(_.travelDistance += 1)
    if (!enemies.isEmpty) {
      spawnEnemies
    }
    
    
  }
  
}