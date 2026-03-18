function wordBlanks(myNoun,myAdjective, myVerb, myAdverb){
    var result = "";
    result += "the"+ " "+myAdjective+ " " +myNoun+ " "+myVerb + " "+" to the store " + myAdverb
    return result;
}

console.log(wordBlanks("dog", "big", "ran","quickly"));