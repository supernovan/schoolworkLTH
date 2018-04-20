package Neat

import scala.collection.mutable.{ArrayBuffer => ArrayBuffer}
import Neat.Link

class Neuron(var neuronType: Int,var id: Int, y: Double, x: Double, var actResp: Double) {
  var vecLinksIn = ArrayBuffer.empty[Link]
  var vecLinksOut = ArrayBuffer.empty[Link]
  
  var sumActivation: Double = 0
  var output: Double = 0
  
  var xPos: Int = 0
  var yPos: Int = 0
  
  var splitX: Double = x
  var splitY: Double = y
}