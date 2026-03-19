// callback function 

function one(call_two){
        call_two();

    console.log("Step 1 complete Call step 2");
}

function two(){
    console.log("Step 2");
}

one(two);