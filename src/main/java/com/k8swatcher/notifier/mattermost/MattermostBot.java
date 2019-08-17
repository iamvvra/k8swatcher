package com.k8swatcher.notifier.mattermost;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@ApplicationScoped
@Path("/k8swatcher")
public class MattermostBot {

    @GET
    public String test() {
        return "success";
    }
}