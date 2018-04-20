package Neat

import scala.collection.mutable.{ArrayBuffer => ArrayBuffer}
import Neat.Utility
import Neat.Neuron


class NeuralNet(var neurons: ArrayBuffer[Neuron], var depth: Int) {
  
  
  
  def update(inputs: ArrayBuffer[Double], runType: Int): ArrayBuffer[Double] =  {
    var outputs = ArrayBuffer.empty[Double]
    
    var flushCount = 0
    
    if (runType == Utility.SNAPSHOT) {
      flushCount = depth // depth usually
//      println(depth)
    } else {
      flushCount = 1
    }
    
    for (i <- 0 until flushCount) {
      outputs.clear()
      
      var indexNeuron = 0
      
      while (neurons(indexNeuron).neuronType == Utility.INPUT) {
        neurons(indexNeuron).output = inputs(indexNeuron)
        indexNeuron += 1
      }
      
      neurons(indexNeuron).output = 1
      indexNeuron += 1
      
      while (indexNeuron < neurons.length) {
        var sum: Double = 0
        
        for (j <- 0 until neurons(indexNeuron).vecLinksIn.length) {
          val weight: Double = neurons(indexNeuron).vecLinksIn(j).weight
          
          val neuronOutput: Double = neurons(indexNeuron).vecLinksIn(j).neuronIn.output
          
          sum += weight*neuronOutput
        }
        if (neurons(indexNeuron).actResp == 0) {
          neurons(indexNeuron).output = if (sum >= 0) 1 else 0;
        } else {
          neurons(indexNeuron).output = Utility.sigmoid(sum.toFloat, neurons(indexNeuron).actResp.toFloat)
        }
        if (neurons(indexNeuron).neuronType == Utility.OUTPUT) {
          outputs += neurons(indexNeuron).output
        }
        
        indexNeuron += 1
      }
    }
    
    if (true) {
      if (runType == Utility.SNAPSHOT) {
        for (n <- 0 until neurons.length) {
          neurons(n).output = 0
        }
      }
    }
    
    outputs
  }
}