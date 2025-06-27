package com.example.cinemate.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringUtilTest {

    @Test
    void capitalizeFirstLetter_ShouldCapitalizeLowercase() {
        assertEquals(
                "Daniil",
                StringUtil.capitalizeFirstLetter("daniil")
        );
    }

    @Test
    void capitalizeFirstLetter_ShouldNotChangeIfAlreadyCapitalized() {
        assertEquals(
                "Daniil",
                StringUtil.capitalizeFirstLetter("Daniil")
        );
    }

    @Test
    void capitalizeFirstLetter_EmptyStringOrNull() {
        assertEquals(
                "",
                StringUtil.capitalizeFirstLetter("")
        );
        assertNull(StringUtil.capitalizeFirstLetter(null));
    }


    @Test
    void getUsernameFromEmail_ShouldNameFromEmail() {
        assertEquals(
                "dan",
                StringUtil.getUsernameFromEmail("dan@gmail.com")
        );
    }

    @Test
    void getUsernameFromEmail_ShouldReturnDefaultUserValue() {
        assertEquals(
                "user",
                StringUtil.getUsernameFromEmail("daniil")
        );
    }

    @Test
    void getUsernameFromEmail_EmptyStringOrNull() {
        assertEquals(
                "user",
                StringUtil.getUsernameFromEmail("")
        );
        assertEquals(
                "user",
                StringUtil.getUsernameFromEmail(null)
        );
    }


    @Test
    void addSymbolInStart_ShouldStrWithSymbol() {
        assertEquals(
                "@dan",
                StringUtil.addSymbolInStart("dan", "@")
        );
    }

    @Test
    void addSymbolInStart_EmptyStringOrNull() {
        assertEquals(
                "",
                StringUtil.addSymbolInStart("", "")
        );
        assertEquals(
                "",
                StringUtil.addSymbolInStart("dan", "")
        );
        assertNull(StringUtil.addSymbolInStart(null, ""));
        assertNull(StringUtil.addSymbolInStart(null, null));
        assertNull(StringUtil.addSymbolInStart("dan", null));
    }


    @Test
    void getFirstLetter_ShouldGetLetter() {
        assertEquals(
                "d",
                StringUtil.getFirstLetter("dan")
        );
    }

    @Test
    void getFirstLetter_EmptyStringOrNull() {
        assertEquals(
                "",
                StringUtil.getFirstLetter("")
        );
        assertEquals(
                "",
                StringUtil.getFirstLetter(null)
        );
    }


    @Test
    void encodeStrForPath_SimpleText() {
        assertEquals(
                "hello",
                StringUtil.encodeStrForPath("hello")
        );
    }

    @Test
    void encodeStrForPath_TextWithSpace() {
        assertEquals(
                "hello+world",
                StringUtil.encodeStrForPath("hello world")
        );
    }

    @Test
    void encodeStrForPath_EmptyString() {
        assertEquals(
                "",
                StringUtil.encodeStrForPath("")
        );
    }

    @Test
    void encodeStrForPath_NullInput() {
        assertThrows(NullPointerException.class, () -> StringUtil.encodeStrForPath(null));
    }
}
