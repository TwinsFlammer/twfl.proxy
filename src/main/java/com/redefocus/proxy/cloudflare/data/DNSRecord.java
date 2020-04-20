package com.redefocus.proxy.cloudflare.data;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;

@RequiredArgsConstructor
@Getter
public class DNSRecord {
    private final String id, type, name, value;

    public String toString() {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", this.id);
        jsonObject.put("type", this.type);
        jsonObject.put("name", this.name);
        jsonObject.put("value", this.value);

        return jsonObject.toString();
    }
}
