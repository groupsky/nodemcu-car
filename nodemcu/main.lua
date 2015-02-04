ENA = 6
ENB = 8
IN1 = 7
IN2 = 5
IN3 = 2
IN4 = 1

BROKER_IP = "178.62.67.131"
BROKER_PORT = 1883
BROKER_USER = ""
BROKER_PASS = ""
BROKER_CLIENT = "car-" .. wifi.sta.getmac()
BROKER_TOPIC = "/hackafe-car"

driver = require("l298")
driver.setup(ENA, IN1, IN2)
driver.setup(ENB, IN3, IN4)

m = mqtt.Client(BROKER_CLIENT, 120, BROKER_USER, BROKER_PASS)
m:on("connect", function(con) print ("[mqtt] connected") end)
m:on("offline", function(con) print ("[mqtt] offline") car:neutral() car:stright() end)

m:on("message", function(conn, topic, data)
  print("[mqtt] " .. topic .. ":" )
  if data ~= nil then
    print("[mqtt] " .. data)
    local cmd = string.sub(data, 1, 1)
    local power = tonumber(string.sub(data, 2))
    if cmd == "l" then driver.on(ENB, IN3, IN4, power) 
    elseif cmd == "r" then driver.on(ENB, IN4, IN3, power) 
    elseif cmd == "c" then driver.neutral(ENB) 
    elseif cmd == "f" then driver.on(ENA, IN1, IN2, power) 
    elseif cmd == "b" then driver.on(ENA, IN2, IN1, power) 
    elseif cmd == "s" then driver.stop(ENA, IN1, IN2) 
    elseif cmd == "n" then driver.neutral(ENA)
    end
  end
end)

m:connect(BROKER_IP, BROKER_PORT, 0, function(conn)
  print("[mqtt] connected")

  m:subscribe(BROKER_TOPIC, 0, function(conn)
    print("[mqtt] subscribe success")

    m:publish(BROKER_TOPIC,"Car Driver is online!",0,0, function(conn) print("[mqtt] sent") end)
  end)
end)
