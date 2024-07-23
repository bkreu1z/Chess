package Responses;

import model.GameData;

import java.util.Set;

public record ListResult(Set<GameData> games) {
}
