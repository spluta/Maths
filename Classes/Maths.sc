Maths : MultiOutUGen
{
  *ar { | riseDur=0.1, fallDur=0.1, logExp=0.5, loop=1, plugged=0, trig=0 |
		if(riseDur.rate != 'audio', {riseDur = K2A.ar(riseDur)});
		if(fallDur.rate != 'audio', {fallDur = K2A.ar(fallDur)});
		if(logExp.rate != 'audio', {logExp = K2A.ar(logExp)});
		if(loop.rate != 'audio', {loop = K2A.ar(loop)});
		if(plugged.rate != 'audio', {plugged = K2A.ar(plugged)});
		if(trig.rate != 'audio', {trig = K2A.ar(trig)});
      ^this.multiNew('audio', riseDur, fallDur, logExp, loop, plugged, trig)
  }

  *kr { | in1, in2, in3, in4, in5, in6 |
      ^this.multiNew('control', in1, in2, in3, in4, in5, in6)
  }

  checkInputs {
    if (rate == 'audio', {
      6.do({|i|
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

  name { ^"Maths" }


  info { ^"Generated with Faust" }
}

