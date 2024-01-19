<template>
  <body @mousemove="fitStageIntoParentContainer">
    <div class="control-header">
      <img class="imagesmain2" src="./assets/background.png" alt="">
      <header id="header" >

      </header>
      <img class="imagesmain1" src="./assets/background.png" alt="">
    </div>

      <div class="main-container">
          <div class="sidenavshape" ref="sidenavshape">
              <p class="title" id="shape">Shapes</p>
              <ul class="buttonshape">
                  <li>
                      <img class="images" src="./assets/rectangle.png" alt="">
                      <button class="shapes" id="rectangle" @click="shapeVariable = 1">Rectangle</button>
                  </li>
                  <li>
                      <img class="images" src="./assets/circle.svg" alt="">
                      <button class="shapes" id="circle" @click="shapeVariable = 2">Circle</button>
                  </li>
                  
                  <li>
                      <img class="images" src="./assets/line.png" alt="">
                      <button class="shapes" id="line" @click="shapeVariable = 3">Line</button>
                  </li>
              </ul>
          </div>
      
          <section>
              <div id="container" @click="createElement" >
                <v-stage ref="stage" :config="stageConfig">
                  <v-layer ref="myLayer">
                    <v-circle v-for="shapeConfig in circles" :key="shapeConfig" class="shapeconfig" :config="shapeConfig"></v-circle>
                    <v-rect v-for="shapeConfig in rectangles" :key="shapeConfig" class="shapeconfig" :config="shapeConfig"></v-rect>
                    <v-arrow v-for="shapeConfig in lines" :key="shapeConfig" class="shapeconfig" :config="shapeConfig"></v-arrow>
                  </v-layer>
                </v-stage>
              </div>
          </section>
          <div class="sidenavoption">
              <p class="title" id="option">Options</p>
              <ul>
                <li>
                    <img class="images" src="./assets/clear.png" alt="" style="width:14px">
                    <button class="options" id="clear" @click="clear">Clear</button>
                </li>
              </ul>
          </div>
      </div>
  </body>
</template>

<script>

export default {
  data() {
    return {
      mouseDown: 0,
      id: 0,
      rectangles: [],
      circles: [],
      lines: [],
      allshapes: [],
      layerConfig: {},
      selectedShapeId: -1,

      stageConfig: {
          width: 1000,
          height: 600,
      },
      shapeVariable: 0,
    };
  },
  created() {
    window.addEventListener("resize", this.fitStageIntoParentContainer)
  },
  methods: {
    fitStageIntoParentContainer() {
      var container = window.getComputedStyle(document.getElementById('container'))

      // now we need to fit stage into parent container
      var containerWidth = parseInt(container.width)
      var containerHeight = parseInt(container.height)
      this.stageConfig.width= containerWidth
      this.stageConfig.height= containerHeight
    },
    clear (){
        this.allshapes = []
        this.rectangles= []
        this.circles= []
        this.ellipses= []
        this.triangles= []
        this.pentagons= []
        this.hexagons= []
        this.lines= []
        fetch("http://localhost:8081/paint/clearAll",{
            method:"POST",
            headers : {
                'Content-type':'application/json',
            },
            }).then(responce=>{
            return responce.text();
            }).then(data =>{
            console.log(data);
            }).catch(error => {
            console.error("Error:", error);
            this.isError = true;
        });
    },
    createElement(event){

        //for adjusting position of shapes
        var item1ForWidth  = document.querySelector('.sidenavshape').offsetWidth
        var item2ForWidth  = parseFloat(window.getComputedStyle(document.getElementById('container')).marginLeft)*2+5
        var item1ForHeight = document.querySelector('.control-header').offsetHeight
        var item2ForHeight = parseFloat(window.getComputedStyle(document.getElementById('container')).marginTop)
        var offsetHeightCanvas = item1ForHeight + item2ForHeight
        var offsetWidthCanvas  = item1ForWidth + item2ForWidth
        
        switch (this.shapeVariable){
            case 1: 
                var rect = ({
                    x: event.clientX-offsetWidthCanvas-100,
                    y: event.clientY-offsetHeightCanvas-50,
                    width: 200,
                    height: 100,
                    fill: 'yellow',
                    stroke: 'black',
                    draggable: true,
                    strokeWidth: 3,
                    name: 'rectangle',
                    id: String(this.id)
                });
                this.id = String((Number(this.id)+ 1))
                this.rectangles.push(rect)
                this.allshapes.push(rect)

                fetch("http://localhost:8081/paint/create",{
                method:"POST",
                headers : {
                    'Content-type':'application/json',
                },
                body : JSON.stringify(rect)
                }).then(responce=>{
                return responce.text();
                }).then(data =>{
                console.log(data);
                }).catch(error => {
                console.error("Error:", error);
                this.isError = true;
                });
                break;
            case 2: 
                var circle = ({
                    x: event.clientX-offsetWidthCanvas,
                    y: event.clientY-offsetHeightCanvas,
                    radius: 60,
                    fill: 'green',
                    stroke: 'black',
                    draggable: true,
                    strokeWidth: 3,
                    name: 'circle',
                    id: String(this.id)
                });
                this.id = String((Number(this.id) + 1));
                this.circles.push(circle)
                this.allshapes.push(circle)

                fetch("http://localhost:8081/paint/create",{
                method:"POST",
                headers : {
                    'Content-type':'application/json',
                },
                body : JSON.stringify(circle)
                }).then(responce=>{
                return responce.text();
                }).then(data =>{
                console.log(data);
                }).catch(error => {
                console.error("Error:", error);
                this.isError = true;
                });
                break;
            case 3: 
                var line = ({
                    points: [event.clientX-100-offsetWidthCanvas, event.clientY-100-offsetHeightCanvas,
                            event.clientX+100-offsetWidthCanvas, event.clientY+100-offsetHeightCanvas],
                    stroke: "black",
                    strokeWidth: 10,
                    lineCap: 'round',
                    lineJoin: 'round',
                    draggable: true,
                    name: 'line',
                    id: String(this.id)
                });
                this.id = String((Number(this.id) + 1));
                this.lines.push(line)
                this.allshapes.push(line)

                fetch("http://localhost:8081/paint/create",{
                method:"POST",
                headers : {
                    'Content-type':'application/json',
                },
                body : JSON.stringify(line)
                }).then(responce=>{
                return responce.text();
                }).then(data =>{
                console.log(data);
                }).catch(error => {
                console.error("Error:", error);
                this.isError = true;
                });
                break;
        }
        this.shapeVariable = 0
    },
  }
}
</script>

<style>
body, p { margin:0; padding:0}


html {
    height: 100%;
}

body{
    height:100%;
    align-items: center;
    justify-content: center;
    background: linear-gradient(to right,#b2d0e8,rgb(106, 133, 182) );
}

.main-container {
    display: flex;
    width: 100%;
    justify-content: space-between;
}

section {
    width: 70%;
    height: calc(900px * 0.6);
}

#container {
    height: 100%;
    background-color: #fefefe;
    margin: calc(15px * 0.6);
    border-radius: calc(50px * 0.6);
}

.control-header {
    display: flex;
}

header{
    width: calc(900px * 0.6);
    height: calc(130px * 0.6);
    align-items: center;
    padding: calc(16px * 0.6);
    color: white;
    border-style:ridge;
    border-radius: calc(50px * 0.6);
    background: linear-gradient(to top,#394b9a 0%,#a7c9e0 );
    margin: auto;
    margin-top: calc(20px * 0.6);
    border-color: black;

}

.draw{
    margin-left: calc(100px * 0.6);
}

#save, #load{
    width: calc(120px * 0.6);
    border-radius: calc(30px * 0.6);
    padding : calc(8px * 0.6) calc(10px * 0.6);
    margin-bottom: calc(4px * 0.6);
    margin-top: calc(4px * 0.6);
    font-weight: 900;
    font-size: calc(33px * 0.6);
    color: black;
    text-align: center;
    font-family: "Times New Roman", Arial, Helvetica, sans-serif;
    cursor: pointer;
    background: #e9eef0;
    margin-left: calc(3px * 0.6);
    align-content: center;
    align-items: center;
    justify-content: center;  
}

#load{
    margin-left: calc(390px * 0.6);
}

#save:hover, #load:hover{
    background-color: rgb(0, 0, 0);
    color: rgb(243, 290, 255);
}

.title{
    margin-top: calc(15px * 0.6);
    align-content: center;
    align-items: center;
    justify-content: center;
    padding: calc(8px * 0.6) calc(40px * 0.6);
    font-weight: 1000;
    font-size: calc(35px * 0.6);
    color: #000000;
    text-align: center;
    font-family:  Helvetica, sans-serif;
    display:flex;
    width: calc(60px * 0.6);
    border-radius: calc(10px * 0.6);
}

.images{
    margin-right: calc(5px * 0.6);
    filter:drop-shadow(100%) contrast(100%) saturate(3000%);
    margin-left: calc(-10px * 0.6);
}
.shapes, .options{
    width: 100%;
    padding : calc(5px * 0.6) 0;
    margin-bottom: calc(5px * 0.6);
    margin-top: calc(4px * 0.6);
    border-radius: calc(15px * 0.6);
    color:rgb(2, 0, 2);
    font-size: calc(20px * 0.6);
    cursor: pointer;
    background: #e9eef0;
    font-family: 'Signika Negative', sans-serif;
}

.options{
    width: 80%;
}

form {
    display: flex;
    flex-direction: column;
    align-items: center;
}

#selectcolor{
    width: 100%;
    margin: calc(4px * 0.6) 0px calc(4px * 0.6) calc(25px * 0.6);
    padding : calc(8px * 0.6) 0;
    color:rgb(2,0,2);
    font-size: calc(22px * 0.6);
    font-weight: bold;
    font-style: italic;
    cursor: pointer;
    font-family: 'Signika Negative', sans-serif;
}

#color{
    height: calc(50px * 0.6);
    width: calc(50px * 0.6);
    background-color: transparent;
    border: inset;
    border-radius: 100%;
    cursor: pointer;
}

.shapes:hover, .options:hover{
    background-color: rgb(0, 0, 0);
    color: rgb(243, 290, 255);
}

.sidenavoption {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 10%;
    padding:calc(5px * 0.6) calc(30px * 0.6) calc(20px * 0.6) calc(30px * 0.6);
    height:calc(600px * 0.6);
    margin: calc(130px * 0.6) calc(15px * 0.6) calc(15px * 0.6) calc(15px * 0.6);
    background: linear-gradient(to right,#394b9a 0%,#a7c9e0 );
    padding-top: calc(20px * 0.6);
    border-radius: calc(50px * 0.6);
    border-style:ridge;
    border-color: black;
}

.sidenavoption button {
    padding: calc(8px * 0.6) calc(25px * 0.6);
    text-align: center;
    text-decoration: none;
    font-size: calc(25px * 0.6);
    color: black;
    display: block;
}

.sidenavoption button:hover {
    color: #f1f1f1;
}
.sidenavshape {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 10%;
    padding:calc(5px * 0.6) calc(30px * 0.6) calc(20px * 0.6);
    height: calc(600px * 0.6);
    margin: calc(130px * 0.6) calc(15px * 0.6) calc(15px * 0.6) calc(15px * 0.6);
    background: linear-gradient(to left,#394b9a 0%,#a7c9e0 );
    padding-top: calc(20px * 0.6);
    border-radius: calc(50px * 0.6);
    border-style:ridge;
    border-color: black;
}

.sidenavshape button {
    padding: calc(6px * 0.6) calc(16px * 0.6);
    text-decoration: none;
    font-size: calc(25px * 0.6);
    color: black;
    display: block;
    text-align: center;
    align-items: center;
    justify-items: center;
}

.sidenavshape button:hover {
    color: #f1f1f1;
}
ul{
    padding-left: calc(5px * 0.6);
    margin: calc(2px * 0.6) 0px 0px calc(5px * 0.6);
}

li{
    display: flex;
    cursor: pointer;
    align-items: center;
}

li #xml, li #json{
    display: inline;
}

.inputs{
    display: inline-flex;
}

.save{
    border-radius: calc(10px * 0.6);
    text-align:left;
    justify-content: center;
    padding: calc(5px * 0.6);
    padding-right:calc(2px * 0.6);
    border-color: #010811;
    border-width: calc(2px * 0.6);
    margin-left:calc(5px * 0.6);
    font-size:calc(15px * 0.6);
    width: 37%;
    
}

.load{
    border-radius: calc(10px * 0.6);
    text-align:left;
    justify-content: center;
    padding: calc(5px * 0.6);
    padding-right:calc(2px * 0.6);
    border-color: #010811;
    border-width: calc(2px * 0.6);
    margin-left:calc(150px * 0.6);
    font-size:calc(15px * 0.6);
    width: 37%;
}
.name{
    border-radius: calc(10px * 0.6);
    text-align:left;
    justify-content: center;
    padding: calc(5px * 0.6);
    padding-right:calc(2px * 0.6);
    border-color: #010811;
    border-width: calc(2px * 0.6);
    font-size:calc(15px * 0.6);
    width: calc(37% * 0.6);
} 

input::-webkit-color-swatch{
    border-radius: calc(20px * 0.6);
    border: calc(4px * 0.6) solid black;
}

.format {
    align-items: center;
    justify-content: center;
    margin-left:calc(15px * 0.6) ;
}

.format  #json, .format #xml{
    font-size: calc(20px * 0.6);
}

.format input{
    accent-color:  rgb(5, 0, 6);
}

#extension{
    display: inline-flex;
    margin-left:calc(70px * 0.6);
}

.imagesmain1{
    float: left;
    width: 200px;
    height: 150px; 
}

.imagesmain2{
    float: right;
    width: 200px;
    height: 150px; 
}

#clear{
    width: 72px;
}

.draw {
    display: flex;
    flex-direction: column;
}
</style>
