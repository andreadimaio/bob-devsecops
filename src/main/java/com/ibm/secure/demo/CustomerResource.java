package com.ibm.secure.demo;

import java.util.List;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.annotation.security.PermitAll;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("/users")
public class CustomerResource {

    record RegisterRequest(String username, String password) {};

    @GET
    @Path("/search")
    @PermitAll
    public List<Customer> searchCustomers(@QueryParam("query") String query) {
        if (query == null || query.isBlank()) {
            return List.of();
        }
        String pattern = "%" + query.strip().toLowerCase() + "%";
        return Customer.find("LOWER(fullName) LIKE ?1", pattern).list();
    }

    @POST
    @Path("/register")
    @PermitAll
    @Transactional
    public Response registerUser(RegisterRequest request) {

        if (request.username == null || request.username.isBlank()) {
            return Response.status(400).entity("{\"error\": \"Username obbligatorio\"}").build();
        }
        if (request.password == null || request.password.isBlank()) {
            return Response.status(400).entity("{\"error\": \"Password obbligatoria\"}").build();
        }

        UserAccount account = new UserAccount();
        account.username = request.username;
        account.password = BcryptUtil.bcryptHash(request.password);
        account.role = "USER";
        account.persist();

        return Response.status(201)
            .entity("{\"message\": \"Utente registrato con successo\"}")
            .build();
    }
}