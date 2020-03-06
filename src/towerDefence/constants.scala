package towerDefence

object constants {
  
  val viewWidth = 1000
  val viewHeight = 750
  val squareWidth = 50
  val squareHeight = 50
  val enemySpeed = 5
  val gameSpeed = 5
  val enemyDelay = 20
  val squaresInRow = viewWidth/squareWidth
  val squaresInColumn = viewHeight/squareHeight
  
  
  def distanceBetweenPoints(a: (Int, Int), b: (Int, Int)) = {
    Math.sqrt((a._2 - b._2) * (a._2 - b._2) + (a._1 - b._1) * (a._1 - b._1))
  }
}