package org.ergunkargun.couriertrackerapi.hateoas;

import org.ergunkargun.couriertrackerapi.controller.CourierController;
import org.ergunkargun.couriertrackerapi.jpa.entity.Store;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class StoreModelAssembler implements RepresentationModelAssembler<Store, EntityModel<Store>> {
    @Override
    public EntityModel<Store> toModel(Store store) {
        return EntityModel.of(store,
                linkTo(methodOn(CourierController.class).readCourier(store.getId())).withSelfRel(),
                linkTo(methodOn(CourierController.class).readCouriers()).withRel("stores"));
    }
}
