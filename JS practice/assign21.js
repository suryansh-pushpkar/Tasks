let stocks = {
    fruits: ["strawberry", " Grapes", "Banana", "apple"],
    liquid: ["water", "ice"],
    holder: ["cone", "cup", "stick"],
    toppings: ["chocolate", "peanuts"]


}

var is_shop_open = true;

let toppings_choice=()=>{
    return new Promise((resolve, reject)=>{
        setTimeout(()=>{
            console.log("Which topping would you like: ");
        },3000)
    });
};


async function kitchen(){
    console.log("A");
    console.log("B");
    console.log("C");
    console.log("D");
    console.log("E"); 
}