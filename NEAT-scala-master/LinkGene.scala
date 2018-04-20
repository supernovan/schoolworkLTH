

package Neat

class LinkGene(var fromNeuron: Int, var toNeuron: Int, var enabled: Boolean, var innovationID: Int, var weight: Double, var reccurent: Boolean = false) {
  
	def <(other: LinkGene): Boolean = {
		0 > innovationID.compareTo(other.innovationID)
	} 
}