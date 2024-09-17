package ru.kuchko.cool_app.controllers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kuchko.cool_app.dto.tag.TagDto;
import ru.kuchko.cool_app.dto.tag.TagUploadDto;
import ru.kuchko.cool_app.entities.TagEntity;
import ru.kuchko.cool_app.exceptions.EntityNoFoundByIdException;
import ru.kuchko.cool_app.repositories.TagRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagRepository tagRepository;

    @GetMapping
    public ResponseEntity<List<TagDto>> getTags() {
        return ResponseEntity.ok(tagRepository.findAll().stream().map(TagDto::from).toList());
    }

    @PostMapping
    @Transactional
    public ResponseEntity<TagDto> createTag(@RequestBody TagUploadDto tagUploadDto) {
        return ResponseEntity.ok(TagDto.from(
                tagRepository.save(new TagEntity(null, tagUploadDto.getTitle()))
        ));
    }

    @PutMapping("/{tagId}")
    @Transactional
    public ResponseEntity<TagDto> updateTag(@RequestBody TagUploadDto tagUploadDto,
                                            @PathVariable Integer tagId) {
        TagEntity tag = tagRepository.findById(tagId).orElseThrow(
                () -> new EntityNoFoundByIdException("tag", tagId)
        );
        tag.setTitle(tagUploadDto.getTitle());
        return ResponseEntity.ok(TagDto.from(tagRepository.save(tag)));
    }

    @DeleteMapping("/{tagId}")
    @Transactional
    public ResponseEntity<Void> deleteTag(@PathVariable Integer tagId) {
        tagRepository.deleteById(tagId);
        return ResponseEntity.ok().build();
    }
}
