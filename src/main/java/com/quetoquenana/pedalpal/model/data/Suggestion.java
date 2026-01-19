package com.quetoquenana.pedalpal.model.data;

import com.quetoquenana.pedalpal.dto.api.request.CreateSuggestionRequest;
import com.quetoquenana.pedalpal.dto.api.request.UpdateSuggestionRequest;
import com.quetoquenana.pedalpal.dto.api.response.ApiBaseResponseView;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "bike_suggestions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Suggestion extends Auditable {

    // JSON Views
    public static class SuggestionsList extends ApiBaseResponseView.Always {}
    public static class SuggestionsDetail extends Suggestion.SuggestionsList {}

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "bike_id", nullable = false)
    private Bike bike;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "suggestion_type", nullable = false)
    private SystemCode suggestionType;

    @Column(name = "confidence_score", precision = 5, scale = 2)
    private BigDecimal confidenceScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id")
    private ProductPackage productsPackage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "text", nullable = false)
    private String description;

    @Column(name = "suggested_date")
    private LocalDate suggestedDate;

    /**
     * Factory to build a Suggestions entity from a create request and already-resolved related entities.
     * The service layer should resolve Bike, suggestionType (SystemCode), package or product before calling this.
     */
    public static Suggestion createFromRequest(
            CreateSuggestionRequest req,
            Bike bike,
            SystemCode suggestionType,
            ProductPackage productsPackage,
            Product product
    ) {
        return Suggestion.builder()
                .bike(bike)
                .suggestionType(suggestionType)
                .confidenceScore(req.getConfidenceScore())
                .productsPackage(productsPackage)
                .product(product)
                .name(req.getName())
                .description(req.getDescription())
                .suggestedDate(req.getSuggestedDate())
                .build();
    }

    /**
     * Partial update from an UpdateSuggestionRequest. Resolved related entities may be null to leave unchanged.
     */
    public void updateFromRequest(
            UpdateSuggestionRequest req,
            Bike bike,
            SystemCode suggestionType,
            ProductPackage productsPackage,
            Product product
    ) {
        if (bike != null) this.bike = bike;
        if (suggestionType != null) this.suggestionType = suggestionType;
        if (req.getConfidenceScore() != null) this.confidenceScore = req.getConfidenceScore();
        if (productsPackage != null) this.productsPackage = productsPackage;
        if (product != null) this.product = product;
        if (req.getName() != null) this.name = req.getName();
        if (req.getDescription() != null) this.description = req.getDescription();
        if (req.getSuggestedDate() != null) this.suggestedDate = req.getSuggestedDate();
    }
}
