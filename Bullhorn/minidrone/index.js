const Drone = require('parrot-minidrone');
const drone = new Drone({
    autoconnect: true,
});
 
drone.on('connected', () => drone.takeOff());

/*
const flightParams = {
        yaw: 0,
        pitch: 0,
        roll: 0,
        altitude: 0,
};

setTimeout(() => {
        drone.setFlightParams({
            yaw: 0,
            pitch: 0,
            roll: 0,
            altitude: 0,
        });
    }, 2000);
    
flightParams.pitch = -70;


drone.setFlightParams(flightParams);
    timeout = setTimeout(() => {
        drone.setFlightParams({
            yaw: 0,
            pitch: 0,
            roll: 0,
            altitude: 0,
        });
    }, 2000);
    
    
flightParams.altitude = 500;

drone.setFlightParams(flightParams);
    timeout = setTimeout(() => {
        drone.setFlightParams({
            yaw: 0,
            pitch: 0,
            roll: 0,
            altitude: 0,
        });
    }, 5000);
*/
    
setTimeout(() => {
    drone.on('flightStatusChange', (status) => {
    if (status === 'hovering') {
        drone.land();
        process.exit();
    }
	});
}, 20000);
