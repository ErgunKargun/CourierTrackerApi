package org.ergunkargun.couriertrackerapi.controller;

import org.ergunkargun.couriertrackerapi.hateoas.StoreModelAssembler;
import org.ergunkargun.couriertrackerapi.jpa.entity.Store;
import org.ergunkargun.couriertrackerapi.service.StoreService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class StoreController {

    private final StoreService storeService;

    private final StoreModelAssembler storeModelAssembler;

    public StoreController(StoreService storeService, StoreModelAssembler storeModelAssembler) {
        this.storeService = storeService;
        this.storeModelAssembler = storeModelAssembler;
    }

    @PostMapping("/store")
    public ResponseEntity<?> createStore(@RequestBody Store store) {
        Store createdstore = storeService.create(store);
        var entityModel = storeModelAssembler.toModel(createdstore);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @GetMapping("/stores")
    public ResponseEntity<?> readStores() {
        List<EntityModel<Store>> stores = storeService.read().stream().map(storeModelAssembler::toModel).toList();
        var collectionModel = CollectionModel.of(stores, linkTo(methodOn(this.getClass()).readStores()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/stores/{id}")
    public ResponseEntity<?> readStore(@PathVariable Long id) {
        Store store = storeService.read(id);
        var entityModel = storeModelAssembler.toModel(store);
        return ResponseEntity.ok(entityModel);
    }

    @PutMapping("/store")
    public ResponseEntity<?> updateStore(@RequestBody Store store) {
        Store updatedstore = storeService.update(store);
        var entityModel = storeModelAssembler.toModel(updatedstore);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @DeleteMapping("/store/{id}")
    public ResponseEntity<?> deleteStore(@PathVariable Long id) {
        storeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
