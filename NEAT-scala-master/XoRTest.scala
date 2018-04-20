package Neat

import scala.collection.mutable.{ ArrayBuffer => ArrayBuffer }
import Neat.Cga
import Neat.Utility
import Neat.XoR
import Neat.Params

object XoRTest {
  var inputs = ArrayBuffer.empty[ArrayBuffer[Double]]
  var outputs = ArrayBuffer.empty[(Int)]

  var species = ArrayBuffer.empty[XoR]

  def main(args: Array[String]) {

    inputs += (ArrayBuffer(0, 0), ArrayBuffer(1, 0), ArrayBuffer(0, 1), ArrayBuffer(1, 1))
    outputs += (0, 1, 1, 0)

    
    var game = true
    var wins = 0.0
    var times = 50
    var averageIterations = 0.0
    

    val nbrGens = 150
    var counter = 1
    var population: Cga = null
    var test = true
    // 150 generations to solve this problem should be enough
    for (k <- 1 to times) {
      species.clear()
      population = new Cga(Params.popSize, Params.nbrInputs, Params.nbrOutputs)
      var brains = population.createPhenotypes()
      for (i <- 0 until brains.length) {
        species += new XoR(brains(i))
      }
      
      println("Omgång: " + k)
      game = true
      for (i <- 1 to nbrGens if test) {
//        if (i % 15 == 0) {
//          println(counter * 10 + "% done")
//          counter += 1
//        }
        //      println("banan")
        for (spc <- 0 until species.length if game) {
//          println("potatis")
          if (spc == 0) {

          }
          var totError = 0.0
          var checkScore = 0
          var table = ArrayBuffer.empty[Double]
          for (j <- 0 until inputs.length) {
            val temp = species(spc).brain.update(inputs(j), Utility.ACTIVE)
            val tempError = if (temp.max == temp(0)) temp(0) else temp(1)
            totError += Math.abs(outputs(j).toDouble - temp.sum)
            if (Math.round(temp.sum) == outputs(j).toInt) checkScore += 1;
            

            
            table += temp.sum
          }
          
//          println("potatis")
          if (table(0) < 0.5 && table(1) >= 0.5 && table(2) >= 0.5 && table(3) < 0.5 || checkScore == 4) {
            wins += 1
            averageIterations += i
            game = false
            println("tog: " + i + " iterationer")
//            Utility.saveNeuralNet(species(spc).brain)
            test = false
          }

          var score = 0.0
 
          if (i != nbrGens) {
            score = 10 - ( totError) * (totError)*5
          } else {
            score = totError
          }
          species(spc).fitness = score
//          println(species(spc).fitness)

          table.clear()
        }
        
        brains = population.epoch(getFitnessVec)

        for (spc <- 0 until Params.nbrSpecies) {
          species(spc).brain = brains(spc)
        }
        //      println("can we done 1 iteration?")
        //      println("banan2")
//        species.sortBy(_.fitness)
      }
      test = true
    }
    
    println("Antalet vinster :" + wins)
  }
  
  def resetGame(species: ArrayBuffer[XoR]) {
    
    val population = new Cga(Params.popSize, Params.nbrInputs, Params.nbrOutputs)
    var brains = population.createPhenotypes()
    for (i <- 0 until brains.length) {
      species(i).brain = brains(i)
    }
    
  }
  
  def getFitnessVec(): ArrayBuffer[Double] = {
    var scores = ArrayBuffer.empty[Double]
    for (i <- 0 until species.length) {
      scores += species(i).fitness
    }
    scores
  }

  def printScores(): Unit = {
    var scores = ArrayBuffer.empty[Double]
    for (i <- 0 until species.length) {
      var score = 0.0
      for (j <- 0 until inputs.length) {
        val temp = species(i).brain.update(inputs(j), Utility.ACTIVE)
        //        if (Math.round(temp.sum) == outputs(j)) {
        //          score += 1
        //        }
        if (Math.round(temp.sum) <= outputs(j) && outputs(j) == 0 ||
          Math.round(temp.sum) >= outputs(j) && outputs(j) == 1) {
          score += 1
        }
      }

      scores += score
    }
    scores = scores.sorted
    scores.foreach(f => println(f))
  }
}