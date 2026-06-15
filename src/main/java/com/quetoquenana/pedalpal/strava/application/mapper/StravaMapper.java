package com.quetoquenana.pedalpal.strava.application.mapper;

import com.quetoquenana.pedalpal.strava.application.command.StravaGearCommand;
import com.quetoquenana.pedalpal.strava.application.result.StravaConnectionStatusResult;
import com.quetoquenana.pedalpal.strava.application.result.StravaAthleteBikeResult;
import com.quetoquenana.pedalpal.strava.domain.model.StravaAthleteBikeDetail;
import com.quetoquenana.pedalpal.strava.domain.model.StravaConnection;
import com.quetoquenana.pedalpal.strava.domain.model.StravaConnectionStatus;
import com.quetoquenana.pedalpal.strava.domain.model.StravaAthleteBike;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class StravaMapper {

    public StravaAthleteBike toModel(StravaGearCommand command) {
        return StravaAthleteBike.builder()
                .id(command.id())
                .name(command.name())
                .nickname(command.nickname())
                .distance(BigDecimal.valueOf(command.distance()))
                .primary(true)
                .retired(false)
                .build();
    }

    public StravaAthleteBikeResult toResult(StravaAthleteBikeDetail model) {
        return new StravaAthleteBikeResult(
                model.getId(),
                model.getName(),
                model.getNickname(),
                model.isPrimary(),
                model.isRetired(),
                model.getDistance(),
                model.getConvertedDistance(),
                model.getBrandName(),
                model.getModelName(),
                model.getFrameType(),
                model.getDescription()
        );
    }

    public StravaConnectionStatusResult toResult(StravaConnection connection) {
        if  (connection == null) {
            return new StravaConnectionStatusResult(
                    false,
                    StravaConnectionStatus.DISCONNECTED,
                    null,
                    null
            );
        }
        return new StravaConnectionStatusResult(
                true,
                connection.getStatus(),
                connection.getStravaAthleteId(),
                connection.getScope()
        );
    }
}
