package com.marketplace.domain.classifiedad.controller;

import com.marketplace.command.Command;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddPictureToClassifiedAd implements Command {
    private UUID id;
    private String uri;
    private int height;
    private int width;
    private int order;
}
