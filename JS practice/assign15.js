const AVG_TEMPRATURES = {
    today : 77.5,
    tommorow : 79
};

function getTempOfTmrw(avgTempratures){
    "use strict";
    const {tommorow : tempOfTomorro} = avgTempratures;
    return tempOfTomorro;
}

console.log(getTempOfTmrw(AVG_TEMPRATURES))