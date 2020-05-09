--[[
自增长+过期时间段,表示在x秒后自动消失
--]]
--key
local incr_key = KEYS[1];
--过期时间
local expire_time = tonumber(ARGV[1]);
local count = redis.pcall('incr', incr_key);
redis.pcall('expire', incr_key, expire_time);
return tonumber(count);