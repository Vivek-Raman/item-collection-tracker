package com.one27001.tracker.model;

import java.io.Serializable;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Checklist implements Serializable {
  private String checklistID;
  @Singular private Map<String, TrackingInfo> stats;
}
