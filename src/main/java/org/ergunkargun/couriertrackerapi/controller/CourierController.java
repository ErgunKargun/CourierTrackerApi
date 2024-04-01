package org.ergunkargun.couriertrackerapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.ergunkargun.couriertrackerapi.hateoas.CourierModelAssembler;
import org.ergunkargun.couriertrackerapi.jpa.entity.Courier;
import org.ergunkargun.couriertrackerapi.service.CourierService;
import org.ergunkargun.couriertrackerapi.observe.CourierLogEvent;
import org.ergunkargun.couriertrackerapi.service.StoreService;
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

    private final StoreService storeService;

    public CourierController(CourierService courierService, CourierModelAssembler courierModelAssembler, ApplicationEventPublisher eventPublisher, StoreService storeService) {
        this.courierService = courierService;
        this.courierModelAssembler = courierModelAssembler;
        this.eventPublisher = eventPublisher;
        this.storeService = storeService;
    }

    private void publishEvent(Courier courier) {
        var stores = storeService.read();
        for (var store : stores) {
            eventPublisher.publishEvent(new CourierLogEvent(this, store, courier));
        }
    }

    @PostMapping("/log/courier")
    public ResponseEntity<?> logCourier(@RequestBody Courier courier) {
        publishEvent(courier);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/courier")
    public ResponseEntity<?> createCourier(@RequestBody Courier courier) {
        Courier createdCourier = courierService.create(courier);
        publishEvent(createdCourier);
        var entityModel = courierModelAssembler.toModel(createdCourier);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @GetMapping("/couriers")
    public ResponseEntity<?> readCouriers() {
        List<EntityModel<Courier>> couriers = courierService.read().stream().map(courierModelAssembler::toModel).toList();
        var collectionModel = CollectionModel.of(couriers, linkTo(methodOn(this.getClass()).readCouriers()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Get courier by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            implementation = Courier.class
                                    )
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Courier not found",
                    content = @Content
            )
    }
    )
    @GetMapping("/couriers/{id}")
    public ResponseEntity<?> readCourier(@Parameter(description = "id of courier") @PathVariable Long id) {
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
