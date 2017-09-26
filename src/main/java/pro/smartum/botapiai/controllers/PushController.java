package pro.smartum.botapiai.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.smartum.botapiai.dto.PushDeviceRegistrationDto;
import pro.smartum.botapiai.dto.PushDeviceUnRegistrationDto;
import pro.smartum.botapiai.services.PushService;

@RestController
@RequestMapping("/pushes")
public class PushController {

    @Autowired
    private PushService pushService;

    @PostMapping("/device")
    public ResponseEntity<Object> registerDevice(@RequestBody PushDeviceRegistrationDto registrationDto) {
        pushService.registerDevice(registrationDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/device")
    public ResponseEntity<Object> unregisterDevice(@RequestBody PushDeviceUnRegistrationDto unRegistrationDto) {
        pushService.unregisterDevice(unRegistrationDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
