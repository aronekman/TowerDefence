package towerDefence.GUI

import java.io.IOException
import java.io.BufferedReader
import java.io.Reader
import towerDefence._
import towerDefence.GUI._

object gameReader {
  
  def loadGame(input: Reader, game: Game) = {
    
    val lineReader = new BufferedReader(input)
    
    try {
      
      var currentLine = lineReader.readLine().trim()
      if (!currentLine.startsWith("#Map")) {
        throw new CorruptedTowerDefenceFileException("Unknown file type");
      }
      
      
      var x = 0
      for (y <- 0 until 17) {
        currentLine = lineReader.readLine().trim()
        for (square <- currentLine) {
          square match {
            case '.' => 
            case 'g' => game.addGrass(positionCalculator(x, y))
            case '-' => game.addRoad(positionCalculator(x, y))
            case '+' => game.addTowerSquare(positionCalculator(x, y))
            case 's' => game.addSpawnPoint(positionCalculator(x, y))
            case 'b' => game.addBase(positionCalculator(x, y))
            case _ =>
          }
          x += 1
        }
        x = 0
      }
      
      while ({currentLine = lineReader.readLine().trim().toLowerCase(); !currentLine.startsWith("#waves")}) {
      }
      
      var wave: Seq[Enemy] = Seq()
      var times: Int = 0
      while ({currentLine = lineReader.readLine().trim().toLowerCase(); !currentLine.startsWith("#end")}) {
        if (currentLine.startsWith("#")) {
          if (!wave.isEmpty) {
            game.addEnemy(wave)
            wave = Seq()
          }
        } else {
          times = currentLine.filter(_.isDigit).toInt
          currentLine.filter(_.isLetter).trim() match {
            case "basicenemy" => {
              wave = wave ++ Seq.fill(times)(new BasicEnemy(game))
            }
          }
        }
      }
      if (!wave.isEmpty) {
        game.addEnemy(wave)
      }
      
    } catch {
      case e: IOException => throw new CorruptedTowerDefenceFileException("Corrupted TowerDefence File")
    }
  }
  
  def positionCalculator(x: Int, y: Int): (Int, Int) = ((x*constants.squareWidth)-constants.squareWidth, (y*constants.squareHeight)-constants.squareHeight)
}

class CorruptedTowerDefenceFileException(message: String) extends Exception(message) 
