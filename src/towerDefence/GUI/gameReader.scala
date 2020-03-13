package towerDefence.GUI

import java.io.IOException
import java.io.BufferedReader
import java.io.Reader
import towerDefence.constants._
import towerDefence.GUI._
import o1._

object gameReader {
  
  def loadGame(input: Reader) = {
    
    val lineReader = new BufferedReader(input)
    
    try {
      
      var currentLine = lineReader.readLine().trim()
      if (!currentLine.startsWith("#Map")) {
        throw new CorruptedTowerDefenceFileException("Unknown file type");
      }
      
      
      var x = 0
      for (y <- 0 until 17) {
        currentLine = lineReader.readLine().trim()
        var wayPoints: Seq[(Int, Int)] = Seq()
        for (square <- currentLine) {
          square match {
            case '.' => 
            case '+' => towerDefenceWorld.addGrass(this.positionCalculator(x, y))
            case '-' => towerDefenceWorld.addRoad(this.positionCalculator(x, y))
            case 's' => towerDefenceWorld.addSpawnPoint(this.positionCalculator(x, y))
            case 'b' => towerDefenceWorld.addBase(this.positionCalculator(x, y))
            case _ =>
          }
          x += 1
        }
        x = 0
      }
      
      towerDefenceWorld.addEnemy("basicEnemy", 5)
      
    } catch {
      case e: IOException => throw new CorruptedTowerDefenceFileException("homma kusi")
    }
  }
  
  def positionCalculator(x: Int, y: Int): (Int, Int) = ((x*squareWidth)-squareWidth, (y*squareHeight)-squareHeight)
}