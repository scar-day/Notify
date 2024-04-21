package me.scarday.notify.social;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@lombok.Builder
public class Builder {
    String message;
    int color;
}
