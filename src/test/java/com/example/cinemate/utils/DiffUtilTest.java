package com.example.cinemate.utils;

import org.junit.jupiter.api.Test;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DiffUtilTest {

    @Test
    void calculateDiffIds_ShouldReturnCorrectAddAndRemove() {
        List<Integer> current = List.of(1, 2, 3);
        List<Integer> updated = List.of(2, 3, 4, 5);

        Pair<Set<Integer>, Set<Integer>> diff = DiffUtil.calculateDiffIds(current, updated);

        assertEquals(Set.of(4, 5), diff.getFirst());
        assertEquals(Set.of(1), diff.getSecond());
    }

    @Test
    void calculateDiffIds_ShouldReturnEmptyIfSame() {
        List<Integer> current = List.of(1, 2, 3);
        List<Integer> updated = List.of(1, 2, 3);

        Pair<Set<Integer>, Set<Integer>> diff = DiffUtil.calculateDiffIds(current, updated);

        assertTrue(diff.getFirst().isEmpty());
        assertTrue(diff.getSecond().isEmpty());
    }

    @Test
    void calculateDiffIds_ShouldReturnAllToAddIfCurrentEmpty() {
        List<Integer> current = List.of();
        List<Integer> updated = List.of(10, 20);

        Pair<Set<Integer>, Set<Integer>> diff = DiffUtil.calculateDiffIds(current, updated);

        assertEquals(Set.of(10, 20), diff.getFirst());
        assertTrue(diff.getSecond().isEmpty());
    }

    @Test
    void calculateDiffIds_shouldReturnAllToRemoveIfUpdatedEmpty() {
        List<Integer> current = List.of(5, 6);
        List<Integer> updated = List.of();

        Pair<Set<Integer>, Set<Integer>> diff = DiffUtil.calculateDiffIds(current, updated);

        assertTrue(diff.getFirst().isEmpty());
        assertEquals(Set.of(5, 6), diff.getSecond());
    }

    @Test
    void calculateDiffIds_shouldHandleBothEmptyLists() {
        Pair<Set<Integer>, Set<Integer>> diff = DiffUtil.calculateDiffIds(List.of(), List.of());

        assertTrue(diff.getFirst().isEmpty());
        assertTrue(diff.getSecond().isEmpty());
    }
}