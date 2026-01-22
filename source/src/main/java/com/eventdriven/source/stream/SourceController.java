package com.eventdriven.source.stream;

import com.eventdriven.source.configuration.StreamConfiguration;
import com.eventdriven.source.dto.OrderDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("api/source")
@RestController
public class SourceController {

    private final StreamConfiguration streamConfiguration;

    @PostMapping
    public void sourceSend(@RequestBody @Valid OrderDto orderDto)
    {
        streamConfiguration.sendOrder(orderDto);
    }


}
