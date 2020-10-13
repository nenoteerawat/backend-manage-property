package com.bayneno.backen_manage_property.enums;

public enum EActionLog {
  CALL_COMPLETE(1),
  CALL_FAILED(2),
  LINE_COMPLETE(3),
  LINE_FAILED(4),
  FOLLOWING(5)
  ;

  int index;

  EActionLog(int index) {
    this.index = index;
  }
}
