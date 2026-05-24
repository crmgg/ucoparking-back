package co.edu.uco.ucoparking.infraestructure.controller.catalog;

import co.edu.uco.ucoparking.infraestructure.controller.catalog.dto.ParameterEntryResponse;
import co.edu.uco.ucoparking.infraestructure.service.ParameterCatalogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/parameters")
public class ParameterCatalogController {

    private final ParameterCatalogService parameterCatalogService;

    public ParameterCatalogController(ParameterCatalogService parameterCatalogService) {
        this.parameterCatalogService = parameterCatalogService;
    }

    @GetMapping("/{key}")
    public Mono<ParameterEntryResponse> getParameter(@PathVariable String key) {
        return Mono.fromCallable(() -> parameterCatalogService.getParameter(key));
    }
}
