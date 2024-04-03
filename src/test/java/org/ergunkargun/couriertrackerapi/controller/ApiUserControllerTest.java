package org.ergunkargun.couriertrackerapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ergunkargun.couriertrackerapi.hateoas.ApiUserModelAssembler;
import org.ergunkargun.couriertrackerapi.jpa.entity.ApiUser;
import org.ergunkargun.couriertrackerapi.jpa.entity.enumaration.Role;
import org.ergunkargun.couriertrackerapi.model.Auth;
import org.ergunkargun.couriertrackerapi.service.ApiUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = ApiUserController.class)
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureRestDocs
public class ApiUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    Environment environment;

    @MockBean
    private ApiUserService apiUserService;

    @MockBean
    private ApiUserModelAssembler apiUserModelAssembler;

    public EntityModel<ApiUser> toModel(ApiUser apiUser) {
        return EntityModel.of(apiUser,
                linkTo(methodOn(ApiUserController.class).readApiUser(apiUser.getId())).withSelfRel(),
                linkTo(methodOn(ApiUserController.class).readApiUsers()).withRel("users"),
                linkTo(methodOn(AuthController.class).register(Auth.builder().username(apiUser.getUsername()).build())).withRel("register"),
                linkTo(methodOn(AuthController.class).signIn(Auth.builder().username(apiUser.getUsername()).build())).withRel("sign-in"));
    }

    @BeforeEach
    public void beforeEach() {
        given(apiUserModelAssembler.toModel(ArgumentMatchers.any(ApiUser.class)))
                .willAnswer(invocation -> toModel(invocation.getArgument(0)));

        given(apiUserService.read())
                .willReturn(
                        List.of(
                                ApiUser.builder()
                                        .username(environment.getProperty("spring.security.user.name"))
                                        .password(environment.getProperty("spring.security.user.password"))
                                        .email("admin@mail.com")
                                        .role(Role.ADMIN)
                                        .build(),
                                ApiUser.builder()
                                        .username(environment.getProperty("api.user-name"))
                                        .password(environment.getProperty("api.user-password"))
                                        .email("user@mail.com")
                                        .role(Role.USER)
                                        .build()
                        )
                );
    }

    @Test
    public void whenGetApiUsers_thenReturnsApiUsers() throws Exception {
        mockMvc
                .perform(get("/v1/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("admin")))
                .andDo(document("api-users"));
    }
}
