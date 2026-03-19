let stocks = {
    fruits: ["strawberry", " Grapes", "Banana", "apple"],
    liquid: ["water", "ice"],
    holder: ["cone", "cup", "stick"],
    toppings: ["chocolate", "peanuts"]


}

var is_shop_open = true;

let order = (time, work) => {

    return new Promise((resolve, reject) => {
        if (is_shop_open) {
            setTimeout(() => {
                resolve(work())
            }, time);
        } else {
            reject(console.log("Shop is Closed"));
        }
    })
}

order(2000,()=>console.log(`${stocks.fruits[0]} is selected`))

.then(()=>{
    return order(0,()=>{console.log("Production Started")})
})

.then(()=>{
    return order(2000,()=>{console.log("Fruit was chopped")})
})

.then(()=>{
    return order(1000,()=>{
        console.log(`${stocks.liquid[1]}`)
    })
})

.then(()=>{
    return order(1000, ()=>{
        console.log("Start the Machine")
    })
})

.then(()=>{
    return order(2000, ()=>{
        console.log(`${stocks.holder[0]} Ice cream placed `)
    })
})

.then(()=>{
    return order(3000,()=>{
        console.log(`${stocks.toppings[0]} added`)
    })
})

.then(()=>{
    return order(1000, ()=>{
        console.log("IceCream is Served")
    })
})

.catch(()=>{
    console.log("Customer Left")
})

.finally(()=>{
    console.log("Day Ended Please come tommorow")
})