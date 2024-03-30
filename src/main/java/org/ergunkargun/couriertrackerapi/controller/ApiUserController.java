package org.ergunkargun.couriertrackerapi.controller;

import org.ergunkargun.couriertrackerapi.hateoas.ApiUserModelAssembler;
import org.ergunkargun.couriertrackerapi.jpa.entity.ApiUser;
import org.ergunkargun.couriertrackerapi.service.ApiUserService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1")
public class ApiUserController {

    private final ApiUserService apiUserService;

    private final ApiUserModelAssembler apiUserModelAssembler;

    public ApiUserController(ApiUserService ApiUserService, ApiUserModelAssembler ApiUserModelAssembler) {
        this.apiUserService = ApiUserService;
        this.apiUserModelAssembler = ApiUserModelAssembler;
    }

    @PostMapping("/user")
    public ResponseEntity<?> createApiUser(@RequestBody ApiUser ApiUser) {
        ApiUser createdApiUser = apiUserService.create(ApiUser);
        var entityModel = apiUserModelAssembler.toModel(createdApiUser);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @GetMapping("/user")
    public ResponseEntity<?> readApiUsers() {
        List<EntityModel<ApiUser>> apiUsers = apiUserService.read().stream().map(apiUserModelAssembler::toModel).toList();
        var collectionModel = CollectionModel.of(apiUsers, linkTo(methodOn(this.getClass()).readApiUsers()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> readApiUser(@PathVariable Long id) {
        ApiUser apiUser = apiUserService.read(id);
        var entityModel = apiUserModelAssembler.toModel(apiUser);
        return ResponseEntity.ok(entityModel);
    }

    @GetMapping("/users/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal UserDetails userDetails) {
        ApiUser apiUser = apiUserService.read(userDetails.getUsername());
        var entityModel = apiUserModelAssembler.toModel(apiUser);
        return ResponseEntity.ok(entityModel);
    }

    @PutMapping("/user")
    public ResponseEntity<?> updateApiUser(@RequestBody ApiUser ApiUser) {
        ApiUser updatedApiUser = apiUserService.update(ApiUser);
        var entityModel = apiUserModelAssembler.toModel(updatedApiUser);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteApiUser(@PathVariable Long id) {
        apiUserService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
