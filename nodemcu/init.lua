-- configuration
enableA = 0
enableB = 7
in1 = 12
in2 = 5
in3 = 8
in4 = 2


-- car definition
car = {}
-- setup method
car.setup = function(enableA, enableB, in1, in2, in3, in4)
  gpio.mode(enableA, gpio.OUTPUT)
  gpio.mode(enableB, gpio.OUTPUT)
  gpio.mode(in1, gpio.OUTPUT)
  gpio.mode(in2, gpio.OUTPUT)
  gpio.mode(in3, gpio.OUTPUT)
  gpio.mode(in4, gpio.OUTPUT)

  gpio.write(enableA, gpio.LOW)
  gpio.write(enableB, gpio.LOW)
  gpio.write(in1, gpio.LOW)
  gpio.write(in2, gpio.LOW)
  gpio.write(in3, gpio.LOW)
  gpio.write(in4, gpio.LOW)
end

car.forward = function()
  gpio.write(enableA, gpio.HIGH)
  gpio.write(in1, gpio.HIGH)
  gpio.write(in2, gpio.LOW)
end

car.reverse = function()
  gpio.write(enableA, gpio.HIGH)
  gpio.write(in1, gpio.LOW)
  gpio.write(in2, gpio.HIGH)
end

car.stop = function()
  gpio.write(enableA, gpio.HIGH)
  gpio.write(in1, gpio.LOW)
  gpio.write(in2, gpio.LOW)
end

car.neutral = function()
  gpio.write(enableA, gpio.LOW)
end

car.left = function()
  gpio.write(enableB, gpio.HIGH)
  gpio.write(in3, gpio.HIGH)
  gpio.write(in4, gpio.LOW)
end

car.right = function()
  gpio.write(enableB, gpio.HIGH)
  gpio.write(in3, gpio.LOW)
  gpio.write(in4, gpio.HIGH)
end

car.stright = function()
  gpio.write(enableB, gpio.LOW)
end
