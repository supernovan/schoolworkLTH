package Neat

import Neat.Utility

import Neat.NeuronGene

class InnovationS(var neuronIn: Int,var neuronOut: Int, var innovType: Int,var innovID: Int) {
	var neuronID = 0
	var splitX: Double = 0
	var splitY: Double = 0
	var neuronType: Int = Utility.NONE_NEURON

	def this(neuron: NeuronGene, innovID: Int, neuronID: Int) {
		this(-1,-1,-1, innovID)
		this.neuronID = neuronID
		this.neuronType = neuron.neuronType
		this.splitX = neuron.splitX
		this.splitY = neuron.splitY
	}

	def this(in: Int, out: Int, innovType: Int, innovID: Int, neuronType: Int, x: Double, y: Double) {
		this(in, out, innovType, innovID)
		this.splitX = x
		this.splitY = y
		this.neuronType = neuronType
	}
}