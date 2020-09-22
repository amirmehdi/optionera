package com.gitlab.amirmehdi.service.dto.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;

/**
 * File Created by mojtabye on 4/24/18.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Instrument {

    private String id;//CIsin
    private String name;//LVal18AFC
    private String description;//LSoc30
    private long totalShares;//ZTitAd
    private long baseVolume;//BaseVol
    private long minOrderCount;//QtitMinSaiOmProd
    private long maxOrderCount;//QtitMaxSaiOmProd
    private String tseId;
    private Sector sector;
    private SubSector subSector;
    private Board board;
    private String marketCode;
    private String marketName;
    private double freeFloatPercentage;

    public String getMarketCode() {
        if (marketCode == null)
            return "";
        return marketCode.replaceAll(" ", "");
    }

    public Instrument setMarketCode(String marketCode) {
        this.marketCode = marketCode;
        return this;
    }

    public String getMarketName() {
        return marketName;
    }

    public Instrument setMarketName(String marketName) {
        this.marketName = marketName;
        return this;
    }

    public Sector getSector() {
        return sector;
    }

    public Instrument setSector(Sector sector) {
        this.sector = sector;
        return this;
    }

    public SubSector getSubSector() {
        return subSector;
    }

    public Instrument setSubSector(SubSector subSector) {
        this.subSector = subSector;
        return this;
    }

    public Board getBoard() {
        return board;
    }

    public Instrument setBoard(Board board) {
        this.board = board;
        return this;
    }

    public double getFreeFloatPercentage() {
        return freeFloatPercentage;
    }

    public Instrument setFreeFloatPercentage(double freeFloatPercentage) {
        this.freeFloatPercentage = freeFloatPercentage;
        return this;
    }

    public String getTseId() {
        return tseId;
    }

    public Instrument setTseId(String tseId) {
        this.tseId = tseId;
        return this;
    }

    public String getId() {
        return id;
    }

    public Instrument setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Instrument setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Instrument setDescription(String description) {
        this.description = description;
        return this;
    }

    public long getTotalShares() {
        return totalShares;
    }

    public Instrument setTotalShares(long totalShares) {
        this.totalShares = totalShares;
        return this;
    }

    public long getBaseVolume() {
        return baseVolume;
    }

    public Instrument setBaseVolume(long baseVolume) {
        this.baseVolume = baseVolume;
        return this;
    }


    public long getMinOrderCount() {
        return minOrderCount;
    }

    public Instrument setMinOrderCount(long minOrderCount) {
        this.minOrderCount = minOrderCount;
        return this;
    }

    public long getMaxOrderCount() {
        return maxOrderCount;
    }

    public Instrument setMaxOrderCount(long maxOrderCount) {
        this.maxOrderCount = maxOrderCount;
        return this;
    }

    @JsonIgnore
    public boolean isBourse() {
        return Arrays.asList(
                "GROUP_31",
                "GROUP_51",
                "GROUP_61",
                "GROUP_91",
                "GROUP_F1",
                "GROUP_F2",
                "GROUP_F3",
                "GROUP_F4",
                "GROUP_G1",
                "GROUP_H1",
                "GROUP_N1",
                "GROUP_N2",
                "GROUP_N4",
                "GROUP_Q1",
                "GROUP_Q2",
                "GROUP_Q3",
                "GROUP_Q4",
                "GROUP_S1",
                "GROUP_1N",
                "GROUP_2N")
                .contains(marketCode);
    }
}
