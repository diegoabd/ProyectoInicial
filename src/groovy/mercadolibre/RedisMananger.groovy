package mercadolibre

import redis.clients.jedis.Jedis

/**
 * Created by dabdala on 18/6/15.
 */
class RedisMananger {
    Jedis jedis
    public String getRedistItems(def idListJedisToConnect, def whereToConnectJedis) {
        initializeJedis(whereToConnectJedis)
        String value = jedis.get(idListJedisToConnect)
        return value
    }

    def initializeJedis(def whereToConnectJedis){
        jedis = new Jedis(whereToConnectJedis)
        jedis.connect()
    }

}
