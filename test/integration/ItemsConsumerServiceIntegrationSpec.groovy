import grails.test.spock.IntegrationSpec
import itemsconsumer2.ItemsConsumerService
import redis.clients.jedis.Jedis

/**
 * Created by dabdala on 1/6/15.
 */

class ItemsConsumerServiceIntegrationSpec extends IntegrationSpec {

    def itemsConsumerService


    void "test obtener datos en json de los items"(){
        given:
        def result1 = "MLM"
        def result2 = "MLB"
        def item1  = "MLM490230572"
        def item2 = "MLB657379875"

        when:

        //llamar metodo que devuelve un json por cada itemid
        def valueReturned = itemsConsumerService.obtenerJson(item1)
        def valueReturned2 = itemsConsumerService.obtenerJson(item2)

        //guardar datos de respuesta en json
        //obtener campos del json
        //guardar datos en lista

        then:
        //debe devolver una lista cargada con los campos que contiene el json
        assert valueReturned.site_id.equals(result1)
        assert valueReturned2.site_id.equals(result2)

    }



    void "test guardar en tabla los datos del json"(){

        given:
        //simular lista con los datos del json
        def mapaItems = [iditem: "MLB657379875",site_id: "MLB", title: "Sela Coqueluxe ( Ideal Para Marcha ) Inox", permalink: "http://produto.mercadolivre.com.br/MLB-657379875-sela-coqueluxe-ideal-para-marcha-inox-_JM", acepta_mp: "true", non_mercado_pago_payment_methods: [[id: "MLMWC", description: "Acordar con el comprador", type: "G"],[id: "MLMMO", description: "Efectivo", type: "G"]]]

        when:
        itemsConsumerService.guardarEnBase(mapaItems)
        def valueReturned = itemsConsumerService.consultarBase(mapaItems.iditem)

        then:
        assert valueReturned.equals("MLB")
    }

    void "test invocar servicio consumer"(){

        given:
        def result = "MLM496221059"
        def a = "a"
        def b = "a"

        when:
        itemsConsumerService.executeProcesMessaje(result)
        then:
        assert a == b
    }

}
