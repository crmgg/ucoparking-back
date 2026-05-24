package co.edu.uco.ucoparking.infraestructure.controller.catalog;

import co.edu.uco.ucoparking.infraestructure.controller.catalog.dto.CatalogEntryResponse;
import co.edu.uco.ucoparking.infraestructure.service.MessageCatalogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/messages")
public class MessageCatalogController {

    private final MessageCatalogService messageCatalogService;

    public MessageCatalogController(MessageCatalogService messageCatalogService) {
        this.messageCatalogService = messageCatalogService;
    }

    @GetMapping("/{code}")
    public Mono<CatalogEntryResponse> getMessage(
            @PathVariable String code,
            @RequestParam(defaultValue = "es") String locale) {
        return Mono.fromCallable(() -> {
            var response = new CatalogEntryResponse();
            response.setCode(code);
            response.setLocale(locale);
            response.setValue(messageCatalogService.getMessage(code, locale));
            return response;
        });
    }
}
