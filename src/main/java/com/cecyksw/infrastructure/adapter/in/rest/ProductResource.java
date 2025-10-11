package com.cecyksw.infrastructure.adapter.in.rest;

import com.cecyksw.domain.model.Product;
import com.cecyksw.domain.port.in.CreateProductUseCase;
import com.cecyksw.domain.port.in.DeleteProductUseCase;
import com.cecyksw.domain.port.in.GetProductUseCase;
import com.cecyksw.domain.port.in.UpdateProductUseCase;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

/**
 * REST adapter (controller) for product operations.
 * This is an input adapter that exposes our use cases through HTTP.
 */
@Path("/api/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Products", description = "Product management endpoints")
public class ProductResource {

    private final CreateProductUseCase createProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;

    @Inject
    public ProductResource(
            CreateProductUseCase createProductUseCase,
            GetProductUseCase getProductUseCase,
            UpdateProductUseCase updateProductUseCase,
            DeleteProductUseCase deleteProductUseCase) {
        this.createProductUseCase = createProductUseCase;
        this.getProductUseCase = getProductUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
    }

    @POST
    @Operation(summary = "Create a new product")
    @APIResponse(responseCode = "201", description = "Product created successfully")
    public Response createProduct(Product product) {
        Product created = createProductUseCase.createProduct(product);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    @Operation(summary = "Get all products")
    @APIResponse(responseCode = "200", description = "List of all products")
    public List<Product> getAllProducts() {
        return getProductUseCase.getAllProducts();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get a product by ID")
    @APIResponse(responseCode = "200", description = "Product found")
    @APIResponse(responseCode = "404", description = "Product not found")
    public Response getProductById(@PathParam("id") Long id) {
        return getProductUseCase.getProductById(id)
                .map(product -> Response.ok(product).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update a product")
    @APIResponse(responseCode = "200", description = "Product updated successfully")
    @APIResponse(responseCode = "404", description = "Product not found")
    public Response updateProduct(@PathParam("id") Long id, Product product) {
        return updateProductUseCase.updateProduct(id, product)
                .map(updated -> Response.ok(updated).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a product")
    @APIResponse(responseCode = "204", description = "Product deleted successfully")
    @APIResponse(responseCode = "404", description = "Product not found")
    public Response deleteProduct(@PathParam("id") Long id) {
        boolean deleted = deleteProductUseCase.deleteProduct(id);
        return deleted
                ? Response.noContent().build()
                : Response.status(Response.Status.NOT_FOUND).build();
    }
}
