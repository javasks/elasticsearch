package com.javasks.elastic.resource;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@RestController
@RequestMapping("/elasticSearch/users")
public class UsersController {

    TransportClient client;

    public UsersController() throws UnknownHostException {
        client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));

    }

    @PostMapping("/{id}")
    public String insert(@PathVariable final String id) throws IOException {

        IndexResponse response = client.prepareIndex("employee", "id", id)
                .setSource(jsonBuilder()
                        .startObject()
                        .field("name", "Sandeep")
                        .field("gitRepo", "javasks")
                        .field("teamName", "Development")
                        .endObject()
                )
                .get();
        return response.getResult().toString();
    }


    @GetMapping("/{id}")
    public Map<String, Object> view(@PathVariable final String id) {
        GetResponse getResponse = client.prepareGet("employee", "id", id).get();
        System.out.println(getResponse.getSource());
        return getResponse.getSource();
    }

    @PutMapping("/{id}")
    public String update(@PathVariable final String id) throws IOException, InterruptedException, ExecutionException {

        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index("employee")
                .type("id")
                .id(id)
                .doc(jsonBuilder()
                        .startObject()
                        .field("gender", "male")
                        .endObject());
        UpdateResponse updateResponse = client.update(updateRequest).get();
        System.out.println(updateResponse.status());
        return updateResponse.status().toString();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable final String id) {

        DeleteResponse deleteResponse = client.prepareDelete("employee", "id", id).get();

        System.out.println(deleteResponse.getResult().toString());
        return deleteResponse.getResult().toString();
    }
}
