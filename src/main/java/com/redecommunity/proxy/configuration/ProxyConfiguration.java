package com.redecommunity.proxy.configuration;

import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import com.redecommunity.proxy.Proxy;
import lombok.Getter;
import org.apache.commons.io.Charsets;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by @SrGutyerrez
 */
public class ProxyConfiguration {
    private File file;

    @Getter
    private Integer id;
    @Getter
    private String name, address;

    public ProxyConfiguration() {
        this.file = new File("_proxy.json");

        try {
            this.createFile();

            this.loadFile();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void createFile() throws IOException {
        if (!this.file.exists()) {
            this.file.createNewFile();

            ByteSource byteSource = new ByteSource() {
                @Override
                public InputStream openStream() {
                    return Proxy.getInstance().getResource("_proxy.json");
                }
            };

            String sourceValue = byteSource.asCharSource(Charsets.UTF_8).read();

            Files.write(sourceValue, this.file, Charsets.UTF_8);
        }
    }

    public void loadFile() throws IOException {
        FileReader fileReader = new FileReader(this.file);

        JSONObject jsonObject = (JSONObject) JSONValue.parse(fileReader);

        this.id = ((Long) jsonObject.get("proxy_id")).intValue();
        this.name = (String) jsonObject.get("proxy_name");
        this.address = (String) jsonObject.get("proxy_address");
    }
}
