-- driver configuration
local carConfig = {
  enableA = 6,
  enableB = 8,
  in1 = 7,
  in2 = 5,
  in3 = 2,
  in4 = 1,
}

-- network configuration
local netConfig = {
  port = 1337
}

car = require "car"
net = require "net"

-- setup car
car:setup(carConfig.enableA, carConfig.enableB, carConfig.in1, carConfig.in2, carConfig.in3, carConfig.in4)

-- wait for wifi connection
tmr.alarm(1, 1000, 1, function()
   if wifi.sta.getip()=="0.0.0.0" or wifi.sta.getip() == nil then
      print("Connect AP, Waiting...")
   else
      net:start(netConfig.port)
      tmr.stop(1)
   end
end)
