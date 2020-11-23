package com.marketplace.domain.classifiedad.controller;

import com.marketplace.command.Command;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd.PictureDto;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddPicturesToClassifiedAd implements Command {
  private UUID id;
  private List<PictureDto> pictures;

}
