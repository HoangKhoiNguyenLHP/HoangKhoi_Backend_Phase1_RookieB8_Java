package nh.khoi.ecommerce.utils;

import java.util.function.Function;

public class SlugUtil
{
    public static String toSlug(String input) {
        return input.toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");
    }

    public static String generateUniqueSlug(String baseSlug, Function<String, Boolean> slugExisted) {
        String slug = baseSlug;
        int counter = 1;

        while(slugExisted.apply(slug)) {
            slug = baseSlug + "-" + counter;
            ++counter;
        }
        return slug;
    }
}