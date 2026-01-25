package com.eventdriven.source.stream;

import com.eventdriven.source.configuration.StreamConfiguration;
import com.eventdriven.source.dto.OrderDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "Source Controller",
        description = "Source Controller for manage order"
)
@RequiredArgsConstructor
@RequestMapping("api/source")
@RestController
public class SourceController {

    private final StreamConfiguration streamConfiguration;

    @Operation(
            summary = "Send order to stream",
            description = "Send order to stream"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Order sent to stream"
    )
    @PostMapping
    public void sourceSend(@RequestBody @Valid OrderDto orderDto)
    {
        streamConfiguration.sendOrder(orderDto);
    }


}
