package com.fultil.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductCategory {
    ELECTRONICS("Electronics"),
    FASHION("Fashion"),
    HOME_AND_KITCHEN("Home and Kitchen"),
    SPORTS_AND_OUTDOORS("Sports and Outdoors"),
    BEAUTY_AND_PERSONAL_CARE("Beauty and Personal Care"),
    BABY_PRODUCTS("Baby Products"),
    TOYS_AND_GAMES("Toys and Games"),
    BOOKS("Books"),
    MUSIC_AND_MEDIA("Music and Media"),
    ART_AND_CRAFTS("Art and Crafts"),
    FOOD_AND_BEVERAGES("Food and Beverages");

    private final String description;
}
