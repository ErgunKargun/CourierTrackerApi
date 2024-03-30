package org.ergunkargun.couriertrackerapi.hateoas;

import org.ergunkargun.couriertrackerapi.controller.CourierController;
import org.ergunkargun.couriertrackerapi.jpa.entity.Courier;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CourierModelAssembler implements RepresentationModelAssembler<Courier, EntityModel<Courier>> {
    @Override
    public EntityModel<Courier> toModel(Courier courier) {
        return EntityModel.of(courier,
                linkTo(methodOn(CourierController.class).readCourier(courier.getId())).withSelfRel(),
                linkTo(methodOn(CourierController.class).readCouriers()).withRel("couriers"));
    }
}
