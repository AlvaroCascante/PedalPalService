package com.quetoquenana.pedalpal.common.domain.model;

import java.util.Map;
import java.util.Set;


/*
Generic reusable state machine helper.

Responsibilities:
- store allowed transitions for a given enum status type
- answer whether a transition is allowed
- return allowed target statuses from a given status

Usage:
- AppointmentTransitions wraps StateMachine<AppointmentStatus>
- ServiceOrderTransitions wraps StateMachine<ServiceOrderStatus>

This class must stay framework-free and reusable in the domain layer.
*/


public class StateMachine<S extends Enum<S>> {

    private final Map<S, Set<S>> transitions;

    public StateMachine(Map<S, Set<S>> transitions) {
        this.transitions = transitions;
    }

    public boolean canTransition(S from, S to) {
        return transitions.getOrDefault(from, Set.of()).contains(to);
    }

    public Set<S> allowedFrom(S from) {
        return transitions.getOrDefault(from, Set.of());
    }
}