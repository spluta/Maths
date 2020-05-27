MathsT : MultiOutUGen
{
  *ar { | riseDur=0.1, fallDur=0.1, logExp=0.5, trig=1 |
		if(riseDur.rate != 'audio', {riseDur = K2A.ar(riseDur)});
		if(fallDur.rate != 'audio', {fallDur = K2A.ar(fallDur)});
		if(logExp.rate != 'audio', {logExp = K2A.ar(logExp)});
		if(trig.rate != 'audio', {trig = K2A.ar(trig)});
      ^this.multiNew('audio', riseDur, fallDur, logExp, trig)
  }


  *kr { | in1, in2, in3, in4 |
      ^this.multiNew('control', in1, in2, in3, in4)
  }

  checkInputs {
    if (rate == 'audio', {
      4.do({|i|
        if (inputs.at(i).rate != 'audio', {
          ^(" input at index " + i + "(" + inputs.at(i) +
            ") is not audio rate");
        });
      });
    });
    ^this.checkValidInputs
  }

  init { | ... theInputs |
      inputs = theInputs
      ^this.initOutputs(3, rate)
  }

  name { ^"MathsT" }


  info { ^"Generated with Faust" }
}

