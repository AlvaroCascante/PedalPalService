package com.quetoquenana.pedalpal.serviceorder.application.command;

import java.util.UUID;

/*
Application command for adding a comment to a ServiceOrder.

Fields:
- serviceOrderId
- comment
- customerVisible
*/
public record AddServiceOrderCommentCommand(
        UUID serviceOrderId,
        String comment,
        Boolean customerVisible
) {
}