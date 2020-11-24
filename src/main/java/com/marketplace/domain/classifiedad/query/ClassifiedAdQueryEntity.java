package com.marketplace.domain.classifiedad.query;

import com.marketplace.domain.classifiedad.ClassifiedAdState;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd.PictureDto;
import com.marketplace.domain.classifiedad.command.UpdateClassifiedAd.PriceDto;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassifiedAdQueryEntity {

  private UUID id;
  private UUID ownerId;
  private UUID approver;
  private String title;
  private String text;
  private PriceDto price;
  private List<PictureDto> pictures;
  private ClassifiedAdState state;



}
