jQuery(document).ready(function($){

  var brokerHost = "broker.mqtt-dashboard.com";
  var brokerPort = 8000;
  var topic = "/hackafe-car";

  function makeid()
  {
      var text = "";
      var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

      for( var i=0; i < 10; i++ )
          text += possible.charAt(Math.floor(Math.random() * possible.length));

      return text;
  }

  // Create a client instance
  client = new Messaging.Client(brokerHost, brokerPort, "web-"+makeid());

  // set callback handlers
  client.onConnectionLost = onConnectionLost;
  client.onMessageArrived = onMessageArrived;

  // connect the client
  client.connect({
    timeout: 3,
    keepAliveInterval: 60,
    cleanSession: true,
    useSSL: false,
    onSuccess: onConnect,
    onFailure: function(){
      console.log('fail', arguments);
    }
  });


  // called when the client connects
  function onConnect() {
    // Once a connection has been made, make a subscription and send a message.
    console.log("onConnect");
    client.subscribe(topic);
    message = new Messaging.Message("webClient connected");
    message.destinationName = topic;
    client.send(message);
  }

  // called when the client loses its connection
  function onConnectionLost(responseObject) {
    if (responseObject.errorCode !== 0) {
      console.log("onConnectionLost:"+responseObject.errorMessage);
    }
  }

  // called when a message arrives
  function onMessageArrived(message) {
    console.log("onMessageArrived:"+message.payloadString);
  }

  function send(msg) {
    message = new Messaging.Message(msg);
    message.destinationName = topic;
    client.send(message);
    console.log('sent: '+msg);
  }

  window.Car = {
    left: function(power) {
      if (!power) power = 100;
      send("l"+power);
    },
    right: function(power) {
      if (!power) power = 100;
      send("r"+power);
    },
    center: function() {
      send("c");
    },
    forward: function(power) {
      if (!power) power = 100;
      send("f"+power);
    },
    reverse: function(power) {
      if (!power) power = 100;
      send("b"+power);
    },
    stop: function() {
      send("s");
    },
    neutral: function() {
      send("n");
    },
  }


var log = $('#log')[0],
    pressedKeys = [];

  var olddir='c';
  var olddrive='n';
  var $dir=$('#dir');
  var $drive=$('#dri');
  var pressed = [];

  var timer=false;

  var updateCar = function() {
    var dir, drive;
    if (pressed[37] && !pressed[39])
      dir='l';
    else if (pressed[39] && !pressed[37])
      dir='r';
    else
      dir='c';

    if (pressed[38] && !pressed[40] && !pressed[32])
      drive='f';
    else if (pressed[40] && !pressed[38] && !pressed[32])
      drive='b';
    else if (pressed[32])
      drive='s';
    else
      drive='n';

    $dir.text(dir);
    $drive.text(drive);

    if (olddir != dir) {
      olddir = dir;
      send(dir);
    }
    if (olddrive != drive) {
      clearTimeout(timer);
      olddrive = drive;
      send(drive);
      function f() {
        if (drive == olddrive) {
          drive = 'n';
        } else {
          drive = olddrive;
        }
        send(drive);
        timer = setTimeout(f, 50);
      }
      if (false && drive != 'n')
      timer = setTimeout(f, 50);
    }
  }

  $(document).keydown(function(e) {
    handleAction(e.keyCode, true);
    return absorbEvent_(e);
  });



  $(document.body).keyup(function (e) {
    handleAction(e.keyCode, false);
    return absorbEvent_(e);
  });

    function absorbEvent_(event) {
      var e = event || window.event;
      e.preventDefault && e.preventDefault();
      e.stopPropagation && e.stopPropagation();
      e.cancelBubble = true;
      e.returnValue = false;
      return false;
    }

  var handleAction = function(action, press) {
    console.log('action', action, press);
    pressed[action] = press;
    var li = pressedKeys[key];
    if (!li) {
        li = log.appendChild(document.createElement('li'));
        pressedKeys[key] = li;
    }
    $(li).text((press?'Down':'Up')+': ' + key);
    if (press)
      $(li).removeClass('key-up');
    else
      $(li).addClass('key-up');
    var key = $('.key[data-key='+action+']');
    if (key.length !== 0) {
      if (press)
        key.addClass('down');
      else
        key.removeClass('down');
    }

    updateCar();
  }

  var handleStart = function(evt) {
    console.log('touchstart', evt, this);
    var key = $(this).data('key');
    handleAction(key, true);
    return absorbEvent_(evt);
  }

  var handleEnd = function(evt) {
    console.log('touchend', evt);
    var key = $(this).data('key');
    handleAction(key, false);
    return absorbEvent_(evt);
  }

  var handleMove = function(e) {
    console.log('touch', e.type, e);

    $('.key').each(function() {
      for (var i=0; i<e.changedTouches.length; i++) {
        var touch = e.changedTouches[i];
        var x = touch.clientX,
            y = touch.clientY,
            t = $(this),
            o = t.offset(),
            l = o.left,
            r = l+t.outerWidth(),
            to = o.top,
            b = to+t.outerHeight(),
            inside = x >= l && x <= r && y >= to && y <= b,
            k = t.data('key');
          handleAction(k, inside);
      };
    });

    // return absorbEvent_(e);
  };

  $('.key').each(function(idx, el) {
    console.log('attaching to ', el);
    el.addEventListener("touchstart", handleStart, false);
    el.addEventListener("touchend", handleEnd, false);
    el.addEventListener("touchcancel", handleEnd, false);
    el.addEventListener("touchleave", handleEnd, false);
    el.addEventListener("touchmove", handleMove, false);
  });

/*
    canvas = document.getElementById("mainCanvas");
    canvas.width = document.body.clientWidth; //document.width is obsolete
    canvas.height = document.body.clientHeight; //document.height is obsolete

  var oldx = 0;
  var oldy = 0;
  GameController.init({
    left: {
      touchRadius: Math.min(canvas.width, canvas.height)/10,
      type: 'joystick',
      position: { left: '50%', top: '50%' },
      joystick: {
        radius: canvas.height/2,
        touchEnd: function() {
          console.log('===== touch end');
          Car.neutral();
          Car.center();
        },
        touchMove: function(details) {
          var x = Math.round(details.normalizedX * 100);
          var y = Math.round(details.normalizedY * 100);

          console.log(x, y);

          if (Math.abs(x - oldx) > 5) {
            if (Math.abs(x) < 10) {
              Car.center();
            } else if (x < 0) {
              Car.left(-x);
            } else {
              Car.right(x);
            }
            oldx = x;
          }

          if (Math.abs(y - oldy) > 5) {
            if (Math.abs(y) < 10) {
              Car.neutral();
            } if (y < 0) {
              Car.reverse(-y);
            } else {
              Car.forward(y);
            }
            oldy = y;
          }
        }
      }
    },
    right: false,
  });
  */
});
