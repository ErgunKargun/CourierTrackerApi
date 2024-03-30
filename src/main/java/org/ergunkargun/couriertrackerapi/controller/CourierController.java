package org.ergunkargun.couriertrackerapi.controller;

import org.ergunkargun.couriertrackerapi.hateoas.CourierModelAssembler;
import org.ergunkargun.couriertrackerapi.jpa.entity.Courier;
import org.ergunkargun.couriertrackerapi.service.CourierService;
import org.ergunkargun.couriertrackerapi.observe.CourierLogEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1")
public class CourierController {

    private final CourierService courierService;

    private final CourierModelAssembler courierModelAssembler;

    private final ApplicationEventPublisher eventPublisher;

    public CourierController(CourierService courierService, CourierModelAssembler courierModelAssembler, ApplicationEventPublisher eventPublisher) {
        this.courierService = courierService;
        this.courierModelAssembler = courierModelAssembler;
        this.eventPublisher = eventPublisher;
    }

    @PostMapping("/log/courier")
    public ResponseEntity<?> logCourier(@RequestBody Courier courier) {
        eventPublisher.publishEvent(new CourierLogEvent(this, courier));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/courier")
    public ResponseEntity<?> createCourier(@RequestBody Courier courier) {
        Courier createdCourier = courierService.create(courier);
        eventPublisher.publishEvent(new CourierLogEvent(this, createdCourier));
        var entityModel = courierModelAssembler.toModel(createdCourier);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @GetMapping("/couriers")
    public ResponseEntity<?> readCouriers() {
        List<EntityModel<Courier>> couriers = courierService.read().stream().map(courierModelAssembler::toModel).toList();
        var collectionModel = CollectionModel.of(couriers, linkTo(methodOn(this.getClass()).readCouriers()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/couriers/{id}")
    public ResponseEntity<?> readCourier(@PathVariable Long id) {
        Courier courier = courierService.read(id);
        var entityModel = courierModelAssembler.toModel(courier);
        return ResponseEntity.ok(entityModel);
    }

    @GetMapping("/courier-distance/{id}")
    public ResponseEntity<?> readCourierDistance(@PathVariable Long id) {
        Courier courier = courierService.read(id);
        return ResponseEntity.ok(courier.getDistance());
    }

    @PutMapping("/courier")
    public ResponseEntity<?> updateCourier(@RequestBody Courier courier) {
        Courier updatedCourier = courierService.update(courier);
        var entityModel = courierModelAssembler.toModel(updatedCourier);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @DeleteMapping("/courier/{id}")
    public ResponseEntity<?> deleteCourier(@PathVariable Long id) {
        courierService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
