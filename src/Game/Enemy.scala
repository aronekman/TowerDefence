package Game

abstract class Enemy {
  var maxHP: Int
  var HP: Int = maxHP
  var reward: Int
  var position: (Int, Int)
  
  def isAlive: Boolean = HP > 0
  
  def takeDamage(DMG: Int) = {
    this.HP -= DMG
  }
}