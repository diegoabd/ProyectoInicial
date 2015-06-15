package itemsconsumer2

import grails.test.spock.IntegrationSpec
import redis.clients.jedis.Jedis

class ItemsConsumerServiceSpec extends IntegrationSpec {

  void "test cargar y consultar redis"() {

       given:
            def idService = "ITEMS_LIST"
            //cargar la base redis
            def result = "MLM496221059," +
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
               "MLA554932900"

            //conectar a la base
            def jedis = new Jedis("localhost")
            jedis.connect()
            //setear objeto con datos de la base
            jedis.set(idService, result)
            //simular conexion
            def servicio = [initializeJedis:{true}] as ItemsConsumerService
            servicio.idListJedisToConnect = idService
            //servicio.whereToConnectJedis = "localhost"
            //cargar el objeto jedis del servicio con el objeto jedis (mockeado) del test
            servicio.jedis = jedis

        when:
            //ejecutar metodo que obtiene datos de la base redis
            def valueReturned = servicio.getRedistItems()

        then:
            //debe devolver los datos previamente cargados
            assert valueReturned.equals(result)

    }

    void "test armar lista datos"(){

        given:
        //simular devolucion de datos del metodo serviceMethod
        def result = "MLM496221059," +
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
                "MLA554932900"
        List<String> listaResult  = Arrays.asList("MLM496221059", "MLB657379875", "MLB647561964","MLB647562073","MLA558565483","MLM494095780","MLB647559082","MLM494095767","MLM490229985","MLA556604833","MLB646885992","MLM490230484","MLM496910399","MLM496910482","MLA556604926","MLB658976766","MLM490230561","MLB658978360","MLA554932900")

        def servicio = [serviceMethod:{true}] as ItemsConsumerService
        def delimitador = ","

        when:
        //ejecutar metodo que envia los datos del redis al array para splitearlos por la coma
        def valueReturned = servicio.splitDatos(result, delimitador)

        then:
        //debe devolver los datos previamente cargados
        assert valueReturned.equals(listaResult)
    }
}