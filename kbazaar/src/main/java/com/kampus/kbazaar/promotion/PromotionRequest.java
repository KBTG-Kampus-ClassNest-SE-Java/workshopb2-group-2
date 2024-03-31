package com.kampus.kbazaar.promotion;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PromotionRequest {

    private String code;
    private String name;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String discountType;
    private BigDecimal discountAmount;
    private String applicableTo;
    private String productSkus;

    // Constructor
    public PromotionRequest(
            String code,
            String name,
            String description,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String discountType,
            BigDecimal discountAmount,
            String applicableTo,
            String productSkus) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
        this.applicableTo = applicableTo;
        this.productSkus = productSkus;
    }

    // Getters and Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getApplicableTo() {
        return applicableTo;
    }

    public void setApplicableTo(String applicableTo) {
        this.applicableTo = applicableTo;
    }

    public String getProductSkus() {
        return productSkus;
    }

    public void setProductSkus(String productSkus) {
        this.productSkus = productSkus;
    }

    // toString method to represent object as a String
    @Override
    public String toString() {
        return "Request{"
                + "code='"
                + code
                + '\''
                + ", name='"
                + name
                + '\''
                + ", description='"
                + description
                + '\''
                + ", startDate="
                + startDate
                + ", endDate="
                + endDate
                + ", discountType='"
                + discountType
                + '\''
                + ", discountAmount="
                + discountAmount
                + ", applicableTo='"
                + applicableTo
                + '\''
                + ", productSkus='"
                + productSkus
                + '\''
                + '}';
    }
}
