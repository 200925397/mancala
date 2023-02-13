package com.fnb.mancala.controller;

import lombok.Value;

@Value
class Payload {
    private Integer index;
    private Boolean isPlayer2Turn;
}
