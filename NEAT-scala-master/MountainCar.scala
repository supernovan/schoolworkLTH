package Neat

import scala.collection.mutable.{ ArrayBuffer => ArrayBuffer }
import Neat.Cga
import Neat.Utility
import Neat.XoR
import Neat.Params
import Neat.NeuralNet
import java.io._
import java.net.Socket



object MountainCar {
  
  def main(args: Array[String]) {
    var species = ArrayBuffer.empty[NeuralNet]
    var fitnesses = ArrayBuffer.fill(100)(-1.0)
    var pop = new Cga(Params.popSize, Params.nbrInputs, Params.nbrOutputs)
    
    var s = new Socket("localhost", 9000)
    var is: DataInputStream = new DataInputStream(new BufferedInputStream(s.getInputStream()))
    var os: DataOutputStream = new DataOutputStream( new BufferedOutputStream(s.getOutputStream()))
    s.setTcpNoDelay(true)
    val games = 50
    var nbrSpecies = Params.popSize
    var flag = true
    var wins = 0
    
    println("from which gen?")
    val nbr = scala.io.StdIn.readInt()
    
    println("wanna read from a textfile? 1 for yes, 0 for no")
    val inp = scala.io.StdIn.readInt()
    if (inp == 1) {
      
      for (i <- 0 until nbrSpecies) {
        val net = Utility.readNeuralNet("BestSpcGen" + inp.toString())
        species += net
      }
    } else {
      species = pop.createPhenotypes()
    }
    
    
    resetFitness(fitnesses, nbrSpecies)
    
    var maxF = -1.0
    var bestSpc: NeuralNet = null
    
    
    
    var lowestItr = 2000
    var nbrWins = 0
    
    val meanSum = ArrayBuffer.empty[Double]
    
    if (inp == 1) {
      species = pop.epoch(fitnesses)
    }
    for (tt <- 0 until 1) {
      for (game <- 0 until 15) {
        
        for (spc <- 0 until nbrSpecies) {
          val time1 = System.currentTimeMillis()
          flag = true
          for (itr <- 0 until 2000 if flag) {
  //          print("Para har värdet: ")
           
            val para = is.readLine()
            
  //          println(para)
            if (para.toInt == 3) {
//              println("win")
              nbrWins += 1
              wins += 1
              flag = false
              fitnesses(spc) = 0.5 + 0.01*(2000-itr)
              if (lowestItr > itr) {
                lowestItr = itr
              }
//              Utility.saveNeuralNet(species(spc), true, 33)
            } else if (para.toInt == 4) {
              
              var temp1: String = is.readLine()
               
  //            println("pos is: " + pos)
  //            val time1 = System.currentTimeMillis()
              var temp2: String = is.readLine()
  //            println(System.currentTimeMillis() - time1)
              val pos = temp1.toDouble
              val speed = temp2.toDouble
              
  //            println("speed is: " + speed)
              val inputs = ArrayBuffer(pos, speed)
              
              var currVal = species(spc).update(inputs, Utility.ACTIVE).sum
  //            println(System.currentTimeMillis() - time1)
              if (pos > fitnesses(spc)) {
                fitnesses(spc) = pos
              }
              if (math.round(currVal) < 0.5) {
                os.writeBytes(0.toString() + "\n")
              } else if (Math.round(currVal) >= 1.5) {
                os.writeBytes(2.toString()+ "\n")
              } else {
                os.writeBytes(1.toString()+ "\n")
              }
              os.flush();
             
            }
          }
  //        println(System.currentTimeMillis() - time1)
  //        val check = is.readLine()
  //        if (check.toInt == 5) {
  //          //          println("no Error")
  //        } else {
  //          println("issues")
  //        }
        }
        println("gen " + game + ": " + fitnesses.toList.max)
        
        if (fitnesses.toList.max > maxF) {
          maxF = fitnesses.toList.max
          bestSpc = species(fitnesses.indexOf(fitnesses.toList.max))
        }
        species = pop.epoch(fitnesses)
        println(fitnesses.sum/fitnesses.length.toDouble)
        meanSum += fitnesses.sum/fitnesses.length.toDouble
        resetFitness(fitnesses, nbrSpecies)
        pop = new Cga(Params.popSize, Params.nbrInputs, Params.nbrOutputs)
      }
//      Utility.saveNeuralNet(bestSpc, true, nbr+5)
    }
    println("lowestItr: " + lowestItr)
    println("nbrWins: " + + nbrWins)
    Utility.saveBuffer(meanSum)
  }
  
  def resetFitness(vecFitness: ArrayBuffer[Double], nbrSpecies: Int) {
    for (i <- 0 until nbrSpecies) {
      vecFitness(i) = -1.0
    }
  }
}