

package Neat

class NeuronGene (nType: Int, id: Int, x: Double, y: Double, rec: Boolean = false, act: Double = 1) {
  var reccurent = rec
  var neuronType = nType
  var ID = id
  var splitX = x
  var splitY = y
  var actResp = act
}