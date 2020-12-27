package com.gitlab.amirmehdi.service.dto.tadbirrlc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SymbolInfo {
    @JsonProperty("isFuture")
    private boolean isFuture;
    @JsonProperty("nc")
    private String nc;
    @JsonProperty("ltp")
    private long ltp;
    @JsonProperty("cp")
    private long cp;
    @JsonProperty("ht")
    private long ht;
    @JsonProperty("lt")
    private long lt;
    @JsonProperty("st")
    private long st;
    @JsonProperty("gs")
    private long gs;
    @JsonProperty("pcp")
    private long pcp;
    @JsonProperty("gc")
    private String gc;
    @JsonProperty("nst")
    private long nst;
    @JsonProperty("nt")
    private long nt;
    @JsonProperty("tv")
    private long tv;
    @JsonProperty("ltd")
    private String ltd;
    @JsonProperty("mxp")
    private long mxp;
    @JsonProperty("minprod")
    private long minprod;
    @JsonProperty("pv")
    private double pv;
    @JsonProperty("vs")
    private long vs;
    @JsonProperty("hp")
    private long hp;
    @JsonProperty("lp")
    private long lp;
    @JsonProperty("rp")
    private long rp;
    @JsonProperty("bv")
    private long bv;
    @JsonProperty("est")
    private String est;
    @JsonProperty("cp12")
    private long cp12;
    @JsonProperty("ect")
    private String ect;
    @JsonProperty("opts")
    private double opts;
    @JsonProperty("mp")
    private long mp;
    @JsonProperty("mt")
    private String mt;
    @JsonProperty("cpv")
    private long cpv;
    @JsonProperty("cpvp")
    private double cpvp;
    @JsonProperty("lpv")
    private long lpv;
    @JsonProperty("lpvp")
    private double lpvp;
    @JsonProperty("th")
    private long th;
    @JsonProperty("tl")
    private long tl;
    @JsonProperty("iscu")
    private boolean iscu;
    @JsonProperty("isagsp")
    private boolean isagsp;
    @JsonProperty("ic")
    private String ic;
    @JsonProperty("iso")
    private boolean iso;
}
