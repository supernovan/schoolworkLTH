package Neat

import scala.collection.mutable.{ArrayBuffer => ArrayBuffer}
import Neat.Utility
import Neat.Genome
import Neat.Params


class Species(firstGenome: Genome, var speciesID: Int) {
  
  var bestFit: Double = firstGenome.fitness
  var gensNoImprov: Int = 0
  var age: Int = 0
  var leader = firstGenome
  var nbrToSpawn: Double = 0

  val members = ArrayBuffer.empty[Genome]
  members += leader
  
  def nbrMembers(): Int = {
    members.length
  }
  
  def addMember(newMember: Genome): Unit = {
  	if (bestFit < newMember.fitness) {
  		bestFit = newMember.fitness
  		gensNoImprov = 0
  		leader = newMember
  	}

  	members += newMember
  }

  def purge(): Unit = {
  	members.clear

  	age += 1
  	gensNoImprov +=  1

  	nbrToSpawn = 0.0
  }

  def adjustFitness(): Unit = {
  	var total: Double = 0.0

  	for (i <- 0 until members.length) {
  		var fitness: Double = members(i).fitness

  		if (age < Params.youngAgeThreshold) {
  			fitness *= Params.youngAgeBoost
  		} 
  		
  		if (age > Params.oldAgeThreshold) {
  			fitness *= Params.oldAgePenelty
  		}

  		total += fitness
  		val adjustF: Double = fitness/members.length.toDouble
  		members(i).adjustFitness  = adjustF
  	}
  }

  def calculateSpawnAmount(): Unit = {
  	for (i <- 0 until members.length) {
  		nbrToSpawn += members(i).amountToSpawn
  	}
  }

  def spawn(): Genome = {
  	
  	if (members.length == 1) {
  		members(0)
  	} else {
  		var maxIndex = ((Params.survivalRate * members.length).toInt - 1)
  		
  		if (maxIndex < 0) {
  		  maxIndex = 0
  		}
  		
  		var index = Utility.randInt(0, maxIndex)
  		if (index >= members.length) {
  		  index = members.length - 1 
  		}
  		members(index)
  	}
  }
}