package towerDefence

abstract class Enemy {
  var maxHP: Int
  var HP: Int = maxHP
  var reward: Int
  var posX: Int
  var posY: Int
  var travelDistance: Int = 0
  
  def isAlive: Boolean = HP > 0
  
  def takeDamage(DMG: Int) = {
    this.HP -= DMG
  }
}