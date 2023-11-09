package com.lepl.api.character;

import com.lepl.Service.character.CharacterItemService;
import com.lepl.Service.character.CharacterService;
import com.lepl.Service.character.ExpService;
import com.lepl.Service.character.ItemService;
import com.lepl.domain.character.Item;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/item")
@Slf4j
public class ItemApiController {

    private final ItemService itemService;
    private final ExpService expService;
    private final CharacterService characterService;
    private final CharacterItemService characterItemService;

    /*
        등록된 특정 아이템 조회
        Item 형태로 받기
     */
    @GetMapping("/{itemId}")
    public Item findItem(@PathVariable("itemId") Long itemId) {
        Item item = itemService.findOne(itemId);
        log.debug("item = {}", item);
        log.debug("item id = {}", itemId);
        return item;
    }

    /*
        등록된 전체 아이템 조회
        List 형태로 받기
     */
    @GetMapping("/all")
    public List<ItemDto> findItems() {
        List<Item> items = itemService.findAll();
        List<ItemDto> result = items.stream()
                .map(o -> new ItemDto(o))
                .collect(Collectors.toList());
        return result;
    }

    @Getter
    static class ItemDto {
        private Long itemId;
        private String type;
        private String name;
        private int price;
        private int purchase_quantity;
        private LocalDateTime start_time;
        private LocalDateTime end_time;

        public ItemDto(Item item) {
            itemId = item.getId();
            type = item.getType();
            name = item.getName();
            price = item.getPrice();
            purchase_quantity = item.getPurchase_quantity();
            start_time = item.getStart_time();
            end_time = item.getEnd_time();
        }
    }


}
