package producer.producerConsumer.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import producer.producerConsumer.services.ServiceFacade;
import producer.producerConsumer.services.models.ElementDto;
import producer.producerConsumer.services.simulation.Graph;

import java.util.List;

@RestController()
@CrossOrigin("*")
@RequestMapping("/producer")
public class Control {
    @Autowired
    private ServiceFacade s ;

    @PostMapping("/simulation")
    public ResponseEntity<Graph> simulation(@RequestBody List<ElementDto> dtos) {
        try {
//            ServiceFacade s = new ServiceFacade() ;
            System.out.println("arrive");
            s.startSimulation(dtos) ;
            System.out.println("end simulation");
            return ResponseEntity.ok(s.getGraph());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
