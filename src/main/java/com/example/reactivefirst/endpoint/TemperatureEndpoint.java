package com.example.reactivefirst.endpoint;

import com.example.reactivefirst.data.Temperature;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/temperature")
public class TemperatureEndpoint {

    private final Temperature temperature = new Temperature(0);
    private final Temperature temperature1 = new Temperature(1);
    private final Temperature temperature4 = new Temperature(4);
    private final List<Temperature> temperatures = new ArrayList<>(List.of(temperature1, temperature4));

    @GetMapping
    Flux<Temperature> get() {
       // MongoDatabase database = client.getDatabase("households");
        return Flux.interval(Duration.ofSeconds(1))
                //read from mongodb
                .map(event -> temperature);
    }

    @PostMapping("/{target}")
    Mono<Temperature> set(@PathVariable int target) {
        temperature.setTemperature(target);
        //mongodb insert/overwrite
        return Mono.just(temperature);
    }

    @CrossOrigin
    @GetMapping("/all")
    Flux<Temperature> getAll() {
        return Flux.just(temperatures.toArray(Temperature[]::new));
    }

    @PostMapping("/all")
    Mono<Temperature> newTemp(@RequestBody int temperature) {
        Temperature newTemperature = new Temperature(temperature);
        temperatures.add(newTemperature);
        return Mono.just(newTemperature);
    }
}
