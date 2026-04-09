function getChai(kind: string| number){
    if(typeof kind === 'string'){
        return `making ${kind} chai...`;

    }
    return `chai order : ${kind}`;
}

function serveChai (msg ? :string ){
    if(msg){
        return `serving ${msg}`;
    }
    return `serving default ginger tea`
}

type MasalaChai = {type: "Masala"; spicelevel : number};
type GingerChai = {type: "ginger"; amount : number};
type ElaichiChai = {type: "elaichi"; aroma : number};

type Chai = MasalaChai | GingerChai | ElaichiChai

function MakeChai(order : Chai){
     //Checks
}

