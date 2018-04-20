package Neat

import scala.collection.mutable.{ ArrayBuffer => ArrayBuffer }
import Neat.Cga
import Neat.Utility
import Neat.XoR
import Neat.Params
import Neat.NeuralNet
import java.io._
import java.net.Socket
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream



object render {
  
  def main(args: Array[String]) {
    var species = ArrayBuffer.empty[NeuralNet]
    var fitnesses = ArrayBuffer.fill(100)(-1.0)
    var pop = new Cga(Params.popSize, Params.nbrInputs, Params.nbrOutputs)
    
    var s = new Socket("192.168.1.186", 9000)
    var is: DataInputStream = new DataInputStream(new BufferedInputStream(s.getInputStream()))
    var os: DataOutputStream = new DataOutputStream( new BufferedOutputStream(s.getOutputStream()))
    s.setTcpNoDelay(true)
    val games = 50
    var nbrSpecies = Params.popSize
    var flag = true
    var wins = 0
    
    println("wanna read from a textfile?2 for super mode, 1 for yes, 0 for no")
    val inp = scala.io.StdIn.readInt()
    if (inp == 1) {
      println("from which gen?")
      val inp = scala.io.StdIn.readInt()
      for (i <- 0 until nbrSpecies) {
        val net = Utility.readNeuralNet("BestSpcGen" + inp.toString())
        species += net
        
      }
      Utility.saveNeuralNet(species(0), true, 43)
    } else if (inp == 2) {
      val inp = scala.io.StdIn.readInt()
      for (i <- 0 until 3) {
        val inp = scala.io.StdIn.readInt()
        val net = Utility.readNeuralNet("BestSpcGen" + inp.toString())
        species += net
        
      }
    } else {
    
      species = pop.createPhenotypes()
    }
    
    
    resetFitness(fitnesses, nbrSpecies)
    
    var maxF = -1.0
    var bestSpc: NeuralNet = null
    
//    if (inp == 1) {
//      species = pop.epoch(fitnesses)
//    }
    
    for (game <- 0 until 1 if flag) {
      
      for (spc <- 0 until 3 if flag) {
        val time1 = System.currentTimeMillis()
        for (itr <- 0 until 2000 if flag) {
//          print("Para har värdet: ")
         
          val para = is.readLine()
          
//          println(para)
          if (para.toInt == 3) {
            println("win")
            wins += 1
            flag = false
            Utility.saveNeuralNet(species(spc), true, 33)
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
              println(pos)
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
        flag = true
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
      resetFitness(fitnesses, nbrSpecies)
      pop = new Cga(Params.popSize, Params.nbrInputs, Params.nbrOutputs)
    }
//    Utility.saveNeuralNet(bestSpc, true, 0)
  }
  
  def resetFitness(vecFitness: ArrayBuffer[Double], nbrSpecies: Int) {
    for (i <- 0 until nbrSpecies) {
      vecFitness(i) = -1.0
    }
  }
  
  
  
}