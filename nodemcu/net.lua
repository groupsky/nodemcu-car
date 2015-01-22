local net = {
  srv
}
net.__index = net

function net:start(port)
  self.srv=net.createServer(net.TCP, 180)
  self.srv:listen(port, function(conn)
    print("[net] Wifi console connected.")

    function s_output(str)
      if conn~=nil then
        conn:send(str)
      end
    end
    node.output(s_output,0)

    conn:on("receive", function(conn, pl)
      node.input(pl)
      if conn==nil then
        print("[net] conn is nil.")
      end
    end)

    conn:on("disconnection", function(conn)
      node.output(nil)
    end)

  end)

  print("Server running at " .. wifi.sta.getip() .. ":" .. port)
end

function net:stop()
  self.srv:close()
  print("Server stopped")
end

return net
