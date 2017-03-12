const keypress = require('keypress');
const Drone = require('parrot-minidrone');
var Array = require('node-array');
const drone = new Drone({
    autoconnect: true,
});
let timeout = null;

var data2;
var negative = false;
var valor = 0;
var array = [];
var valor = 0;
var digit = 0;
var minus = false;
var n = 0;
var available = false;
var ax, ay, az, gx, gy, gz;
var ax_ = 0;
var ay_ = 0;
var az_ = 0;
var gx_ = 0;
var gy_ = 0;
var gz_ = 0;
var lastTime = 1000000;
var preto = false;
var lastIsButton = false;
var gradX = 0;
var gradY = 0;
var gradZ = 0;
var first = true;

var pitch = 0;
var roll = 0;
var yaw = 0;
var velocidad = 15;

var SerialPort = require("serialport");
	//var serialport = new SerialPort("/dev/tty.usbmodem1411");
	var serialport = new SerialPort("/dev/cu.HC-06-DevB");
	//var serialport = new SerialPort("/dev/cu.Bluetooth-Incoming-Port");
	serialport.on('open', function(){
		const flightParams = {
	        yaw: 0,
	        pitch: 0,
	        roll: 0,
	        altitude: 0,
	    };

		console.log('Serial Port Opened');
			serialport.on('data', function(data){
				digit = data[0];
				//console.log(digit);
				if (digit >= 0 && digit <= 9) {
					valor = valor * 10 + digit;
				} else if (digit == 44) {	//44 = ','
					//console.log(valor);
					if (minus) {
						valor = -valor;
						//console.log(array[array.length - 1]);
					}
					switch (n) {
						//case 0: ax = valor; break;
						//case 1: ay = valor; break;
						//case 2: az = valor; break;
						case 0: gx = valor; break;
						case 1: gy = valor; break;
						case 2: gz = valor; break;
					}
					//console.log(valor);
					
					minus = false;
					valor = 0;
					if(n >= 2){
						n = 0;
						//console.log(gx);
						//console.log(gy);
						//console.log(gz);
						if(first){
							gradX = gx;
							gradY = gy;
							gradZ = gz;
							first = false;
						} else {
							available = true;
						
							if (Math.abs(gx - gradX) > 5000){
								
								if(gradX < gx){//restar
									console.log("Giro sobre X hacia atras");
									console.log(gx);
									
									pitch -= velocidad;
									flightParams.pitch = - velocidad;
									
								}else{	//sumar
									console.log("Giro sobre X hacia adelante");
									console.log(gx);
									pitch += velocidad;
									flightParams.pitch = velocidad;
								}
								//console.log("muevo x");
								//gradX = gx;
							}						
							
							
							if (Math.abs(gy - gradY) > 3000){
								if(gradY > gy){//restar
									console.log("Giro sobre Y hacia atras");
									roll -= velocidad;
									flightParams.roll = -velocidad;
									
								}else{	//sumar
									console.log("Giro sobre Y hacia adelante");
									roll += velocidad;
									flightParams.roll = velocidad;
								}
								//console.log("muevo Y");	
								//gradY = gy;
							}
							
							
							if (Math.abs(gz - gradZ) > 5000){
								if(gradZ > gz){//restar
									console.log("Giro sobre z Hacia atras");
									yaw -= velocidad;
									flightParams.yaw = -velocidad;
									
								}else{	//sumar
									console.log("Giro sobre Z hacia adelante");
									yaw += velocidad;
									flightParams.yaw = velocidad;
								}
								//console.log("muevo z");
								//gradY = gy;
							}
						
							drone.setFlightParams(flightParams);
							
							//flightParams.pitch = ;
							//flightParams.roll = ;
							//flightParams.altitude = ;
							//flightParams.yaw = ;
							    						/*
							setTimeout(function() {
															drone.setFlightParams(flightParams);
							    								timeout = setTimeout(() => {
							    									drone.setFlightParams({
							    										yaw: 0,
							    										pitch: 0,
							    										roll: 0,
							    										altitude: 0,
							    										});
							    										}, 400);
							    							
							    						}, 500);
							*/
						}
						//console.log("then no mayor que dos parametros");
					} else {
						//console.log("Else del if mayor que 2 parametros");
						n++;
					}
					
					
				} else if (digit == 45){ //45 = '-'
					minus = true;
				}else if(digit == 11){ //11 igual a boton
					var timeC = Date.now()/100;
					if(!preto){
						lastTime = timeC;
						preto = true;
					}else{
						if(timeC - lastTime < 10){	//medio segundo
							drone.takeoffOrLand();
							flightParams.pitch = 0;
							flightParams.roll = 0;
							flightParams.altitude = 0;
							flightParams.yaw = 0;
							drone.setFlightParams(flightParams);
							
							//console.log("Aterrizo");
						}else{
							flightParams.pitch = 0;
							flightParams.roll = 0;
							flightParams.altitude = 0;
							flightParams.yaw = 0;
							drone.setFlightParams(flightParams);
						}
						preto = false;
						//
						
						//console.log(timeC - lastTime);
					}
					
				}
				//console.log(array);
				
				//array.push(data[0]);
			});
			
	});
	
/* 	for(//var i = 0; i < 6;i++){ */
        //(function(){
        //    //var j = i;
        //    //console.log("Loading message %d".green, j);
        //    //htmlMessageboardString += MessageToHTMLString(BoardMessages[j]);
        //    
    //}	//
	
	



keypress(process.stdin);

// listen for the "keypress" event
process.stdin.on('keypress', (ch, key) => {

    const keyName = key && key.name ? key.name : false;
    const inputSensitivity = 70;
	
	
		
	
    if (!keyName) {
        return;
    }
    if (timeout) {
        clearTimeout(timeout);
        timeout = null;
    }

    const flightParams = {
        yaw: 0,
        pitch: 0,
        roll: 0,
        altitude: 0,
    };

    switch (keyName) {
    case 'up':
        flightParams.pitch = inputSensitivity;
        break;
    case 'down':
        flightParams.pitch = -inputSensitivity;
        break;
    case 'left':
        flightParams.roll = -inputSensitivity;
        break;
    case 'right':
        flightParams.roll = inputSensitivity;
        break;
    case 'w':
        flightParams.altitude = inputSensitivity;
        break;
    case 's':
        flightParams.altitude = -inputSensitivity;
        break;
    case 'a':
        flightParams.yaw = -inputSensitivity;
        break;
    case 'd':
        flightParams.yaw = inputSensitivity;
        break;
    case 't':
        drone.takeoffOrLand();
        break;
    case 'f':
        drone.trim();
        break;
    case 'escape':
        drone.emergency();
        break;
    case 'c':
        process.exit();
        break;
    default:
        break;
    }

    drone.setFlightParams(flightParams);
    timeout = setTimeout(() => {
        drone.setFlightParams({
            yaw: 0,
            pitch: 0,
            roll: 0,
            altitude: 0,
        });
    }, 400);
});

process.stdin.setRawMode(true);
process.stdin.resume();
