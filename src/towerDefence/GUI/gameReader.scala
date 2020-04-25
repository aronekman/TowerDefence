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
      
      //lisää peliin tason kaikki komponentit
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
      
      // muuttaa pelaajan alku HP:ta ja rahaa
      while ({currentLine = lineReader.readLine().trim().toLowerCase(); !currentLine.startsWith("#waves")}) {
        if (currentLine.contains("hp")) {
          var hp = currentLine.filter(_.isDigit).toInt
          game.HP = hp
        } else if (currentLine.contains("money")) {
          var money = currentLine.filter(_.isDigit).toInt
          game.money = money
        }
      }
      
      // lisää peliin viholliset aalloissa
      var wave: Seq[Enemy] = Seq()
      var times: Int = 0
      while ({currentLine = lineReader.readLine().trim().toLowerCase(); !currentLine.startsWith("#end")}) {
        if (currentLine.startsWith("#")) {
          if (!wave.isEmpty) {
            game.addEnemy(wave)
            wave = Seq()
          }
        } else if (currentLine != "") {
          times = currentLine.dropWhile(_ != ' ').tail.toInt
          currentLine.takeWhile(_ != ' ').trim() match {
            case "enemy1" => {
              wave = wave ++ Seq.fill(times)(new Enemy1(game))
            }
            case "enemy2" => {
              wave = wave ++ Seq.fill(times)(new Enemy2(game))
            }
            case "enemy3" => {
              wave = wave ++ Seq.fill(times)(new Enemy3(game))
            }
            case "enemy4" => {
              wave = wave ++ Seq.fill(times)(new Enemy4(game))
            }
            case _ =>
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
