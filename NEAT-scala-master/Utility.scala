package Neat

import scala.util.Random
import java.io.BufferedWriter
import java.io.BufferedReader
import java.io.FileWriter
import java.io.FileReader
import java.io.IOException
import java.io.File

import scala.collection.mutable.{ ArrayBuffer => ArrayBuffer }

object Utility {

  val BIAS = 1
  val INPUT = 2
  val OUTPUT = 3
  val HIDDEN = 4
  val NONE = 5

  val NEW_NEURON = 11
  val NEW_LINK = 12
  val NONE_NEURON = 13

  //For the neuralnet class
  val SNAPSHOT = 21
  val ACTIVE = 22

  def sigmoid(netInput: Float, resp: Float): Float = {
    (1.0 / (1.0 + 1 * Math.exp((-netInput / 4.924273)))).toFloat
  }

  def randInt(min: Int, max: Int): Int = {
    Random.nextInt(max - min + 1) + min
  }

  def randomClamped(): Double = {
    val test = Random.nextFloat() - Random.nextFloat()
    test
  }
  
  
  def saveBuffer(list: ArrayBuffer[Double]) {
    val fileName = "meanSum2"
    val fw = new FileWriter(fileName + ".txt", false)

    val bw = new BufferedWriter(fw)
    bw.write("list: ")
    for (i <- 0 until list.length) {
      bw.write(list(i).toString + " ")
    }
    bw.flush()
    bw.close()
  }
  
  def saveNeuralNet(net: NeuralNet, overW: Boolean, gen: Int = 10) = {
    var nbrFile = gen
    val fileName = "BestSpcGen"

    val links = ArrayBuffer.empty[Link]
    val neurons = ArrayBuffer.empty[Neuron]
    for (i <- 0 until net.neurons.length) {

      if (!neurons.contains(net.neurons(i))) {
        neurons += net.neurons(i)
      }

      for (j <- 0 until net.neurons(i).vecLinksIn.length) {
        if (!links.contains(net.neurons(i).vecLinksIn(j))) {
          links += net.neurons(i).vecLinksIn(j)
        }
      }

      for (j <- 0 until net.neurons(i).vecLinksOut.length) {
        if (!links.contains(net.neurons(i).vecLinksOut(j))) {
          links += net.neurons(i).vecLinksOut(j)
        }
      }

    }
    var test = new File("BestSpcGen" + nbrFile.toString() + ".txt")
//    while (test.exists() && overW == false) {
//      nbrFile += 10
//      test = new File("BestSpcGen" + nbrFile.toString() + ".txt")
//    }
    if (overW) {
      
    }

    val fw = new FileWriter("BestSpcGen" + nbrFile.toString() + ".txt", false)

    val bw = new BufferedWriter(fw)

    bw.write("neurons:\n")
    for (i <- 0 until net.neurons.length) {
      bw.write("neuron: " + i + " ")
      bw.write("vecLinksIn: ")
      for (link <- neurons(i).vecLinksIn) bw.write(links.indexOf(link).toString() + " ")
      bw.write("vecLinksOut: ")
      for (link <- neurons(i).vecLinksOut) bw.write(links.indexOf(link).toString() + " ")
      bw.write("nValues " + net.neurons(i).neuronType + " ")
      bw.write(net.neurons(i).id + " ")
      bw.write(net.neurons(i).splitY + " ")
      bw.write(net.neurons(i).splitX + " ")
      bw.write(net.neurons(i).actResp + " ")
      bw.write(net.neurons(i).sumActivation + " ")
      bw.write(net.neurons(i).output + " ")
      bw.write(net.neurons(i).xPos + " ")
      bw.write(net.neurons(i).yPos + "\n")
    }

    bw.write("depth: " + net.depth + "\n")

    bw.write("listLinks:\n")
    var ind = 0
    for (link <- links) {
      bw.write("link " + ind + " ")
      bw.write(link.weight + " ")
      bw.write(neurons.indexOf(link.neuronIn) + " ")
      bw.write(neurons.indexOf(link.neuronOut) + " ")
      bw.write(link.reccurent + "\n")
      ind += 1
    }
    bw.flush()
    bw.close()

  }

  def readNeuralNet(name: String): NeuralNet = {
    val fr = new FileReader(name + ".txt")
    val br = new BufferedReader(fr)
    val links = ArrayBuffer.empty[Link]
    val neurons = ArrayBuffer.empty[Neuron]
    val indexLinksIn = ArrayBuffer.empty[ArrayBuffer[Int]]
    val indexLinksOut = ArrayBuffer.empty[ArrayBuffer[Int]]
    var line = ""
    val net = new NeuralNet(ArrayBuffer.empty[Neuron], 0)
    var check = true
    while ((line = br.readLine()) != null && check) {
      if (line == null) {
        check = false
      } else {
        val ls = line.split(" ")
        val temp = ls(0)
        println(temp)
        temp match {
          case "neuron:" => {
            indexLinksIn += ArrayBuffer.empty[Int]
            indexLinksOut += ArrayBuffer.empty[Int]
            for (i <- 3 until ls.indexOf("vecLinksOut:")) {
              indexLinksIn(ls(1).toInt) += ls(i).toInt
            }
            for (i <- ls.indexOf("vecLinksOut:") + 1 until ls.indexOf("nValues")) {
              println(ls(1).toString())
              println(indexLinksOut.length)
              indexLinksOut(ls(1).toInt) += ls(i).toInt
            }
            val tv = ls.indexOf("nValues") + 1
            val n = new Neuron(ls(tv).toInt, ls(tv + 1).toInt, ls(tv + 2).toDouble, ls(tv + 3).toDouble, ls(tv + 4).toDouble)
            n.sumActivation = ls(tv + 5).toDouble
            n.output = ls(tv + 6).toDouble
            n.xPos = ls(tv + 7).toInt
            n.yPos = ls(tv + 8).toInt
            neurons += n
          }
          case "link"   => links += new Link(ls(2).toDouble, neurons(ls(3).toInt), neurons(ls(4).toInt), ls(5).toBoolean)

          case "depth:" => net.depth = ls(1).toInt
          case _ => println("banan")
        }
      }
    }

    for (i <- 0 until indexLinksIn.length) {
      for (j <- 0 until indexLinksIn(i).length) {
        neurons(i).vecLinksIn += links(indexLinksIn(i)(j))
      }
    }

    for (i <- 0 until indexLinksOut.length) {
      for (j <- 0 until indexLinksOut(i).length) {
        neurons(i).vecLinksOut += links(indexLinksOut(i)(j))
      }
    }
    net.neurons = neurons
    net
  }

}