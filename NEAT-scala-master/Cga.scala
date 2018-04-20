package Neat

import scala.collection.mutable.{ ArrayBuffer => ArrayBuffer }
import Neat.InnovationC
import Neat.Species
import scala.util.Random
import Neat.NeuralNet
import Neat.LinkGene
import Neat.Utility
import Neat.NeuronGene
import Neat.Genome
import Neat.Params

class Cga(size: Int, inputs: Int, outputs: Int) {
  var vecGenomes = ArrayBuffer.empty[Genome]
  var vecBestGenomes = ArrayBuffer.empty[Genome]
  var vecSpecies = ArrayBuffer.empty[Species]
  var vecSplits = ArrayBuffer.empty[(Double, Int)]

  var innov: InnovationC = _

  var iGen: Int = 0
  var popSize: Int = size
  var nextGenomeID: Int = 0
  var nextSpeciesID: Int = 0
  var indexFitnessGenome: Int = 0
  var bestEverFitness: Double = 0.0

  var totalAdjFit: Double = 0.0
  var averageAdjFit: Double = 0.0
  
  var counter = 0
  for (i <- 0 until popSize) {
    vecGenomes += new Genome(nextGenomeID, inputs, outputs)
    nextGenomeID += 1
  }

  val genome = new Genome(1, inputs, outputs)

  innov = new InnovationC(genome.links, genome.neurons)

  vecSplits = useSplit(0, 1, 0)

  def createPhenotypes(): ArrayBuffer[NeuralNet] = {
    var networks = ArrayBuffer.empty[NeuralNet]

    for (i <- 0 until popSize) {
      calculateNetDepth(vecGenomes(i))

      var net = vecGenomes(i).createPhenotype

      networks += net
    }
    networks
  }

  def calculateNetDepth(gen: Genome): Unit = {
    var maxSoFar: Int = 0
    for (i <- 0 until gen.nbrNeurons) {
      for (j <- 0 until vecSplits.length) {

        if (gen.splitY(i) == vecSplits(j)._1 &&
          vecSplits(j)._2 > maxSoFar) {
          maxSoFar = vecSplits(j)._2
        }
      }
    }
    gen.netDepth = maxSoFar + 2
  }

  def addNeuronID(nodeID: Int, vec: ArrayBuffer[Int]): Unit = {
    var check = true
    for (i <- 0 until vec.length if check) {
      if (vec(i) == nodeID) {
        check = false
      }
    }
    if (check) {
      //		  println("här eller där?")
      vec += nodeID
    }
  }

  def epoch(fitnessScores: ArrayBuffer[Double]): ArrayBuffer[NeuralNet] = {
    if (fitnessScores.length != vecGenomes.length) {
      println("Kaos i orten, inte samma storlek i epoch")
    }
    counter += 1
    resetAndKill()
    
    for (i <- 0 until vecGenomes.length) {
      vecGenomes(i).fitness = fitnessScores(i)
    }

    sortAndRecord()
    if (counter % 10 == 0) {
      println(vecGenomes(0).fitness)
    }
    
    
//    println(vecGenomes(0).fitness + " ")
    speciateAndCalculateSpawnLevels()
    
    var newPop = ArrayBuffer.empty[Genome]

    var nbrSpawnedSoFar: Int = 0

    var baby: Genome = null
//    var nbr1 = 0
//    var nbr2 = 0
//    var nbr3 = 0
//    var nbr4 = 0
    
 
    for (spc <- 0 until vecSpecies.length) {

      if (nbrSpawnedSoFar < Params.popSize) {

        var nbrToSpawn: Int = Math.round(vecSpecies(spc).nbrToSpawn).toInt

        var bestChoice = false

        while (nbrToSpawn > 0) {
          
          if (!bestChoice) {
            baby = vecSpecies(spc).leader
            bestChoice = true
          } else {

            if (vecSpecies(spc).nbrMembers == 1) {
//              nbr1 += 1
              baby = vecSpecies(spc).spawn
            } else {
              
              val g1 = vecSpecies(spc).spawn

              if (Random.nextFloat() < Params.crossoverRate) {
                var g2 = vecSpecies(spc).spawn
                val nbrAttempts: Int = 5

                for (i <- 0 until nbrAttempts if g1.ID == g2.ID) {
                  g2 = vecSpecies(spc).spawn
                }

                if (g1.ID != g2.ID) {
                  baby = crossover(g1, g2)
//                  nbr2 += 1
                } else {
                  baby = g1
//                  nbr3 += 1
                }
              } else {
//                nbr4 += 1
                baby = g1
              }

            }
            
            nextGenomeID += 1
            baby.ID = nextGenomeID
            
            if (Random.nextFloat() < Params.chanceAddNode) {
              baby.addNeuron(Params.chanceAddNode, innov, Params.nbrTriesToFindOldLink)
            }
            baby.addLink(Params.chanceAddLink, Params.chanceAddReccurentLink,
              innov, Params.nbrTriesToFindLoopedLink, Params.nbrAddLinkAttempts)

            //baby.mutateWeights(Params.mutationRateWeight, Params.probWeightReplace, Params.maxWeightPert)
            baby.mutateWeights()

            baby.mutateActivationResponse(Params.activationMutationRate, Params.maxActivationPert)
            //maybe enable/disable a random gene
            if (Random.nextFloat() < 0.07) {
              baby.mutateToggleEnable(baby)
            }
            
            if (Random.nextFloat() < 0.07) {
              baby.mutateReenableFirst(baby)
            }
            //Maybe enable first disabled gene you can find
          }

          baby.sortGenes

          newPop += baby

          nbrSpawnedSoFar += 1
          
          if (nbrSpawnedSoFar >= Params.popSize) {
            nbrToSpawn = 0
          }
        }
      }
    }

//    println(nbr1.toString + " " + nbr2.toString + " " + nbr3.toString + " " + nbr4.toString)
    if (nbrSpawnedSoFar < Params.popSize) {
      val rqd: Int = Params.popSize - nbrSpawnedSoFar

      for (i <- 0 until rqd) {
        newPop += tournamentSelection(popSize / 5)
      }
    }
    
    vecGenomes = newPop

    var newPhenotypes = ArrayBuffer.empty[NeuralNet]

    for (i <- 0 until vecGenomes.length) {

      calculateNetDepth(vecGenomes(i))

      val phenotype = vecGenomes(i).createPhenotype

      newPhenotypes += phenotype
    }

    iGen += 1
    newPhenotypes
  }

  def sortAndRecord(): Unit = {
//    print("Osorterad: ")
//    vecGenomes.foreach(f => print(f.fitness + " "))
//    println("\n")
//    print("Sorterad: ")
      vecGenomes = vecGenomes.sortWith(sortByFitness)
//    vecGenomes.foreach(f => print(f.fitness + " "))
//    println("\n")
    
    if (vecGenomes(0).fitness > bestEverFitness) {
      bestEverFitness = vecGenomes(0).fitness
    }

    storeBestGenomes()
  }

  def sortByFitness(g1: Genome, g2: Genome) = {
    g1.fitness > g2.fitness
  }

  def storeBestGenomes(): Unit = {
    vecBestGenomes.clear

    for (i <- 0 until Params.nbrBestSpeies) {
      vecBestGenomes += vecGenomes(i)
    }
  }

  def getBestPhenotypesLastGen(): ArrayBuffer[NeuralNet] = {
    var brains = ArrayBuffer.empty[NeuralNet]

    for (gen <- 0 until vecBestGenomes.length) {

      calculateNetDepth(vecBestGenomes(gen))
      brains += vecBestGenomes(gen).createPhenotype
    }

    brains
  }

  def adjustSpeciesFitness(): Unit = {
    for (i <- 0 until vecSpecies.length) {
      vecSpecies(i).adjustFitness
    }
  }

  def speciateAndCalculateSpawnLevels(): Unit = {
    var added = false

//    adjustCompatibilityThreshold

    for (gen <- 0 until vecGenomes.length) {
      for (spc <- 0 until vecSpecies.length if !added) {

        val compatibility = vecGenomes(gen).getCompatibilityScore(vecSpecies(spc).leader)

        if (compatibility <= Params.compatibilityThreshold) {

          vecSpecies(spc).addMember(vecGenomes(gen))

          vecGenomes(gen).species = vecSpecies(spc).speciesID
         
          added = true

        }
      }
      if (!added) {
        vecSpecies += (new Species(vecGenomes(gen), nextSpeciesID))
        nextSpeciesID += 1
      }
      added = false
    }

    adjustSpeciesFitness

    for (gen <- 0 until vecGenomes.length) {
      totalAdjFit += vecGenomes(gen).adjustFitness
    }

    averageAdjFit = totalAdjFit / vecGenomes.length.toDouble

    for (gen <- 0 until vecGenomes.length) {
      val toSpawn = vecGenomes(gen).adjustFitness / averageAdjFit
      vecGenomes(gen).amountToSpawn = toSpawn
    }

    for (spc <- 0 until vecSpecies.length) {
      vecSpecies(spc).calculateSpawnAmount
    }
  }

  def adjustCompatibilityThreshold(): Unit = {
    if (Params.maxNumberOfSpieces >= 1) {
      val thresholdIncrement = 0.01

      if (vecSpecies.length > Params.maxNumberOfSpieces) {
        Params.compatibilityThreshold += thresholdIncrement
      } else if (vecSpecies.length < 2) {
        Params.compatibilityThreshold -= thresholdIncrement
      }
//        else if (vecSpecies.length < Params.maxNumberOfSpieces) {
//        Params.compatibilityThreshold -= thresholdIncrement
//      }
//      if (Params.compatibilityThreshold < Params.maxNumberOfSpieces) {
//        Params.compatibilityThreshold = Params.maxNumberOfSpieces
//      }
    }
  }

  def tournamentSelection(nbrComparisons: Int): Genome = {
    var bestFitnessSoFar: Double = 0.0

    var chosenOne: Int = 0

    for (i <- 0 until nbrComparisons) {
      val thisTry: Int = Utility.randInt(0, vecGenomes.length - 1)

      if (vecGenomes(thisTry).fitness > bestFitnessSoFar) {
        chosenOne = thisTry

        bestFitnessSoFar = vecGenomes(thisTry).fitness
      }
    }

    vecGenomes(chosenOne)
  }

  def crossover(mom: Genome, dad: Genome): Genome = {

    var best: Int = 0
    if (mom.fitness == dad.fitness) {

      if (mom.nbrGenes == dad.nbrGenes) {
        best = Random.nextInt(2) + 1
      } else {

        if (mom.nbrGenes < dad.nbrGenes) {
          best = 1
        } else {
          best = 2
        }
      }
    } else {

      if (mom.fitness > dad.fitness) {
        best = 1
      } else {
        best = 2
      }
    }

    val babyNeurons = ArrayBuffer.empty[NeuronGene]
    val babyGenes = ArrayBuffer.empty[LinkGene]

    var vecNeuron = ArrayBuffer.empty[Int]

    var selectedGene: LinkGene = null

    val maxMom = mom.nbrGenes
    val maxDad = dad.nbrGenes
    var momNbr = 0
    var dadNbr = 0

    //    if (momNbr < 0 || dadNbr < 0) {
    //      println(momNbr + " " + momNbr + "Problem i orten")
    //    }

    while (momNbr < maxMom || dadNbr < maxDad) {

      if (momNbr >= maxMom && dadNbr < maxDad) {

        if (best == 2) {
          selectedGene = dad.links(dadNbr)
        }
        dadNbr += 1

      } else if (momNbr < maxMom && dadNbr >= maxDad) {

        if (best == 1) {
          selectedGene = mom.links(momNbr)
        }
        momNbr += 1

      } else if (mom.links(momNbr).innovationID < dad.links(dadNbr).innovationID) {

        if (best == 1) {
          selectedGene = mom.links(momNbr)
        }

        momNbr += 1
      } else if (dad.links(dadNbr).innovationID < mom.links(momNbr).innovationID) {

        if (best == 2) {
          selectedGene = dad.links(dadNbr)
        }

        dadNbr += 1
      } else if (dad.links(dadNbr).innovationID == mom.links(momNbr).innovationID) {

        if (Random.nextBoolean()) {
          selectedGene = dad.links(dadNbr)
        } else {
          selectedGene = mom.links(momNbr)
        }

        momNbr += 1
        dadNbr += 1
      }

      val size = babyGenes.length
      if (size == 0) {
        babyGenes += selectedGene
      } else {
        if (babyGenes(size - 1).innovationID != selectedGene.innovationID) {
          babyGenes += selectedGene
        }
      }

      addNeuronID(selectedGene.fromNeuron, vecNeuron)
      addNeuronID(selectedGene.toNeuron, vecNeuron)
    }
    vecNeuron = vecNeuron.sorted

    for (i <- 0 until vecNeuron.length) {
      babyNeurons += innov.createNeuronFromID(vecNeuron(i))
    }
    //    println("problemet är här?")
    //    println(babyNeurons + " " + babyGenes)
    val babyGenome = new Genome(nextGenomeID, babyNeurons, babyGenes, mom.nbrInputs, mom.nbrOutputs)
    nextGenomeID += 1
    babyGenome
  }

  def resetAndKill(): Unit = {
    totalAdjFit = 0
    averageAdjFit = 0

    var i = 0

    while (i < vecSpecies.length) {
      vecSpecies(i).purge

      if (vecSpecies(i).gensNoImprov > Params.nbrGensAllowedNoImprov &&
        vecSpecies(i).bestFit < bestEverFitness) {
        vecSpecies.remove(i)
        i -= 1
      }
      i += 1
    }

    for (i <- 0 until vecGenomes.length) {
      vecGenomes(i).deletePhenotype
    }
  }

  //Uncertain if the recursive part works as intended

  def useSplit(low: Double, high: Double, depth: Int): ArrayBuffer[(Double, Int)] = {
    vecSplits = ArrayBuffer.empty[(Double, Int)]
    val returnV = split(low, high, depth)
    returnV
  }

  def split(low: Double, high: Double, depth: Int): ArrayBuffer[(Double, Int)] = {
    val span = high - low

    vecSplits += (((low + span/2).toDouble, depth + 1))

    if (depth > 6) {
      vecSplits
    } else {
      split(low, low + span / 2, depth + 1)
      split(low + span / 2, high, depth + 1)

      vecSplits
    }
  }
}