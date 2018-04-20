package Neat

import scala.collection.mutable.{ArrayBuffer => ArrayBuffer}
import Neat.InnovationS
import Neat.LinkGene
import Neat.Utility
import Neat.NeuronGene

class InnovationC(startGenes: ArrayBuffer[LinkGene], startNeurons: ArrayBuffer[NeuronGene]) {
	val vecInnovs: ArrayBuffer[InnovationS] = ArrayBuffer.empty[InnovationS]
	var nextInnovNbr = 0
	var nextNeuronID = 0


  for (i <- 0 until startNeurons.length) {
  	vecInnovs += new InnovationS(startNeurons(i), nextInnovNbr, nextNeuronID)
  	nextNeuronID +=1
  	nextInnovNbr +=1
  }

  for (i <- 0 until startGenes.length) {
    vecInnovs += new InnovationS(startGenes(i).fromNeuron, startGenes(i).toNeuron, Utility.NEW_LINK, nextInnovNbr)
  	nextInnovNbr +=1
  }

  def checkInnovation(in: Int, out: Int, innovType: Int): Int = {
  	var innovID = -1
  	var flag = true
  	for (i <- 0 until vecInnovs.length if flag) {
  	  
  		if (vecInnovs(i).neuronIn == in && 
  		    vecInnovs(i).neuronOut == out && 
  		    vecInnovs(i).innovType == innovType) {
  			innovID = vecInnovs(i).innovID
  			flag = false
  		}
  	}
  	innovID
  }

  def createNewInnovation(in: Int, out: Int, innovType: Int): Int = {
  	val nInnov = new InnovationS(in, out, innovType, nextInnovNbr)

  	if (innovType == Utility.NEW_NEURON) {
  		nInnov.neuronID = nextNeuronID
  		nextNeuronID += 1
  	}
  	vecInnovs += nInnov
  	nextInnovNbr += 1
  	nextInnovNbr-1
  }

  def createNewInnovation(from: Int, to: Int, innovType: Int, neuronType: Int, x: Double, y: Double): Int = {
  	val nInnov = new InnovationS(from, to, innovType, nextInnovNbr, neuronType, x, y)

  	if (innovType == Utility.NEW_NEURON) {
  		nInnov.neuronID = nextNeuronID
  		nextNeuronID += 1
  	}
  	vecInnovs += nInnov
  	nextInnovNbr += 1
  	nextNeuronID-1
  }

  def createNeuronFromID(nID: Int): NeuronGene = {
  	val temp = new NeuronGene(Utility.HIDDEN, 0, 0, 0)

  	var check = true
  	for (i <- 0 until vecInnovs.length if check) {
  	  
  		if (vecInnovs(i).neuronID == nID) {
  			temp.neuronType = vecInnovs(i).neuronType
  			temp.ID = vecInnovs(i).neuronID
  			temp.splitX = vecInnovs(i).splitX
  			temp.splitY = vecInnovs(i).splitY
  			check = false
  		}
  	}
  	temp
  }

  def getNeuronID(index: Int): Int = {
  	vecInnovs(index).neuronID
  }
  
  def nextNumber(num: Int = 0): Int = {
    nextInnovNbr += num
    nextInnovNbr
  }

  def flush(): Unit = {
  	vecInnovs.clear
  }
}