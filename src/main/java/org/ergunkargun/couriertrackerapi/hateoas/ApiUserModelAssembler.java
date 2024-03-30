package org.ergunkargun.couriertrackerapi.hateoas;

import org.ergunkargun.couriertrackerapi.controller.ApiUserController;
import org.ergunkargun.couriertrackerapi.controller.AuthController;
import org.ergunkargun.couriertrackerapi.jpa.entity.ApiUser;
import org.ergunkargun.couriertrackerapi.model.Auth;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ApiUserModelAssembler implements RepresentationModelAssembler<ApiUser, EntityModel<ApiUser>> {
    @Override
    public EntityModel<ApiUser> toModel(ApiUser apiUser) {
        return EntityModel.of(apiUser,
                linkTo(methodOn(ApiUserController.class).readApiUser(apiUser.getId())).withSelfRel(),
                linkTo(methodOn(ApiUserController.class).readApiUsers()).withRel("users"),
                linkTo(methodOn(AuthController.class).register(Auth.builder().username(apiUser.getUsername()).build())).withRel("register"),
                linkTo(methodOn(AuthController.class).signIn(Auth.builder().username(apiUser.getUsername()).build())).withRel("sign-in"));
    }
}

