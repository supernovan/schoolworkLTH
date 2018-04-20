package Neat

import scala.util.Random
import scala.collection.mutable.{ ArrayBuffer => ArrayBuffer}
import Neat.InnovationC
import Neat.NeuralNet
import Neat.LinkGene
import Neat.Link
import Neat.Utility
import Neat.NeuronGene
import Neat.Params
import Neat.Neuron

class Genome(var ID: Int, var neurons: ArrayBuffer[NeuronGene], var links: ArrayBuffer[LinkGene],
             var nbrInputs: Int, var nbrOutputs: Int) {
   
  def this(ID: Int, nbrInputs: Int, nbrOutputs: Int) = {
    this(ID, ArrayBuffer.empty[NeuronGene], ArrayBuffer.empty[LinkGene], nbrInputs, nbrOutputs)

    val inputRowSlice = 0.8 / nbrInputs.toDouble

    for (i <- 0 until nbrInputs) {
      neurons += new NeuronGene(Utility.INPUT, i, 0, 0.1 + i * inputRowSlice)
    }

    neurons += new NeuronGene(Utility.BIAS, nbrInputs, 0, 0.9)

    val outputRowSlice = 1.0 / (nbrOutputs + 1).toDouble

    for (i <- 0 until nbrOutputs) {
      neurons += new NeuronGene(Utility.OUTPUT, i + nbrInputs + 1, 1, (i + 1) * outputRowSlice)
    }

    for (i <- 0 until nbrInputs + 1) {
      for (j <- 0 until nbrOutputs) {
        links += new LinkGene(neurons(i).ID, neurons(nbrInputs + j + 1).ID, true,
          nbrInputs + nbrOutputs + 1 + links.length, Utility.randomClamped())
      }
    }
    //print(neurons.length)
  }

  var fitness: Double = 0
  var phenotype: NeuralNet = _
  var adjustFitness: Double = 0
  var species: Int = 0

  var amountToSpawn: Double = 0.0
  var nbrSpecies = 0

  var netDepth = 0
  
  def initializeWeights(): Unit = {
    for (i <- 0 until links.length) {
      links(i).weight = Utility.randomClamped()
    }
  }
  
  def createPhenotype(): NeuralNet = {
    deletePhenotype

    var vecNeurons = ArrayBuffer.empty[Neuron]

    for (i <- 0 until neurons.length) {
      val neuron = new Neuron(neurons(i).neuronType, neurons(i).ID, neurons(i).splitY, neurons(i).splitX, neurons(i).actResp)
      vecNeurons += neuron
    }

    for (gene <- 0 until links.length) {
      if (links(gene).enabled) {
        var element = getElementPos(links(gene).fromNeuron)
        val fromNeuron = vecNeurons(element)

        element = getElementPos(links(gene).toNeuron)
        val toNeuron = vecNeurons(element)

        val tempLink = new Link(links(gene).weight, fromNeuron, toNeuron, links(gene).reccurent)

        fromNeuron.vecLinksOut += tempLink
        toNeuron.vecLinksIn += tempLink
      }
    }

    phenotype = new NeuralNet(vecNeurons, netDepth)

    phenotype
  }
  
  def deletePhenotype(): Unit = {
    phenotype = null
  }
   
   def getElementPos(neuronID: Int): Int = {
    var check = true
    var index = -1
    for (i <- 0 until neurons.length if check) {
      val d = neurons(i).ID
      if (neurons(i).ID == neuronID) {
        index = i
        check = false
      }
    }

    if (index == -1) {
      print("Can't find: " + neuronID)
    }
    index

  }
   
  def duplicateLink(in: Int, out: Int): Boolean = {
    var check = false
    for (i <- 0 until links.length if !check) {
      if (links(i).fromNeuron == in && links(i).toNeuron == out) {
        check = true
      }
    }
    check
  }
  
  def addLink(mutationRate: Double, reccurentRate: Double, innov: InnovationC, nbrTryLoop: Int, nbrTryAddLink: Int): Unit = {
    if (!(Random.nextFloat() > mutationRate)) {

      // two neurons to be linked together
      var ID_n1 = -1
      var ID_n2 = -1

      // if it's reccurent or not
      var rec = false

      // if YES try nbrTryLoop times to find a neuron

      if (Random.nextFloat() < Params.chanceAddReccurentLink) {
        var i = nbrTryLoop

        while (i > 0) {
          //          print(neurons.length.toString() + nbrInputs)
          val posNeuron = Utility.randInt(nbrInputs + 1, neurons.length - 1)

          //Check that the neuron doesn't already have a reccurent or
          // is a bias/input neuron
          if (!neurons(posNeuron).reccurent &&
              neurons(posNeuron).neuronType != Utility.BIAS &&
              (neurons(posNeuron).neuronType != Utility.INPUT)) {
            ID_n1 = neurons(posNeuron).ID
            ID_n2 = neurons(posNeuron).ID
            neurons(posNeuron).reccurent = true

            rec = true
            i = 1
          }
          i -= 1

        }
      } else {
        var j = nbrTryAddLink
        while (j > 0) {
          if (neurons.length - 1 < 0) {
            //          println(neurons.length)
          }
          ID_n1 = neurons(Utility.randInt(0, neurons.length - 1)).ID
          var posNeuron = 0
//          if (neurons.length - 1 < nbrInputs + 1) {
//            println((neurons.length).toString() + (nbrInputs) + "banan")
//            posNeuron = Utility.randInt(nbrInputs, neurons.length - 1)
//          } else {
//            
//          }
          posNeuron = Utility.randInt(nbrInputs + 1, neurons.length - 1)
          ID_n2 = neurons(posNeuron).ID
//          print(ID_n1 + "choklad ")
//          println(ID_n2 + "banan")
          // Make sure they are not connected to the same neuron
          if (ID_n2 != 2) {
//            j -= 1
          } else {
            if ((duplicateLink(ID_n1, ID_n2) ||
              (ID_n1 == ID_n2) )) { //kanske behöver fixa
              ID_n1 = -1
              ID_n2 = -1
            } else {
              j = 0
            }
          }
          j -= 1
        }
      }

      if (ID_n1 < 0 || ID_n2 < 0) {
        
        //println("more issues")
      } else {
        // is this innovation already created
        val ID = innov.checkInnovation(ID_n1, ID_n2, Utility.NEW_LINK)

        // is the link reccurent?
//        println(ID_n1.toString + " & " + ID_n2)
        if (neurons(getElementPos(ID_n1)).splitY > neurons(getElementPos(ID_n2)).splitY) {
          rec = true
          
        }

        if (ID < 0) {
          innov.createNewInnovation(ID_n1, ID_n2, Utility.NEW_LINK)

          val ID = innov.nextNumber() - 1;

          val link = new LinkGene(ID_n1, ID_n2, true, ID, Utility.randomClamped(), rec)

          links += link
        } else {
          val link = new LinkGene(ID_n1, ID_n2, true, ID, Utility.randomClamped(), rec)
          links += link
        }
      }
    }
  }
  
  def addNeuron(mutationRate: Double, innov: InnovationC, nbrTryOldLink: Int): Unit = {
    if (neurons.length < Params.maxPermittedNeurons) {
      

      // if valID link is found, insert new neuron
      var valIDLink = false

      var indexLink = 0

      // first we select a link, if the genome is  small we make sure
      // one old link link is split to avoID a chaining effect.
      //if it contains less than 5 hIDden neurons it's too small
      val threshhold = nbrInputs + nbrOutputs + 15

      if (links.length < threshhold) {
        var i = nbrTryOldLink
        while (i > 0) {

          val high: Int = nbrGenes - 1 - (Math.sqrt(nbrGenes)).toInt
//          println(high)
          if (high <= 0) {
//            println(nbrGenes)
          }

          indexLink = Utility.randInt(0, high)

          //Make sure that the link is enabled and not reccurent/bias

          val fromNeuron = links(indexLink).fromNeuron

          if (links(indexLink).enabled &&
            !links(indexLink).reccurent &&
            neurons(getElementPos(fromNeuron)).neuronType != Utility.BIAS) {

            valIDLink = true
            i = 1

          }
          i -= 1
        }
      } else {
        //Check for the chaining effect
        while (!valIDLink) {

          indexLink = Utility.randInt(0, links.length - 1)

          val fromNeuron = links(indexLink).fromNeuron
          if (links(indexLink).enabled &&
            !links(indexLink).reccurent &&
            neurons(getElementPos(fromNeuron)).neuronType != Utility.BIAS) {

            valIDLink = true
          }
        }
      }

      //disable this gene
      if (valIDLink) {
        links(indexLink).enabled = false

        // grab the weight from this gene
        val weight = links(indexLink).weight

        val from = links(indexLink).fromNeuron
        val to = links(indexLink).toNeuron

        val newDepth = ((neurons(getElementPos(from)).splitY +
          neurons(getElementPos(to)).splitY) / 2)
        val newWIDth = ((neurons(getElementPos(from)).splitX +
          neurons(getElementPos(to)).splitX) / 2);

        var ID = innov.checkInnovation(from, to, Utility.NEW_NEURON)

        if (ID >= 0) {
          val neuronID = innov.getNeuronID(ID)

          if (alreadyHaveThisNeuronID(neuronID)) {
            ID = -1
          }
        }
        if (ID < 0) {
          val newNeuronID = innov.createNewInnovation(from, to, Utility.NEW_NEURON, Utility.HIDDEN, newWIDth, newDepth)
          neurons += (new NeuronGene(Utility.HIDDEN, newNeuronID, newDepth, newWIDth))

          val IDLink1 = innov.nextNumber()
//          print(IDLink1 + "banan ")
          innov.createNewInnovation(from, newNeuronID, Utility.NEW_LINK)

          val link1 = new LinkGene(from, newNeuronID, true, IDLink1, 1.0)
          links += link1

          val IDLink2 = innov.nextNumber()
//          print(IDLink2 + "\n")
          innov.createNewInnovation(newNeuronID, to, Utility.NEW_LINK)

          val link2 = new LinkGene(newNeuronID, to, true, IDLink2, weight)

          links += link2
        } else {
          val newNeuronID = innov.getNeuronID(ID)

          val IDLink1 = innov.checkInnovation(from, newNeuronID, Utility.NEW_LINK)
          val IDLink2 = innov.checkInnovation(newNeuronID, to, Utility.NEW_LINK)

          if (IDLink1 < 0 || IDLink2 < 0) {
            print("error, why is this happning?")

          } else {

            val link1 = new LinkGene(from, newNeuronID, true, IDLink1, 1.0)
            val link2 = new LinkGene(newNeuronID, to, true, IDLink2, weight)

            links += link1
            links += link2

            val newNeuron = new NeuronGene(Utility.HIDDEN, newNeuronID, newDepth, newWIDth)

            neurons += newNeuron
          }
        }
      }
    }
  }
  
  def alreadyHaveThisNeuronID(ID: Int): Boolean = {
    var check = false
    for (i <- 0 until neurons.length if !check) {
      if (ID == neurons(i).ID) {
        check = true
      }
    }
    check
  }

  def mutateWeights(mutRate: Double, newMutRate: Double, maxPertubation: Double): Unit = {
    for (gen <- 0 until links.length) {
      if (Random.nextFloat() < mutRate) {
        if (Random.nextFloat() < newMutRate) {
          links(gen).weight = Utility.randomClamped()
        } else {
          links(gen).weight += Utility.randomClamped() * maxPertubation
        }
      }
    }
  }
  
  def mutateWeights(): Unit = {
    val ordiMutProb: Double = 0.7
    val meanWPert: Double = 0.5
    val meanWRep: Double = 0.5
    val mutPow: Double = 2.5
    val endPart = links.length * 0.8
    for (i <- 0 until links.length) {
      if (Random.nextFloat() < ordiMutProb) {
        if (Random.nextFloat() < meanWPert - 0.1) {
          links(i).weight += Utility.randomClamped() * mutPow
        } else if (Random.nextFloat() < meanWRep - 0.1) {
          links(i).weight = Utility.randomClamped()
        }
      } else if (links.length >= 10 && i > endPart) {
        
        if (Random.nextFloat() < meanWPert + 0.1) {
          links(i).weight += Utility.randomClamped()*mutPow
        } else if (Random.nextFloat() < meanWRep + 0.1) {
          links(i).weight = Utility.randomClamped()
        }
      }
    }
  }

  def mutateActivationResponse(mutRate: Double, maxPertubation: Double): Unit = {
    for (gen <- 0 until neurons.length) {
      if (Random.nextFloat() < Params.activationMutationRate) {
        neurons(gen).actResp += Utility.randomClamped() * Params.maxActivationPert
        
        if (neurons(gen).actResp > 1) {
          neurons(gen).actResp = 1
        } else if (neurons(gen).actResp < 0) {
          neurons(gen).actResp = 0
        }
      }
    }
  }
  
  def mutateToggleEnable(gen: Genome): Unit = {
  val chosenLink = Utility.randInt(0, gen.links.length - 1)
  var flag = true
  if (gen.links(chosenLink).enabled) {
    
    for (i <- 0 until gen.links.length if flag) {
      if (gen.links(chosenLink).fromNeuron == gen.links(i).fromNeuron &&
          gen.links(i).enabled == true &&
          gen.links(chosenLink).innovationID != gen.links(i).innovationID) {
        flag = false
      }
      
      if (i <  gen.links.length && flag) {
        gen.links(chosenLink).enabled = false
      }
    }
  } else {
    gen.links(chosenLink).enabled = true
  }
}
  
  def mutateReenableFirst(genome: Genome) {
    var flag = true
    for (i <- 0 until genome.links.length if flag) {
      
      if (genome.links(i).enabled == false) {
        genome.links(i).enabled = true
        flag = false
      }
    }
  }

  def getCompatibilityScore(other: Genome): Double = {
    var nbrDisjoint = 0
    var nbrExcess = 0
    var nbrMatched = 0

    var weightDifference: Double = 0

    var g1 = 0
    var g2 = 0

    while (g1 < links.length - 1 || g2 < other.links.length - 1) {
      
      if (g1 == links.length - 1) {
        g2 += 1
        nbrExcess += 1
      } else if (g2 == other.links.length - 1) {
        g1 += 1
        nbrExcess += 1
      } else {
        val ID1 = links(g1).innovationID
        val ID2 = other.links(g2).innovationID

        if (ID1 == ID2) {
          g1 += 1
          g2 += 1
          nbrMatched += 1
          weightDifference += Math.abs(links(g1).weight.toFloat - other.links(g2).weight.toFloat)
        } else if (ID1 < ID2) {
          nbrDisjoint += 1
          g1 += 1
        } else if (ID1 > ID2) {
          nbrDisjoint += 1
          g2 += 1
        }
      }
    }

    var longest = other.nbrGenes

    if (nbrGenes > longest) {
      longest = nbrGenes
    }

    val mDisjoint: Double = 1
    val mExcess: Double = 1
    val mMatched: Double = 0.4

    val score = mExcess * nbrExcess / longest.toDouble +
      mDisjoint * nbrDisjoint / longest.toDouble +
      mMatched * weightDifference / nbrMatched;
    score
  }

  def sortGenes: Unit = {
    links = links.sortBy(_.innovationID)
  }

  def <(other: Genome): Boolean = {
    fitness < other.fitness
  }

  def nbrGenes(): Int = {
    links.length
  }
  
  def nbrNeurons(): Int = {
    neurons.length
  }

  def splitY(ind: Int): Double = {
    neurons(ind).splitY
  }
  
}