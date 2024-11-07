package com.e106.reco.domain.workspace.entity.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class WorkspaceStateConverter implements Converter<String, WorkspaceState> {

    @Override
    public WorkspaceState convert(String state) {
        for (WorkspaceState workspaceState : WorkspaceState.values()) {
            if (workspaceState.name().equalsIgnoreCase(state)) {
                return workspaceState;
            }
        }
        throw new IllegalArgumentException("Unknown workspace state: " + state);
    }
}
