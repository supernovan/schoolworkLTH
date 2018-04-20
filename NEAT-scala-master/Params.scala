

package Neat

object Params {
  
  
  
  
  var popSize = 100
  
  
  var maxNumberOfSpieces = 17
  
  var chanceAddLink = 0.11
  var chanceAddNode = 0.05
  var chanceAddReccurentLink = 0.00
  
  var activationMutationRate = 0.06
  
  var compatibilityThreshold = 6.0
  
  var mutationRateWeight = 0.5
  var probWeightReplace = 0.3
  var maxWeightPert = 0.6
  var maxActivationPert = 0.05
  
  var nbrTriesToFindLoopedLink = 5
  var nbrAddLinkAttempts = 15
  var nbrTriesToFindOldLink = 10
  
  var youngAgeThreshold = 10
  var oldAgeThreshold = 15
  var oldAgePenelty = 0.01
  var youngAgeBoost = 1.1
  
  var survivalRate = 0.25
  
  //Cga
  
  
  var crossoverRate = 0.7
  var nbrGensAllowedNoImprov = 7
  var maxPermittedNeurons = 12
  
  var nbrBestSpeies =5
  var nbrSpecies = 11
  var nbrInputs = 2
  var nbrOutputs = 2
}