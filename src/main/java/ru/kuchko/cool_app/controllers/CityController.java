package ru.kuchko.cool_app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kuchko.cool_app.dto.city.CityDto;
import ru.kuchko.cool_app.dto.city.CityUploadDto;
import ru.kuchko.cool_app.entities.CityEntity;
import ru.kuchko.cool_app.exceptions.EntityNoFoundByIdException;
import ru.kuchko.cool_app.repositories.CityRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cities")
public class CityController {
    private final CityRepository cityRepository;
    @GetMapping
    public ResponseEntity<List<CityDto>> getCities() {
        List<CityDto> cities = cityRepository.findAll()
                .stream().map(CityDto::from).toList();
        return ResponseEntity.ok(cities);
    }

    @PostMapping
    public ResponseEntity<CityDto> createCity(@RequestBody CityUploadDto cityUploadDto) {
        CityEntity city = new CityEntity(null, cityUploadDto.getName());
        CityEntity savedCity = cityRepository.save(city);
        return ResponseEntity.ok(CityDto.from(savedCity));
    }

    @PutMapping("/{cityId}")
    public ResponseEntity<CityDto> createCity(@RequestBody CityUploadDto cityUploadDto,
                                              @PathVariable Integer cityId) {
        CityEntity city = cityRepository.findById(cityId)
                .orElseThrow(() -> new EntityNoFoundByIdException("city", cityId));
        city.setName(cityUploadDto.getName());
        CityEntity savedCity = cityRepository.save(city);
        return ResponseEntity.ok(CityDto.from(savedCity));
    }

    @DeleteMapping("/{cityId}")
    public ResponseEntity<Void> deleteCity(@PathVariable Integer cityId) {
        cityRepository.deleteById(cityId);
        return ResponseEntity.ok().build();
    }


}
