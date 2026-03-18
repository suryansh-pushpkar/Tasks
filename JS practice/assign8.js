var ourDog = {
    "name ": "Camper",
    "legs":4,
    "tails": 1,
    "friends": ["Everything"]
};

console.log(ourDog.friends);
console.log(ourDog["legs"]);
ourDog.bark = "Boof";
console.log(ourDog)
delete ourDog.bark;
console.log(ourDog )