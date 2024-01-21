<template>
    <body @mousemove="fitStageIntoParentContainer">
        <div class="control-header">
            <img class="imagesleft" src="./assets/imagenew.png" alt="">
            
            <header id="header">
                <p class="products">Number of Products: {{ numberOfProducts }}</p>
                <button class="productsbutton" @click="decrement">-</button>
                <button class="productsbutton" @click="increment">+</button>
            </header>
            <img class="imagesright" src="./assets/imagenew.png" alt="">
        </div>

        <div class="main-container">
            <section>
                <div id="container" @click="createElement" @mousemove="handleLineMove($event)">
                    <v-stage ref="stage" :config="stageConfig" @mousedown="handleStageMouseDown" @touchstart="handleStageMouseDown">
                        <v-layer ref="myLayer">
                            <v-arrow v-for="shapeConfig in lines" :key="shapeConfig" class="shapeconfig"
                                :config="shapeConfig"></v-arrow>
                            <v-circle v-for="shapeConfig in circles" :key="shapeConfig" class="shapeconfig"
                                :config="shapeConfig" @click="handleShapeClick(shapeConfig, $event)" @dragend="handleDragEnd"
                                @mouseenter="enterChangeColorCircle(shapeConfig)" @mouseleave="leaveChangeColorCircle(shapeConfig)"></v-circle>
                            <v-rect v-for="shapeConfig in rectangles" :key="shapeConfig" class="shapeconfig"
                                :config="shapeConfig" @click="handleShapeClick(shapeConfig, $event)" @dragend="handleDragEnd"
                                @mouseenter="enterChangeColorRectangle(shapeConfig)" @mouseleave="leaveChangeColorRectangle(shapeConfig)"></v-rect>

                        </v-layer>
                    </v-stage>
                </div>
            </section>
            <div class="sidenavshape" ref="sidenavshape">
                <p class="title" id="shape">Process</p>
                <ul class="buttonshape">
                    <li>
                        <img class="images" src="./assets/machine.png" alt="">
                        <button class="shapes" id="circle" @click="handleOtherShapes(2)">Machine</button>
                    </li>
                    <li>
                        <img class="images" src="./assets/consumers.png" alt="">
                        <button class="shapes" id="rectangle" @click="handleOtherShapes(1)">Queue</button>
                    </li>
                    <li>
                        <img class="images" src="./assets/line.png" alt="" style="margin-right: 18px;">
                        <button class="shapes" id="line" @click="handleLine()">Link</button>
                    </li>
                </ul>
                <p class="title" id="option">Options</p>
                <ul class="buttonshape">
                    <li>
                        <img class="images" src="./assets/play.png" alt="">
                        <button class="options" id="simulate" @click="play">Simulate</button>
                    </li>
                    <li>
                        <img class="images" src="./assets/replay.png" alt="">
                        <button class="options" id="resimulate" @click="replay">Resimulate</button>
                    </li>
                    <li>
                        <img class="images" src="./assets/replay.png" alt="">
                        <button class="options" id="endSimulation" @click="end">End simulation</button>
                    </li>
                    <li>
                        <img class="images" src="./assets/clear.png" alt="" style="margin-right: 11px;">
                        <button class="options" id="clear" @click="clear" style="width:140px">Clear</button>
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
            isUpdating: false,
            products: 0,
            defaultCircleRadius: 60,
            defaultRectangleWidth: 200,
            defaultRectangleHeight: 100,
            defaultStroke: "black",
            defaultStrokeWidthShape: 3,
            defaultStrokeWidthLine: 10,
            defaultLineCap: 'round',
            defaultLineJoin: 'round',
            defaultCirclefill: "#05bc05",
            defaultRectangleFill: "#ffff00",
            interval: null,
            sourceConfig: null,
            sourceShapeName: "",
            shapeConfigurations: 0,
            sourceX: 0,
            sourceY: 0,
            numberOfProducts: 0,
            mouseDown: 0,
            start: false,
            id: 0,
            rectangles: [],
            circles: [],
            lines: [],
            allshapes: [],

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
        async end (){
            this.isUpdating = false
            document.getElementsByClassName("shapes").disabled = false
            document.getElementById("simulate").disabled = false
            document.getElementById("resimulate").disabled = false
            document.getElementById("clear").disabled = false

            this.numberOfProducts = 0
            const responcee = await fetch("http://localhost:8081/producer/endSim",{
                method:"POST",
                headers : {
                    'Content-type':'application/json',
                },
                }).then(responce=>{
                return responce.text();
                }).catch(error => {
                console.error("Error:", error);
                this.isError = true;
            });
            clearInterval(this.interval)
            this.interval = null

            this.circles = []
            this.allshapes.forEach((arrayItem) => {
                if(arrayItem.name === "circle"){
                    arrayItem.fill = this.defaultCirclefill
                    this.circles.push(arrayItem)
                }
                    
            });
        },
        async play(){
            document.getElementsByClassName("shapes").disabled = true
            document.getElementById("simulate").disabled = true
            document.getElementById("resimulate").disabled = true
            document.getElementById("clear").disabled = true


            this.products = this.numberOfProducts
            this.handleOtherShapes(-1)
            console.log(JSON.stringify(this.allshapes))
            let controller = new AbortController();
            setTimeout(() => controller.abort(), 1000);
            const responcee = await fetch("http://localhost:8081/producer/simulation",{
                
                signal: controller.signal,
                method:"POST",
                headers : {
                    'Content-type':'application/json',
                },
                body : JSON.stringify(this.allshapes)
                }).then(responce=>{
                return responce.text();
                }).catch(error => {
                console.error("Error:", error);
                this.isError = true;
            });
            this.isUpdating = true
            this.interval = setInterval(this.updateSimulation, 500);
        },
        async replay(){
            this.end()

            document.getElementsByClassName("shapes").disabled = true
            document.getElementById("simulate").disabled = true
            document.getElementById("resimulate").disabled = true
            document.getElementById("clear").disabled = true

            this.numberOfProducts = 0
            for(let i = 0; i < this.products; i++){
                this.increment()
            }
            this.handleOtherShapes(-1)
            let controller = new AbortController();
            setTimeout(() => controller.abort(), 1000);
            const responcee = await fetch("http://localhost:8081/producer/simulation",{
                
                signal: controller.signal,
                method:"POST",
                headers : {
                    'Content-type':'application/json',
                },
                body : JSON.stringify(this.allshapes)
                }).then(responce=>{
                return responce.text();
                }).catch(error => {
                console.error("Error:", error);
                this.isError = true;
            });
            this.isUpdating = true
            this.interval = setInterval(this.updateSimulation, 500);
        },
        async updateSimulation () {
            
            var temp = []
            const responcee = await fetch("http://localhost:8081/producer/updateState",{
                method:"POST",
                headers : {
                    'Content-type':'application/json',
                },
                }).then(responce=>{
                return responce.text();
                }).then(data =>{
                if(JSON.parse(data).length == this.allshapes.length){temp = JSON.parse(data);}
                }).catch(error => {
                console.error("Error:", error);
                this.isError = true;
            });
            console.log(this.allshapes)


            for(let i = 0 ; i < this.allshapes.length; i++){
                if (temp[i].name == "circle"){
                    for(let j =0; j < this.circles.length; j++){
                        if(this.circles[j].id == temp[i].id){
                            if(temp[i].fill === null || temp[i].fill == "#ddd"){
                                this.circles[j].fill = this.defaultCirclefill
                            }else {
                                this.circles[j].fill = temp[i].fill
                            }
                        }
                    }
                }
            }
        },
        handleDragEnd(e){
            var shape = this.allshapes.find((r) => r.id === e.target.id())
            shape.x = e.target.x()
            shape.y = e.target.y()
        },
        handleStageMouseDown(e) {
            if(e.target.attrs.name == "line" && this.shapeVariable == 3 && this.sourceX != 0){
                this.handleOtherShapes(0)
            }else if(e.target.attrs.name == "line" && this.shapeVariable == 3){
                this.handleOtherShapes(-1)
            }
        },
        handleOtherShapes(shapeVariable) {
            if(shapeVariable == 0){
                this.lines.pop()
            }
            if(this.shapeVariable == 3){
              document.getElementById('line').style.background = "#e9eef0"
              document.getElementById('line').style.color= "#000000"
              this.start = false
              this.shapeConfigurations = 0
              this.sourceX = 0
              this.sourceY = 0
              this.sourceShapeName = ""
              this.sourceConfig = null
            }
            this.shapeVariable = shapeVariable
        },
        enterChangeColorCircle(shapeConfig) {
            if(this.isUpdating == false){
                shapeConfig.fill = "#0a7b0a"
            }
        },
        leaveChangeColorCircle(shapeConfig) {
            if(this.isUpdating == false){
                shapeConfig.fill = "#05bc05"
            }
        },
        enterChangeColorRectangle(shapeConfig) {
            if(this.isUpdating == false){
                shapeConfig.fill = "#d3d306"
            }
        },
        leaveChangeColorRectangle(shapeConfig) {
            if(this.isUpdating == false){
                shapeConfig.fill = "#ffff00"
            }
        },
        async increment() {
            this.numberOfProducts++;
            const responcee = await fetch("http://localhost:8081/producer/addProduct",{
                method:"POST",
                headers : {
                    'Content-type':'application/json',
                },
                body : this.numberOfProducts
                }).then(responce=>{
                return responce.text();
                }).catch(error => {
                console.error("Error:", error);
                this.isError = true;
            });
        },
        async decrement() {
            if (this.numberOfProducts > 0) {
                this.numberOfProducts--;
                const responcee = await fetch("http://localhost:8081/producer/deleteProduct",{
                    method:"POST",
                    headers : {
                        'Content-type':'application/json',
                    },
                    body : this.numberOfProducts
                    }).then(responce=>{
                    return responce.text();
                    }).catch(error => {
                    console.error("Error:", error);
                    this.isError = true;
                });
            }
        },
        fitStageIntoParentContainer() {
            var container = window.getComputedStyle(document.getElementById('container'))

            // now we need to fit stage into parent container
            var containerWidth = parseInt(container.width)
            var containerHeight = parseInt(container.height)
            this.stageConfig.width = containerWidth
            this.stageConfig.height = containerHeight
        },
        clear() {
            this.handleOtherShapes(0)
            this.allshapes = []
            this.rectangles = []
            this.circles = []
            this.lines = []
            this.id = 0
        },
        handleLineMove(event){
          var item1ForWidth = 0
          var item2ForWidth = parseFloat(window.getComputedStyle(document.getElementById('container')).marginLeft)
          var item1ForHeight = document.querySelector('.control-header').offsetHeight
          var item2ForHeight = parseFloat(window.getComputedStyle(document.getElementById('container')).marginTop)
          var offsetHeightCanvas = item1ForHeight + item2ForHeight
          var offsetWidthCanvas = item1ForWidth + item2ForWidth

          if(this.shapeVariable == 3 && this.start == true){
            var line = ({
                points: [this.sourceX, this.sourceY, event.clientX - offsetWidthCanvas, event.clientY - offsetHeightCanvas],
                stroke: "black",
                strokeWidth: 10,
                lineCap: 'round',
                lineJoin: 'round',
                draggable: true,
                name: 'line',
                id: String(this.id)
            });
            this.lines.pop()
            this.lines.push(line)
          }
        },
        handleShapeClick(shapeConfig, event){
          var item1ForWidth = 0
          var item2ForWidth = parseFloat(window.getComputedStyle(document.getElementById('container')).marginLeft)
          var item1ForHeight = document.querySelector('.control-header').offsetHeight
          var item2ForHeight = parseFloat(window.getComputedStyle(document.getElementById('container')).marginTop)
          var offsetHeightCanvas = item1ForHeight + item2ForHeight
          var offsetWidthCanvas = item1ForWidth + item2ForWidth

          
          this.shapeConfigurations = shapeConfig
          var pts = []

          if(this.shapeVariable == 3){
            if(this.start == false){
              this.start = true
              this.sourceConfig = this.shapeConfigurations
              this.sourceShapeName = this.shapeConfigurations.name
              if(this.shapeConfigurations.name == "circle"){
                this.sourceX = this.shapeConfigurations.x
                this.sourceY = this.shapeConfigurations.y
              }else if (this.shapeConfigurations.name == "rectangle"){
                this.sourceX = this.shapeConfigurations.x + 100
                this.sourceY = this.shapeConfigurations.y + 50
              }
              
              pts.push(this.sourceX)
              pts.push(this.sourceY)

              pts.push(event.clientX - offsetWidthCanvas)
              pts.push(event.clientY - offsetWidthCanvas)
              var line = ({
                  points: pts,
                  stroke: "black",
                  strokeWidth: 10,
                  lineCap: 'round',
                  lineJoin: 'round',
                  draggable: true,
                  name: 'line',
                  id: String(this.id)
              });
              this.lines.push(line)

            }else {
                if(this.shapeConfigurations.name == "circle"){
                    if(this.shapeConfigurations.x == this.sourceX || this.sourceShapeName == "circle"){
                        this.handleOtherShapes(0)
                    }else{
                        
                        var yBig = this.sourceY - this.shapeConfigurations.y
                        var xBig = this.sourceX - this.shapeConfigurations.x
                        var length = Math.sqrt(Math.pow(xBig, 2) + Math.pow(yBig, 2));
                        var xNormal = xBig / length
                        var yNormal = yBig / length
                        var xResult = xNormal * this.shapeConfigurations.radius * 1.1
                        var yResult = yNormal * this.shapeConfigurations.radius * 1.1

                        var line = ({
                            points: [this.sourceX, this.sourceY, this.shapeConfigurations.x + xResult, this.shapeConfigurations.y + yResult],
                            stroke: "black",
                            strokeWidth: 10,
                            lineCap: 'round',
                            lineJoin: 'round',
                            draggable: false,
                            name: 'line',
                            id: String(this.id),
                            from: String(this.sourceConfig.id),
                            to: String(this.shapeConfigurations.id)
                        });
                        this.shapeConfigurations.draggable = false
                        this.sourceConfig.draggable = false
                        this.id = String((Number(this.id) + 1));
                        this.lines.pop()
                        this.lines.push(line)
                        this.allshapes.push(line)
                        this.handleOtherShapes(-1)

                    }
                    
                }else if (this.shapeConfigurations.name == "rectangle"){
                    if(this.shapeConfigurations.x == this.sourceX - 100 || this.sourceShapeName == "rectangle"){
                        this.handleOtherShapes(0)
                    }
                    else{
                        var point = this.rectangleIntersect(100, 50,
                            this.sourceX - (this.shapeConfigurations.x + 100) , this.sourceY - (this.shapeConfigurations.y + 50));

                        var line = ({
                            points: [this.sourceX, this.sourceY,
                                    (point && { x: point.x + this.shapeConfigurations.x + 100, y: point.y + this.shapeConfigurations.y + 50 }).x ,
                                    (point && { x: point.x + this.shapeConfigurations.x + 100, y: point.y + this.shapeConfigurations.y + 50 }).y ],
                            stroke: "black",
                            strokeWidth: 10,
                            lineCap: 'round',
                            lineJoin: 'round',
                            draggable: false,
                            name: 'line',
                            id: String(this.id),
                            from: String(this.sourceConfig.id),
                            to: String(this.shapeConfigurations.id)
                        });
                        this.shapeConfigurations.draggable = false
                        this.sourceConfig.draggable = false
                        this.id = String((Number(this.id) + 1));

                        this.lines.pop()
                        this.lines.push(line)
                        this.allshapes.push(line)
                        this.handleOtherShapes(-1)
                    }
                }else {
                    this.handleOtherShapes(0)
                }
            }
          }
        },
        rectangleIntersect(w, h, x, y) {
            var sx = x > 0 ? 1 : -1;
            var sy = y > 0 ? 1 : -1;

            x *= sx;
            y *= sy;

            if (x < w && y < h) return null;

            var m = x * h;
            var n = y * w;

            if (m < n) w = m / y;
            if (m > n) h = n / x;

            return { x: sx * w * 1.1 , y: sy * h * 1.1};
        },
        handleLine(){
            //for adjusting position of shapes
            if(this.shapeVariable != 3){
              document.getElementById('line').style.background = "#000000"
              document.getElementById('line').style.color= "#e9eef0"
              this.shapeVariable = 3
            }else {
              this.handleOtherShapes(0)
            }
        },
        createElement(event) {

            //for adjusting position of shapes
            var item1ForWidth = 0
            var item2ForWidth = parseFloat(window.getComputedStyle(document.getElementById('container')).marginLeft)
            var item1ForHeight = document.querySelector('.control-header').offsetHeight
            var item2ForHeight = parseFloat(window.getComputedStyle(document.getElementById('container')).marginTop)
            var offsetHeightCanvas = item1ForHeight + item2ForHeight
            var offsetWidthCanvas = item1ForWidth + item2ForWidth

            switch (this.shapeVariable) {
                case 1:
                    var rect = ({
                        x: event.clientX - offsetWidthCanvas - 100,
                        y: event.clientY - offsetHeightCanvas - 50,
                        width: 200,
                        height: 100,
                        fill: 'yellow',
                        stroke: 'black',
                        draggable: true,
                        strokeWidth: 3,
                        name: 'rectangle',
                        id: String(this.id)
                    });
                    this.id = String((Number(this.id) + 1))
                    this.rectangles.push(rect)
                    this.allshapes.push(rect)
                    break;
                case 2:
                    var circle = ({
                        x: event.clientX - offsetWidthCanvas,
                        y: event.clientY - offsetHeightCanvas,
                        radius: 60,
                        fill: '#05bc05',
                        stroke: 'black',
                        draggable: true,
                        strokeWidth: 3,
                        name: 'circle',
                        id: String(this.id)
                    });
                    this.id = String((Number(this.id) + 1));
                    this.circles.push(circle)
                    this.allshapes.push(circle)
                    break;
            }
            if(this.shapeVariable != 3){
              this.shapeVariable = 0
            }
        },
    }
}
</script>

<style>
body,
p {
    margin: 0;
    padding: 0
}


html {
    height: 100%;
}

body {
    height: 100%;
    align-items: center;
    justify-content: center;
    background: linear-gradient(to right, #b2d0e8, rgb(106, 133, 182));
}

.main-container {
    display: flex;
    width: 100%;
    justify-content: space-evenly;
}

section {
    width: 90%;
    height: calc(1200px * 0.6);
}

#container {
    height: 100%;
    background-color: #fefefe;
    margin: calc(15px * 0.6);
    margin-left: 2%;
    border-radius: calc(50px * 0.6);
}

.draw {
    margin-left: calc(100px * 0.6);
}

.title {
    align-content: center;
    align-items: center;
    justify-content: center;
    padding: calc(8px * 0.6) calc(40px * 0.6);
    font-weight: 1000;
    font-size: calc(55px * 0.6);
    color: #000000;
    text-align: center;
    font-family: Helvetica, sans-serif;
    display: flex;
    width: calc(60px * 0.6);
    border-radius: calc(10px * 0.6);
}

.products {
    align-content: center;
    align-items: center;
    justify-content: center;
    padding: calc(8px * 0.6) calc(40px * 0.6);
    font-weight: 1000;
    font-size: calc(45px * 0.6);
    color: #000000;
    text-align: center;
    font-family: Helvetica, sans-serif;
    display: flex;
    border-radius: calc(10px * 0.6);
}

.images {
    margin-right: calc(5px * 0.6);
    filter: drop-shadow(100%) contrast(100%) saturate(3000%);
    margin-left: calc(-10px * 0.6);
}

.shapes,
.options {
    width: 100%;
    padding: calc(5px * 0.6) 0;
    margin: calc(8px * 0.6);
    border-radius: calc(15px * 0.6);
    color: rgb(0, 0, 0);
    font-size: calc(30px * 0.6);
    font-weight: 500;
    cursor: pointer;
    background: #e9eef0;
    font-family: 'Signika Negative', sans-serif;
}

.shapes:hover,
.options:hover {
    background-color: rgb(0, 0, 0) !important;
    color: #e9eef0 !important;
}

.sidenavshape {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 10%;
    padding: calc(5px * 0.6) calc(10px * 0.6) calc(20px * 0.6);
    height: calc(800px * 0.6);
    margin: calc(130px * 0.6) calc(15px * 0.6) calc(15px * 0.6) calc(15px * 0.6);
    background: linear-gradient(to left, #394b9a 0%, #a7c9e0);
    padding-top: calc(20px * 0.6);
    border-radius: calc(50px * 0.6);
    border-style: ridge;
    border-color: black;
}

.sidenavshape button {
    padding: calc(16px * 0.6) calc(16px * 0.6);
    text-decoration: none;
    font-size: calc(30px * 0.6);
    color: black;
    display: block;
    text-align: center;
    align-items: center;
    justify-items: center;
}

.sidenavshape button:hover {
    color: #f1f1f1;
}

ul {
    padding-left: calc(5px * 0.6);
    margin: calc(2px * 0.6) 0px 0px calc(5px * 0.6);
}

li {
    display: flex;
    cursor: pointer;
    align-items: center;
}

.control-header {
    display: flex;
    align-items: center;
}

header {
    width: calc(900px * 0.6);
    height: calc(130px * 0.6);
    align-items: center;
    padding: calc(16px * 0.6);
    color: white;
    border-style: ridge;
    border-radius: calc(50px * 0.6);
    background: linear-gradient(to top, #394b9a 0%, #a7c9e0);
    margin: auto;
    border-color: black;

}

.productsbutton {
    width: calc(50px * 0.6);
    padding: calc(5px * 0.6) calc(5px * 0.6);
    margin-left: calc(250px * 0.6);
    border-radius: 100%;
    color: rgb(2, 0, 2);
    font-size: calc(35px * 0.6);
    font-weight: 500;
    cursor: pointer;
    background: #e9eef0;
    font-family: 'Signika Negative', sans-serif;
    border: none;
}

.productsbutton:hover {
    background-color: rgb(0, 0, 0);
    color: rgb(243, 255, 255);
}

.imagesleft {
    float: left;
    margin-left: auto;
    margin-right: auto;
    width: calc(700px * 0.6);
}

.imagesright {
    float: right;
    margin-left: auto;
    margin-right: auto;
    width: calc(700px * 0.6);
}

#clear {
    width: 72px;
}

.draw {
    display: flex;
    flex-direction: column;
}



</style>