package com.example.demo5;

import java.util.UUID;

public record ReasonDTO(UUID reasonId, boolean archived, String reasonName) {
}
