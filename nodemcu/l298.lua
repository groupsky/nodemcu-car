local moduleName = ...
local M = {}
_G[moduleName] = M

function pwmon(pin, power)
  if power ~= nil and power < 90 then
    pwm.setup(pin, 120, 0)
    pwm.setduty(pin, power*10+100)
    pwm.start(pin)
  else
    pwmoff(pin)
  end
end

function pwmoff(pin)
  pwm.stop(pin)
  pwm.close(pin)
end

function M.setup(enable, in1, in2)
  gpio.mode(enable, gpio.OUTPUT)
  gpio.mode(in1, gpio.OUTPUT)
  gpio.mode(in2, gpio.OUTPUT)

  gpio.write(enable, gpio.LOW)
  gpio.write(in1, gpio.LOW)
  gpio.write(in2, gpio.LOW)
end

function M.on(enable, in1, in2, power)
  pwmon(enable, power)
  gpio.write(enable, gpio.HIGH)
  gpio.write(in1, gpio.HIGH)
  gpio.write(in2, gpio.LOW)
end

function M.stop(enable, in1, in2)
  pwmoff(enable)
  gpio.write(enable, gpio.HIGH)
  gpio.write(in1, gpio.LOW)
  gpio.write(in2, gpio.LOW)
end

function M.neutral(enable)
  pwmoff(enable)
  gpio.write(enable, gpio.LOW)
end

return M