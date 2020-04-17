package com.redecommunity.proxy.cloudflare.api;

import com.google.common.annotations.Beta;
import com.google.common.collect.Lists;
import com.mongodb.util.JSON;
import com.redecommunity.proxy.cloudflare.data.DNSRecord;
import com.redecommunity.proxy.cloudflare.util.Constants;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

@Beta
public class CloudFlareAPI {
    public static void main(String[] args) {
        DNSRecord dnsRecord = CloudFlareAPI.listDNSRecords()
                .stream()
                .filter(dnsRecord1 -> dnsRecord1.getType().equalsIgnoreCase("SRV"))
                .filter(dnsRecord1 -> {
                    System.out.println(dnsRecord1);

                    return dnsRecord1.getValue().equals("0\t25565\tredefocus.com");
                })
                .findFirst()
                .orElse(null);

        System.out.println(dnsRecord);

        if (dnsRecord == null) {
            if (CloudFlareAPI.createRecord(
                    "SRV",
                    "proxy-1",
                    "redefocus.com",
                    25565
            )) {
                System.out.println("Criado !");
            } else {
                System.out.println("Ocorreu um erro ao criar...");
            }
        }

        if (dnsRecord != null) {
            System.out.println("Deletar");

            if (CloudFlareAPI.deleteRecord(dnsRecord.getId())) {
                System.out.println("Deletado!");
            } else {
                System.out.println("Ocorreu um erro ao deletar...");
            }
        }
    }

    public static List<DNSRecord> listDNSRecords() {
        List<DNSRecord> records = Lists.newArrayList();

        try {
            HttpURLConnection httpURLConnection = CloudFlareAPI.getHttpURLConnection(
                    String.format(
                            Constants.BASE_URL,
                            "zones/" + Constants.ZONE_ID + "/dns_records"
                    ),
                    "GET"
            );

            httpURLConnection.connect();

            InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());

            StringBuilder response = new StringBuilder();

            try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }
            }

            JSONObject jsonObject = (JSONObject) JSONValue.parse(response.toString());

            JSONArray jsonArray = (JSONArray) jsonObject.get("result");

            jsonArray.forEach(object -> {
                JSONObject data = (JSONObject) object;

                DNSRecord dnsRecord = new DNSRecord(
                        (String) data.get("id"),
                        (String) data.get("type"),
                        (String) data.get("zone_name"),
                        (String) data.get("content")
                );

                records.add(dnsRecord);
            });
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return records;
    }

    public static Boolean createRecord(String type, String name, String address) {
        return CloudFlareAPI.createRecord(type, name, address);
    }

    public static Boolean createRecord(String type, String name, String address, Integer port) {
        try {
            HttpURLConnection httpURLConnection = CloudFlareAPI.getHttpURLConnection(
                    String.format(
                            Constants.BASE_URL,
                            "zones/" + Constants.ZONE_ID + "/dns_records"
                    ),
                    "POST"
            );

            httpURLConnection.setDoOutput(true);

            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());

            JSONObject data = new JSONObject();

            data.put("type", type);
            data.put("ttl", 1);

            switch (type) {
                case "A": {
                    data.put("name", name);
                    data.put("content", address);
                    data.put("proxied", true);
                }
                case "SRV": {
                    JSONObject data2 = new JSONObject();

                    data2.put("name", name);
                    data2.put("service", "_minecraft");
                    data2.put("proto", "_tcp");
                    data2.put("priority", 0);
                    data2.put("weight", 0);
                    data2.put("port", port);
                    data2.put("target", address);

                    data.put("data", data2);
                }
            }

            dataOutputStream.writeBytes(data.toString());

            httpURLConnection.connect();

            return httpURLConnection.getResponseCode() == 200;
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    public static Boolean deleteRecord(String id) {
        try {
            HttpURLConnection httpURLConnection = CloudFlareAPI.getHttpURLConnection(
                    String.format(
                            Constants.BASE_URL,
                            "/zones/" + Constants.ZONE_ID + "/dns_records/" + id
                    ),
                    "DELETE"
            );

            httpURLConnection.connect();

            return httpURLConnection.getResponseCode() == 200;
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    private static HttpURLConnection getHttpURLConnection(String urlName, String requestMethod) throws IOException {
        URL url = new URL(urlName);

        URLConnection urlConnection = url.openConnection();
        HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;

        httpURLConnection.setRequestMethod(requestMethod);

        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        httpURLConnection.setRequestProperty("X-Auth-Email", "srgutyerrez@gmail.com");
        httpURLConnection.setRequestProperty("X-Auth-Key", "27aaaba6e8cb517531e38f747ef8e6ecf828a");

        return httpURLConnection;
    }
}
