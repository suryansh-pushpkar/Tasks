var global = 5;
function f1(){
   var global = 10;
    console.log(global);
}
f1();
console.log(global);

