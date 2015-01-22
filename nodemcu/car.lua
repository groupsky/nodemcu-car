local car = {
  enableA,
  enableB,
  in1,
  in2,
  in3,
  in4
}
car.__index = car

function car:setup(enableA, enableB, in1, in2, in3, in4)
  self.enableA = enableA
  self.enableB = enableB
  self.in1 = in1
  self.in2 = in2
  self.in3 = in3
  self.in4 = in4

  gpio.mode(self.enableA, gpio.OUTPUT)
  gpio.mode(self.enableB, gpio.OUTPUT)
  gpio.mode(self.in1, gpio.OUTPUT)
  gpio.mode(self.in2, gpio.OUTPUT)
  gpio.mode(self.in3, gpio.OUTPUT)
  gpio.mode(self.in4, gpio.OUTPUT)

  gpio.write(self.enableA, gpio.LOW)
  gpio.write(self.enableB, gpio.LOW)
  gpio.write(self.in1, gpio.LOW)
  gpio.write(self.in2, gpio.LOW)
  gpio.write(self.in3, gpio.LOW)
  gpio.write(self.in4, gpio.LOW)
end

function car:forward()
  gpio.write(self.enableA, gpio.HIGH)
  gpio.write(self.in1, gpio.HIGH)
  gpio.write(self.in2, gpio.LOW)
end

function car:reverse()
  gpio.write(self.enableA, gpio.HIGH)
  gpio.write(self.in1, gpio.LOW)
  gpio.write(self.in2, gpio.HIGH)
end

function car:stop()
  gpio.write(self.enableA, gpio.HIGH)
  gpio.write(self.in1, gpio.LOW)
  gpio.write(self.in2, gpio.LOW)
end

function car:neutral()
  gpio.write(self.enableA, gpio.LOW)
end

function car:left()
  gpio.write(self.enableB, gpio.HIGH)
  gpio.write(self.in3, gpio.HIGH)
  gpio.write(self.in4, gpio.LOW)
end

function car:right()
  gpio.write(self.enableB, gpio.HIGH)
  gpio.write(self.in3, gpio.LOW)
  gpio.write(self.in4, gpio.HIGH)
end

function car:stright()
  gpio.write(self.enableB, gpio.LOW)
end

return car
