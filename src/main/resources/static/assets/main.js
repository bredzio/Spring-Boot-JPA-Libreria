var input = document.getElementById("ejemplares");
            var input2 = document.getElementById("ejemplaresPrestados");

            input.addEventListener('click',e=>{
                input2.setAttribute("max",input.value)    
})