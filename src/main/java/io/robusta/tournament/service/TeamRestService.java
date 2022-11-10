package io.robusta.tournament.service;

import io.robusta.tournament.controller.payload.TeamPayload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TeamRestService {

    private final String teamServiceUrl;

    public TeamRestService(@Value("${team.service.url}") String teamServiceUrl) {
        this.teamServiceUrl = teamServiceUrl;
    }

    public TeamPayload retrieveTeamById(Long id) {
        try {
            return new RestTemplate().getForObject(teamServiceUrl + "teams/" + id, TeamPayload.class);
        } catch (Exception e) {
            return null;
        }
    }
}
