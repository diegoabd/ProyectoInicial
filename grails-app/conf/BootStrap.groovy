
import redis.clients.jedis.Jedis

class BootStrap {

    def init = { servletContext ->

      /*  def keys = "ITEMS_LIST"

        println("Cargando Base: ")

        Jedis jedis = new Jedis("localhost")
        jedis.connect()

        jedis.set(keys , "MLM496221059," +
                "MLB657379875," +
                "MLB647561964," +
                "MLB647562073," +
                "MLA558565483," +
                "MLM494095780," +
                "MLB647559082," +
                "MLM494095767," +
                "MLM490229985," +
                "MLA556604833," +
                "MLB646885992," +
                "MLM490230484," +
                "MLM496910399," +
                "MLM496910482," +
                "MLA556604926," +
                "MLB658976766," +
                "MLM490230561," +
                "MLB658978360," +
                "MLA554932900")

        String items = jedis.get(keys)
        def listaItems = items.split(",")

        listaItems.each {
            println it
        }
*/

    }
    def destroy = {
    }
}
