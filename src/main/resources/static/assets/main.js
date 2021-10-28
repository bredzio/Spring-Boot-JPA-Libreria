const form = document.getElementById("formulario");
var today=new Date();
var year=today.getFullYear();     

var input = document.getElementById("ejemplares");
var input2 = document.getElementById("ejemplaresPrestados");
var anio = document.getElementById("anio");
anio.setAttribute("max",year);

anio.addEventListener('input',e=>{
    if(anio.value>2025){
        document.getElementById("demoAnio").innerHTML="Ingrese un año menor";
    }

    if(anio.value<1){
        document.getElementById("demoAnio").innerHTML="Ingrese un año mayor a 0";
    }    
})

input.addEventListener('input',e=>{
    input2.setAttribute("max",input.value)    
})


input2.addEventListener('input',e=>{
    if(input2.value<1){
        document.getElementById("demo").innerHTML="Ingrese un número mayor a 0";

    }
    
    if(input2.value>input.value){
        document.getElementById("demo").innerHTML="Ingrese una cantidad menor a la cantidad total de ejemplares";

    }
})