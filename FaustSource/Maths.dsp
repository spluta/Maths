declare name        "Maths";
declare version     "1.1";
declare author      "Sam Pluta";
declare license     "BSD";
declare copyright   "(c)Sam Pluta 2020";

import("stdfaust.lib");
import("MathsLib.lib");

Maths(riseDur, fallDur, logExp, onOff, plugged, trigIn) = selectSig : _,_,_
with {
  selectSig = mathsC, mathsT:ro.interleave(3,2):select2(selecter),select2(selecter),select2(selecter);
  freq = 1/(riseDur+fallDur);
  mathsC = MathsC(riseDur, fallDur, logExp, onOff);
  mathsT = MathsT(riseDur, fallDur, logExp, trigger);
  x = mathsC:ba.selectn(3, 1);
  trigger = ba.if(plugged>0, float(float(trigIn>0)+float(trigIn'<=0)==2.0), float(float(x>0)+float(x'<=0)==2.0));
  selecter = ba.if(plugged>0, 1, 1-onOffB);
  onOffB = ba.if(onOff>0, 1, 0);
};

process (riseDur, fallDur, logExp, onOff, plugged, trigIn) = Maths(riseDur, fallDur, logExp, onOff, plugged, trigIn);
