declare name        "MathsT";
declare version     "1.1";
declare author      "Sam Pluta";
declare license     "BSD";
declare copyright   "(c)Sam Pluta 2020";

import("stdfaust.lib");
import("MathsLib.lib");

process (riseDur, fallDur, logExp, trigger) = MathsT(riseDur, fallDur, logExp, trigger);
