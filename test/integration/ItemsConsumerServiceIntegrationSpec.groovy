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

        when:
        //llamar metodo que devuelve un json por cada itemid
        def valueReturned = itemsConsumerService.obtenerJson(item)

        //guardar datos de respuesta en json
        //obtener campos del json
        //guardar datos en lista

        then:
        //debe devolver una lista cargada con los campos que contiene el json
        assert valueReturned.site_id.equals(result)

        where:
        result | item
        "MLM" | "MLM490230572"
        "MLB" | "MLB657379875"

    }



    void "test guardar en tabla los datos del json"(){

        given:

        when:
        itemsConsumerService.guardarEnBase(mapaItems)

        then:
        def result = itemsConsumerService.consultarBase(mapaItems.iditem)
        assert result == site

        where:
        //probar con datos errones para que falle un registro de la tabla hijo y no grabe en padre tampoco
        //probar con datos correctos para que inserte padre y los dos hijos
        //probar con datos errones en padre para que no inserte nada

        mapaItems | site
        [iditem: "MLB657379875",site_id: "MLB", title: "Sela Coqueluxe ( Ideal Para Marcha ) Inox", permalink: "http://produto.mercadolivre.com.br/MLB-657379875-sela-coqueluxe-ideal-para-marcha-inox-_JM", acepta_mp: "true", non_mercado_pago_payment_methods: [[id: "MLMWC", description: "Acordar con el comprador", type: "G"],[id: "MLMMO", description: "Efectivo", type: "G123456789"]]] | "MLB"
        [iditem: "MLB657379876",site_id: "MLB", title: "Sela Coqueluxe ( Ideal Para Marcha ) Inox", permalink: "http://produto.mercadolivre.com.br/MLB-657379875-sela-coqueluxe-ideal-para-marcha-inox-_JM", acepta_mp: "true", non_mercado_pago_payment_methods: [[id: "MLMWC", description: "Acordar con el comprador", type: "G"],[id: "MLMMO", description: "Efectivo", type: "G"]]] | "MLB"
        [iditem: "MLB657379877",site_id: "MLB", title: "Sela Coqueluxe ( Ideal Para Marcha ) Inox", permalink: "http://produto.mercadolivre.com.br/MLB-657379875-sela-coqueluxe-ideal-para-marcha-inox-_JM", acepta_mp: "1234567890123456789012234567890", non_mercado_pago_payment_methods: [[id: "MLMWC", description: "Acordar con el comprador", type: "G"],[id: "MLMMO", description: "Efectivo", type: "G123456789"]]] | "MLB"

    }

    void "test invocar servicio consumer utilizando datos del boostrap"(){

        given:
        def iditems = ""
        def valor = "MLM496221059"
        def site = "MLM"

        when:
        itemsConsumerService.executeProcesMessaje(iditems)

        then:
        def result = itemsConsumerService.consultarBase(valor)
        assert result == site

    }

    void "test invocar servicio consumer con id"(){

        given:

        when:
        //def result = valor// "MLM496221059,MLM490230561"
        itemsConsumerService.executeProcesMessaje(valor)

        then:
        def result = itemsConsumerService.consultarBase(valor)
        assert result == site

        where:
        valor | site
        "MLM496221059" | "MLM"
        "MLB658976766" | "MLB"

    }

}
