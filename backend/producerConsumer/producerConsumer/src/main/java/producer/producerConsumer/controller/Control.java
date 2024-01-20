package producer.producerConsumer.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import producer.producerConsumer.services.ServiceFacade;
import producer.producerConsumer.services.models.Element;
import producer.producerConsumer.services.models.ElementDto;
import producer.producerConsumer.services.models.Link;
import producer.producerConsumer.services.simulation.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController()
@CrossOrigin("*")
@RequestMapping("/producer")
public class Control {
    @Autowired
    private ServiceFacade s ;

    @PostMapping("/simulation")
    public void simulation(@RequestBody List<ElementDto> dtos) throws InterruptedException {
//            ServiceFacade s = new ServiceFacade() ;
            System.out.println("arrive");
            s.startSimulation(dtos) ;
            System.out.println("end simulation");
            return ;
    }
    @PostMapping("/addProduct")
    public void addProduct(){
        s.addProduct();
    }

    @PostMapping("/updateState")
    public List<ElementDto> updateState(){
        List<ElementDto> dtos = new ArrayList<>() ;
        for (Map.Entry<String, Element> entry : s.getGraph().getElements().entrySet()){
            ElementDto dto = new ElementDto();
            dto.id = entry.getValue().getId();
            dto.color = entry.getValue().getColor();
            dto.x = entry.getValue().getX();
            dto.y = entry.getValue().getY();
            dto.name = entry.getValue().getName();
            if(entry.getValue().getName().equals("line")){
                dto.points = ((Link)entry.getValue()).getPoints();
                dto.to = ((Link)entry.getValue()).getTo();
                dto.from = ((Link)entry.getValue()).getFrom();
            }
            dto.text = entry.getValue().getText();
            dtos.add(dto);
        }
    return dtos ;
    }


}
