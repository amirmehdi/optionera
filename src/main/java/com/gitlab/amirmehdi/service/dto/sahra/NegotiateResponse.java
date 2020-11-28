package com.gitlab.amirmehdi.service.dto.sahra;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NegotiateResponse {

    @JsonProperty("Url")
    public String url;
    @JsonProperty("ConnectionToken")
    public String connectionToken;
    @JsonProperty("ConnectionId")
    public String connectionId;
    @JsonProperty("KeepAliveTimeout")
    public double keepAliveTimeout;
    @JsonProperty("DisconnectTimeout")
    public double disconnectTimeout;
    @JsonProperty("ConnectionTimeout")
    public double connectionTimeout;
    @JsonProperty("TryWebSockets")
    public boolean tryWebSockets;
    @JsonProperty("ProtocolVersion")
    public String protocolVersion;
    @JsonProperty("TransportConnectTimeout")
    public double transportConnectTimeout;
    @JsonProperty("LongPollDelay")
    public double longPollDelay;}
