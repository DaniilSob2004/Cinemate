package com.example.cinemate.utils;

import lombok.experimental.UtilityClass;
import org.springframework.data.util.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@UtilityClass
public class DiffUtil {

    public static Pair<Set<Integer>, Set<Integer>> calculateDiffIds(List<Integer> current, List<Integer> updated) {
        Set<Integer> currentSet = new HashSet<>(current);
        Set<Integer> updatedSet = new HashSet<>(updated);

        Set<Integer> toAdd = new HashSet<>(updatedSet);
        toAdd.removeAll(currentSet);

        Set<Integer> toRemove = new HashSet<>(currentSet);
        toRemove.removeAll(updatedSet);

        return Pair.of(toAdd, toRemove);
    }
}
