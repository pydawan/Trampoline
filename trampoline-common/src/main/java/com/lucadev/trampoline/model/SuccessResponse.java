package com.lucadev.trampoline.model;

import lombok.Getter;
import lombok.ToString;

/**
 * Extension of {@link MessageResponse} to include a status flag to notify of success or failure.
 *
 * @author <a href="mailto:Luca.Camphuisen@hva.nl">Luca Camphuisen</a>
 * @since 21-4-18
 */
@Getter
@ToString
public class SuccessResponse extends MessageResponse {

    private final boolean success;

    /**
     * Construct a {@code SuccessResponse}
     * The message in this case depends on the success.
     * If success is true it will default to "ok" else "error"
     *
     * @param success if the action was completed with success.
     */
    public SuccessResponse(boolean success) {
        super(success ? "ok" : "error");
        this.success = success;
    }

    /**
     * Construct a {@code SuccessResponse}
     *
     * @param success if the action was completed with success.
     * @param message the message related to the success state of this model.
     */
    public SuccessResponse(boolean success, String message) {
        super(message);
        this.success = success;
    }
}
