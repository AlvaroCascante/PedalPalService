package com.quetoquenana.pedalpal.systemCode.domain.model;

import com.quetoquenana.pedalpal.common.domain.model.GeneralStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

/**
 * Domain model for system codes with basic invariants.
 */
@Getter
@Setter
public class SystemCode {
    private UUID id;
    private String category;
    private String code;
    private String label;
    private String description;
    private String codeKey;
    private GeneralStatus status;
    private Integer position;

    /**
     * Creates a SystemCode with validated core fields.
     */
    @Builder
    public SystemCode(UUID id,
                      String category,
                      String code,
                      String label,
                      String description,
                      String codeKey,
                      GeneralStatus status,
                      Integer position) {
        validateNotBlank("category", category);
        validateNotBlank("code", code);
        validateStatus(status);
        this.id = id;
        this.category = category;
        this.code = code;
        this.label = label;
        this.description = description;
        this.codeKey = codeKey;
        this.status = status;
        this.position = position;
    }

    /**
     * Updates the category value with validation.
     */
    public void setCategory(String category) {
        validateNotBlank("category", category);
        this.category = category;
    }

    /**
     * Updates the code value with validation.
     */
    public void setCode(String code) {
        validateNotBlank("code", code);
        this.code = code;
    }

    /**
     * Updates the status value with validation.
     */
    public void setStatus(GeneralStatus status) {
        validateStatus(status);
        this.status = status;
    }

    private static void validateNotBlank(String field, String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
    }

    private static void validateStatus(GeneralStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("status is required");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SystemCode that)) return false;
        return Objects.equals(category, that.category) && Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category, code);
    }
}
