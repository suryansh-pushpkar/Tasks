let stocks = {
    fruits: ["strawberry", " Grapes", "Banana", "apple"],
    liquid: ["water", "ice"],
    holder: ["cone", "cup", "stick"],
    toppings: ["chocolate", "peanuts"]


}



let order = (fruit_name, call_production) => {
    setTimeout(() => {
        console.log(`${stocks.fruits[fruit_name]} was selected`);
        call_production();

    }, 2000)
};

let production = () => {

    setTimeout(() => { console.log("Production has started");
0
setTimeout(()=>{
    console.log("Fruit has been chopped");

    setTimeout(()=>{
        console.log(`${stocks.liquid[0]} and ${stocks.liquid[1]} was added`);

        setTimeout(()=>{
            console.log("Machine Started");

            setTimeout(()=>{
console.log(`${stocks.holder[0]} was selected`);

setTimeout(()=>{
    console.log(`${stocks.toppings[0]} added `);

    setTimeout(()=>{console.log("Serve the Ice Cream")},2000)
},3000)


            },2000);
        },1000)
    },1000)
},2000)

     }, 0);

};

order(0, production);
