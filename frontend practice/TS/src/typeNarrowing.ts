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