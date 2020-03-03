package Game
import o1._

abstract class Tower {
  var DMG: Int
  var cost: Int
  var range: Int
  
}

class Welho extends Tower {
  var DMG = 100
  var cost = 100
  var range = 80

}

class archer extends Tower {
  var DMG = 50
  var cost = 50
  var range = 100
}